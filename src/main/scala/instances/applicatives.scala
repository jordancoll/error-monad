package instances

import classes.ErrorApplicative
import data.Validated

given ErrorApplicative[Validated] with
  import Validated.*

  override def pure[E, A](a: A): Validated[E, A] = Valid(a)

  override def ap[EA, A, EB, B](fa: Validated[EA, A])(ff: Validated[EB, A => B]): Validated[EA | EB, B] = (fa, ff) match
    case (Valid(a), Valid(f)) => Valid(f(a))
    case (Invalid(errorsA), Invalid(errorsB)) => Invalid(errorsA ++ errorsB)
    case (Invalid(errorsA), _) => Invalid(errorsA)
    case (Valid(a), Invalid(errorsB)) => Invalid(errorsB)
