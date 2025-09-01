package com.srhdp.choreographycommon.common.publisher;

import com.srhdp.choreographycommon.common.events.DomainEvent;
import com.srhdp.choreographycommon.common.outbox.Outbox;
import reactor.core.publisher.Flux;

public interface EventPublisher<T extends DomainEvent> {

    Flux<Outbox<T>> publish();

}
