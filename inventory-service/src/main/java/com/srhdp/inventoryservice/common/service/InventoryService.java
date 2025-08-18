package com.srhdp.inventoryservice.common.service;

import com.srhdp.inventoryservice.common.dto.InventoryDeductRequest;
import com.srhdp.inventoryservice.common.dto.OrderInventoryDto;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface InventoryService {
    Mono<OrderInventoryDto> deduct(InventoryDeductRequest request);
    Mono<OrderInventoryDto> restore(UUID orderId);
}
