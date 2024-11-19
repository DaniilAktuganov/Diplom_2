package praktikum.client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import praktikum.RequestSpecBuilder;
import praktikum.models.UpdatedUserCreds;
import praktikum.models.User;

import static io.restassured.RestAssured.given;
import static praktikum.models.UserCreds.*;

public class UserClient {
    private static final String CREATE_USER_URL = "/api/auth/register";
    private static final String LOGIN_USER_URL = "/api/auth/login";
    private static final String PATCH_USER_URL = "/api/auth/user";
    private static final String DELETE_USER_URL = "/api/auth/user";


    @Step("Создание пользователя")
    public Response sendPostRequestAuthRegister(User user) {
        return given(RequestSpecBuilder.getRequestSpec())
                .header("Content-type", "application/json")
                .body(user)
                .when()
                .post(CREATE_USER_URL);
    }

    @Step("Логин пользователя в системе")
    public Response sendPostRequestAuthLogin(User user) {
        return given(RequestSpecBuilder.getRequestSpec())
                .header("Content-type", "application/json")
                .body(credsFromUser(user))
                .when()
                .post(LOGIN_USER_URL);
    }

    @Step("Логин пользователя в системе с неверным email")
    public Response sendPostRequestAuthLoginWithInvalidEmail(User user) {
        return given(RequestSpecBuilder.getRequestSpec())
                .header("Content-type", "application/json")
                .body(credsFromUserWithInvalidEmail(user))
                .when()
                .post(LOGIN_USER_URL);
    }

    @Step("Логин пользователя в системе c неверным паролем")
    public Response sendPostRequestAuthLoginWithInvalidPassword(User user) {
        return given(RequestSpecBuilder.getRequestSpec())
                .header("Content-type", "application/json")
                .body(credsFromUserWithInvalidPassword(user))
                .when()
                .post(LOGIN_USER_URL);
    }

    @Step("Удаление пользователя")
    public Response sendDeleteRequestAuthUser(String accessToken) {
        return given(RequestSpecBuilder.getRequestSpec())
                .header("Authorization", accessToken)
                .when()
                .delete(DELETE_USER_URL);
    }

    @Step("Обновление email пользователя")
    public Response sendPatchRequestAuthUserWithNewEmail(String accessToken, User user) {
        return updateUser(accessToken, user, UpdatedUserCreds.updateUserEmail(user));
    }

    @Step("Обновление имени пользователя")
    public Response sendPatchRequestAuthUserWithNewName(String accessToken, User user) {
        return updateUser(accessToken, user, UpdatedUserCreds.updateUserName(user));
    }


    @Step("Обновление email пользователя без авторизации")
    public Response sendPatchRequestAuthUserWithNewEmailWithoutLogin(User user) {
        return updateUserWithoutLogin(user, UpdatedUserCreds.updateUserEmail(user));
    }

    @Step("Обновление имени пользователя без авторизации")
    public Response sendPatchRequestAuthUserWithNewNameWithoutLogin(User user) {
        return updateUserWithoutLogin(user, UpdatedUserCreds.updateUserName(user));
    }

    @Step("Обновление данных пользователя")
    private Response updateUser(String accessToken, User user, User updatedUser) {
        return given(RequestSpecBuilder.getRequestSpec())
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .body(updatedUser)
                .when()
                .patch(PATCH_USER_URL);
    }

    @Step("Обновление данных пользователя без авторизации")
    private Response updateUserWithoutLogin(User user, User updatedUser) {
        return given(RequestSpecBuilder.getRequestSpec())
                .header("Content-type", "application/json")
                .body(updatedUser)
                .when()
                .patch(PATCH_USER_URL);
    }

    @Step("Получение токена")
    public String getAccessToken(Response response) {
        return response.path("accessToken");
    }
}