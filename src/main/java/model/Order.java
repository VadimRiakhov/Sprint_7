package model;

import com.fasterxml.jackson.annotation.JsonProperty;

// класс для десериализации ответа на запрос заказа по track номеру
public class Order {
    @JsonProperty("order")
    NewOrderData newOrderData;

    public NewOrderData getNewOrderData() {
        return newOrderData;
    }

    public void setNewOrderData(NewOrderData newOrderData) {
        this.newOrderData = newOrderData;
    }
}
