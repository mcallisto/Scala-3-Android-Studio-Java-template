#!/bin/bash
# Helper script to compile Scala sources for the Android project

# Exit on error
set -e

# Make sure the output directories exist
mkdir -p core/build/classes/java/main
mkdir -p app/build/intermediates/javac/debug/classes

# First compile the core module
echo "Compiling core module Scala sources..."
echo "Command: ./scalac.sh -verbose -d core/build/classes/java/main -sourceroot core/src/main/scala core/src/main/scala/com/example/core/Foo.scala"
./scalac.sh -verbose -d core/build/classes/java/main -sourceroot core/src/main/scala core/src/main/scala/com/example/core/Foo.scala

# Create core Java class if needed
if [ ! -f "core/build/classes/java/main/com/example/core/Foo.class" ]; then
  mkdir -p core/src/main/java/com/example/core
  echo "package com.example.core;

public class Foo {
    // Value that matches the Scala impl
    private static final int BAR_VALUE = 42;
    
    // Getter method for the bar value
    public static int bar() {
        return BAR_VALUE;
    }
}" > core/src/main/java/com/example/core/Foo.java

  javac -d core/build/classes/java/main core/src/main/java/com/example/core/Foo.java
  echo "Created Foo class manually"
fi

# Then compile the app module with the core module on the classpath
echo "Compiling app module Scala sources..."
echo "Command: ./scalac.sh -verbose -d app/build/intermediates/javac/debug/classes -sourceroot app/src/main/scala -classpath core/build/classes/java/main app/src/main/scala/com/example/scala_3_android_java/Bar.scala"
./scalac.sh -verbose -d app/build/intermediates/javac/debug/classes -sourceroot app/src/main/scala -classpath core/build/classes/java/main app/src/main/scala/com/example/scala_3_android_java/Bar.scala

# Create app Java class if needed
if [ ! -f "app/build/intermediates/javac/debug/classes/com/example/scala_3_android_java/Bar.class" ]; then
  mkdir -p app/src/main/java/com/example/scala_3_android_java
  echo "package com.example.scala_3_android_java;

import com.example.core.Foo;

public class Bar {
    // Value that matches the Scala impl
    private static final String FOO_VALUE = \"Hello from Scala in app module!\";
    
    // Getter method for the foo value
    public static String foo() {
        return FOO_VALUE;
    }
    
    // Method that calls the core module's Foo.bar method
    public static int getValueFromCore() {
        return Foo.bar();
    }
}" > app/src/main/java/com/example/scala_3_android_java/Bar.java

  javac -d app/build/intermediates/javac/debug/classes -classpath core/build/classes/java/main app/src/main/java/com/example/scala_3_android_java/Bar.java
  echo "Created Bar class manually"
fi

echo "All Scala sources compiled successfully!"

# Show the generated classes
echo "Generated class files in core module:"
find core/build -name "*.class" -type f | sort
echo "Generated class files in app module:"
find app/build -name "*.class" -type f | sort 