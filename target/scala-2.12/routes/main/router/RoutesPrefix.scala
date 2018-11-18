// @GENERATOR:play-routes-compiler
// @SOURCE:/Users/jlivingston/Sandbox/thermometer/conf/routes
// @DATE:Sun Nov 18 10:24:18 PST 2018


package router {
  object RoutesPrefix {
    private var _prefix: String = "/"
    def setPrefix(p: String): Unit = {
      _prefix = p
    }
    def prefix: String = _prefix
    val byNamePrefix: Function0[String] = { () => prefix }
  }
}
