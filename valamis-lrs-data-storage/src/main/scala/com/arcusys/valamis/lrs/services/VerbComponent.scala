package com.arcusys.valamis.lrs.services

import com.arcusys.valamis.lrs.SeqWithCount
import com.arcusys.valamis.lrs.converter.ActivityConverter
import com.arcusys.valamis.lrs.datasource.DataContext
import com.arcusys.valamis.lrs.datasource.row.ActivityRow
import com.arcusys.valamis.lrs.tincan._
import org.joda.time.DateTime
/**
 * Created by Iliya Tryapitsin on 15.06.15.
 */
trait VerbComponent {
  this: DataContext with StatementFinderComponent =>

  import driver.simple._

  def verbAmount(start: Option[DateTime]): Int =
    db.withSession { implicit session =>
      statements.since(start).length.run
    }

  def verbAmountByGroup(start: Option[DateTime]): Seq[(String, Int)] =
    db.withSession { implicit session =>
      statements since start groupBy { s =>
        s.verbId
      } map { case (id, s) => (id, s.countDistinct) } list
    }

  def verbIdsWithDate(start: Option[DateTime]): Seq[(String, DateTime)] =
    db.withSession { implicit session =>
      statements since start map { s => (s.verbId, s.timestamp) } list
    }


  def verbWithActivities(filter:  Option[String] = None,
                         limit:   Int            = 10,
                         offset:  Int            = 0,
                         sortDir: Boolean        = true): SeqWithCount[(Verb, (String, LanguageMap))] =
    db withSession { implicit session =>
      val query = statements
        .join(activities)
        .on      { (s, o) => s.objectKey === o.key }
        .filter  { case ((s, o)) => (s.verbDisplay like filter) || (o.name like filter) }
        .map     { case ((s, o)) => (s.verbId, s.verbDisplay, o.id, o.name) }
        .groupBy { case s => s }
        .map     { case (k, v) => k }

      val orderedQuery = if (sortDir)
        query.sortBy { case ((verbId, _, _, _)) => verbId }
      else query

      val count = query.length.run

      val result = orderedQuery
        .drop(offset)
        .take(limit)
        .run map { rec => (Verb(rec._1, rec._2), (rec._3, rec._4)) }

      SeqWithCount(result, count)
    }
}

