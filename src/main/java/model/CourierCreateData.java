package model;

import com.fasterxml.jackson.annotation.JsonInclude;

// класс создания нового курьера
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CourierCreateData {

    // логин курьера
    private String login;
    // пароль курьера
    private String password;
    // имя курьера
    private String firstName;

    public CourierCreateData(String login, String password, String firstName) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
    }

    public CourierCreateData(String login, String password) {
        this.login = login;
        this.password = password;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public CourierCreateData() {
    }
}
