import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;

import static io.restassured.RestAssured.given;

public class IngredientsClient {
    private static final String INGREDIENTS_PATH = "https://stellarburgers.nomoreparties.site/api/ingredients";
    public ValidatableResponse getIngredients() {
        return given()
                .spec(RestClient.getBaseSpec())
                .get(INGREDIENTS_PATH)
                .then()
                .statusCode(HttpStatus.SC_OK);
    }
}
