package com.nscharrenberg.kwetter.integration;

import com.github.tomakehurst.wiremock.client.WireMock;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import com.jayway.restassured.RestAssured;
import static com.jayway.restassured.RestAssured.given;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.parsing.Parser;
import java.io.File;
import java.net.URL;
import org.hamcrest.Matchers;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class PermissionTest {
    private final WireMock wiremock = new WireMock(8888);

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        File[] files = Maven.resolver()
                .loadPomFromFile("pom.xml")
                .importRuntimeDependencies()
                .resolve()
                .withTransitivity()
                .asFile();
        return ShrinkWrap.create(WebArchive.class)
                .addPackages(true, "com.nscharrenberg.kwetter")
                .addAsWebInfResource(new File("src/main/resources/META-INF/beans.xml"))
                .addAsLibraries(files);
    }

    @ArquillianResource
    private URL contextPath;

    @Test
    @InSequence(1)
    public void testGetUsers() {
        RestAssured.registerParser("text/plain", Parser.TEXT);
        given()
                .when()
                .get(contextPath.toString() + "api/users")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON);
    }
}
