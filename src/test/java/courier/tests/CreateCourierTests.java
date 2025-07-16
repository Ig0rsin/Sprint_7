package courier.tests;

import constants.ErrorMessages;
import constants.UserData;
import models.courier.CourierRequest;
import models.courier.CourierResponse;
import models.errors.ErrorResponse;
import org.apache.http.HttpStatus;
import base.TestBase;
import io.qameta.allure.Description;
import org.junit.jupiter.api.DisplayName;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CreateCourierTests extends TestBase {

    @Test
    @DisplayName("Проверка создания курьера")
    @Description("Позитивная проверка возможности создания курьера")
    public void courierCanBeCreatedSuccessfullyTest() {
        Response courierResponse = this.client.create(this.courier);
        assertEquals(HttpStatus.SC_CREATED, courierResponse.statusCode(), "Неверный статус-код");
        assertTrue(courierResponse.as(CourierResponse.class).isOk(), "Неверное значение поля 'ok'");
    }

    @Test
    @DisplayName("Проверка невозможности создания 2х одинаковых курьеров")
    @Description("Проверка невозможности создания 2х курьеров с одинаковыми данными")
    public void unableToCreateTwoSameCouriersTest() {
        Response courierResponse = this.client.create(this.courier);
        assertEquals(HttpStatus.SC_CREATED, courierResponse.statusCode(), "Неверный статус-код при создании первого курьера");

        Response sameCourierResponse = this.client.create(this.courier);
        assertEquals(HttpStatus.SC_CONFLICT, sameCourierResponse.statusCode(), "Неверный статус-код при попытке создать дублирующего курьера");
        assertEquals(ErrorMessages.CREATE_ACCOUNT_ALREADY_USED, sameCourierResponse.as(ErrorResponse.class).getMessage(), "Неверное сообщение об ошибке");
    }

    @Test
    @DisplayName("Проверка невозможности создания курьеров с одинаковым логином")
    @Description("Проверка невозможности создания 2х курьеров с одинаковым логином")
    public void unableToCreateCourierWithSameLoginTest() {
        Response courierResponse = this.client.create(this.courier);
        assertEquals(HttpStatus.SC_CREATED, courierResponse.statusCode(), "Неверный статус-код при создании первого курьера");

        CourierRequest sameLoginCourier = new CourierRequest(UserData.LOGIN, UserData.PASSWORD_UPDATED, UserData.FIRST_NAME_UPDATED);
        Response sameLoginCourierResponse = this.client.create(sameLoginCourier);
        assertEquals(HttpStatus.SC_CONFLICT, sameLoginCourierResponse.statusCode(), "Неверный статус-код при создании курьера с существующим логином");
        assertEquals(ErrorMessages.CREATE_ACCOUNT_ALREADY_USED, sameLoginCourierResponse.as(ErrorResponse.class).getMessage(), "Неверное сообщение об ошибке");
    }

}

