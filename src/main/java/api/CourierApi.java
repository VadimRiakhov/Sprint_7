package api;

import constants.EndPoints;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import model.CourierCreateData;
import model.CourierLoginData;

import static io.restassured.RestAssured.given;

public class CourierApi extends RestApi{

    @Step("Courier create")
    public ValidatableResponse createCourier(CourierCreateData courier){
        return given()
                .spec(requestSpecification())
                .and()
                .body(courier)
                .when()
                .post(EndPoints.COURIER_CREATE)
                .then();
    }

    @Step("Courier login")
    public ValidatableResponse loginCourier(CourierLoginData courier){
        return given()
                .spec(requestSpecification())
                .and()
                .body(courier)
                .when()
                .post(EndPoints.COURIER_LOGIN)
                .then();

    }

    @Step("Courier delete")
    public ValidatableResponse deleteCourier(String courierId){

        return given()
                .spec(requestSpecification())
                .when()
                .delete(EndPoints.COURIER_DELETE, courierId)
                .then();
    }

}
