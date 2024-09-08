import io.qameta.allure.Description;
import io.restassured.http.Header;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.Test;
import java.util.Arrays;

import static org.junit.Assert.*;

public class GetOrdersFromUserTest {

    @Test
    @Description("Get orders from user without authorization")
    public void getOrdersFromUserWithoutAuthorization() {
        Header header = new Header("authorization", "");
        OrderClient orderClient = new OrderClient();
        ValidatableResponse getOrders = orderClient.getOrdersFromUser(header);
        assertEquals(HttpStatus.SC_UNAUTHORIZED, getOrders.extract().statusCode());
        assertFalse(getOrders.extract().path("success"));
        assertEquals("You should be authorised", getOrders.extract().path("message"));
    }

    @Test
    @Description("Get orders from user wit authorization")
    public void getOrdersFromUserWithAuthorization() {
        UserClient userClient = new UserClient();
        User user = UserGenerator.getUser();
        userClient.createTestUser(user);
        String accessToken = userClient.logInTestUser(user);

        Header header = new Header("authorization", accessToken);
        Ingredients ingredients = new Ingredients( Arrays.asList("61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa74"));
        OrderClient orderClient = new OrderClient();

        orderClient.createOrder(ingredients, header).statusCode(HttpStatus.SC_OK);

        ValidatableResponse getOrders = orderClient.getOrdersFromUser(header);

        assertEquals(HttpStatus.SC_OK, getOrders.extract().statusCode());
        UsersOrders usersOrders = getOrders.extract().body().as(UsersOrders.class);

        assertTrue(usersOrders.isSuccess());
        assertFalse(usersOrders.getOrders().isEmpty());

        userClient.deleteTestUser(accessToken);
    }
}
