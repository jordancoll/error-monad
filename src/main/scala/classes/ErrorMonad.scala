package classes

trait ErrorMonad[F[_, _]] extends ErrorApplicative[F] {

  def pure[E, A](a: A): F[E, A]

  extension [EA, EB, A, B](fa: F[EA, A]) {
    def flatMap(f: A => F[EB, B]): F[EA | EB, B]

    override def ap(ff: F[EB, A => B]): F[EA | EB, B] = {
      fa.flatMap { a =>
        ff.map(f => f(a))
      }
    }

    def handleErrors(f: EA => A): F[Nothing, A]
  }

  extension [E, A, B](fa: F[E, A])
    override def map(f: A => B): F[E, B] = {
      fa.flatMap(a => pure(f(a)))
    }

  // Extra method for error handling 
  // inspired by https://typelevel.org/blog/2018/04/13/rethinking-monaderror.html
  def raiseError[E, A](e: E) : F[E, A]

}
