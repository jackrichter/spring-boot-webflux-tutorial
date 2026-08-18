package net.javaguides.springboot;

import net.javaguides.springboot.controller.EmployeeController;
import net.javaguides.springboot.dto.EmployeeDto;
import net.javaguides.springboot.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for EmployeeController.
 */
@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private EmployeeService employeeService;

    @Test
    public void givenEmployeeObject_whenSaveEmployee_thenReturnSavedEmployee() {

        // given - pre-conditions or setup
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setFirstName("Ramesh");
        employeeDto.setLastName("Fadatare");
        employeeDto.setEmail("ramesh@gmail.com");

        // stab the method call
        BDDMockito.given(employeeService.saveEmployee(ArgumentMatchers.any(EmployeeDto.class)))
                .willReturn(Mono.just(employeeDto));

        // when - action or behavior
        WebTestClient.ResponseSpec response = webTestClient.post().uri("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(employeeDto), EmployeeDto.class)
                .exchange();        // Makes the REST API call. Returns ResponseSpec object.

        // then - verify the result or the output
        response.expectStatus().isCreated()
                .expectBody()
                .consumeWith(System.out::println)       // Print the response body
                .jsonPath("$.firstName").isEqualTo(employeeDto.getFirstName())
                .jsonPath("$.lastName").isEqualTo(employeeDto.getLastName())
                .jsonPath("$.email").isEqualTo(employeeDto.getEmail());
    }

    @Test
    public void givenEmployeeId_whenGetEmployee_thenReturnEmployeeObject() {

        // given - pre-condition
        String employeeId = "123";

        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setFirstName("Ramesh");
        employeeDto.setLastName("Fadatare");
        employeeDto.setEmail("ramesh@gmail.com");

        BDDMockito.given(employeeService.getEmployee(employeeId))
                .willReturn(Mono.just(employeeDto));

        // when - action
        WebTestClient.ResponseSpec response = webTestClient.get()
                .uri("/api/employees/{id}", Collections.singletonMap("id", employeeId))
                .exchange();

        // then - verify the output
        response.expectStatus().isOk()
                .expectBody()
                .consumeWith(System.out::println)       // Print the response body
                .jsonPath("$.firstName").isEqualTo(employeeDto.getFirstName())
                .jsonPath("$.lastName").isEqualTo(employeeDto.getLastName())
                .jsonPath("$.email").isEqualTo(employeeDto.getEmail());
    }

    @Test
    public void givenListOfEmployees_whenGetAllEmployees_thenReturnListOfEmployees() {

        // given - pre-condition
        List<EmployeeDto> list = new ArrayList<>();

        EmployeeDto employeeDto1 = new EmployeeDto();
        employeeDto1.setFirstName("Ramesh");
        employeeDto1.setLastName("Fadatare");
        employeeDto1.setEmail("ramesh@gmail.com");
        list.add(employeeDto1);

        EmployeeDto employeeDto2 = new EmployeeDto();
        employeeDto2.setFirstName("Tony");
        employeeDto2.setLastName("Starck");
        employeeDto2.setEmail("tony@gmail.com");
        list.add(employeeDto2);

        Flux<EmployeeDto> employeeFlux = Flux.fromIterable(list);

        BDDMockito.given(employeeService.getAllEmployees())
                .willReturn(employeeFlux);

        // when - action
        WebTestClient.ResponseSpec response = webTestClient.get()
                .uri("/api/employees")
                .accept(MediaType.APPLICATION_JSON)
                .exchange();

        // then - verify the output of response
        response.expectStatus().isOk()
                .expectBodyList(EmployeeDto.class)
                .consumeWith(System.out::println);
    }

    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnUpdatedEmployee() {

        // given
        String employeeId = "123";

        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setFirstName("Ramesh");
        employeeDto.setLastName("Fadatare");
        employeeDto.setEmail("ramesh@gmail.com");

        // Mocking the action - updateEmployee
        BDDMockito.given(employeeService.updateEmployee(
                ArgumentMatchers.any(EmployeeDto.class),
                ArgumentMatchers.any(String.class)))
                .willReturn(Mono.just(employeeDto));

        // when
        WebTestClient.ResponseSpec response = webTestClient.put()
                .uri("/api/employees/{id}", Collections.singletonMap("id", employeeId))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(employeeDto), EmployeeDto.class)
                .exchange();

        // then
        response.expectStatus().isOk()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.firstName").isEqualTo(employeeDto.getFirstName())
                .jsonPath("$.lastName").isEqualTo(employeeDto.getLastName())
                .jsonPath("$.email").isEqualTo(employeeDto.getEmail());
    }

    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenReturnNoContent() {

        // given
        String employeeId = "123";
        Mono<Void> voidMono = Mono.empty();

        BDDMockito.given(employeeService.deleteEmployee(employeeId))
                .willReturn(voidMono);

        // when
        WebTestClient.ResponseSpec response = webTestClient.delete()
                .uri("/api/employees/{id}", Collections.singletonMap("id", employeeId))
                .exchange();

        // then
        response.expectStatus().isNoContent()
                .expectBody()
                .consumeWith(System.out::println);
    }
}