package com.srhdp.customerpayment.application.entity;


import com.srhdp.choreographycommon.common.events.payment.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerPayment {

    @Id
    private UUID paymentId;
    private UUID orderId;
    private Integer customerId;
    private PaymentStatus status;
    private Integer amount;

}