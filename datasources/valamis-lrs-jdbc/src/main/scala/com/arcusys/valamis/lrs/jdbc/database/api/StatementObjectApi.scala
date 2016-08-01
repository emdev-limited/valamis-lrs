package com.arcusys.valamis.lrs.jdbc.database.api

import com.arcusys.valamis.lrs._
import com.arcusys.valamis.lrs.jdbc.database.converter._
import com.arcusys.valamis.lrs.jdbc.database.LrsDataContext
import com.arcusys.valamis.lrs.jdbc.database.api.query.StatementObjectQueries
import com.arcusys.valamis.lrs.jdbc.database.row.StatementObjectRow
import com.arcusys.valamis.lrs.tincan._
import scala.concurrent._
import scala.async.Async.async

/**
 * LRS component for a Tincan [[StatementObject]]
 * @author Created by Iliya Tryapitsin on 08.07.15.
 */
trait StatementObjectApi
  extends StatementObjectQueries {
  this: LrsDataContext
    with SubStatementApi
    with ActivityApi
    with StatementRefApi
    with ActorApi =>

  import driver.simple._
  implicit def executor: ExecutionContextExecutor

  /**
   * Find Tincan [[StatementObject]] by storage key
   * @param key Storage key
   * @param session
   * @return Tincan [[StatementObject]] instance
   */
  def findStatementObjectByKey (key: StatementObjectRow#Type)
                               (implicit session: Session): StatementObject =
    getStatementObjectTypeQC (key) first match {
      case StatementObjectType.Agent              => findAgentByKey       (key)
      case StatementObjectType.Group              => findGroupByKey       (key)
      case StatementObjectType.Activity           => findActivityByKey    (key)
      case StatementObjectType.StatementReference => findStatementRefByKey(key)
      case StatementObjectType.SubStatement       => findSubStatementByKey(key)
    }

  /**
   * Load Tincan [[StatementObject]] by list of storage keys
   * @param keys Storage keys list
   * @param session
   * @return [[StatementObject]]s with storage keys list
   */
  def findStatementObjectsByKeysAsync (keys: Seq[StatementObjectRow#Type])
                                      (implicit session: Session): Future[Map[StatementObjectRow#Type, StatementObject]] = {
    val objects = findStatementObjectTypesQ (keys).run

    val actorsTask = async {
      objects filter { it =>
        it.objectType == StatementObjectType.Agent ||
        it.objectType == StatementObjectType.Group

      } map {
        it => it.key get

      } afterThat findActorsByKeys
    }

    val activitiesTask = async {
      objects filter {
        it => it.objectType == StatementObjectType.Activity

      } map {
        it => it.key get

      } afterThat findActivitiesByKeys
    }

    val subStatementsTask = async {
      objects filter {
        it => it.objectType == StatementObjectType.SubStatement

      } map {
        it => it.key get

      } afterThat findSubStatementsByKeys
    }

    val statementRefsTask = async {
      objects filter {
        it => it.objectType == StatementObjectType.StatementReference
      } map {
        it => it.key get

      } afterThat findStatementRefsByKeys
    }

    val tasks = subStatementsTask ::
      statementRefsTask           ::
      activitiesTask              ::
      actorsTask                  ::
      Nil

    Future sequence tasks map {
      x => x flatMap identity toMap
    }
  }

  def findStatementObjectsByKeys (keys: Seq[StatementObjectRow#Type])
                                      (implicit session: Session): Map[StatementObjectRow#Type, StatementObject] = {
    val objects = findStatementObjectTypesQ (keys).run

    val actorsTask =
      objects filter { it =>
        it.objectType == StatementObjectType.Agent ||
        it.objectType == StatementObjectType.Group

      } map {
        it => it.key get

      } afterThat findActorsByKeys

    val activitiesTask =
      objects filter {
        it => it.objectType == StatementObjectType.Activity

      } map {
        it => it.key get

      } afterThat findActivitiesByKeys

    val subStatementsTask =
      objects filter {
        it => it.objectType == StatementObjectType.SubStatement

      } map {
        it => it.key get

      } afterThat findSubStatementsByKeys

    val statementRefsTask =
      objects filter {
        it => it.objectType == StatementObjectType.StatementReference
      } map {
        it => it.key get

      } afterThat findStatementRefsByKeys

    val tasks = subStatementsTask ::
      statementRefsTask           ::
      activitiesTask              ::
      actorsTask                  ::
      Nil

    tasks flatMap identity toMap
  }


  implicit class StatementObjectInsertQueries (q: StatementObjQ) {

    /**
     * Insert Tincan [[StatementObject]] to the storage
     * @param obj [[StatementObject]] instance
     * @param session
     * @return Identity key in the storage
     */
    def add (obj: StatementObject)
            (implicit session: Session): StatementObjectRow#Type =
      q returning q.map { x =>
        x.key
      } += obj.toStatementObj

    /**
     * Insert list of Tincan Statement objects to the storage
     * @param objs [[StatementObject]] instance
     * @param session
     * @return Identity keys in the storage
     */
    def addSeq (objs: Seq[StatementObject])
               (implicit session: Session): Seq[StatementObjectRow#Type] =
      q returning q.map { x =>
        x.key
      } ++= objs.map { x =>
        x.toStatementObj
      }

    /**
     * Insert general Tincan [[StatementObject]] to the storage
     * @param s [[StatementObject]] instance
     * @param session
     * @return Identity key in the storage
     */
    def addExt (s: StatementObject)
               (implicit session: Session): StatementObjectRow#Type = s match {
      case o: SubStatement => subStatements add o
      case o: Activity     => activities    addOrUpdate o
      case o: Agent        => actors        addUnique o
      case o: Group        => actors        addUnique o
      case o: StatementReference => statementReferences addUnique o
      case _  => throw new IllegalArgumentException("Unknown statement object type")
    }
  }
}
