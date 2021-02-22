package classes

trait ErrorMonad[F[_, _]] extends ErrorApplicative[F]:

  def pure[E, A](a: A): F[E, A]

  def flatMap[EA, A, EB, B](fa: F[EA, A])(f: A => F[EB, B]): F[EA | EB, B]

  def handleErrors[EA, A](fa: F[EA, A])(f: EA => A): F[Nothing, A]

  override def ap[EA, A, EB, B](fa: F[EA, A])(ff: F[EB, A => B]): F[EA | EB, B] =
    flatMap(fa)(a => map(ff)(f => f(a)))

  override def map[EA, A, B](fa: F[EA, A])(f: A => B): F[EA, B] =
    flatMap(fa)(a => pure(f(a)))

  // Extra method for error handling 
  // inspired by https://typelevel.org/blog/2018/04/13/rethinking-monaderror.html
  def raiseError[E, A](e: E) : F[E, A]
