package com.ticketbooking.demo.service;

import com.ticketbooking.demo.model.Event;
import com.ticketbooking.demo.model.Seat;
import com.ticketbooking.demo.repository.EventRepository;
import com.ticketbooking.demo.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final SeatRepository seatRepository;

    @Transactional
    public Event createEventWithSeats(String name, String venue, LocalDateTime eventTime, int totalSeats) {
        Event event = new Event();
        event.setName(name);
        event.setVenue(venue);
        event.setEventTime(eventTime);
        Event savedEvent = eventRepository.save(event);

        List<Seat> seats = new ArrayList<>();
        for (int i = 1; i <= totalSeats; i++) {
            Seat seat = new Seat();
            seat.setEvent(savedEvent);
            seat.setSeatNumber("A" + i);
            seat.setStatus(Seat.SeatStatus.AVAILABLE);
            seats.add(seat);
        }
        seatRepository.saveAll(seats);

        return savedEvent;
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public List<Seat> getSeatsByEvent(Long eventId) {
        return seatRepository.findByEventId(eventId);
    }
}