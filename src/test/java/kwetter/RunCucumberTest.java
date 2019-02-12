/*
 * Copyright (c) 2019. Noah Scharrenberg
 */

package kwetter;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {"pretty"},
        features = "src/test/java/kwetter"
)
public class RunCucumberTest {
}
