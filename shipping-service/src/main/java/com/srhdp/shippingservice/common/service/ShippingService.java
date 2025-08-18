package com.srhdp.shippingservice.common.service;

import com.srhdp.shippingservice.common.dto.ScheduleRequest;
import com.srhdp.shippingservice.common.dto.ShipmentDto;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ShippingService {

    Mono<Void> addShipment(ScheduleRequest request);

    Mono<Void> cancel(UUID orderId);

    Mono<ShipmentDto> schedule(UUID orderId);

}
