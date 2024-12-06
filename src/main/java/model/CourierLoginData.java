package model;

import com.fasterxml.jackson.annotation.JsonInclude;

// класс логина курьера
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CourierLoginData {
    // логин курьера
    private String login;
    // пароль курьера
    private String password;

    public CourierLoginData(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public CourierLoginData(CourierCreateData courierCreateData) {
        this.login = courierCreateData.getLogin();
        this.password = courierCreateData.getPassword();
    }

    public CourierLoginData() {
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
