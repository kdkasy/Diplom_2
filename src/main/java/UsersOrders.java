import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UsersOrders {
        private boolean success;
        private List<Orders> orders;
        private String total;
        private String totalToday;
}
