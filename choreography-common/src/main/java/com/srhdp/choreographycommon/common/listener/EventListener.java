package com.srhdp.choreographycommon.common.listener;

import com.srhdp.choreographycommon.common.events.DomainEvent;
import reactor.core.publisher.Mono;

public interface EventListener<T extends DomainEvent> {

    Mono<Void> listen(T event);

}
