package com.example.scala_3_android_java

/**
 * A simple Scala object accessible from Java code
 */
object Bar:
  // A value that will be accessible from Java
  val foo: String = "Hello from Scala in app module!"
  
  // A method that returns the foo value
  def foo(): String = foo
  
  // Access the core module's Foo.bar value
  def getValueFromCore(): Int = com.example.core.Foo.bar 