import api.CourierApi;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import model.CourierCreateData;
import model.CourierGeneratorData;
import model.CourierLoginData;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CourierLoginTest {

    private CourierApi courierApi;
    private CourierCreateData courierCreateData;
    private CourierLoginData courierLoginData;

    @Before
    public void setUp() {
        courierApi = new CourierApi();
        // создаем объект класса создания курьера
        courierCreateData = CourierGeneratorData.getRandomCourier();
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
        if(response.extract().statusCode()==HttpStatus.SC_OK){
            String courierId = response
                    .extract().path("id").toString();
            courierApi.deleteCourier(courierId);
        }
    }
    // логин курьера с существующими учетными данными
    @Test
    @DisplayName("Login courier with existing credentials")
    @Description("Positive test for courier Login with valid credentials should response Ok")
    public void courierLoginExistingCredentialsOkTest() {
        ValidatableResponse response = courierApi.loginCourier(courierLoginData);
        courierApi.checkResponseForCourierLoginExistingCredentials(response);
    }
    // логин курьера без поля login
    @Test
    @DisplayName("Login courier without field login")
    @Description("Negative test for courier Login without field login should response Bad request")
    public void courierLoginWithoutLoginResponseBadRequestTest(){
        CourierLoginData copyOfCourierLoginData = new CourierLoginData();
        copyOfCourierLoginData.setPassword(courierLoginData.getPassword());
        ValidatableResponse response = courierApi.loginCourier(copyOfCourierLoginData);
        courierApi.checkResponseForCourierLoginWithoutMandatoryField(response);
    }

    // логин курьера без поля password
    @Test
    @DisplayName("Login courier without field password")
    @Description("Negative test for courier Login without field password should response Bad request")
    public void courierLoginWithoutPasswordResponseBadRequestTest(){
        CourierLoginData copyOfCourierLoginData = new CourierLoginData();
        copyOfCourierLoginData.setLogin(courierLoginData.getLogin());
        ValidatableResponse response = courierApi.loginCourier(copyOfCourierLoginData);
        courierApi.checkResponseForCourierLoginWithoutMandatoryField(response);
    }

    // логин курьера с неправильным логином
    @Test
    @DisplayName("Login courier with wrong login")
    @Description("Negative test for courier Login with wrong login should response Not found")
    public void courierLoginWrongLoginResponseNotFoundTest(){
        String wrongLogin = "Johnny";
        CourierLoginData copyOfCourierLoginData = new CourierLoginData(courierCreateData);
        copyOfCourierLoginData.setLogin(wrongLogin);
        ValidatableResponse response = courierApi.loginCourier(copyOfCourierLoginData);
        courierApi.checkResponseForCourierLoginWithWrongCredentials(response);
    }

    // логин курьера с неправильным паролем
    @Test
    @DisplayName("Login courier with wrong password")
    @Description("Negative test for courier Login with wrong password should response Not found")
    public void courierLoginWrongPasswordResponseNotFoundTest(){
        String wrongPassword = "12345";
        CourierLoginData copyOfCourierLoginData = new CourierLoginData(courierCreateData);
        copyOfCourierLoginData.setPassword(wrongPassword);
        ValidatableResponse response = courierApi.loginCourier(copyOfCourierLoginData);
        courierApi.checkResponseForCourierLoginWithWrongCredentials(response);
    }
}
