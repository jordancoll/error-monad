package data

import com.twitter.util.Future

type Outcome[E, T] = Either[E, T]

type OutcomeF[E, T] = EitherT[Future, E, T]

object OutcomeF {

  def apply[E, A](either: Either[E, A]): OutcomeF[E, A] = EitherT(Future.value(either))

  def value[A](a: A): OutcomeF[Nothing, A] = apply(Right(a))

  def error[E](e: E): OutcomeF[E, Nothing] = apply(Left(e))
}
