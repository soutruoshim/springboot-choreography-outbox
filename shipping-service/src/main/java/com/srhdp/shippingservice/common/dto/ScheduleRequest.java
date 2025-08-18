package com.srhdp.shippingservice.common.dto;

import java.util.UUID;

public record ScheduleRequest(UUID orderId,
                              Integer productId,
                              Integer customerId,
                              Integer quantity) {
}
