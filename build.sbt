// Turn this project into a Scala.js project by importing these settings
scalaJSSettings

name := "react-scala-js"

version := "0.1-SNAPSHOT"

libraryDependencies ++= Seq(
  "com.scalarx" %% "scalarx" % "0.2.4-JS",
  "org.webjars" % "react" % "0.9.0",
  "org.scala-lang.modules.scalajs" %% "scalajs-jasmine-test-framework" % scalaJSVersion % "test"
)
