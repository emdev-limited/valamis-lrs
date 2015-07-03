package com.arcusys.valamis.lrs.liferay.history.ver230.from

  import com.arcusys.valamis.lrs.liferay.history.ver230.from.row.ContextRow
import com.arcusys.valamis.lrs.tincan.{Context, Group}

import scala.slick.jdbc._
import com.arcusys.valamis.lrs.liferay.history.BaseComponent
import com.arcusys.valamis.lrs.liferay.history.Helper._

/**
 * Created by Iliya Tryapitsin on 25/03/15.
 */
trait ContextComponent {
  this: BaseComponent
    with ActorComponent
    with ContextActivityComponent
    with StatementRefComponent =>

  private implicit val getResult = GetResult(r => {
    ContextRow(
      r.nextLong(), //`id_` bigint(20) NOT NULL,
      r.nextStringOption(), //`registration` varchar(512) DEFAULT NULL,
      r.nextLongOption(), //`instructorID` int(11) DEFAULT NULL,
      r.nextLongOption(), //`teamID` int(11) DEFAULT NULL,
      r.nextLongOption(), //`contextActivitiesID` int(11) DEFAULT NULL,
      r.nextStringOption(), //`revision` longtext,
      r.nextStringOption(), //`platform` longtext,
      r.nextStringOption(), //`language` longtext,
      r.nextStringOption(), //`statement` longtext,
      r.nextStringOption() //`extensions` longtext,
    )
  })

  private def contexts(implicit session: JdbcBackend#Session) = loadContexts

  def loadContexts(implicit session: JdbcBackend#Session) = StaticQuery
    .queryNA[ContextRow](s"select * from ${getTableName("Learn_LFTincanLrsContext")}")
    .list

  protected def getContext(key: Option[Long])(implicit session: JdbcBackend#Session) = key match {
    case Some(v) => contexts.find(x => x.key == v) match {
      case None => None
      case Some(context) => Some(
        Context(
          registration = getUUIDOption(context.registration),
          instructor = getActor(context.instructorId),
          team = getActor(context.teamId).map(x => x.asInstanceOf[Group]),
          contextActivities = getContextActivities(context.contextActivitiesId),
          revision = context.revision,
          platform = context.platform,
          language = context.language,
          statement = getStatementRef(context.statement),
          extensions = getExtensions(context.extensions)
        )
      )
    }
    case None => None
  }
}
