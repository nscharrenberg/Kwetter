package cucumber.steps;

import cucumber.api.PendingException;
import cucumber.api.java.Before;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.container.WorldContainer;

import static org.hamcrest.Matchers.*;

public class GetTweetStepDef {
    private WorldContainer worldContainer;

    public GetTweetStepDef(WorldContainer worldContainer) {
        this.worldContainer = worldContainer;
    }

    @Before
    public void setup() {
        worldContainer.request = null;
        worldContainer.response = null;
        worldContainer.validate = null;
        worldContainer.apiUrl = null;
    }

    @When("^I perform GET for the tweet with id \"([^\"]*)\"$")
    public void iPerformGETForTheTweetWithId(Integer tweetId) throws Throwable {
        worldContainer.request = worldContainer.request.pathParam("id", tweetId);
        worldContainer.response = worldContainer.request.when().get(worldContainer.apiUrl + "/{id}");
    }

    @When("^I perform GET for the tweets with author name \"([^\"]*)\"$")
    public void iPerformGETForTheTweetsWithAuthorName(String name) throws Throwable {
        worldContainer.request = worldContainer.request.pathParam("name", name);
        worldContainer.response = worldContainer.request.when().get(worldContainer.apiUrl + "/{name}");
    }

    @Then("^I should see the tweet id as \"([^\"]*)\"$")
    public void iShouldSeeTheTweetIdAs(Integer tweetId) throws Throwable {
        worldContainer.validate = worldContainer.response.then().assertThat().body("id", is(tweetId));
    }

    @Then("^I should see the tweets author name as \"([^\"]*)\"$")
    public void iShouldSeeTheTweetsAuthorNameAs(String name) throws Throwable {
        worldContainer.validate = worldContainer.response.then().assertThat().body("$", hasProperty("author", hasProperty("username", is(name))));
    }
}
