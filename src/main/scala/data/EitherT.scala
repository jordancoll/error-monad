package data

case class EitherT[F[_], L, R](value: F[Either[L, R]])
