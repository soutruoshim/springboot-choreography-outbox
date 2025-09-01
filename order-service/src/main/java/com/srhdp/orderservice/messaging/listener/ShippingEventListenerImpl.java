package com.srhdp.orderservice.messaging.listener;

import com.srhdp.choreographycommon.common.events.shipping.ShippingEvent;
import com.srhdp.choreographycommon.common.listener.ShippingEventListener;
import com.srhdp.orderservice.common.service.shipping.ShippingComponentStatusListener;
import com.srhdp.orderservice.messaging.mapper.ShippingEventMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ShippingEventListenerImpl implements ShippingEventListener {

    private final ShippingComponentStatusListener statusListener;

    @Override
    public Mono<Void> handle(ShippingEvent.ShippingScheduled event) {
        var dto = ShippingEventMapper.toDto(event);
        return this.statusListener.onSuccess(dto)
                .then(Mono.empty());
    }

}