package com.project.back_end.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "Name cannot be null")
    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters long")
    private String name;
    
    @NotNull(message = "Specialty cannot be null")
    @Size(min = 3, max = 50, message = "Specialty must be between 3 and 50 characters long")
    private String specialty;

    @NotNull(message = "Email cannot be null")
    @Email(message = "Must be correct email format")
    private String email;

    // 5. 'password' field:
    // - Type: private String
    // - Description:
    // - Represents the doctor's password for login authentication.
    // - The @NotNull annotation ensures that a password must be provided.
    // - The @Size(min = 6) annotation ensures that the password must be at least 6
    // characters long.
    // - The @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) annotation
    // ensures that the password is not serialized in the response (hidden from the
    // frontend).

    @NotNull(message = "Password cannot be null")
    @Size(min = 6, message = "Password must has at least 6 characters")
    @JsonProperty(access = Access.WRITE_ONLY)
    private String password;

    // - The @Pattern(regexp = "^[0-9]{10}$") annotation validates that the phone
    // number must be exactly 10 digits long.
    @NotNull(message = "Phone must not be null")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number is invalid")
    private String phone;

    // - Represents the available times for the doctor in a list of time slots.
    // - Each time slot is represented as a string (e.g., "09:00-10:00",
    // "10:00-11:00").
    // - The @ElementCollection annotation ensures that the list of time slots is
    // stored as a separate collection in the database.
    @ElementCollection
    private List<String> availableTimes;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone() {
        return phone;
    }

    public String getSpecialty() {
        return specialty;
    }

    public List<String> getAvailableTimes() {
        return availableTimes;
    }

    public void setAvailableTimes(List<String> availableTimes) {
        this.availableTimes = availableTimes;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }
}
