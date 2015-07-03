package com.arcusys.valamis.lrs.api

import java.net.URI
import java.util.UUID

import com.arcusys.valamis.lrs.serializer.{GroupSerializer, AgentSerializer, StatementSerializer}
import com.arcusys.valamis.lrs.tincan.{Actor, Constants, Statement, StatementResult}
import com.arcusys.valamis.utils.serialization.JsonHelper

import org.apache.http.HttpStatus
import org.apache.http.client.entity.EntityBuilder
import org.apache.http.client.methods.{HttpGet, HttpPost, HttpPut}

import org.joda.time.DateTime

import scala.util.{Failure, Success, Try}

final class StatementApi(implicit lrs: LrsSettings) extends BaseApi() {
  import Constants.Tincan.Field._
  import Constants.Tincan._
  import Constants._

  override def addressPathSuffix = "statements"

  def post(statementJsonString: String): Try[Int] = {
    val uri = uriBuilder
      .clearParameters()
      .setPath(s"/$path/$addressPathSuffix")
      .build()

    val httpPost = new HttpPost(uri)
    initRequestAsJson(httpPost)

    val entity = EntityBuilder.create()
      .setText(statementJsonString)
      .build()

    httpPost.setEntity(entity)

    val response = httpClient.execute(httpPost)
    try {
      if ((response.getStatusLine.getStatusCode == HttpStatus.SC_OK) ||
        (response.getStatusLine.getStatusCode == HttpStatus.SC_NO_CONTENT)) {
        Success(response.getStatusLine.getStatusCode)
      } else {
        Failure(new FailureRequestException(response.getStatusLine.getStatusCode))
      }
    } finally {
      response.close()
    }
  }

  def put(statementId: UUID, statementJsonString: String): Try[Int] = {
    val uri = uriBuilder
      .clearParameters()
      .setPath(s"/$path/$addressPathSuffix")
      .addParameter("statementId", statementId.toString)
      .build()

    val httpPut = new HttpPut(uri)
    initRequestAsJson(httpPut)

    val entity = EntityBuilder.create()
      .setText(statementJsonString)
      .build()

    httpPut.setEntity(entity)

    val response = httpClient.execute(httpPut)
    try {
      if (response.getStatusLine.getStatusCode == HttpStatus.SC_NO_CONTENT) {
        Success(HttpStatus.SC_NO_CONTENT)
      } else {
        Failure(new FailureRequestException(response.getStatusLine.getStatusCode))
      }
    } finally {
      response.close()
    }
  }

  def addStatement(statement: Statement): Try[Int] = {
    val statementJsonString = JsonHelper.toJson[Statement](statement, new StatementSerializer)

    if (statement.id.isEmpty)
      post(statementJsonString)
    else
      put(statement.id.get, statementJsonString)
  }

  def addStatements(statements: Seq[Statement]): Try[Int] = {
    val statementJsonString = JsonHelper.toJson[Seq[Statement]](statements, new StatementSerializer)
    post(statementJsonString)
  }

  def getById(statementId: UUID): Try[String] = {
    val uri = uriBuilder
      .clearParameters()
      .setPath(s"/$path/$addressPathSuffix")
      .addParameter("statementId", statementId.toString)
      .build()

    val httpGet = new HttpGet(uri)
    initRequestAsJson(httpGet)

    val response = httpClient.execute(httpGet)
    getContent(response)
  }

  def getStatementById(statementId: UUID): Try[Statement] = {
    getById(statementId).map(JsonHelper.fromJson[Statement](_, new StatementSerializer))
  }

  def getByVoidedStatementId(statementId: UUID): Try[Statement] = {
    val uri = uriBuilder
      .clearParameters()
      .setPath(s"/$path/$addressPathSuffix")
      .addParameter("voidedStatementId", statementId.toString)
      .build()

    val httpGet = new HttpGet(uri)
    initRequestAsJson(httpGet)

    val response = httpClient.execute(httpGet)
    getContent(response).map(JsonHelper.fromJson[Statement](_, new StatementSerializer))
  }

  def getByParams(agent:        Option[Actor]    = None,
                  verb:         Option[URI]      = None,
                  activity:     Option[URI]      = None,
                  registration: Option[UUID]     = None,
                  since:        Option[DateTime] = None,
                  until:        Option[DateTime] = None,
                  relatedActivities: Boolean     = false,
                  relatedAgents: Boolean         = false,
                  limit:        Option[Int]      = None,
                  format:       Option[String]   = None,
                  attachments:  Boolean          = false,
                  ascending:    Boolean          = false,
                  offset:       Option[Int]      = None): Try[StatementResult] = {

    val builder = uriBuilder
      .clearParameters()
      .setPath(s"/$path/$addressPathSuffix")

    val agentJson = JsonHelper.toJson(agent, new AgentSerializer, new GroupSerializer)

    val uri = builder
      .addParameter(Agent,             agentJson                 )
      .addParameter(Ascending,         ascending         toString)
      .addParameter(Attachments,       attachments       toString)
      .addParameter(RelatedAgents,     relatedAgents     toString)
      .addParameter(RelatedActivities, relatedActivities toString)

      .addOptionParameter(Verb,         verb)
      .addOptionParameter(Since,        since)
      .addOptionParameter(Until,        until)
      .addOptionParameter(Limit,        limit)
      .addOptionParameter(Offset,       offset)
      .addOptionParameter(Format,       format)
      .addOptionParameter(Activity,     activity)
      .addOptionParameter(Registration, registration)
      .build()

    val httpGet = new HttpGet(uri)
    initRequestAsJson(httpGet)

    val response = httpClient.execute(httpGet)
    getContent(response) map { json =>
      JsonHelper.fromJson[StatementResult](json, new StatementSerializer)
    }
  }


}
