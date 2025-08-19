package com.srhdp.orderservice.common.service;

import java.util.UUID;
import reactor.core.publisher.Mono;

public interface OrderComponentFetcher<T> {
    Mono<T> getComponent(UUID orderId);
}
