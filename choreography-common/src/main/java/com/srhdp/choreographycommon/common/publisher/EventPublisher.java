package com.srhdp.choreographycommon.common.publisher;

import com.srhdp.choreographycommon.common.events.DomainEvent;
import reactor.core.publisher.Flux;

public interface EventPublisher<T extends DomainEvent> {

    Flux<T> publish();

}
