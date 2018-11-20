package controllers;

import models.Event;
import models.Event.Trend;

import play.Logger;
import play.mvc.*;
import play.twirl.api.Html;

import services.ThermometerService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class Thermometer extends Controller {

    private final String UNIT;
    private final Double THRESHOLD;
    private final ThermometerService thermometerService;

    @Inject
    public Thermometer(@Named("thermometer.unit") String unit,
                       @Named("thermometer.threshold") Double threshold,
                       ThermometerService thermometerService) {
        this.UNIT = unit;
        this.THRESHOLD = threshold;

        this.thermometerService = thermometerService;
    }

    /**
     * Empty file default case example endpoint
     *
     * The configuration in the {@code routes} file means that
     * this method will be called when the application receives a
     * {@code GET} request with a path of {@code /}.
     */
    public Result index() {
        try (Stream<String> lines = Files.lines(Paths.get("empty.txt"))) {

            return ok(generateDisplay(lines, new ArrayList<>()));
        } catch (IOException e) {
            Logger.error("Error!", e);
        } catch (Exception e) {
            Logger.error("Uncaught error!", e);
        }

        return badRequest("Your sh*t is broke. Check logs.");
    }

    /**
     * Base case 1, testing one Freezing event when trending down
     *
     * The configuration in the {@code routes} file means that
     * this method will be called when the application receives a
     * {@code GET} request with a path of {@code /case1}.
     */
    public Result case1() {
        try (Stream<String> lines = Files.lines(Paths.get("input.txt"))) {
            List<Event> events = new ArrayList<>();
            events.add(new Event("Freezing", 0.0, Trend.DOWN));

            return ok(generateDisplay(lines, events));
        } catch (IOException e) {
            Logger.error("Error!", e);
        } catch (Exception e) {
            Logger.error("Uncaught error!", e);
        }

        return badRequest("Your sh*t is broke. Check logs.");
    }

    /**
     * Helper for populating scala template display
     *
     * @param lines
     * @param events
     * @return
     */
    private Html generateDisplay(Stream<String> lines, List<Event> events) {
        return views.html.index.render(UNIT, THRESHOLD, thermometerService.parseEvents(lines, events));
    }

}
