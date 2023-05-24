package com.minvoo.springboot.mapper;

import com.minvoo.springboot.dto.EmployeeDto;
import com.minvoo.springboot.model.Employee;

public class EmployeeMapper {

    private EmployeeMapper() {
    }

    public static EmployeeDto mapToEmployeeDto(Employee employee) {
        return new EmployeeDto(
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail()
        );
    }

    public  static Employee mapToEmployee(EmployeeDto employeeDto) {
        return new Employee(
                employeeDto.getId(),
                employeeDto.getFirstName(),
                employeeDto.getLastName(),
                employeeDto.getEmail()
        );
    }
}
