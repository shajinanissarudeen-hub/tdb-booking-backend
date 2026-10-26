
package com.example.demo.service;

import com.example.demo.dto.CreateSlotRequest;
import com.example.demo.entity.DarshanSlot;
import com.example.demo.repository.DarshanSlotRepository;
import org.springframework.stereotype.Service;

@Service
public class DarshanSlotService {

    private final DarshanSlotRepository repository;

    public DarshanSlotService(DarshanSlotRepository repository) {
        this.repository = repository;
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
}
