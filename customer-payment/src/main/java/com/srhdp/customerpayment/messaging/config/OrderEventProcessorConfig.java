package com.srhdp.customerpayment.messaging.config;

import com.srhdp.choreographycommon.common.events.order.OrderEvent;
import com.srhdp.choreographycommon.common.events.payment.PaymentEvent;
import com.srhdp.choreographycommon.common.processor.OrderEventProcessor;
import com.srhdp.choreographycommon.common.util.MessageConverter;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import reactor.core.publisher.Flux;

import java.util.function.Function;

@Configuration
@RequiredArgsConstructor
public class OrderEventProcessorConfig {

    private static final Logger log = LoggerFactory.getLogger(OrderEventProcessorConfig.class);
    private final OrderEventProcessor<PaymentEvent> eventProcessor;

    @Bean
    public Function<Flux<Message<OrderEvent>>, Flux<Message<PaymentEvent>>> processor() {
        return flux -> flux.map(MessageConverter::toRecord)
                .doOnNext(r -> log.info("customer payment received {}", r.message()))
                .concatMap(r -> this.eventProcessor.process(r.message())
                        .doOnSuccess(e -> r.acknowledgement().acknowledge())
                )
                .map(this::toMessage);
    }

    private Message<PaymentEvent> toMessage(PaymentEvent event) {
        return MessageBuilder.withPayload(event)
                .setHeader(KafkaHeaders.KEY, event.orderId().toString())
                .build();
    }

}
