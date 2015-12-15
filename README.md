# play-products [![Maven Central](https://img.shields.io/maven-central/v/org.julienrf/play-products_2.11.svg)](https://maven-badges.herokuapp.com/maven-central/org.julienrf/play-products_2.11)

A convenient syntactic sugar to combine monoidal structures (e.g. `Reads`, `OWrites`, `OFormat`), powered by shapeless.

Consider the following definition:

~~~ scala
case class User(name: String, age: Int)
~~~

With `play-products` you can define a JSON codec for it as follows:

~~~ scala
import julienrf.products.syntax._
import play.api.libs.json.{__, Reads}

val userReads: OFormat[User] =
 (
   (__ \ "name").format[String] :*:
   (__ \ "age").format[Int]
 ).as[User]
~~~

Instead of the current syntax supported out of the box by Play:

~~~ scala
import play.api.libs.functional.syntax._
import play.api.libs.json.{__, Reads}

val userReads: OFormat[User] =
 (
   (__ \ "name").format[String] ~
   (__ \ "age").format[Int]
 )(User.apply _, unlift(User.unapply _))
~~~

## Features

- use `.as[Qux]` to map values into a `Qux` (where `Qux` is a case class) ;
- use `.tupled` to map values into a tuple ;
- use `.map(f)`, `.contramap(f)` or `.inmap(f, g)` to map values using the supplied function(s) ;
- combine as many values as you want (you are not limited to 22, as in Play) ;
- works with any type constructor `F[_]` as long as there are instances of `FunctionalCanBuild[F]` and `Variant[F]`.

## License

This content is released under the [MIT License](http://opensource.org/licenses/mit-license.php).
