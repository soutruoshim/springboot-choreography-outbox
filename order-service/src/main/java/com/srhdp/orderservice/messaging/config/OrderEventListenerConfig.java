package com.srhdp.orderservice.messaging.config;

import com.srhdp.choreographycommon.common.events.order.OrderEvent;
import com.srhdp.orderservice.common.service.OrderEventListener;
import com.srhdp.orderservice.messaging.publisher.OrderEventListenerImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Sinks;

@Configuration
public class OrderEventListenerConfig {

    @Bean
    public OrderEventListener orderEventListener(){
        var sink = Sinks.many().unicast().<OrderEvent>onBackpressureBuffer();
        var flux = sink.asFlux();
        return new OrderEventListenerImpl(sink, flux);
    }

}