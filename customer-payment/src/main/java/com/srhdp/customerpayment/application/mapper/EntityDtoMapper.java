package com.srhdp.customerpayment.application.mapper;

import com.srhdp.customerpayment.application.entity.CustomerPayment;
import com.srhdp.customerpayment.common.dto.PaymentDto;
import com.srhdp.customerpayment.common.dto.PaymentProcessRequest;

public class EntityDtoMapper {

    public static CustomerPayment toCustomerPayment(PaymentProcessRequest request) {
        return CustomerPayment.builder()
                .customerId(request.customerId())
                .orderId(request.orderId())
                .amount(request.amount())
                .build();
    }

    public static PaymentDto toDto(CustomerPayment payment) {
        return PaymentDto.builder()
                .amount(payment.getAmount())
                .status(payment.getStatus())
                .paymentId(payment.getPaymentId())
                .customerId(payment.getCustomerId())
                .orderId(payment.getOrderId())
                .build();
    }
}
