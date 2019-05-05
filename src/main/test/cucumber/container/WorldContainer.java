package cucumber.container;

import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

public class WorldContainer {
    public RequestSpecification request;
    public Response response;
    public ValidatableResponse validate;
    public String apiUrl;
}
