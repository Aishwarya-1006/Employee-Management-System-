package net.exampleproject.ems.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import net.exampleproject.ems.dto.AddressDto;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Employee {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long empid;

    @Column(name="emp_name", nullable = false)
    private String empname;

    @Column(name="phone_number", nullable = false)
    private Long phno;

    @Column(name="email_id",nullable = false)
    private String email;

    @Column(name="password", nullable = false)
    private String password;


    //ManyToOne mapping to Department
    @ManyToOne
    @JoinColumn(name = "deptid")

    @JsonBackReference
    @JsonIgnore
    private Department department;


    //OneToOne mapping to Address
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")

    @JsonBackReference
    @JsonIgnore
    private Address address;

    //ManyToMany mapping to certificate
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable( name = "employee_certificate", joinColumns = @JoinColumn(name = "empid"),
            inverseJoinColumns = @JoinColumn(name = "certificate_id"))

    @JsonBackReference
    @JsonIgnore
    private List<Certificate> certificates;

}