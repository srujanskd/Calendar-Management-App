package com.srujanskd.calendarmanagement.controller;


import com.srujanskd.calendarmanagement.model.Employee;
import com.srujanskd.calendarmanagement.repository.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Slf4j
@RestController
@RequestMapping("/api")
public class EmployeeController {
    @Autowired
    EmployeeRepository employeeRepository;

    @GetMapping("/employees")
    public ResponseEntity<List<Employee>> getEmployees() {
        try {
            log.debug(employeeRepository.findAll().toString());
            return new ResponseEntity<>(employeeRepository.findAll(), HttpStatus.OK);
        } catch (Exception e) {
            log.warn(e.toString());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/employee")
    public ResponseEntity<Employee> getEmployee(@RequestParam(name = "id", required = false) Long id,
                                                @RequestParam(name = "email", required = false) String email){
        if(id != null) {
            Employee emp = employeeRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Employee not found for this id :: " + id));

            return ResponseEntity.ok().body(emp);
        }
        else if(email != null) {
            Employee emp = employeeRepository.findByEmail(email);
            return ResponseEntity.ok().body(emp);
        }
        return (ResponseEntity<Employee>) ResponseEntity.badRequest();
    }

    @PostMapping("/employee")
    public ResponseEntity<Employee> addEmployee(@RequestBody Employee employee) {
        try {
            Employee newEmployee = employeeRepository
                    .save(Employee.builder()
                            .id(employee.getId())
                            .name(employee.getName())
                            .address(employee.getAddress())
                            .email(employee.getEmail())
                            .officeLocation(employee.getOfficeLocation())
                            .build());
            log.debug(newEmployee.toString());
            return new ResponseEntity<>(newEmployee, HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PutMapping("/employee")
    public ResponseEntity<Employee> updateEmployee(@RequestParam(name = "id", required = false) Long id,
                                                   @RequestParam(name = "email", required = false) String email,
                                                   @RequestBody Employee employeeDetails) {
        if (id != null) {
            Employee employee = employeeRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Employee not found for this id :: " + id));

            employee.setEmail(employeeDetails.getEmail());
            employee.setName(employeeDetails.getName());
            employee.setAddress(employeeDetails.getAddress());
            employee.setOfficeLocation(employeeDetails.getOfficeLocation());
            final Employee updatedEmployee = employeeRepository.save(employee);
            return ResponseEntity.ok(updatedEmployee);
        } else if (email != null) {
            Employee employee = employeeRepository.findByEmail(email);
            employee.setId(employeeDetails.getId());
            employee.setName(employeeDetails.getName());
            employee.setAddress(employeeDetails.getAddress());
            employee.setOfficeLocation(employeeDetails.getOfficeLocation());
            final Employee updatedEmployee = employeeRepository.save(employee);
            return ResponseEntity.ok(updatedEmployee);
        }
        return (ResponseEntity<Employee>) ResponseEntity.badRequest();
    }

    @DeleteMapping("/employee")
    public Map<String, Boolean> deleteEmployee(@RequestParam(name = "id", required = false) Long id,
                                               @RequestParam(name = "email", required = false)String email) {
        if(id != null) {
            Employee employee = employeeRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Employee not found for this id :: " + id));

            employeeRepository.delete(employee);
            Map<String, Boolean> response = new HashMap<>();
            response.put("deleted", Boolean.TRUE);
            return response;
        }
        else if(email != null) {
            Employee employee = employeeRepository.findByEmail(email);
            employeeRepository.delete(employee);
            Map<String, Boolean> response = new HashMap<>();
            response.put("deleted", Boolean.TRUE);
            return response;
        }
        return (Map<String, Boolean>) ResponseEntity.badRequest();
    }

}
