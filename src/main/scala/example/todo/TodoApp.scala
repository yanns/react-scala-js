package example.todo

import scala.scalajs.js
import js.Dynamic.{ global => g, literal => tojs }
import rx._
import react.{ReactModule, React}
import scala.scalajs.js.annotation.JSExport
import scala.scalajs.js.JSApp

@JSExport
object TodoApp extends JSApp {

  case class Todo(id: Long, label: String, selected: Boolean = false) {
    def change() = copy(selected = !this.selected)
  }

  case class Model(
    benchResult: String = "",
    newTodo: String = "",
    todos: List[Todo] = Nil
  ) {

    def maxTodoId: Long = this.todos match {
      case Nil => 0
      case _ => this.todos.map(_.id).max
    }

    def addTodo() = this.copy(newTodo = "", todos = Todo(maxTodoId + 1, this.newTodo) :: this.todos)
    def deleteSelected() = this.copy(todos = this.todos.filter(_.selected == false))
  }

  val appModel = Var(Model())

  def bench(block: => Unit) = {
    val start = System.currentTimeMillis()
    block
    val duration = System.currentTimeMillis() - start
    appModel() = appModel().copy(benchResult = s"$duration ms")
  }


  val todoRow = new React[Todo] {
    val onChange = (current: Todo) => () => {
      appModel.update {
        val m = appModel()
        m.copy(todos = m.todos.map(t => if (t.eq(current)) t.change() else t ))
      }
    }

    override def render(model: Todo): js.Dynamic = {

      val id = s"todo${model.id}"
      DOM.div(null,
        DOM.input(tojs(
          "type" -> "checkbox",
          "id" -> id,
          "checked" -> model.selected,
          "onChange" -> onChange(model)
        )),
        DOM.label(tojs("htmlFor" -> id), s"${model.label}")
      )
    }
  }

  val newTodo = new React[String] {
    val onChange = (e: js.Dynamic) => {
      appModel.update {
        appModel().copy(newTodo = e.target.value.asInstanceOf[String])
      }
    }

    val handleSubmit = (e: js.Dynamic) => {
      bench {
        e.preventDefault()
        appModel.update(appModel().addTodo())
      }
    }

    val handleDelete = (e: js.Dynamic) => {
      bench {
        e.preventDefault()
        appModel.update(appModel().deleteSelected())
      }
    }

    val selectAll = (e: js.Dynamic) => {
      bench {
        e.preventDefault()
        appModel.update(appModel().copy(todos = appModel().todos.map(_.copy(selected = true))))
      }
    }

    override def render(model: String): js.Dynamic = {
      DOM.div(null,
        DOM.form(tojs("onSubmit" -> handleSubmit),
          DOM.input(tojs(
            "type" -> "text",
            "value" -> model,
            "onChange" -> onChange
          )),
          DOM.button(null, "add TODO")
        ),
        DOM.button(tojs("onClick" -> handleDelete), "deleted selected"),
        DOM.a(tojs("onClick" -> selectAll, "href" -> "#"), "select all")
      )
    }
  }

  val todoList = new React[List[Todo]] {
    override def render(model: List[Todo]): js.Dynamic = {
      val list = model.map { todo => DOM.div(tojs("key" -> todo.id), todoRow(todo)) }
      DOM.div(null, js.Any.fromArray(list.toArray))
    }
  }

  class TodoApp extends React[Model] {

    val bench1 = () => {
      bench {
        // change the model 200 times, like https://github.com/swannodette/todomvc/blob/gh-pages/labs/architecture-examples/om/src/todomvc/app.cljs#L178
        // it would be much more efficient to update to model one time with the new 200 items
        for (i <- 1 to 200) {
          val m = appModel()
          val newId = m.maxTodoId + 1
          appModel() = m.copy(todos = Todo(newId, s"todo $newId") :: m.todos)
        }
      }
    }

    val add200TodosFast = () => {
      bench {
        appModel.update {
          val m = appModel()
          val max = m.maxTodoId
          val newTodos = (for (i <- 1 to 200; newId = max + i) yield Todo(newId, s"todo $newId")).toList
          m.copy(todos = newTodos ::: m.todos)
        }
      }
    }

    val bench2 = () => {
      bench {
        for (i <- 1 to 200) {
          val m = appModel()
          val newId = m.maxTodoId + 1
          appModel() = m.copy(todos = Todo(newId, s"todo $newId") :: m.todos)
        }
        for (i <- 1 to 5) {
          appModel.update {
            val m = appModel()
            m.copy(todos = m.todos.map(_.change()))
          }
        }
        for (i <- 1 to 200) {
          val m = appModel()
          appModel() = m.copy(todos = m.todos.tail)
        }
      }

    }

    override def render(model: Model): js.Dynamic = {
      DOM.div(null,
        DOM.h1(null, "TODOs"),
        DOM.button(tojs("onClick" -> bench1), "add 200 todos"),
        DOM.button(tojs("onClick" -> bench2), "add 200 todos, switch state 5 times, and delete todos"),
        DOM.button(tojs("onClick" -> add200TodosFast), "add 200 todos (fast)"),
        newTodo(model.newTodo),
        DOM.p(null, model.benchResult),
        todoList(model.todos)
      )
    }
  }

  def main(): Unit = {

    new ReactModule(
      model = appModel,
      rootComponent = new TodoApp(),
      domElement = g.document.getElementById("todoapp")
    ).start()

  }
}
