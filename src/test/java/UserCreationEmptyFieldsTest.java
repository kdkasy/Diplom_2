import io.qameta.allure.Description;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(Parameterized.class)
public class UserCreationEmptyFieldsTest {
    private final String login;
    private final String password;
    private final String name;

    public UserCreationEmptyFieldsTest(String login, String password, String name) {
        this.login = login;
        this.password = password;
        this.name = name;
    }

    @Parameterized.Parameters
    public static Object[][] getCredentials() {
        return new Object[][]{
                {"name", null, "123"},
                {"name", "", "123"},
                { "name", "123@yandex.ru", null},
                { "name", "123@yandex.ru", ""},
                {null, "123@yandex.ru", "123"},
                {"", "12123@yandex.ru3", "123"},
        };
    }

    @Test
    @Description("Create user with empty credentials fields: Check \"\" and null fields")
    public void emptyCredentialsTest() {
        UserClient courierClient = new UserClient();
        User user = new User(login, password, name);

        ValidatableResponse createResponse = courierClient.createUser(user);

        assertEquals(HttpStatus.SC_FORBIDDEN, createResponse.extract().statusCode());
        assertFalse(createResponse.extract().path("success"));
        assertEquals("Email, password and name are required fields", createResponse.extract().path("message"));
    }

}
