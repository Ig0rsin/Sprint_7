package courier.login;

import org.apache.http.HttpStatus;
import base.LoginCourierTestBase;
import io.qameta.allure.Description;
import org.junit.jupiter.api.DisplayName;
import io.restassured.response.Response;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import constants.UserData;
import models.courier.CourierLoginRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LoginCourierMandatoryFieldsTests extends LoginCourierTestBase {

    static class TestCase {
        String login;
        String password;
        int expectedStatusCode;

        TestCase(String login, String password, int expectedStatusCode) {
            this.login = login;
            this.password = password;
            this.expectedStatusCode = expectedStatusCode;
        }
    }

    public static java.util.stream.Stream<TestCase> getLoginCourierData() {
        return java.util.stream.Stream.of(
                new TestCase(null, null, HttpStatus.SC_BAD_REQUEST),
                new TestCase(UserData.LOGIN, null, HttpStatus.SC_BAD_REQUEST),
                new TestCase(null, UserData.PASSWORD, HttpStatus.SC_BAD_REQUEST),
                new TestCase(UserData.LOGIN, UserData.PASSWORD_UPDATED, HttpStatus.SC_NOT_FOUND),
                new TestCase(UserData.FIRST_NAME, UserData.PASSWORD, HttpStatus.SC_NOT_FOUND)
        );
    }

    @ParameterizedTest
    @MethodSource("getLoginCourierData")
    @DisplayName("Проверка создания логина курьера без обязательных полей")
    @Description("Проверка невозможности логина курьера без обязательных полей")
    public void allFieldsShouldBeFilledInToLoginCourierTest(TestCase testCase) {
        CourierLoginRequest courierLoginInvalid = new CourierLoginRequest(testCase.login, testCase.password);
        Response courierLoginResponse = this.client.login(courierLoginInvalid);
        assertEquals(testCase.expectedStatusCode, courierLoginResponse.statusCode(), "Неверный статус-код");
    }
}

