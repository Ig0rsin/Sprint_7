package courier.login;

import org.apache.http.HttpStatus;
import base.LoginCourierTestBase;
import io.qameta.allure.Description;
import org.junit.jupiter.api.DisplayName;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import models.courier.CourierLoginResponse;

public class LoginCourierTest extends LoginCourierTestBase {

    @Test
    @DisplayName("Проверка логина курьера")
    @Description("Позитивная проверка возможности курьера залогиниться")
    public void courierCanLoginSuccessfullyTest() {
        Response courierLoginResponse = this.client.login(courierLogin);
        assertEquals(HttpStatus.SC_OK, courierLoginResponse.statusCode(), "Неверный статус-код");
        CourierLoginResponse loginResponse = courierLoginResponse.as(CourierLoginResponse.class);
        assertNotEquals(0, loginResponse.getId(), "ID не должен быть 0");
    }

}

