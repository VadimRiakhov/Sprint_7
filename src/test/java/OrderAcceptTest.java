import api.CourierApi;
import api.OrderApi;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import model.CourierCreateData;
import model.CourierGeneratorData;
import model.CourierLoginData;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import model.NewOrderData;

import static constants.ScooterColor.*;

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
        CourierCreateData courierCreateData = CourierGeneratorData.getRandomCourier();
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
    @DisplayName("Order accept with existing order id and courier id")
    @Description("Positive test with existing credentials should response Ok")
    public void acceptOrderValidCredentialsResponseOkTest(){
        ValidatableResponse response = orderApi.acceptOrder(orderId, courierId);
        orderApi.checkResponseForOrderAcceptValidCredentials(response);
    }

    // принимаем заказ без id курьера
    @Test
    @DisplayName("Order accept without courier id")
    @Description("Negative test without courier id should response Bad request")
    public void acceptOrderWithoutCourierIdResponseBadRequestTest(){
        ValidatableResponse response = orderApi.acceptOrder(orderId, "");
        orderApi.checkResponseForOrderAcceptWithoutMandatoryParameter(response);
    }
    // принимаем заказ без id заказа
    @Test
    @DisplayName("Order accept without order id")
    @Description("Negative test without order id should response Bad request")
    public void acceptOrderWithoutOrderIdResponseBadRequestTest(){
        ValidatableResponse response = orderApi.acceptOrder("", courierId);
        orderApi.checkResponseForOrderAcceptWithoutMandatoryParameter(response);
    }

    // принимаем заказ с несуществующим id курьера
    @Test
    @DisplayName("Order accept with nonexistent courier id")
    @Description("Negative test with nonexistent courier id should response Not found")
    public void acceptOrderWithNonexistentCourierIdResponseNotFoundTest(){
        String nonexistentCourierId = "999999";
        ValidatableResponse response = orderApi.acceptOrder(orderId, nonexistentCourierId);
        orderApi.checkResponseForOrderAcceptWithNonexistentCourierId(response);
    }

    // принимаем заказ с несуществующим id заказа
    @Test
    @DisplayName("Order accept with nonexistent order id")
    @Description("Negative test with nonexistent order id should response Not found")
    public void acceptOrderWithNonexistentOrderIdResponseNotFoundTest(){
        String nonexistentOrderId = "999999";
        ValidatableResponse response = orderApi.acceptOrder(nonexistentOrderId, courierId);
        orderApi.checkResponseForOrderAcceptWithNonexistentOrderId(response);
    }

    // принимаем заказ, который уже принят другим курьером
    @Test
    @DisplayName("Order accept that accepted by another courier")
    @Description("Negative test for accept order that accepted by another courier should response Conflict")
    public void acceptOrderAcceptedByAnotherCourierResponseConflictTest(){
        CourierCreateData anotherCourierCreateData = CourierGeneratorData.getRandomCourier();
        // создаем еще одного курьера курьера
        courierApi.createCourier(anotherCourierCreateData);
        // создаем объект класса логина курьера
        CourierLoginData anotherCourierLoginData = new CourierLoginData(anotherCourierCreateData);
        // получаем id курьера
        String anotherCourierId = courierApi.loginCourier(anotherCourierLoginData)
                .extract().jsonPath().get("id").toString();
        // принимаем заказ новым курьером
        orderApi.acceptOrder(orderId, anotherCourierId);
        // принимаем заказ курьером, созданным в Before
        ValidatableResponse response = orderApi.acceptOrder(orderId, courierId);
        orderApi.checkResponseForOrderAcceptedByAnotherCourier(response);
        courierApi.deleteCourier(anotherCourierId);
    }
}
