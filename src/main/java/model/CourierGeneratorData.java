package model;

import io.qameta.allure.Step;
import org.apache.commons.lang3.RandomStringUtils;

public class CourierGeneratorData {

    @Step("Generate random courier")
    public static CourierCreateData getRandomCourier(){
        String login = RandomStringUtils.randomAlphabetic(8);
        String password = RandomStringUtils.randomAlphabetic(8);
        String firstName = RandomStringUtils.randomAlphabetic(8);

        return new CourierCreateData(login, password, firstName);
    }

    @Step("Generate random courier with empty field login")
    public static CourierCreateData getRandomCourierWithoutLogin(){
        String password = RandomStringUtils.randomAlphabetic(8);
        String firstName = RandomStringUtils.randomAlphabetic(8);

        return new CourierCreateData(null, password, firstName);
    }

    @Step("Generate random courier with empty field password")
    public static CourierCreateData getRandomCourierWithoutPassword(){
        String login = RandomStringUtils.randomAlphabetic(8);
        String firstName = RandomStringUtils.randomAlphabetic(8);

        return new CourierCreateData(login, null, firstName);
    }

}
