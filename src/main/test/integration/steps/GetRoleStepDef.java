package integration.steps;

import cucumber.api.PendingException;
import cucumber.api.java.Before;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import integration.container.WorldContainer;

import static org.hamcrest.Matchers.is;

public class GetRoleStepDef {

    private WorldContainer worldContainer;

    public GetRoleStepDef(WorldContainer worldContainer) {
        this.worldContainer = worldContainer;
    }

    @Before
    public void setup() {
        worldContainer.request = null;
        worldContainer.response = null;
        worldContainer.validate = null;
        worldContainer.apiUrl = null;
    }

    @When("^I perform GET for the role with id \"([^\"]*)\"$")
    public void iPerformGETForTheRoleWithId(Integer roleId) throws Throwable {
        worldContainer.request = worldContainer.request.pathParam("id", roleId);
        worldContainer.response = worldContainer.request.when().get(worldContainer.apiUrl + "/{id}");
    }

    @When("^I perform GET for the role with name \"([^\"]*)\"$")
    public void iPerformGETForTheRoleWithName(String name) throws Throwable {
        worldContainer.request = worldContainer.request.pathParam("name", name);
        worldContainer.response = worldContainer.request.when().get(worldContainer.apiUrl + "/{name}");
    }

    @Then("^I should see the role name as \"([^\"]*)\"$")
    public void iShouldSeeTheRoleNameAs(String name) throws Throwable {
        worldContainer.validate = worldContainer.response.then().assertThat().body("name", is(name));
    }
}
