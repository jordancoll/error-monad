package classes 

trait ErrorFunctor[F[_, _]] {

  extension [E, A, B](fa: F[E, A])
    def map(f: A => B): F[E, B]

}
