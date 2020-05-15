package data

import com.twitter.util.Future

type Outcome[E, T] = EitherT[Future, E, T]

object Outcome {

  def apply[E, A](either: Either[E, A]): Outcome[E, A] = EitherT(Future.value(either))

  def value[E, A](a: A): Outcome[E, A] = apply(Right(a))

  def error[E, A](e: E): Outcome[E, A] = apply(Left(e))
}
