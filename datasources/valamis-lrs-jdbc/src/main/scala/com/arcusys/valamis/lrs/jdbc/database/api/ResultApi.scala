package com.arcusys.valamis.lrs.jdbc.database.api

import com.arcusys.valamis.lrs._
import com.arcusys.valamis.lrs.jdbc.database.converter._
import com.arcusys.valamis.lrs.jdbc.database.LrsDataContext
import com.arcusys.valamis.lrs.jdbc.database.api.query.ResultQueries
import com.arcusys.valamis.lrs.jdbc.database.row.ResultRow
import com.arcusys.valamis.lrs.tincan._

/**
 * Created by Iliya Tryapitsin on 08.07.15.
 */
trait ResultApi extends ResultQueries {
  this: LrsDataContext with ScoreApi =>

  import executionContext.driver.simple._

  implicit class ResultRowExtension (result: ResultRow) {

    /**
     * Load Tincan [[Score]] for current Tincan [[Result]] storage record
     * @param s
     * @return Tinca [[Score]] instance
     */
    def getScore(implicit s: Session) = result.scoreId map {
      key => findScoreByKey(key)
    }
  }


  /**
   * Building Tincan [[Result]]s by storage records
   * @param recs List of storage records
   * @param s
   * @return Tincan [[Result]]s with storage keys
   */
  protected def convertResults (recs: Seq[ResultRow])
                               (implicit s: Session): Map[ResultRow#KeyType, Result] = {
    val scores = findScoresByKeys (recs map { _.scoreId } takeDefined() )

    recs map { x =>
      val result = x.convert withScore {
        scores get x.scoreId

      } build

      (x.key, result)
    } toMap
  }

  /**
   * Building Tincan [[Result]] by storage record
   * @param rec Storage record
   * @param s
   * @return Tincan [[Result]] instance
   */
  protected def convertResult (rec: ResultRow)
                              (implicit s: Session) = rec.convert withScore rec.getScore build

  implicit class ResultInsertQueries (q: ResultQ) {

    /**
     * Insert Tincan Result to the storage
     * @param result Result instance
     * @param session
     * @return Identity key in the storage
     */
    def add (result: Result)
            (implicit session: Session): ResultRow#Type =
      q returning q.map { x =>
        x.key

      } += result.convert withScoreKey {
        scores add result.score
      }

    /**
     * Insert Tincan Result to the storage
     * @param result Result instance
     * @param session
     * @return Identity key in the storage
     */
    def add (result: Option[Result])
            (implicit session: Session): ResultRow#KeyType = result map { v => q add v }
  }

}
