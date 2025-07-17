package orders.tests;

import clients.OrdersClient;
import io.qameta.allure.Description;
import org.junit.jupiter.api.DisplayName;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

public class GetOrdersTests {

    @Test
    @DisplayName("Проверка получения списка заказов")
    @Description("Позитивная проверка возможности получить заполненный список заказов")
    public void getOrdersListTest() {
        OrdersClient client = new OrdersClient();
        Response ordersResponse = client.getList();

        // Проверка статус-кода
        assertEquals(HttpStatus.SC_OK, ordersResponse.statusCode(), "Неверный статус-код");

        // Проверка тела ответа с помощью Hamcrest матчеров
        ordersResponse
                .then()
                .assertThat()
                .body("orders", not(empty()))
                .body("orders.size()", greaterThan(0));
    }

}

