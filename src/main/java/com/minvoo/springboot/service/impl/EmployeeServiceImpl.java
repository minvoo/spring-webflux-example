package com.minvoo.springboot.service.impl;

import com.minvoo.springboot.dto.EmployeeDto;
import com.minvoo.springboot.mapper.EmployeeMapper;
import com.minvoo.springboot.model.Employee;
import com.minvoo.springboot.repository.EmployeeRepository;
import com.minvoo.springboot.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeRepository employeeRepository;

    @Override
    public Mono<EmployeeDto> saveEmployee(EmployeeDto employeeDto) {
        Employee employee = EmployeeMapper.mapToEmployee(employeeDto);
        Mono<Employee> savedEmployee = employeeRepository.save(employee);
        return savedEmployee.map((employeeEntity)
                -> EmployeeMapper.mapToEmployeeDto(employeeEntity));
    }

    @Override
    public Mono<EmployeeDto> getEmployee(String id) {
        Mono<Employee> employeeMono = employeeRepository.findById(id);
        return employeeMono.map(employee -> EmployeeMapper.mapToEmployeeDto(employee));
    }

    @Override
    public Flux<EmployeeDto> getAllEmployees() {
        Flux<Employee> allEmployees = employeeRepository.findAll();
        return allEmployees.map(employee -> EmployeeMapper.mapToEmployeeDto(employee))
                .switchIfEmpty(Flux.empty());
    }

    @Override
    public Mono<EmployeeDto> updateEmployee(String id, EmployeeDto employeeDto) {

        Mono<EmployeeDto> employee = getEmployee(id);

        Mono<Employee> employeeMono = employee.flatMap((existingEmployee) -> {

            existingEmployee.setFirstName(employeeDto.getFirstName());
            existingEmployee.setLastName(employeeDto.getLastName());
            existingEmployee.setEmail(employeeDto.getEmail());
            Employee employeeEntity = EmployeeMapper.mapToEmployee(existingEmployee);
            return employeeRepository.save(employeeEntity);
        });

        return employeeMono.map(e -> EmployeeMapper.mapToEmployeeDto(e));

    }

    @Override
    public void deleteEmployee(String id) {
        employeeRepository.deleteById(id);
    }
}
