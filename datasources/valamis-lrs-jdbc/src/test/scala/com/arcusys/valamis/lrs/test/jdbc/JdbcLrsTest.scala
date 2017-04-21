//package com.arcusys.valamis.lrs.test.jdbc
//
//import java.net.URI
//import java.sql.Connection
//import java.util.UUID
//
//import com.arcusys.valamis.lrs.{StatementQuery, Lrs}
//import com.arcusys.valamis.lrs.history.ver230.{DbSchemaUpgrade => Ver230}
//import com.arcusys.valamis.lrs.history.ver240.{DbSchemaUpgrade => Ver240}
//import com.arcusys.valamis.lrs.history.ver250.{DbSchemaUpgrade => Ver250}
//import com.arcusys.valamis.lrs.jdbc.JdbcLrs
//import com.arcusys.valamis.lrs.tincan._
//import org.joda.time.DateTime
//import org.scalatest.{BeforeAndAfter, FunSuite}
//
//import scala.concurrent.duration.Duration
//import scala.slick.driver.H2Driver
//import scala.slick.driver.H2Driver.simple._
//
//class JdbcLrsTest extends FunSuite with BeforeAndAfter {
//
//  val db = Database.forURL("jdbc:h2:mem:test3", driver = "org.h2.Driver")
//  var connection: Connection = _
//
//  var lrs: Lrs = _
//  val driver = H2Driver
//
//  before {
//    connection = db.createConnection()
//
//    lrs = new JdbcLrs(driver, db, null)
//
//    val schema230 = new Ver230(driver, db, lrs.asInstanceOf[JdbcLrs])
//    schema230.upgrade
//
//    val schema240 = new Ver240(driver, db, lrs.asInstanceOf[JdbcLrs])
//    schema240.upgrade
//
//    val schema250 = new Ver250(driver, db)
//    schema250.upgrade
//
//  }
//  after {
//    val schema240 = new Ver240(driver, db, lrs.asInstanceOf[JdbcLrs])
//    schema240.downgrade
//    val schema230 = new Ver230(driver, db, lrs.asInstanceOf[JdbcLrs])
//    schema230.downgrade
//
//    connection.close()
//  }
//
//  test("add and get statement") {
//    val id = UUID.randomUUID()
//    val timeout = Duration.Inf
//    val statement = Statement(
//      id = Option(id),
//      actor = Agent(Option("Test Test"), Option("mailto:test@test.com")),
//      verb = Verb("http://adlnet.gov/expapi/verbs/answered", display = Map("en-US" -> "test")),
//      obj = Activity("http://adlnet.gov/expapi/activity/act1"),
//      stored = DateTime.now,
//      timestamp = DateTime.now
//    )
//    lrs.addStatement(statement)
//    val statements = lrs.findStatements(new StatementQuery)
//    assert(1 == statements.seq.size)
//    assert(statements.seq.head.id == statement.id)
//  }
//
//  test("statement filters: statementId") {
//    val id = UUID.randomUUID()
//    val statement = Statement(
//      id = Option(id),
//      actor = Agent(Option("Test Test"), Option("mailto:test@test.com")),
//      verb = Verb("http://adlnet.gov/expapi/verbs/answered", display = Map("en-US" -> "test")),
//      obj = Activity("http://adlnet.gov/expapi/activity/act1"),
//      stored = DateTime.now,
//      timestamp = DateTime.now
//    )
//    lrs.addStatement(statement)
//    val statements = lrs.findStatements(new StatementQuery(statementId = Option(id)))
//    assert(1 == statements.seq.size)
//    assert(statements.seq.head.id == statement.id)
//  }
//
//  test("statement filters: voidedStatementId") {
//    val id = UUID.randomUUID()
//    val statement = Statement(
//      id = Option(id),
//      actor = Agent(Option("Test Test"), Option("mailto:test@test.com")),
//      verb = Verb("http://adlnet.gov/expapi/verbs/answered", display = Map("en-US" -> "test")),
//      obj = Activity("http://adlnet.gov/expapi/activity/act1"),
//      stored = DateTime.now,
//      timestamp = DateTime.now
//    )
//    val id2 = UUID.randomUUID()
//    val statement2 = Statement(
//      id = Option(id2),
//      actor = Agent(Option("Test Test"), Option("mailto:test@test.com")),
//      verb = Verb("http://adlnet.gov/expapi/verbs/voided", display = Map("en-US" -> "voided")),
//      obj = StatementReference(id),
//      stored = DateTime.now,
//      timestamp = DateTime.now
//    )
//    lrs.addStatement(statement)
//    lrs.addStatement(statement2)
//    var statements = lrs.findStatements(new StatementQuery(statementId = Option(id)))
//    assert(0 == statements.seq.size)
//
//    statements = lrs.findStatements(new StatementQuery(voidedStatementId = Option(id)))
//    assert(1 == statements.seq.size)
//    assert(statements.seq.head.id == statement.id)
//  }
//
//  test("statement filters: agent") {
//    val id = UUID.randomUUID()
//    val statement = Statement(
//      id = Option(id),
//      actor = Agent(Option("Test Test"), Option("mailto:test@test.com")),
//      verb = Verb("http://adlnet.gov/expapi/verbs/answered", display = Map("en-US" -> "test")),
//      obj = Activity("http://adlnet.gov/expapi/activity/act1"),
//      stored = DateTime.now,
//      timestamp = DateTime.now
//    )
//    val id2 = UUID.randomUUID()
//    val statement2 = Statement(
//      id = Option(id2),
//      actor = Agent(Option("Test Test"), Option("mailto:test1@test.com")),
//      verb = Verb("http://adlnet.gov/expapi/verbs/passed", display = Map("en-US" -> "passed")),
//      obj = Activity("http://adlnet.gov/expapi/activity/act2"),
//      stored = DateTime.now,
//      timestamp = DateTime.now
//    )
//    lrs.addStatement(statement)
//    lrs.addStatement(statement2)
//    var statements = lrs.findStatements(new StatementQuery(agent = Option(Agent(mBox = Option("mailto:test@test.com")))))
//    assert(1 == statements.seq.size)
//    assert(statements.seq.head.id == statement.id)
//
//    statements = lrs.findStatements(new StatementQuery(agent = Option(Agent(mBox = Option("mailto:test1@test.com")))))
//    assert(1 == statements.seq.size)
//    assert(statements.seq.head.id == statement2.id)
//  }
//
//  test("statement filters: verb") {
//    val id = UUID.randomUUID()
//    val statement = Statement(
//      id = Option(id),
//      actor = Agent(Option("Test Test"), Option("mailto:test@test.com")),
//      verb = Verb("http://adlnet.gov/expapi/verbs/answered", display = Map("en-US" -> "test")),
//      obj = Activity("http://adlnet.gov/expapi/activity/act1"),
//      stored = DateTime.now,
//      timestamp = DateTime.now
//    )
//    val id2 = UUID.randomUUID()
//    val statement2 = Statement(
//      id = Option(id2),
//      actor = Agent(Option("Test Test"), Option("mailto:test1@test.com")),
//      verb = Verb("http://adlnet.gov/expapi/verbs/passed", display = Map("en-US" -> "passed")),
//      obj = Activity("http://adlnet.gov/expapi/activity/act2"),
//      stored = DateTime.now,
//      timestamp = DateTime.now
//    )
//    lrs.addStatement(statement)
//    lrs.addStatement(statement2)
//    var statements = lrs.findStatements(new StatementQuery(verb = Option(new URI("http://adlnet.gov/expapi/verbs/answered"))))
//    assert(1 == statements.seq.size)
//    assert(statements.seq.head.id == statement.id)
//
//    statements = lrs.findStatements(new StatementQuery(verb = Option(new URI("http://adlnet.gov/expapi/verbs/passed"))))
//    assert(1 == statements.seq.size)
//    assert(statements.seq.head.id == statement2.id)
//  }
//
//  test("statement filters: activity") {
//    val id = UUID.randomUUID()
//    val statement = Statement(
//      id = Option(id),
//      actor = Agent(Option("Test Test"), Option("mailto:test@test.com")),
//      verb = Verb("http://adlnet.gov/expapi/verbs/answered", display = Map("en-US" -> "test")),
//      obj = Activity("http://adlnet.gov/expapi/activity/act1"),
//      stored = DateTime.now,
//      timestamp = DateTime.now
//    )
//    val id2 = UUID.randomUUID()
//    val statement2 = Statement(
//      id = Option(id2),
//      actor = Agent(Option("Test Test"), Option("mailto:test1@test.com")),
//      verb = Verb("http://adlnet.gov/expapi/verbs/passed", display = Map("en-US" -> "passed")),
//      obj = Activity("http://adlnet.gov/expapi/activity/act2"),
//      stored = DateTime.now,
//      timestamp = DateTime.now
//    )
//    lrs.addStatement(statement)
//    lrs.addStatement(statement2)
//    var statements = lrs.findStatements(new StatementQuery(activity = Option(new URI("http://adlnet.gov/expapi/activity/act1"))))
//    assert(1 == statements.seq.size)
//    assert(statements.seq.head.id == statement.id)
//
//    statements = lrs.findStatements(new StatementQuery(activity = Option(new URI("http://adlnet.gov/expapi/activity/act2"))))
//    assert(1 == statements.seq.size)
//    assert(statements.seq.head.id == statement2.id)
//  }
//
//  test("statement filters: registration") {
//    val id = UUID.randomUUID()
//    val idReg = UUID.randomUUID()
//    val statement = Statement(
//      id = Option(id),
//      actor = Agent(Option("Test Test"), Option("mailto:test@test.com")),
//      verb = Verb("http://adlnet.gov/expapi/verbs/answered", display = Map("en-US" -> "test")),
//      obj = Activity("http://adlnet.gov/expapi/activity/act1"),
//      stored = DateTime.now,
//      timestamp = DateTime.now,
//      context = Option(Context(registration = Option(idReg)))
//    )
//    val id2 = UUID.randomUUID()
//    val statement2 = Statement(
//      id = Option(id2),
//      actor = Agent(Option("Test Test"), Option("mailto:test1@test.com")),
//      verb = Verb("http://adlnet.gov/expapi/verbs/passed", display = Map("en-US" -> "passed")),
//      obj = Activity("http://adlnet.gov/expapi/activity/act2"),
//      stored = DateTime.now,
//      timestamp = DateTime.now
//    )
//    lrs.addStatement(statement)
//    lrs.addStatement(statement2)
//    var statements = lrs.findStatements(new StatementQuery(activity = Option(new URI("http://adlnet.gov/expapi/activity/act1"))))
//    assert(1 == statements.seq.size)
//    assert(statements.seq.head.id == statement.id)
//
//    statements = lrs.findStatements(new StatementQuery(activity = Option(new URI("http://adlnet.gov/expapi/activity/act2"))))
//    assert(1 == statements.seq.size)
//    assert(statements.seq.head.id == statement2.id)
//  }
//
//  test("statement filters: related_activities") {
//    val id = UUID.randomUUID()
//    val idReg = UUID.randomUUID()
//    val statement = Statement(
//      id = Option(id),
//      actor = Agent(Option("Test Test"), Option("mailto:test@test.com")),
//      verb = Verb("http://adlnet.gov/expapi/verbs/answered", display = Map("en-US" -> "test")),
//      obj = Activity("http://adlnet.gov/expapi/activity/act1"),
//      stored = DateTime.now,
//      timestamp = DateTime.now,
//      context = Option(Context(
//        registration = Option(idReg),
//        contextActivities = Option(ContextActivities(grouping = Seq(ActivityReference("http://adlnet.gov/expapi/activity/act3"))))
//      ))
//    )
//    val id2 = UUID.randomUUID()
//    val statement2 = Statement(
//      id = Option(id2),
//      actor = Agent(Option("Test Test"), Option("mailto:test1@test.com")),
//      verb = Verb("http://adlnet.gov/expapi/verbs/passed", display = Map("en-US" -> "passed")),
//      obj = Activity("http://adlnet.gov/expapi/activity/act2"),
//      stored = DateTime.now,
//      timestamp = DateTime.now
//    )
//    val id3 = UUID.randomUUID()
//    val statement3 = Statement(
//      id = Option(id3),
//      actor = Agent(Option("Test Test"), Option("mailto:test1@test.com")),
//      verb = Verb("http://adlnet.gov/expapi/verbs/passed", display = Map("en-US" -> "passed")),
//      obj = SubStatement(
//        actor = Agent(Option("Test Test"), Option("mailto:test3@test.com")),
//        verb = Verb("http://adlnet.gov/expapi/verbs/passed", display = Map("en-US" -> "passed")),
//        obj = Activity("http://adlnet.gov/expapi/activity/act4")
//      ),
//      stored = DateTime.now,
//      timestamp = DateTime.now
//    )
//    lrs.addStatement(statement)
//    lrs.addStatement(statement2)
//    lrs.addStatement(statement3)
//    var statements = lrs.findStatements(new StatementQuery(activity = Option(new URI("http://adlnet.gov/expapi/activity/act3")), relatedActivities = true))
//    assert(1 == statements.seq.size)
//    assert(statements.seq.head.id == statement.id)
//
//    statements = lrs.findStatements(new StatementQuery(activity = Option(new URI("http://adlnet.gov/expapi/activity/act4")), relatedActivities = true))
//    assert(1 == statements.seq.size)
//    assert(statements.seq.head.id == statement3.id)
//  }
//
//  test("statement filters: related_agents") {
//    val id = UUID.randomUUID()
//    val idReg = UUID.randomUUID()
//    val statement = Statement(
//      id = Option(id),
//      actor = Agent(Option("Test Test"), Option("mailto:test@test.com")),
//      verb = Verb("http://adlnet.gov/expapi/verbs/answered", display = Map("en-US" -> "test")),
//      obj = Activity("http://adlnet.gov/expapi/activity/act1"),
//      stored = DateTime.now,
//      timestamp = DateTime.now,
//      context = Option(Context(
//        registration = Option(idReg),
//        instructor = Option(Agent(Option("Test Test"), Option("mailto:test3@test.com")))
//      ))
//    )
//    val id2 = UUID.randomUUID()
//    val statement2 = Statement(
//      id = Option(id2),
//      actor = Agent(Option("Test Test"), Option("mailto:test1@test.com")),
//      verb = Verb("http://adlnet.gov/expapi/verbs/passed", display = Map("en-US" -> "passed")),
//      obj = Activity("http://adlnet.gov/expapi/activity/act2"),
//      stored = DateTime.now,
//      timestamp = DateTime.now,
//      context = Option(Context(
//        registration = Option(idReg),
//        team = Option(Group(Option("Test Test"), Option(Seq(Agent(Option("Test Test"), Option("mailto:test4@test.com")))), Option("mailto:test5@test.com")))
//      ))
//    )
//    val id3 = UUID.randomUUID()
//    val statement3 = Statement(
//      id = Option(id3),
//      actor = Agent(Option("Test Test"), Option("mailto:test2@test.com")),
//      verb = Verb("http://adlnet.gov/expapi/verbs/passed", display = Map("en-US" -> "passed")),
//      obj = SubStatement(
//        actor = Agent(Option("Test Test"), Option("mailto:test6@test.com")),
//        verb = Verb("http://adlnet.gov/expapi/verbs/passed", display = Map("en-US" -> "passed")),
//        obj = Activity("http://adlnet.gov/expapi/activity/act4")
//      ),
//      stored = DateTime.now,
//      timestamp = DateTime.now
//    )
//    lrs.addStatement(statement)
//    lrs.addStatement(statement2)
//    lrs.addStatement(statement3)
//    var statements = lrs.findStatements(new StatementQuery(agent = Option(Agent(mBox = Option("mailto:test3@test.com"))), relatedAgents = true))
//    assert(1 == statements.seq.size)
//    assert(statements.seq.head.id == statement.id)
//
//    statements = lrs.findStatements(new StatementQuery(agent = Option(Agent(mBox = Option("mailto:test4@test.com"))), relatedAgents = true))
//    assert(1 == statements.seq.size)
//    assert(statements.seq.head.id == statement2.id)
//
//    statements = lrs.findStatements(new StatementQuery(agent = Option(Agent(mBox = Option("mailto:test5@test.com"))), relatedAgents = true))
//    assert(1 == statements.seq.size)
//    assert(statements.seq.head.id == statement2.id)
//
//    statements = lrs.findStatements(new StatementQuery(agent = Option(Agent(mBox = Option("mailto:test6@test.com"))), relatedAgents = true))
//    assert(1 == statements.seq.size)
//    assert(statements.seq.head.id == statement3.id)
//  }
//
//  test("statement filters: since and until") {
//    val id = UUID.randomUUID()
//    val idReg = UUID.randomUUID()
//    val statement = Statement(
//      id = Option(id),
//      actor = Agent(Option("Test Test"), Option("mailto:test@test.com")),
//      verb = Verb("http://adlnet.gov/expapi/verbs/answered", display = Map("en-US" -> "test")),
//      obj = Activity("http://adlnet.gov/expapi/activity/act1"),
//      stored = DateTime.now,
//      timestamp = DateTime.now.minusDays(5),
//      context = Option(Context(
//        registration = Option(idReg),
//        instructor = Option(Agent(Option("Test Test"), Option("mailto:test3@test.com")))
//      ))
//    )
//    val id2 = UUID.randomUUID()
//    val statement2 = Statement(
//      id = Option(id2),
//      actor = Agent(Option("Test Test"), Option("mailto:test1@test.com")),
//      verb = Verb("http://adlnet.gov/expapi/verbs/passed", display = Map("en-US" -> "passed")),
//      obj = Activity("http://adlnet.gov/expapi/activity/act2"),
//      stored = DateTime.now,
//      timestamp = DateTime.now.minusDays(3),
//      context = Option(Context(
//        registration = Option(idReg),
//        team = Option(Group(Option("Test Test"), Option(Seq(Agent(Option("Test Test"), Option("mailto:test4@test.com")))), Option("mailto:test5@test.com")))
//      ))
//    )
//    val id3 = UUID.randomUUID()
//    val statement3 = Statement(
//      id = Option(id3),
//      actor = Agent(Option("Test Test"), Option("mailto:test2@test.com")),
//      verb = Verb("http://adlnet.gov/expapi/verbs/passed", display = Map("en-US" -> "passed")),
//      obj = SubStatement(
//        actor = Agent(Option("Test Test"), Option("mailto:test6@test.com")),
//        verb = Verb("http://adlnet.gov/expapi/verbs/passed", display = Map("en-US" -> "passed")),
//        obj = Activity("http://adlnet.gov/expapi/activity/act4")
//      ),
//      stored = DateTime.now,
//      timestamp = DateTime.now
//    )
//    lrs.addStatement(statement)
//    lrs.addStatement(statement2)
//    lrs.addStatement(statement3)
//    var statements = lrs.findStatements(new StatementQuery(since = Option(DateTime.now.minusDays(2))))
//    assert(1 == statements.seq.size)
//    assert(statements.seq.head.id == statement3.id)
//
//    statements = lrs.findStatements(new StatementQuery(since = Option(DateTime.now.minusDays(4))))
//    assert(2 == statements.seq.size)
//
//    statements = lrs.findStatements(new StatementQuery(until = Option(DateTime.now.minusDays(4))))
//    assert(1 == statements.seq.size)
//    assert(statements.seq.head.id == statement.id)
//
//    statements = lrs.findStatements(new StatementQuery(until = Option(DateTime.now.minusDays(2))))
//    assert(2 == statements.seq.size)
//  }
//
//  test("statement filters: limit") {
//    val id = UUID.randomUUID()
//    val idReg = UUID.randomUUID()
//    val statement = Statement(
//      id = Option(id),
//      actor = Agent(Option("Test Test"), Option("mailto:test@test.com")),
//      verb = Verb("http://adlnet.gov/expapi/verbs/answered", display = Map("en-US" -> "test")),
//      obj = Activity("http://adlnet.gov/expapi/activity/act1"),
//      stored = DateTime.now,
//      timestamp = DateTime.now.minusDays(5),
//      context = Option(Context(
//        registration = Option(idReg),
//        instructor = Option(Agent(Option("Test Test"), Option("mailto:test3@test.com")))
//      ))
//    )
//    val id2 = UUID.randomUUID()
//    val statement2 = Statement(
//      id = Option(id2),
//      actor = Agent(Option("Test Test"), Option("mailto:test1@test.com")),
//      verb = Verb("http://adlnet.gov/expapi/verbs/passed", display = Map("en-US" -> "passed")),
//      obj = Activity("http://adlnet.gov/expapi/activity/act2"),
//      stored = DateTime.now,
//      timestamp = DateTime.now.minusDays(3),
//      context = Option(Context(
//        registration = Option(idReg),
//        team = Option(Group(Option("Test Test"), Option(Seq(Agent(Option("Test Test"), Option("mailto:test4@test.com")))), Option("mailto:test5@test.com")))
//      ))
//    )
//    val id3 = UUID.randomUUID()
//    val statement3 = Statement(
//      id = Option(id3),
//      actor = Agent(Option("Test Test"), Option("mailto:test2@test.com")),
//      verb = Verb("http://adlnet.gov/expapi/verbs/passed", display = Map("en-US" -> "passed")),
//      obj = SubStatement(
//        actor = Agent(Option("Test Test"), Option("mailto:test6@test.com")),
//        verb = Verb("http://adlnet.gov/expapi/verbs/passed", display = Map("en-US" -> "passed")),
//        obj = Activity("http://adlnet.gov/expapi/activity/act4")
//      ),
//      stored = DateTime.now,
//      timestamp = DateTime.now
//    )
//    lrs.addStatement(statement)
//    lrs.addStatement(statement2)
//    lrs.addStatement(statement3)
//    var statements = lrs.findStatements(new StatementQuery(limit = 1))
//    assert(1 == statements.seq.size)
//    assert(statements.seq.head.id == statement3.id)
//
//    statements = lrs.findStatements(new StatementQuery(limit = 2))
//    assert(2 == statements.seq.size)
//
//  }
//
//  test("statement filters: format") {}
//
//  test("statement filters: attachments") {
//    val id = UUID.randomUUID()
//    val idReg = UUID.randomUUID()
//    val statement = Statement(
//      id = Option(id),
//      actor = Agent(Option("Test Test"), Option("mailto:test@test.com")),
//      verb = Verb("http://adlnet.gov/expapi/verbs/answered", display = Map("en-US" -> "test")),
//      obj = Activity("http://adlnet.gov/expapi/activity/act1"),
//      stored = DateTime.now,
//      timestamp = DateTime.now.minusDays(5),
//      context = Option(Context(
//        registration = Option(idReg),
//        instructor = Option(Agent(Option("Test Test"), Option("mailto:test3@test.com")))
//      )),
//      attachments = Seq(Attachment("att1", Map("fi" -> "asdasd"), Option(Map("fi" -> "asdasd")), "json", 100, "asdasdasdsdas"))
//    )
//    val id2 = UUID.randomUUID()
//    val statement2 = Statement(
//      id = Option(id2),
//      actor = Agent(Option("Test Test"), Option("mailto:test1@test.com")),
//      verb = Verb("http://adlnet.gov/expapi/verbs/passed", display = Map("en-US" -> "passed")),
//      obj = Activity("http://adlnet.gov/expapi/activity/act2"),
//      stored = DateTime.now,
//      timestamp = DateTime.now.minusDays(3),
//      context = Option(Context(
//        registration = Option(idReg),
//        team = Option(Group(Option("Test Test"), Option(Seq(Agent(Option("Test Test"), Option("mailto:test4@test.com")))), Option("mailto:test5@test.com")))
//      ))
//    )
//    val id3 = UUID.randomUUID()
//    val statement3 = Statement(
//      id = Option(id3),
//      actor = Agent(Option("Test Test"), Option("mailto:test2@test.com")),
//      verb = Verb("http://adlnet.gov/expapi/verbs/passed", display = Map("en-US" -> "passed")),
//      obj = SubStatement(
//        actor = Agent(Option("Test Test"), Option("mailto:test6@test.com")),
//        verb = Verb("http://adlnet.gov/expapi/verbs/passed", display = Map("en-US" -> "passed")),
//        obj = Activity("http://adlnet.gov/expapi/activity/act4")
//      ),
//      stored = DateTime.now,
//      timestamp = DateTime.now
//    )
//    lrs.addStatement(statement)
//    lrs.addStatement(statement2)
//    lrs.addStatement(statement3)
//    var statements = lrs.findStatements(new StatementQuery)
//    assert(3 == statements.seq.size)
//    assert(!statements.seq.exists(s => s.attachments.nonEmpty))
//
//    statements = lrs.findStatements(new StatementQuery(attachments = true))
//    assert(3 == statements.seq.size)
//    assert(statements.seq.exists(s => s.attachments.nonEmpty))
//
//  }
//
//  test("statement filters: ascending") {
//    val id = UUID.randomUUID()
//    val idReg = UUID.randomUUID()
//    val statement = Statement(
//      id = Option(id),
//      actor = Agent(Option("Test Test"), Option("mailto:test@test.com")),
//      verb = Verb("http://adlnet.gov/expapi/verbs/answered", display = Map("en-US" -> "test")),
//      obj = Activity("http://adlnet.gov/expapi/activity/act1"),
//      stored = DateTime.now.minusDays(5),
//      timestamp = DateTime.now.minusDays(5),
//      context = Option(Context(
//        registration = Option(idReg),
//        instructor = Option(Agent(Option("Test Test"), Option("mailto:test3@test.com")))
//      )),
//      attachments = Seq(Attachment("att1", Map("fi" -> "asdasd"), Option(Map("fi" -> "asdasd")), "json", 100, "asdasdasdsdas"))
//    )
//    val id2 = UUID.randomUUID()
//    val statement2 = Statement(
//      id = Option(id2),
//      actor = Agent(Option("Test Test"), Option("mailto:test1@test.com")),
//      verb = Verb("http://adlnet.gov/expapi/verbs/passed", display = Map("en-US" -> "passed")),
//      obj = Activity("http://adlnet.gov/expapi/activity/act2"),
//      stored = DateTime.now.minusDays(3),
//      timestamp = DateTime.now.minusDays(3),
//      context = Option(Context(
//        registration = Option(idReg),
//        team = Option(Group(Option("Test Test"), Option(Seq(Agent(Option("Test Test"), Option("mailto:test4@test.com")))), Option("mailto:test5@test.com")))
//      ))
//    )
//    val id3 = UUID.randomUUID()
//    val statement3 = Statement(
//      id = Option(id3),
//      actor = Agent(Option("Test Test"), Option("mailto:test2@test.com")),
//      verb = Verb("http://adlnet.gov/expapi/verbs/passed", display = Map("en-US" -> "passed")),
//      obj = SubStatement(
//        actor = Agent(Option("Test Test"), Option("mailto:test6@test.com")),
//        verb = Verb("http://adlnet.gov/expapi/verbs/passed", display = Map("en-US" -> "passed")),
//        obj = Activity("http://adlnet.gov/expapi/activity/act4")
//      ),
//      stored = DateTime.now,
//      timestamp = DateTime.now
//    )
//    lrs.addStatement(statement)
//    lrs.addStatement(statement2)
//    lrs.addStatement(statement3)
//    var statements = lrs.findStatements(new StatementQuery)
//    assert(3 == statements.seq.size)
//    assert(statements.seq.head.id == statement3.id)
//
//    statements = lrs.findStatements(new StatementQuery(ascending = true))
//    assert(3 == statements.seq.size)
//    assert(statements.seq.head.id == statement.id)
//  }
//
//  test("activity profile") {
//    val doc1 = Document(updated = DateTime.now.minusDays(10), contents = "content", cType = ContentType.json)
//    lrs.addOrUpdateActivityProfile("http://adlnet.gov/expapi/activity/act1", "profile1", doc1)
//
//    val doc2 = Document(updated = DateTime.now.minusDays(5), contents = "content", cType = ContentType.json)
//    lrs.addOrUpdateActivityProfile("http://adlnet.gov/expapi/activity/act1", "profile2", doc2)
//    val doc3 = Document(updated = DateTime.now, contents = "content", cType = ContentType.json)
//    lrs.addOrUpdateActivityProfile("http://adlnet.gov/expapi/activity/act1", "profile1", doc3)
//
//    val profile = lrs.getActivityProfile("http://adlnet.gov/expapi/activity/act1", "profile1")
//    assert(profile.isDefined)
//    assert(profile.get.id == doc1.id)
//
//    var profiles = lrs.getActivityProfileIds("http://adlnet.gov/expapi/activity/act1", Option(DateTime.now.minusDays(1)))
//    assert(profiles.size == 1)
//    profiles = lrs.getActivityProfileIds("http://adlnet.gov/expapi/activity/act1", None)
//    assert(profiles.size == 2)
//
//    lrs.deleteActivityProfile("http://adlnet.gov/expapi/activity/act1", "profile1")
//
//    profiles = lrs.getActivityProfileIds("http://adlnet.gov/expapi/activity/act1", None)
//    assert(profiles.size == 1)
//    assert(profiles.head == "profile2")
//  }
//
//  test("agent profile") {
//    val agent = Agent(Option("Test Test"), mBox = Option("mailto:test@test.com"))
//    val doc1 = Document(updated = DateTime.now.minusDays(10), contents = "content", cType = ContentType.json)
//    lrs.addOrUpdateAgentProfile(agent, "profile1", doc1)
//    val doc2 = Document(updated = DateTime.now.minusDays(5), contents = "content", cType = ContentType.json)
//    lrs.addOrUpdateAgentProfile(agent, "profile2", doc2)
//    val doc3 = Document(updated = DateTime.now, contents = "content", cType = ContentType.json)
//    lrs.addOrUpdateAgentProfile(agent, "profile1", doc3)
//
//    val profile = lrs.getAgentProfile(agent, "profile1")
//    assert(profile.isDefined)
//    assert(profile.get.id == doc1.id)
//
//    var profiles = lrs.getAgentProfiles(agent, Option(DateTime.now.minusDays(1)))
//    assert(profiles.size == 1)
//    profiles = lrs.getAgentProfiles(agent, None)
//    assert(profiles.size == 2)
//
//    lrs.deleteAgentProfile(agent, "profile1")
//
//    profiles = lrs.getAgentProfiles(agent, None)
//    assert(profiles.size == 1)
//    assert(profiles.head == "profile2")
//  }
//
//  test("state profile") {
//    val agent = Agent(Option("Test Test"), mBox = Option("mailto:test@test.com"))
//    val registration = Option(UUID.randomUUID())
//    val doc1 = Document(updated = DateTime.now.minusDays(10), contents = "content", cType = ContentType.json)
//    lrs.addOrUpdateStateProfile(agent, "http://adlnet.gov/expapi/activity/act1", "profile1", None, doc1)
//    val doc2 = Document(updated = DateTime.now.minusDays(5), contents = "content", cType = ContentType.json)
//    lrs.addOrUpdateStateProfile(agent, "http://adlnet.gov/expapi/activity/act1", "profile2", registration, doc2)
//    val doc3 = Document(updated = DateTime.now, contents = "content", cType = ContentType.json)
//    lrs.addOrUpdateStateProfile(agent, "http://adlnet.gov/expapi/activity/act1", "profile1", None, doc3)
//
//    val profile = lrs.getStateProfile(agent, "http://adlnet.gov/expapi/activity/act1", "profile1", None)
//    assert(profile.isDefined)
//    assert(profile.get.id == doc1.id)
//
//    var profiles = lrs.getStateProfiles(agent, "http://adlnet.gov/expapi/activity/act1", None, Option(DateTime.now.minusDays(1)))
//    assert(profiles.size == 1)
//    profiles = lrs.getStateProfiles(agent, "http://adlnet.gov/expapi/activity/act1", None, None)
//    assert(profiles.size == 1)
//    assert(profiles.head == "profile1")
//    profiles = lrs.getStateProfiles(agent, "http://adlnet.gov/expapi/activity/act1", registration, None)
//    assert(profiles.size == 1)
//    assert(profiles.head == "profile2")
//
//    lrs.deleteStateProfile(agent, "http://adlnet.gov/expapi/activity/act1", "profile1", None)
//
//    profiles = lrs.getStateProfiles(agent, "http://adlnet.gov/expapi/activity/act1", None, None)
//    assert(profiles.isEmpty)
//
//    lrs.deleteStateProfiles(agent, "http://adlnet.gov/expapi/activity/act1", registration)
//
//    profiles = lrs.getStateProfiles(agent, "http://adlnet.gov/expapi/activity/act1", registration, None)
//    assert(profiles.isEmpty)
//  }
//}