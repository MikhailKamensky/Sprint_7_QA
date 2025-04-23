import io.restassured.RestAssured;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import praktikum.sprint7.clients.CourierClient;
import praktikum.sprint7.models.Courier;
import praktikum.sprint7.models.CourierLoginResponse;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static praktikum.sprint7.models.CourierCreds.credsFromCourier;

public class CourierCreateTests {
    private static final String BASE_URL = "https://qa-scooter.praktikum-services.ru";

    public static String login = "MKamen4";
    public static String password = "random123";
    public static String firstName = "Михаил";

    private CourierClient courierClient;
    private Courier courier;
    private int id;

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
        courierClient = new CourierClient();
        courier = new Courier(login, password, firstName);
    }

    @Test
    @Description("Создание курьера с валидными параметрами")
    public void createCourierTest() {
        Response response = courierClient.create(courier);

        response.then().statusCode(SC_CREATED).and().body("ok", equalTo(true));
    }

    @Test
    @Description("Создание двух курьеров с одинаковым логином")
    public void createIdenticalCourierTest() {
        Response response = courierClient.create(courier);
        assertEquals("Некорректный код ответа", SC_CREATED, response.statusCode());
        Courier identicalCourier = new Courier(login, password, firstName);
        Response identicalCourierResponse = courierClient.create(identicalCourier);
        assertEquals("Можно создать двух курьеров с одинаковым логинами", SC_CONFLICT, identicalCourierResponse.statusCode());

    }

    @Test
    @Description("Создание курьера без передачи логина")
    public void createlCourierWithoutLoginTest() {
        Courier courier = new Courier(null, password, firstName);
        Response response = courierClient.create(courier);
        assertEquals("Некорректный код ответа", SC_BAD_REQUEST, response.statusCode());
    }

    @Test
    @Description("Создание курьера без передачи пароля")
    public void createlCourierWithoutPasswordTest() {
        Courier courier = new Courier(login, null, firstName);
        Response response = courierClient.create(courier);
        assertEquals("Некорректный код ответа", SC_BAD_REQUEST, response.statusCode());
    }


    @After
    public void tearTest() {
        try {
            if (courier.getLogin() != null && courier.getPassword() != null) {
                Response loginResponse = courierClient.login(credsFromCourier(courier));
                if (loginResponse.statusCode() == SC_OK) {
                    id = loginResponse.as(CourierLoginResponse.class).getId();
                    courierClient.delete(id);
                }
            }
        } catch (Exception e) {
            System.out.println("Ошибка при удалении курьера: " + e.getMessage());
        }
    }
}