package orders.tests;

import io.qameta.allure.Description;
import org.junit.jupiter.api.DisplayName;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import clients.OrdersClient;
import constants.ScooterColor;
import models.orders.OrderRequest;
import models.orders.OrderResponse;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class CreateOrderTests {

    static Stream<List<ScooterColor>> colorProvider() {
        return Stream.of(
                null,
                List.of(ScooterColor.BLACK, ScooterColor.GRAY),
                List.of(ScooterColor.BLACK),
                List.of(ScooterColor.GRAY)
        );
    }

    @ParameterizedTest
    @MethodSource("colorProvider")
    @DisplayName("Проверка создания заказа")
    @Description("Позитивная проверка возможности создания заказа с разными цветами самоката")
    public void orderCanBeCreatedSuccessfullyTest(List<String> color) {
        OrderRequest orderRequest = getOrderWithCustomColor(color);
        OrdersClient client = new OrdersClient();

        Response orderResponse = client.create(orderRequest);
        assertEquals(HttpStatus.SC_CREATED, orderResponse.statusCode(), "Неверный статус-код");
        OrderResponse responseBody = orderResponse.as(OrderResponse.class);
        assertNotEquals(0, responseBody.getTrack(), "track не должен быть 0");
    }

    private OrderRequest getOrderWithCustomColor(List<String> color) {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setFirstName("Уасся");
        orderRequest.setLastName("Пупкин");
        orderRequest.setAddress("Отрадная д.10");
        orderRequest.setMetroStation(6);
        orderRequest.setPhone("+7 999 888 77 66");
        orderRequest.setRentTime(4);
        orderRequest.setDeliveryDate(getDateInDaysFromNow(2));
        orderRequest.setComment("Быстро!");
        orderRequest.setColor(color);

        return orderRequest;
    }

    private String getDateInDaysFromNow(int daysToAdd) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.now().plusDays(daysToAdd).format(formatter);
    }
}

