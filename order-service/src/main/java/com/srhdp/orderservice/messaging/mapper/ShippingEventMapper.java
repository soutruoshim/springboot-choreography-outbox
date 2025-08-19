package com.srhdp.orderservice.messaging.mapper;

import com.srhdp.choreographycommon.common.events.shipping.ShippingEvent;
import com.srhdp.orderservice.common.dto.OrderShipmentSchedule;

public class ShippingEventMapper {

    public static OrderShipmentSchedule toDto(ShippingEvent.ShippingScheduled event) {
        return OrderShipmentSchedule.builder()
                .orderId(event.orderId())
                .deliveryDate(event.expectedDelivery())
                .build();
    }

}
