package com.ticketbooking.demo.service;

import com.ticketbooking.demo.model.Booking;
import com.ticketbooking.demo.model.Seat;
import com.ticketbooking.demo.repository.BookingRepository;
import com.ticketbooking.demo.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final SeatRepository seatRepository;

    @Transactional
    public Booking bookSeat(Long userId, Long seatId) {
        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new RuntimeException("Seat not found"));

        if (seat.getStatus() == Seat.SeatStatus.BOOKED) {
            throw new RuntimeException("Seat " + seat.getSeatNumber() + " is already booked");
        }

        seat.setStatus(Seat.SeatStatus.BOOKED);
        seatRepository.save(seat);

        Booking booking = new Booking();
        booking.setUserId(userId);
        booking.setSeat(seat);
        booking.setBookedAt(LocalDateTime.now());
        booking.setStatus(Booking.BookingStatus.CONFIRMED);

        return bookingRepository.save(booking);
    }

    @Transactional
    public Booking cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (booking.getStatus() == Booking.BookingStatus.CANCELLED) {
            throw new RuntimeException("Booking is already cancelled");
        }

        booking.setStatus(Booking.BookingStatus.CANCELLED);

        Seat seat = booking.getSeat();
        seat.setStatus(Seat.SeatStatus.AVAILABLE);
        seatRepository.save(seat);

        return bookingRepository.save(booking) ;
    }

    public List<Booking> getUserBookings(Long userId) {
        return bookingRepository.findByUserId(userId);
    }
}
