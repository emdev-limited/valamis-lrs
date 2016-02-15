import sbt._, Keys._

// === Common settings for all projects
object Settings {
  val graphSettings = net.virtualvoid.sbt.graph.Plugin.graphSettings

  val commonSettings = Seq(
    organization := "com.arcusys.valamis",
    version := "2.6.1",
    scalaVersion := Version.scala,
    crossScalaVersions := Seq(Version.scala, "2.10.5"),
    parallelExecution in Test := false,
    resolvers ++= Seq(
      DefaultMavenRepository,
      ArcusysResolvers.public,
      ArcusysResolvers.typesafeReleases,
      ArcusysResolvers.typesafeSnapshots
    ),
    resolvers += Resolver.mavenLocal,
    libraryDependencies ++= Dependencies.common,
    dependencyOverrides  += Libraries.logback,
    javacOptions        ++= Seq("-source", "1.6", "-target", "1.6"),
    scalacOptions        += "-target:jvm-1.6",
    ivyScala := ivyScala.value map { _.copy(overrideScalaVersion = true) }
  ) ++ graphSettings

  val disablePublishSettings = Seq(
    publish := {},
    publishLocal := {},
    publishM2:= {}
  )

}