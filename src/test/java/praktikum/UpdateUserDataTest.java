package praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.client.UserClient;
import praktikum.models.UpdatedUserCreds;
import praktikum.models.User;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.Matchers.equalTo;
import static praktikum.generators.UserGenerator.createDefaultUser;

public class UpdateUserDataTest {

    private User user;
    private UserClient userClient;
    private String accessToken;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = createDefaultUser();
        userClient.sendPostRequestAuthRegister(user);
    }

    @Test
    @DisplayName("Успешное изменение email пользователя после авторизации")
    @Description("Проверка, что email пользователя можно изменить после авторизации")
    public void updateUserEmailAfterLoginTest() {
        Response loginResponse = userClient.sendPostRequestAuthLogin(user);
        accessToken = userClient.getAccessToken(loginResponse);
        User updatedUser = UpdatedUserCreds.updateUserEmail(user);
        Response response = userClient.sendPatchRequestAuthUserWithNewEmail(accessToken, updatedUser);
        response.then()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("user.email", equalTo(updatedUser.getEmail()))
                .body("user.name", equalTo(user.getName()));
    }

    @Test
    @DisplayName("Успешное изменение имени пользователя после авторизации")
    @Description("Проверка, что имя пользователя можно изменить после авторизации")
    public void updateUserNameAfterLoginTest() {
        Response loginResponse = userClient.sendPostRequestAuthLogin(user);
        accessToken = userClient.getAccessToken(loginResponse);
        User updatedUser = UpdatedUserCreds.updateUserName(user);
        Response response = userClient.sendPatchRequestAuthUserWithNewName(accessToken, updatedUser);
        response.then()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("user.email", equalTo(user.getEmail()))
                .body("user.name", equalTo(updatedUser.getName()));
    }

    @Test
    @DisplayName("Проверка изменения email пользователя без авторизации")
    @Description("Невозможно изменить email пользователя без авторизации")
    public void updateUserEmailWithoutLoginTest() {
        User updatedUser = UpdatedUserCreds.updateUserEmail(user);
        Response response = userClient.sendPatchRequestAuthUserWithNewEmailWithoutLogin(updatedUser);
        response.then()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }

    @Test
    @DisplayName("Проверка изменения имени пользователя без авторизации")
    @Description("Невозможно изменить имя пользователя без авторизации")
    public void updateUserNameWithoutLoginTest() {
        User updatedUser = UpdatedUserCreds.updateUserName(user);
        Response response = userClient.sendPatchRequestAuthUserWithNewNameWithoutLogin(updatedUser);
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