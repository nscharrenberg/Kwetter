package cucumber.steps;

import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.container.WorldContainer;

import static org.hamcrest.Matchers.is;

public class GetUserStepDef {
    private WorldContainer worldContainer;

    public GetUserStepDef(WorldContainer worldContainer) {
        this.worldContainer = worldContainer;
    }

    @Before
    public void setup() {
        worldContainer.request = null;
        worldContainer.response = null;
        worldContainer.validate = null;
        worldContainer.apiUrl = null;
    }

    @When("^I perform GET for the user with id \"([^\"]*)\"$")
    public void iPerformGETForTheUserWithId(String userId) throws Throwable {
        worldContainer.request = worldContainer.request.pathParam("id", userId);
        worldContainer.response = worldContainer.request.when().get(worldContainer.apiUrl + "/{id}");
    }

    @When("^I perform GET for the user with username \"([^\"]*)\"$")
    public void iPerformGETForTheUserWithUsername(String username) throws Throwable {
        worldContainer.request = worldContainer.request.pathParam("username", username);
        worldContainer.response = worldContainer.request.when().get(worldContainer.apiUrl + "/{username}");
    }

    @Then("^I should see the user username as \"([^\"]*)\"$")
    public void iShouldSeeTheUserUsernameAs(String username) throws Throwable {
        worldContainer.validate = worldContainer.response.then().assertThat().body("username", is(username));
    }
}
