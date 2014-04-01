package example.helloworld

import scala.scalajs.js
import js.Dynamic.{ global => g, literal => tojs }
import rx._
import react.{ReactModule, React}
import scala.scalajs.js.annotation.JSExport
import scala.scalajs.js.JSApp

@JSExport
object HelloWorldExample extends JSApp {

  val appModel = Var("world")

  class HelloWorld extends React[String] {

    val onClick = () => {
      if (appModel() == "world") {
        appModel() = "universe"
      } else {
        appModel() = "world"
      }
      false.asInstanceOf[js.Boolean]
    }

    override def render(name: String): js.Dynamic = {
      DOM.div(null,
        DOM.h1(null, s"Hello, $name!"),
        DOM.a(tojs(
          "href" -> "#",
          "onClick" -> onClick
        ), "click here")
      )
    }
  }

  def main(): Unit = {

    new ReactModule(
      model = appModel,
      rootComponent = new HelloWorld(),
      domElement = g.document.getElementById("helloworld")
    ).start()

  }
}
