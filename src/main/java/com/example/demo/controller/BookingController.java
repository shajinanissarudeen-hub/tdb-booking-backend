
package com.example.demo.controller;

import com.example.demo.dto.CreateBookingRequest;
import com.example.demo.entity.Booking;
import com.example.demo.service.DarshanSlotService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final DarshanSlotService service;

    public BookingController(DarshanSlotService service) {
        this.service = service;
    }

    @PostMapping("/{slotId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Booking createBooking(
            @PathVariable Long slotId,
            @RequestHeader("Idempotency-Key") String idempotencyKey,
            @Valid @RequestBody CreateBookingRequest request) {

        return service.createBooking(slotId, request, idempotencyKey);
    }


    @DeleteMapping("/{bookingId}")
    public Booking cancelBooking(@PathVariable Long bookingId) {
        return service.cancelBooking(bookingId);
    }

}
