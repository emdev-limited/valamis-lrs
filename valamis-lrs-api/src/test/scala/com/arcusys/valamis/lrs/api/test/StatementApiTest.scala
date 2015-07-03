//package com.arcusys.valamis.lrs.api.test
//
//import com.arcusys.valamis.lrs.api.StatementApi
//import org.scalatest.{Ignore, FeatureSpec}
//
//import scala.util.{Failure, Success}
//
///**
// * Created by Iliya Tryapitsin on 15.06.15.
// */
//@Ignore
//class StatementApiTest extends FeatureSpec with BaseFeatureTests {
//
//  feature("get statements") {
//    val statementApi = new StatementApi()
//    val statements = statementApi.getByParams(
//      offset = Some(100),
//      limit  = Some(10 )
//    ) match {
//      case Success(result) => result
//      case Failure(ex)     => fail(ex)
//    }
//
//    assert(statements.statements.size == 10)
//  }
//}
