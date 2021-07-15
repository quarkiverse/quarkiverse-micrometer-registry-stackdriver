package io.quarkiverse.it.micrometer.native_mode;

import static io.restassured.RestAssured.when;

import java.util.List;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.Response;

/**
 * Test functioning MeterRegistries
 * Use test execution order to ensure one http server request measurement
 * is present when the endpoint is scraped.
 */
@QuarkusTest
class NativeMeterRegistriesTest {

    @Test
    void testRegistryInjection() {
        // We expect all 6 registries on the classpath to be initialized
        Response response = when().get("/message/ping");
        Assertions.assertEquals(200, response.statusCode());

        List<Object> registries = response.jsonPath().getList("$");
        MatcherAssert.assertThat(registries, Matchers.containsInAnyOrder(
                "io.micrometer.azuremonitor.AzureMonitorMeterRegistry",
                "io.micrometer.datadog.DatadogMeterRegistry",
                "io.micrometer.graphite.GraphiteMeterRegistry",
                "io.micrometer.jmx.JmxMeterRegistry",
                "io.micrometer.newrelic.NewRelicMeterRegistry",
                "io.micrometer.signalfx.SignalFxMeterRegistry",
                "io.micrometer.stackdriver.StackdriverMeterRegistry",
                "io.micrometer.statsd.StatsdMeterRegistry"));
    }

    @Test
    void testRegistryBehavior() {
        Response response = when().get("/rest/hello");
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals("hello", response.body().asString());

        response = when().get("/rest/goodbye");
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals("goodbye", response.body().asString());

        response = when().get("/rest/countthem");
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals("ok", response.body().asString());
    }
}
