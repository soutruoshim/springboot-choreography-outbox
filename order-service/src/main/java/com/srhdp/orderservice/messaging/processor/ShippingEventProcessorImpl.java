package com.srhdp.orderservice.messaging.processor;

import com.srhdp.choreographycommon.common.events.order.OrderEvent;
import com.srhdp.choreographycommon.common.events.shipping.ShippingEvent;
import com.srhdp.choreographycommon.common.processor.ShippingEventProcessor;
import com.srhdp.orderservice.common.service.shipping.ShippingComponentStatusListener;
import com.srhdp.orderservice.messaging.mapper.ShippingEventMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ShippingEventProcessorImpl implements ShippingEventProcessor<OrderEvent> {

    private final ShippingComponentStatusListener statusListener;

    @Override
    public Mono<OrderEvent> handle(ShippingEvent.ShippingScheduled event) {
        var dto = ShippingEventMapper.toDto(event);
        return this.statusListener.onSuccess(dto)
                .then(Mono.empty());
    }

}