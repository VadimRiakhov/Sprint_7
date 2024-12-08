package api;

import constants.EndPoints;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import model.CourierCreateData;
import model.CourierLoginData;
import org.apache.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

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

    // проверка статуса и тела ответа при создании курьера с валидными данными
    @Step("Check status code and response body for request with valid credentials")
    public void checkResponseForCourierCreateValidCredentials(ValidatableResponse response){
                response.log().all()
                        .assertThat()
                        .statusCode(HttpStatus.SC_CREATED)
                        .and()
                        .body("ok", is(true));
    }

    // проверка статуса и тела ответа при создании курьера с существующим логином
    @Step("Check status code and response body for request with existing login")
    public void checkResponseForCourierCreateExistingCourier(ValidatableResponse response){
        response.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_CONFLICT)
                .and()
                .body("message", equalTo("Этот логин уже используется."));
    }

    // проверка статуса и тела ответа при создании курьера без обязательного поля
    @Step("Check status code and response body for request without mandatory field")
    public void checkResponseForCourierCreateWithoutMandatoryField(ValidatableResponse response){
        response.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .and()
                .body("message", equalTo( "Недостаточно данных для создания учетной записи"));
    }

    // проверка статуса и тела ответа при удалении курьера с существующим id
    @Step("Check status code and response body for request with existing id")
    public void checkResponseForCourierDeleteExistingId(ValidatableResponse response){
        response.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("ok", is(true));
    }

    // проверка статуса и тела ответа при удалении курьера с несуществующим id
    @Step("Check status code and response body for request with nonexistent id")
    public void checkResponseForCourierDeleteNonexistentId(ValidatableResponse response){
        response.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .and()
                .body("message", equalTo("Курьера с таким id нет."));
    }

    // проверка статуса и тела ответа при отправке запроса без id
    @Step("Check status code and response body for request without id")
    public void checkResponseForCourierDeleteWithoutId(ValidatableResponse response){
        response.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .and()
                .body("message", equalTo( "Недостаточно данных для удаления курьера"));
    }

    // проверка статуса и тела ответа при логине курьера с существующими данными
    @Step("Check status code and response body for request with existing credentials")
    public void checkResponseForCourierLoginExistingCredentials(ValidatableResponse response){
        response.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body(containsString("id"));
    }

    // проверка статуса и тела ответа при логине с неправильным логином или паролем
    @Step("Check status code and response body for request with wrong credentials")
    public void checkResponseForCourierLoginWithWrongCredentials(ValidatableResponse response){
        response.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .and()
                .body("message", equalTo("Учетная запись не найдена"));
    }

    // проверка статуса и тела ответа при логине без обязательного поля
    @Step("Check status code and response body for request without mandatory field")
    public void checkResponseForCourierLoginWithoutMandatoryField(ValidatableResponse response){
        response.log().all()
                .assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .and()
                .body("message", equalTo( "Недостаточно данных для входа"));
    }
}
