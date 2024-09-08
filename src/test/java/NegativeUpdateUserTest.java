import com.google.gson.Gson;
import io.qameta.allure.Description;
import io.restassured.http.Header;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class NegativeUpdateUserTest {
    private UserClient userClient;
    private User user;
    private String accessToken;

    @Before
    public void createUser() {
        userClient = new UserClient();
        user = UserGenerator.getUser();

        System.out.println(user.getPassword());
        System.out.println(user.getEmail());

        ValidatableResponse createResponse = userClient.createUser(user);
        assertEquals(HttpStatus.SC_OK, createResponse.extract().statusCode());
        accessToken = createResponse.extract().path("accessToken");
        assertNotEquals(null, accessToken);
    }

    @After
    public void deleteUser() {
        ValidatableResponse deleteResponse = userClient.deleteUser(accessToken);
        assertEquals(HttpStatus.SC_ACCEPTED, deleteResponse.extract().statusCode());
    }

    @Test
    @Description("Test fail to update data without authorization ")
    public void updateUserWithoutAuthorization() {
        user.setName("Name");
        Header authHeader = new Header("authorization", "");
        Gson gson = new Gson(); // Or use new GsonBuilder().create();
        String jsonUser = gson.toJson(user);
        ValidatableResponse updateResponse = userClient.updateUser(authHeader, jsonUser);
        assertEquals(HttpStatus.SC_UNAUTHORIZED, updateResponse.extract().statusCode());
        assertFalse(updateResponse.extract().path("success"));
        assertEquals("You should be authorised", updateResponse.extract().path("message"));
    }

    @Test
    @Description("Test update data with authorization ")
    public void updateUserWithUsedEmail() {
        User existedUser = UserGenerator.getUser();

        ValidatableResponse createResponse = userClient.createUser(existedUser);
        assertEquals(HttpStatus.SC_OK, createResponse.extract().statusCode());
        String existedUserAccessToken = createResponse.extract().path("accessToken");
        assertNotEquals(null, existedUserAccessToken);

        user.setEmail(existedUser.getEmail());
        Header authHeader = new Header("authorization", accessToken);

        Gson gson = new Gson(); // Or use new GsonBuilder().create();
        String jsonUser = gson.toJson(user);
        ValidatableResponse updateResponse = userClient.updateUser(authHeader, jsonUser);

        assertEquals(HttpStatus.SC_FORBIDDEN, updateResponse.extract().statusCode());
        assertFalse(updateResponse.extract().path("success"));
        assertEquals("User with such email already exists", updateResponse.extract().path("message"));
        
        ValidatableResponse deleteResponse = userClient.deleteUser(existedUserAccessToken);
        assertEquals(HttpStatus.SC_ACCEPTED, deleteResponse.extract().statusCode());
    }
}
