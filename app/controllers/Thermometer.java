package controllers;

import models.Event;
import models.Event.Trend;

import play.Logger;
import play.mvc.*;
import play.twirl.api.Html;

import com.typesafe.config.Config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.inject.Inject;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class Thermometer extends Controller {

    private static String regex = "[-]?[/d]*.?[/d]* [CF]";
    private static String format = "\\[temp:(?<temp>[A-Z]+)\\]\\[unit:(?<unit>\\d+)\\]";

    private String UNIT;
    private double THRESHHOLD;

    @Inject
    public Thermometer(Config conf) {
        UNIT = conf.getString("thermometer.unit");
        THRESHHOLD = conf.getDouble("thermometer.threshold");


    }

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result index() {
        try {
            return ok(generateDisplay(new ArrayList<>()));
        } catch (IOException e) {
            Logger.error("Error!", e);
        } catch (Exception e) {
            Logger.error("Uncaught error!", e);
        }

        return badRequest("Your shit is broke.");
    }

    public Result case1() {
        try {
            List<Event> events = new ArrayList<>();
            events.add(new Event("Freezing", 0, Trend.DOWN));
            return ok(generateDisplay(events));
        } catch (IOException e) {
            Logger.error("Error!", e);
        } catch (Exception e) {
            Logger.error("Uncaught error!", e);
        }

        return badRequest("Your shit is broke.");
    }

    private Html generateDisplay(List<Event> events) throws IOException {
        File input = new File("input.txt");
        Logger.info("Opened file!");
        Scanner scanner = new Scanner(input);

        List<String> outputArray = new ArrayList<>();
        List<Event> triggeredEvents = scanner.hasNextLine() ?
                        parseEvents(scanner, events, new ArrayList<>(), parseLine(scanner)) : new ArrayList<>();

        return views.html.index.render(UNIT, THRESHHOLD, outputArray, triggeredEvents);
    }

    private List<Event> parseEvents(Scanner scanner, List<Event> events, List<Event> triggered, double thisTemp) {
        if (!scanner.hasNextLine()) {
            return triggered;
        }

        double nextTemp = parseLine(scanner);

        events.stream()
              .filter(event -> event.getTemperature() == nextTemp)
              .findFirst()
              .ifPresent(event -> {
                  if ((Trend.UP == event.getTrend() && thisTemp < nextTemp) ||
                      (Trend.DOWN == event.getTrend() && thisTemp > nextTemp)) {
                    triggered.add(event);
                  }
              });

        return parseEvents(scanner, events, triggered, nextTemp);
    }

    private double parseLine(Scanner scanner) {
        String line = scanner.nextLine();
        if (line.indexOf(UNIT) > 0) {
            return Double.valueOf(line.split(UNIT)[0]);
        } else {
            throw new IllegalArgumentException("Invalid temperature format detected: " + line);
        }
    }

}
