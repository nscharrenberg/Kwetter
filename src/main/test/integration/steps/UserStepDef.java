package integration.steps;


import controllers.viewModels.FollowViewModel;
import controllers.viewModels.RoleViewModel;
import controllers.viewModels.UserViewModel;
import cucumber.api.PendingException;
import cucumber.api.java8.En;
import integration.WireMockRules;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.junit.Before;
import org.junit.BeforeClass;

import javax.ws.rs.core.MediaType;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.restassured.RestAssured.given;

public class UserStepDef extends WireMockRules implements En {
    private Response response;
    private RequestSpecification request;
    private ValidatableResponse json;

    @BeforeClass
    public static void setup() {
        RestAssured.port = 8080;
        RestAssured.baseURI = "http://localhost";
        RestAssured.basePath = "/api/";
    }

    @Before
    public void beforeTest() {
        response = null;
        request = null;
        json = null;
    }


    public UserStepDef() {
        Given("^a group with the name \"([^\"]*)\" exists.$", (String roleName) -> {
            RoleViewModel rvm = new RoleViewModel();
            rvm.setName(roleName);

            wireMock.stubFor(post(urlMatching("/api/roles")).willReturn(aResponse().withStatus(201)));
            given().contentType(MediaType.APPLICATION_JSON).body(rvm).when().post("roles").then().assertThat().statusCode(201);
        });

        Given("^a user with \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\"$", (String username, String email, String password, String biography, String website, Double longitude, Double latitude) -> {
            UserViewModel uvm = new UserViewModel();
            uvm.setUsername(username);
            uvm.setEmail(email);
            uvm.setPassword(password);
            uvm.setBiography(biography);
            uvm.setWebsite(website);
            uvm.setLongitude(longitude);
            uvm.setLatitude(latitude);

            wireMock.stubFor(post(urlMatching("/api/roles")).willReturn(aResponse().withStatus(201)));
            given().contentType(MediaType.APPLICATION_JSON).body(uvm).when().post("users").then().assertThat().statusCode(201);
        });

        Given("^a second user with \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\"$", (String username, String email, String password, String biography, String website, Double longitude, Double latitude) -> {
            UserViewModel uvm = new UserViewModel();
            uvm.setUsername(username);
            uvm.setEmail(email);
            uvm.setPassword(password);
            uvm.setBiography(biography);
            uvm.setWebsite(website);
            uvm.setLongitude(longitude);
            uvm.setLatitude(latitude);

            wireMock.stubFor(post(urlMatching("/api/roles")).willReturn(aResponse().withStatus(201)));
            given().contentType(MediaType.APPLICATION_JSON).body(uvm).when().post("users").then().assertThat().statusCode(201);
        });

        When("^I register a new account with \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\"$", (String username, String email, String password, String biography, String website, Double longitude, Double latitude) -> {
            UserViewModel uvm = new UserViewModel();
            uvm.setUsername(username);
            uvm.setEmail(email);
            uvm.setPassword(password);
            uvm.setBiography(biography);
            uvm.setWebsite(website);
            uvm.setLongitude(longitude);
            uvm.setLatitude(latitude);

            request = given().contentType(MediaType.APPLICATION_JSON).body(uvm);
            response = request.when().post("users");
        });

        When("^I update my biography with username \"([^\"]*)\" to \"([^\"]*)\"$", (String username, String updateBiography) -> {
            UserViewModel uvm = new UserViewModel();
            uvm.setBiography(updateBiography);
            wireMock.stubFor(patch(urlPathMatching("/api/users/[^/]+")).willReturn(aResponse().withStatus(200)));
            request = given().pathParam("username", username).contentType(MediaType.APPLICATION_JSON).body(uvm);
            response = request.when().patch("users/name/{username}");
        });

        When("^User with username \"([^\"]*)\" calls the follow api to follow \"([^\"]*)\"$", (String username, String usernameToFollow) -> {
            wireMock.stubFor(patch(urlPathMatching("/api/users/[^/]+/follow")).willReturn(aResponse().withStatus(200)));


            FollowViewModel fvm = new FollowViewModel();
            fvm.setUserId(1);
            fvm.setUserToFollowId(2);

            request = given().pathParam("id", 2).contentType(MediaType.APPLICATION_JSON).body(fvm);
            response = request.when().post("users/{id}/follow");
        });


        Then("^The server responds with status code (\\d+)$", (Integer statusCode) -> {
            json = response.then().assertThat().statusCode(statusCode);
        });

        And("^A user containing \"([^\"]*)\" should exist$", (String username) -> {
            given().pathParam("name", username).when().get("users/name/{name}").then().assertThat().statusCode(200);
        });

        And("^A user containing \"([^\"]*)\" should be added to the followers list of \"([^\"]*)\"$", (String username, String follower) -> {
            //todo: add test to see if the user contains the follower in his follower list.
        });

    }
}
