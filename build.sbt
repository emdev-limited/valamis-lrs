import sbt._
import sbt.Keys._
import Settings._
import Tasks._

def lrsStorage: Project = Settings.lrsStorage match {
  case Settings.StorageType.jdbcType => `valamis-lrs-jdbc`
  case _ => throw new UnsupportedOperationException("Unsupported data storage")
}

def lfService = Settings.liferay.version match {
  case Settings.Liferay620.version => lfService620
}

lazy val lfService620 = (project in file("liferay/liferay620-services"))
  .settings(commonSettings: _*)
  .settings(disablePublishSettings: _*)
  .settings(organization := "com.arcusys.learn")
  .settings(name := "liferay620-services")
  .settings(libraryDependencies ++= Dependencies.liferay62)

lazy val `valamis-lrs-jdbc` = (project in file("datasources/valamis-lrs-jdbc"))
  .settings(commonSettings: _*)
  .settings(disablePublishSettings: _*)
  .settings(
    name := "valamis-lrs-jdbc",
    libraryDependencies ++= Dependencies.database,
    mappings in(Compile, packageBin) ++= mappings.in(`valamis-lrs-tincan`, Compile, packageBin).value,
    mappings in(Compile, packageBin) ++= mappings.in(`valamis-lrs-util`,   Compile, packageBin).value
  )
  .enablePlugins(SbtOsgi)
  .settings(OsgiKeys.requireCapability :=
    "osgi.ee;filter:=\"(&(osgi.ee=JavaSE)(version=1.7))\"")
  .settings(OsgiKeys.importPackage := Seq("com.arcusys.valamis.lrs.spark.*;resolution:=optional",
    "org.apache.spark.*;resolution:=optional",
    s"""scala.slick.*;version="[${Version.slick},3)"""",
    s"""com.arcusys.slick.migration.*;version="[${Version.slickMigrations},3)"""",
    s"""com.arcusys.slick.drivers.*;version="[${Version.slickDrivers},3)"""",
    "*"))
  .settings(OsgiKeys.exportPackage ++= Seq("com.arcusys.valamis.lrs.jdbc.*",
    "com.arcusys.valamis.lrs.guice"))
  .dependsOn(
    `valamis-lrs-util`,
    `valamis-lrs-tincan`,
    `valamis-lrs-test` % Test
  )


lazy val `valamis-lrs-liferay` = (project in file("valamis-lrs-liferay"))
  .settings(commonSettings: _*)
  .enablePlugins(SbtOsgi)
  .settings(
    name := "valamis-lrs-liferay",
    publishMavenStyle :=  true,
    sqlStatementsTask <<= sqlStatementsGeneration,
    sqlTablesTask     <<= sqlTablesGeneration,
    libraryDependencies ++= Settings.liferay.dependencies,
    libraryDependencies ++= Dependencies.web,
    libraryDependencies ++= Dependencies.metrics,
    makePomConfiguration := makePomConfiguration.value.copy(
      process = PomFilters.dependencies(_)(filterOff = Seq("valamis-lrs-tincan", "valamis-lrs-data-storage", "valamis-lrs-auth"))
    ),
    mappings     in(Compile, packageBin) ++= mappings.in(lrsStorage, Compile, packageBin).value,
    mappings     in(Compile, packageBin) ++= mappings.in(Compile, packageBin).value,
    artifactName in packageWar := { (sv: ScalaVersion, module: ModuleID, artifact: Artifact) =>  "valamis-lrs-portlet." + artifact.extension }
  )
  .settings(postProcess in webapp := { webappDir =>
    IO.delete(webappDir / Settings.liferayPluginPropertiesPath)

    val propertiesContent = Settings.getLiferayPluginProperties(
      webappDir / "../../src/main/webapp/WEB-INF/liferay-plugin-package.properties")

    IO.write(webappDir / Settings.liferayPluginPropertiesPath, propertiesContent)
  })
  //we have to explicitly point out to javax.servlet 3.x version, because
  //otherwise it appears to be 2.6 version which is not available for import resolution
  .settings(OsgiKeys.importPackage := Seq("javax.servlet.*; version=\"[3.0.0,4)\"",
  "org.apache.spark.*;resolution:=optional",
  "com.arcusys.valamis.lrs.spark.*;resolution:=optional",
  "*"))
  .settings(OsgiKeys.requireCapability :=
    "osgi.ee;filter:=\"(&(osgi.ee=JavaSE)(version=1.7))\"")
  .settings(OsgiKeys.privatePackage ++= Seq("html.apps.html.*"))
  .settings(OsgiKeys.exportPackage ++= Seq("com.arcusys.valamis.lrs.liferay.*"))
  .dependsOn(
    `valamis-lrs-util`,
    `valamis-lrs-tincan`,
    `valamis-lrs-protocol`,
    `valamis-lrs-api`  % Test,
    `valamis-lrs-test` % Test,
    lrsStorage,
    lfService
  )
  .enablePlugins(SbtTwirl)

