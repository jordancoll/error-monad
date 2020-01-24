package classes

trait Monad[F[_]] {
  
  def pure[A](a: A): F[A]

  def [A, B](fa: F[A]) flatMap(f: A => F[B]): F[B]

  def [A, B](fa: F[A]) map(f: A => B): F[B] = {
    flatMap(fa)(a => pure(f(a)))
  }
}
