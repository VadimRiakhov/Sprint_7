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

public class CourierCreateTest {

    private CourierApi courierApi;
    private CourierCreateData courierCreateData;

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
        if(response.extract().statusCode()==HttpStatus.SC_OK){
            String courierId = response
                    .extract().path("id").toString();
            courierApi.deleteCourier(courierId);
        }
    }

    // создание курьера с валидными данными
    @Test
    @DisplayName("Create courier with valid credentials")
    @Description("Positive test for courier create")
    public void createCourierValidCredentialsTrueTest() {
        courierCreateData = CourierGeneratorData.getRandomCourier();
        ValidatableResponse response = courierApi.createCourier(courierCreateData);
        courierApi.checkResponseForCourierCreateValidCredentials(response);
    }

    // создание курьера с существующим логином
    @Test
    @DisplayName("Create courier with existing login")
    @Description("Negative test for courier create with existing login should response Conflict")
    public void createCourierExistingLoginConflictTest(){
        courierCreateData = CourierGeneratorData.getRandomCourier();
        courierApi.createCourier(courierCreateData);
        ValidatableResponse response = courierApi.createCourier(courierCreateData);
        courierApi.checkResponseForCourierCreateExistingCourier(response);
    }

    // создание курьера с пустым полем login в запросе
    @Test
    @DisplayName("Create courier with empty login")
    @Description("Negative test for courier create with empty login should response Bad request")
    public void createCourierWithoutLoginBadRequestTest(){
        courierCreateData = CourierGeneratorData.getRandomCourierWithoutLogin();
        ValidatableResponse response = courierApi.createCourier(courierCreateData);
        courierApi.checkResponseForCourierCreateWithoutMandatoryField(response);
    }

    // создание курьера с пустым полем password в запросе
    @Test
    @DisplayName("Create courier with empty password")
    @Description("Negative test for courier create with empty password should response Bad request")
    public void createCourierWithoutPasswordBadRequestTest(){
        courierCreateData = CourierGeneratorData.getRandomCourierWithoutPassword();
        ValidatableResponse response = courierApi.createCourier(courierCreateData);
        courierApi.checkResponseForCourierCreateWithoutMandatoryField(response);
    }


}
