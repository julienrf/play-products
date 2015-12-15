package julienrf.products

import play.api.libs.functional.{Applicative, FunctionalCanBuild, InvariantFunctorExtractor, ContravariantFunctorExtractor, FunctorExtractor, VariantExtractor}
import shapeless.{HNil, ::}

import scala.language.implicitConversions
import scala.language.higherKinds

package object syntax {

  implicit def toProductBuilder[F[_], A](fa: F[A])(implicit
    fcb: FunctionalCanBuild[F],
    ve: VariantExtractor[F]
  ): Builder[F, A :: HNil] = {
    val fahnil: F[A :: HNil] = ve match {
      case FunctorExtractor(functor) => functor.fmap[A, A :: HNil](fa, _ :: HNil)
      case ContravariantFunctorExtractor(contravariant) => contravariant.contramap[A, A :: HNil](fa, _.head)
      case InvariantFunctorExtractor(invariant) => invariant.inmap[A, A :: HNil](fa, _ :: HNil, _.head)
    }
    new Builder[F, A :: HNil](fahnil)
  }

  // Workaround https://github.com/playframework/playframework/pull/5321
  implicit def functionalCanBuildApplicative[M[_]](implicit app : Applicative[M]) : FunctionalCanBuild[M] =
    play.api.libs.functional.syntax.functionalCanBuildApplicative[M]

}
