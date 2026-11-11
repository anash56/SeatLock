package com.example.seatlock.event;

import com.example.seatlock.common.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Transactional
    public EventResponse create(CreateEventRequest request) {
        Event event = new Event();
        event.setTitle(request.title().trim());
        event.setDescription(request.description());
        event.setCategory(request.category());

        Event saved = eventRepository.save(event);
        return EventResponse.from(saved);
    }

    @Transactional(readOnly = true)
    public List<EventResponse> getAll() {
        return eventRepository.findAll()
                .stream()
                .map(EventResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public EventResponse getById(Long id) {
        Event event = findOrThrow(id);
        return EventResponse.from(event);
    }

    @Transactional
    public EventResponse update(Long id, UpdateEventRequest request) {
        Event event = findOrThrow(id);
        event.setTitle(request.title().trim());
        event.setDescription(request.description());
        event.setCategory(request.category());
        // No save() call needed: see "dirty checking" below
        return EventResponse.from(event);
    }

    @Transactional
    public void delete(Long id) {
        Event event = findOrThrow(id);
        eventRepository.delete(event);
    }

    private Event findOrThrow(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id " + id));
    }
}