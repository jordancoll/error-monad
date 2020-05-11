package data

import com.twitter.util.Future

type Outcome[E, T] = EitherT[Future, E, T]

object Outcome {

  def apply[E, A](either: Either[E, A]): Outcome[E, A] = EitherT(Future.value(either))

  def value[A](a: A): Outcome[Nothing, A] = apply(Right(a))

  def error[E, T](e: E): Outcome[E, T] = apply(Left(e))
}
