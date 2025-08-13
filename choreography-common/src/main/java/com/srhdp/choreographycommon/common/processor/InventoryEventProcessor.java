package com.srhdp.choreographycommon.common.processor;

import com.srhdp.choreographycommon.common.events.DomainEvent;
import com.srhdp.choreographycommon.common.events.inventory.InventoryEvent;
import reactor.core.publisher.Mono;

public interface InventoryEventProcessor<R extends DomainEvent> extends EventProcessor<InventoryEvent, R> {

    @Override
    default Mono<R> process(InventoryEvent event) {
        return switch (event) {
            case InventoryEvent.InventoryDeducted e -> this.handle(e);
            case InventoryEvent.InventoryDeclined e -> this.handle(e);
            case InventoryEvent.InventoryRestored e -> this.handle(e);
        };
    }

    Mono<R> handle(InventoryEvent.InventoryDeducted event);

    Mono<R> handle(InventoryEvent.InventoryDeclined event);

    Mono<R> handle(InventoryEvent.InventoryRestored event);

}
