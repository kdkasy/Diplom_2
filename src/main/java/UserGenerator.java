import com.github.javafaker.Faker;

public class UserGenerator {

    public static User getUser() {
        Faker faker = new Faker();
        return new User(faker.name().firstName(), faker.internet().emailAddress(), faker.internet().password());
    }
}
