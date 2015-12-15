package julienrf.products

import org.scalatest.FunSuite
import play.api.libs.functional.{InvariantFunctor, ~, FunctionalCanBuild}

class ProductsSuite extends FunSuite {

  case class Id[A](a: A)

  object Id {
    implicit val idFcb: FunctionalCanBuild[Id] =
      new FunctionalCanBuild[Id] {
        def apply[A, B](ma: Id[A], mb: Id[B]): Id[A ~ B] = Id(new ~(ma.a, mb.a))
      }
    implicit val invariant: InvariantFunctor[Id] =
      new InvariantFunctor[Id] {
        def inmap[A, B](m: Id[A], f1: (A) => B, f2: (B) => A) = Id(f1(m.a))
      }
  }

  case class User(name: String, age: Int)

  import syntax._
  import play.api.libs.json.{__, Json, JsSuccess}

  test("push several values within a context") {

    assert((Id("foo") :*: Id(42)).tupled == Id(("foo", 42)))
    assert((Id("foo") :*: Id(42)).as[User] == Id(User("foo", 42)))
    assert((Id("foo") :*: Id(42)).inmap(User.apply _, Function.unlift(User.unapply _)) == Id(User("foo", 42)))

  }

  test("play-json integration") {

    val userReads =
      (
        (__ \ "name").read[String] :*:
        (__ \ "age").read[Int]
      ).as[User]

    assert(userReads.reads(Json.obj("name" -> "foo", "age" -> 42)) == JsSuccess(User("foo", 42)))

    val reads2 =
      ((__ \ "foo").read[Int] :*: (__ \ "bar").read[String]).map((i: Int, s: String) => (i, s))

  }
}
