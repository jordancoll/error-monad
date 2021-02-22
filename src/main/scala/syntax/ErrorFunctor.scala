package syntax

import classes.ErrorFunctor

extension [F[_, _]: ErrorFunctor, EA, A](fa: F[EA, A])

  def map[B](f: A => B): F[EA, B] =
      summon[ErrorFunctor[F]].map(fa)(f)
