package model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

// класс логина курьера
@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CourierLoginData {
    // логин курьера
    private String login;
    // пароль курьера
    private String password;

    public CourierLoginData(CourierCreateData courierCreateData) {
        this.login = courierCreateData.getLogin();
        this.password = courierCreateData.getPassword();
    }

}
