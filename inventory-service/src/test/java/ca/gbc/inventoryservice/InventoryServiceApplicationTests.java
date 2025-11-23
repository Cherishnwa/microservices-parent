package ca.gbc.inventoryservice;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Testcontainers
class InventoryServiceApplicationTests {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest");

    @LocalServerPort
    private Integer port;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;

        //noinspection SqlResolve,SqlWithoutWhere
        jdbcTemplate.execute("DELETE FROM t_inventory;");
        //noinspection SqlResolve,SqlWithoutWhere
        jdbcTemplate.execute("INSERT INTO t_inventory (sku_code, quantity) VALUES ('SKU001', 200);");
        //noinspection SqlResolve,SqlWithoutWhere
        jdbcTemplate.execute("INSERT INTO t_inventory (sku_code, quantity) VALUES ('SKU002', 50);");
    }

    static {
        postgreSQLContainer.start();
    }

    @Test
    void shouldReturnTrueWhenItemIsInStock() {
        given()
                .queryParam("skuCode", "SKU001")
                .queryParam("quantity", 100)
                .when()
                .get("/api/inventory")
                .then()
                .log().all()
                .statusCode(200)
                .body(is("true"));
    }

    @Test
    void shouldReturnFalseWhenItemDoesNotExist() {
        given()
                .queryParam("skuCode", "NON_EXISTENT_SKU")
                .queryParam("quantity", 100)
                .when()
                .get("/api/inventory")
                .then()
                .log().all()
                .statusCode(200)
                .body(is("false"));
    }
}