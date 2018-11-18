
package views.html

import _root_.play.twirl.api.TwirlFeatureImports._
import _root_.play.twirl.api.TwirlHelperImports._
import _root_.play.twirl.api.Html
import _root_.play.twirl.api.JavaScript
import _root_.play.twirl.api.Txt
import _root_.play.twirl.api.Xml
import models._
import controllers._
import play.api.i18n._
import views.html._
import play.api.templates.PlayMagic._
import java.lang._
import java.util._
import scala.collection.JavaConverters._
import play.core.j.PlayMagicForJava._
import play.mvc._
import play.api.data.Field
import play.mvc.Http.Context.Implicit._
import play.data._
import play.core.j.PlayFormsMagicForJava._

object index extends _root_.play.twirl.api.BaseScalaTemplate[play.twirl.api.HtmlFormat.Appendable,_root_.play.twirl.api.Format[play.twirl.api.HtmlFormat.Appendable]](play.twirl.api.HtmlFormat) with _root_.play.twirl.api.Template4[String,Double,List[String],List[Event],play.twirl.api.HtmlFormat.Appendable] {

  /**/
  def apply/*1.2*/(units:String,
  threshold:Double,
  output:List[String],
  events:List[Event]
):play.twirl.api.HtmlFormat.Appendable = {
    _display_ {
      {


Seq[Any](format.raw/*6.1*/("""
"""),_display_(/*7.2*/main("Thermometer App")/*7.25*/ {_display_(Seq[Any](format.raw/*7.27*/("""
  """),format.raw/*8.3*/("""<h1>Welcome to Justin's Thermometer App</h1>
  <h2>
    <b>Temperature Unit:</b> """),_display_(/*10.31*/units),format.raw/*10.36*/("""<br/>
    <b>Fluctuation Threshold:</b> """),_display_(/*11.36*/threshold),format.raw/*11.45*/("""
  """),format.raw/*12.3*/("""</h2>

  <h3>Input:</h3>
  <p>
    """),_display_(/*16.6*/for(line <- output) yield /*16.25*/ {_display_(Seq[Any](format.raw/*16.27*/("""
      """),format.raw/*17.7*/("""<ul>"""),_display_(/*17.12*/line),format.raw/*17.16*/("""</ul>
    """)))}),format.raw/*18.6*/("""
  """),format.raw/*19.3*/("""</p>

  <h3>Events:</h3>
  <p>
    """),_display_(/*23.6*/for(event <- events) yield /*23.26*/ {_display_(Seq[Any](format.raw/*23.28*/("""
      """),format.raw/*24.7*/("""<ul>New ("""),_display_(/*24.17*/event/*24.22*/.getName),format.raw/*24.30*/(""") Event Occurred! """),_display_(/*24.49*/event/*24.54*/.getTemperature),format.raw/*24.69*/(""" """),_display_(/*24.71*/units),format.raw/*24.76*/(""" """),format.raw/*24.77*/("""reached when trending """),_display_(/*24.100*/event/*24.105*/.getTrend.getDisplayName()),format.raw/*24.131*/("""</ul>
    """)))}),format.raw/*25.6*/("""
  """),format.raw/*26.3*/("""</p>
""")))}),format.raw/*27.2*/("""
"""))
      }
    }
  }

  def render(units:String,threshold:Double,output:List[String],events:List[Event]): play.twirl.api.HtmlFormat.Appendable = apply(units,threshold,output,events)

  def f:((String,Double,List[String],List[Event]) => play.twirl.api.HtmlFormat.Appendable) = (units,threshold,output,events) => apply(units,threshold,output,events)

  def ref: this.type = this

}


              /*
                  -- GENERATED --
                  DATE: Sun Nov 18 10:35:58 PST 2018
                  SOURCE: /Users/jlivingston/Sandbox/thermometer/app/views/index.scala.html
                  HASH: f8d5d6180ea0a81cb39544ff3600c88379940475
                  MATRIX: 980->1|1154->82|1181->84|1212->107|1251->109|1280->112|1389->194|1415->199|1483->240|1513->249|1543->252|1605->288|1640->307|1680->309|1714->316|1746->321|1771->325|1812->336|1842->339|1904->375|1940->395|1980->397|2014->404|2051->414|2065->419|2094->427|2140->446|2154->451|2190->466|2219->468|2245->473|2274->474|2325->497|2340->502|2388->528|2429->539|2459->542|2495->548
                  LINES: 28->1|37->6|38->7|38->7|38->7|39->8|41->10|41->10|42->11|42->11|43->12|47->16|47->16|47->16|48->17|48->17|48->17|49->18|50->19|54->23|54->23|54->23|55->24|55->24|55->24|55->24|55->24|55->24|55->24|55->24|55->24|55->24|55->24|55->24|55->24|56->25|57->26|58->27
                  -- GENERATED --
              */
          