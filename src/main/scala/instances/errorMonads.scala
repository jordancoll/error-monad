package instances

import classes._
import data._
import com.twitter.util.Future

given eithetTErrorMonad[F[_]](using m: Monad[F]) as ErrorMonad[[L, R] =>> EitherT[F, L, R]] {

  override def pure[L, R](a: R): EitherT[F, L, R] = {
    val e: Either[L, R] = Right(a)
    EitherT(m.pure(e))
  }

  override def [L, LL, R, RR](eitherT: EitherT[F, L, R])
     flatMap(f: R => EitherT[F, LL, RR]): EitherT[F, L | LL, RR] = {
    val either = m.flatMap(eitherT.value) {
      case Right(v) => f(v).value.map(either => either: Either[L | LL, RR])
      case Left(e) => m.pure(Left(e: L | LL)): F[Either[L | LL, RR]]
    }
    EitherT(either)
  }

  def raiseError[E, A](e: E): EitherT[F, E, A] = {
    EitherT(m.pure(Left(e)))
  }

  override def [E, A](fa: EitherT[F, E, A]) handleErrors(f: E => A): EitherT[F, Nothing, A] = {
    val eitherF: F[Either[Nothing, A]] = fa.value.map {
      case Right(a) => Right(a)
      case Left(e) => Right(f(e))
    }
    EitherT(eitherF)
  }
}


// Although compiller can derrive `implicitly[ErrorMonad[[L, R] =>> EitherT[Future, L, R]]]`
// for comprehension is not going to work without the following workaround:
given outcomeErrorMonad as ErrorMonad[[L, R] =>> EitherT[Future, L, R]] = eithetTErrorMonad
