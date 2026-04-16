package com.project.back_end.services;

import java.time.LocalDate;
import java.time.temporal.TemporalAccessor;
import java.util.List;

import org.springframework.data.mongodb.core.aggregation.ArrayOperators.In;
import org.springframework.stereotype.Service;

import com.project.back_end.models.Appointment;
import com.project.back_end.models.Doctor;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.DoctorRepository;

import io.jsonwebtoken.lang.Collections;
import jakarta.transaction.Transactional;

@Service
public class DoctorService {

    // 1. **Add @Service Annotation**:
    // - This class should be annotated with `@Service` to indicate that it is a
    // service layer class.
    // - The `@Service` annotation marks this class as a Spring-managed bean for
    // business logic.
    // - Instruction: Add `@Service` above the class declaration.

    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final TokenService tokenService;

    public DoctorService(DoctorRepository doctorRepository, AppointmentRepository appointmentRepository,
            TokenService tokenService) {
        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
        this.tokenService = tokenService;
    }
    // 2. **Constructor Injection for Dependencies**:
    // - The `DoctorService` class depends on `DoctorRepository`,
    // `AppointmentRepository`, and `TokenService`.
    // - These dependencies should be injected via the constructor for proper
    // dependency management.
    // - Instruction: Ensure constructor injection is used for injecting
    // dependencies into the service.

    // 3. **Add @Transactional Annotation for Methods that Modify or Fetch Database
    // Data**:
    // - Methods like `getDoctorAvailability`, `getDoctors`, `findDoctorByName`,
    // `filterDoctorsBy*` should be annotated with `@Transactional`.
    // - The `@Transactional` annotation ensures that database operations are
    // consistent and wrapped in a single transaction.
    // - Instruction: Add the `@Transactional` annotation above the methods that
    // perform database operations or queries.

    @Transactional
    public List<String> getDoctorAvailability(Long doctorId, LocalDate date) {
        // Implementation to retrieve available time slots for the doctor on the given
        // date
        Doctor doctor = doctorRepository.findById(doctorId).orElse(null);
        if (doctor == null) {
            return Collections.emptyList(); // or throw an exception
        }
        List<Appointment> appointments = appointmentRepository.findByDoctorIdAndAppointmentDate(doctorId, date);
        List<String> bookedTimeList = appointments.stream()
                .map(appointment -> appointment.getAppointmentTime().toLocalTime().toString())
                .toList();
        List<String> availableTimeList = doctor.getAvailableTimes().stream()
                .filter(time -> !bookedTimeList.contains(time))
                .toList();
        return availableTimeList;
    }
    // 4. **getDoctorAvailability Method**:
    // - Retrieves the available time slots for a specific doctor on a particular
    // date and filters out already booked slots.
    // - The method fetches all appointments for the doctor on the given date and
    // calculates the availability by comparing against booked slots.
    // - Instruction: Ensure that the time slots are properly formatted and the
    // available slots are correctly filtered.

    @Transactional
    public Integer saveDoctor(Doctor doctor) {
        try {
            if (doctorRepository.findByEmail(doctor.getEmail()) != null) {
                return -1; // Conflict: Doctor with the same email already exists
            }
            doctorRepository.save(doctor);
            return 1; // Success
        } catch (Exception e) {
            return 0; // Internal error
        }
    }
    // 5. **saveDoctor Method**:
    // - Used to save a new doctor record in the database after checking if a doctor
    // with the same email already exists.
    // - If a doctor with the same email is found, it returns `-1` to indicate
    // conflict; `1` for success, and `0` for internal errors.
    // - Instruction: Ensure that the method correctly handles conflicts and
    // exceptions when saving a doctor.

