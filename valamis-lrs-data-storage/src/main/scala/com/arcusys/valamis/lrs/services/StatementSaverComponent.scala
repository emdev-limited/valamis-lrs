package com.arcusys.valamis.lrs.services

import java.util.UUID
import com.arcusys.valamis.lrs._
import com.arcusys.valamis.lrs.converter._
import com.arcusys.valamis.lrs.datasource.DataContext
import com.arcusys.valamis.lrs.datasource.row._
import com.arcusys.valamis.lrs.exception.ConflictEntityException
import com.arcusys.valamis.lrs.tincan._
import org.joda.time.DateTime

/**
 * Created by Iliya Tryapitsin on 28/01/15.
 */
private[lrs] trait StatementSaverComponent  {
  this: DataContext =>

  import driver.simple._

  /**
   * Save if not exist and return key of Statement object
   * @param obj General statement object
   * @return Statement object key
   */
  protected def getStatementObjectKey(obj:              StatementObject,
                                      groupRowKey:      Option[StatementObjectRow#Type] = None)
                                     (implicit session: Session):  StatementObjectRow#Type = obj match {
    case obj: Agent =>
      val actorRow = getActorKey(obj)

      if (actorRow.isEmpty) {

        val statementRowKey = statementObjects
          .returning(statementObjects.map(x => x.key))
          .insert(StatementObjectRow(objectType = StatementObjectType.agent))

        val accountRowKey = insertAccount(obj.account)
        actors.insert(AgentConverter.asRow(obj, statementRowKey, accountRowKey, groupRowKey))

        statementRowKey
      } else actorRow.get

    case obj: Group =>
      val groupRow = getActorKey(obj)

      if (groupRow.isEmpty) {

        val statementRowKey = statementObjects
          .returning(statementObjects.map(x => x.key))
          .insert(StatementObjectRow(objectType = StatementObjectType.group))

        val accountRowKey = insertAccount(obj.account)
        actors.insert(GroupConverter.asRow(obj, statementRowKey, accountRowKey))

        if (obj.member.isDefined)
          obj.member.get.foreach(x => getStatementObjectKey(x, Some(statementRowKey)))

        statementRowKey
      } else groupRow.get

    case obj: Activity =>
      val activityRowKey = getActivityKey(obj)

      if (activityRowKey.isEmpty) {
        val statementObjectRowKey = statementObjects
          .returning(statementObjects.map(x => x.key))
          .insert(StatementObjectRow(objectType = StatementObjectType.activity))

        activities += ActivityConverter.asRow(obj, statementObjectRowKey)
        statementObjectRowKey
      } else activityRowKey.get

    case obj: SubStatement =>
      val statementObjKey = statementObjects
        .returning(statementObjects.map(x => x.key))
        .insert(StatementObjectRow(objectType = StatementObjectType.subStatement))

      val actorRowKey = getStatementObjectKey(obj.actor)

      val objKey = getStatementObjectKey(obj.obj)

      subStatements += SubStatementRow(statementObjKey, objKey, actorRowKey, obj.verb.id, obj.verb.display)
      statementObjKey

    case obj: StatementReference =>
      statementRefKey(obj) getOrElse {
        val statementObjectRowKey = statementObjects
          .returning(statementObjects.map(x => x.key))
          .insert(StatementObjectRow(objectType = StatementObjectType.statementReference))

        statementReferences += StatementReferenceRow(statementObjectRowKey, obj.id.toString)
        statementObjectRowKey
      }
  }

  private def getStatementObjectKey(obj: Option[StatementObject])
                                   (implicit session: Session): StatementObjectRow#KeyType = obj match {
    case Some(value) => Some(getStatementObjectKey(value))
    case None => None
  }

  private def insertAccount(account: Option[Account])
                           (implicit session: Session): Option[AccountRow#Type] = account match {
    case Some(value) =>
      val accountRowKey = accountKeyQ(value)

      if (accountRowKey.isEmpty) {
        val row = AccountConverter.asRow(value)
        accounts
          .returning(accounts.map(x => x.key))
          .insert(row)
          .toOption
      } else accountRowKey

    case None => None
  }

  private def getResultRowKey(entity: Option[Result])
                             (implicit session: Session) = entity match {
    case Some(result) =>
      val scoreRowKey = insertScoreAndReturnKey(result.score)
      val scoreRow = ResultConverter.asRow(result, scoreRowKey)
      results
        .returning(results.map(x => x.key))
        .insert(scoreRow)
        .toOption
    case None => None
  }

  private def insertScoreAndReturnKey(score: Option[Score])
                                     (implicit session: Session) = score match {
    case Some(s) => scores
      .returning(scores.map(x => x.key))
      .insert(ScoreConverter.asRow(s))
      .toOption
    case None => None
  }

  private def insertContextActivities(contextRowKey:     ContextRow#Type,
                                      cntxActivities: Option[ContextActivities])
                                     (implicit session:  Session) = cntxActivities match {
    case Some(value) => {
      val ids = value.category.map(_.id) ++
        value.grouping.map(_.id) ++
        value.other.map(_.id) ++
        value.parent.map(_.id)

      val existActivities = if (ids.isEmpty) Seq()
        else activities
        .filter(x => x.id inSet ids)
        .map(x => x.id)
        .list

      ids.foreach(x => if (!existActivities.contains(x))
        getStatementObjectKey(Activity(id = x))
      else Unit)

      val categories = if (value.category.isEmpty) Seq()
        else activities
        .filter(x => x.id inSet value.category.map(y => y.id))
        .map(x => (x.key, x.id))
        .list
        .map(x => ContextActivityRow(contextRowKey, x._1, ContextActivityType.category))

      val grouping = if (value.grouping.isEmpty) Seq()
        else activities
        .filter(x => x.id inSet value.grouping.map(y => y.id))
        .map(x => x.key)
        .list
        .map(x => ContextActivityRow(contextRowKey, x, ContextActivityType.grouping))

      val other = if (value.other.isEmpty) Seq()
        else activities
        .filter(x => x.id inSet value.other.map(y => y.id))
        .map(x => x.key)
        .list
        .map(x => ContextActivityRow(contextRowKey, x, ContextActivityType.other))

      val parent = if (value.parent.isEmpty) Seq()
        else activities
        .filter(x => x.id inSet value.parent.map(y => y.id))
        .map(x => x.key)
        .list
        .map(x => ContextActivityRow(contextRowKey, x, ContextActivityType.parent))

      contextActivities.insertAll(other ++ categories ++ grouping ++ parent: _*)
    }
    case None => Unit
  }

  private def getContextRowKey(entity: Option[Context])
                              (implicit session: Session) = entity match {
    case Some(value) => {
      val instructorRowKey = getStatementObjectKey(value.instructor)
      val teamRowKey = getStatementObjectKey(value.team)
      val statementRefRowKey = getStatementObjectKey(value.statement)
      val contextRow = ContextConverter.asRow(value, instructorRowKey, teamRowKey, statementRefRowKey)

      contexts.insert(contextRow)

      insertContextActivities(contextRow.key.get, value.contextActivities)

      Some(contextRow.key.get)
    }

    case None => None
  }

  /**
   * Save new statement in the LRS
   * @param entity Statement entity
   * @return Saved statement id
   */
  def addStatement(entity: Statement): UUID = db.withSession { implicit session =>

    entity checkDuplicate

    val actorKey        = getStatementObjectKey(entity.actor)
    val authorityKey    = getStatementObjectKey(entity.authority)
    val stObjKey        = getStatementObjectKey(entity.obj)
    val resultRowKey    = getResultRowKey(entity.result)
    val contextRowKey   = getContextRowKey(entity.context)
    val currentDateTime = DateTime.now

    val statementRow = StatementRow(
      entity.id.map(x => x.toString).getOrElse(UUID.randomUUID.toString),
      actorKey,
      entity.verb.id,
      entity.verb.display,
      stObjKey,
      resultRowKey,
      contextRowKey,
      entity.timestamp.getOrElse(currentDateTime),
      currentDateTime,
      authorityKey,
      entity.version)

    statements += statementRow

    val atts = entity.attachments.map(a => AttachmentConverter.asRow(a, statementRow.key))
    if(atts.length > 0)
      attachments.insertAll(atts: _*)

    UUID.fromString(statementRow.key)
  }
}
