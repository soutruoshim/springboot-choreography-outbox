package com.srhdp.orderservice.application.service;

import com.srhdp.orderservice.application.entity.OrderInventory;
import com.srhdp.orderservice.application.mapper.EntityDtoMapper;
import com.srhdp.orderservice.application.repository.OrderInventoryRepository;
import com.srhdp.orderservice.common.dto.OrderInventoryDto;
import com.srhdp.orderservice.common.service.OrderFulfillmentService;
import com.srhdp.orderservice.common.service.inventory.InventoryComponentFetcher;
import com.srhdp.orderservice.common.service.inventory.InventoryComponentStatusListener;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InventoryComponentService implements InventoryComponentFetcher, InventoryComponentStatusListener {

    private static final OrderInventoryDto DEFAULT = OrderInventoryDto.builder().build();
    private final OrderInventoryRepository repository;
    private final OrderFulfillmentService fulfillmentService;


    @Override
    public Mono<OrderInventoryDto> getComponent(UUID orderId) {
        return this.repository.findByOrderId(orderId)
                .map(EntityDtoMapper::toOrderInventoryDto)
                .defaultIfEmpty(DEFAULT);
    }

    @Override
    public Mono<Void> onSuccess(OrderInventoryDto message) {
        return this.repository.findByOrderId(message.orderId())
                .switchIfEmpty(Mono.defer(() -> this.add(message, true)))
                .then(this.fulfillmentService.complete(message.orderId()));
    }

    @Override
    public Mono<Void> onFailure(OrderInventoryDto message) {
        return this.repository.findByOrderId(message.orderId())
                .switchIfEmpty(Mono.defer(() -> this.add(message, false)))
                .then(this.fulfillmentService.cancel(message.orderId()));
    }

    @Override
    public Mono<Void> onRollback(OrderInventoryDto message) {
        return this.repository.findByOrderId(message.orderId())
                .doOnNext(e -> e.setStatus(message.status()))
                .flatMap(this.repository::save)
                .then();
    }

    private Mono<OrderInventory> add(OrderInventoryDto dto, boolean isSuccess) {
        var entity = EntityDtoMapper.toOrderInventory(dto);
        entity.setSuccess(isSuccess);
        return this.repository.save(entity);
    }

}
