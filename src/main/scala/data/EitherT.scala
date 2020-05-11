package data

import classes._

// case class EitherT[F[_], L, R](value: F[Either[L, R]])

case class EitherT[F[+_], +L, R](value: F[Either[L, R]]) {
  def flatMap[LL >: L, RR](f: R => EitherT[F, LL, RR]): EitherT[F, LL, RR] = ???

  def map[RR](f: R => RR): EitherT[F, L, RR] = ???
}
