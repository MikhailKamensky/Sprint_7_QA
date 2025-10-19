package praktikum.sprint7.clients;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import praktikum.sprint7.models.Courier;
import praktikum.sprint7.models.CourierCreds;

import static io.restassured.RestAssured.given;

public class CourierClient {
    private static final String API_V1_COURIER = "api/v1/courier";
    private static final String API_V1_COURIER_LOGIN = "api/v1/courier/login";

    @Step("POST request to api/v1/courier")
    public Response create(Courier courier) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post(API_V1_COURIER);
    }

    @Step("POST request to api/v1/courier/login")
    public Response login(CourierCreds courierCreds) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(courierCreds)
                .when()
                .post(API_V1_COURIER_LOGIN);
    }

    @Step("DELETE request to api/v1/courier/id")
    public Response delete(int id) {
        return given()
                .header("Content-type", "application/json")
                .when()
                .delete(API_V1_COURIER + "/" + id);//добавил в ручку по указанию наставника, возвращает 404 через Postman
    }
}