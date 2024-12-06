import api.OrderApi;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import model.Order;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import model.NewOrderData;

import static constants.ScooterColor.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;

// получение информации по заказу по track номеру
public class OrderGetByTrackTest {
    private String orderTrack;
    OrderApi orderApi;
    NewOrderData newOrderData;
    String  firstName = "Иван";
    String lastName = "Иванов";
    String address = "Москва";
    String metroStation = "4";
    String phone = " +79998887766";
    int rentTime = 2;
    String deliveryDate = "2024-12-25";
    String comment = "Не звонить";
    String[] scooterColor = new String[]{BLACK};

    @Before
    public void setUp(){
        orderApi = new OrderApi();
        // создаем объект класса нового заказа
        newOrderData = new NewOrderData(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, scooterColor);
        // создаем заказ и получаем его track номер
        orderTrack = orderApi.createOrder(newOrderData)
                .extract().jsonPath().get("track").toString();
    }

    // удаляем созданный заказ
    @After
    public void cancelOrder(){
        orderApi.cancelOrder(orderTrack);
    }

    // запрос с существующим track номером
    @Test
    @DisplayName("Request for Get order with existing track number")
    public void getOrderByExistingTrackResponseOk(){
        ValidatableResponse response = orderApi.getOrderByTrack(orderTrack);
        checkResponseForGetOrderExistingTrack(response);
    }

    // запрос с несуществующим track номером
    @Test
    @DisplayName("Request for Get order with nonexistent track number")
    public void getOrderByNonexistentTrackResponseNotFound(){
        String nonexistentOrderTrack = "999999";
        ValidatableResponse response = orderApi.getOrderByTrack(nonexistentOrderTrack);
        checkResponseForGetOrderNonexistentTrack(response);
    }

    // запрос без track номера
    @Test
    @DisplayName("Request for Get order without track number")
    public void getOrderWithoutTrackResponseBadRequest(){
        String emptyOrderTrack = "";
        ValidatableResponse response = orderApi.getOrderByTrack(emptyOrderTrack);
        checkResponseForGetOrderWithoutTrack(response);
    }

    // проверка статуса и тела ответа при запросе с существующим track номером
    @Step("check status code and response body for request with existing track number")
    public void checkResponseForGetOrderExistingTrack(ValidatableResponse response){
        // проверка статуса ответа, соответствие ответа схеме Json и десериализация ответа сервера
        Order dataFromResponse = response.log().all()
                                        .assertThat()
                                        .statusCode(HttpStatus.SC_OK)
                                        .and()
                                        .body(matchesJsonSchemaInClasspath("orderGetByTrackSchema.json"))
                                        .extract().as(Order.class);
        // приводим дату доставки к формату, который использовали при создании заказа
        dataFromResponse.getNewOrderData().setDeliveryDate(dataFromResponse.getNewOrderData().getDeliveryDate().substring(0,10));
        // сравниваем объект, который передавали в запрос, и объект, который получили в ответ
        assertEquals(newOrderData, dataFromResponse.getNewOrderData());
    }

    @Step("check status code and response body for request with nonexistent track number")
    public void checkResponseForGetOrderNonexistentTrack(ValidatableResponse response){
        response.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .and()
                .body("message", equalTo("Заказ не найден"));
    }

    @Step("check status code and response body for request without track number")
    public void checkResponseForGetOrderWithoutTrack(ValidatableResponse response){
        response.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .and()
                .body("message", equalTo("Недостаточно данных для поиска"));
    }

}
