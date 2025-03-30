package com.example.scala_3_android_java;

import com.example.core.Foo;

public class Bar {
    // Value that matches the Scala impl
    private static final String FOO_VALUE = "Hello from Scala in app module!";
    
    // Getter method for the foo value
    public static String foo() {
        return FOO_VALUE;
    }
    
    // Method that calls the core module's Foo.bar method
    public static int getValueFromCore() {
        return Foo.bar();
    }
}
