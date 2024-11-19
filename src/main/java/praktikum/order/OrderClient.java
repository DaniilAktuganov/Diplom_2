package praktikum.order;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import praktikum.RequestSpecBuilder;
import praktikum.models.Order;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class OrderClient {

    private static final String CREATE_ORDER_URL = "/api/orders";
    private static final String GET_ORDERS_URL = "/api/orders";

    @Step("Создание заказа")
    public Response sendPostRequestOrdersWithLogin(String accessToken, Order ingredients) {
        return given(RequestSpecBuilder.getRequestSpec())
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .body(ingredients)
                .post(CREATE_ORDER_URL)
                .then()
                .extract()
                .response();
    }

    @Step("Создание заказа без авторизации")
    public Response sendPostRequestOrdersWithoutLogin(Order ingredients) {
        return given(RequestSpecBuilder.getRequestSpec())
                .header("Content-type", "application/json")
                .body(ingredients)
                .post(CREATE_ORDER_URL);
    }

    @Step("Получение заказов без авторизацией")
    public Response sendGetRequestOrdersWithoutLogin() {
        return given(RequestSpecBuilder.getRequestSpec())
                .get(GET_ORDERS_URL);
    }

    @Step("Получение заказов с авторизацией")
    public Response sendGetRequestOrdersWithLogin(String accessToken) {
        return given(RequestSpecBuilder.getRequestSpec())
                .header("Authorization", accessToken)
                .get(GET_ORDERS_URL);
    }

    @Step("Подсчет количества заказов пользователя")
    public int countingTotal(Response response) {
        List<?> orders = response.jsonPath().getList("orders");
        return (int)orders.stream()
                .filter(order -> order instanceof java.util.Map)
                .map(order -> (String) ((java.util.Map<?, ?>) order).get("_id"))
                .filter(id -> id != null && !id.isEmpty())
                .distinct()
                .count();
    }

    @Step("Подсчет количества заказов пользователя за сегодня")
    public int countingTotalToday(Response response) {
        LocalDate today = LocalDate.now();
        String todayString = today.format(DateTimeFormatter.ISO_LOCAL_DATE);
        List<?> orders = response.jsonPath().getList("orders");
        return (int) orders.stream()
                .map(order -> ((Map<String, String>) order).get("createdAt"))
                .filter(orderDate -> orderDate != null && orderDate.startsWith(todayString))
                .count();
    }

    @Step("Получение статуса заказа")
    public String getStatus(Response response) {
        String status = response.jsonPath().getString("order.status");
        return status;
    }
}