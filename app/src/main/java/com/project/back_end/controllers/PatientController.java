package com.project.back_end.controllers;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.back_end.DTO.Login;
import com.project.back_end.models.Patient;
import com.project.back_end.services.PatientService;
import com.project.back_end.services.Services;
import com.project.back_end.services.TokenService;

@RestController
@RequestMapping("/patient")
public class PatientController {
    private final PatientService patientService;
    private final Services service;
    private final TokenService tokenService;

    public PatientController(PatientService patientService, Services service, TokenService tokenService) {
        this.patientService = patientService;
        this.service = service;
        this.tokenService = tokenService;
    }

    @GetMapping("{token}")
    public ResponseEntity<Map<String, Object>> getPatient(@PathVariable String token) {
        ResponseEntity<Map<String, String>> validationResponse = service.validateToken(token, "patient");
        if (validationResponse.getStatusCode().isSameCodeAs(HttpStatus.UNAUTHORIZED)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Token is invalid"));
        }
        String patientEmail = tokenService.extractIdentifier(token);
        Patient patient = patientService.getPatientByEmail(patientEmail);
        return ResponseEntity.ok(Map.of("patient", patient));
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> createPatient(@RequestBody Patient patient) {
        if (patientService.getPatientByEmail(patient.getEmail()) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Patient already exists"));
        }
        patientService.createPatient(patient);
        return ResponseEntity.ok(Map.of("message", "Patient created successfully"));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Login loginDto) {
        ResponseEntity<Map<String, String>> validationResponse = service.validatePatientLogin(loginDto);
        if (validationResponse.getStatusCode().isSameCodeAs(HttpStatus.UNAUTHORIZED)) {
            return validationResponse;
        }
        String  token = tokenService.generateToken(loginDto.getIdentifier());
        return ResponseEntity.ok(Map.of("token", token));
    }

    @GetMapping("/{patientId}/{role}/{token}")
    public ResponseEntity<Map<String, Object>> getPatientAppointment(@PathVariable Long patientId, @PathVariable String token, @PathVariable String role) {
        ResponseEntity<Map<String, String>> validationResponse = service.validateToken(token, role);
        if (validationResponse.getStatusCode().isSameCodeAs(HttpStatus.UNAUTHORIZED)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Token is invalid"));
        }
        // Continue with appointment retrieval logic
        return ResponseEntity.ok(Map.of("appointments", patientService.getPatientAppointment(patientId)));
    }

    @GetMapping("/filter/{condition}/{doctorName}/{token}")
    public ResponseEntity<Map<String, Object>> filterPatientAppointment(@PathVariable String condition, @PathVariable String doctorName, @PathVariable String token) {
        ResponseEntity<Map<String, String>> validationResponse = service.validateToken(token, "patient");
        if (validationResponse.getStatusCode().isSameCodeAs(HttpStatus.UNAUTHORIZED)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Token is invalid"));
        }
        // Continue with appointment filtering logic
        return service.filterPatient(condition, doctorName, token);
    }

    // 1. Set Up the Controller Class:
    // - Annotate the class with `@RestController` to define it as a REST API
    // controller for patient-related operations.
    // - Use `@RequestMapping("/patient")` to prefix all endpoints with `/patient`,
    // grouping all patient functionalities under a common route.

    // 2. Autowire Dependencies:
    // - Inject `PatientService` to handle patient-specific logic such as creation,
    // retrieval, and appointments.
    // - Inject the shared `Service` class for tasks like token validation and login
    // authentication.

    // 3. Define the `getPatient` Method:
    // - Handles HTTP GET requests to retrieve patient details using a token.
    // - Validates the token for the `"patient"` role using the shared service.
    // - If the token is valid, returns patient information; otherwise, returns an
    // appropriate error message.

    // 4. Define the `createPatient` Method:
    // - Handles HTTP POST requests for patient registration.
    // - Accepts a validated `Patient` object in the request body.
    // - First checks if the patient already exists using the shared service.
    // - If validation passes, attempts to create the patient and returns success or
    // error messages based on the outcome.

    // 5. Define the `login` Method:
    // - Handles HTTP POST requests for patient login.
    // - Accepts a `Login` DTO containing email/username and password.
    // - Delegates authentication to the `validatePatientLogin` method in the shared
    // service.
    // - Returns a response with a token or an error message depending on login
    // success.

    // 6. Define the `getPatientAppointment` Method:
    // - Handles HTTP GET requests to fetch appointment details for a specific
    // patient.
    // - Requires the patient ID, token, and user role as path variables.
    // - Validates the token using the shared service.
    // - If valid, retrieves the patient's appointment data from `PatientService`;
    // otherwise, returns a validation error.

    // 7. Define the `filterPatientAppointment` Method:
    // - Handles HTTP GET requests to filter a patient's appointments based on
    // specific conditions.
    // - Accepts filtering parameters: `condition`, `name`, and a token.
    // - Token must be valid for a `"patient"` role.
    // - If valid, delegates filtering logic to the shared service and returns the
    // filtered result.

}
