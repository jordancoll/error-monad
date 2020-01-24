# Type-Safe Error Handling in Dotty

This repository demonstrates how [Union Types](https://dotty.epfl.ch/docs/reference/new-types/union-types.html) can be used for type-safe error handling.

## `ErrorMonad`
The `ErrorMonad[F[_, _]]` type class that expresses composition of error-prone operations.

The key difference here from a regular `Monad` is the addition of a left side that represents an error type. These error types are summed on the left side when composing operations, providing the user a precise list of possible errors:

```scala
def [EA, EB, A, B](fa: F[EA, A]) flatMap(f: A => F[EB, B]): F[EA | EB, B]
```

### Operation composition example
Let's make french fries:

```scala
val pommes = for {
  potato <- getPotato() //: Either[GetPotatoError, Potato]
  pommes <- makePommes(potato) //: Either[MakePommesError, Pommes]
} yield pommes //: Either[GetPotatoError | MakePommesError, Pommes]
```

You can check out a more detailed example that uses an async context [here](./src/main/scala/examples/OutcomeExample.scala). We supplied an implementation using `com.twitter.util.Future`, but it can be easily swapped out for any other effect library (like `cats-effect`).

## `ErrorApplicative`
Similar to `ErrorMonad`, we defined the `ErrorApplicative` type class that can express parallel composition:

```scala
def [EA, EB, A, B](fa: F[EA, A]) product(fb: F[EB, B]): F[EA | EB, (A, B)]
```

See an example that accumulates validation errors [here](./src/main/scala/examples/ValidationExample.scala)
