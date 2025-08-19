package com.srhdp.orderservice.messaging.processor;

import com.srhdp.choreographycommon.common.events.inventory.InventoryEvent;
import com.srhdp.choreographycommon.common.events.order.OrderEvent;
import com.srhdp.choreographycommon.common.processor.InventoryEventProcessor;
import com.srhdp.orderservice.common.service.OrderFulfillmentService;
import com.srhdp.orderservice.common.service.inventory.InventoryComponentStatusListener;
import com.srhdp.orderservice.messaging.mapper.InventoryEventMapper;
import com.srhdp.orderservice.messaging.mapper.OrderEventMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class InventoryEventProcessorImpl implements InventoryEventProcessor<OrderEvent> {

    private final OrderFulfillmentService fulfillmentService;
    private final InventoryComponentStatusListener statusListener;

    @Override
    public Mono<OrderEvent> handle(InventoryEvent.InventoryDeducted event) {
        var dto = InventoryEventMapper.toDto(event);
        return this.statusListener.onSuccess(dto)
                .then(this.fulfillmentService.complete(event.orderId()))
                .map(OrderEventMapper::toOrderCompletedEvent);
    }

    @Override
    public Mono<OrderEvent> handle(InventoryEvent.InventoryDeclined event) {
        var dto = InventoryEventMapper.toDto(event);
        return this.statusListener.onFailure(dto)
                .then(this.fulfillmentService.cancel(event.orderId()))
                .map(OrderEventMapper::toOrderCancelledEvent);
    }

    @Override
    public Mono<OrderEvent> handle(InventoryEvent.InventoryRestored event) {
        var dto = InventoryEventMapper.toDto(event);
        return this.statusListener.onRollback(dto)
                .then(Mono.empty());
    }

}
