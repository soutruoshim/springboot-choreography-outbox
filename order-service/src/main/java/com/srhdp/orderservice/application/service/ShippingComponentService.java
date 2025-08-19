package com.srhdp.orderservice.application.service;

import com.srhdp.choreographycommon.common.events.order.OrderStatus;
import com.srhdp.orderservice.application.repository.PurchaseOrderRepository;
import com.srhdp.orderservice.common.dto.OrderShipmentSchedule;
import com.srhdp.orderservice.common.service.shipping.ShippingComponentStatusListener;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ShippingComponentService implements ShippingComponentStatusListener {

    private final PurchaseOrderRepository repository;

    @Override
    public Mono<Void> onSuccess(OrderShipmentSchedule message) {
        return this.repository.findByOrderIdAndStatus(message.orderId(), OrderStatus.COMPLETED)
                .doOnNext(e -> e.setDeliveryDate(message.deliveryDate()))
                .flatMap(this.repository::save)
                .then();
    }

    @Override
    public Mono<Void> onFailure(OrderShipmentSchedule message) {
        return Mono.empty();
    }

    @Override
    public Mono<Void> onRollback(OrderShipmentSchedule message) {
        return Mono.empty();
    }

}