package com.minvoo.springboot.service;

import com.minvoo.springboot.dto.EmployeeDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface EmployeeService {

    Mono<EmployeeDto> saveEmployee(EmployeeDto employeeDto);
    Mono<EmployeeDto> getEmployee(String id);

    Flux<EmployeeDto> getAllEmployees();

    Mono<EmployeeDto> updateEmployee(String id, EmployeeDto employeeDto);

    void deleteEmployee(String id);
}
