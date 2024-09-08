import com.google.gson.Gson;
import io.qameta.allure.Description;
import io.restassured.http.Header;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class SuccessfulUpdateUserTest {
    private final String name;
    private final String email;
    private final String password;

    private UserClient userClient;
    private User user;
    private String accessToken;


    public SuccessfulUpdateUserTest(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    @Parameterized.Parameters
    public static Object[][] getCredentials() {
        return new Object[][]{
                {"Name", null, null},
                {null, "newemail1@mail.ru", null},
                {null, null, "newpassword"},
                {"Name", "newemail2@mail.ru", "newpassword"}
        };
    }

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
    @Description("Successful update user fields test")
    public void successfulUpdateUserFieldsTest() {
        User updateUser = new User(name, email, password);

        Gson gson = new Gson();
        String jsonUser = gson.toJson(updateUser);
        ValidatableResponse updateResponse = userClient.updateUser(new Header("authorization", accessToken), jsonUser);
        assertTrue(updateResponse.extract().path("success"));
        if (email != null) {
            assertEquals( email, updateResponse.extract().path("user.email"));
        } else {
            assertEquals(user.getEmail(), updateResponse.extract().path("user.email"));
        }
        if(name != null){
            assertEquals(name, updateResponse.extract().path("user.name"));
        }
        else {
            assertEquals(user.getName(), updateResponse.extract().path("user.name"));
        }
    }
}

