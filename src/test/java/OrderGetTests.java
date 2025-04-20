import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.notNullValue;

public class OrderGetTests {
        private static final String BASE_URL = "https://qa-scooter.praktikum-services.ru";
        public static  final String ORDER_GET_LIST = "/api/v1/orders";

        @Test
        @DisplayName("Получение списка заказов")
        public void orderGetList() {
            given().log().all()
                    .baseUri(BASE_URL)
                    .get(ORDER_GET_LIST)
                    .then()
                    .assertThat().body("orders", notNullValue())
                    .and()
                    .statusCode(SC_OK);
        }

}
