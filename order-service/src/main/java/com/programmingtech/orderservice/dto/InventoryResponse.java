package com.programmingtech.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

//here is a trick
//we can't reach the inventoryresponse in inventory service
//so we should copy the same class in here (order service)
public class InventoryResponse {
    public String skuCode;
    public Boolean isInStock;
}
