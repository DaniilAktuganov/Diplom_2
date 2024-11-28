package praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.models.User;
import praktikum.client.UserClient;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.*;
import static praktikum.generators.UserGenerator.*;


public class CreateUserTest {

    private UserClient userClient;
    private User user;
    private Response response;

    @Before
    public void setUp() {
        userClient = new UserClient();
    }

    @Test
    @DisplayName("Успешное создание пользователя")
    @Description("Проверка, что пользователя можно создать с корректными данными")
    public void createUserTest() {
        user = createDefaultUser();
        response = userClient.sendPostRequestAuthRegister(user);
        response.then()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("user.email", equalTo(user.getEmail()))
                .body("user.name", equalTo(user.getName()))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue());
    }

    @Test
    @DisplayName("Проверка создания пользователя, который уже зарегистрирован")
    @Description("Невозможно создать пользователя, который уже зарегистрирован")
    public void createTwoIdenticalUsersTest() {
        user = createDefaultUser();
        response = userClient.sendPostRequestAuthRegister(user);
        Response duplicateResponse = userClient.sendPostRequestAuthRegister(user);
        duplicateResponse.then()
                .statusCode(SC_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("User already exists"));
    }
    @Test
    @DisplayName("Проверка создания пользователя без email")
    @Description("Невозможно создать пользователя без email")
    public void createUserWithoutEmailTest() {
        user = createDefaultUserWithoutEmail();
        response = userClient.sendPostRequestAuthRegister(user);
        response.then()
                .statusCode(SC_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Проверка создания пользователя без пароля")
    @Description("Невозможно создать пользователя без пароля")
    public void createUserWithoutPasswordTest() {
        user = createDefaultUserWithoutPassword();
        response = userClient.sendPostRequestAuthRegister(user);
        response.then()
                .statusCode(SC_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Проверка создания пользователя без имени")
    @Description("Невозможно создать пользователя без имени")
    public void createUserWithoutNameTest() {
        user = createDefaultUserWithoutName();
        response = userClient.sendPostRequestAuthRegister(user);
        response.then()
                .statusCode(SC_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @After
    public void tearDown() {
        String accessToken = userClient.getAccessToken(response);
        if (accessToken != null) {
            userClient.sendDeleteRequestAuthUser(accessToken);
        }
    }
}