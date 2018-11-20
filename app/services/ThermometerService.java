package services;

import models.Event;
import models.Event.Trend;

import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
     * This includes some logic for tracking existing triggered Events
     * and determining if we care to log an event occurrence if the values
     *
     * @param lines a stream of provided String lines for iterating over
     * @param events a list of user defined events for the system to monitor
     * @return a list of all "triggered" events found when parsing the input
     */
    public List<Event> parseEvents(Stream<String> lines, List<Event> events) {
        List<Event> triggered = new ArrayList<>();
        List<Double> values = lines.map(line -> {
            try {
                return Double.valueOf(line.split(UNIT)[0]);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }).collect(Collectors.toList());

        System.out.println("Parsed input of: " + values.toString());

        Double lastTemp = null;
        values.removeIf(Objects::isNull);
        for (Double thisTemp: values) {
            // Hack to move past the first value in the list so we can start comparisons
            if (lastTemp == null) {
                lastTemp = thisTemp;
                continue;
            }

            // Update fluctuating boolean for each triggered event based on threshold and current temp
            for (Event e : triggered) {
                e.calculateFluctuating(df, THRESHOLD, lastTemp);
            }

            // Attempt to match an event based on the current and previous temp values
            Event match = matchEvent(events, thisTemp, lastTemp);
            if (match != null) {
                // Search for existing Event and bump the occurrence (if not fluctuating in/under the THRESHOLD)
                Event existing = bumpExistingNonFluctuating(triggered, match);

                // If we didn't find a match, add the new Event to the triggered events list and bump the occurrence
                if (existing == null) {
                    match.bumpOccurrences();
                    triggered.add(match);
                }
            }

            // update history
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
     * Helper function..
     * Uses overridden .equals() method in Event.java to decide if we've already triggered that event.
     * Instead of keeping a list of all events that have happened, we're going to only include 1 matching events but instead
     * keep track of the occurrences of that event and bump when we find a match that hasn't been fluctuating.
     *
     * @param triggered current list of triggered events
     * @param event current event that's been triggered
     * @return an existing Event object in the list of triggered events
     */
    private Event bumpExistingNonFluctuating(List<Event> triggered, Event event) {
        Event existing = triggered.stream()
                                  .filter(e -> e.equals(event))
                                  .findFirst()
                                  .orElse(null);

        // If we found an existing Event and we've moved outside the fluctuation threshold, bump occurrences and reset boolean
        if (existing != null && !existing.isFluctuating()) {
            existing.setFluctuating(true);
            existing.bumpOccurrences();
        }

        return existing;
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
