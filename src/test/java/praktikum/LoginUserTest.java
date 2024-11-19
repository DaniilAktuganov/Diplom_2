package praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.models.User;
import praktikum.client.UserClient;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static praktikum.generators.UserGenerator.*;

public class LoginUserTest {

    private User user;
    private UserClient userClient;
    private Response response;

    @Before
    public void setUp() {
        userClient = new UserClient();
    }

    @Test
    @DisplayName("Успешная авторизация")
    @Description("Проверка, что пользователь может войти в систему с корректными данными")
    public void loginUserTest() {
        user = createDefaultUser();
        response = userClient.sendPostRequestAuthRegister(user);
        Response loginResponse = userClient.sendPostRequestAuthLogin(user);
        loginResponse.then()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue())
                .body("user.email", equalTo(user.getEmail()))
                .body("user.name", equalTo(user.getName()));
    }

    @Test
    @DisplayName("Авторизация с неправильным email")
    @Description("Невозможно авторизоваться с неправильным email")
    public void loginUserWithInvalidEmailTest() {
        user = createDefaultUser();
        response = userClient.sendPostRequestAuthRegister(user);
        Response loginResponse = userClient.sendPostRequestAuthLoginWithInvalidEmail(user);
        loginResponse.then()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Авторизация с неправильным паролем")
    @Description("Невозможно авторизоваться с неправильным паролем")
    public void loginUserWithInvalidPasswordTest() {
        user = createDefaultUser();
        response = userClient.sendPostRequestAuthRegister(user);
        Response loginResponse = userClient.sendPostRequestAuthLoginWithInvalidPassword(user);
        loginResponse.then()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @After
    public void tearDown() {
        String accessToken = userClient.getAccessToken(response);
        if (accessToken != null) {
            userClient.sendDeleteRequestAuthUser(accessToken);
        }
    }
}