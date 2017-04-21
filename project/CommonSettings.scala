import sbt._
import Keys._

// === Common settings for all projects
object Settings {
  val graphSettings = net.virtualvoid.sbt.graph.Plugin.graphSettings

  val liferay = Liferay620

  val commonSettings = Seq(
    organization := "com.arcusys.valamis",
    version := Version.project,
    scalaVersion := Version.scala,
    parallelExecution in Test := false,
    resolvers ++= Seq(
      ArcusysResolvers.mavenCentral,
      ArcusysResolvers.public,
      ArcusysResolvers.typesafeReleases,
      ArcusysResolvers.typesafeSnapshots,
      ArcusysResolvers.liferayPublic
    ),
    resolvers += Resolver.mavenLocal,
    libraryDependencies ++= Dependencies.common,
    updateOptions := updateOptions.value.withLatestSnapshots(false),
    javacOptions        ++= Seq("-source", "1.6", "-target", "1.6"),
    scalacOptions        += "-target:jvm-1.6"
  ) ++ graphSettings

  val disablePublishSettings = Seq(
    publish := {},
    publishLocal := {},
    publishM2:= {}
  )

  val lrsStorage = StorageType.jdbcType

  object StorageType {
    val jdbcType = "jdbc"
  }

  object Liferay620 {
    val dependencies = Dependencies.liferay62
    val supportVersion = "6.2.5"
    val version = Version.liferayPortal62
  }

  val liferayPluginProperties = LiferayPluginProperties

  object LiferayPluginProperties {
    val longDescription = "Valamis LRS"
    val pageUrl = "http://valamis.arcusys.com/"
    val tags = "valamis,tincan,eLearning,xApi"
    val author="Arcusys Oy."
    val version = Version.project
  }

  val liferayPluginPropertiesPath =  "WEB-INF/liferay-plugin-package.properties"

  def getLiferayPluginProperties(source: File): String = {

    IO.read(source)
      .replace("${supported.liferay.versions}", Settings.liferay.supportVersion)
      .replace("${properties.longDescription}", Settings.liferayPluginProperties.longDescription)
      .replace("${properties.pageUrl}", Settings.liferayPluginProperties.pageUrl)
      .replace("${properties.tags}", Settings.liferayPluginProperties.tags)
      .replace("${properties.author}", Settings.liferayPluginProperties.author)
      .replace("${properties.version}", Settings.liferayPluginProperties.version)
  }

}
