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

    private Double temperature;

    private Trend trend;

    private boolean fluctuating = false;

    public Event(String name, Double temperature, Trend trend) {
        this.name = name;
        this.temperature = temperature;
        this.trend = trend;
    }

    public Event(String name, Double temperature, Trend trend, boolean fluctuating) {
        this.name = name;
        this.temperature = temperature;
        this.trend = trend;
        this.fluctuating = fluctuating;
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

    public boolean isFluctuating() {
        return fluctuating;
    }

    public void setFluctuating(boolean fluctuating) {
        this.fluctuating = fluctuating;
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
