package net.exampleproject.ems.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.exampleproject.ems.entity.Address;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDto {

 private Long empid;
 private String empname;
 private Long phno;
 private String email;
 private String password;

 //Department
 private Long deptid;
 private String deptname;

 //Address
 private AddressDto address;

 // List of certificates
 private List<CertificateDto> certificates;

 
}