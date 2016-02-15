package com.arcusys.valamis.lrs.tincan.test

import com.arcusys.valamis.lrs.serializer._
import com.arcusys.valamis.lrs.test.tincan._
import com.arcusys.json.{JsonDeserializeException, JsonHelper}
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

    def scenarioTemplate[T <: AnyRef](testCase: (String, AnyRef), serializer: CustomSerializer[T])
                           (implicit man: Manifest[T]) = scenario(s"${testCase._1}") {
      val rawData = testCase._2.asInstanceOf[AnyRef]
      val rawDataJson = JsonHelper.toJson(rawData)
      val deserializedRawData = JsonHelper.fromJson[T](rawDataJson, serializer)
      val serializedRawData = JsonHelper.toJson(deserializedRawData, serializer)
    }

    statements  foreach { x => scenarioTemplate(x.asInstanceOf[(String, AnyRef)], new StatementSerializer) }
    activities  foreach { x => scenarioTemplate(x.asInstanceOf[(String, AnyRef)], new ActivitySerializer ) }
    agents      foreach { x => scenarioTemplate(x.asInstanceOf[(String, AnyRef)], new AgentSerializer    ) }
    verbs       foreach { x => scenarioTemplate(x.asInstanceOf[(String, AnyRef)], VerbSerializer         ) }
    subStmnts   foreach { x => scenarioTemplate(x.asInstanceOf[(String, AnyRef)], new SubStatementSerializer) }
    scores      foreach { x => scenarioTemplate(x.asInstanceOf[(String, AnyRef)], ScoreSerializer        ) }
    results     foreach { x => scenarioTemplate(x.asInstanceOf[(String, AnyRef)], ResultSerializer       ) }
    contexts    foreach { x => scenarioTemplate(x.asInstanceOf[(String, AnyRef)], new ContextSerializer  ) }

  }

  feature("Bad serialize/de-serialize tests") {
    scenario("Throw exception for incorrect URI in account") {
      intercept[JsonDeserializeException] {
        val stmnt = Statements.Good.minimal.copy(actor = Agents.Bad.`incorrect Account uri`)
        val rawDataJson = JsonHelper.toJson(stmnt)
        val deserializedRawData = JsonHelper.fromJson[com.arcusys.valamis.lrs.tincan.Statement](rawDataJson, new StatementSerializer())
      }
    }
  }
}
