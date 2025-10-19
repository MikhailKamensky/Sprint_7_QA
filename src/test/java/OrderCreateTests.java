import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import praktikum.sprint7.clients.CourierClient;
import praktikum.sprint7.clients.OrderClient;
import praktikum.sprint7.models.Order;
import praktikum.sprint7.models.OrderCreateResponse;

import java.util.List;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;

@RunWith(Parameterized.class)
public class OrderCreateTests {
    private static final String BASE_URL = "https://qa-scooter.praktikum-services.ru";
    private OrderClient orderClient;
    private int orderTrack;
    private Response response;

    private List<String> color;
    public OrderCreateTests(List<String> color) {
        this.color = color;
    }

    @Parameterized.Parameters (name = "Цвет самоката - {0}")
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
        orderClient = new OrderClient();
    }

    @Test
    @DisplayName("Создание заказа")
    @Description("Проверка создания заказа с валидными данными")
    public void orderCreate() {
        Order order = new Order(color);
        response =  orderClient.createOrder(order);
        response.then().body("track", instanceOf(Integer.class)).and().statusCode(SC_CREATED);
    }


    @After
    public void clearTest() {
        orderTrack = response.as(OrderCreateResponse.class).getOrderTrack();
        Response cancelResponse = orderClient.cancelOrder(orderTrack);
        cancelResponse.then().statusCode(SC_OK);  // Проверяем успешность отмены
        System.out.println("Отменён заказ с track: " + orderTrack);
    }
}
