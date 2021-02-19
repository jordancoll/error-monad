package examples

import data.EitherF
import instances.given
import syntax.*

import com.twitter.util.Await

case class Potato(potatoId: Int)

case class Pommes(potato: Potato)

enum Sauce:
  case Ketchup, Mayo

case class PommesWithSauce(pommes: Pommes, sauce: Sauce)

object EitherFExample extends App:

  val eitherF = 
    for
      potato <- getPotato()
      pommes <- makePommes(potato)
      result <- addSauce(pommes, Sauce.Mayo).handleErrors {
        case OutOfSauce(_) => PommesWithSauce(pommes, Sauce.Ketchup)
      }
    yield result

  Await.result(eitherF.value) match
    case Left(GetPotatoError(_)) => Console.err.println("Could not get potato")
    case Left(MakePommesError(_)) => Console.err.println("Could not make pommes")
    case Right(pommes) => println(s"🍟🍟🍟 $pommes 🍟🍟🍟")

  case class GetPotatoError(m: String)

  def getPotato(): EitherF[GetPotatoError, Potato] =
    EitherF.value(Potato(5))

  case class MakePommesError(m: String)

  def makePommes(potato: Potato): EitherF[MakePommesError, Pommes] =
    EitherF.value(Pommes(potato))

  case class OutOfSauce(sauce: Sauce)

  def addSauce(pommes: Pommes, sauce: Sauce): EitherF[OutOfSauce, PommesWithSauce] =
    EitherF.error(OutOfSauce(sauce))
