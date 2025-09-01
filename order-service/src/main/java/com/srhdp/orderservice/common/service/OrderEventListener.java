package com.srhdp.orderservice.common.service;

import com.srhdp.orderservice.common.dto.PurchaseOrderDto;
import reactor.core.publisher.Mono;
public interface OrderEventListener {
    Mono<Void> onOrderCreated(PurchaseOrderDto dto);

    Mono<Void> onOrderCancelled(PurchaseOrderDto dto);

    Mono<Void> onOrderCompleted(PurchaseOrderDto dto);
}
