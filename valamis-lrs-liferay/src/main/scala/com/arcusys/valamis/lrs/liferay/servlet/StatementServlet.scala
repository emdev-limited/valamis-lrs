package com.arcusys.valamis.lrs.liferay.servlet

import java.util.UUID
import javax.servlet.annotation.MultipartConfig
import javax.servlet.http.{HttpServletRequest, HttpServletResponse}

import com.arcusys.valamis.lrs.utils._
import com.arcusys.valamis.lrs.StatementQuery
import com.arcusys.valamis.lrs.liferay.exception.NotFoundException
import com.arcusys.valamis.lrs.liferay.servlet.request.TincanStatementRequest
import com.arcusys.valamis.lrs.tincan.{Statement, StatementResult}
import com.google.inject.{Inject, Injector, Singleton}

@Singleton
@MultipartConfig
class StatementServlet @Inject()(inj: Injector) extends BaseLrsServlet(inj) {

  override def doGet(request: HttpServletRequest,
                     response: HttpServletResponse): Unit = jsonAction[TincanStatementRequest](model => {
    val query = StatementQuery(
      model.statementId,
      model.voidedStatementId,
      model.agent,
      model.verb,
      model.activity,
      model.registration,
      model.since,
      model.until,
      model.relatedActivities,
      model.relatedAgents,
      model.limit,
      model.offset,
      model.format,
      model.attachments,
      model.ascending)

    val statements = lrs.findStatements(query)

    // Statement requested by id not found
    if (model.isRequestingSingleStatement && statements.seq.isEmpty) {
      val stmntId = query.statementId ++ query.voidedStatementId head

      throw new NotFoundException(s"Statement with UUID=$stmntId not found")
    }

    if(model.isRequestingSingleStatement) statements.seq.headOption
    else {
      val more = getMore(statements.isEmpty, model)
      StatementResult(statements, more)
    }

  }, request, response)

  override def doPost(request: HttpServletRequest,
                      response: HttpServletResponse): Unit = jsonAction[TincanStatementRequest](model => {
    val result = model.statements map { x => lrs.addStatement(x.copy(id = UUID.randomUUID.toOption)) }
    result

  }, request, response)

  override def doPut(request: HttpServletRequest,
                     response: HttpServletResponse): Unit = jsonAction[TincanStatementRequest](model => {
    model.statements.map(x => model.statementId match {
      case Some(value) => lrs.addStatement(x.copy(id = Some(value)))
      case None => lrs.addStatement(x)
    })
    Unit
  }, request, response)

  private def getMore(isNotNeed: Boolean, model: TincanStatementRequest): String =
    if(isNotNeed) EmptyString
    else model.toMoreIRL

  override val ServletName: String = "Statements"

}
