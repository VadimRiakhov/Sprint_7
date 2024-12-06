import api.CourierApi;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import model.CourierCreateData;
import model.CourierLoginData;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.*;

public class CourierLoginTest {

    private CourierApi courierApi;
    private CourierCreateData courierCreateData;
    private CourierLoginData courierLoginData;

    @Before
    public void setUp() {
        String login = "John22";
        String password = "1234";
        String firstName = "John";
        courierApi = new CourierApi();
        // создаем объект класса создания курьера
        courierCreateData = new CourierCreateData(login, password, firstName);
        // создаем курьера
        courierApi.createCourier(courierCreateData);
        // создаем объект класса логина курьера
        courierLoginData = new CourierLoginData(courierCreateData);
    }

    // удаление созданного курьера
    @After
    public void deleteCourier(){

        ValidatableResponse response = courierApi.loginCourier(courierLoginData);
        // если логин был успешным, то удаляем курьера
        if(response.extract().statusCode()==200){
            String courierId = response
                    .extract().path("id").toString();
            courierApi.deleteCourier(courierId);
        }
    }
    // логин курьера с существующими учетными данными
    @Test
    @DisplayName("Login courier with existing credentials")
    public void courierLoginExistingCredentialsOk() {
        ValidatableResponse response = courierApi.loginCourier(courierLoginData);
        checkResponseForCourierLoginExistingCredentials(response);
    }
    // логин курьера без поля login
    @Test
    @DisplayName("Login courier without field login")
    public void courierLoginWithoutLoginResponseBadRequest(){
        CourierLoginData copyOfCourierLoginData = new CourierLoginData();
        copyOfCourierLoginData.setPassword(courierLoginData.getPassword());
        ValidatableResponse response = courierApi.loginCourier(copyOfCourierLoginData);
        checkResponseForCourierLoginWithoutMandatoryField(response);
    }

    // логин курьера без поля password
    @Test
    @DisplayName("Login courier without field password")
    public void courierLoginWithoutPasswordResponseBadRequest(){
        CourierLoginData copyOfCourierLoginData = new CourierLoginData();
        copyOfCourierLoginData.setLogin(courierLoginData.getLogin());
        ValidatableResponse response = courierApi.loginCourier(copyOfCourierLoginData);
        checkResponseForCourierLoginWithoutMandatoryField(response);
    }

    // логин курьера с неправильным логином
    @Test
    @DisplayName("Login courier with wrong login")
    public void courierLoginWrongLoginResponseNotFound(){
        String wrongLogin = "Johnny";
        CourierLoginData copyOfCourierLoginData = new CourierLoginData(courierCreateData);
        copyOfCourierLoginData.setLogin(wrongLogin);
        ValidatableResponse response = courierApi.loginCourier(copyOfCourierLoginData);
        checkResponseForCourierLoginWithWrongCredentials(response);
    }

    // логин курьера с неправильным паролем
    @Test
    @DisplayName("Login courier with wrong password")
    public void courierLoginWrongPasswordResponseNotFound(){
        String wrongPassword = "12345";
        CourierLoginData copyOfCourierLoginData = new CourierLoginData(courierCreateData);
        copyOfCourierLoginData.setPassword(wrongPassword);
        ValidatableResponse response = courierApi.loginCourier(copyOfCourierLoginData);
        checkResponseForCourierLoginWithWrongCredentials(response);
    }

    // проверка статуса и тела ответа при логине курьера с существующими данными
    @Step("Check status code and response body for request with existing credentials")
    public void checkResponseForCourierLoginExistingCredentials(ValidatableResponse response){
        response.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body(containsString("id"));
    }

    // проверка статуса и тела ответа при логине с неправильным логином или паролем
    @Step("Check status code and response body for request with wrong credentials")
    public void checkResponseForCourierLoginWithWrongCredentials(ValidatableResponse response){
        response.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .and()
                .body("message", equalTo("Учетная запись не найдена"));
    }

    // проверка статуса и тела ответа при логине без обязательного поля
    @Step("Check status code and response body for request without mandatory field")
    public void checkResponseForCourierLoginWithoutMandatoryField(ValidatableResponse response){
        response.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .and()
                .body("message", equalTo( "Недостаточно данных для входа"));
    }
}
