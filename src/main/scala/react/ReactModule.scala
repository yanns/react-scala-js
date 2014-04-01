package react

import rx._
import scala.scalajs.js.Dynamic._
import scala.scalajs.js
import scala.scalajs.js.Dynamic.{global => g, _}

/**
 * @param model model for a module
 * @param rootComponent the root React component that is attached to a DOM element
 * @param domElement DOM element to render the root component
 */
case class ReactModule[M <: AnyRef](
   model: Var[M],
   rootComponent: React[M],
   domElement: js.Dynamic) {

  def start(): Obs = {
    val mountedComp = MountedReact(rootComponent, model, domElement)
    ReactRendering.mountedComponents +=  mountedComp

    Obs(model, skipInitial = true) {
      ReactRendering.triggerRender()
    }
  }

}
