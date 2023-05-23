package com.davifs92.orderservice.service;

import com.davifs92.orderservice.dto.InventoryResponse;
import com.davifs92.orderservice.dto.OrderLineItemsDto;
import com.davifs92.orderservice.dto.OrderRequest;
import com.davifs92.orderservice.entity.Order;
import com.davifs92.orderservice.entity.OrderLineItems;
import com.davifs92.orderservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private WebClient.Builder webClientBuilder;

    public void placeOrder(OrderRequest orderRequest){
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

       List<OrderLineItems> orderLineItemsList = orderRequest.getOrderLineItemsListDto()
                .stream()
                .map(orderLineItemsDto -> mapToDto(orderLineItemsDto))
                .toList();
       order.setOrderLineItemsList(orderLineItemsList);
       List<String> skuCodes = order.getOrderLineItemsList()
        .stream()
               .map(OrderLineItems::getSkuCode).toList();

       InventoryResponse[] inventoryResponses = webClientBuilder.build().get()
               .uri("http://inventory-service/api/inventory", uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                       .retrieve()
                        .bodyToMono(InventoryResponse[].class)
                        .block();

       boolean allProductsInStock = Arrays.stream(inventoryResponses).allMatch(InventoryResponse::isInStock);

       if(allProductsInStock) {
           orderRepository.save(order);
       }
       else {
           throw new IllegalArgumentException("Product is not in stock, please try another product");
       }

    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItems;
    }
}
