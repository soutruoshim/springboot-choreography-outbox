package com.srhdp.orderservice.common.dto;

import com.srhdp.choreographycommon.common.events.payment.PaymentStatus;
import lombok.Builder;

import java.util.UUID;

@Builder
public record OrderPaymentDto(UUID orderId,
                              UUID paymentId,
                              PaymentStatus status,
                              String message) {
}
