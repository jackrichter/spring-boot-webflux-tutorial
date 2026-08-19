package net.javaguides.springboot;

import net.javaguides.springboot.dto.EmployeeDto;
import net.javaguides.springboot.service.EmployeeService;
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

    @Test
    public void testSaveEmployee() {

        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setFirstName("Jane");
        employeeDto.setLastName("Flowers");
        employeeDto.setEmail("jane@gmail.com");

        // Service and Repository layers are implicitly(internally) called, through the Http call to the Rest Api
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
    public void testGetSingleEmployee() {

        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setFirstName("Meena");
        employeeDto.setLastName("Fadatare");
        employeeDto.setEmail("meena@gmail.com");

        // SaveEmployee returns a Mono<EmployeeDto>. To get the EmployeeDto object, we need to call .block() method !!!
        EmployeeDto savedEmployee = employeeService.saveEmployee(employeeDto).block();

        // Call the getEmployee Rest Api
        webTestClient.get()
                .uri("/api/employees/{id}", Collections.singletonMap("id", savedEmployee.getId()))
                .exchange()
                .expectStatus().isOk()
                .expectBody()           // get the response body
                .consumeWith(System.out::println)
                .jsonPath("$.id").isEqualTo(savedEmployee.getId())
                .jsonPath("$.firstName").isEqualTo(savedEmployee.getFirstName())
                .jsonPath("$.lastName").isEqualTo(savedEmployee.getLastName())
                .jsonPath("$.email").isEqualTo(savedEmployee.getEmail());
    }

    @Test
    public void testGetAllEmployees() {

        EmployeeDto employeeDto1 = new EmployeeDto();
        employeeDto1.setFirstName("John");
        employeeDto1.setLastName("Cena");
        employeeDto1.setEmail("john@gmail.com");

        employeeService.saveEmployee(employeeDto1).block();

        EmployeeDto employeeDto2 = new EmployeeDto();
        employeeDto2.setFirstName("Meena");
        employeeDto2.setLastName("Fadatare");
        employeeDto2.setEmail("meena@gmail.com");

        employeeService.saveEmployee(employeeDto2).block();

        webTestClient.get()
                .uri("/api/employees")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(EmployeeDto.class)
                .consumeWith(System.out::println);
    }
}
