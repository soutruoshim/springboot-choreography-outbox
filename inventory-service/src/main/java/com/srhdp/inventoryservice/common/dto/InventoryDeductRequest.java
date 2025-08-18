package com.srhdp.inventoryservice.common.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record InventoryDeductRequest(UUID orderId,
                                     Integer productId,
                                     Integer quantity) {
}
