package com.example.scala_3_android_java

import com.example.core.Foo
import org.junit.Test
import org.junit.Assert.*

import scala.jdk.javaapi.OptionConverters

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ScalaUnitTest {

  @Test
  def addition_isCorrect(): Unit = {
    assertEquals(4, 2 + 2)
  }

  @Test def integer_fromScalaCore(): Unit = {
    assertEquals(42, Foo.bar)
  }

  @Test def converted_option_fromScalaCore(): Unit = {
    assertEquals(42, OptionConverters.toJava(Foo.option).get)
  }

}