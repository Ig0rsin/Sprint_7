package courier.tests;

import io.qameta.allure.Description;
import org.junit.jupiter.api.DisplayName;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import clients.CourierClient;
import constants.UserData;
import models.courier.CourierRequest;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CourierMandatoryFieldsTests {

    static class TestData {
        String login;
        String password;
        String firstName;
        int expectedStatusCode;

        TestData(String login, String password, String firstName, int expectedStatusCode) {
            this.login = login;
            this.password = password;
            this.firstName = firstName;
            this.expectedStatusCode = expectedStatusCode;
        }
    }

    static Stream<TestData> getCourierData() {
        return Stream.of(
                new TestData(null, null, null, HttpStatus.SC_BAD_REQUEST),
                new TestData(UserData.LOGIN, null, null, HttpStatus.SC_BAD_REQUEST),
                new TestData(null, UserData.PASSWORD, null, HttpStatus.SC_BAD_REQUEST),
                new TestData(null, null, UserData.FIRST_NAME, HttpStatus.SC_BAD_REQUEST),
                new TestData(UserData.LOGIN, null, UserData.FIRST_NAME, HttpStatus.SC_BAD_REQUEST),
                new TestData(null, UserData.PASSWORD, UserData.FIRST_NAME, HttpStatus.SC_BAD_REQUEST)
        );
    }

    @ParameterizedTest
    @MethodSource("getCourierData")
    @DisplayName("Проверка создания курьера без обязательных полей")
    @Description("Проверка невозможности создания курьера без обязательных полей")
    public void allFieldsShouldBeFilledInToCreateCourierTest(TestData data) {
        CourierRequest courier = new CourierRequest(data.login, data.password, data.firstName);
        CourierClient client = new CourierClient();

        Response courierResponse = client.create(courier);
        assertEquals(data.expectedStatusCode, courierResponse.statusCode(), "Неверный статус-код");
    }

}

