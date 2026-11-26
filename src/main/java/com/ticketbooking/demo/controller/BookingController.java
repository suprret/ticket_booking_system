package com.ticketbooking.demo.controller;

import com.ticketbooking.demo.model.Booking;
import com.ticketbooking.demo.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    public record BookSeatRequest(Long userId, Long seatId) {}

    @PostMapping
    public ResponseEntity<Booking> bookSeat(@RequestBody BookSeatRequest request) {
        return ResponseEntity.ok(bookingService.bookSeat(request.userId(), request.seatId()));
    }

    @PostMapping("/{bookingId}/cancel")
    public ResponseEntity<Booking> cancelBooking(@PathVariable Long bookingId) {
        return ResponseEntity.ok(bookingService.cancelBooking(bookingId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Booking>> getUserBookings(@PathVariable Long userId) {
        return ResponseEntity.ok(bookingService.getUserBookings(userId));
    }
}
