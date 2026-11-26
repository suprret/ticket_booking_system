package com.ticketbooking.demo.repository;

import com.ticketbooking.demo.model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findByEventId(Long eventId);
}