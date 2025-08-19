package com.srhdp.orderservice.common.dto;

import lombok.Builder;

@Builder
public record OrderCreateRequest(Integer customerId,
                                 Integer productId,
                                 Integer quantity,
                                 Integer unitPrice) {
}
