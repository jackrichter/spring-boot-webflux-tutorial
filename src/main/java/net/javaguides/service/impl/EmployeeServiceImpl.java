package net.javaguides.service.impl;

import lombok.AllArgsConstructor;
import net.javaguides.dto.EmployeeDto;
import net.javaguides.entity.Employee;
import net.javaguides.mapper.EmployeeMapper;
import net.javaguides.repository.EmployeeRepository;
import net.javaguides.service.EmployeeService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Override
    public Mono<EmployeeDto> saveEmployee(EmployeeDto employeeDto) {
        Employee employee = EmployeeMapper.mapToEmployee(employeeDto);
        Mono<Employee> savedEmployee = employeeRepository.save(employee);
        return savedEmployee
                .map(entity -> EmployeeMapper.mapToEmployeeDto(entity));
    }

    @Override
    public Mono<EmployeeDto> getEmployee(String employeeId) {
        Mono<Employee> savedEmployee = employeeRepository.findById(employeeId);
        return savedEmployee
                .map(entity -> EmployeeMapper.mapToEmployeeDto(entity));
    }

    @Override
    public Flux<EmployeeDto> getAllEmployees() {
        Flux<Employee> employeeFlux = employeeRepository.findAll();
        return employeeFlux
                .map(entity -> EmployeeMapper.mapToEmployeeDto(entity))
                .switchIfEmpty(Flux.empty());       // if no employee found, return empty Flux
    }

    @Override
    public Mono<EmployeeDto> updateEmployee(EmployeeDto employeeDto, String employeeId) {
        Mono<Employee> employeeMono = employeeRepository.findById(employeeId);

        // Use the 'flatmap' to perform the update and save the updated employee to database
        Mono<Employee> updatedEmployee = employeeMono
                .flatMap((existingEmployee) -> {
                    existingEmployee.setFirstName(employeeDto.getFirstName());
                    existingEmployee.setLastName(employeeDto.getLastName());
                    existingEmployee.setEmail(employeeDto.getEmail());

                    return employeeRepository.save(existingEmployee);
                });

        return updatedEmployee
                .map(entity -> EmployeeMapper.mapToEmployeeDto(entity));
    }
}
