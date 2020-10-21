package classes

trait ErrorSemigroupal[F[_, _]] {

  extension [EA, EB, A, B](fa: F[EA, A])
    def product(fb: F[EB, B]): F[EA | EB, (A, B)]

}
