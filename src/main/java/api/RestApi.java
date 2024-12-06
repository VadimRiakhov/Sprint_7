package api;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static constants.UrlConstants.SCOOTER_MAIN_PAGE_URL;
import static java.util.Locale.filter;

public class RestApi {

    protected RequestSpecification requestSpecification(){
        return new RequestSpecBuilder()
                .setBaseUri(SCOOTER_MAIN_PAGE_URL)
                .setContentType(ContentType.JSON)
                .build()
                .filter(new AllureRestAssured())
                .log().all();
    }
}
