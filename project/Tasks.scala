import sbt.classpath.ClasspathUtilities
import sbt.complete.Parsers.spaceDelimited
import sbt._
import Keys._
import complete.Parser
import complete.DefaultParsers._

object Tasks {
  val sqlStatementsTask = TaskKey[Unit]("sql-statements", "Write sql queries for PACL")
  val sqlTablesTask = TaskKey[Unit]("sql-tables", "Write sql tables for PACL")

  val sqlStatementsGeneration: Def.Initialize[Task[Unit]] = (fullClasspath in Runtime) map { classpath =>
    println("Sql queries generation ...")

    val loader: ClassLoader = ClasspathUtilities.toLoader(classpath.map(_.data).map(_.getAbsoluteFile))
    val clss = loader.loadClass("com.arcusys.valamis.lrs.liferay.util.SqlAccessGenerator")
    clss.getMethod("sqlStatements").invoke(clss.newInstance())
  }

  val sqlTablesGeneration: Def.Initialize[Task[Unit]] = (fullClasspath in Runtime) map { classpath =>
    println("Sql tables generation ...")

    val loader: ClassLoader = ClasspathUtilities.toLoader(classpath.map(_.data).map(_.getAbsoluteFile))
    val clss = loader.loadClass("com.arcusys.valamis.lrs.liferay.util.SqlAccessGenerator")
    clss.getMethod("sqlTables").invoke(clss.newInstance())
  }

}
