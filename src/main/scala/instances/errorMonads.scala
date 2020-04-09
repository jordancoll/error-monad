package instances

import classes._
import data._
import com.twitter.util.Future

given ErrorMonad[Either] {

  override def pure[L, R](a: R): Either[L, R] = Right(a)

  override def [L, LL, R, RR](either: Either[L, R])
      flatMap(f: R => Either[LL, RR]): Either[L | LL, RR] = {

    either match {
      case Right(v) => f(v)
      case Left(e) => Left(e)
    }
  }

  override def raiseError[E, A](e: E): Either[E, A] = Left(e)

  override def [E, A](fa: Either[E, A]) handleErrors(f: E => A): Either[Nothing, A] = {
    fa match {
      case Right(v) => Right(v)
      case Left(e) => Right(f(e))
    }
  }
}

given (using m: Monad[Future]) as ErrorMonad[Outcome] {

  override def pure[L, R](a: R): Outcome[L, R] = Outcome.value(a)

  override def [L, LL, R, RR](outcome: Outcome[L, R])
      flatMap(f: R => Outcome[LL, RR]): Outcome[L | LL, RR] = {

    val either = m.flatMap(outcome.value) {
      case Right(v) => f(v).value
      case Left(e) => m.pure(Left(e))
    }
    EitherT(either)
  }

  override def raiseError[E, A](e: E): Outcome[E, A] = Outcome.error(e)

  override def [E, A](fa: Outcome[E, A]) handleErrors(f: E => A): Outcome[Nothing, A] = {
    val either: Future[Either[Nothing, A]] = fa.value.map(_.handleErrors(f))
    EitherT(either)
  }
}
