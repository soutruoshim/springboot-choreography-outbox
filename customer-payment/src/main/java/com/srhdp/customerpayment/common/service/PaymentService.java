package com.srhdp.customerpayment.common.service;

import com.srhdp.customerpayment.common.dto.PaymentDto;
import com.srhdp.customerpayment.common.dto.PaymentProcessRequest;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface PaymentService {

    Mono<PaymentDto> process(PaymentProcessRequest request);

    Mono<PaymentDto> refund(UUID orderId);

}
