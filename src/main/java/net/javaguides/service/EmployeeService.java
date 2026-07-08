package net.javaguides.service;

import net.javaguides.dto.EmployeeDto;
import reactor.core.publisher.Mono;

public interface EmployeeService {

    Mono<EmployeeDto> saveEmployee(EmployeeDto employeeDto);
}
