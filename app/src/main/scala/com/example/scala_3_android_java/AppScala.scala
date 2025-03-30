package com.example.scala_3_android_java

/**
 * A simple Scala utility class for the app module
 */
object AppScala:
  // A simple method that returns a greeting
  def greeting(name: String): String = s"Hello, $name from Scala in the app module!"
  
  // A method that uses a more complex Scala feature (higher-order function)
  def processNumbers(numbers: List[Int], transformer: Int => Int): List[Int] =
    numbers.map(transformer)
  
  // A method that adds numbers
  def sum(numbers: List[Int]): Int = numbers.sum

  def sayHello(name: String): String = s"Hello, $name from app Scala!"
  
  def getInt(): Int = 42 