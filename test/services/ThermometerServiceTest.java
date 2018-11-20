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
        List<Event> triggeredEvents = thermometerService.parseEvents(new ArrayList<String>().stream(), events);
        assertTrue(triggeredEvents.isEmpty());

        Logger.info("Passed empty file test!");
    }

    /**
     * Tests malformed data #1
     */
    @Test
    public void testMalformedData1() {
        List<String> testStrings = new ArrayList<>();
        testStrings.add("!@#$%^&**(( cf");
        assertTrue(thermometerService.parseEvents(testStrings.stream(), events).isEmpty());

        Logger.info("Passed malformed data test 1!");
    }

    /**
     * Tests malformed data #2
     */
    @Test
    public void testMalformedData2() {
        List<String> testStrings = new ArrayList<>();
        testStrings.add("C");
        assertTrue(thermometerService.parseEvents(testStrings.stream(), events).isEmpty());

        Logger.info("Passed malformed data test 2!");
    }

    /**
     * Tests malformed data #3
     * Attempt to continue through if we can regardless of some bad values
     */
    @Test
    public void testMalformedData3() {
        List<String> testStrings = new ArrayList<>();
        testStrings.add("C");
        testStrings.add("0.kasdfj");
        testStrings.add("0.5 C");
        testStrings.add("#$%455");
        testStrings.add("0.0 C");

        List<Event> triggeredEvents = thermometerService.parseEvents(testStrings.stream(), events);

        assertTrue(!triggeredEvents.isEmpty());
        assertEquals(freezing.getName(), triggeredEvents.get(0).getName());

        Logger.info("Passed malformed data test 3!");
    }

    /**
     * Tests malformed data #4
     * Testing valid data with the wrong units, shouldn't match anything and return empty list
     */
    @Test
    public void testMalformedData4() {
        List<String> testStrings = new ArrayList<>();
        testStrings.add("1.0 F");
        testStrings.add("0.5 F");
        testStrings.add("0.0 F");

        List<Event> triggeredEvents = thermometerService.parseEvents(testStrings.stream(), events);

        assertTrue(triggeredEvents.isEmpty());

        Logger.info("Passed malformed data test 4!");
    }

    /**
     * Tests one Freezing event, happy path trending down, no threshold
     */
    @Test
    public void testFreezingDownNoThreshold() {
        List<String> testStrings = new ArrayList<>();
        testStrings.add("1.0 C");
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
        testStrings.add("99.0 C");
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