    @Transactional
    public Integer updateDoctor(Doctor doctor) {
        try {
            if (!doctorRepository.existsById(doctor.getId())) {
                return -1; // Not found: Doctor does not exist
            }
            Doctor existingDoctor = doctorRepository.findByEmail(doctor.getEmail());
            if (existingDoctor != null && !existingDoctor.getId().equals(doctor.getId())) {
                return -1; // Conflict: Another doctor with the same email exists
            }
            doctorRepository.save(doctor);
            return 1; // Success
        } catch (Exception e) {
            return 0; // Internal error
        }
    }
    // 6. **updateDoctor Method**:
    // - Updates an existing doctor's details in the database. If the doctor doesn't
    // exist, it returns `-1`.
    // - Instruction: Make sure that the doctor exists before attempting to save the
    // updated record and handle any errors properly.

    @Transactional
    public List<Doctor> getDoctors() {
        return doctorRepository.findAll();
    }
    // 7. **getDoctors Method**:
    // - Fetches all doctors from the database. It is marked with `@Transactional`
    // to ensure that the collection is properly loaded.
    // - Instruction: Ensure that the collection is eagerly loaded, especially if
    // dealing with lazy-loaded relationships (e.g., available times).


    @Transactional
    public Integer deleteDoctor(Long doctorId) {
        try {
            if (!doctorRepository.existsById(doctorId)) {
                return -1; // Not found: Doctor does not exist
            }
            appointmentRepository.deleteAllByDoctorId(doctorId); // Delete associated appointments
            doctorRepository.deleteById(doctorId); // Delete the doctor
            return 1; // Success
        } catch (Exception e) {
            return 0; // Internal error
        }
    }
    // 8. **deleteDoctor Method**:
    // - Deletes a doctor from the system along with all appointments associated
    // with that doctor.
    // - It first checks if the doctor exists. If not, it returns `-1`; otherwise,
    // it deletes the doctor and their appointments.
    // - Instruction: Ensure the doctor and their appointments are deleted properly,
    // with error handling for internal issues.



    // 9. **validateDoctor Method**:
    // - Validates a doctor's login by checking if the email and password match an
    // existing doctor record.
    // - It generates a token for the doctor if the login is successful, otherwise
    // returns an error message.
    // - Instruction: Make sure to handle invalid login attempts and password
    // mismatches properly with error responses.


    @Transactional
    public List<Doctor> findDoctorByName(String name) {
        List<Doctor> doctors = doctorRepository.findByNameLike(name);
        return doctors;
    }
    // 10. **findDoctorByName Method**:
    // - Finds doctors based on partial name matching and returns the list of
    // doctors with their available times.
    // - This method is annotated with `@Transactional` to ensure that the database
    // query and data retrieval are properly managed within a transaction.
    // - Instruction: Ensure that available times are eagerly loaded for the
    // doctors.

    @Transactional
    public List<Doctor> filterDoctorsByNameSpecilityandTime(String name, String specialty, String amOrPm) {
        List<Doctor> doctors = doctorRepository.findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(name, specialty);
        List<Doctor> filteredDoctors = doctors.stream()
                .filter(doctor -> doctor.getAvailableTimes().stream()
                        .anyMatch(availableTime -> isMatchTimePeriod(availableTime, amOrPm)))
                .toList();
        return filteredDoctors;
    }
    // 11. **filterDoctorsByNameSpecilityandTime Method**:
    // - Filters doctors based on their name, specialty, and availability during a
    // specific time (AM/PM).
    // - The method fetches doctors matching the name and specialty criteria, then
    // filters them based on their availability during the specified time period.
    // - Instruction: Ensure proper filtering based on both the name and specialty
    // as well as the specified time period.

    @Transactional
    public List<Doctor> filterDoctorByTime(String amOrPm) {
        List<Doctor> doctors = doctorRepository.findAll();
        List<Doctor> filteredDoctors = doctors.stream()
                .filter(doctor -> doctor.getAvailableTimes().stream()
                        .anyMatch(availableTime -> isMatchTimePeriod(availableTime, amOrPm)))
                .toList();
        return filteredDoctors;
    }
    // 12. **filterDoctorByTime Method**:
    // - Filters a list of doctors based on whether their available times match the
    // specified time period (AM/PM).
    // - This method processes a list of doctors and their available times to return
    // those that fit the time criteria.
    // - Instruction: Ensure that the time filtering logic correctly handles both AM
    // and PM time slots and edge cases.

