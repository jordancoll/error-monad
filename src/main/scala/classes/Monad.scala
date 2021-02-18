package classes

trait Monad[F[_]]:
  
  def pure[A](a: A): F[A]

  extension [A](fa: F[A])

    def flatMap[B](f: A => F[B]): F[B]

    def map[B](f: A => B): F[B] = fa.flatMap(a => pure(f(a)))
