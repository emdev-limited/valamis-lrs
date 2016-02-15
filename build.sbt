import sbt._
import sbt.Keys._
import Settings._
import Tasks._

lazy val `valamis-lrs-jdbc` = (project in file("datasources/valamis-lrs-jdbc"))
  .settings(commonSettings: _*)
  .settings(disablePublishSettings: _*)
  .settings(
    name := "valamis-lrs-jdbc",
    libraryDependencies ++= Dependencies.database,
    mappings in(Compile, packageBin) ++= mappings.in(`valamis-lrs-tincan`, Compile, packageBin).value,
    mappings in(Compile, packageBin) ++= mappings.in(`valamis-lrs-util`,   Compile, packageBin).value
  )
  .dependsOn(
    `valamis-lrs-util`,
    `valamis-lrs-tincan`,
    `valamis-lrs-test` % Test
  )

lazy val `valamis-lrs-liferay` = (project in file("valamis-lrs-liferay"))
  .settings(commonSettings: _*)
  .settings(publishToNexusSettings: _*)
  .settings(
    name := "valamis-lrs-liferay",
    publishMavenStyle :=  true,
    sqlStatementsTask <<= sqlStatementsGeneration,
    sqlTablesTask     <<= sqlTablesGeneration,
    libraryDependencies ++= Dependencies.liferay62,
    makePomConfiguration := makePomConfiguration.value.copy(
      process = PomFilters.dependencies(_)(filterOff = Seq("valamis-lrs-tincan", "valamis-lrs-data-storage", "valamis-lrs-auth"))
    ),
    mappings     in(Compile, packageBin) ++= mappings.in(`valamis-lrs-jdbc`, Compile, packageBin).value,
    artifactName in packageWar := { (sv: ScalaVersion, module: ModuleID, artifact: Artifact) =>  "valamis-lrs-portlet." + artifact.extension }
  )
  .dependsOn(
    `valamis-lrs-util`,
    `valamis-lrs-tincan`,
    `valamis-lrs-protocol`,
    `valamis-lrs-api`  % Test,
    `valamis-lrs-test` % Test,
    `valamis-lrs-jdbc`
  )
  .enablePlugins(SbtTwirl)

lazy val `valamis-lrs-api` = (project in file("valamis-lrs-api"))
  .settings(commonSettings: _*)
  .settings(publishToNexusSettings: _*)
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

lazy val `valamis-lrs-test` = (project in file("valamis-lrs-test"))
  .settings(commonSettings: _*)
  .settings(disablePublishSettings: _*)
  .settings(
    name := "valamis-lrs-test",
    libraryDependencies ++= (Dependencies.database ++ Dependencies.testCluster)
  )
  .dependsOn(`valamis-lrs-util`)

lazy val `valamis-lrs` = (project in file("."))
  .settings(commonSettings: _*)
  .settings(disablePublishSettings: _*)
  .settings(name := "valamis-lrs")
  .aggregate(
    `valamis-lrs-liferay`,
    `valamis-lrs-tincan`,
    `valamis-lrs-jdbc`,
    `valamis-lrs-test`,
    `valamis-lrs-util`,
    `valamis-lrs-api`
  )
