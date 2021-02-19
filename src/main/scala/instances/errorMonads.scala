package instances

import classes.{ErrorMonad, Monad}
import data.EitherT
import com.twitter.util.Future

given eitherTErrorMonad[F[_]: Monad]: ErrorMonad[[L, R] =>> EitherT[F, L, R]] with

  override def pure[L, R](a: R): EitherT[F, L, R] = {
    val either: Either[L, R] = Right(a)
    EitherT(summon[Monad[F]].pure(either))
  }

  override def flatMap[EA, A, EB, B](fa: EitherT[F, EA, A])(f: A => EitherT[F, EB, B]): EitherT[F, EA | EB, B] =
    val either = fa.value.flatMap {
      case Right(v) => f(v).value.map(either => either: Either[EA | EB, B])
      case Left(e) => summon[Monad[F]].pure(Left(e: EA | EB)): F[Either[EA | EB, B]]
    }
    EitherT(either)

  override def handleErrors[EA, A](fa: EitherT[F, EA, A])(f: EA => A): EitherT[F, Nothing, A] =
    val eitherF: F[Either[Nothing, A]] = fa.value.map {
      case Right(a) => Right(a)
      case Left(e) => Right(f(e))
    }
    EitherT(eitherF)

  override def raiseError[E, A](e: E): EitherT[F, E, A] =
    EitherT(summon[Monad[F]].pure(Left(e)))

