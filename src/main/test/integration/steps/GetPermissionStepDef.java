package integration.steps;

import cucumber.api.PendingException;
import cucumber.api.java.Before;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import integration.container.WorldContainer;

import static org.hamcrest.Matchers.is;

public class GetPermissionStepDef {
    private WorldContainer worldContainer;

    public GetPermissionStepDef(WorldContainer worldContainer) {
        this.worldContainer = worldContainer;
    }

    @Before
    public void setup() {
        worldContainer.request = null;
        worldContainer.response = null;
        worldContainer.validate = null;
        worldContainer.apiUrl = null;
    }

    @When("^I perform GET for the permission with id \"([^\"]*)\"$")
    public void iPerformGETForThePermissionWithId(Integer permissionId) throws Throwable {
        worldContainer.request = worldContainer.request.pathParam("id", permissionId);
        worldContainer.response = worldContainer.request.when().get(worldContainer.apiUrl + "/{id}");
    }

    @When("^I perform GET for the permission with name \"([^\"]*)\"$")
    public void iPerformGETForThePermissionWithName(String name) throws Throwable {
        worldContainer.request = worldContainer.request.pathParam("name", name);
        worldContainer.response = worldContainer.request.when().get(worldContainer.apiUrl + "/{name}");
    }

    @Then("^I should see the permission name as \"([^\"]*)\"$")
    public void iShouldSeeThePermissionNameAs(String name) throws Throwable {
        worldContainer.validate = worldContainer.response.then().assertThat().body("name", is(name));
    }
}
