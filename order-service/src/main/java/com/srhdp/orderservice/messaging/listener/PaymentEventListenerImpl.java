package com.srhdp.orderservice.messaging.listener;

import com.srhdp.choreographycommon.common.events.payment.PaymentEvent;
import com.srhdp.choreographycommon.common.listener.PaymentEventListener;
import com.srhdp.orderservice.common.service.payment.PaymentComponentStatusListener;
import com.srhdp.orderservice.messaging.config.OrderEventPublisherConfig;
import com.srhdp.orderservice.messaging.mapper.PaymentEventMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PaymentEventListenerImpl implements PaymentEventListener {

    private final PaymentComponentStatusListener statusListener;
    private static final Logger log = LoggerFactory.getLogger(OrderEventPublisherConfig.class);

    @Override
    public Mono<Void> handle(PaymentEvent.PaymentDeducted event) {
        var dto = PaymentEventMapper.toDto(event);
        log.info("PaymentEventListenerImpl PaymentDeducted ids {}", dto);
        return this.statusListener.onSuccess(dto);
    }

    @Override
    public Mono<Void> handle(PaymentEvent.PaymentDeclined event) {
        var dto = PaymentEventMapper.toDto(event);
        return this.statusListener.onFailure(dto);
    }

    @Override
    public Mono<Void> handle(PaymentEvent.PaymentRefunded event) {
        var dto = PaymentEventMapper.toDto(event);
        return this.statusListener.onRollback(dto);
    }
}
