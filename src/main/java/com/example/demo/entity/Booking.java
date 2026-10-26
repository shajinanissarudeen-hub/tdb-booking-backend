
package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String devoteeName;
    private String phoneNumber;

    private boolean cancelled = false;

    @Column(unique = true)
    private String receiptNumber;

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

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public DarshanSlot getSlot() {
        return slot;
    }

    public void setSlot(DarshanSlot slot) {
        this.slot = slot;
    }

    public String getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }
}
