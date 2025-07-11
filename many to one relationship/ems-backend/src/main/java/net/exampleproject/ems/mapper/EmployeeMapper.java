package net.exampleproject.ems.mapper;

import net.exampleproject.ems.dto.EmployeeDto;
import net.exampleproject.ems.entity.Employee;
import net.exampleproject.ems.entity.Department;

public class EmployeeMapper {

    public static EmployeeDto mapToEmployeeDto(Employee employee) {
        return new EmployeeDto(
                employee.getEmpid(),
                employee.getEmpname(),
                employee.getPhno(),
                employee.getEmail(),
                employee.getPassword(),
                employee.getDepartment() != null ? employee.getDepartment().getDeptid() : null,
                employee.getDepartment() != null ? employee.getDepartment().getDeptname() : null
        );
    }

    public static Employee mapToEmployee(EmployeeDto dto) {
        return new Employee(
                dto.getEmpid(),
                dto.getEmpname(),
                dto.getPhno(),
                dto.getEmail(),
                dto.getPassword(),
                null // Leave Department null here; Service will set it!
        );
    }

    public static void updateEmployeeFromDto(Employee employee, EmployeeDto dto){
        employee.setEmpname(dto.getEmpname());
        employee.setPhno(dto.getPhno());
        employee.setEmail(dto.getEmail());
        employee.setPassword(dto.getPassword());
    }
}

