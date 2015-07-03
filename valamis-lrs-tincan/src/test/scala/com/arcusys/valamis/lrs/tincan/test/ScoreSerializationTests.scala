package com.arcusys.valamis.lrs.tincan.test

import com.arcusys.valamis.lrs.serializer.ScoreSerializer
import com.arcusys.valamis.lrs.test.tincan.Scores
import com.arcusys.valamis.lrs.tincan.Score
import com.arcusys.valamis.utils.serialization.JsonHelper
import org.json4s._
import org.json4s.jackson.JsonMethods
import org.json4s.jackson.JsonMethods._

/**
 * Created by Iliya Tryapitsin on 11/03/15.
 */
class ScoreSerializationTests extends BaseSerializationTests {
  behavior of "Score serializer/deserializer testing"

  it should "serialize/deserialize" in {
    implicit val formats = DoubleJsonFormats

    val typical = JsonHelper.toJson(Scores.typical)
    val typicalScore = JsonHelper.fromJson[Score](typical, ScoreSerializer)
    val typicalScoreSerialized = JsonHelper.toJson(typicalScore, ScoreSerializer)

    val scaledOnly = JsonHelper.toJson(Scores.scaledOnly)
    val scaledOnlyScore = JsonHelper.fromJson[Score](scaledOnly, ScoreSerializer)
    val scaledOnlyScoreSerialized = JsonHelper.toJson(scaledOnlyScore, ScoreSerializer)

    val rawOnly = JsonHelper.toJson(Scores.rawOnly)
    val rawOnlyScore = JsonHelper.fromJson[Score](rawOnly, ScoreSerializer)
    val rawOnlyScoreSerialized = JsonHelper.toJson(rawOnlyScore, ScoreSerializer)

    val minOnly = JsonHelper.toJson(Scores.minOnly)
    val minOnlyScore = JsonHelper.fromJson[Score](minOnly, ScoreSerializer)
    val minOnlyScoreSerialized = JsonHelper.toJson(minOnlyScore, ScoreSerializer)

    val maxOnly = JsonHelper.toJson(Scores.maxOnly)
    val maxOnlyScore = JsonHelper.fromJson[Score](maxOnly, ScoreSerializer)
    val maxOnlyScoreSerialized = JsonHelper.toJson(maxOnlyScore, ScoreSerializer)

    val scaledAndRaw = JsonHelper.toJson(Scores.scaledAndRaw)
    val scaledAndRawScore = JsonHelper.fromJson[Score](scaledAndRaw, ScoreSerializer)
    val scaledAndRawScoreSerialized = JsonHelper.toJson(scaledAndRawScore, ScoreSerializer)

    val scaledAndMin = JsonHelper.toJson(Scores.scaledAndMin)
    val scaledAndMinScore = JsonHelper.fromJson[Score](scaledAndMin, ScoreSerializer)
    val scaledAndMinScoreSerialized = JsonHelper.toJson(scaledAndMinScore, ScoreSerializer)

    val scaledAndMax = JsonHelper.toJson(Scores.scaledAndMax)
    val scaledAndMaxScore = JsonHelper.fromJson[Score](scaledAndMax, ScoreSerializer)
    val scaledAndMaxScoreSerialized = JsonHelper.toJson(scaledAndMaxScore, ScoreSerializer)

    val rawAndMin = JsonHelper.toJson(Scores.rawAndMin)
    val rawAndMinScore = JsonHelper.fromJson[Score](rawAndMin, ScoreSerializer)
    val rawAndMinScoreSerialized = JsonHelper.toJson(rawAndMinScore, ScoreSerializer)

    val rawAndMax = JsonHelper.toJson(Scores.rawAndMax)
    val rawAndMaxScore = JsonHelper.fromJson[Score](rawAndMax, ScoreSerializer)
    val rawAndMaxScoreSerialized = JsonHelper.toJson(rawAndMaxScore, ScoreSerializer)

    val minAndMax = JsonHelper.toJson(Scores.minAndMax)
    val minAndMaxScore = JsonHelper.fromJson[Score](minAndMax, ScoreSerializer)
    val minAndMaxScoreSerialized = JsonHelper.toJson(minAndMaxScore, ScoreSerializer)

    val scaledRawAndMin = JsonHelper.toJson(Scores.scaledRawAndMin)
    val scaledRawAndMinScore = JsonHelper.fromJson[Score](scaledRawAndMin, ScoreSerializer)
    val scaledRawAndMinScoreSerialized = JsonHelper.toJson(scaledRawAndMinScore, ScoreSerializer)

    val scaledRawAndMax = JsonHelper.toJson(Scores.scaledRawAndMax)
    val scaledRawAndMaxScore = JsonHelper.fromJson[Score](scaledRawAndMax, ScoreSerializer)
    val scaledRawAndMaxScoreSerialized = JsonHelper.toJson(scaledRawAndMaxScore, ScoreSerializer)

    val rawMinAndMax = JsonHelper.toJson(Scores.rawMinAndMax)
    val rawMinAndMaxScore = JsonHelper.fromJson[Score](rawMinAndMax, ScoreSerializer)
    val rawMinAndMaxScoreSerialized = JsonHelper.toJson(rawMinAndMaxScore, ScoreSerializer)

    val allProperties = JsonHelper.toJson(Scores.allProperties)
    val allPropertiesScore = JsonHelper.fromJson[Score](allProperties, ScoreSerializer)
    val allPropertiesScoreSerialized = JsonHelper.toJson(allPropertiesScore, ScoreSerializer)
  }

}
