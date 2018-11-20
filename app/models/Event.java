package models;

import java.text.DecimalFormat;

public class Event {

    public enum Trend {
        UP("Up"),
        DOWN("Down");

        private String displayName;

        Trend(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return this.displayName;
        }
    }

    private String name;

    private Double temperature;

    private Trend trend;

    private int occurrences = 0;

    private boolean fluctuating = true;

    public Event(String name, Double temperature, Trend trend) {
        this.name = name;
        this.temperature = temperature;
        this.trend = trend;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Trend getTrend() {
        return trend;
    }

    public void setTrend(Trend trend) {
        this.trend = trend;
    }

    public int getOccurrences() {
        return occurrences;
    }

    public void setOccurrences(int occurrences) {
        this.occurrences = occurrences;
    }

    /**
     * Helper to bump the occurrence count
     */
    public void bumpOccurrences() {
        this.occurrences++;
    }

    public boolean isFluctuating() {
        return fluctuating;
    }

    public void setFluctuating(boolean fluctuating) {
        this.fluctuating = fluctuating;
    }

    /**
     * Helper function to update the Event record fluctuating boolean with a comparison
     * of the defined threshold and the absolute value of the difference between current temp and the Event recorded temp
     * @param df
     * @param threshold
     * @param thisTemp
     */
    public void calculateFluctuating(DecimalFormat df, Double threshold, Double thisTemp) {
        if (threshold != null && threshold.compareTo(Math.abs(Double.valueOf(df.format(thisTemp - this.temperature)))) < 1) {
            this.fluctuating = false;
        }
    }

    @Override
    public String toString() {
        return String.format("%s|%f|%s", name, temperature, trend.displayName);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof Event)) {
            return false;
        }

        Event e = (Event)o;
        return e.name.equals(this.name) && e.temperature.equals(this.temperature) && e.trend.equals(this.trend);
    }
}
