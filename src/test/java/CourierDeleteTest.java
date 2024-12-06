import api.CourierApi;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import model.CourierCreateData;
import model.CourierLoginData;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.*;
import io.qameta.allure.junit4.DisplayName;

public class CourierDeleteTest {
    private String courierId;
    private CourierApi courierApi;
    CourierLoginData courierLoginData;

    @Before
    public void setUp() {
        String login = "John22";
        String password = "1234";
        String firstName = "John";

        courierApi = new CourierApi();
        // создаем объект класса создания курьера
        CourierCreateData courierCreateData = new CourierCreateData(login, password, firstName);
        // создаем курьера
        courierApi.createCourier(courierCreateData);
        // создаем объект класса логина курьера
        courierLoginData = new CourierLoginData(courierCreateData);
        // получаем id курьера
        courierId = courierApi.loginCourier(courierLoginData)
                .extract().path("id").toString();
    }

    // удаляем созданного курьера, если он не удалился в тесте
    @After
    public void cleanUp() {
        ValidatableResponse response = courierApi.loginCourier(courierLoginData);
        // если логин был успешным, то удаляем курьера
        if(response.extract().statusCode()==200){
            courierApi.deleteCourier(courierId);
        }
    }
    // удаление курьера с существующим id
    @Test
    @DisplayName("Delete courier with existing Id")
    public void deleteCourierExistingId(){
        ValidatableResponse response = courierApi.deleteCourier(courierId);
        checkResponseForCourierDeleteExistingId(response);
    }

    // удаление курьера с несуществующим id
    @Test
    @DisplayName("Delete courier with nonexistent Id")
    public void deleteCourierNonexistentId(){
        // несуществующий id курьера
        String nonexistentCourierId = "999999";
        ValidatableResponse response = courierApi.deleteCourier(nonexistentCourierId);
        checkResponseForCourierDeleteNonexistentId(response);
    }

    // удаление курьера с запросом без id
    @Test
    @DisplayName("Delete courier by request without Id")
    public void deleteCourierWithoutId(){
        ValidatableResponse response = courierApi.deleteCourier("");
        checkResponseForCourierDeleteWithoutId(response);
    }

    // проверка статуса и тела ответа при удалении курьера с существующим id
    @Step("Check status code and response body for request with existing id")
    public void checkResponseForCourierDeleteExistingId(ValidatableResponse response){
        response.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("ok", is(true));
    }

    // проверка статуса и тела ответа при удалении курьера с несуществующим id
    @Step("Check status code and response body for request with nonexistent id")
    public void checkResponseForCourierDeleteNonexistentId(ValidatableResponse response){
        response.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .and()
                .body("message", equalTo("Курьера с таким id нет."));
    }

    // проверка статуса и тела ответа при отправке запроса без id
    @Step("Check status code and response body for request without id")
    public void checkResponseForCourierDeleteWithoutId(ValidatableResponse response){
        response.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .and()
                .body("message", equalTo( "Недостаточно данных для удаления курьера"));
    }
}
