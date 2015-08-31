package sorm.test.types

import org.scalatest.{SequentialNestedSuiteExecution, FunSuite}
import org.scalatest.matchers.ShouldMatchers
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import sorm._
import sext._, embrace._
import sorm.test.MultiInstanceSuite

@RunWith(classOf[JUnitRunner])
class EnumSupportSuite extends FunSuite with ShouldMatchers with MultiInstanceSuite {
  import EnumSupportSuite._

  def entities = Set() + Entity[A]()
  instancesAndIds foreach { case (db, dbId) =>
    val a1 = db.save(A(None, B.One))
    val a2 = db.save(A(None, B.Two))
    val a3 = db.save(A(None, B.Three))
    val a4 = db.save(A(None, B.Two))
    test(dbId + " - Equality query"){
      db.query[A].whereEqual("a", B.Two).fetch()
        .should(
          have size (2) and
          contain (a2)
        )
    }
    test(dbId + " - Not equal query"){
      db.query[A].whereNotEqual("a", B.Two).fetch()
        .should(
          contain (a1) and
          contain (a3)
        )
    }
  }

}
object EnumSupportSuite {
  case class A ( var id: Option[Long], a : B.Value ) extends Persistable
  object B extends Enumeration {
    val One, Two, Three = Value
  }


}
