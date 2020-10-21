package instances

import classes._
import com.twitter.util.Future

given Monad[List] {

  override def pure[A](a: A): List[A] = List(a)

  extension [A, B](fa: List[A])
    override def flatMap(f: A => List[B]): List[B] = fa.flatMap(f)

}

given Monad[Future] {

  override def pure[A](a: A): Future[A] = Future(a)

  extension [A, B](fa: Future[A])
    override def flatMap(f: A => Future[B]): Future[B] = fa.flatMap(f)

}
