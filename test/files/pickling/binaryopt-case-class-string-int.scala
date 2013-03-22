import scala.pickling._
import binaryopt._

case class Person(name: String, age: Int)

object Test extends App {

  val p = Person("Jim", 43)

  val pickle = p.pickle

  println("ARRAY:")
  println(pickle.value.mkString("[", ",", "]"))

  val up = pickle.unpickle[Person]
  println(s"unpickled: $up")
}