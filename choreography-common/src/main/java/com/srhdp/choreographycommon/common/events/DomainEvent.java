package com.srhdp.choreographycommon.common.events;

import java.time.Instant;

public interface DomainEvent {
    Instant createdAt();
}
