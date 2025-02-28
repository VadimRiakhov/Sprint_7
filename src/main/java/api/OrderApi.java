package api;

import constants.EndPoints;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import model.NewOrderData;
import org.apache.http.HttpStatus;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

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

    // проверяем ответ с валидными данными
    @Step("check status and response body with valid credentials")
    public void checkResponseForOrderAcceptValidCredentials(ValidatableResponse response){
        response.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("ok", is(true));
    }

    // проверяем ответ запроса без id курьера или id заказа
    @Step("check status and response body without mandatory parameter")
    public void checkResponseForOrderAcceptWithoutMandatoryParameter(ValidatableResponse response){
        response.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .and()
                .body("message", equalTo("Недостаточно данных для поиска"));
    }

    // проверяем ответ запроса с несуществующим id курьера
    @Step("check status and response body with nonexistent courier id")
    public void checkResponseForOrderAcceptWithNonexistentCourierId(ValidatableResponse response){
        response.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .and()
                .body("message", equalTo("Курьера с таким id не существует"));
    }

    // проверяем ответ запроса с несуществующим id заказа
    @Step("check status and response body with nonexistent order id")
    public void checkResponseForOrderAcceptWithNonexistentOrderId(ValidatableResponse response){
        response.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .and()
                .body("message", equalTo("Заказа с таким id не существует"));
    }

    // проверяем ответ запроса принятого другим курьером заказа
    @Step("check status and response body for order accepted by another courier")
    public void checkResponseForOrderAcceptedByAnotherCourier(ValidatableResponse response){
        response.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_CONFLICT)
                .and()
                .body("message", equalTo("Этот заказ уже в работе"));
    }

    // проверка статуса и тела ответа при создании заказа с валидными данными
    @Step("Check status code and response body")
    public void checkResponseForOrderCreateValidCredentials(ValidatableResponse response){
        response.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_CREATED)
                .and()
                .body(containsString("track"));
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

    // проверка статуса и тела ответа при запросе с существующим track номером
    @Step("check status code and response body for request with existing track number")
    public void checkResponseForGetOrderExistingTrack(ValidatableResponse response){
        // проверка статуса ответа и соответствие ответа схеме Json
        response.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body(matchesJsonSchemaInClasspath("orderGetByTrackSchema.json"));
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
