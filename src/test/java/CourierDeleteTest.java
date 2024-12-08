import api.CourierApi;
import io.qameta.allure.Description;
import io.restassured.response.ValidatableResponse;
import model.CourierCreateData;
import model.CourierGeneratorData;
import model.CourierLoginData;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.qameta.allure.junit4.DisplayName;

public class CourierDeleteTest {
    private String courierId;
    private CourierApi courierApi;
    CourierLoginData courierLoginData;

    @Before
    public void setUp() {

        courierApi = new CourierApi();
        // создаем объект класса создания курьера
        CourierCreateData courierCreateData = CourierGeneratorData.getRandomCourier();
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
        if(response.extract().statusCode()==HttpStatus.SC_OK){
            courierApi.deleteCourier(courierId);
        }
    }
    // удаление курьера с существующим id
    @Test
    @DisplayName("Delete courier with existing id")
    @Description("Positive test for courier delete with existing id should response Ok")
    public void deleteCourierExistingIdTest(){
        ValidatableResponse response = courierApi.deleteCourier(courierId);
        courierApi.checkResponseForCourierDeleteExistingId(response);
    }

    // удаление курьера с несуществующим id
    @Test
    @DisplayName("Delete courier with nonexistent Id")
    @Description("Negative test for courier delete with nonexistent id should response Not found")
    public void deleteCourierNonexistentIdTest(){
        // несуществующий id курьера
        String nonexistentCourierId = "999999";
        ValidatableResponse response = courierApi.deleteCourier(nonexistentCourierId);
        courierApi.checkResponseForCourierDeleteNonexistentId(response);
    }

    // удаление курьера с запросом без id
    @Test
    @DisplayName("Delete courier by request without Id")
    @Description("Negative test for courier delete without id should response Bad request")
    public void deleteCourierWithoutIdTest(){
        ValidatableResponse response = courierApi.deleteCourier("");
        courierApi.checkResponseForCourierDeleteWithoutId(response);
    }
}
