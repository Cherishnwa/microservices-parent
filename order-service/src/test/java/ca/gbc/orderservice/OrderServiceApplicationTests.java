package ca.gbc.orderservice;

import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class OrderServiceApplicationTests {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("order-service-test")
            .withUsername("test")
            .withPassword("test");

    @LocalServerPort
    private Integer port;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    @Test
    void shouldPlaceOrder() {
        String requestBody = """
                {
                    "orderLineItemDtoList": [
                        {
                            "skuCode": "iphone_15_pro",
                            "price": 1299.99,
                            "quantity": 1
                        },
                        {
                            "skuCode": "airpods_pro",
                            "price": 249.99,
                            "quantity": 2
                        }
                    ]
                }
                """;

        RestAssured.given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/api/order")
                .then()
                .log().all()
                .statusCode(201)
                .body(Matchers.equalTo("Order Placed Successfully"));
    }

    @Test
    void shouldPlaceOrderWithSingleItem() {
        String requestBody = """
                {
                    "orderLineItemDtoList": [
                        {
                            "skuCode": "macbook_pro_16",
                            "price": 2499.00,
                            "quantity": 1
                        }
                    ]
                }
                """;

        RestAssured.given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/api/order")
                .then()
                .log().all()
                .statusCode(201)
                .body(Matchers.equalTo("Order Placed Successfully"));
    }

    @Test
    void shouldPlaceOrderWithMultipleItems() {
        String requestBody = """
                {
                    "orderLineItemDtoList": [
                        {
                            "skuCode": "ipad_air",
                            "price": 599.99,
                            "quantity": 2
                        },
                        {
                            "skuCode": "apple_pencil",
                            "price": 129.99,
                            "quantity": 2
                        },
                        {
                            "skuCode": "magic_keyboard",
                            "price": 299.99,
                            "quantity": 1
                        }
                    ]
                }
                """;

        RestAssured.given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/api/order")
                .then()
                .log().all()
                .statusCode(201)
                .body(Matchers.equalTo("Order Placed Successfully"));
    }

    @Test
    void shouldPlaceEmptyOrder() {
        String requestBody = """
                {
                    "orderLineItemDtoList": []
                }
                """;

        RestAssured.given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/api/order")
                .then()
                .log().all()
                .statusCode(201)
                .body(Matchers.equalTo("Order Placed Successfully"));
    }
}