package com.arcusys.valamis.lrs.api.valamis

import com.arcusys.valamis.lrs.SeqWithCount
import com.arcusys.valamis.lrs.api.{BaseApi, LrsSettings}
import com.arcusys.valamis.lrs.serializer.DateTimeSerializer
import com.arcusys.valamis.lrs.tincan.{LanguageMap, Verb}
import com.arcusys.valamis.lrs.tincan.valamis.VerbStatistics
import com.arcusys.valamis.utils.serialization.JsonHelper
import org.apache.http.client.methods.HttpGet
import org.joda.time.DateTime

import scala.util.Try

/**
 * Created by Iliya Tryapitsin on 16.06.15.
 */
class VerbApi(implicit lrs: LrsSettings) extends BaseApi() {

  override def addressPathSuffix = "verb"

  private val Since   = "since"
  private val Filter  = "filter"
  private val Limit   = "limit"
  private val Offset  = "offset"
  private val Action  = "action"
  private val AscSort = "asc-sort"
  private val VerbStatistics      = "verb-statistics"
  private val VerbsWithActivities = "verb-with-activities"
  /**
   * Return verbs from Valamis LRS
   * @param since Return since
   * @return
   */
  def getStatistics(since: Option[DateTime] = None): Try[VerbStatistics] = {
    val uri = uriBuilder
      .clearParameters()
      .setPath(s"/$path/$addressPathSuffix")
      .addOptionParameter(Since, since)
      .addParameter(Action, VerbStatistics)
      .build()

    val httpGet = new HttpGet(uri)
    initRequestAsJson(httpGet)

    val response = httpClient.execute(httpGet)
    getContent(response) map { json =>
      JsonHelper.fromJson[VerbStatistics](json, DateTimeSerializer)
    }
  }

  def getWithActivities(filter: Option[String] = None,
                        limit:  Int            = 100,
                        offset: Int            = 0,
                        ascSort:Boolean        = true  ): Try[SeqWithCount[(Verb, (String, LanguageMap))]] = {

    val uri = uriBuilder
      .clearParameters()
      .setPath(s"/$path/$addressPathSuffix")
      .addOptionParameter(Filter, filter)
      .addParameter(Limit,   limit   toString)
      .addParameter(Offset,  offset  toString)
      .addParameter(AscSort, ascSort toString)
      .addParameter(Action,  VerbsWithActivities)
      .build()

    val httpGet = new HttpGet(uri)
    initRequestAsJson(httpGet)

    val response = httpClient.execute(httpGet)
    getContent(response) map { json =>
      JsonHelper.fromJson[SeqWithCount[(Verb, (String, LanguageMap))]](json, DateTimeSerializer)
    }
  }
}
