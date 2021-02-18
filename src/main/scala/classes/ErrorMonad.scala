package classes

trait ErrorMonad[F[_, _]] extends ErrorApplicative[F]:

  def pure[E, A](a: A): F[E, A]

  extension [EA, A](fa: F[EA, A])
    def flatMap[EB, B](f: A => F[EB, B]): F[EA | EB, B]

    override def ap[EB, B](ff: F[EB, A => B]): F[EA | EB, B] =
      fa.flatMap { a =>
        ff.map(f => f(a))
      }

    def handleErrors(f: EA => A): F[Nothing, A]

  extension [E, A](fa: F[E, A])
    override def map[B](f: A => B): F[E, B] =
      fa.flatMap(a => pure(f(a)))

  // Extra method for error handling 
  // inspired by https://typelevel.org/blog/2018/04/13/rethinking-monaderror.html
  def raiseError[E, A](e: E) : F[E, A]

