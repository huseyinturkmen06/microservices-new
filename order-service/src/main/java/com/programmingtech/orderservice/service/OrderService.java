package com.programmingtech.orderservice.service;


import com.programmingtech.orderservice.dto.InventoryResponse;
import com.programmingtech.orderservice.dto.OrderLineItemsDto;
import com.programmingtech.orderservice.dto.OrderRequest;
import com.programmingtech.orderservice.model.Order;
import com.programmingtech.orderservice.model.OrderLineItems;
import com.programmingtech.orderservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
//@RequiredArgsConstructor
//this is the faster way of constructor injection
public class OrderService {


    private final OrderRepository orderRepository;
    private final WebClient webClient;

    @Autowired
    public OrderService(OrderRepository orderRepository, WebClient webClient) {
        this.orderRepository = orderRepository;
        this.webClient = webClient;
    }




    public void placeOrder(OrderRequest orderRequest){
        Order order=new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList()
                .stream()
                //.map(orderLineItemsDto -> mapToDto(orderLineItemsDto))
                //it could be generated with lambda instead of method reference like above
                .map(this::mapToDto)
                .toList();
                //there was a intruducing operation in here

        order.setOrderLineItemsList(orderLineItems);

        List<String> skuCodes=order.getOrderLineItemsList().stream()
                .map(orderLineItem ->orderLineItem.getSkuCode()).toList();

        // cal inventory service and check if out product is in stock or not
        //here is the beginning of the microservices usage

        //we are making a request to a get method id inventory service
        //whic method returns a boolean value
        InventoryResponse[] inventoryResponseArray= webClient.get()
                    .uri("http://localhost:8082/api/inventory",
                            uriBuilder -> uriBuilder.queryParam("skuCode",skuCodes).build())
                    .retrieve()
                    .bodyToMono(InventoryResponse[].class)
                    .block();
        //web client makes asyncronious request but we want it to make syncronios
        //and providinf it by writing block

        boolean allProductsIsInStock= Arrays.stream(inventoryResponseArray).
                allMatch(inventoryResponse -> inventoryResponse.isInStock);


        //we are checking the all products in the order before creating the order always
        //and we are checking multi products att the same time by using list instead of using one item
        if(allProductsIsInStock){
            orderRepository.save(order);
        }
        else{
            new IllegalArgumentException("Product isn't in stock, please try again later");
        }


    }
    //convert each orderLineItemsDto to OrderLineItems


    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems= new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItems.getSkuCode());
        return  orderLineItems;
    }


}
