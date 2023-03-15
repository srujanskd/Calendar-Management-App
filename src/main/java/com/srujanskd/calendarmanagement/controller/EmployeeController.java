package com.srujanskd.calendarmanagement.controller;

import com.srujanskd.calendarmanagement.model.Employee;
import com.srujanskd.calendarmanagement.repository.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

}
