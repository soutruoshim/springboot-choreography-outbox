package com.srhdp.orderservice.messaging.mapper;

import com.srhdp.choreographycommon.common.events.payment.PaymentEvent;
import com.srhdp.choreographycommon.common.events.payment.PaymentStatus;
import com.srhdp.orderservice.common.dto.OrderPaymentDto;

public class PaymentEventMapper {

    public static OrderPaymentDto toDto(PaymentEvent.PaymentDeducted event) {
        return OrderPaymentDto.builder()
                .orderId(event.orderId())
                .paymentId(event.paymentId())
                .status(PaymentStatus.DEDUCTED)
                .build();
    }

    public static OrderPaymentDto toDto(PaymentEvent.PaymentDeclined event) {
        return OrderPaymentDto.builder()
                .orderId(event.orderId())
                .status(PaymentStatus.DECLINED)
                .message(event.message())
                .build();
    }

    public static OrderPaymentDto toDto(PaymentEvent.PaymentRefunded event) {
        return OrderPaymentDto.builder()
                .orderId(event.orderId())
                .status(PaymentStatus.REFUNDED)
                .build();
    }

}