package com.srhdp.choreographycommon.common.listener;

import com.srhdp.choreographycommon.common.events.inventory.InventoryEvent;
import reactor.core.publisher.Mono;

public interface InventoryEventListener extends EventListener<InventoryEvent> {

    @Override
    default Mono<Void> listen(InventoryEvent event) {
        return switch (event) {
            case InventoryEvent.InventoryDeducted e -> this.handle(e);
            case InventoryEvent.InventoryDeclined e -> this.handle(e);
            case InventoryEvent.InventoryRestored e -> this.handle(e);
        };
    }

    Mono<Void> handle(InventoryEvent.InventoryDeducted event);

    Mono<Void> handle(InventoryEvent.InventoryDeclined event);

    Mono<Void> handle(InventoryEvent.InventoryRestored event);

}
