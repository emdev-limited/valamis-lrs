package com.arcusys.valamis.lrs.history.ver230.from

import java.util.UUID

import com.arcusys.valamis.lrs.history.ver230.from.row.StatementRefRow
import com.arcusys.valamis.lrs.tincan._

import scala.slick.jdbc._
import com.arcusys.valamis.lrs.history.BaseComponent
import com.arcusys.valamis.lrs.history.Helper._

/**
 * Created by Iliya Tryapitsin on 25/03/15.
 */
trait StatementRefComponent {
  this: BaseComponent
    with ActivityComponent
    with SubStatementComponent
    with ActorComponent =>

  private implicit val getResult = GetResult(r => {
    StatementRefRow(
      r.nextLong(), //`id_` bigint(20) NOT NULL,
      r.nextStringOption() //`uuid_` varchar(75) DEFAULT NULL,
    )
  })

  private def statementRefs(implicit session: JdbcBackend#Session) = loadStatementRefs

  def loadStatementRefs(implicit session: JdbcBackend#Session) = StaticQuery
    .queryNA[StatementRefRow](s"select * from ${getTableName("Learn_LFTCLrsStmntRef")}")
    .list

  protected def getStatementRef(id: Option[String])(implicit session: JdbcBackend#Session): Option[StatementReference] = if (id.isEmpty) None
  else Some(StatementReference(getUUID(id.get)))

  protected def getStatementObject(key: Long,
                                   objType: String)(implicit session: JdbcBackend#Session): StatementObject = objType match {
    case Constants.Tincan.Activity =>  {
      val activity = activities.find(x => x.key == key)

      require(activity.isDefined, s"Cann't find Activity with key = $key")

      activity.get.toModel
    }
    case Constants.Tincan.StatementReference => {
      val ref = statementRefs.filter(x => x.id == key).head

      require(ref.uuid.isDefined, s"uuid should be defined in StatementRef with key = ${ref.id}")

      StatementReference(UUID.fromString(ref.uuid.get))
    }
    case Constants.Tincan.SubStatement => {
      val subStatement = subStatements.filter(x => x.key == key).head

      require(subStatement.actorId.isDefined, s"Actor Id in SubStatement should be defined: SubStatement Id = ${subStatement.key}")
      require(subStatement.objId.isDefined,   s"ObjId in SubStatement should be defined: SubStatement Id = ${subStatement.key}")
      require(subStatement.objType.isDefined, s"ObjType in SubStatement should be defined: SubStatement Id = ${subStatement.key}")

      val verb = getVerb(subStatement.verbId, subStatement.verbDisplay)
      val actor = getActor(subStatement.actorId.get)
      val obj = getStatementObject(subStatement.objId.get, subStatement.objType.get)

      SubStatement(actor, verb, obj)
    }
    case Constants.Tincan.Actor |
         Constants.Tincan.Agent |
         Constants.Tincan.Group => getActor(key)
  }
}