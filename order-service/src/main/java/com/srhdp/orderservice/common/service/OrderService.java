package com.srhdp.orderservice.common.service;

import com.srhdp.orderservice.common.dto.OrderCreateRequest;
import com.srhdp.orderservice.common.dto.OrderDetails;
import com.srhdp.orderservice.common.dto.PurchaseOrderDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface OrderService {
    Mono<PurchaseOrderDto> placeOrder(OrderCreateRequest request);

    Flux<PurchaseOrderDto> getAllOrders();

    Mono<OrderDetails> getOrderDetails(UUID orderId);
}
