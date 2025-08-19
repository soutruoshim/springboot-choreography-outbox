package com.srhdp.orderservice.common.dto;

import com.srhdp.choreographycommon.common.events.inventory.InventoryStatus;
import lombok.Builder;

import java.util.UUID;

@Builder
public record OrderInventoryDto(UUID orderId,
                                UUID inventoryId,
                                InventoryStatus status,
                                String message) {
}
