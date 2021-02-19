package classes 

trait ErrorApplicative[F[_, _]] extends ErrorSemigroupal[F] with ErrorFunctor[F]:

  def pure[E, A](a: A): F[E, A]

  def ap[EA, A, EB, B](fa: F[EA, A])(ff: F[EB, A => B]): F[EA | EB, B]

  override def product[EA, A, EB, B](fa: F[EA, A], fb: F[EB, B]): F[EA | EB, (A, B)] =
    val ff: F[EA, B => (A, B)] = map(fa)(a => (a, _))
    ap(fb)(ff)

  override def map[EA, A, B](fa: F[EA, A])(f: A => B): F[EA, B] = ap(fa)(pure(f))

