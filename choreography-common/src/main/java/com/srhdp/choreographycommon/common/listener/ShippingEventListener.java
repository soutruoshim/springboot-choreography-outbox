package com.srhdp.choreographycommon.common.listener;

import com.srhdp.choreographycommon.common.events.DomainEvent;
import com.srhdp.choreographycommon.common.events.shipping.ShippingEvent;
import com.srhdp.choreographycommon.common.processor.EventProcessor;
import reactor.core.publisher.Mono;

public interface ShippingEventListener extends EventListener<ShippingEvent> {

    /*
        To follow the same pattern as other event processors.
        also for type!
     */

    @Override
    default Mono<Void> listen(ShippingEvent event) {
        return switch (event) {
            case ShippingEvent.ShippingScheduled e -> this.handle(e);
        };
    }

    Mono<Void> handle(ShippingEvent.ShippingScheduled event);

}
