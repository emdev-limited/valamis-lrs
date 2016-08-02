package com.arcusys.valamis.lrs.api.valamis

import com.arcusys.valamis.lrs.SeqWithCount
import com.arcusys.valamis.lrs.api._
import com.arcusys.valamis.lrs.serializer.DateTimeSerializer
import com.arcusys.valamis.lrs.tincan.Verb
import com.arcusys.valamis.lrs.tincan.valamis.{ActivityIdLanguageMap,VerbStatistics}
import org.apache.http.client.methods.HttpGet
import org.joda.time.DateTime

import scala.util.Try

/**
 * Created by Iliya Tryapitsin on 16.06.15.
 */
class VerbApi(val oauthInvoker: Option[OAuthInvoker] = None)(implicit lrs: LrsSettings) extends BaseApi() {

  val addressPathSuffix = "valamis/verb"

  private val Since   = "since"
  private val Verb    = "verb"
  private val Filter  = "filter"
  private val Limit   = "limit"
  private val Offset  = "offset"
  private val Action  = "action"
  private val NameSort = "name-sort"
  private val TimeSort = "time-sort"
  private val SortTimeFirst = "sortTimeFirst"
  private val VerbStatistics      = "verb-statistics"
  private val VerbsWithActivities = "verb-with-activities"
  private val VerbsAmount = "verb-amount"

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

    val respContent = invokeHttpRequest(oauthInvoker, httpGet)
    respContent map { json =>
      fromJson[VerbStatistics](json, DateTimeSerializer)
    }
  }

  def getWithActivities(filter: Option[String] = None,
                        limit:  Int             = 100,
                        offset: Int             = 0,
                        nameSortDesc: Boolean   = true,
                        timeSortDesc: Boolean   = false,
                        sortTimeFirst: Boolean  = false): Try[SeqWithCount[(Verb, ActivityIdLanguageMap, Option[DateTime])]] = {

    val uri = uriBuilder
      .clearParameters()
      .setPath(s"/$path/$addressPathSuffix")
      .addOptionParameter(Filter,  filter)
      .addParameter(Limit,         limit         toString)
      .addParameter(Offset,        offset        toString)
      .addParameter(NameSort,      nameSortDesc  toString)
      .addParameter(TimeSort,      timeSortDesc  toString)
      .addParameter(SortTimeFirst, sortTimeFirst toString)
      .addParameter(Action,  VerbsWithActivities)
      .build()

    val httpGet = new HttpGet(uri)
    initRequestAsJson(httpGet)

    val respContent = invokeHttpRequest(oauthInvoker, httpGet)
    respContent map { json =>
      fromJson[SeqWithCount[(Verb, ActivityIdLanguageMap, Option[DateTime])]](json, DateTimeSerializer)
    }
  }

  def getAmount(since: Option[DateTime] = None, verb: Option[String] = None): Try[String] = {
    val uri = uriBuilder
      .clearParameters()
      .setPath(s"/$path/$addressPathSuffix")
      .addOptionParameter(Since, since)
      .addOptionParameter(Verb, verb)
      .addParameter(Action, VerbsAmount)
      .build()

    val httpGet = new HttpGet(uri)
    initRequestAsJson(httpGet)

    invokeHttpRequest(oauthInvoker, httpGet)
  }
}
