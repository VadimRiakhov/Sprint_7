package model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Arrays;

// класс создания нового заказа
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMetroStation() {
        return metroStation;
    }

    public void setMetroStation(String metroStation) {
        this.metroStation = metroStation;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getRentTime() {
        return rentTime;
    }

    public void setRentTime(int rentTime) {
        this.rentTime = rentTime;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String[] getColor() {
        return color;
    }

    public void setColor(String[] color) {
        this.color = color;
    }

    public NewOrderData(String firstName, String lastName, String address, String metroStation, String phone, int rentTime, String deliveryDate, String comment, String[] color) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.color = color;
    }
    public NewOrderData(){};

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
