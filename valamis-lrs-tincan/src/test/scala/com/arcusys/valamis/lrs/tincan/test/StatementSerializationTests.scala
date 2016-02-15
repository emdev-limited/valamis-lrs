package com.arcusys.valamis.lrs.tincan.test.serializer

import com.arcusys.json.{JsonDeserializeException, JsonHelper}
import com.arcusys.valamis.lrs.serializer._
import com.arcusys.valamis.lrs.test.tincan._
import com.arcusys.valamis.lrs.tincan.Statement
import org.scalatest.FlatSpecLike

/**
 * Created by Iliya Tryapitsin on 30/12/14.
 */
class StatementSerializationTests extends BaseSerializationTemplate(Statements.Good, Statements.Bad) with FlatSpecLike {
  behavior of "Statement serializer/deserializer testing"

  override def badTemplate(caseName: String) = it should s"throw JSONDeserializerException for $caseName statement" in {

    val statement = badData.get(caseName)
    val json = JsonHelper.toJson(statement)
    intercept[JsonDeserializeException] {
      JsonHelper.fromJson[Statement](json, new StatementSerializer)
    }
  }

  override def goodTemplate(caseName: String) = it should s"be successful for $caseName" in {

    import Helper._

    val rawStatement = goodData.get(caseName)
    val json = JsonHelper.toJson(rawStatement)
    val statement = JsonHelper.fromJson[Statement](json, new StatementSerializer)
    val serializedStatement = JsonHelper.toJson[Statement](statement, new StatementSerializer)
    val deserializedStatement = JsonHelper.fromJson[Statement](serializedStatement, new StatementSerializer)
    val deserializedStatementFields = deserializedStatement.fieldValues
    val statementFields = statement.fieldValues

    statementFields.foreach { field => assert (field._2 equals deserializedStatementFields.get(field._1).get ) }
  }

  run
}
