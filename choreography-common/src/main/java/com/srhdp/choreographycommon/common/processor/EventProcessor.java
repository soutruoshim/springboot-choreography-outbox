package com.srhdp.choreographycommon.common.processor;

import com.srhdp.choreographycommon.common.events.DomainEvent;
import reactor.core.publisher.Mono;

public interface EventProcessor<T extends DomainEvent, R extends DomainEvent> {
    Mono<R> process(T event);
}
