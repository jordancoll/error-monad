package instances

import classes._
import data._
import com.twitter.util.Future

given ErrorMonad[Outcome] {

  override def pure[L, R](a: R): Outcome[L, R] = Right(a)

  override def [L, LL, R, RR](outcome: Outcome[L, R]) 
      flatMap(f: R => Outcome[LL, RR]): Outcome[L | LL, RR] = {
    outcome match {
      case Right(v) => f(v)
      case Left(e) => Left(e)
    }
  }

  override def raiseError[E, A](e: E): Outcome[E, A] = Left(e)

  override def [E, A](fa: Outcome[E, A]) handleErrors(f: E => A): Outcome[Nothing, A] = {
    fa match {
      case Right(v) => Right(v)
      case Left(e) => Right(f(e))
    }
  }
}

given (given m: Monad[Future]): ErrorMonad[OutcomeF] {
  
  override def pure[L, R](a: R): OutcomeF[L, R] = OutcomeF.value(a)
  
  override def [L, LL, R, RR](outcomeF: OutcomeF[L, R]) 
      flatMap(f: R => OutcomeF[LL, RR]): OutcomeF[L | LL, RR] = {

    val either = m.flatMap(outcomeF.value) {
      case Right(v) => f(v).value
      case Left(e) => m.pure(Left(e))
    }
    EitherT(either)
  }

  override def raiseError[E, A](e: E): OutcomeF[E, A] = OutcomeF.error(e)

  override def [E, A](fa: OutcomeF[E, A]) handleErrors(f: E => A): OutcomeF[Nothing, A] = {
    val either: Future[Either[Nothing, A]] = fa.value.map(_.handleErrors(f))
    EitherT(either)
  }
}