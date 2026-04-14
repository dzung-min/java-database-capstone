package com.project.back_end.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

@Entity
public class Appointment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @NotNull(message = "Doctor cannot be null")
  private Doctor doctor;

  @ManyToOne
  @NotNull(message = "Patient cannot be null")
  private Patient patient;

  @Future(message = "Appointment time must be in the future")
  private LocalDateTime appointmentTime;

  // - Represents the current status of the appointment. It is an integer where:
  // - 0 means the appointment is scheduled.
  // - 1 means the appointment has been completed.
  @NotNull(message = "Status cannot be bull")
  private int status;

  // - It calculates the end time of the appointment by adding one hour to the
  // start time (appointmentTime).
  // - It is used to get an estimated appointment end time for display purposes.
  public LocalDateTime getEndTime() {
    return appointmentTime.plusHours(1);
  }

  // - This method extracts only the date part from the appointmentTime field.
  public LocalDate getAppointmentDate() {
    return LocalDate.from(appointmentTime);
  }

  // - This method extracts only the time part from the appointmentTime field.
  public LocalTime getAppointmentTimeOnly() {
    return LocalTime.from(appointmentTime);
  }

  public Appointment() {
  }

  public Long getId() {
    return id;
  }

  public LocalDateTime getAppointmentTime() {
    return appointmentTime;
  }

  public Doctor getDoctor() {
    return doctor;
  }

  public Patient getPatient() {
    return patient;
  }

  public int getStatus() {
    return status;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setAppointmentTime(LocalDateTime appointmentTime) {
    this.appointmentTime = appointmentTime;
  }

  public void setDoctor(Doctor doctor) {
    this.doctor = doctor;
  }

  public void setPatient(Patient patient) {
    this.patient = patient;
  }

  public void setStatus(int status) {
    this.status = status;
  }
}
