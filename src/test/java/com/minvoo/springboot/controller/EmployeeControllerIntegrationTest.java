package com.minvoo.springboot.controller;

import com.minvoo.springboot.dto.EmployeeDto;
import com.minvoo.springboot.repository.EmployeeRepository;
import com.minvoo.springboot.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Collections;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeControllerIntegrationTest {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private EmployeeRepository employeeRepository;

    @BeforeEach
    public void before(){
        System.out.println("Before Each Test");
        employeeRepository.deleteAll().subscribe();
    }

    @Test
    public void testSaveEmployee(){

        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setFirstName("Mariusz");
        employeeDto.setLastName("Maciejewski");
        employeeDto.setEmail("mail@com.pl");

        webTestClient.post().uri("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(employeeDto), EmployeeDto.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.firstName").isEqualTo(employeeDto.getFirstName())
                .jsonPath("$.lastName").isEqualTo(employeeDto.getLastName())
                .jsonPath("$.email").isEqualTo(employeeDto.getEmail());
    }
    @Test
    public void testGetSingleEmployee(){

        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setFirstName("Mariusz");
        employeeDto.setLastName("Maciejewski");
        employeeDto.setEmail("mail@com.pl");

        EmployeeDto savedEmployee = employeeService.saveEmployee(employeeDto).block();

        webTestClient.get().uri("/api/employees/{id}", Collections.singletonMap("id",savedEmployee.getId()))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.id").isEqualTo(savedEmployee.getId())
                .jsonPath("$.firstName").isEqualTo(employeeDto.getFirstName())
                .jsonPath("$.lastName").isEqualTo(employeeDto.getLastName())
                .jsonPath("$.email").isEqualTo(employeeDto.getEmail());
    }

    @Test
    public void testGetAllEmployees(){

        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setFirstName("John");
        employeeDto.setLastName("Cena");
        employeeDto.setEmail("john@gmail.com");

        employeeService.saveEmployee(employeeDto).block();

        EmployeeDto employeeDto1 = new EmployeeDto();
        employeeDto1.setFirstName("Mariusz");
        employeeDto1.setLastName("Maciejewski");
        employeeDto1.setEmail("mail@com.pl");

        employeeService.saveEmployee(employeeDto1).block();

        webTestClient.get().uri("/api/employees")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(EmployeeDto.class)
                .consumeWith(System.out::println);
    }

    @Test
    public void testUpdateEmployee(){

        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setFirstName("Mariusz");
        employeeDto.setLastName("Maciejewski");
        employeeDto.setEmail("mail@com.pl");

        EmployeeDto savedEmployee = employeeService.saveEmployee(employeeDto).block();

        EmployeeDto updatedEmployee = new EmployeeDto();
        updatedEmployee.setFirstName("Mariusz1");
        updatedEmployee.setLastName("Maciejewski");
        updatedEmployee.setEmail("mail@com.pl");

        webTestClient.put().uri("/api/employees/{id}", Collections.singletonMap("id", savedEmployee.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(updatedEmployee), EmployeeDto.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.firstName").isEqualTo(updatedEmployee.getFirstName())
                .jsonPath("$.lastName").isEqualTo(updatedEmployee.getLastName())
                .jsonPath("$.email").isEqualTo(updatedEmployee.getEmail());
    }

    @Test
    public void testDeleteEmployee(){

        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setFirstName("Mariusz");
        employeeDto.setLastName("Maciejewski");
        employeeDto.setEmail("mail@com.pl");

        EmployeeDto savedEmployee = employeeService.saveEmployee(employeeDto).block();

        webTestClient.delete().uri("/api/employees/{id}", Collections.singletonMap("id", savedEmployee.getId()))
                .exchange()
                .expectStatus().isNoContent()
                .expectBody()
                .consumeWith(System.out::println);

    }

}
