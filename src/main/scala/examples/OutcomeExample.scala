package examples

import classes._
import instances.{given, _}
import data._

import com.twitter.util.{Await, Future}

case class Potato(potatoId: Int)
case class Pommes(potato: Potato)
enum Sauce {
  case Ketchup, Mayo
}
case class PommesWithSauce(pommes: Pommes, sauce: Sauce)

object OutcomeExample extends App {

  val outcomeF: OutcomeF[GetPotatoError | MakePommesError, PommesWithSauce] = for {
    potato <- getPotato()
    pommes <- makePommes(potato)
    result <- addSauce(pommes, Sauce.Mayo).handleErrors {
      case OutOfSauce(_) => PommesWithSauce(pommes, Sauce.Ketchup)
    }
  } yield result

  Await.result(outcomeF.value) match {
    case Left(GetPotatoError(_)) => Console.err.println("Could not get potato")
    case Left(MakePommesError(_)) => Console.err.println("Could not make pommes")
    case Right(pommes) => println(s"🍟🍟🍟 $pommes 🍟🍟🍟")
  }

  case class GetPotatoError(m: String)

  def getPotato(): OutcomeF[GetPotatoError, Potato] = {
    OutcomeF.value(Potato(5))
  }

  case class MakePommesError(m: String)

  def makePommes(potato: Potato): OutcomeF[MakePommesError, Pommes] = {
    OutcomeF.value(Pommes(potato))
  }

  case class OutOfSauce(sauce: Sauce)

  def addSauce(pommes: Pommes, sauce: Sauce): OutcomeF[OutOfSauce, PommesWithSauce] = {
    OutcomeF.error(OutOfSauce(sauce))
  }
}
