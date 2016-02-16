package com.arcusys.valamis.lrs.api.valamis

import java.net.URI

import com.arcusys.valamis.lrs.api.{BaseApi, LrsSettings}
import com.arcusys.valamis.lrs.serializer._
import com.arcusys.valamis.lrs.tincan.Actor
import com.arcusys.valamis.lrs.tincan.Constants.Tincan._
import com.arcusys.valamis.lrs.tincan._
import org.apache.http.client.methods.HttpGet
import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat

import scala.util.Try

/**
 * Created by Iliya Tryapitsin on 21.07.15.
 */
class StatementExtApi(implicit lrs: LrsSettings) extends BaseApi() {

  val addressPathSuffix = "valamis/statements"

  private val Action     = "action"

  private val FindMinDate = "find-min-date"
  private val GetCountByParams = "get-count-by-params"

  private val Agent      = "agent"
  private val Verbs      = "verbs"
  private val Activities = "activities"
  private val Since      = "since"

  def getByParamsCount(agent:   Actor,
                       verbIds: Seq[URI]= Seq()): Try[Int] = {
    val builder = uriBuilder
      .clearParameters()
      .setPath(s"/$path/$addressPathSuffix")

    val agentJson = toJson(agent, new ActorSerializer)
    val verbsJson = toJson(verbIds map { _.toString  })

    val uri = builder
      .addParameter(Agent,  agentJson)
      .addParameter(Verbs,  verbsJson )
      .addParameter(Action, GetCountByParams)
      .build()

    val httpGet = new HttpGet(uri)
    initRequestAsJson(httpGet)

    val response = httpClient execute httpGet
    getContent(response) map { json =>
      fromJson[Int](json)
    }
  }

  def findMinDate(agent:       Actor,
                  since:       DateTime         = DateTime.now(),
                  verbIds:     Seq[URI]         = Seq(),
                  activityIds: Seq[Activity#Id] = Seq()): Try[Seq[(URI, Activity#Id, DateTime)]] = {
    val builder = uriBuilder
      .clearParameters()
      .setPath(s"/$path/$addressPathSuffix")

    val agentJson      = toJson(agent, new ActorSerializer)
    val activitiesJson = toJson(activityIds)
    val verbsJson      = toJson(verbIds map { _.toString })
    val dtFormatter    = ISODateTimeFormat.dateTime()

    val uri = builder
      .addParameter(Agent,      agentJson              )
      .addParameter(Activities, activitiesJson         )
      .addParameter(Verbs,      verbsJson              )
      .addParameter(Since,      dtFormatter print since)
      .addParameter(Action,     FindMinDate)
      .build()

    val httpGet = new HttpGet(uri)
    initRequestAsJson(httpGet)

    val response = httpClient execute httpGet
    getContent(response) map { json =>
      fromJson[Seq[(URI, Activity#Id, DateTime)]](json)
    }

  }
}
