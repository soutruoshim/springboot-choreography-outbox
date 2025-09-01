package com.srhdp.shippingservice.application.service;


import com.srhdp.choreographycommon.common.events.shipping.ShippingStatus;
import com.srhdp.choreographycommon.common.util.DuplicateEventValidator;
import com.srhdp.shippingservice.application.entity.Shipment;
import com.srhdp.shippingservice.application.mapper.EntityDtoMapper;
import com.srhdp.shippingservice.application.repository.ShipmentRepository;
import com.srhdp.shippingservice.common.dto.ScheduleRequest;
import com.srhdp.shippingservice.common.dto.ShipmentDto;
import com.srhdp.shippingservice.common.service.ShippingService;
import com.srhdp.shippingservice.messaging.config.OrderEventProcessorConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class ShippingServiceImpl implements ShippingService {


    private final ShipmentRepository repository;
    private static final Logger log = LoggerFactory.getLogger(ShippingServiceImpl.class);

    @Override
    public Mono<Void> addShipment(ScheduleRequest request) {
        return DuplicateEventValidator.validate(
                this.repository.existsByOrderId(request.orderId()),
                Mono.defer(() -> this.add(request))
        );
    }

    private Mono<Void> add(ScheduleRequest request) {
        var shipment = EntityDtoMapper.toShipment(request);
        shipment.setStatus(ShippingStatus.PENDING);
        return this.repository.save(shipment)
                .then();
    }

    @Override
    public Mono<Void> cancel(UUID orderId) {
        return this.repository.deleteByOrderId(orderId);
    }

    @Override
    public Mono<ShipmentDto> schedule(UUID orderId) {
        return this.repository.findByOrderIdAndStatus(orderId, ShippingStatus.PENDING)
                .flatMap(this::schedule);
    }

    private Mono<ShipmentDto> schedule(Shipment shipment) {
        shipment.setDeliveryDate(Instant.now().plus(Duration.ofDays(3)));
        shipment.setStatus(ShippingStatus.SCHEDULED);
        return this.repository.save(shipment)
                .map(EntityDtoMapper::toDto);
    }

}