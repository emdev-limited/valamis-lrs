package com.arcusys.valamis.lrs.jdbc.database.api

import com.arcusys.valamis.lrs.jdbc.database.LrsDataContext
import com.arcusys.valamis.lrs.jdbc.database.api.query.ScoreQueries
import com.arcusys.valamis.lrs.jdbc.database.row.ScoreRow
import com.arcusys.valamis.lrs.tincan.Score

/**
 * Created by Iliya Tryapitsin on 08.07.15.
 */
trait ScoreApi extends ScoreQueries {
  this: LrsDataContext =>

  import executionContext.driver.simple._

  /**
   * Load Tincan [[Score]] by storage key
   * @param key Storage key
   * @param s
   * @return Tinca [[Score]] instance
   */
  def findScoreByKey (key: ScoreRow#Type)
                     (implicit s: Session) = findScoreByKeyQC(key).first convert

  /**
   * Load Tincan [[Score]]s by storage keys
   * @param keys Storage keys
   * @param s
   * @return Tincan [[Score]] instances
   */
  def findScoresByKeys (keys: Seq[ScoreRow#Type])
                       (implicit s: Session): Map[ScoreRow#KeyType, Score] = {
    /*
    * If keys are empty, executes unapproved sql
    * Prevent this
    */
    if(keys.isEmpty) return Map()

    findScoresByKeysQ(keys).run map {
      x => (x.key, x convert)
    } toMap
  }

  implicit class ScoresInsertQueries (q: ScoreQ) {

    /**
     * Insert Tincan Score to the storage
     * @param score Score instance
     * @param session
     * @return Identity key in the storage
     */
    def add (score: Score)
            (implicit session: Session): ScoreRow#Type =
      q returning q.map { x =>
        x.key
      } += score.convert

    /**
     * Insert Tincan Score to the storage
     * @param score Score instance
     * @param session
     * @return Identity key in the storage
     */
    def add (score: Option[Score])
            (implicit session: Session): ScoreRow#KeyType =
      score map { v => q add v }
  }

}
