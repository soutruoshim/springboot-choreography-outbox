package com.srhdp.customerpayment.common.dto;

import com.srhdp.choreographycommon.common.events.payment.PaymentStatus;
import lombok.Builder;

import java.util.UUID;

@Builder
public record PaymentDto(UUID paymentId,
                         UUID orderId,
                         Integer customerId,
                         Integer amount,
                         PaymentStatus status) {
}
