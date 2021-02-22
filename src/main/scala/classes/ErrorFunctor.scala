package classes 

trait ErrorFunctor[F[_, _]]:

  def map[E, A, B](fa: F[E, A])(f: A => B): F[E, B]


