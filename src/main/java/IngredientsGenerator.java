import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class IngredientsGenerator {
    private static final int NUMBER_OF_INGREDIENTS = 4;

    public static Ingredients generateIngredients() {
        IngredientsClient ingredientsClient = new IngredientsClient();
        List<String> allIngredients = ingredientsClient.getIngredients().extract().path("data._id");
        Random random = new Random();
        List<String> randomIngredients = new ArrayList<>(NUMBER_OF_INGREDIENTS);

        for (int i = 0; i < NUMBER_OF_INGREDIENTS; i++) {
            int randomIndex = random.nextInt(allIngredients.size());
            randomIngredients.add(allIngredients.get(randomIndex));
            allIngredients.remove(randomIndex);
        }
        return new Ingredients(randomIngredients);
    }
}