    public List<Doctor> filterDoctorByNameAndTime(String name, String amOrPm) {
        List<Doctor> doctors = doctorRepository.findByNameLike(name);
        List<Doctor> filteredDoctors = doctors.stream()
                .filter(doctor -> doctor.getAvailableTimes().stream()
                        .anyMatch(availableTime -> isMatchTimePeriod(availableTime, amOrPm)))
                .toList();
        return filteredDoctors;
    }
    // 13. **filterDoctorByNameAndTime Method**:
    // - Filters doctors based on their name and the specified time period (AM/PM).
    // - Fetches doctors based on partial name matching and filters the results to
    // include only those available during the specified time period.
    // - Instruction: Ensure that the method correctly filters doctors based on the
    // given name and time of day (AM/PM).

    public List<Doctor> filterDoctorByNameAndSpecility(String name, String specialty) {
        List<Doctor> doctors = doctorRepository.findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(name, specialty);
        return doctors;
    }
    // 14. **filterDoctorByNameAndSpecility Method**:
    // - Filters doctors by name and specialty.
    // - It ensures that the resulting list of doctors matches both the name
    // (case-insensitive) and the specified specialty.
    // - Instruction: Ensure that both name and specialty are considered when
    // filtering doctors.

    public List<Doctor> filterDoctorByTimeAndSpecility(String specialty, String amOrPm) {
        List<Doctor> doctors = doctorRepository.findBySpecialtyIgnoreCase(specialty);
        List<Doctor> filteredDoctors = doctors.stream()
                .filter(doctor -> doctor.getAvailableTimes().stream()
                        .anyMatch(availableTime -> isMatchTimePeriod(availableTime, amOrPm)))
                .toList();
        return filteredDoctors;
    }
    // 15. **filterDoctorByTimeAndSpecility Method**:
    // - Filters doctors based on their specialty and availability during a specific
    // time period (AM/PM).
    // - Fetches doctors based on the specified specialty and filters them based on
    // their available time slots for AM/PM.
    // - Instruction: Ensure the time filtering is accurately applied based on the
    // given specialty and time period (AM/PM).

    public List<Doctor> filterDoctorBySpecility(String specialty) {
        List<Doctor> doctors = doctorRepository.findBySpecialtyIgnoreCase(specialty);
        return doctors;
    }
    // 16. **filterDoctorBySpecility Method**:
    // - Filters doctors based on their specialty.
    // - This method fetches all doctors matching the specified specialty and
    // returns them.
    // - Instruction: Make sure the filtering logic works for case-insensitive
    // specialty matching.

    public List<Doctor> filterDoctorsByTime(String amOrPm) {
        List<Doctor> doctors = doctorRepository.findAll();
        List<Doctor> filteredDoctors = doctors.stream()
                .filter(doctor -> doctor.getAvailableTimes().stream()
                        .anyMatch(availableTime -> isMatchTimePeriod(availableTime, amOrPm)))
                .toList();
        return filteredDoctors;
    }
    // 17. **filterDoctorsByTime Method**:
    // - Filters all doctors based on their availability during a specific time
    // period (AM/PM).
    // - The method checks all doctors' available times and returns those available
    // during the specified time period.
    // - Instruction: Ensure proper filtering logic to handle AM/PM time periods.

    private boolean isMatchTimePeriod(String time, String amOrPm) {
        if (amOrPm.equalsIgnoreCase("AM")) {
            return time.toLowerCase().endsWith("am");
        } else if (amOrPm.equalsIgnoreCase("PM")) {
            return time.toLowerCase().endsWith("pm");
        }
        return false;
    }
}
