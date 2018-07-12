package io.chrisdavenport.cormorant.instances

import cats.implicits._
import io.chrisdavenport.cormorant._
import scala.util.Try

trait base {
  implicit val stringGet: Get[String] = Get.by(_.x)
  implicit val stringPut: Put[String] = Put.by(CSV.Field(_))

  implicit val unitGet : Get[Unit] = new Get[Unit]{
    def get(csv: CSV.Field): Either[Error.DecodeFailure, Unit] = 
      if (csv.x == "") Right(())
      else Left(Error.DecodeFailure.single("Failed to decode Unit: Received Field $field"))
  }
  implicit val unitPut : Put[Unit] = stringPut.contramap(_ => "")

  implicit val boolGet: Get[Boolean] = Get.tryOrMessage[Boolean](
    field => Try(field.x.toBoolean),
    field => s"Failed to decode Boolean: Received Field $field"
  )
  implicit val boolPut: Put[Boolean] = stringPut.contramap(_.toString)

  implicit val javaBoolGet = boolGet.map(java.lang.Boolean.valueOf)
  implicit val javaBoolPut: Put[java.lang.Boolean] = boolPut.contramap(_.booleanValue())

  implicit val charGet : Get[Char] = new Get[Char]{
    def get(csv: CSV.Field): Either[Error.DecodeFailure, Char] = 
      if (csv.x.size == 1) Right(csv.x.charAt(0))
      else Left(Error.DecodeFailure.single("Failed to decode Char: Received Field $field"))
  }
  implicit val charPut: Put[Char] = stringPut.contramap(_.toString)

  implicit val javaCharGet: Get[java.lang.Character] = charGet.map(java.lang.Character.valueOf)
  implicit val javaCharPut: Put[java.lang.Character] = charPut.contramap(_.charValue())

  implicit val floatGet: Get[Float] = Get.tryOrMessage(
    field => Try(field.x.toDouble.toFloat),
    field => s"Failed to decode Float: Received Field $field"
  )
  implicit val floatPut: Put[Float] = stringPut.contramap(_.toString)

  implicit val javaFloatGet : Get[java.lang.Float] = floatGet.map(java.lang.Float.valueOf)
  implicit val javaFolatPut : Put[java.lang.Float] = floatPut.contramap(_.floatValue())

  implicit val doubleGet: Get[Double] = Get.tryOrMessage[Double](
    field => Try(field.x.toDouble),
    field => s"Failed to decode Double: Received Field $field"
  )
  implicit val doublePut: Put[Double] = stringPut.contramap(_.toString)

  implicit val javaDoubleGet : Get[java.lang.Double] = doubleGet.map(java.lang.Double.valueOf)
  implicit val javaDoublePut : Put[java.lang.Double] = doublePut.contramap(_.doubleValue())

  implicit val byteGet : Get[Byte] = Get.tryOrMessage[Byte](
    field => Try(field.x.toByte),
    field => s"Failed to decode Byte: Received Field $field"
  )
  implicit val bytePut: Put[Byte] = intPut.contramap(_.toInt)

  implicit val javaByteGet : Get[java.lang.Byte] = byteGet.map(java.lang.Byte.valueOf)
  implicit val javaBytePut : Put[java.lang.Byte] = bytePut.contramap(_.byteValue())

  implicit val shortGet : Get[Short] = Get.tryOrMessage[Short](
    field => Try(field.x.toShort),
    field => s"Failed to decode Short: Received Field $field"
  )
  implicit val shortPut: Put[Short] = stringPut.contramap(_.toString)

  implicit val javaShortGet : Get[java.lang.Short] = shortGet.map(java.lang.Short.valueOf)
  implicit val javaShortPut : Put[java.lang.Short] = shortPut.contramap(_.shortValue())

  implicit val intGet: Get[Int] = Get.tryOrMessage[Int](
    field => Try(field.x.toInt), 
    field => s"Failed to decode Int: Received Field $field"
  )
  implicit val intPut: Put[Int] = stringPut.contramap(_.toString)

  implicit val javaIntegerGet: Get[java.lang.Integer] = intGet.map(java.lang.Integer.valueOf)
  implicit val javaIntegerPut: Put[java.lang.Integer] = intPut.contramap(_.intValue())

  implicit def optionGet[A: Get]: Get[Option[A]] = new Get[Option[A]]{
    def get(field: CSV.Field): Either[Error.DecodeFailure, Option[A]] = 
      if (field.x == "") Right(Option.empty[A])
      else Get[A].map[Option[A]](a => Some(a)).get(field)
  }
  implicit def optionPut[A](implicit P: Put[A]): Put[Option[A]] = new Put[Option[A]]{
    def put(a: Option[A]): CSV.Field = a.fold(CSV.Field(""))(a => P.put(a))
  }




}