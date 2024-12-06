import api.OrderApi;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.CoreMatchers.notNullValue;

public class OrdersGetTest {

    OrderApi orderApi;

    @Before
    public void setUp() {
        orderApi = new OrderApi();
    }

    // запрос списка заказов без параметров
    @Test
    @DisplayName("Get orders without parameters")
    public void getOrdersNoParametersResponseOk(){
        ValidatableResponse response = orderApi.getOrders();
        checkResponseForGetOrders(response);
    }
    // запрос списка заказов с параметром nearestStation
    @Test
    @DisplayName("get orders by nearest station")
    public void getOrdersParameterNearestStationResponseOk(){
        String nearestStation = "[\"4\"]";
        ValidatableResponse response = orderApi.getOrdersByNearestStation(nearestStation);
        checkResponseForGetOrders(response);
    }

    // проверка статуса и тела ответа
    @Step("check status code and response body")
    public void checkResponseForGetOrders(ValidatableResponse response){
        response.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body(notNullValue())
                .and()
                .body(matchesJsonSchemaInClasspath("ordersGetSchema.json"));
    }
}
