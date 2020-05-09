package instances

import classes._
import data._
import com.twitter.util.Future

def eithetTErrorMonad[F[+_]](using m: Monad[F]) = new ErrorMonad[[L, R] =>> EitherT[F, L, R]] {

  override def pure[L, R](a: R): EitherT[F, L, R] = {
    val e: Either[L, R] = Right(a)
    EitherT(m.pure(e))
  }

  override def [L, LL, R, RR](eitherT: EitherT[F, L, R])
     flatMap(f: R => EitherT[F, LL, RR]): EitherT[F, L | LL, RR] = {
    val either = m.flatMap(eitherT.value) {
      case Right(v) => f(v).value
      case Left(e) => m.pure(Left(e))
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

given outcomeErrorMonad as ErrorMonad[Outcome] = eithetTErrorMonad
