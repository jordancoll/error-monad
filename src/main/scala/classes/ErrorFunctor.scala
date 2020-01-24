package classes 

trait ErrorFunctor[F[_, _]] {
  
  def [E, A, B](fa: F[E, A]) map(f: A => B): F[E, B]
}
