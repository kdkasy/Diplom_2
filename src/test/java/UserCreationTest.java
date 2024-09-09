import io.qameta.allure.Description;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UserCreationTest {

    private UserClient userClient;
    private User user;
    private String token;

    @Before
    public void setUp(){
        userClient = new UserClient();
        user = UserGenerator.getUser();
    }

    @After
    public void cleanUp(){
        ValidatableResponse deleteResponse = userClient.deleteUser(token);
        assertEquals(HttpStatus.SC_ACCEPTED, deleteResponse.extract().statusCode());
    }

    @Test
    @Description("User can be created")
    public void uniqueUserCanBeCreated() {
        ValidatableResponse createResponse = userClient.createUser(user);

        assertEquals(HttpStatus.SC_OK, createResponse.extract().statusCode());
        assertTrue(createResponse.extract().path("success"));
        assertNotEquals(null, createResponse.extract().path("refreshToken"));
        assertEquals(user.getEmail(), createResponse.extract().path("user.email"));
        assertEquals(user.getName(), createResponse.extract().path("user.name"));
        token = createResponse.extract().path("accessToken");
        assertNotEquals(null, token);
    }

    @Test
    @Description("Could not create two identical users")
    public void couldNotCreateExistedUser() {
        ValidatableResponse createResponse = userClient.createUser(user);
        assertEquals(HttpStatus.SC_OK, createResponse.extract().statusCode());
        token = createResponse.extract().path("accessToken");
        assertNotEquals(null, token);

        ValidatableResponse createFailedResponse = userClient.createUser(user);

        assertEquals(HttpStatus.SC_FORBIDDEN, createFailedResponse.extract().statusCode());
        assertFalse(createFailedResponse.extract().path("success"));
        assertEquals("User already exists", createFailedResponse.extract().path("message"));
    }
}
