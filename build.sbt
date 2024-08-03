ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.4.2"

lazy val root = (project in file("."))
  .settings(
    name := "playground"
  )

libraryDependencies ++= {
  val version = "3.3.4"
  val os = "linux"

  Seq(
    "lwjgl",
    "lwjgl-opengl",
    "lwjgl-glfw"
  ).flatMap {
    module => {
      Seq(
        "org.lwjgl" % module % version,
        "org.lwjgl" % module % version classifier s"natives-$os"
      )
    }
  }
}