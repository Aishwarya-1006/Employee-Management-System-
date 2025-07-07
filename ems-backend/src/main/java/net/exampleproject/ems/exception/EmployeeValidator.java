
package net.exampleproject.ems.exception;

import net.exampleproject.ems.dto.EmployeeDto;
import net.exampleproject.ems.repository.EmployeeRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class EmployeeValidator extends RuntimeException{

    public static void validate(EmployeeDto dto, EmployeeRepo repo, boolean isUpdate, Long employeeId) {
        List<String> errors = new ArrayList<>();

        //  Null / empty checks
        if (dto.getEmpname() == null || dto.getEmpname().trim().isEmpty()) {
            errors.add("Employee name cannot be empty.");
        }

        if (dto.getEmail() == null || dto.getEmail().trim().isEmpty()) {
            errors.add("Email cannot be empty.");
        }

        if (dto.getPhno() == null ) {
            errors.add("Phone number cannot be empty.");
        }

        if (dto.getPassword() == null || dto.getPassword().trim().isEmpty()) {
            errors.add("Password cannot be empty.");
        }

        // If required fields are missing, return early
        if (!errors.isEmpty()) {
            throw new BadRequestException(errors);
        }

        // 1. Duplicate email check
        repo.findByEmail(dto.getEmail()).ifPresent(existing -> {
            if (!isUpdate || !existing.getEmpid().equals(employeeId)) {
                errors.add("Email already exists.");
            }
        });

        // 2. Email format check (Only Gmail)
        String emailRegex = "^[A-Za-z0-9+_.-]+@gmail\\.com$";
        if (!Pattern.matches(emailRegex, dto.getEmail())) {
            errors.add("Invalid email format. Only Gmail addresses are allowed.");
        }

        //  3. Phone number format check
        String phoneRegex = "\\d{10}";
        if (!Pattern.matches(phoneRegex, String.valueOf(dto.getPhno()))) {
            errors.add("Phone number must be exactly 10 digits.");
        }

        //  4. Password strength check
        String password = dto.getPassword();
        if (password.length() < 6) {
            errors.add("Password must be at least 6 characters.");
        } else {
            if (!password.matches(".*[A-Za-z].*")) {
                errors.add("Password must contain at least one letter.");
            }
            if (!password.matches(".*\\d.*")) {
                errors.add("Password must contain at least one digit.");
            }
        }

        // Throw errors if any
        if (!errors.isEmpty()) {
            throw new BadRequestException(errors);
        }
    }
}
