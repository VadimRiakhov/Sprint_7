import api.OrderApi;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import model.Order;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import model.NewOrderData;

import static constants.ScooterColor.*;

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
    @Description("Positive test for order cancel with existing track number should response Ok")
    public void getOrderByExistingTrackResponseOkTest(){
        ValidatableResponse response = orderApi.getOrderByTrack(orderTrack);
        Order dataFromResponse = response.extract().as(Order.class);
        // проверяем статус ответа и соответсвие json схеме
        orderApi.checkResponseForGetOrderExistingTrack(response);
        // приводим дату доставки к формату, который использовали при создании заказа
        dataFromResponse.getNewOrderData().setDeliveryDate(dataFromResponse.getNewOrderData().getDeliveryDate().substring(0,10));
        // сравниваем объект, который передавали в запрос, и объект, который получили в ответ
        assertEquals(newOrderData, dataFromResponse.getNewOrderData());
    }

    // запрос с несуществующим track номером
    @Test
    @DisplayName("Request for Get order with nonexistent track number")
    @Description("Negative test for order cancel with nonexistent track number should response Not found")
    public void getOrderByNonexistentTrackResponseNotFoundTest(){
        String nonexistentOrderTrack = "999999";
        ValidatableResponse response = orderApi.getOrderByTrack(nonexistentOrderTrack);
        orderApi.checkResponseForGetOrderNonexistentTrack(response);
    }

    // запрос без track номера
    @Test
    @DisplayName("Request for Get order without track number")
    @Description("Negative test for order cancel without track number should response Bad request")
    public void getOrderWithoutTrackResponseBadRequestTest(){
        String emptyOrderTrack = "";
        ValidatableResponse response = orderApi.getOrderByTrack(emptyOrderTrack);
        orderApi.checkResponseForGetOrderWithoutTrack(response);
    }
}
