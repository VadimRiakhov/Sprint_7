package model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.Arrays;

// класс создания нового заказа
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class NewOrderData
{
    String  firstName;
    String lastName;
    String address;
    String metroStation;
    String phone;
    int rentTime;
    String deliveryDate;
    String comment;
    String[] color;


    @Override
    public boolean equals(Object obj){
        if(obj == this){ // проверка на идентичность
            return true;
        }
        if (!(obj instanceof NewOrderData)) { // проверка типа объекта
            return false;
        }
        NewOrderData dataFromResponse = (NewOrderData) obj;
        return this.firstName.equals(dataFromResponse.firstName) &&
                this.lastName.equals(dataFromResponse.lastName) &&
                this.address.equals(dataFromResponse.address) &&
                this.metroStation.equals(dataFromResponse.metroStation) &&
                this.phone.equals(dataFromResponse.phone) &&
                this.rentTime==dataFromResponse.rentTime &&
                this.deliveryDate.equals(dataFromResponse.deliveryDate) &&
                this.comment.equals(dataFromResponse.comment) &&
                Arrays.toString(this.color).equals(Arrays.toString(dataFromResponse.color));
    }
}
