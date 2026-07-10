package net.javaguides.controller;

import lombok.AllArgsConstructor;
import net.javaguides.dto.EmployeeDto;
import net.javaguides.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/employees")
@AllArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    // Reactive Save Employee REST API (we have to return a Reactive type!)
    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public Mono<EmployeeDto> saveEmployee(@RequestBody EmployeeDto employeeDto) {
        return  employeeService.saveEmployee(employeeDto);
    }

    // Reactive Get Single Employee REST API
    @GetMapping("/{id}")
    public Mono<EmployeeDto> getEmployee(@PathVariable("id") String employeeId) {
        return employeeService.getEmployee(employeeId);
    }

    // Reactive Get All Employees REST API
    @GetMapping
    public Flux<EmployeeDto> getAllEmployees() {
        return employeeService.getAllEmployees();
    }
}
