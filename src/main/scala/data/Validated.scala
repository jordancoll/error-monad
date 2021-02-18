package data

import classes.*

enum Validated[+E, +A]:
  case Invalid[E](errors: List[E]) extends Validated[E, Nothing]
  case Valid(a: A) extends Validated[Nothing, A]
