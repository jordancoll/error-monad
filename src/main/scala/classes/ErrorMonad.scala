package classes

trait ErrorMonad[F[_, _]] extends ErrorApplicative[F] {

  def pure[E, A](a: A): F[E, A]

  def [EA, EB, A, B](fa: F[EA, A]) flatMap(f: A => F[EB, B]): F[EA | EB, B]

  override def [E, A, B](fa: F[E, A]) map(f: A => B): F[E, B] = {
    fa.flatMap(a => pure(f(a)))
  }

  override def [EA, EB, A, B](fa: F[EA, A]) ap(ff: F[EB, A => B]): F[EA | EB, B] = {
    fa.flatMap { a =>
      ff.map(f => f(a))
    }
  }

  // Extra methods for error handling 
  // inspired by https://typelevel.org/blog/2018/04/13/rethinking-monaderror.html

  def raiseError[E, A](e: E) : F[E, A]

  def [E, A](fa: F[E, A]) handleErrors(f: E => A): F[Nothing, A]
}
