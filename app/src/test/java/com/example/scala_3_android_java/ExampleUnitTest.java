package com.example.scala_3_android_java;

import org.junit.Test;

import static org.junit.Assert.*;

import com.example.core.Foo;

import scala.jdk.javaapi.OptionConverters;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void integer_fromScalaCore() {
        assertEquals(42, Foo.bar());
    }

    @Test
    public void converted_option_fromScalaCore() {
        assertEquals(42, OptionConverters.toJava(Foo.option()).get());
    }
}