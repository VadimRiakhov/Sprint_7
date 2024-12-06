package api;

import constants.EndPoints;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import model.NewOrderData;

import static io.restassured.RestAssured.given;

public class OrderApi extends RestApi{
    @Step("Order create")
    public ValidatableResponse createOrder(NewOrderData newOrderData){
        return given()
                .spec(requestSpecification())
                .and()
                .body(newOrderData)
                .when()
                .post(EndPoints.ORDER_CREATE)
                .then();
    }

    @Step("Order accept")
    public ValidatableResponse acceptOrder(String orderId, String courierId){
        return given()
                .spec(requestSpecification())
                .when()
                .put(EndPoints.ORDER_ACCEPT, orderId, courierId)
                .then();
    }

    @Step("Order finish")
    public ValidatableResponse finishOrder(String orderId){
        return given()
                .spec(requestSpecification())
                .when()
                .put(EndPoints.ORDER_FINISH, orderId)
                .then();
    }

    @Step("Order cancel")
    public ValidatableResponse cancelOrder(String orderTrack){
        return given()
                .spec(requestSpecification())
                .when()
                .put(EndPoints.ORDER_CANCEL, orderTrack)
                .then();
    }

    @Step("Order get by track")
    public ValidatableResponse getOrderByTrack(String orderTrack){
        return given()
                .spec(requestSpecification())
                .when()
                .get(EndPoints.ORDER_GET_BY_TRACK, orderTrack)
                .then();
    }

    @Step("Get orders")
    public ValidatableResponse getOrders(){
        return given()
                .spec(requestSpecification())
                .when()
                .get(EndPoints.ORDERS_GET)
                .then();
    }

    @Step("Get orders by nearest station")
    public ValidatableResponse getOrdersByNearestStation(String nearestStation){
        return given()
                .spec(requestSpecification())
                .when()
                .queryParam("nearestStation", nearestStation)
                .get(EndPoints.ORDERS_GET)
                .then();
    }
}
