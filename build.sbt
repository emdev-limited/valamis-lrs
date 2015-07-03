import sbt._
import sbt.Keys._
import sbt.classpath.ClasspathUtilities

// === Common settings for all projects
val commonSettings = Seq(
  organization              := "com.arcusys.valamis",
  version                   := "2.4",
  scalaVersion              := Version.scala,
  parallelExecution in Test := false,
  resolvers ++= Seq(
    ArcusysResolvers.mavenCentral,
    ArcusysResolvers.public,
    ArcusysResolvers.releases,
    ArcusysResolvers.typesafeReleases,
    ArcusysResolvers.typesafeSnapshots,
    ArcusysResolvers.snapshots
  ),
  resolvers += Resolver.mavenLocal,
  libraryDependencies ++= Dependencies.common,
  dependencyOverrides  += Libraries.logback,
  javacOptions ++= Seq("-source", "1.6", "-target", "1.6"),
  scalacOptions        += "-target:jvm-1.6"
)

val sqlStatementsTask = TaskKey[Unit]("sql-statements", "Write sql queries for PACL")
val sqlTablesTask     = TaskKey[Unit]("sql-tables",     "Write sql tables for PACL")

val sqlStatementsGeneration: Def.Initialize[Task[Unit]] = (fullClasspath in Runtime) map {classpath =>
  println("Sql queries generation ...")

  val loader: ClassLoader = ClasspathUtilities.toLoader(classpath.map(_.data).map(_.getAbsoluteFile))
  val clss = loader.loadClass("com.arcusys.valamis.lrs.liferay.util.SqlAccessGenerator")
  clss.getMethod("sqlStatements").invoke(clss.newInstance())
}

val sqlTablesGeneration: Def.Initialize[Task[Unit]] = (fullClasspath in Runtime) map {classpath =>
  println("Sql tables generation ...")

  val loader: ClassLoader = ClasspathUtilities.toLoader(classpath.map(_.data).map(_.getAbsoluteFile))
  val clss = loader.loadClass("com.arcusys.valamis.lrs.liferay.util.SqlAccessGenerator")
  clss.getMethod("sqlTables").invoke(clss.newInstance())
}

val publishToNexusSettings = Seq(
  publishTo := {
    if (isSnapshot.value)
      Some(ArcusysResolvers.snapshots)
    else
      Some(ArcusysResolvers.releases)
  }
)

// === Project definitions
lazy val `valamis-lrs-data-storage` = (project in file("valamis-lrs-data-storage"))
  .settings(commonSettings: _*)
  .settings(publishToNexusSettings: _*)
  .settings(
    name := "valamis-lrs-data-storage",
    libraryDependencies ++= Dependencies.database,
    mappings in(Compile, packageBin) ++= mappings.in(`valamis-lrs-tincan`, Compile, packageBin).value,
    makePomConfiguration := makePomConfiguration.value.copy(
      process = PomFilters.dependencies(_)(filterOff = Seq("valamis-lrs-tincan"))
    )
  )
  .dependsOn(
    `valamis-lrs-tincan`,
    `valamis-lrs-test` % Test
  )

lazy val `valamis-lrs-auth` = (project in file("valamis-lrs-auth"))
  .settings(commonSettings: _*)
  .settings(publishToNexusSettings: _*)
  .settings(
    name := "valamis-lrs-auth",
    publishMavenStyle := true,
    libraryDependencies ++= Dependencies.database,
    mappings in(Compile, packageBin) ++= mappings.in(`valamis-lrs-tincan`, Compile, packageBin).value,
    makePomConfiguration := makePomConfiguration.value.copy(
      process = PomFilters.dependencies(_)(filterOff = Seq("valamis-lrs-tincan", "valamis-lrs-util"))
    )
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
    publishMavenStyle := true,
    sqlStatementsTask <<= sqlStatementsGeneration,
    sqlTablesTask     <<= sqlTablesGeneration,
    libraryDependencies ++= Dependencies.web,
    makePomConfiguration := makePomConfiguration.value.copy(
      process = PomFilters.dependencies(_)(filterOff = Seq("valamis-lrs-tincan", "valamis-lrs-data-storage", "valamis-lrs-auth"))
    ),
    mappings     in(Compile, packageBin) ++= mappings.in(`valamis-lrs-data-storage`, Compile, packageBin).value,
    artifactName in packageWar := { (sv: ScalaVersion, module: ModuleID, artifact: Artifact) =>  "valamis-lrs-portlet." + artifact.extension }
  )
  .dependsOn(
    `valamis-lrs-auth`,
    `valamis-lrs-tincan`,
    `valamis-lrs-api` % Test,
    `valamis-lrs-test` % Test,
    `valamis-lrs-data-storage`
  )
  .enablePlugins(SbtTwirl)

lazy val `valamis-lrs-api` = (project in file("valamis-lrs-api"))
  .settings(commonSettings: _*)
  .settings(publishToNexusSettings: _*)
  .settings(
    mappings in(Compile, packageBin) ++= mappings.in(`valamis-lrs-tincan`, Compile, packageBin).value,
    name := "valamis-lrs-api",
    libraryDependencies ++= Dependencies.api,
    publishMavenStyle := true,
    makePomConfiguration := makePomConfiguration.value.copy(
      process = PomFilters.dependencies(_)(filterOff = Seq("valamis-lrs-tincan"))
    )
  )
  .dependsOn(
    `valamis-lrs-tincan`
  )

// === Additional project definitions
lazy val `valamis-lrs-tincan` = (project in file("valamis-lrs-tincan"))
  .settings(commonSettings: _*)
  .settings(
    name := "valamis-lrs-tincan",
    publishMavenStyle := true,
    makePomConfiguration := makePomConfiguration.value.copy(
      process = PomFilters.dependencies(_)(filterOff = Seq("valamis-lrs-util"))
    ),
    mappings in(Compile, packageBin) ++= mappings.in(`valamis-lrs-util`, Compile, packageBin).value,
    libraryDependencies ++= Dependencies.tincan
  )
  .dependsOn(
    `valamis-lrs-util`,
    `valamis-lrs-test` % Test
  )

lazy val `valamis-lrs-util` = (project in file("valamis-lrs-util")).
  settings(commonSettings: _*).
  settings(
    name := "valamis-lrs-util",
    libraryDependencies ++= Dependencies.util
  )

lazy val `valamis-lrs-test` = (project in file("valamis-lrs-test")).
  settings(commonSettings: _*).
  settings(
    name := "valamis-lrs-test",
    libraryDependencies ++= Dependencies.database
  ) dependsOn `valamis-lrs-util`

lazy val `valamis-lrs` = (project in file(".")).
  settings(commonSettings: _*).
  settings(name := "valamis-lrs").
  aggregate(
    `valamis-lrs-data-storage`,
    `valamis-lrs-liferay`,
    `valamis-lrs-tincan`,
    `valamis-lrs-auth`,
    `valamis-lrs-test`,
    `valamis-lrs-util`,
    `valamis-lrs-api`
  )
