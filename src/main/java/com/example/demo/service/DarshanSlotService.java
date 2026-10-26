
package com.example.demo.service;

import com.example.demo.dto.CreateBookingRequest;
import com.example.demo.dto.CreateSlotRequest;
import com.example.demo.entity.Booking;
import com.example.demo.entity.DarshanSlot;
import com.example.demo.entity.IdempotencyRecord;
import com.example.demo.exception.SlotFullException;
import com.example.demo.repository.BookingRepository;
import com.example.demo.repository.DarshanSlotRepository;
import com.example.demo.repository.IdempotencyRecordRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class DarshanSlotService {

    private final DarshanSlotRepository repository;
    private final BookingRepository bookingRepository;
    private final IdempotencyRecordRepository idempotencyRepository;

    public DarshanSlotService(
            DarshanSlotRepository repository,
            BookingRepository bookingRepository,
            IdempotencyRecordRepository idempotencyRepository) {

        this.repository = repository;
        this.bookingRepository = bookingRepository;
        this.idempotencyRepository = idempotencyRepository;
    }

    // Create a darshan slot
    public DarshanSlot createSlot(CreateSlotRequest request) {

        if (request.getEndTime().compareTo(request.getStartTime()) <= 0) {
            throw new IllegalArgumentException(
                    "End time must be after start time");
        }

        DarshanSlot slot = new DarshanSlot();
        slot.setTempleName(request.getTempleName());
        slot.setSlotDate(request.getSlotDate());
        slot.setStartTime(request.getStartTime());
        slot.setEndTime(request.getEndTime());
        slot.setCapacity(request.getCapacity());
        slot.setBookedCount(0);

        return repository.save(slot);
    }

    // P3: Create a booking with idempotency protection
    @Transactional
    public Booking createBooking(
            Long slotId,
            CreateBookingRequest request,
            String idempotencyKey) {

        if (idempotencyKey == null || idempotencyKey.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Idempotency-Key header is required");
        }

        // Check whether this key has already been used
        var existing = idempotencyRepository
                .findByIdempotencyKey(idempotencyKey);

        if (existing.isPresent()) {
            IdempotencyRecord record = existing.get();

            // Reject reuse of the key with different request details
            if (!record.getSlotId().equals(slotId)
                    || !record.getDevoteeName()
                    .equals(request.getDevoteeName())
                    || !record.getPhoneNumber()
                    .equals(request.getPhoneNumber())) {

                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "Idempotency key already used for a different request");
            }

            // Return the original booking for a retry
            return record.getBooking();
        }

        // P2: Lock the slot row to prevent overbooking
        DarshanSlot slot = repository.findByIdForUpdate(slotId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Darshan slot not found"));

        if (slot.getRemainingCapacity() <= 0) {
            throw new SlotFullException("Darshan slot is full");
        }

        // Create the booking
        slot.setBookedCount(slot.getBookedCount() + 1);

        Booking booking = new Booking();
        booking.setDevoteeName(request.getDevoteeName());
        booking.setPhoneNumber(request.getPhoneNumber());
        booking.setSlot(slot);

        Booking savedBooking = bookingRepository.save(booking);

        savedBooking.setReceiptNumber(
                String.format(
                        "TDB-%d-%06d",
                        java.time.Year.now().getValue(),
                        savedBooking.getId()
                )
        );

        savedBooking = bookingRepository.save(savedBooking);

        // Save the idempotency record in the same transaction
        IdempotencyRecord record = new IdempotencyRecord();
        record.setIdempotencyKey(idempotencyKey);
        record.setSlotId(slotId);
        record.setDevoteeName(request.getDevoteeName());
        record.setPhoneNumber(request.getPhoneNumber());
        record.setBooking(savedBooking);

        idempotencyRepository.save(record);

        return savedBooking;
    }

    // Get all darshan slots
    public List<DarshanSlot> getAllSlots() {
        return repository.findAll();
    }

    // Get a darshan slot by ID
    public DarshanSlot getSlotById(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Darshan slot not found"));
    }


    @Transactional
    public Booking cancelBooking(Long bookingId) {

        Booking booking = bookingRepository.findByIdForUpdate(bookingId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Booking not found"));

        if (booking.isCancelled()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Booking is already cancelled");
        }

        DarshanSlot slot = repository.findByIdForUpdate(
                        booking.getSlot().getId())
                .orElseThrow(() ->
                        new IllegalArgumentException("Darshan slot not found"));

        booking.setCancelled(true);
        slot.setBookedCount(slot.getBookedCount() - 1);

        return bookingRepository.save(booking);
    }

}
