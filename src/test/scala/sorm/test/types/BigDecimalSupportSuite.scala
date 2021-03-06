package sorm.test.types

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.scalatest.matchers.ShouldMatchers
import sorm._
import sorm.test.MultiInstanceSuite

object BigDecimalSupportSuite {
  case class A ( a : BigDecimal ) extends Persistable
}

@RunWith(classOf[JUnitRunner])
class BigDecimalSupportSuite extends FunSuite with ShouldMatchers with MultiInstanceSuite {
  import BigDecimalSupportSuite._

  def entities = Set(Entity[A]())
  instancesAndIds foreach { case (db, dbId) =>
    val seq : Seq[BigDecimal] = Seq(2, 2.230192321, 3.3209483290840923839230, 0.213)
    seq.foreach(v => db.save(A(v)))
    test(dbId + " - fetching"){
      db.query[A].order("_id").fetch().map(_.a)
        .should(equal(seq))
    }
    test(dbId + " - filtering"){
      db.query[A].whereLarger("a", 2).fetch()
        .should(have('size(seq.count(_ > 2))))
    }
  }
}
