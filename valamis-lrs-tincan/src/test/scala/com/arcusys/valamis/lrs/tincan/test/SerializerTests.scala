package com.arcusys.valamis.lrs.tincan.test

import com.arcusys.valamis.lrs.serializer._
import com.arcusys.valamis.lrs.test.tincan._
import com.arcusys.valamis.utils.serialization.JsonHelper
import org.json4s.CustomSerializer
import org.scalatest.{FeatureSpec, GivenWhenThen}

/**
 * Created by Iliya Tryapitsin on 10/03/15.
 */
class SerializerTests extends FeatureSpec with GivenWhenThen {
  feature("Good serialize/de-serialize tests") {

    import Helper._

    val activities = Activities     .Good.fieldValues
    val agents     = Agents         .Good.fieldValues
    val verbs      = Verbs          .Good.fieldValues
    val subStmnts  = SubStatements  .Good.fieldValues
    val scores     = Scores         .Good.fieldValues
    val results    = Results        .Good.fieldValues
    val contexts   = Contexts       .Good.fieldValues
    val statements = Statements     .Good.fieldValues

    def scenarioTemplate[T](testCase: (String, Any), serializer: CustomSerializer[T])
                           (implicit man: Manifest[T]) = scenario(s"${testCase._1}") {
      val rawData = testCase._2
      val rawDataJson = JsonHelper.toJson(rawData)
      val deserializedRawData = JsonHelper.fromJson[T](rawDataJson, serializer)
      val serializedRawData = JsonHelper.toJson(deserializedRawData, serializer)
    }

    statements  foreach { x => scenarioTemplate(x, new StatementSerializer) }
    activities  foreach { x => scenarioTemplate(x, new ActivitySerializer ) }
    agents      foreach { x => scenarioTemplate(x, new AgentSerializer    ) }
    verbs       foreach { x => scenarioTemplate(x, VerbSerializer         ) }
    subStmnts   foreach { x => scenarioTemplate(x, new SubStatementSerializer) }
    scores      foreach { x => scenarioTemplate(x, ScoreSerializer        ) }
    results     foreach { x => scenarioTemplate(x, ResultSerializer       ) }
    contexts    foreach { x => scenarioTemplate(x, new ContextSerializer  ) }
  }
}
