package com.ticketbooking.demo.controller;

import com.ticketbooking.demo.model.Event;
import com.ticketbooking.demo.model.Seat;
import com.ticketbooking.demo.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    public record CreateEventRequest(String name, String venue, LocalDateTime eventTime, int totalSeats) {}

    @PostMapping
    public ResponseEntity<Event> createEvent(@RequestBody CreateEventRequest request) {
        Event event = eventService.createEventWithSeats(
                request.name(),
                request.venue(),
                request.eventTime(),
                request.totalSeats()
        );
        return ResponseEntity.ok(event);
    }

    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents() {
        return ResponseEntity.ok(eventService.getAllEvents());
    }

    @GetMapping("/{eventId}/seats")
    public ResponseEntity<List<Seat>> getSeats(@PathVariable Long eventId) {
        return ResponseEntity.ok(eventService.getSeatsByEvent(eventId));
    }
}
