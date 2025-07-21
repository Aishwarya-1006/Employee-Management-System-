package net.exampleproject.ems.service.impl;

import net.exampleproject.ems.dto.EmployeeDto;
import net.exampleproject.ems.entity.Certificate;
import net.exampleproject.ems.entity.Department;
import net.exampleproject.ems.entity.Employee;
import net.exampleproject.ems.exception.EmployeeValidator;
import net.exampleproject.ems.exception.ResourceNotFoundException;
import net.exampleproject.ems.mapper.EmployeeMapper;
import net.exampleproject.ems.repository.CertificateRepo;
import net.exampleproject.ems.repository.DepartmentRepo;
import net.exampleproject.ems.repository.EmployeeRepo;
import net.exampleproject.ems.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepo employeeRepository;
    private final DepartmentRepo departmentRepo;
    private final CertificateRepo certificateRepo;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepo employeeRepository, DepartmentRepo departmentRepo, CertificateRepo certificateRepo) {
        this.employeeRepository = employeeRepository;
        this.departmentRepo = departmentRepo;
        this.certificateRepo = certificateRepo;
    }

    @Override
    public EmployeeDto createEmployee(EmployeeDto employeeDto) {
        EmployeeValidator.validate(employeeDto, employeeRepository, false, null);

        // Get or create Department
        Department dept = departmentRepo.findByDeptnameIgnoreCase(employeeDto.getDeptname())
                .orElseGet(() -> {
                    Department newDept = new Department();
                    newDept.setDeptname(employeeDto.getDeptname());
                    return departmentRepo.save(newDept);
                });

        // Certificate handling: Use existing certificate by name or create new one
        List<Certificate> certificates = employeeDto.getCertificates().stream()
                .map(certDto -> certificateRepo.findByCnameIgnoreCase(certDto.getCname())
                        .orElseGet(() -> {
                            Certificate newCert = new Certificate();
                            newCert.setCname(certDto.getCname());
                            return certificateRepo.save(newCert);
                        }))
                .collect(Collectors.toList());

        // Map DTO to Entity
        Employee employee = EmployeeMapper.mapToEmployee(employeeDto);
        employee.setDepartment(dept);
        employee.setCertificates(certificates);

        // Save employee
        Employee savedEmployee = employeeRepository.save(employee);
        return EmployeeMapper.mapToEmployeeDto(savedEmployee);
    }

    @Override
    public EmployeeDto getEmployeeById(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee does not exist with ID: " + employeeId));
        return EmployeeMapper.mapToEmployeeDto(employee);
    }

    @Override
    public List<EmployeeDto> getAllEmployees() {
        return employeeRepository.findAll()
                .stream()
                .map(EmployeeMapper::mapToEmployeeDto)
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeDto updateEmployee(Long employeeId, EmployeeDto updatedEmployee) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee does not exist with ID: " + employeeId));

        EmployeeValidator.validate(updatedEmployee, employeeRepository, true, employeeId);

        // Update basic employee fields
        EmployeeMapper.updateEmployeeFromDto(employee, updatedEmployee);

        // Certificate handling: Use existing certificate by name or create new one
        List<Certificate> updatedCerts = updatedEmployee.getCertificates().stream()
                .map(certDto -> certificateRepo.findByCnameIgnoreCase(certDto.getCname())
                        .orElseGet(() -> {
                            Certificate newCert = new Certificate();
                            newCert.setCname(certDto.getCname());
                            return certificateRepo.save(newCert);
                        }))
                .collect(Collectors.toList());
        employee.setCertificates(updatedCerts);

        // Save updated employee
        Employee updated = employeeRepository.save(employee);
        return EmployeeMapper.mapToEmployeeDto(updated);
    }

    @Override
    public void deleteEmployee(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee does not exist with ID: " + employeeId));
        employeeRepository.deleteById(employeeId);
    }
}
