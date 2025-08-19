package com.srhdp.orderservice.messaging.processor;

import com.srhdp.choreographycommon.common.events.order.OrderEvent;
import com.srhdp.choreographycommon.common.events.payment.PaymentEvent;
import com.srhdp.choreographycommon.common.processor.PaymentEventProcessor;
import com.srhdp.orderservice.common.service.OrderFulfillmentService;
import com.srhdp.orderservice.common.service.payment.PaymentComponentStatusListener;
import com.srhdp.orderservice.messaging.mapper.OrderEventMapper;
import com.srhdp.orderservice.messaging.mapper.PaymentEventMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PaymentEventProcessorImpl implements PaymentEventProcessor<OrderEvent> {

    private final OrderFulfillmentService fulfillmentService;
    private final PaymentComponentStatusListener statusListener;

    @Override
    public Mono<OrderEvent> handle(PaymentEvent.PaymentDeducted event) {
        var dto = PaymentEventMapper.toDto(event);
        return this.statusListener.onSuccess(dto)
                .then(this.fulfillmentService.complete(event.orderId()))
                .map(OrderEventMapper::toOrderCompletedEvent);
    }

    @Override
    public Mono<OrderEvent> handle(PaymentEvent.PaymentDeclined event) {
        var dto = PaymentEventMapper.toDto(event);
        return this.statusListener.onFailure(dto)
                .then(this.fulfillmentService.cancel(event.orderId()))
                .map(OrderEventMapper::toOrderCancelledEvent);
    }

    @Override
    public Mono<OrderEvent> handle(PaymentEvent.PaymentRefunded event) {
        var dto = PaymentEventMapper.toDto(event);
        return this.statusListener.onRollback(dto)
                .then(Mono.empty());
    }
}
