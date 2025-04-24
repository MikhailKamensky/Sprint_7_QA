import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import praktikum.sprint7.clients.CourierClient;
import praktikum.sprint7.clients.OrderClient;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.notNullValue;

public class OrderGetTests {
        private static final String BASE_URL = "https://qa-scooter.praktikum-services.ru";
        public static  final String ORDER_GET_LIST = "/api/v1/orders";
        private OrderClient orderClient;

        @Before
        public void setUp() {
                RestAssured.baseURI = BASE_URL;
                orderClient = new OrderClient();
        }


        @Test
        @DisplayName("Получение списка заказов")
        @Description("Проверка получения списка заказов")
        public void orderGetListTest() {
                Response response = orderClient.getOrderList();
                response.then()
                        .assertThat().body("orders", notNullValue())
                        .and()
                        .statusCode(SC_OK);

        }

}
