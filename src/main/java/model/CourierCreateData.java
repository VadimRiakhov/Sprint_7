package model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

// класс создания нового курьера

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CourierCreateData {
    // логин курьера
    private String login;
    // пароль курьера
    private String password;
    // имя курьера
    private String firstName;

}
