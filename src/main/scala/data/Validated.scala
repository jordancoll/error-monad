package data

import classes._

enum Validated[+E, +A] {

  case Invalid[E](errors: List[E]) extends Validated[E, Nothing]

  case Valid(a: A) extends Validated[Nothing, A]
}
