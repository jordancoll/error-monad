package classes

trait ErrorSemigroupal[F[_, _]] {

  def [EA, EB, A, B](fa: F[EA, A]) product(fb: F[EB, B]): F[EA | EB, (A, B)]
}
