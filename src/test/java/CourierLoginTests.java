import io.restassured.RestAssured;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import praktikum.sprint7.clients.CourierClient;
import praktikum.sprint7.models.Courier;
import praktikum.sprint7.models.CourierCreds;
import praktikum.sprint7.models.CourierLoginResponse;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static praktikum.sprint7.models.CourierCreds.credsFromCourier;

public class CourierLoginTests {
    private static final String BASE_URL = "https://qa-scooter.praktikum-services.ru";

    public static String login = "MKamen4";
    public static String password = "random123";
    public static String firstName = "Михаил";
    public static String invalidLogin = "qwerty";
    public static String invalidPassword = "asdfgh";
    public static String messageAuthorizationWithoutLoginOrPassword = "Недостаточно данных для входа";
    public static String messageNonexistetntCourierAuthorization = "Учетная запись не найдена";

    private CourierClient courierClient;
    private Courier courier;
    private int id;
    private Response validLoginResponse;

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
        courierClient = new CourierClient();
        courier = new Courier(login, password, firstName);
        courierClient.create(courier);
        validLoginResponse = courierClient.login(credsFromCourier(courier));
    }

    @Test
    @Description("Авторизация созданного курьера")
    public void loginCourierTest() {

        Response loginResponse = courierClient.login(credsFromCourier(courier));
        assertEquals("Некорректный код ответа при авторизации", SC_OK, loginResponse.statusCode());

    }

    @Test
    @Description("Попытка авторизации без логина")
    public void loginCourierWithoutLoginTest() {

        Response loginResponse = courierClient.login(new CourierCreds(null, password));
        loginResponse.then().body("message", equalTo(messageAuthorizationWithoutLoginOrPassword)).and().statusCode(SC_BAD_REQUEST);

    }

    @Test
    @Description("Попытка авторизации без логина")
    public void loginCourierWithoutPasswordTest() {


        Response loginResponse = courierClient.login(new CourierCreds(login, null));
        loginResponse.then().body("message", equalTo(messageAuthorizationWithoutLoginOrPassword)).and().statusCode(SC_BAD_REQUEST);


    }

    @Test
    @Description("Авторизация c неверным логином")
    public void loginWithInvalidLoginTest() {
        Response loginResponse = courierClient.login(new CourierCreds(invalidLogin, password));
        loginResponse.then().body("message", equalTo(messageNonexistetntCourierAuthorization)).and().statusCode(SC_NOT_FOUND);
    }

    @Test
    @Description("Авторизация c неверным паролем")
    public void loginWithInvalidPasswordTest() {
        Response loginResponse = courierClient.login(new CourierCreds(login, invalidPassword));
        loginResponse.then().body("message", equalTo(messageNonexistetntCourierAuthorization)).and().statusCode(SC_NOT_FOUND);
    }


    @After
    public void tearTest() {
        id = validLoginResponse.as(CourierLoginResponse.class).getId();
        courierClient.delete(id);
    }

}