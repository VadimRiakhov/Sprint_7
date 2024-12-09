import api.OrderApi;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;

public class OrdersGetTest {

    OrderApi orderApi;

    @Before
    public void setUp() {
        orderApi = new OrderApi();
    }

    // запрос списка заказов без параметров
    @Test
    @DisplayName("Get orders without parameters")
    @Description("Positive test for get orders without parameters should response Ok with array of orders")
    public void getOrdersNoParametersResponseOkTest(){
        ValidatableResponse response = orderApi.getOrders();
        orderApi.checkResponseForGetOrders(response);
    }
    // запрос списка заказов с параметром nearestStation
    @Test
    @DisplayName("get orders by nearest station")
    @Description("Positive test for get orders with parameter \"nearestStation\" should response Ok with array of orders")
    public void getOrdersParameterNearestStationResponseOkTest(){
        String nearestStation = "[\"4\"]";
        ValidatableResponse response = orderApi.getOrdersByNearestStation(nearestStation);
        orderApi.checkResponseForGetOrders(response);
    }
}
