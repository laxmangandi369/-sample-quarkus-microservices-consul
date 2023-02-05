package pl.piomin.services.quarkus.employee;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import pl.piomin.services.quarkus.employee.model.Employee;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EmployeeResourceTests {

    @Test
    @Order(1)
    void add() {
        Employee e = Instancio.of(Employee.class)
                .ignore(Select.field(Employee::getId))
                .set(Select.field(Employee::getDepartmentId), 1L)
                .generate(Select.field(Employee::getAge), gen -> gen.ints().range(18, 65))
                .create();

        given().body(e).contentType(ContentType.JSON)
                .when().post("/employees").then()
                .statusCode(200)
                .body("id", notNullValue());
    }

    @Test
    @Order(2)
    void findAll() {
        when().get("/employees").then()
                .statusCode(200)
                .body("size()", is(1));
    }

    @Test
    @Order(2)
    void findById() {
        when().get("/employees/{id}", 1).then()
                .statusCode(200)
                .body("id", is(1));
    }

    @Test
    @Order(2)
    void findByDepartmentId() {
        when().get("/employees/department/{departmentId}", 1).then()
                .statusCode(200)
                .body("size()", is(1));
    }

    @Test
    @Order(2)
    void findByDepartmentIdNotFound() {
        when().get("/employees/department/{departmentId}", 100).then()
                .statusCode(200)
                .body("size()", is(0));
    }
}
