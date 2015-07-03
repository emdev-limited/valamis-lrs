package com.arcusys.valamis.lrs.datasource

import java.util.UUID

import com.arcusys.valamis.lrs.converter.AttachmentConverter
import com.arcusys.valamis.lrs.datasource.row.StatementRow
import com.arcusys.valamis.lrs.exception.ConflictEntityException
import com.arcusys.valamis.lrs.tincan._

trait DataQuery {
  this: DataContext =>

  import driver.simple._

  def getActorKey(obj: Actor,
                  query: Query[ActorsTable,ActorsTable#TableElementType, Seq] = actors)
                 (implicit session: Session) =
    obj.account match {

      case Some(value) => query
        .leftJoin(accounts).on((actor, account) => actor.accountKey === account.key)
        .filter(x =>  x._1.mBox === obj.mBox || x._1.mBoxSha1Sum === obj.mBoxSha1Sum || x._1.openId === obj.openId ||
          x._2.name === value.name && x._2.homepage === value.homePage)
        .map(x => x._1.key)
        .firstOption

      case None => query
        .filter(x => x.mBox === obj.mBox || x.mBoxSha1Sum === obj.mBoxSha1Sum || x.openId === obj.openId)
        .map(x => x.key)
        .firstOption
    }


  def getAgentKey(obj: Agent)(implicit session: Session) = getActorKey(obj, getAgents)

  def getActivityKey(obj: Activity)
                    (implicit session: Session) = activities
    .filter(x => x.id === obj.id)
    .map(x => x.key)
    .firstOption

  def statementRefKey(ref: StatementReference)
                     (implicit session: Session) =
  {
    val id = ref.id toString

    statementReferences filter {
      x => x.statementId === id
    } map {
      x => x.key
    } firstOption
  }

  def accountKeyQ(obj: Account)
                 (implicit session: Session) =
    accounts filter { x =>
      x.homepage === obj.homePage || x.name === obj.name
    } map {
      x => x.key
    } firstOption

  implicit class StatementRowKeyExtensions(key: UUID) {

    private val k = key toString

    def existVoidedQ(implicit session: Session) =
      statementReferences filter { x =>
        x.statementId === k
      } exists

    def existStatementQ(implicit session: Session) =
      statements filter { x =>
        x.key === k
      } exists
  }


  implicit class StatementExtension(statement: Statement) {

    def checkDuplicate(implicit session: Session): Unit = {
      statement.id filter { id =>
        id.existStatementQ.run
      } map { x =>
        throw new ConflictEntityException(s"Statement with key = '${x}' already exist")
      }
    }
  }
}
