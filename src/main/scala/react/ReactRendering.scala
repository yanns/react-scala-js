package react

import scala.scalajs.js
import scala.scalajs.js.Dynamic.{global => g}
import scala.collection.mutable.ArrayBuffer

object ReactRendering {

  // register for all mounted components
  var mountedComponents = ArrayBuffer.empty[MountedReact[_]]

  private var renderTriggered = false

  val renderAll = () => {
    mountedComponents.foreach(_.render())
    renderTriggered = false
  }

  // does the browser support requestAnimationFrame? (http://caniuse.com/#feat=requestanimationframe)
  private val useRequestAnimationFrame = g.window.requestAnimationFrame.isInstanceOf[js.Function]

  def triggerRender(): Unit = {
    if (!renderTriggered) {
      renderTriggered = true
      if (useRequestAnimationFrame) {
        g.window.requestAnimationFrame(renderAll)
      } else {
        g.window.setTimeout(renderAll, 16)
      }
    }
  }



}
