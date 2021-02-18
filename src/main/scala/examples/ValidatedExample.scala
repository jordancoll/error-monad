package examples

import classes._
import data.Validated
import instances.given

import Validated._

case class Name(value: String)
case class Address(value: String)
case class Age(value: Int)

case class Student(name: Name, age: Age, address: Address)

case class InvalidName(name: Name)
case class InvalidAge(age: Age)
case class InvalidAdress(address: Address)

def validate(name: Name, age: Age, address: Address): Validated[InvalidName | InvalidAge | InvalidAdress, Student] =
  (validateName(name), validateAge(age), validateAddress(address)) mapN Student.apply

private def validateName(name: Name): Validated[InvalidName, Name] =
  if name.value.length < 2 then
    Invalid(List(InvalidName(name)))
  else
    Valid(name)

private def validateAge(age: Age): Validated[InvalidAge, Age] =
  if age.value < 18 then
    Invalid(List(InvalidAge(age)))
  else
    Valid(age)

private def validateAddress(address: Address): Validated[InvalidAdress, Address] =
  if address.value.length <= 10 then
    Invalid(List(InvalidAdress(address)))
  else
    Valid(address)

object ValidatedExample extends App:
  println(validate(Name("Joe"), Age(20), Address("4881 Duff Avenue 1")))

