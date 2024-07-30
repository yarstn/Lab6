package com.example.employeemanagementsystem.Model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Employee {
//    @NotNull //nums
    @NotEmpty(message = "Id Cannt be empty")
    @Size(min = 3, max = 20, message = "id size must be min 2")
    private String id;
    @NotEmpty(message = "name Cannt be empty") //string
    @Size(min = 4, max = 30,  message = "name size must be min 4")
    @Pattern(regexp = "[a-zA-Z]+", message = "name must be letters only")
    private String name;
    @NotEmpty(message = "email Cannt be empty")
    @Email(message = "email must be valid")
    private String email;
    @NotNull(message = "phone Cannt be empty")
    @Pattern(regexp = "^05\\d{8}$", message = "phone must be strat with 05")
    @Size(min = 10, max = 10, message = "phone Cannt be more than 10 nums")
    private String phone;
    @NotNull(message = "age Cannt be empty")
    @Min(25)
    @Digits(integer = 2, fraction = 0)
    private int age;
    @NotEmpty(message = "position Cannt be empty")
    @Pattern(regexp = "^(supervisor|coordinator)$", message = "Position must be either 'supervisor' or 'coordinator'")
    private String position;
    @AssertFalse
    private boolean onLeave;
    @NotNull(message = "hireDate Cannt be empty")
    @PastOrPresent
    private LocalDate hireDate;
    @NotNull(message = "annualLeave Cannt be empty")
    @Positive
    private int annualLeave;
}
