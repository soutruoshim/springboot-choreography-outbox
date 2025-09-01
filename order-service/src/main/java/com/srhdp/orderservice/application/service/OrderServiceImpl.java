package com.srhdp.orderservice.application.service;

import com.srhdp.orderservice.application.mapper.EntityDtoMapper;
import com.srhdp.orderservice.application.repository.PurchaseOrderRepository;
import com.srhdp.orderservice.common.dto.OrderCreateRequest;
import com.srhdp.orderservice.common.dto.OrderDetails;
import com.srhdp.orderservice.common.dto.PurchaseOrderDto;
import com.srhdp.orderservice.common.service.OrderEventListener;
import com.srhdp.orderservice.common.service.OrderService;
import com.srhdp.orderservice.common.service.inventory.InventoryComponentFetcher;
import com.srhdp.orderservice.common.service.payment.PaymentComponentFetcher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final PurchaseOrderRepository repository;
    private final OrderEventListener eventListener;
    private final PaymentComponentFetcher paymentComponentFetcher;
    private final InventoryComponentFetcher inventoryComponentFetcher;

    @Override
    public Mono<PurchaseOrderDto> placeOrder(OrderCreateRequest request) {
        var entity = EntityDtoMapper.toPurchaseOrder(request);
        return this.repository.save(entity)
                .map(EntityDtoMapper::toPurchaseOrderDto)
                .flatMap(dto -> this.eventListener.onOrderCreated(dto).thenReturn(dto));
    }

    @Override
    public Flux<PurchaseOrderDto> getAllOrders() {
        return this.repository.findAll()
                .map(EntityDtoMapper::toPurchaseOrderDto);
    }

    @Override
    public Mono<OrderDetails> getOrderDetails(UUID orderId) {
        return this.repository.findById(orderId)
                .map(EntityDtoMapper::toPurchaseOrderDto)
                .flatMap(dto -> this.paymentComponentFetcher.getComponent(orderId)
                        .zipWith(this.inventoryComponentFetcher.getComponent(orderId))
                        .map(t -> EntityDtoMapper.toOrderDetails(dto, t.getT1(), t.getT2()))
                );
    }

}