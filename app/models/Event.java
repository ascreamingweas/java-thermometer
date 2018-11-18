package models;

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

    private double temperature;

    private Trend trend;

    public Event(String name, double temperature, Trend trend) {
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

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public Trend getTrend() {
        return trend;
    }

    public void setTrend(Trend trend) {
        this.trend = trend;
    }
}
