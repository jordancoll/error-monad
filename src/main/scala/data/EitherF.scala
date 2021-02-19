package data

import com.twitter.util.Future

type EitherF[E, T] = EitherT[Future, E, T]

object EitherF:

  def apply[E, A](either: Either[E, A]): EitherF[E, A] = EitherT(Future.value(either))

  def value[E, A](a: A): EitherF[E, A] = apply(Right(a))

  def error[E, A](e: E): EitherF[E, A] = apply(Left(e))
