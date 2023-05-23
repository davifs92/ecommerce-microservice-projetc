package com.davifs92.orderservice.dto;

import com.davifs92.orderservice.entity.OrderLineItems;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {

    private List<OrderLineItemsDto> orderLineItemsListDto;
}
