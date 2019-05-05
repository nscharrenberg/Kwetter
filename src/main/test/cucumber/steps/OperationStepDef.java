package cucumber.steps;

import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.KwetterTest;
import cucumber.container.WorldContainer;
import io.restassured.RestAssured;

import javax.ws.rs.core.MediaType;

import static io.restassured.RestAssured.given;

public class OperationStepDef extends KwetterTest {
    private WorldContainer worldContainer;

    public OperationStepDef(WorldContainer worldContainer) {
        this.worldContainer = worldContainer;
    }

    @Before
    public void setup() {
        RestAssured.port = this.port;
        RestAssured.baseURI = this.baseURI;
        RestAssured.basePath = this.basePath;
    }

    /**
     * Part of the Given statement
     * Sets the proper URL and contentType for the GET statements
     * @param url
     * @throws Throwable
     */
    @Given("^I perform GET operation for \"([^\"]*)\"$")
    public void iPerformGETOperationFor(String url) throws Throwable {
        worldContainer.apiUrl = url;
        worldContainer.request = given().contentType(MediaType.APPLICATION_JSON);
    }

    /**
     * Part of a Then statement
     * Checks if the statuscode is correct
     * @param arg0
     * @throws Throwable
     */
    @And("^I should see the statuscode \"([^\"]*)\"$")
    public void iShouldSeeTheStatuscode(Integer arg0) throws Throwable {
        worldContainer.response.then().assertThat().statusCode(arg0);
    }
}
