package com.arcusys.valamis.lrs.jdbc.history.ver230.from

  import com.arcusys.valamis.lrs.jdbc.history.ver230.from.row.ResultRow
import com.arcusys.valamis.lrs.tincan.Result

import scala.slick.jdbc._
import com.arcusys.valamis.lrs.jdbc.history.BaseComponent
import com.arcusys.valamis.lrs.jdbc.history.Helper._

/**
 * Created by Iliya Tryapitsin on 25/03/15.
 */
trait ResultComponent {
  this: BaseComponent =>

  private implicit val getResult = GetResult(r => {
    ResultRow(
      r.nextLong(), //`id_` bigint(20) NOT NULL,
      r.nextStringOption(), //`score` longtext,
      r.nextStringOption(), //`success` tinyint(4) DEFAULT NULL,
      r.nextStringOption(), //`completion` tinyint(4) DEFAULT NULL,
      r.nextStringOption(), //`response` longtext,
      r.nextStringOption(), //`duration` varchar(512) DEFAULT NULL,
      r.nextStringOption() //`extension` longtext,
    )
  })

  private def results(implicit session: JdbcBackend#Session) = loadResults

  def loadResults(implicit session: JdbcBackend#Session) = StaticQuery
    .queryNA[ResultRow](s"select * from ${getTableName("Learn_LFTincanLrsResult")}")
    .list

  protected def getResult(key: Option[Long])(implicit session: JdbcBackend#Session): Option[Result] = key match {
    case Some(v) => {
      results.find(x => x.key == v) match {
        case None => None
        case Some(result) => Some(
          Result(
            score = getScore(result.score),
            success = result.success.map { string2bool },
            completion = result.completion.map { string2bool },
            response = result.response,
            duration = result.duration,
            extensions = getExtensions(result.extension)
          )
        )
      }
    }
    case None => None
  }
}
