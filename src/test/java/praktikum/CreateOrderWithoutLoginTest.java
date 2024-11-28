package praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import praktikum.client.UserClient;
import praktikum.generators.OrderGenerator;
import praktikum.models.Order;
import praktikum.models.User;
import praktikum.order.OrderClient;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static praktikum.generators.UserGenerator.createDefaultUser;

@RunWith(MockitoJUnitRunner.class)
public class CreateOrderWithoutLoginTest {

    @Mock
    private UserClient userClient;

    @Mock
    private OrderClient orderClient;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    @Description("Заказ невозможно создать без авторизации")
    public void createOrderWithoutLoginTest() {
        User user = createDefaultUser();
        userClient.sendPostRequestAuthRegister(user);
        Order orderIngredients = OrderGenerator.createOrder();
        orderClient.sendPostRequestOrdersWithoutLogin(orderIngredients);
        verifyUserLoginInvocation(user);
    }

    private void verifyUserLoginInvocation(User user) {
        verify(userClient, times(1)).sendPostRequestAuthLogin(user);
    }
}