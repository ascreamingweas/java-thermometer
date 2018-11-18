// @GENERATOR:play-routes-compiler
// @SOURCE:/Users/jlivingston/Sandbox/thermometer/conf/routes
// @DATE:Sun Nov 18 10:24:18 PST 2018

package controllers;

import router.RoutesPrefix;

public class routes {
  
  public static final controllers.ReverseThermometer Thermometer = new controllers.ReverseThermometer(RoutesPrefix.byNamePrefix());
  public static final controllers.ReverseAssets Assets = new controllers.ReverseAssets(RoutesPrefix.byNamePrefix());

  public static class javascript {
    
    public static final controllers.javascript.ReverseThermometer Thermometer = new controllers.javascript.ReverseThermometer(RoutesPrefix.byNamePrefix());
    public static final controllers.javascript.ReverseAssets Assets = new controllers.javascript.ReverseAssets(RoutesPrefix.byNamePrefix());
  }

}
