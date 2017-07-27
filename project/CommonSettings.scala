import sbt._
import Keys._

import com.arcusys.sbt.tasks._

import com.arcusys.sbt.keys.CommonKeys
import com.arcusys.sbt.keys.OsgiCommonKeys
import com.arcusys.sbt.keys.DeployKeys

// === Common settings for all projects
object Settings {
  val graphSettings = net.virtualvoid.sbt.graph.Plugin.graphSettings

  val liferay = Liferay620

  val commonSettings = Seq(
    CommonKeys.lfVersion := liferay.version,
    DeployKeys.lf6Version := Liferay620.version,
    OsgiCommonKeys.lf7Version := Liferay700.version,
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
    javacOptions ++= Seq("-source", "1.6", "-target", "1.6"),
    scalacOptions += "-target:jvm-1.6"
  ) ++ graphSettings

  val publishToNexusSettings = Seq(
    publishTo := {
      Some(ArcusysResolvers.public)
    }
  )

  val disablePublishSettings = Seq(
    publish := {},
    publishLocal := {},
    publishM2 := {}
  )

  val lrsStorage = StorageType.jdbcType

  object StorageType {
    val jdbcType = "jdbc"
  }

  object Liferay620 {
    val dependencies = Dependencies.liferay62
    val supportVersion = "6.2.0+,6.2.10+"
    val version = Version.liferayPortal62
  }

  object Liferay700 {
    val dependencies = Dependencies.liferay70
    val supportVersion = "7.0.0+"
    val version = Version.liferayPortal70
  }

  val liferayPluginProperties = LiferayPluginProperties

  object LiferayPluginProperties {
    val longDescription = "Valamis LRS"
    val pageUrl = "http://valamis.arcusys.com/"
    val tags = "valamis,tincan,eLearning,xApi"
    val author = "Arcusys Oy."
    val version = Version.project
  }

  val liferayPluginPropertiesPath = "WEB-INF/liferay-plugin-package.properties"

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
