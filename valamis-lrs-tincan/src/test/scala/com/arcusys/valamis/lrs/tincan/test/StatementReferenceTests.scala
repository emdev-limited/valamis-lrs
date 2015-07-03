package com.arcusys.valamis.lrs.tincan.test

import com.arcusys.valamis.lrs.test.tincan.StatementRefs
import com.arcusys.valamis.lrs.tincan.StatementReference
import com.arcusys.valamis.lrs.serializer._
import com.arcusys.valamis.utils.serialization.JsonHelper

/**
 * Created by Iliya Tryapitsin on 03/04/15.
 */
class StatementReferenceTests extends BaseSerializationTests {
  behavior of "Statement reference serializer/deserializer testing"

  it should "serialize/deserialize" in {
    val json = StatementRefs.typical
    val statementRefJson = JsonHelper.toJson(json, tincanSerializers(): _*)
    val statementRef = JsonHelper.fromJson[StatementReference](statementRefJson, tincanSerializers(): _*)
    val serializedStatementRef = JsonHelper.toJson(statementRef, tincanSerializers(): _*)

    assert(serializedStatementRef.length == statementRefJson.length)
  }

}
