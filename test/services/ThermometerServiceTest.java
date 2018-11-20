package services;

import com.typesafe.config.Config;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import play.Application;
import play.Logger;
import play.inject.guice.GuiceApplicationBuilder;
import play.test.WithApplication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import models.Event;
import models.Event.Trend;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@RunWith(MockitoJUnitRunner.class)
public class ThermometerServiceTest extends WithApplication {

    private static String UNIT = "C";
    private static Double THRESHOLD = 0.5;
    private static String DECIMALFORMAT = "###.##";

    private List<Event> events = new ArrayList<>();
    private Event boiling = new Event("Boiling", 100.0, Trend.UP);
    private Event freezing = new Event("Freezing", 0.0, Trend.DOWN);

    @Mock Config config;
    @InjectMocks ThermometerService thermometerService = new ThermometerService(UNIT, THRESHOLD, DECIMALFORMAT);

    @Override
    protected Application provideApplication() {
        return new GuiceApplicationBuilder().build();
    }

    /**
     * Setup for tests
     */
    @Before
    public void before() {
        // Init default events
        events.add(boiling);
        events.add(freezing);
    }

    /**
     * Tests the configuration usage
     */
    @Test
    public void confTest() {
        assertEquals(UNIT, thermometerService.getUnit());
        assertEquals(THRESHOLD, thermometerService.getThreshold());

        Logger.info("Passed configuration test!");
    }

    /**
     * Tests empty file contents
     */
    @Test
    public void testEmptyFile() {
        Stream<String> testStrings = new ArrayList<String>().stream();
        List<Event> triggeredEvents = thermometerService.parseEvents(testStrings, events);
        assertTrue(triggeredEvents.isEmpty());

        Logger.info("Passed empty file test!");
    }

    /**
     * Tests one Freezing event, happy path trending down, no threshold
     */
    @Test
    public void testFreezingDownNoThreshold() {
        List<String> testStrings = new ArrayList<>();
        testStrings.add("0.5 C");
        testStrings.add("0.0 C");

        List<Event> triggeredEvents = thermometerService.parseEvents(testStrings.stream(), events);

        assertTrue(!triggeredEvents.isEmpty());
        assertEquals(freezing.getName(), triggeredEvents.get(0).getName());

        Logger.info("Passed freezing DOWN no threshold test!");
    }

    /**
     * Tests one Boiling event, happy path trending up, no threshold
     */
    @Test
    public void testBoilingUpNoThreshold() {
        List<String> testStrings = new ArrayList<>();
        testStrings.add("99.5 C");
        testStrings.add("100.0 C");

        List<Event> triggeredEvents = thermometerService.parseEvents(testStrings.stream(), events);

        assertTrue(!triggeredEvents.isEmpty());
        assertEquals(boiling.getName(), triggeredEvents.get(0).getName());

        Logger.info("Passed boiling UP no threshold test!");
    }

    private void overrideConfig(String unit, Double threshold) {
        when(config.getString("thermometer.unit")).thenReturn(unit);
        when(config.getDouble("thermometer.threshold")).thenReturn(threshold);
    }

}
