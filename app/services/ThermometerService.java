package services;

import models.Event;
import models.Event.Trend;

import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.inject.Inject;
import javax.inject.Named;

@Service
public class ThermometerService {

    private final String UNIT;
    private final Double THRESHOLD;
    private final DecimalFormat df;

    @Inject
    public ThermometerService(@Named("thermometer.unit") String unit,
                              @Named("thermometer.threshold") Double threshold,
                              @Named("thermometer.decimalFormat") String decimalFormat) {
        this.UNIT = unit;
        this.THRESHOLD = threshold;
        this.df = new DecimalFormat(decimalFormat);
    }

    /**
     * Core function for iterating over the provided input stream
     * of temperatures and matching all user defined events.
     *
     * @param lines a stream of provided String lines for iterating over
     * @param events a list of user defined events for the system to monitor
     * @return a list of all "triggered" events found when parsing the input
     */
    public List<Event> parseEvents(Stream<String> lines, List<Event> events) {
        List<Event> triggered = new ArrayList<>();
        List<Double> values = lines.map(line -> Double.valueOf(line.split(UNIT)[0]))
                                   .collect(Collectors.toList());

        System.out.println("Parsed input of: " + values.toString());

        Double lastTemp = null;
        for (Double thisTemp: values) {
            if (lastTemp == null) {
                lastTemp = thisTemp;
                continue;
            }

            Event match = matchEvent(events, thisTemp, lastTemp);
            if (match != null && (THRESHOLD != null &&
                                  THRESHOLD.compareTo(Math.abs(Double.valueOf(df.format(thisTemp - lastTemp)))) < 1)) {
                //TODO: Add event specific threshold fluctuation tracking/comparison
//                if (eventExists(triggered, match) && (THRESHOLD))

                triggered.add(match);
            }

            lastTemp = thisTemp;
        }

        return triggered;
    }

    /**
     * Helper function to find the first match in a list of provided, user defined events.
     * This does not account for duplicate events defined with the same temperature, first one wins.
     *
     * @param events list of events to filter on
     * @param thisTemp the value of the current temperature in the iteration
     * @param lastTemp the value of the last temperature in the iteration
     * @return a matching Event object (if present), otherwise null
     */
    public Event matchEvent(List<Event> events, Double thisTemp, Double lastTemp) {
        return events.stream()
                     .filter(event -> thisTemp != null && thisTemp.equals(event.getTemperature()) &&
                                      ((Trend.UP == event.getTrend() && lastTemp < thisTemp) ||
                                       (Trend.DOWN == event.getTrend() && lastTemp > thisTemp)))
                     .findFirst()
                     .orElse(null);
    }

    /**
     * Uses overridden .equals() method in Event.java to decide if we've already triggered that event
     *
     * @param triggered
     * @param event
     * @return
     */
    private boolean eventExists(List<Event> triggered, Event event) {
        return triggered.stream()
                        .anyMatch(e -> e.equals(event));
    }

    /**
     * Used for testing
     * @return
     */
    public String getUnit() {
        return UNIT;
    }

    /**
     * Used for testing
     * @return
     */
    public Double getThreshold() {
        return THRESHOLD;
    }
}
