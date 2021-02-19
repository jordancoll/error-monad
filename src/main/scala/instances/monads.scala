package instances

import classes.Monad
import com.twitter.util.Future

given Monad[List] with

  override def pure[A](a: A): List[A] = List(a)

  extension [A](fa: List[A])
    override def flatMap[B](f: A => List[B]): List[B] = fa.flatMap(f)


given Monad[Future] with

  override def pure[A](a: A): Future[A] = Future(a)

  extension [A](fa: Future[A])
    override def flatMap[B](f: A => Future[B]): Future[B] = fa.flatMap(f)

