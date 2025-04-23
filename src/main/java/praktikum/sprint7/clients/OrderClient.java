package praktikum.sprint7.clients;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import praktikum.sprint7.models.Order;
import praktikum.sprint7.models.OrderCreateResponse;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

public class OrderClient {
    private static final String API_V1_ORDERS  = "/api/v1/orders";
    public static  final String ORDER_GET_LIST = "/api/v1/orders";
    public static  final String API_V1_ORDER_CANCEL = "/api/v1/orders/cancel";


    @Step("POST request to /api/v1/orders")
    public Response createOrder(Order order) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(order)
                .when()
                .post(API_V1_ORDERS);
    }

    @Step("GET request to /api/v1/orders")
    public Response getOrderList() {
        return given().log().all()
                .when()
                .get(ORDER_GET_LIST);
    }

    @Step("PUT request /api/v1/orders/cancel")
    public Response cancelOrder(int track) {
        return given()
                .header("Content-type", "application/json")
                .body("{\"track\": " + track + "}")
                .when()
                .put(API_V1_ORDER_CANCEL);
    }


}
