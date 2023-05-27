package com.minvoo.springboot.controller;

import com.minvoo.springboot.dto.EmployeeDto;
import com.minvoo.springboot.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = EmployeeController.class)
public class EmployeeControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private EmployeeService employeeService;

    @Test
    public void givenEmployeeObject_whenSaveEmployee_thenReturnSavedEmployee() {

        // given - pre-conditions or set up
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setFirstName("Mariusz");
        employeeDto.setLastName("Maciejewski");
        employeeDto.setEmail("mar@mac.pl");

        BDDMockito.given(employeeService.saveEmployee(ArgumentMatchers.any(EmployeeDto.class)))
                .willReturn(Mono.just(employeeDto));

        // when - action or behaviour
        WebTestClient.ResponseSpec response = webTestClient.post().uri("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(employeeDto), EmployeeDto.class)
                .exchange();

        // then - verify the result or output
        response.expectStatus().isCreated()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.firstName").isEqualTo(employeeDto.getFirstName())
                .jsonPath("$.lastName").isEqualTo(employeeDto.getLastName())
                .jsonPath("$.email").isEqualTo(employeeDto.getEmail());
    }

    @Test
    public void givenEmployeeId_whenGetEmployee_thenReturnEmployee() {

        //given
        String employeeId = "1";

        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setFirstName("Mariusz");
        employeeDto.setLastName("Maciejewski");
        employeeDto.setEmail("mar@mac.pl");

        BDDMockito.given(employeeService.getEmployee(employeeId)).willReturn(Mono.just(employeeDto));
        //when
        WebTestClient.ResponseSpec response = webTestClient.get()
                .uri("/api/employees/{id}", Collections.singletonMap("id", employeeId))
                .exchange();

        //then
        response.expectStatus().isOk()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.firstName").isEqualTo(employeeDto.getFirstName())
                .jsonPath("$.lastName").isEqualTo(employeeDto.getLastName())
                .jsonPath("$.email").isEqualTo(employeeDto.getEmail());
    }

    @Test
    public void givenListOfEmployees_whenGetAllEmployees_thenReturnListOfEmployees() {

        //given
        List<EmployeeDto> employeeDtoList = new ArrayList<>();

        EmployeeDto employeeDto1 = new EmployeeDto();
        employeeDto1.setFirstName("Mariusz");
        employeeDto1.setLastName("Maciejewski");
        employeeDto1.setEmail("mar@mac.pl");

        EmployeeDto employeeDto2 = new EmployeeDto();
        employeeDto2.setFirstName("Mariusz");
        employeeDto2.setLastName("Maciejewski");
        employeeDto2.setEmail("mar@mac.pl");

        employeeDtoList.add(employeeDto1);
        employeeDtoList.add(employeeDto2);

        Flux<EmployeeDto> employeeDtoFlux = Flux.fromIterable(employeeDtoList);
        BDDMockito.given(employeeService.getAllEmployees()).willReturn(employeeDtoFlux);

        //when
        WebTestClient.ResponseSpec response = webTestClient.get().uri("/api/employees")
                .accept(MediaType.APPLICATION_JSON)
                .exchange();

        //then
        response.expectStatus().isOk()
                .expectBodyList(EmployeeDto.class)
                .consumeWith(System.out::println)
                .hasSize(2);
    }

    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnUpdatedEmployee() {

        //given
        String employeeId = "1";

        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setFirstName("Mariusz");
        employeeDto.setLastName("Maciejewski");
        employeeDto.setEmail("mar@mac.pl");

        BDDMockito.given(employeeService.updateEmployee(ArgumentMatchers.any(String.class),
                        ArgumentMatchers.any(EmployeeDto.class)))
                .willReturn(Mono.just(employeeDto));

        //when
        WebTestClient.ResponseSpec response = webTestClient.put().uri("/api/employees/{id}", Collections.singletonMap("id", employeeId))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(employeeDto),EmployeeDto.class)
                .exchange();

        //then
        response.expectStatus().isOk()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.firstName").isEqualTo(employeeDto.getFirstName())
                .jsonPath("$.lastName").isEqualTo(employeeDto.getLastName())
                .jsonPath("$.email").isEqualTo(employeeDto.getEmail());
    }

    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenReturnNothing(){

        // given - pre-conditions
        String employeeId = "123";
        Mono<Void> voidMono = Mono.empty();
        BDDMockito.given(employeeService.deleteEmployee(employeeId))
                .willReturn(voidMono);

        // when - action or behaviour
        WebTestClient.ResponseSpec response = webTestClient
                .delete()
                .uri("/api/employees/{id}", Collections.singletonMap("id", employeeId))
                .exchange();

        // then - verify the output
        response.expectStatus().isNoContent()
                .expectBody()
                .consumeWith(System.out::println);
    }
}
