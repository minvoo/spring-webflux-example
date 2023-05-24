package com.minvoo.springboot.repository;

import com.minvoo.springboot.model.Employee;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface EmployeeRepository extends ReactiveCrudRepository<Employee, String> {


}
