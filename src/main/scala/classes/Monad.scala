package classes

trait Monad[F[_]] {
  
  def pure[A](a: A): F[A]

  extension [A, B](fa: F[A]) {
    def flatMap(f: A => F[B]): F[B]

    def map(f: A => B): F[B] = {
      fa.flatMap(a => pure(f(a)))
    }
  }

}
