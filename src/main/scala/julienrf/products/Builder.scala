package julienrf.products

import play.api.libs.functional.{InvariantFunctor, ContravariantFunctor, ContravariantFunctorExtractor, FunctionalCanBuild, Functor, FunctorExtractor, InvariantFunctorExtractor, VariantExtractor, ~}
import shapeless.ops.function.FnToProduct
import shapeless.ops.hlist.Tupler
import shapeless.{Generic, ::, HList}

import scala.language.higherKinds

class Builder[F[_], L <: HList](fl: F[L])(implicit
  fcb: FunctionalCanBuild[F],
  ve: VariantExtractor[F]
) {

  def :*: [H](fh: F[H]): Builder[F, H :: L] = {
    val fhl =
      ve match {
        case FunctorExtractor(functor) => functor.fmap[H ~ L, H :: L](fcb(fh, fl), { case h ~ t => h :: t })
        case ContravariantFunctorExtractor(contravariant) => contravariant.contramap[H ~ L, H :: L](fcb(fh, fl), ht => new ~(ht.head, ht.tail))
        case InvariantFunctorExtractor(invariant) => invariant.inmap[H ~ L, H :: L](fcb(fh, fl), { case h ~ t => h :: t }, ht => new ~(ht.head, ht.tail))
      }
    new Builder(fhl)
  }

  def as[A](implicit ma: MappedAs[L, A]): F[A] = ma(fl)

  def tupled[Tuple](implicit
    tupler: Tupler.Aux[L, Tuple],
    ma: MappedAs[L, Tuple]
  ): F[Tuple] = ma(fl)

  def map[A, Fun](f: Fun)(implicit
    functor: Functor[F],
    fn: FnToProduct.Aux[Fun, L => A]
  ): F[A] =
    functor.fmap(fl, fn(f))

  def contramap[A, Tuple](f: A => Tuple)(implicit
    contravariant: ContravariantFunctor[F],
    gen: Generic.Aux[Tuple, L]
  ): F[A] =
    contravariant.contramap(fl, f.andThen(gen.to))

  def inmap[A, Tuple, Fun](f: Fun, g: A => Tuple)(implicit
    invariant: InvariantFunctor[F],
    fn: FnToProduct.Aux[Fun, L => A],
    gen: Generic.Aux[Tuple, L]
  ): F[A] =
    invariant.inmap(fl, fn(f), g.andThen(gen.to))

}
