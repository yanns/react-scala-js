package react

import scala.scalajs.js
import js.Dynamic.{ global => g }

/**
 * wrapper for a JavaScript React class
 * @tparam M Model of the React component
 */
trait React[M <: AnyRef] {

  val console = js.Dynamic.global.console
  val logEnabled = false

  val componentName = this.getClass.getName

  // render

  val DOM = g.React.DOM

  private val renderJS = (comp: js.Dynamic) => {
    val _model = comp.props.m
    render(_model.asInstanceOf[M])
  }

  /**
   * renders the model
   * @param model model that can be rendered.
   * @return virtual dom (like g.React.DOM...)
   */
  def render(model: M): js.Dynamic

  // shouldComponentUpdate

  private val shouldComponentUpdateJS = (comp: js.Dynamic, nextProps: js.Dynamic, nextState: js.Dynamic) => {
    val model = comp.props.m.asInstanceOf[M]
    val nextModel = nextProps.m.asInstanceOf[M]
    val shouldUpdate = shouldComponentUpdate(model, nextModel)
    if (logEnabled && shouldUpdate) {
      console.log(s"component '$componentName' should be updated\n    model='$model'\nnextModel='$nextModel']")
    }
    shouldUpdate
  }

  def shouldComponentUpdate(model: M, nextModel: M): Boolean = {
    // instance equality when using immutable model
    model.ne(nextModel)
  }

  // React component

  val reactComponent = g.React.createClass(js.Dynamic.literal(
    "displayName" -> componentName,
    "render" -> (renderJS: js.ThisFunction),
    "shouldComponentUpdate" -> (shouldComponentUpdateJS: js.ThisFunction)
  ))

  /**
   * render the component.
   * @param model model to render. This model must be immutable.
   */
  def apply(model: M): js.Dynamic = {
    reactComponent(modelToJS(model))
  }

  // helpers

  def modelToJS(model: M): js.Dynamic = {
    js.Dynamic.literal(
      "m" -> model.asInstanceOf[js.Any]
    )
  }
}



