import io.qameta.allure.Description;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class LogInWithWrongCredentialsTest {
        private final String login;
        private final String password;
        private User user;
        private UserClient userClient;
        private String accessToken;
        private final static String USER_EMAIL = "kelley.west@yahoo.com";
        private final static String USER_PASSWORD = "6lr5ryjm4am";
        private final static String USER_NAME = "kelley";

        public LogInWithWrongCredentialsTest(String login, String password) {
            this.login = login;
            this.password = password;
        }

        @Parameterized.Parameters
        public static Object[][] getCredentials() {
            return new Object[][]{
                    {null, USER_PASSWORD},
                    {USER_EMAIL, null},
                    {"123" + USER_EMAIL, USER_PASSWORD},
                    {USER_EMAIL, "123" + USER_PASSWORD},
            };
        }

    @Before
    public void createUser() {
            userClient = new UserClient();
            user = new User(USER_NAME, USER_EMAIL, USER_PASSWORD);
            accessToken = userClient.createTestUser(user);
    }

    @After
    public void deleteUser() {
            userClient.deleteTestUser(accessToken);
    }

    @Test
    @Description("Log in with wrong credentials test")
    public void logInWithWrongCredentials() {
            user.setPassword(password);
            user.setEmail(login);
            ValidatableResponse logInResponse = userClient.logIn(user);

            assertEquals(HttpStatus.SC_UNAUTHORIZED, logInResponse.extract().statusCode());
            assertFalse(logInResponse.extract().path("success"));
            assertEquals("email or password are incorrect", logInResponse.extract().path("message"));
    }
}
