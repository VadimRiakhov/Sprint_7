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

public class CourierCreateTest {

    private CourierApi courierApi;
    private CourierCreateData courierCreateData;
    private final String login = "John22";
    private final String password = "1234";
    private final String firstName = "John";

    @Before
    public void setUp(){
        courierApi = new CourierApi();
    }

    // удаление созданного курьера
    @After
    public void deleteCourier(){
        CourierLoginData courierLoginData = new CourierLoginData(courierCreateData);
        ValidatableResponse response = courierApi.loginCourier(courierLoginData);
        // если логин был успешным, то удаляем курьера
        if(response.extract().statusCode()==200){
            String courierId = response
                    .extract().path("id").toString();
            courierApi.deleteCourier(courierId);
        }
    }

    // создание курьера с валидными данными
    @Test
    @DisplayName("Create courier with valid credentials")
    public void createCourierValidCredentialsTrue() {
        courierCreateData = new CourierCreateData(login, password, firstName);
        ValidatableResponse response = courierApi.createCourier(courierCreateData);
        checkResponseForCourierCreateValidCredentials(response);
    }

    // создание курьера с существующим логином
    @Test
    @DisplayName("Create courier with existing login")
    public void createCourierExistingLoginConflict(){
        courierCreateData = new CourierCreateData(login, password, firstName);
        courierApi.createCourier(courierCreateData);
        ValidatableResponse response = courierApi.createCourier(courierCreateData);
        checkResponseForCourierCreateExistingCourier(response);
    }

    // создание курьера с пустым полем login в запросе
    @Test
    @DisplayName("Create courier with empty login")
    public void createCourierWithoutLoginBadRequest(){
        courierCreateData = new CourierCreateData();
        courierCreateData.setPassword(password);
        ValidatableResponse response = courierApi.createCourier(courierCreateData);
        checkResponseForCourierCreateWithoutMandatoryField(response);
    }

    // создание курьера с пустым полем password в запросе
    @Test
    @DisplayName("Create courier with empty password")
    public void createCourierWithoutPasswordBadRequest(){
        courierCreateData = new CourierCreateData();
        courierCreateData.setLogin(login);
        ValidatableResponse response = courierApi.createCourier(courierCreateData);
        checkResponseForCourierCreateWithoutMandatoryField(response);
    }

    // проверка статуса и тела ответа при создании курьера с валидными данными
    @Step("Check status code and response body for request with valid credentials")
    public void checkResponseForCourierCreateValidCredentials(ValidatableResponse response){
                response.log().all()
                        .assertThat()
                        .statusCode(HttpStatus.SC_CREATED)
                        .and()
                        .body("ok", is(true));
    }

    // проверка статуса и тела ответа при создании курьера с существующим логином
    @Step("Check status code and response body for request with existing login")
    public void checkResponseForCourierCreateExistingCourier(ValidatableResponse response){
        response.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_CONFLICT)
                .and()
                .body("message", equalTo("Этот логин уже используется."));
    }

    // проверка статуса и тела ответа при создании курьера без обязательного поля
    @Step("Check status code and response body for request without mandatory field")
    public void checkResponseForCourierCreateWithoutMandatoryField(ValidatableResponse response){
        response.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .and()
                .body("message", equalTo( "Недостаточно данных для создания учетной записи"));
    }
}
