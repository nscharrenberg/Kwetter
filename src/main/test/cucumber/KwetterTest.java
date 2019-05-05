package cucumber;

import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Rule;

public class KwetterTest {
    public int port = 8080;
    public String baseURI = "http://localhost";
    public String basePath = "/kwetter/api";
}
