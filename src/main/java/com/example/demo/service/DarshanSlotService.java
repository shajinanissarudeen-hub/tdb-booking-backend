
package com.example.demo.service;

import com.example.demo.dto.CreateBookingRequest;
import com.example.demo.dto.CreateSlotRequest;
import com.example.demo.entity.Booking;
import com.example.demo.entity.DarshanSlot;
import com.example.demo.exception.SlotFullException;
import com.example.demo.repository.BookingRepository;
import com.example.demo.repository.DarshanSlotRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DarshanSlotService {

    private final DarshanSlotRepository repository;
    private final BookingRepository bookingRepository;

    public DarshanSlotService(
            DarshanSlotRepository repository,
            BookingRepository bookingRepository) {
        this.repository = repository;
        this.bookingRepository = bookingRepository;
    }

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

    @Transactional
    public Booking createBooking(
            Long slotId, CreateBookingRequest request) {

        DarshanSlot slot = repository.findByIdForUpdate(slotId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Darshan slot not found"));

        if (slot.getRemainingCapacity() <= 0) {
            throw new SlotFullException("Darshan slot is full");
        }

        slot.setBookedCount(slot.getBookedCount() + 1);

        Booking booking = new Booking();
        booking.setDevoteeName(request.getDevoteeName());
        booking.setPhoneNumber(request.getPhoneNumber());
        booking.setSlot(slot);

        return bookingRepository.save(booking);
    }

    public java.util.List<DarshanSlot> getAllSlots() {
        return repository.findAll();
    }

    public DarshanSlot getSlotById(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Darshan slot not found"));
    }
}
