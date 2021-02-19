package syntax

import classes.ErrorApplicative

// these methods should be autogenerated
extension [F[_, _]: ErrorApplicative, E1, E2, E3, A1, A2, A3, B]
          (t: (F[E1, A1], F[E2, A2], F[E3, A3]))

  def mapN(f: (A1, A2, A3) => B): F[E1 | E2 | E3, B] =
    val p = summon[ErrorApplicative[F]].product(summon[ErrorApplicative[F]].product(t._1, t._2), t._3)
    summon[ErrorApplicative[F]].map(p) {
      case ((a1, a2), a3) => f(a1, a2, a3)
    }

    