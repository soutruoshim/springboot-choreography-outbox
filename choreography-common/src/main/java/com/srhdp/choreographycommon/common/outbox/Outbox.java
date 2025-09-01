package com.srhdp.choreographycommon.common.outbox;

import com.srhdp.choreographycommon.common.events.DomainEvent;
import lombok.Builder;

@Builder
public record Outbox<T extends DomainEvent>(Long correlationId,
                                            T event) {
}
