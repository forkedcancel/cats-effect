val V = _root_.scalafix.sbt.BuildInfo

inThisBuild(
  List(
    scalaVersion := V.scala213,
    addCompilerPlugin(scalafixSemanticdb)
  )
)

lazy val rules = project.settings(
  libraryDependencies += "ch.epfl.scala" %% "scalafix-core" % V.scalafixVersion
)

lazy val v3_0_0_input = project
  .in(file("v3_0_0/input"))
  .settings(
    libraryDependencies ++= Seq(
      "org.typelevel" %% "cats-effect" % "2.3.3"
    ),
    scalacOptions += "-P:semanticdb:synthetics:on"
  )

lazy val v3_0_0_output = project
  .in(file("v3_0_0/output"))
  .settings(
    libraryDependencies ++= Seq(
      "org.typelevel" %% "cats-effect" % "3.0.0-RC2"
    )
  )

lazy val v3_0_0_tests = project
  .in(file("v3_0_0/tests"))
  .settings(
    libraryDependencies += "ch.epfl.scala" % "scalafix-testkit" % V.scalafixVersion % Test cross CrossVersion.full,
    Compile / compile :=
      (Compile / compile).dependsOn(v3_0_0_input / Compile / compile).value,
    scalafixTestkitOutputSourceDirectories :=
      (v3_0_0_output / Compile / sourceDirectories).value,
    scalafixTestkitInputSourceDirectories :=
      (v3_0_0_input / Compile / sourceDirectories).value,
    scalafixTestkitInputClasspath :=
      (v3_0_0_input / Compile / fullClasspath).value
  )
  .dependsOn(v3_0_0_input, rules)
  .enablePlugins(ScalafixTestkitPlugin)
