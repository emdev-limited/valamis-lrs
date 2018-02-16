package com.arcusys.valamis.lrs.jdbc.history.ver270

import javax.inject.Inject

import com.arcusys.slick.drivers.OracleDriver
import com.arcusys.slick.migration._
import com.arcusys.slick.migration.table.TableMigration
import com.arcusys.valamis.lrs.Lrs
import com.arcusys.valamis.lrs.jdbc._
import com.arcusys.valamis.lrs.jdbc.database.typemap.joda.SimpleJodaSupport
import com.arcusys.valamis.lrs.jdbc.history.BaseDbUpgrade
import org.apache.commons.logging.Log

import scala.slick.driver.JdbcDriver
import scala.slick.jdbc.JdbcBackend
import scala.slick.jdbc.meta.{MIndexInfo, MQName, MTable}
import scala.slick.lifted.AbstractTable


/**
  * Created by Iliya Tryapitsin on 20.04.15.
  */
class DbSchemaUpgrade @Inject()(val jdbcDriver: JdbcDriver,
                                val database: JdbcBackend#Database,
                                val lrs: Lrs,
                                val logger: Log) extends BaseDbUpgrade {
  val jodaSupport = new SimpleJodaSupport(jdbcDriver)

  val dataContext = lrs.asInstanceOf[JdbcLrs]
  val contextActivities = dataContext.contextActivities baseTableRow
  val statements = dataContext.statements baseTableRow
  val context = dataContext.contexts baseTableRow

  def upgradeMigrations = MigrationSeq()

  def downgradeMigrations = throw new Exception("Can't apply migration 'cause it contains irreversible steps")

  import jdbcDriver.simple._

  private lazy val allTables = tables

  def getMQName(name: String)(implicit session: Session): Option[MQName] = {
    jdbcDriver match {
      case OracleDriver => MTable.getTables(cat = Some(""),
        schemaPattern = None,//without schemaPattern = None Oracle doesn't find a table
        namePattern = Some(name),
        types = None).list.headOption.map(_.name)
      case _ => allTables.find(_.name.name == name).map(_.name)
    }
  }

  private def addIndicesForSchema(tableMigration: TableMigration[_ <: AbstractTable[_]], table: TableQuery[_ <: AbstractTable[_]])(implicit session: Session) = {
    val schema = table.baseTableRow
    val tableName = jdbcDriver match {
      case OracleDriver => schema.tableName.toUpperCase
      case _ => schema.tableName
    }

    getMQName(tableName).foreach { tableMQName =>
      val dbIndices = MIndexInfo.getIndexInfo(tableMQName)
        .list.flatMap(_.indexName).distinct


      schema.indexes.foreach { schemaIndex =>
        if (!dbIndices.exists { dbIndexName =>
          dbIndexName.equalsIgnoreCase(schemaIndex.name)
        }) {
          tableMigration.addIndexes(Seq(schemaIndex)).apply()
        }
      }
    }
  }

  def addIndices() = database.withSession { implicit session =>

    addIndicesForSchema(TableMigration(dataContext.contextActivities), dataContext.contextActivities)
    addIndicesForSchema(TableMigration(dataContext.statements), dataContext.statements)
    addIndicesForSchema(TableMigration(dataContext.contexts), dataContext.contexts)
  }

  override def up(lrs: Lrs): Unit = {
    logger.info("Upgrading to 2.7")

    val dataMigration = new DataUpgrade(dataContext, jdbcDriver, database)
    logger.info("Applying data changes")
    dataMigration.upgrade

    logger.info("Applying create index")
    addIndices()
  }

  override def down(lrs: Lrs): Unit = ()
}