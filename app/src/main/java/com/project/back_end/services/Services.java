package com.project.back_end.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.project.back_end.DTO.Login;
import com.project.back_end.models.Admin;
import com.project.back_end.models.Appointment;
import com.project.back_end.models.Doctor;
import com.project.back_end.models.Patient;
import com.project.back_end.repo.AdminRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;

@Service
public class Services {

    private final TokenService tokenService;
    private final AdminRepository adminRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final DoctorService doctorService;
    private final PatientService patientService;

    public Services(TokenService tokenService, AdminRepository adminRepository,
                   DoctorRepository doctorRepository, PatientRepository patientRepository,
                   DoctorService doctorService, PatientService patientService) {
        this.tokenService = tokenService;
        this.adminRepository = adminRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.doctorService = doctorService;
        this.patientService = patientService;
    }



    // 1. **@Service Annotation**
    // The @Service annotation marks this class as a service component in Spring.
    // This allows Spring to automatically detect it through component scanning
    // and manage its lifecycle, enabling it to be injected into controllers or
    // other services using @Autowired or constructor injection.

    // 2. **Constructor Injection for Dependencies**
    // The constructor injects all required dependencies (TokenService,
    // Repositories, and other Services). This approach promotes loose coupling,
    // improves testability,
    // and ensures that all required dependencies are provided at object creation
    // time.

    public ResponseEntity<Map<String, String>> validateToken(String token, String user) {
        if (tokenService.validateToken(token, user)) {
            return ResponseEntity.ok(Map.of("message", "Token is valid"));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Token is invalid"));
        }
    }
    // 3. **validateToken Method**
    // This method checks if the provided JWT token is valid for a specific user. It
    // uses the TokenService to perform the validation.
    // If the token is invalid or expired, it returns a 401 Unauthorized response
    // with an appropriate error message. This ensures security by preventing
    // unauthorized access to protected resources.

    public ResponseEntity<Map<String, String>> validateAdmin(Admin receivedAdmin) {
        try {
            Admin admin = adminRepository.findByUsername(receivedAdmin.getUsername()).orElse(null);
            if (admin != null) {
                if (admin.getPassword().equals(receivedAdmin.getPassword())) {
                    String token = tokenService.generateToken(admin.getUsername());
                    return ResponseEntity.ok(Map.of("token", token));
                } else {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Addmin credential is incorrect"));
                }
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Addmin credential is incorrect"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "An error occurred: " + e.getMessage()));
        }
    }
    // 4. **validateAdmin Method**
    // This method validates the login credentials for an admin user.
    // - It first searches the admin repository using the provided username.
    // - If an admin is found, it checks if the password matches.
    // - If the password is correct, it generates and returns a JWT token (using the
    // admin’s username) with a 200 OK status.
    // - If the password is incorrect, it returns a 401 Unauthorized status with an
    // error message.
    // - If no admin is found, it also returns a 401 Unauthorized.
    // - If any unexpected error occurs during the process, a 500 Internal Server
    // Error response is returned.
    // This method ensures that only valid admin users can access secured parts of
    // the system.

    public ResponseEntity<Map<String, Object>> filterDoctor(String name, String specialty, String date) {
        try {
            List<Doctor> doctors = doctorService.getDoctors();
            if (name != null) {
                doctors = doctors.stream().filter(doctor -> doctor.getName().toLowerCase().contains(name.toLowerCase())).toList();
            }
            if (specialty != null) {
                doctors = doctors.stream().filter(doctor -> doctor.getSpecialty().toLowerCase().contains(specialty.toLowerCase())).toList();
            }
            if (date != null) {
                doctors = doctors.stream().filter(doctor -> doctor.getAvailableTimes().stream().anyMatch(time -> time.contains(date))).toList();
            }
            return ResponseEntity.ok(Map.of("doctors", doctors));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "An error occurred: " + e.getMessage()));
        }
    }
    // 5. **filterDoctor Method**
    // This method provides filtering functionality for doctors based on name,
    // specialty, and available time slots.
    // - It supports various combinations of the three filters.
    // - If none of the filters are provided, it returns all available doctors.
    // This flexible filtering mechanism allows the frontend or consumers of the API
    // to search and narrow down doctors based on user criteria.

    public Integer validateAppointment(Appointment appointment) {
        Doctor doctor = doctorRepository.findById(appointment.getDoctor().getId()).orElse(null);
        if (doctor != null) {
            List<String> availableTimes = doctor.getAvailableTimes();
            for (String slot : availableTimes) {
                if (slot.equals(appointment.getAppointmentTime().toString())) {
                    return 1; // Valid appointment time
                }
            }
            return 0; // Invalid appointment time
        }
        return -1; // Doctor not found
    }
    // 6. **validateAppointment Method**
    // This method validates if the requested appointment time for a doctor is
    // available.
    // - It first checks if the doctor exists in the repository.
    // - Then, it retrieves the list of available time slots for the doctor on the
    // specified date.
    // - It compares the requested appointment time with the start times of these
    // slots.
    // - If a match is found, it returns 1 (valid appointment time).
    // - If no matching time slot is found, it returns 0 (invalid).
    // - If the doctor doesn’t exist, it returns -1.
    // This logic prevents overlapping or invalid appointment bookings.

    public boolean validatePatient(Patient patient) {
        if (patientRepository.findByEmail(patient.getEmail()) != null) {
            return false; // Email already exists
        }
        if (patientRepository.(patient.getPhone()) != null) {
            return false; // Phone number already exists
        }
        return true;
    }

    // 7. **validatePatient Method**
    // This method checks whether a patient with the same email or phone number
    // already exists in the system.
    // - If a match is found, it returns false (indicating the patient is not valid
    // for new registration).
    // - If no match is found, it returns true.
    // This helps enforce uniqueness constraints on patient records and prevent
    // duplicate entries.

    public ResponseEntity<Map<String, String>> validatePatientLogin(Login login) {
        Patient patient = patientRepository.findByEmail(login.getIdentifier());
        if (patient != null && patient.getPassword().equals(login.getPassword())) {
            return ResponseEntity.ok(Map.of("message", "Login successful"));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid credentials"));
    }

    // 8. **validatePatientLogin Method**
    // This method handles login validation for patient users.
    // - It looks up the patient by email.
    // - If found, it checks whether the provided password matches the stored one.
    // - On successful validation, it generates a JWT token and returns it with a
    // 200 OK status.
    // - If the password is incorrect or the patient doesn't exist, it returns a 401
    // Unauthorized with a relevant error.
    // - If an exception occurs, it returns a 500 Internal Server Error.
    // This method ensures only legitimate patients can log in and access their data
    // securely.

    public ResponseEntity<Map<String, Object>> filterPatient(String condition, String doctorName, String token) {
        String email = tokenService.extractIdentifier(token);
        if (email != null) {
            Patient patient = patientRepository.findByEmail(email);
            if (patient == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Patient not found"));
            }
            return patientService.filterByDoctorAndCondition(condition, doctorName, patient.getId());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid token"));
        }
    }
    // 9. **filterPatient Method**
    // This method filters a patient's appointment history based on condition and
    // doctor name.
    // - It extracts the email from the JWT token to identify the patient.
    // - Depending on which filters (condition, doctor name) are provided, it
    // delegates the filtering logic to PatientService.
    // - If no filters are provided, it retrieves all appointments for the patient.
    // This flexible method supports patient-specific querying and enhances user
    // experience on the client side.

}
