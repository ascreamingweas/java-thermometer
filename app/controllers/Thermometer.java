package controllers;

import models.Event;
import models.Event.Trend;

import play.Logger;
import play.mvc.*;
import play.twirl.api.Html;

import com.typesafe.config.Config;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.inject.Inject;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class Thermometer extends Controller {

    private String UNIT;
    private double THRESHHOLD;
    private List<String> outputArray;

    @Inject
    public Thermometer(Config conf) {
        UNIT = conf.getString("thermometer.unit");
        THRESHHOLD = conf.getDouble("thermometer.threshold");
        outputArray = new ArrayList<>();
    }

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result index() {
        try (Stream<String> lines = Files.lines(Paths.get("empty.txt"))) {

            return ok(generateDisplay(lines, new ArrayList<>()));
        } catch (IOException e) {
            Logger.error("Error!", e);
        } catch (Exception e) {
            Logger.error("Uncaught error!", e);
        }

        return badRequest("Your shit is broke.");
    }

    public Result case1() {
        try (Stream<String> lines = Files.lines(Paths.get("input.txt"))) {
            List<Event> events = new ArrayList<>();
            events.add(new Event("Freezing", 0, Trend.DOWN));

            return ok(generateDisplay(lines, events));
        } catch (IOException e) {
            Logger.error("Error!", e);
        } catch (Exception e) {
            Logger.error("Uncaught error!", e);
        }

        return badRequest("Your shit is broke.");
    }

    private Html generateDisplay(Stream<String> lines, List<Event> events) {
        return views.html.index.render(UNIT, THRESHHOLD, outputArray, parseEvents(lines, events));
    }

    public List<Event> parseEvents(Stream<String> lines, List<Event> events) {
        List<Event> triggered = new ArrayList<>();
        List<Double> values = lines.map(line -> Double.valueOf(line.split(UNIT)[0]))
                                   .collect(Collectors.toList());

        Double lastTemp = null;
        for (Double thisTemp: values) {
            if (lastTemp == null) {
                lastTemp = thisTemp;
                continue;
            }

            Event match = matchEvent(events, thisTemp, lastTemp);
            if (match != null) {
                triggered.add(match);
            }

            lastTemp = thisTemp;
        }

        return triggered;
    }

    public Event matchEvent(List<Event> events, Double thisTemp, Double lastTemp) {
        return events.stream()
                     .filter(event -> event.getTemperature() == thisTemp &&
                                      ((Trend.UP == event.getTrend() && thisTemp < lastTemp) ||
                                       (Trend.DOWN == event.getTrend() && thisTemp > lastTemp)))
                     .findFirst()
                     .orElse(null);
    }

//    public double parseLine(String line) {
//        outputArray.add(line);
//        if (line.indexOf(UNIT) > 0) {
//            return ;
//        } else {
//            throw new IllegalArgumentException("Invalid temperature format detected: " + line);
//        }
//    }

}
