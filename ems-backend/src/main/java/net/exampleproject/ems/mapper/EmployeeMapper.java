package net.exampleproject.ems.mapper;

import net.exampleproject.ems.dto.AddressDto;
import net.exampleproject.ems.dto.CertificateDto;
import net.exampleproject.ems.dto.EmployeeDto;
import net.exampleproject.ems.entity.Address;
import net.exampleproject.ems.entity.Certificate;
import net.exampleproject.ems.entity.Employee;

import java.util.List;
import java.util.stream.Collectors;

public class EmployeeMapper {

    public static EmployeeDto mapToEmployeeDto(Employee employee) {
        List<CertificateDto> certificateDtos = null;
        if (employee.getCertificates() != null) {
            certificateDtos = employee.getCertificates().stream()
                    .map(CertificateMapper::mapToCertificateDto)
                    .collect(Collectors.toList());
        }


        AddressDto addressDto = null;
        if (employee.getAddress() != null) {
            addressDto = AddressMapper.mapToAddressDto(employee.getAddress());
        }

        return new EmployeeDto(
                employee.getEmpid(),
                employee.getEmpname(),
                employee.getPhno(),
                employee.getEmail(),
                employee.getPassword(),
                employee.getDepartment() != null ? employee.getDepartment().getDeptid() : null,
                employee.getDepartment() != null ? employee.getDepartment().getDeptname() : null,
                addressDto,
                certificateDtos
        );
    }

    public static Employee mapToEmployee(EmployeeDto dto) {
        List<Certificate> certificates = null;
        if (dto.getCertificates() != null) {
            certificates = dto.getCertificates().stream()
                    .map(CertificateMapper::mapToCertificate)
                    .collect(Collectors.toList());
        }

        Address address = null;
        if (dto.getAddress() != null) {
            address = AddressMapper.mapToAddress(dto.getAddress());
        }

        return new Employee(
                dto.getEmpid(),
                dto.getEmpname(),
                dto.getPhno(),
                dto.getEmail(),
                dto.getPassword(),
                null, // Department mapped separately
                address,
                certificates
        );
    }

    public static void updateEmployeeFromDto(Employee employee, EmployeeDto dto) {
        employee.setEmpname(dto.getEmpname());
        employee.setPhno(dto.getPhno());
        employee.setEmail(dto.getEmail());
        employee.setPassword(dto.getPassword());

        if (dto.getCertificates() != null) {
            List<Certificate> certificates = dto.getCertificates().stream()
                    .map(CertificateMapper::mapToCertificate)
                    .collect(Collectors.toList());
            employee.setCertificates(certificates);
        }
    }
}
