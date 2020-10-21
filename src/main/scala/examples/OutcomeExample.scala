package examples

import classes._
import instances.{_, given _}
import data._

import com.twitter.util.Await

case class Potato(potatoId: Int)
case class Pommes(potato: Potato)
enum Sauce {
  case Ketchup, Mayo
}
case class PommesWithSauce(pommes: Pommes, sauce: Sauce)

object OutcomeExample extends App {

  val outcome: Outcome[GetPotatoError | MakePommesError, PommesWithSauce] = for {
    potato <- getPotato()
    pommes <- makePommes(potato)
    result <- addSauce(pommes, Sauce.Mayo).handleErrors {
      case OutOfSauce(_) => PommesWithSauce(pommes, Sauce.Ketchup)
    }
  } yield result

  Await.result(outcome.value) match {
    case Left(GetPotatoError(_)) => Console.err.println("Could not get potato")
    case Left(MakePommesError(_)) => Console.err.println("Could not make pommes")
    case Right(pommes) => println(s"🍟🍟🍟 $pommes 🍟🍟🍟")
  }

  case class GetPotatoError(m: String)

  def getPotato(): Outcome[GetPotatoError, Potato] = {
    Outcome.value(Potato(5))
  }

  case class MakePommesError(m: String)

  def makePommes(potato: Potato): Outcome[MakePommesError, Pommes] = {
    Outcome.value(Pommes(potato))
  }

  case class OutOfSauce(sauce: Sauce)

  def addSauce(pommes: Pommes, sauce: Sauce): Outcome[OutOfSauce, PommesWithSauce] = {
    Outcome.error(OutOfSauce(sauce))
  }
}
