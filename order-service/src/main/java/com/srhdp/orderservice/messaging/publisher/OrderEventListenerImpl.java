package com.srhdp.orderservice.messaging.publisher;

import com.srhdp.choreographycommon.common.events.order.OrderEvent;
import com.srhdp.choreographycommon.common.publisher.EventPublisher;
import com.srhdp.orderservice.common.dto.PurchaseOrderDto;
import com.srhdp.orderservice.common.service.OrderEventListener;
import com.srhdp.orderservice.messaging.mapper.OrderEventMapper;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.time.Duration;

@RequiredArgsConstructor
public class OrderEventListenerImpl implements OrderEventListener, EventPublisher<OrderEvent> {

    private final Sinks.Many<OrderEvent> sink;
    private final Flux<OrderEvent> flux;

    @Override
    public Flux<OrderEvent> publish() {
        return this.flux;
    }

    @Override
    public void emitOrderCreated(PurchaseOrderDto dto) {
        var event = OrderEventMapper.toOrderCreatedEvent(dto);
        this.sink.emitNext(
                event,
                Sinks.EmitFailureHandler.busyLooping(Duration.ofSeconds(1))
        );
    }

}