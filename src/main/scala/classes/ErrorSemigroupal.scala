package classes

trait ErrorSemigroupal[F[_, _]]:

  def product[EA, A, EB, B](fa: F[EA, A], fb: F[EB, B]): F[EA | EB, (A, B)]
