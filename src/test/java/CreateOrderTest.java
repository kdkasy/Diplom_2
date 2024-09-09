import io.qameta.allure.Description;
import io.restassured.http.Header;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class CreateOrderTest {

    private final Ingredients ingredients = IngredientsGenerator.generateIngredients();
    private  final Ingredients emptyIngredients = new Ingredients();
    private  final Ingredients wrongHashIngredients = new Ingredients( Arrays.asList("61c0001bdaaa6d","61cdaaa74"));

    @Test
    @Description("Test order create with authorization")
    public void createOrderWithAuthorization() {
        UserClient userClient = new UserClient();
        User user = UserGenerator.getUser();

        userClient.createTestUser(user);
        String accessToken = userClient.logInTestUser(user);

        OrderClient orderClient = new OrderClient();
        Header header = new Header("authorization", accessToken);
        ValidatableResponse createOrder = orderClient.createOrder(ingredients, header);
        assertEquals(HttpStatus.SC_OK, createOrder.extract().statusCode());
        assertTrue(createOrder.extract().path("success"));
        assertNotNull(createOrder.extract().path("name"));
        assertNotNull(createOrder.extract().path("order.number"));
        assertNotNull(createOrder.extract().path("order.ingredients"));
        assertEquals(user.getName(), createOrder.extract().path("order.owner.name"));
        assertEquals(user.getEmail(), createOrder.extract().path("order.owner.email"));

        userClient.deleteTestUser(accessToken);
    }

    @Test
    @Description("Test order create without authorization")
    public void createOrderWithoutAuthorization() {
        OrderClient orderClient = new OrderClient();
        Header header = new Header("authorization", "");
        ValidatableResponse createOrder = orderClient.createOrder(ingredients, header);
        assertEquals(HttpStatus.SC_OK, createOrder.extract().statusCode());
        assertTrue(createOrder.extract().path("success"));
        assertNotNull(createOrder.extract().path("name"));
        assertNotNull(createOrder.extract().path("order.number"));
    }

    @Test
    @Description("Test order create without ingredients")
    public void createOrderWithEmptyIngredients() {
        OrderClient orderClient = new OrderClient();
        Header header = new Header("authorization", "");
        ValidatableResponse createOrder = orderClient.createOrder(emptyIngredients, header);
        assertEquals(HttpStatus.SC_BAD_REQUEST, createOrder.extract().statusCode());
        assertFalse(createOrder.extract().path("success"));
        assertEquals("Ingredient ids must be provided", createOrder.extract().path("message"));
    }

    @Test
    @Description("Test order create with wrong hash ingredients")
    public void createOrderWithWrongHashIngredients() {
        OrderClient orderClient = new OrderClient();
        Header header = new Header("authorization", "");
        ValidatableResponse createOrder = orderClient.createOrder(wrongHashIngredients, header);
        assertEquals(HttpStatus.SC_INTERNAL_SERVER_ERROR, createOrder.extract().statusCode());
    }
}
