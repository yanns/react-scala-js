package react

import scala.scalajs.js
import scala.scalajs.js.Dynamic.{global => g}
import rx.Var

/**
 * A React component that is mounted
 * @param react react component to mount in DOM
 * @param model model of the react component
 * @param domElement dom element where to display to react component
 */
case class MountedReact[M <: AnyRef](react: React[M], model: Var[M], domElement: js.Dynamic) {

  val console = js.Dynamic.global.console

  val mountedReactComponent = g.React.renderComponent(
    react(model()),
    domElement
  )

  /**
   * set props for a mounted component
   */
  def render(): Unit = {
    mountedReactComponent.setProps(react.modelToJS(model()))
  }

}
