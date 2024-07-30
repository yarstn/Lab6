package com.example.employeemanagementsystem.Controller;
import com.example.employeemanagementsystem.ApiResponse;
import com.example.employeemanagementsystem.Model.Employee;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/vi/employee")
public class EmployeeController {
    ArrayList<Employee> employees = new ArrayList<Employee>();
@GetMapping("/get")
    public ArrayList<Employee> getEmployees() {
        return employees;
    }
    @PostMapping("/add")
    public ResponseEntity addEmployee(@Valid @RequestBody Employee employee, Errors errors) {
    if (errors.hasErrors()){
        String errorMessage = errors.getAllErrors().get(0).getDefaultMessage();
        return ResponseEntity.badRequest().body(errorMessage);
    }
    employees.add(employee);
    return ResponseEntity.status(200).body(new ApiResponse("Employee added successfully"));

    }

    @PutMapping("/update/{id}")
    public ResponseEntity updateEmployee(@PathVariable String id, @Valid @RequestBody Employee employee, Errors errors) {
    if (errors.hasErrors()){
        String errorMessage = errors.getAllErrors().get(0).getDefaultMessage();
        return ResponseEntity.badRequest().body(errorMessage);
    }
        for (int i = 0; i < employees.size(); i++) {
            if (employees.get(i).getId().equals(id)) {
                employees.set(i, employee);
                return ResponseEntity.ok(new ApiResponse("Employee updated successfully"));
            }
        }
        return ResponseEntity.status(404).body(new ApiResponse("Employee not found"));

    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteEmployee(@PathVariable String id) {
        for (Employee employee : employees) {
            if (employee.getId().equals(id)) {
                employees.remove(employee);
                return ResponseEntity.ok(new ApiResponse("Employee deleted successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse("Employee not found"));
            }
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse("Error deleting employee"));

    }


    @GetMapping("/search/{position}")
    public ResponseEntity searchEmployee(@PathVariable String position) {
        ArrayList<Employee> matchingEmployees = new ArrayList<>();
        for (Employee emp : employees) {
            if (emp.getPosition().equalsIgnoreCase(position)) {
                matchingEmployees.add(emp);
            }
        }
return ResponseEntity.ok(matchingEmployees);
    }


    @GetMapping("/age-range/{minAge}/{maxAge}")
    public ResponseEntity getEmployeesByAgeRange( @PathVariable int minAge,@PathVariable int maxAge) {
//    if (errors.hasErrors()){
//        String errorMessage = errors.getAllErrors().get(0).getDefaultMessage();
//        return ResponseEntity.badRequest().body(errorMessage);
//    }
        List<Employee> employeesInAgeRange = new ArrayList<>();
       for (Employee emp : employees) {
           if (emp.getAge() >= minAge && emp.getAge() <= maxAge) {
               employeesInAgeRange.add(emp);

           }
       }
       return ResponseEntity.ok(employeesInAgeRange);
    }
    //leave
    @PutMapping("/apply-leave/{employeeId}")
    public ResponseEntity applyForAnnualLeave(@PathVariable String employeeId) {
        Employee employee = null; //store id
        for (Employee emp : employees) {
            if (emp.getId().equals(employeeId)) {
                employee = emp;
                break;
            }
        }
        if (employee == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Employee not found"));
        }

        if (employee.isOnLeave()) {
            return ResponseEntity.badRequest().body(new ApiResponse("Employee is already on leave"));
        }

        if (employee.getAnnualLeave() <= 0) {
            return ResponseEntity.badRequest().body(new ApiResponse("Employee has no annual leave remaining"));
        }
        employee.setOnLeave(true);
        employee.setAnnualLeave(employee.getAnnualLeave() - 1);
        return ResponseEntity.ok(new ApiResponse("Annual leave applied successfully"));
    }

    @GetMapping("/employees-with-no-leave")
    public ResponseEntity getEmployeesWithNoAnnualLeave() {
        List<Employee> employeesWithNoLeave = new ArrayList<>();
        for (Employee employee : employees) {
            if (employee.getAnnualLeave() <= 0) {
                employeesWithNoLeave.add(employee);
            }
        }

        return ResponseEntity.ok(employeesWithNoLeave);
    }

    @PutMapping("/promote/{idSuper}/{idEmp}")
    public ResponseEntity promoteEmployee(@PathVariable String idSuper, @PathVariable String idEmp) {
        // check supervisor id
        Employee employeeToPromote = null;
        Employee supervisor = null;
        for (Employee employee : employees) {
            if (employee.getId().equals(idSuper) && employee.getPosition().contains("supervisor")) {
                supervisor = employee;
            }
            if (employee.getId().equals(idEmp)) {
                employeeToPromote = employee;
            }
        }
        if (supervisor == null) {
            return ResponseEntity.status(400).body(new ApiResponse("Supervisor not found"));
        }

        if (employeeToPromote == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Employee not found"));
        }

        // Validate the promotion conditions
        if (employeeToPromote.getAge() < 30) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Employee must be at least 30 years old to be promoted"));
        }
        if (employeeToPromote.isOnLeave()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Employee cannot be promoted while on leave"));
        }
        // Promote the employee
        employeeToPromote.setPosition("supervisor");
        return ResponseEntity.ok(new ApiResponse("Employee promoted successfully"));

    }



}