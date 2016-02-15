package com.arcusys.valamis.lrs.tincan.test

import com.arcusys.valamis.lrs.serializer.EnumNameIgnoreCaseSerializer
import com.arcusys.valamis.lrs.tincan.{ContextActivities, StatementObjectType}
import com.arcusys.json.JsonHelper
import org.json4s.{DefaultFormats, Formats}
/**
 * Created by Iliya Tryapitsin on 30/12/14.
 */
class ContextActivitySerializationTests extends BaseSerializationTests {
  behavior of "Context activity serializer/deserializer testing"

  it should "JSON with context activities deserialize correct" in {
    val raw = loadDataFromTxtResource("/context-activity-1.json")
    implicit val jsonFormats: Formats = DefaultFormats
    val result = JsonHelper.fromJson[ContextActivities](raw, new EnumNameIgnoreCaseSerializer(StatementObjectType))

    assert(!result.grouping.isEmpty)
    assert(Some(StatementObjectType.Activity) == result.grouping.head.objectType)
    assert("http://tincanapi.com/GolfExample_TCAPI" == result.grouping.head.id)

    val json = JsonHelper.toJson(result, new EnumNameIgnoreCaseSerializer(StatementObjectType))
    assert(!json.isEmpty)
  }

  it should "JSON with context activities case #2 deserialize correct" in {
    val raw = loadDataFromTxtResource("/context-activity-2.json")
    val result = JsonHelper.fromJson[ContextActivities](raw, new EnumNameIgnoreCaseSerializer(StatementObjectType))

    assert(!result.grouping.isEmpty)
    assert(None == result.grouping.head.objectType)
    assert("http://example.adlnet.gov/xapi/example/Algebra1" == result.grouping.head.id)
    assert(!result.parent.isEmpty)
    assert(None == result.parent.head.objectType)
    assert("http://example.adlnet.gov/xapi/example/test 1" == result.parent.head.id)
    
    val json = JsonHelper.toJson(result, new EnumNameIgnoreCaseSerializer(StatementObjectType))
    assert(!json.isEmpty)
  }

//  it should "serialize/deserialize context activities" in {
//    val typical = JsonHelper.toJson(ca.typical)
//    val typicalCA = JsonHelper.fromJson[ContextActivities](typical, new EnumNameIgnoreCaseSerializer(StatementObjectType))
//    val typicalCASerialized = JsonHelper.toJson(typicalCA, new EnumNameIgnoreCaseSerializer(StatementObjectType))
//
//    val categoryOnly = JsonHelper.toJson(ca.categoryOnly)
//    val categoryOnlyCA = JsonHelper.fromJson[ContextActivities](categoryOnly, new EnumNameIgnoreCaseSerializer(StatementObjectType))
//    val categoryOnlyCASerialized = JsonHelper.toJson(categoryOnlyCA, new EnumNameIgnoreCaseSerializer(StatementObjectType))
//
//    val parentOnly = JsonHelper.toJson(ca.parentOnly)
//    val parentOnlyCA = JsonHelper.fromJson[ContextActivities](parentOnly, new EnumNameIgnoreCaseSerializer(StatementObjectType))
//    val parentOnlyCASerialized = JsonHelper.toJson(parentOnlyCA, new EnumNameIgnoreCaseSerializer(StatementObjectType))
//
//    val otherOnly = JsonHelper.toJson(ca.otherOnly)
//    val otherOnlyCA = JsonHelper.fromJson[ContextActivities](otherOnly, new EnumNameIgnoreCaseSerializer(StatementObjectType))
//    val otherOnlyCASerialized = JsonHelper.toJson(otherOnlyCA, new EnumNameIgnoreCaseSerializer(StatementObjectType))
//
//    val groupingOnly = JsonHelper.toJson(ca.groupingOnly)
//    val groupingOnlyCA = JsonHelper.fromJson[ContextActivities](groupingOnly, new EnumNameIgnoreCaseSerializer(StatementObjectType))
//    val groupingOnlyCASerialized = JsonHelper.toJson(groupingOnlyCA, new EnumNameIgnoreCaseSerializer(StatementObjectType))
//
//    val allPropertiesEmpty = JsonHelper.toJson(ca.allPropertiesEmpty)
//    val allPropertiesEmptyCA = JsonHelper.fromJson[ContextActivities](allPropertiesEmpty, new EnumNameIgnoreCaseSerializer(StatementObjectType))
//    val allPropertiesEmptyCASerialized = JsonHelper.toJson(allPropertiesEmptyCA, new EnumNameIgnoreCaseSerializer(StatementObjectType))
//
//    val allProperties = JsonHelper.toJson(ca.allProperties)
//    val allPropertiesCA = JsonHelper.fromJson[ContextActivities](allProperties, new EnumNameIgnoreCaseSerializer(StatementObjectType))
//    val allPropertiesCASerialized = JsonHelper.toJson(allPropertiesCA, new EnumNameIgnoreCaseSerializer(StatementObjectType))
//
//  }
}
