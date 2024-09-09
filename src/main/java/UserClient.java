import io.qameta.allure.Step;
import io.restassured.http.Header;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;

import static io.restassured.RestAssured.given;

public class UserClient {
    private static final String USER_PATH = "/api/auth/register";
    private static final String UPDATE_USER_PATH = "/api/auth/user";
    private static final String LOG_IN_PATH = "/api/auth/login";

    @Step("Get response of create user")
    public ValidatableResponse createUser(User courier) {
        return given()
                .spec(RestClient.getBaseSpec())
                .body(courier)
                .when()
                .post(USER_PATH)
                .then();
    }

    @Step("Create test user")
    public String createTestUser(User user) {//UserClient userClient, User user) {
        ValidatableResponse createResponse = this.createUser(user).statusCode(HttpStatus.SC_OK);
        String accessToken = createResponse.extract().path("accessToken");
        assert(accessToken != null);
        return accessToken;
    }

    @Step("Get response of log in")
    public ValidatableResponse logIn(User user){
                return given()
                .spec(RestClient.getBaseSpec())
                .body(UserCredentials.from(user))
                .when()
                .post(LOG_IN_PATH)
                .then();
    }

    @Step("Log in test user")
    public String logInTestUser (User user) { //(UserClient userClient, User user) {
        ValidatableResponse logInResponse = this.logIn(user).statusCode(HttpStatus.SC_OK);
        String accessToken = logInResponse.extract().path("accessToken");
        assert(accessToken != null);
        return accessToken;
    }

    @Step("Get response of delete user")
    public ValidatableResponse deleteUser(String token) {
        return given()
                .spec(RestClient.getBaseSpec())
                .header("authorization", token)
                .delete(UPDATE_USER_PATH)
                .then();
    }

    @Step("Delete test user")
    public void deleteTestUser(String token) {//UserClient userClient, User user) {
        this.deleteUser(token).statusCode(HttpStatus.SC_ACCEPTED);
    }



    @Step("Get response of update user information")
    public ValidatableResponse updateUser(Header header, String user) {
        return given()
                .spec(RestClient.getBaseSpec())
                .header(header)
                .body(user)
                .when()
                .patch(UPDATE_USER_PATH)
                .then();
    }
}
