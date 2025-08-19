package com.srhdp.orderservice.common.service.payment;

import com.srhdp.orderservice.common.dto.OrderPaymentDto;
import com.srhdp.orderservice.common.service.OrderComponentStatusListener;

public interface PaymentComponentStatusListener extends OrderComponentStatusListener<OrderPaymentDto> {
}
