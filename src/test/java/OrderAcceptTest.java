import api.CourierApi;
import api.OrderApi;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import model.CourierCreateData;
import model.CourierLoginData;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import model.NewOrderData;

import static constants.ScooterColor.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

public class OrderAcceptTest {
    private String orderId;
    private String courierId;
    OrderApi orderApi;
    CourierApi courierApi;
    String orderTrack;

    @Before
    public void setUp(){
        String  firstNameCustomer = "Иван";
        String lastName = "Иванов";
        String address = "Москва";
        String metroStation = "4";
        String phone = " +79998887766";
        int rentTime = 2;
        String deliveryDate = "2024-12-25";
        String comment = "Не звонить";
        String[] scooterColor = new String[]{BLACK};

        String login = "John22";
        String password = "1234";
        String firstNameCourier = "John";

        orderApi = new OrderApi();
        courierApi = new CourierApi();
        CourierApi courierApi = new CourierApi();
        // создаем объект класса нового заказа
        NewOrderData newOrderData = new NewOrderData(firstNameCustomer, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, scooterColor);
        // создаем заказ и получаем track номер заказа
        orderTrack = orderApi.createOrder(newOrderData)
                .extract().jsonPath().get("track").toString();
        // по track номеру получаем информацию по заказу и извлекаем id заказа
        orderId = orderApi.getOrderByTrack(orderTrack)
                .extract().jsonPath().get("order.id").toString();
        // создаем объект класса создания курьера
        CourierCreateData courierCreateData = new CourierCreateData(login, password, firstNameCourier);
        // создаем курьера
        courierApi.createCourier(courierCreateData);
        // создаем объект класса логина курьера
        CourierLoginData courierLoginData = new CourierLoginData(courierCreateData);
        // получаем id курьера
        courierId = courierApi.loginCourier(courierLoginData)
                .extract().jsonPath().get("id").toString();
    }

    // завершаем созданный заказ и удаляем курьера
    @After
    public void cleanUp(){
        // завершаем заказ
        orderApi.finishOrder(orderId);
        // удаляем курьера
        courierApi.deleteCourier(courierId);
    }

    // принимаем заказ с существующими id заказа и id курьера
    @Test
    public void acceptOrderValidCredentialsResponseOk(){
        ValidatableResponse response = orderApi.acceptOrder(orderId, courierId);
        checkResponseForOrderAcceptValidCredentials(response);
    }

    // принимаем заказ без id курьера
    @Test
    public void acceptOrderWithoutCourierIdResponseBadRequest(){
        ValidatableResponse response = orderApi.acceptOrder(orderId, "");
        checkResponseForOrderAcceptWithoutMandatoryParameter(response);
    }
    // принимаем заказ без id заказа
    @Test
    public void acceptOrderWithoutOrderIdResponseBadRequest(){
        ValidatableResponse response = orderApi.acceptOrder("", courierId);
        checkResponseForOrderAcceptWithoutMandatoryParameter(response);
    }

    // принимаем заказ с несуществующим id курьера
    @Test
    public void acceptOrderWithNonexistentCourierIdResponseNotFound(){
        String nonexistentCourierId = "999999";
        ValidatableResponse response = orderApi.acceptOrder(orderId, nonexistentCourierId);
        checkResponseForOrderAcceptWithNonexistentCourierId(response);
    }

    // принимаем заказ с несуществующим id заказа
    @Test
    public void acceptOrderWithNonexistentOrderIdResponseNotFound(){
        String nonexistentOrderId = "999999";
        ValidatableResponse response = orderApi.acceptOrder(nonexistentOrderId, courierId);
        checkResponseForOrderAcceptWithNonexistentOrderId(response);
    }

    // проверяем ответ с валидными данными
    @Step("check status and response body with valid credentials")
    public void checkResponseForOrderAcceptValidCredentials(ValidatableResponse response){
        response.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("ok", is(true));
    }

    // проверяем ответ запроса без id курьера или id заказа
    @Step("check status and response body without mandatory parameter")
    public void checkResponseForOrderAcceptWithoutMandatoryParameter(ValidatableResponse response){
        response.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .and()
                .body("message", equalTo("Недостаточно данных для поиска"));
    }

    // проверяем ответ запроса с несуществующим id курьера
    @Step("check status and response body with nonexistent courier id")
    public void checkResponseForOrderAcceptWithNonexistentCourierId(ValidatableResponse response){
        response.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .and()
                .body("message", equalTo("Курьера с таким id не существует"));
    }

    // проверяем ответ запроса с несуществующим id заказа
    @Step("check status and response body with nonexistent order id")
    public void checkResponseForOrderAcceptWithNonexistentOrderId(ValidatableResponse response){
        response.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .and()
                .body("message", equalTo("Заказа с таким id не существует"));
    }
}
