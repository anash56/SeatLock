package com.example.seatlock.event;

import java.time.Instant;

public record EventResponse(
        Long id,
        String title,
        String description,
        String category,
        Instant createdAt
) {
    public static EventResponse from(Event event) {
        return new EventResponse(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getCategory(),
                event.getCreatedAt()
        );
    }
}