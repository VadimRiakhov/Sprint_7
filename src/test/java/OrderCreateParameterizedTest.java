import api.OrderApi;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import model.NewOrderData;
import static constants.ScooterColor.*;
import static org.hamcrest.CoreMatchers.containsString;

@RunWith(Parameterized.class)
public class OrderCreateParameterizedTest {
    private final String[] scooterColor;
    OrderApi orderApi;
    String orderTrack;

    public OrderCreateParameterizedTest(String[] scooterColor) {
        this.scooterColor = scooterColor;
    }

    @Parameterized.Parameters
    public static Object[][] getData(){
        return new Object[][]{
                {new String[]{BLACK}},
                {new String[]{GREY}},
                {new String[]{BLACK, GREY}},
                {new String[]{}},
        };
    }

    @Before
    public void setUp(){
        orderApi = new OrderApi();
    }
    // удаляем созданный заказ
    @After
    public void cancelOrder(){
        orderApi.cancelOrder(orderTrack);
    }

    // создание нового заказа с разными цветами самоката
    @Test
    public void orderCreateDifferentScooterColorResponseCreated(){
        String  firstName = "Иван";
        String lastName = "Иванов";
        String address = "Москва";
        String metroStation = "4";
        String phone = " +79998887766";
        int rentTime = 2;
        String deliveryDate = "2024-12-25";
        String comment = "Не звонить";

        // создаем объект класса нового заказа
        NewOrderData newOrderData = new NewOrderData(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, scooterColor);
        // создаем заказ
        ValidatableResponse response = orderApi.createOrder(newOrderData);
        // получаем track номер заказа
        orderTrack = response.extract().jsonPath().get("track").toString();
        //проверяем ответ
        checkResponseForOrderCreate(response);
    }

    // проверка статуса и тела ответа при создании заказа
    @Step("Check status code and response body")
    public void checkResponseForOrderCreate(ValidatableResponse response){
        response.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_CREATED)
                .and()
                .body(containsString("track"));
    }
}
