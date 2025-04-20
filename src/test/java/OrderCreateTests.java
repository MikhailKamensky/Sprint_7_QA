import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import praktikum.sprint7.clients.CourierClient;
import praktikum.sprint7.clients.OrderClient;
import praktikum.sprint7.models.Order;

import java.util.List;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;

@RunWith(Parameterized.class)
public class OrderCreateTests {
    private static final String BASE_URL = "https://qa-scooter.praktikum-services.ru";


    private List<String> color;
    public void OrderCreateTests(List<String> color) {
        this.color = color;
    }

    @Parameterized.Parameters ()
    public static Object[][] getColors() {
        return new Object[][] {
                {List.of("BLACK", "GREY")},
                {List.of("BLACK")},
                {List.of("GREY")},
                {List.of()}
        };
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
    }


    @Test
    @DisplayName("Создание заказа")
    public void orderCreate() {
        Order order = new Order(color);
        OrderClient orderClient = new OrderClient();
        Response response =  orderClient.createOrder(order);
        response.then().body("track", instanceOf(Integer.class)).and().statusCode(SC_OK);
    }

}
