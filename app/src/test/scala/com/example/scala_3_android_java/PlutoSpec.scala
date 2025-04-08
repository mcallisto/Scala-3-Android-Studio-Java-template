package com.example.scala_3_android_java

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class PlutuSpec extends AnyFlatSpec with should.Matchers {

  "Inputs" can "be cleaned of undone commands" in {
    (2 + 2) shouldEqual
      5
  }