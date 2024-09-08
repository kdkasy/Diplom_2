import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserCredentials {
    private String email;
    private String password;

    public static UserCredentials from (User user) {
        return new UserCredentials(user.getEmail(), user.getPassword());
    }
}