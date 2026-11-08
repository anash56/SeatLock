package com.example.seatlock.event;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {

    private final EventRepository eventRepository;

    // Constructor injection: Spring passes the repository in
    public EventController(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @PostMapping
    public Event create(@RequestBody Event event) {
        return eventRepository.save(event);
    }

    @GetMapping
    public List<Event> getAll() {
        return eventRepository.findAll();
    }
}