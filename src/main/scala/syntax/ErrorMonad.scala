package syntax

import classes.ErrorMonad

extension [F[_, _]: ErrorMonad, EA, A](fa: F[EA, A])

  def flatMap[EB, B](f: A => F[EB, B]): F[EA | EB, B] =
    summon[ErrorMonad[F]].flatMap(fa)(f)
  
  def handleErrors(f: EA => A): F[Nothing, A] =
    summon[ErrorMonad[F]].handleErrors(fa)(f)