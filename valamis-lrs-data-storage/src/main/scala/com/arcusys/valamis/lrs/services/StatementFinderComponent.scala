package com.arcusys.valamis.lrs.services

import java.net.URI
import java.util.UUID

import com.arcusys.valamis.lrs.converter.{ActivityConverter, AttachmentConverter, ScoreConverter}
import com.arcusys.valamis.lrs.datasource.DataContext
import com.arcusys.valamis.lrs.datasource.row._
import com.arcusys.valamis.lrs.tincan._
import org.joda.time.DateTime

import scala.slick.lifted

/**
 * Created by Iliya Tryapitsin on 28/01/15.
 */
private[lrs] trait StatementFinderComponent {
  this: DataContext =>

  import jodaSupport._
  import driver.simple._

  def findStatements(query: StatementQuery): PartialSeq[Statement] = db.withSession { implicit session =>

    query match {
      case q@StatementQuery(Some(statementId), None, None, None, None, None, None, None, _, _, _, _, _, _, _) =>
        val statements = findStatementsByIdExcludeVoided(statementId)
        PartialSeq(statements, true)

      case q@StatementQuery(None, Some(voidedStatementId), None, None, None, None, None, None, _, _, _, _, _, _, _) =>
        val statements = findStatementsByVoidedId(voidedStatementId)
        PartialSeq(statements, true)

      case q@StatementQuery(None, None, actor, verb, activity, registration, since, until, relatedActivities, relatedAgents, limit, offset, format, attachments, ascending) =>

        val filteredQuery = statements
          .excludeVoided()
          .filterByRegistration(registration)
          .filterByActor(actor, relatedAgents)
          .flatMap(q => q.filterByActivity(activity, relatedActivities))

        filteredQuery match {
          case None => PartialSeq(Seq(), true)
          case Some(q) =>
            val query = q
              .since(since)
              .until(until)
              .filterByVerb(verb)
              .filterByAttachments(attachments)
              .sortByStoreTime(ascending)

            val count = query.length.run

            val statements = query
              .limit(limit + offset)
              .drop(offset)
              .map(x => x.key)
              .list
              .flatMap(x => findStatementsById(x))// Issue: findStatementsById have long time execution

            PartialSeq(statements, (limit + offset) >= count)
        }

      case _ => throw new IllegalArgumentException
    }
  }

  private def getStatementRow(key: String)
                             (implicit session: Session) = statements
    .filter { x => x.key === key }
    .first

  private def getStatementActorRow(key: ActorRow#Type)
                                  (implicit session: Session) = actors
    .filter { x => x.key === key }
    .first

  private def getStatementActorRow(key: Option[ActorRow#Type])
                                  (implicit session: Session): Option[ActorRow] = key match {
    case Some(value) => Some(getStatementActorRow(value))
    case None        => None
  }

  private def getStatementObjectRow(row: StatementRow)
                                   (implicit session: Session): StatementObjectRow =
    getStatementObjectRow(row.objectKey)

  private def getStatementObjectRow(key: StatementObjectRow#Type)
                                   (implicit session: Session): StatementObjectRow = statementObjects
    .filter { x => x.key === key }
    .first

  private def getStatementObjectRow(key: Option[StatementObjectRow#Type])
                                   (implicit session: Session): Option[StatementObjectRow] = key match {
    case Some(key) => statementObjects.filter(x => x.key === key).firstOption
    case None => None
  }

  private def getResultRow(row: StatementRow)
                          (implicit session: Session) = results.filter(x => x.key === row.resultKey).firstOption

  private def getContextRow(row: StatementRow)(implicit session: Session) = contexts.filter(x => x.key === row.contextKey).firstOption

  private def getAttachmentRows(row: StatementRow)(implicit session: Session) = attachments.filter(x => x.statementKey === row.key).list

  private def findStatementsById(statementId: String)(implicit session: Session): Seq[Statement] = {
    val stmtRow = getStatementRow(statementId)
    val stmtObjectRow = getStatementObjectRow(stmtRow)
    val stmtActorRow = getStatementActorRow(stmtRow.actorKey)
    val stmtResultRow = getResultRow(stmtRow)
    val contextRow = getContextRow(stmtRow)
    val authorityRow = getStatementActorRow(stmtRow.authorityKey)
    val attachmentRows = getAttachmentRows(stmtRow)
    val statementObject = statementObjectToModel(stmtObjectRow)
    val statementActor = actorRowToModel(stmtActorRow)
    val result = resultRowToResult(stmtResultRow)
    val context = contextRowToContext(contextRow)
    val authority = actorRowToModel(authorityRow)
    val attachments = attachmentRowsToModel(attachmentRows)

    Seq(
      Statement(
        Some(UUID.fromString(stmtRow.key)),
        statementActor,
        Verb(stmtRow.verbId, stmtRow.verbDisplay),
        statementObject,
        result,
        context,
        Some(stmtRow.timestamp),
        Some(stmtRow.stored),
        authority,
        stmtRow.version,
        attachments))
  }

  private def attachmentRowsToModel(rows: Seq[AttachmentRow])
                                   (implicit session: Session): Seq[Attachment] =
    rows.map(row => AttachmentConverter.asModel(row))

  private def contextRowToContext(row: Option[ContextRow])
                                 (implicit session: Session): Option[Context] = row match {
    case Some(value) => {
      val actorRow = getStatementActorRow(value.instructor)
      val groupRow = getStatementActorRow(value.team)
      val contextActivityRows = contextActivities.filter(x => x.contextKey === value.key).list
      val statementRefRow = getStatementObjectRow(value.statement)
      val actor = actorRowToModel(actorRow)
      val group = actorRowToModel(groupRow).asInstanceOf[Option[Group]]
      val cntxActivities = contextActivityRowsToModel(contextActivityRows)
      val statementRef = statementObjectToModel(statementRefRow).asInstanceOf[Option[StatementReference]]

      Some(Context(
        value.registration.map { x => UUID.fromString(x) },
        actor, group,
        cntxActivities,
        value.revision,
        value.platform,
        value.language,
        statementRef,
        value.extensions))
    }
    case None => None
  }

  private def contextActivityRowsToModel(rows: Seq[ContextActivityRow])
                                        (implicit session: Session): Option[ContextActivities] =
    if (rows.isEmpty)
      None
    else {

      val actvts = if (rows.isEmpty) Seq()
        else activities.filter(x => x.key inSet rows.map(x => x.activityKey)).list

      val grouping = rows
        .filter { x => x.contextActivityType == ContextActivityType.grouping }
        .map    { x =>
          ActivityReference(
            actvts.filter(a => a.key == x.activityKey).head.id,
            Some(StatementObjectType.activity)
          )
        } toSet

      val category = rows
        .filter { x => x.contextActivityType == ContextActivityType.category }
        .map    { x =>
          ActivityReference(
            actvts.filter(a => a.key == x.activityKey).head.id,
            Some(StatementObjectType.activity)
          )
        } toSet

      val parent = rows
        .filter { x => x.contextActivityType == ContextActivityType.parent }
        .map    { x =>
          ActivityReference(
            actvts.filter(a => a.key == x.activityKey).head.id,
            Some(StatementObjectType.activity)
          )
        } toSet

      val other = rows
        .filter { x => x.contextActivityType == ContextActivityType.other }
        .map    { x =>
          ActivityReference(
            actvts.filter(a => a.key == x.activityKey).head.id,
            Some(StatementObjectType.activity)
          )
        } toSet

      Some(
        ContextActivities(
          grouping = grouping,
          category = category,
          parent   = parent,
          other    = other
        )
      )
    }

  private def resultRowToResult(row: Option[ResultRow])(implicit session: Session): Option[Result] = row match {
    case Some(value) => {
      val scoreRow = scores.filter(x => x.key === value.scoreId).firstOption
      val score = if (scoreRow.isDefined)
        Some(ScoreConverter.asModel(scoreRow.get))
      else
        None

      Some(Result(score, value.success, value.completion, value.response, value.duration, value.extensions))
    }
    case None => None
  }

  private def statementObjectToModel(obj: Option[StatementObjectRow])
                                    (implicit session: Session): Option[StatementObject] =
    obj match {
      case None => None
      case Some(value) => Some(statementObjectToModel(value))
    }

  private def statementObjectToModel(obj: StatementObjectRow)
                                    (implicit session: Session): StatementObject =
    obj.objectType match {

      case StatementObjectType.activity             =>
        ActivityConverter.asModel(activities.filter(x => x.key === obj.key).first)

      case StatementObjectType.agent                =>
        actorRowToModel(actors.filter(x => x.key === obj.key).first)

      case StatementObjectType.group                =>
        actorRowToModel(actors.filter(x => x.key === obj.key).first)

      case StatementObjectType.person               =>
        throw new NotImplementedError("Person finder")

      case StatementObjectType.subStatement         =>
        val subStatementRow = subStatements.filter(x => x.key === obj.key).first
        subStatementRowToSubStatement(subStatementRow)

      case StatementObjectType.`statementReference` =>
        statementReferences.filter(x => x.key === obj.key).first.toModel
    }

  private def subStatementRowToSubStatement(row: SubStatementRow)
                                           (implicit session: Session): SubStatement = {
    val actorRow = getStatementActorRow(row.actorKey)
    val stmtObjectRow = getStatementObjectRow(row.objectKey)

    val actor = actorRowToModel(actorRow)
    val stmtObject = statementObjectToModel(stmtObjectRow)

    SubStatement(actor, Verb(row.verbId, row.verbDisplay), stmtObject)
  }

  private def actorRowToModel(actorRow: Option[ActorRow])
                             (implicit session: Session): Option[Actor] = actorRow match {
    case Some(value) => Some(actorRowToModel(value))
    case None => None
  }

  private def actorRowToModel(actorRow: ActorRow)
                             (implicit session: Session): Actor = {
    val accountRow = accounts
      .filter(x => x.key === actorRow.accountKey)
      .firstOption

    val account = if (accountRow.isDefined) Some(Account(accountRow.get.homepage, accountRow.get.name))
    else None

    actorRow match {
      case a: GroupRow =>
        val memberRows = actors.filter(x => x.groupKey === actorRow.key).list
        val member = if (memberRows.size == 0)
          None
        else
          Some(memberRows.map(x => actorRowToModel(x)))

        Group(a.name, member, a.mBox, a.mBoxSha1Sum, a.openId, account)

      case a: AgentRow => Agent(a.name, a.mBox, a.mBoxSha1Sum, a.openId, account)
    }
  }

  private def findStatementsByVoidedId(voidedStatementId: UUID)
                                      (implicit session: Session): Seq[Statement] =
    findStatementsById(voidedStatementId.toString)

  private def findStatementsByIdExcludeVoided(statementId: UUID)
                                             (implicit session: Session): Seq[Statement] =
    if (statementId.existVoidedQ.run) Seq()
    else findStatementsById(statementId.toString)

  type StatementRowQuery = lifted.Query[StatementsTable, StatementsTable#TableElementType, Seq]

  implicit class StatementTableExtensions(val query: StatementRowQuery) {

    def since(since: Option[DateTime])(implicit session: Session): StatementRowQuery = since match {
      case Some(value) => query.filter(x => x.timestamp >= value)
      case None => query
    }

    def until(until: Option[DateTime])(implicit session: Session): StatementRowQuery = until match {
      case Some(value) => query.filter(x => x.timestamp <= value)
      case None => query
    }

    def limit(limit: Int)(implicit session: Session): StatementRowQuery = if(limit != 0)
        query.take(limit)
      else query

    def filterByActivity(activity: Option[URI], relatedActivities: Boolean)
                        (implicit session: Session): Option[StatementRowQuery] = {
      activity match {
        case None => Some(query)
        case Some(activityUri) =>
          getActivityKey(Activity(id = activityUri.toString)) match {
            case None => None

            case Some(activityKey) if !relatedActivities =>
              Some(query.filter(x => x.objectKey === activityKey))

            case Some(activityKey) if relatedActivities =>
              val subStatementWithActivity = subStatements
                .filter(x => x.statementObjectKey === activityKey)
                .map(x => x.key)

              val contextWithActivity = contextActivities
                .filter(x => x.activityKey === activityKey)
                .map(x => x.contextKey)
              Some(query.filter(x => (x.objectKey === activityKey)
                || (x.objectKey in subStatementWithActivity)
                || (x.contextKey in contextWithActivity)))
          }
      }
    }

    def filterByActor(actor: Option[Actor], relatedAgents: Boolean)
                     (implicit session: Session): Option[StatementRowQuery] = {
      actor match {
        case None => Some(query)
        case Some(actorValue) =>
          getActorKey(actorValue) match {
            case None => None
            case Some(actorKey) if !relatedAgents =>
              Some(query.filter(x => (x.actorKey === actorKey) || (x.objectKey === actorKey)))
            case Some(actorKey) if relatedAgents =>
              val subStatementWithActivity = subStatements
                .filter(x => (x.actorKey === actorKey) || (x.statementObjectKey === actorKey))
                .map(x => x.key)
              val contextWithActivity = contexts
                .filter(x => (x.instructor === actorKey) || (x.team === actorKey))
                .map(x => x.key)

              Some(query.filter(x => (x.actorKey === actorKey) || (x.objectKey === actorKey)
                || (x.authorityKey === actorKey)
                || (x.objectKey in subStatementWithActivity)
                || (x.contextKey in contextWithActivity)))
          }
      }
    }

    def filterByVerb(verb: Option[URI])(implicit session: Session): StatementRowQuery = verb match {
      case Some(value) => query.filter(x => x.verbId === value.toString)
      case None => query
    }

    def filterByRegistration(registration: Option[UUID])
                            (implicit session: Session): StatementRowQuery = registration match {
      case Some(value) => query.filter { x => x.contextKey === value.toString }
      case None        => query
    }

    def filterByAttachments(useAttachments: Boolean)
                           (implicit session: Session): StatementRowQuery = if (useAttachments)
      query
        .innerJoin(attachments).on((x1, x2) => x1.key === x2.statementKey)
        .map(x => x._1)
    else query

    def excludeVoided()(implicit session: Session): StatementRowQuery = {
      val voidedStatementKeys = statementReferences
        .join(statements).on((ref, st) => st.objectKey === ref.key)
        .filter(j => j._2.verbId like Constants.Tincan.VoidedVerb)
        .map(x => x._1.statementId)
      query.filterNot(x => x.key in voidedStatementKeys)
    }

    def sortByStoreTime(isSort: Boolean): StatementRowQuery = if (isSort)
      query.sortBy(_.stored.asc)
    else query.sortBy(_.stored.desc)
  }
}
