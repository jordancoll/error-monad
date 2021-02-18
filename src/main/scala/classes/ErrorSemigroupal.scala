package classes

trait ErrorSemigroupal[F[_, _]]:

  extension [EA, A](fa: F[EA, A])
    def product[EB, B](fb: F[EB, B]): F[EA | EB, (A, B)]

