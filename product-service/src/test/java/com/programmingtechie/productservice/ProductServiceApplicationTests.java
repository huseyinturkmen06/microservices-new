package com.programmingtechie.productservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.programmingtechie.productservice.dto.ProductRequest;
import com.programmingtechie.productservice.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
//junit test
@AutoConfigureMockMvc
class ProductServiceApplicationTests {



	@Container
	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.2");

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;
	//this converts object to a json or makes inverse ov it

	@Autowired
	private ProductRepository productRepository;

	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry){
		dynamicPropertyRegistry.add("spring.data.mongodb.uri",
				mongoDBContainer::getReplicaSetUrl);
	}
	//we should provide the "spring.data.mongodb.uri" dynamically  while using test intead of using it from app.prop file



	@Test
	void shouldCreateProduct() throws Exception {
		ProductRequest productRequest= getProductRequest();
		String productRequestString=objectMapper.writeValueAsString(productRequest);

		mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
				.contentType(MediaType.APPLICATION_JSON)
				.content(productRequestString)  )
				.andExpect(status().isCreated());
		//status is gonna be created or not
				//comes from mocMVC
		Assertions.assertEquals(1, productRepository.findAll().size());
	}


	private ProductRequest getProductRequest(){
		return ProductRequest.builder()
				.name("IPhone X ").description("Tenth version of IPhone")
				.price(BigDecimal.valueOf(1200))
				.build();
	}

}
