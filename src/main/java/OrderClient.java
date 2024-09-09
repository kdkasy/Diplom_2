import io.qameta.allure.Step;
import io.restassured.http.Header;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class OrderClient extends RestClient{
    private static final String ORDER_PATH = "https://stellarburgers.nomoreparties.site/api/orders";

    @Step("Create Order")
    public ValidatableResponse createOrder(Ingredients ingredients, Header header){
            return given()
                    .spec(RestClient.getBaseSpec())
                    .header(header)
                    .body(ingredients)
                    .when()
                    .post(ORDER_PATH)
                    .then();
    }

    @Step("Get Orders from user")
    public ValidatableResponse getOrdersFromUser(Header header) {
        return given()
                .spec(RestClient.getBaseSpec())
                .header(header)
                .get(ORDER_PATH)
                .then();
    }
}
