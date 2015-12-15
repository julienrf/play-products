package julienrf.products

import play.api.libs.functional.{InvariantFunctorExtractor, ContravariantFunctorExtractor, FunctorExtractor, VariantExtractor}
import shapeless.{Generic, HList}

import scala.annotation.implicitNotFound

import scala.language.higherKinds

@implicitNotFound("Unable to match arguments with the target constructor parameters. Check that they are in the same order;\n arguments  : ${L}\n target type: ${A}\n")
trait MappedAs[L <: HList, A] {
  def apply[F[_]](fl: F[L])(implicit ve: VariantExtractor[F]): F[A]
}

object MappedAs {

  implicit def generic[A, L <: HList](implicit gen: Generic.Aux[A, L]): MappedAs[L, A] =
    new MappedAs[L, A] {
      def apply[F[_]](fl: F[L])(implicit ve: VariantExtractor[F]) =
        ve match {
          case FunctorExtractor(functor) => functor.fmap(fl, gen.from)
          case ContravariantFunctorExtractor(contravariant) => contravariant.contramap(fl, gen.to)
          case InvariantFunctorExtractor(invariant) => invariant.inmap(fl, gen.from, gen.to)
        }
    }

}