package instances

import classes._
import data._
import com.twitter.util.Future

given eitherTErrorMonad[F[_]](using m: Monad[F]) as ErrorMonad[[L, R] =>> EitherT[F, L, R]] {

  override def pure[L, R](a: R): EitherT[F, L, R] = {
    val either: Either[L, R] = Right(a)
    EitherT(m.pure(either))
  }

  extension [EA, EB, A, B](fa: EitherT[F, EA, A]) {
    override def flatMap(f: A => EitherT[F, EB, B]): EitherT[F, EA | EB, B] = {
      val either = fa.value.flatMap {
        case Right(v) => f(v).value.map(either => either: Either[EA | EB, B])
        case Left(e) => m.pure(Left(e: EA | EB)): F[Either[EA | EB, B]]
      }
      EitherT(either)
    }

    override def handleErrors(f: EA => A): EitherT[F, Nothing, A] = {
      val eitherF: F[Either[Nothing, A]] = fa.value.map {
        case Right(a) => Right(a)
        case Left(e) => Right(f(e))
      }
      EitherT(eitherF)
    }
  }

  override def raiseError[E, A](e: E): EitherT[F, E, A] = {
    EitherT(m.pure(Left(e)))
  }

}


// Although the compiler can derive `implicitly[ErrorMonad[[L, R] =>> EitherT[Future, L, R]]]`
// for comprehension is not going to work without the following workaround:
given outcomeErrorMonad as ErrorMonad[[L, R] =>> EitherT[Future, L, R]] = eitherTErrorMonad
