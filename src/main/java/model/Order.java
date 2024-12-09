package model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
// класс для десериализации ответа на запрос заказа по track номеру
public class Order {
    @JsonProperty("order")
    NewOrderData newOrderData;
}
