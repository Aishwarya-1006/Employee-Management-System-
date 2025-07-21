package net.exampleproject.ems.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.exampleproject.ems.entity.Employee;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CertificateDto {

    private Long cid;
    private String cname;

    @JsonManagedReference

    @JsonIgnore
    private List<Employee> employees;

}
