package praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.client.UserClient;
import praktikum.generators.OrderGenerator;
import praktikum.models.Order;
import praktikum.models.User;
import praktikum.order.OrderClient;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.Matchers.*;
import static praktikum.generators.UserGenerator.createDefaultUser;

public class GetUserOrdersTest {

    private UserClient userClient;
    private OrderClient orderClient;
    private OrderGenerator orderGenerator;
    private User user;
    private Response response;
    private String accessToken;
    private Response orderResponse;

    @Before
    public void setUp() {
        userClient = new UserClient();
        orderClient = new OrderClient();
        user = createDefaultUser();
        userClient.sendPostRequestAuthRegister(user);
        Response loginResponse = userClient.sendPostRequestAuthLogin(user);
        accessToken = userClient.getAccessToken(loginResponse);
        Order orderIngredients = orderGenerator.createOrder();
        orderResponse = orderClient.sendPostRequestOrdersWithLogin(accessToken, orderIngredients);
    }

    @Test
    @DisplayName("Успешное получение заказов пользователя")
    @Description("Проверка, что заказы авторизованного пользователя можно получить")
    public void getOrdersTest() {
        response = orderClient.sendGetRequestOrdersWithLogin(accessToken);
        String status = orderClient.getStatus(orderResponse);
        int total = orderClient.countingTotal(response);
        int totalToday = orderClient.countingTotalToday(response);
        response.then()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("orders.ingredients[0]", is(not(empty())))
                .body("orders.id[0]", is(not(empty())))
                .body("orders.status[0]", equalTo(status))
                .body("orders.number[0]", is(not(empty())))
                .body("orders.createdAt[0]", is(not(empty())))
                .body("orders.updateAt[0]", is(not(empty())))
                .body("total", equalTo(total))
                .body("totalToday", equalTo(totalToday));
    }

    @Test
    @DisplayName("Проверка получения заказов неавторизованного пользователя")
    @Description("Получить заказы неавторизованного пользователя невозможно")
    public void getOrdersWithoutLoginTest() {
        response = orderClient.sendGetRequestOrdersWithoutLogin();
        response.then()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userClient.sendDeleteRequestAuthUser(accessToken);
        }
    }
}