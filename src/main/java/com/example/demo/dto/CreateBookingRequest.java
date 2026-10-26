
package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class CreateBookingRequest {

    @NotBlank(message = "Devotee name is required")
    private String devoteeName;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9]{10}$",
            message = "Phone number must contain 10 digits")
    private String phoneNumber;

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
}
