package integration.steps;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import controllers.viewModels.UserViewModel;
import cucumber.api.DataTable;
import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import integration.RestConnectionTest;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class RegistrationSteps {

    private Response response;
    private ValidatableResponse json;
    private RequestSpecification request;
    private String USER_API = RestConnectionTest.BASE_URL + "users";
    private UserViewModel userViewModel;

    @Given("^I want to create an account with the following information$")
    public void i_want_to_create_an_account_with_the_following_information(DataTable arg1) throws Exception {
        userViewModel = arg1.transpose().asList(UserViewModel.class).get(0);
        request = given().contentType("application/json").body(userViewModel);
    }

    @When("^I submit my account information$")
    public void i_submit_my_account_information() throws Exception {
        response = request.when().post(USER_API);
    }

    @Then("^The status code is (\\d+)$")
    public void the_status_code_is(int arg1) throws Exception {
        json = response.then().statusCode(arg1);
    }

    @Then("^response includes the following$")
    public void response_includes_the_following(Map<String,String> arg1) throws Exception {
        for(Map.Entry<String, String> field : arg1.entrySet()) {
            if(StringUtils.isNumeric(field.getValue())) {
                json.body(field.getKey(), containsInAnyOrder(Integer.parseInt(field.getValue())));
            } else {
                json.body(field.getValue(), containsInAnyOrder(field.getValue()));
            }
        }
    }
}
