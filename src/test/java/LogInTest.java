import io.qameta.allure.Description;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotEquals;

public class LogInTest {
    private UserClient userClient;
    private User user;
    private String accessToken;

    @Before
    public void createUser() {
        userClient = new UserClient();
        user = UserGenerator.getUser();
        accessToken = userClient.createTestUser(user);
    }

    @After
    public void deleteUser() {
        userClient.deleteTestUser(accessToken);
    }

    @Test
    @Description("Successful Log In test")
    public void successfulLogInTest() {
        ValidatableResponse logInResponse = userClient.logIn(user);

        assertEquals(HttpStatus.SC_OK, logInResponse.extract().statusCode());
        assertTrue(logInResponse.extract().path("success"));
        assertEquals(accessToken, logInResponse.extract().path("accessToken"));
        assertNotEquals(null, logInResponse.extract().path("refreshToken"));
        assertEquals(user.getEmail(), logInResponse.extract().path("user.email"));
        assertEquals(user.getName(), logInResponse.extract().path("user.name"));
    }
}
