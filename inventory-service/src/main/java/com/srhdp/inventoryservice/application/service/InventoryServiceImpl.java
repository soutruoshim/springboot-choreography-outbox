package com.srhdp.inventoryservice.application.service;

import com.srhdp.choreographycommon.common.events.inventory.InventoryStatus;
import com.srhdp.choreographycommon.common.util.DuplicateEventValidator;
import com.srhdp.inventoryservice.application.entity.OrderInventory;
import com.srhdp.inventoryservice.application.entity.Product;
import com.srhdp.inventoryservice.application.mapper.EntityDtoMapper;
import com.srhdp.inventoryservice.application.repository.InventoryRepository;
import com.srhdp.inventoryservice.application.repository.ProductRepository;
import com.srhdp.inventoryservice.common.dto.InventoryDeductRequest;
import com.srhdp.inventoryservice.common.dto.OrderInventoryDto;
import com.srhdp.inventoryservice.common.exception.OutOfStockException;
import com.srhdp.inventoryservice.common.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private static final Logger log = LoggerFactory.getLogger(InventoryServiceImpl.class);
    private static final Mono<Product> OUT_OF_STOCK = Mono.error(new OutOfStockException());
    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;

    @Override
    public Mono<OrderInventoryDto> deduct(InventoryDeductRequest request) {
        return DuplicateEventValidator.validate(
                        this.inventoryRepository.existsByOrderId(request.orderId()),
                        this.productRepository.findById(request.productId())
                )
                .filter(p -> p.getAvailableQuantity() >= request.quantity())
                .switchIfEmpty(OUT_OF_STOCK)
                .flatMap(p -> this.deductInventory(p, request))
                .doOnNext(dto -> log.info("inventory deducted for {}", dto.orderId()));
    }

    private Mono<OrderInventoryDto> deductInventory(Product product, InventoryDeductRequest request) {
        var orderInventory = EntityDtoMapper.toOrderInventory(request);
        product.setAvailableQuantity(product.getAvailableQuantity() - request.quantity());
        orderInventory.setStatus(InventoryStatus.DEDUCTED);
        return this.productRepository.save(product)
                .then(this.inventoryRepository.save(orderInventory))
                .map(EntityDtoMapper::toDto);
    }

    @Override
    public Mono<OrderInventoryDto> restore(UUID orderId) {
        return this.inventoryRepository.findByOrderIdAndStatus(orderId, InventoryStatus.DEDUCTED)
                .zipWhen(i -> this.productRepository.findById(i.getProductId()))
                .flatMap(t -> this.restore(t.getT1(), t.getT2()))
                .doOnNext(dto -> log.info("restored quantity {} for {}", dto.quantity(), dto.orderId()));
    }

    private Mono<OrderInventoryDto> restore(OrderInventory orderInventory, Product product) {
        product.setAvailableQuantity(product.getAvailableQuantity() + orderInventory.getQuantity());
        orderInventory.setStatus(InventoryStatus.RESTORED);
        return this.productRepository.save(product)
                .then(this.inventoryRepository.save(orderInventory))
                .map(EntityDtoMapper::toDto);
    }

}
