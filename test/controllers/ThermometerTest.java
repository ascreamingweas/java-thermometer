package controllers;

import com.typesafe.config.Config;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.mvc.Http;
import play.mvc.Result;
import play.test.WithApplication;

import static org.junit.Assert.assertEquals;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.GET;
import static play.test.Helpers.route;

import java.util.ArrayList;
import java.util.stream.Stream;

public class ThermometerTest extends WithApplication {

    @Mock Config config;
    @InjectMocks Thermometer thermometer = new Thermometer(config);

    @Override
    protected Application provideApplication() {
        return new GuiceApplicationBuilder().build();
    }

    @Test
    public void testIndex() {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .uri("/");

        Result result = route(app, request);
        assertEquals(OK, result.status());
    }

    @Test
    public void testEmptyFile() {
        Stream<String> testStrings = new ArrayList<String>().stream();


    }

}
