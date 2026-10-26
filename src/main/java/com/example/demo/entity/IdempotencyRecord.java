package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(
        name = "idempotency_record",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_idempotency_key",
                columnNames = "idempotency_key"
        )
)
public class IdempotencyRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "idempotency_key", nullable = false, unique = true)
    private String idempotencyKey;

    @Column(nullable = false)
    private Long slotId;

    @Column(nullable = false)
    private String devoteeName;

    @Column(nullable = false)
    private String phoneNumber;

    @OneToOne(optional = false)
    @JoinColumn(name = "booking_id", nullable = false, unique = true)
    private Booking booking;

    public IdempotencyRecord() {
    }

    public Long getId() {
        return id;
    }

    public String getIdempotencyKey() {
        return idempotencyKey;
    }

    public void setIdempotencyKey(String idempotencyKey) {
        this.idempotencyKey = idempotencyKey;
    }

    public Long getSlotId() {
        return slotId;
    }

    public void setSlotId(Long slotId) {
        this.slotId = slotId;
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

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }
}
