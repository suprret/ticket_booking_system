package com.ticketbooking.demo;

import com.ticketbooking.demo.exception.SeatAlreadyBookedException;
import com.ticketbooking.demo.service.BookingService;
import com.ticketbooking.demo.service.EventService;
import com.ticketbooking.demo.repository.SeatRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class BookingConcurrencyTest {

    @Autowired
    private EventService eventService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private SeatRepository seatRepository;

    @Test
    public void testConcurrentSeatBooking() throws InterruptedException {
        // 1. Create a test event with 1 seat
        var event = eventService.createEventWithSeats(
                "Concurrency Test Concert",
                "Bengaluru Arena",
                LocalDateTime.now().plusDays(5),
                1
        );
        Long seatId = seatRepository.findByEventId(event.getId()).get(0).getId();

        int threadCount = 10;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch readyLatch = new CountDownLatch(threadCount);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(threadCount);

        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failureCount = new AtomicInteger(0);

        // 2. Prepare 10 threads to book the exact same seat simultaneously
        for (int i = 1; i <= threadCount; i++) {
            long userId = 1000L + i;
            executor.submit(() -> {
                try {
                    readyLatch.countDown();
                    startLatch.await(); // All 10 threads wait here until released together
                    bookingService.bookSeat(userId, seatId);
                    successCount.incrementAndGet();
                } catch (SeatAlreadyBookedException e) {
                    failureCount.incrementAndGet();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        readyLatch.await(); // Wait until all 10 threads are ready
        startLatch.countDown(); // Fire all 10 threads at the exact same millisecond
        doneLatch.await(); // Wait for all 10 threads to finish
        executor.shutdown();

        System.out.println("Successful bookings: " + successCount.get());
        System.out.println("Rejected bookings: " + failureCount.get());

        // 3. Prove only 1 user won the seat and 9 were blocked
        assertEquals(1, successCount.get(), "Exactly 1 booking should succeed");
        assertEquals(9, failureCount.get(), "Remaining 9 bookings should fail with conflict");
    }
}