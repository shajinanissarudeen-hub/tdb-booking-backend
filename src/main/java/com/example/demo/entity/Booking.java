
package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String devoteeName;
    private String phoneNumber;

    @ManyToOne(optional = false)
    @JoinColumn(name = "slot_id", nullable = false)
    private DarshanSlot slot;

    public Booking() {
    }

    public Long getId() {
        return id;
    }

    public String getDevoteeName() {
        return devoteeName;
    }

    public void setDevoteeName(String devoteeName) {
        this.devoteeName = devoteeName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public DarshanSlot getSlot() {
        return slot;
    }

    public void setSlot(DarshanSlot slot) {
        this.slot = slot;
    }
}
