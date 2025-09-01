package com.srhdp.orderservice.messaging.publisher;

import com.srhdp.choreographycommon.common.events.order.OrderEvent;
import com.srhdp.choreographycommon.common.publisher.EventPublisher;
import reactor.core.publisher.Mono;

import java.util.List;

public interface OrderEventOutboxService extends EventPublisher<OrderEvent> {

    Mono<Void> deleteEvents(List<Long> ids);

}