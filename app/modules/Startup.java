package modules;

import play.Environment;
import play.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import com.typesafe.config.Config;

public class Startup extends AbstractModule {

    private final Config conf;

    public Startup(Environment environment, Config config) {
        this.conf = config;
    }

    @Override
    protected void configure() {
        // Bind specific Play properties to Named strings
        final String[] props = {"thermometer.unit", "thermometer.threshold", "thermometer.decimalFormat"};

        for (String propName : props) {
            String propValue = conf.getString(propName);
            if (propValue == null) {
                Logger.error("Null was found for {} in the conf file", propName);
            } else {
                bind(String.class).annotatedWith(Names.named(propName)).toInstance(propValue);
            }
        }

        // trigger setup methods in this module
        requestInjection(this);
    }
}
