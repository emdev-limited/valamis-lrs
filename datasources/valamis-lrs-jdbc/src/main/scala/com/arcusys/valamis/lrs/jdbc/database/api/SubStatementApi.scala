package com.arcusys.valamis.lrs.jdbc.database.api

import com.arcusys.valamis.lrs._
import com.arcusys.valamis.lrs.jdbc.database.converter._
import com.arcusys.valamis.lrs.jdbc.database.row.SubStatementRow
import com.arcusys.valamis.lrs.jdbc.database.LrsDataContext
import com.arcusys.valamis.lrs.jdbc.database.api.query.SubStatementQueries
import com.arcusys.valamis.lrs.jdbc.database.row.{ActorRow, SubStatementRow}
import com.arcusys.valamis.lrs.tincan.{Actor, SubStatement}

import scala.async.Async
import scala.concurrent.Await

/**
 * LRS component for a Tincan [[SubStatement]]
 * @author Created by Iliya Tryapitsin on 08.07.15.
 */
trait SubStatementApi extends SubStatementQueries {
  this: LrsDataContext
    with StatementObjectApi
    with ActorApi =>

  import executionContext.driver.simple._

  /**
   * Load Tincan [[SubStatement]]s by storage keys
   * @param keys Storage keys
   * @param session
   * @return Tincan [[SubStatement]] with key list
   */
  def findSubStatementsByKeys (keys: Seq[SubStatementRow#Type])
                              (implicit session: Session): Map[SubStatementRow#Type, SubStatement] =
    if (keys isEmpty) Map()
    else {
      val subStatementRecs = findSubStatementsByKeysQ (keys).run
      val actorRecs = findActorsByKeys ( subStatementRecs map { _.actorKey  } )
      val statementObjRecsTask = findStatementObjectsByKeysAsync ( subStatementRecs map { _.objectKey } )

      subStatementRecs map { rec =>
        val inst = rec.convert withActor {
          actorRecs get rec.actorKey get

        } withStatementObject {
          Await.result(statementObjRecsTask, timeout) get rec.objectKey get

        } build

        (rec.key, inst)
      } toMap
    }

  /**
   * Find Sub Statement Keys by Actor Key
   * @param actor Actor Key
   * @param session
   * @return Tincan Sub Statement keys list
   */
  def findSubStatementKeysByActorKey (actor: ActorRow#Type)
                                     (implicit session: Session) =
    findSubStatementKeysByActorIdQC (actor) run

  /**
   * Find Tincan Sub Statement by storage key
   * @param key Storage key
   * @param session
   * @return Tincan Sub Statement
   */
  def findSubStatementByKey (key: SubStatementRow#Type)
                                 (implicit session: Session): SubStatement =
    findSubStatementByKeyQC (key).first then { rec =>
      rec.convert withActor {
        findActorByKey (rec.actorKey)
      } withStatementObject {
        findStatementObjectByKey (rec.objectKey)
      } build
    }

  implicit class SubStatementInsertQueries (q: SubStatementQ) {

    /**
     * Insert Tincan Sub-statement to the storage
     * @param s Sub-statement instance
     * @param session
     * @return Identity key in the storage
     */
    def add (s: SubStatement)
            (implicit session: Session): SubStatementRow#Type =
      s.convert withKey {
        statementObjects add s

      } withObjectKey {
        statementObjects addExt s.obj

      } withActorKey {
        actors addUnique s.actor

      } build { r =>
        q += r
      }

    def keyForQ (actor: Actor) =
      q filter { it =>
        it.actorKey in { actors keyForQ actor }
      } map {
        x => x.key
      }
  }
}
