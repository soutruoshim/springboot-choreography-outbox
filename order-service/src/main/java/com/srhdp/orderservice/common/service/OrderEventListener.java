package com.srhdp.orderservice.common.service;

import com.srhdp.orderservice.common.dto.PurchaseOrderDto;

public interface OrderEventListener {
    void emitOrderCreated(PurchaseOrderDto dto);
}
