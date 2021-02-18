package classes 

trait ErrorFunctor[F[_, _]]:

  extension [E, A](fa: F[E, A])
    def map[B](f: A => B): F[E, B]