lazy val `valamis-lrs-api` = (project in file("valamis-lrs-api"))
  .settings(commonSettings: _*)
  .settings(
    mappings in(Compile, packageBin) ++=
      mappings.in(`valamis-lrs-tincan`, Compile, packageBin).value ++
      mappings.in(`valamis-lrs-util`,   Compile, packageBin).value,
    name := "valamis-lrs-api",
    libraryDependencies ++= Dependencies.api ++ Dependencies.tincan,
    publishMavenStyle := true,
    makePomConfiguration := makePomConfiguration.value.copy(
      process = PomFilters.dependencies(_)(filterOff = Seq("valamis-lrs-tincan", "valamis-lrs-util"))
    )
  )
  .enablePlugins(SbtOsgi)
  .settings(OsgiKeys.requireCapability :=
    "osgi.ee;filter:=\"(&(osgi.ee=JavaSE)(version=1.7))\"")
  .settings(OsgiKeys.exportPackage ++= Seq("com.arcusys.valamis.lrs.api.*"))
  .dependsOn(
    `valamis-lrs-tincan`
  )

// === Additional project definitions
lazy val `valamis-lrs-tincan` = (project in file("valamis-lrs-tincan"))
  .settings(commonSettings: _*)
  .settings(disablePublishSettings: _*)
  .settings(
    name := "valamis-lrs-tincan",
    libraryDependencies ++= Dependencies.tincan
  )
  .enablePlugins(SbtOsgi)
  .settings(OsgiKeys.requireCapability :=
    "osgi.ee;filter:=\"(&(osgi.ee=JavaSE)(version=1.7))\"")
  .settings(OsgiKeys.exportPackage ++= Seq("com.arcusys.valamis.lrs",
    "com.arcusys.valamis.lrs.exception",
    "com.arcusys.valamis.lrs.security",
    "com.arcusys.valamis.lrs.serializer",
    "com.arcusys.valamis.lrs.tincan.*",
    "com.arcusys.valamis.lrs.validator",
    "com.arcusys.valamis.lrs.util"))
  .dependsOn(
    `valamis-lrs-util`,
    `valamis-lrs-test` % Test
  )

lazy val `valamis-lrs-protocol` = (project in file("valamis-lrs-protocol"))
  .settings(commonSettings: _*)
  .settings(disablePublishSettings: _*)
  .settings(
    name := "valamis-lrs-protocol"
  )
  .enablePlugins(SbtOsgi)
  .settings(OsgiKeys.requireCapability :=
    "osgi.ee;filter:=\"(&(osgi.ee=JavaSE)(version=1.7))\"")
  .settings(OsgiKeys.exportPackage ++= Seq("com.arcusys.valamis.lrs.protocol"))
  .dependsOn(
    `valamis-lrs-util`,
    `valamis-lrs-tincan`,
    `valamis-lrs-test` % Test
  )

lazy val `valamis-lrs-util` = (project in file("valamis-lrs-util"))
  .settings(commonSettings: _*)
  .settings(disablePublishSettings: _*)
  .settings(
    name := "valamis-lrs-util"
  )
  .enablePlugins(SbtOsgi)
  .settings(OsgiKeys.requireCapability :=
    "osgi.ee;filter:=\"(&(osgi.ee=JavaSE)(version=1.7))\"")
  .settings(OsgiKeys.exportPackage ++= Seq("com.arcusys.valamis.lrs.utils"))

lazy val `valamis-lrs-test` = (project in file("valamis-lrs-test"))
  .settings(commonSettings: _*)
  .settings(disablePublishSettings: _*)
  .settings(
    name := "valamis-lrs-test",
    libraryDependencies ++= (Dependencies.database ++ Dependencies.testCluster :+ Libraries.scalatest)
  )
  .dependsOn(`valamis-lrs-util`)

lazy val `valamis-lrs` = (project in file("."))
  .settings(commonSettings: _*)
  .settings(disablePublishSettings: _*)
  .settings(name := "valamis-lrs")
  .aggregate(
    `valamis-lrs-liferay`,
    `valamis-lrs-tincan`,
    lrsStorage,
    `valamis-lrs-test`,
    `valamis-lrs-util`,
    `valamis-lrs-api`,
    `valamis-lrs-protocol`,
    lfService
  )
