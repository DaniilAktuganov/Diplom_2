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

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.*;
import static praktikum.generators.UserGenerator.createDefaultUser;

public class CreateOrderTest {

    private UserClient userClient;
    private OrderClient orderClient;
    private OrderGenerator orderGenerator;
    private User user;
    private Response response;
    private String accessToken;

    @Before
    public void setUp() {
        userClient = new UserClient();
        orderClient = new OrderClient();
        orderGenerator = new OrderGenerator();
        user = createDefaultUser();
        userClient.sendPostRequestAuthRegister(user);
        Response loginResponse = userClient.sendPostRequestAuthLogin(user);
        accessToken = userClient.getAccessToken(loginResponse);
    }

    @Test
    @DisplayName("Успешное создание заказа")
    @Description("Проверка, что заказ можно создать с корректными данными")
    public void createOrderTest() {
        Order orderIngredients = orderGenerator.createOrder();
        response = orderClient.sendPostRequestOrdersWithLogin(accessToken, orderIngredients);
        response.then()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("name", is(not(empty())))
                .body("order.number", notNullValue());
    }

    @Test
    @DisplayName("Проверка создания заказа без ингредиентов")
    @Description("Создать заказ без ингредиентов невозможно")
    public void createOrderWithoutIngredientsTest() {
        Order orderIngredients = orderGenerator.createOrderWithoutIngredients();
        response = orderClient.sendPostRequestOrdersWithLogin(accessToken, orderIngredients);
        response.then()
                .statusCode(SC_BAD_REQUEST)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Проверка создания заказа с неверным хешем ингредиентов")
    @Description("Создать заказ с неверным хешем ингредиентов невозможно")
    public void createOrderWithInvalidHashIngredientsTest() {
        Order orderIngredients = orderGenerator.createOrderWithInvalidHashIngredients();
        response = orderClient.sendPostRequestOrdersWithLogin(accessToken, orderIngredients);
        response.then()
                .statusCode(SC_INTERNAL_SERVER_ERROR);
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userClient.sendDeleteRequestAuthUser(accessToken);
        }
    }
}