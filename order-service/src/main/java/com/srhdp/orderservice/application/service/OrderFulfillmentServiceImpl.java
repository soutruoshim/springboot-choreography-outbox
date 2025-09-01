package com.srhdp.orderservice.application.service;

import com.srhdp.choreographycommon.common.events.order.OrderStatus;
import com.srhdp.orderservice.application.entity.PurchaseOrder;
import com.srhdp.orderservice.application.mapper.EntityDtoMapper;
import com.srhdp.orderservice.application.repository.PurchaseOrderRepository;
import com.srhdp.orderservice.common.dto.PurchaseOrderDto;
import com.srhdp.orderservice.common.service.OrderEventListener;
import com.srhdp.orderservice.common.service.OrderFulfillmentService;
import com.srhdp.orderservice.messaging.config.OrderEventPublisherConfig;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.util.UUID;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class OrderFulfillmentServiceImpl implements OrderFulfillmentService {

    private static final Logger log = LoggerFactory.getLogger(OrderFulfillmentServiceImpl.class);

    private final PurchaseOrderRepository repository;
    private final OrderEventListener orderEventListener;

    @Override
    public Mono<Void> complete(UUID orderId) {
        return this.repository.getWhenOrderComponentsCompleted(orderId)
                .doOnNext(data -> System.out.println("DEBUG >>> " + data))
                .transform(this.updateStatus(OrderStatus.COMPLETED))
                .flatMap(this.orderEventListener::onOrderCompleted);
    }

    @Override
    public Mono<Void> cancel(UUID orderId) {
        return this.repository.findByOrderIdAndStatus(orderId, OrderStatus.PENDING)
                .transform(this.updateStatus(OrderStatus.CANCELLED))
                .flatMap(this.orderEventListener::onOrderCancelled);
    }

    private Function<Mono<PurchaseOrder>, Mono<PurchaseOrderDto>> updateStatus(OrderStatus status) {
        return mono -> mono
                .doOnNext(e -> e.setStatus(status))
                .flatMap(this.repository::save)
                .retryWhen(Retry.max(1).filter(OptimisticLockingFailureException.class::isInstance))
                .map(EntityDtoMapper::toPurchaseOrderDto);
    }

}
