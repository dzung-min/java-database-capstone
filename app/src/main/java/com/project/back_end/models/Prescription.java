package com.project.back_end.models;

import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Document(collection = "prescriptions")
public class Prescription {

  @Id
  private String id;

  @NotNull(message = "Patient's name cannot be null")
  @Size(min = 3, max = 100, message = "Patient's name must be in between 3 and 100 characters long")
  private String patientName;

  @NotNull(message = "Appointment Id cannot be null")
  private Long appointmentId;

  @NotNull(message = "Medication cannot be null")
  @Size(min = 3, max = 100, message = "Medication must be between 3 and 100 characters long")
  private String medication;

  @NotNull(message = "Dosage cannot be null")
  private String dosage;

  @Size(max = 200, message = "Doctor's note cannot exceed 200 characters long")
  private String doctorNotes;

  public Prescription() {
  }

  public Prescription(
      @NotNull(message = "Patient's name cannot be null") @Size(min = 3, max = 100, message = "Patient's name must be in between 3 and 100 characters long") String patientName,
      @NotNull(message = "Appointment Id cannot be null") Long appointmentId,
      @NotNull(message = "Medication cannot be null") @Size(min = 3, max = 100, message = "Medication must be between 3 and 100 characters long") String medication,
      @NotNull(message = "Dosage cannot be null") String dosage,
      @Size(max = 200, message = "Doctor's note cannot exceed 200 characters long") String doctorNotes) {
    this.patientName = patientName;
    this.appointmentId = appointmentId;
    this.medication = medication;
    this.dosage = dosage;
    this.doctorNotes = doctorNotes;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getPatientName() {
    return patientName;
  }

  public void setPatientName(String patientName) {
    this.patientName = patientName;
  }

  public Long getAppointmentId() {
    return appointmentId;
  }

  public void setAppointmentId(Long appointmentId) {
    this.appointmentId = appointmentId;
  }

  public String getMedication() {
    return medication;
  }

  public void setMedication(String medication) {
    this.medication = medication;
  }

  public String getDosage() {
    return dosage;
  }

  public void setDosage(String dosage) {
    this.dosage = dosage;
  }

  public String getDoctorNotes() {
    return doctorNotes;
  }

  public void setDoctorNotes(String doctorNotes) {
    this.doctorNotes = doctorNotes;
  }
}
