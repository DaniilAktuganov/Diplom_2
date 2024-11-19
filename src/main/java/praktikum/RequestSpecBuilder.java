package praktikum;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

public class RequestSpecBuilder {
    private static final String BASE_URI = "https://stellarburgers.nomoreparties.site/";

    public static RequestSpecification getRequestSpec() {
        return RestAssured.given()
                .baseUri(BASE_URI);
    }
}