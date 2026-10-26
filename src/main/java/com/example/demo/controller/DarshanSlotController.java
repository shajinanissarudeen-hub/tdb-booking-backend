
package com.example.demo.controller;

import com.example.demo.dto.CreateSlotRequest;
import com.example.demo.entity.DarshanSlot;
import com.example.demo.service.DarshanSlotService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/slots")
public class DarshanSlotController {

    private final DarshanSlotService service;

    public DarshanSlotController(DarshanSlotService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DarshanSlot createSlot(
            @Valid @RequestBody CreateSlotRequest request) {
        return service.createSlot(request);
    }

    @GetMapping
    public List<DarshanSlot> getAllSlots() {
        return service.getAllSlots();
    }

    @GetMapping("/{id}")
    public DarshanSlot getSlotById(@PathVariable Long id) {
        return service.getSlotById(id);
    }
}
