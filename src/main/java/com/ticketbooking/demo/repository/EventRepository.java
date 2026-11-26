package com.ticketbooking.demo.repository;

import com.ticketbooking.demo.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
}