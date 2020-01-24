package instances

import classes.ErrorApplicative
import data.Validated

given ErrorApplicative[Validated] {
  import Validated._

  override def pure[E, A](a: A): Validated[E, A] = Valid(a)
  
  override def [EA, EB, A, B](fa: Validated[EA, A]) ap(ff: Validated[EB, A => B]): Validated[EA | EB, B] = {
    (fa, ff) match {
      case (Valid(a), Valid(f)) => Valid(f(a))
      case (Invalid(errorsA), Invalid(errorsB)) => Invalid(errorsA ++ errorsB)
      case (Invalid(errorsA), _) => Invalid(errorsA)
      case (Valid(a), Invalid(errorsB)) => Invalid(errorsB)
    }
  }
}