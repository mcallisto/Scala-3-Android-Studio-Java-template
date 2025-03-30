# Keep Scala classes and methods
-dontwarn scala.**
-keep class scala.** { *; }
-keepclassmembers class scala.** { *; }

# Keep all Scala objects
-keep class * extends scala.runtime.AbstractFunction*
-keep class * extends scala.collection.SeqLike
-keep class scala.concurrent.ExecutionContext

# Keep all MODULE$ statics for Scala objects
-keepclassmembers class * {
    public static final ** MODULE$;
}

# Keep all Scala object classes and their methods
-keepclassmembers class * {
    public static ** MODULE$;
}

# Keep Bar specifically
-keep class com.example.scala_3_android_java.Bar$ { *; }
-keep class com.example.scala_3_android_java.Bar { *; }

# Keep AppScala specifically
-keep class com.example.scala_3_android_java.AppScala$ { *; }
-keep class com.example.scala_3_android_java.AppScala { *; }

# Keep Foo from core module
-keep class com.example.core.Foo$ { *; }
-keep class com.example.core.Foo { *; }

# Keep Lambda Factory and related methods
-keepclassmembers class * {
    private static synthetic *** lambda$*(...);
}
-keep class java.lang.invoke.LambdaMetafactory { *; }
-keepclassmembernames class * {
    private static synthetic *** lambda$*(...);
}

# Handle Java 8 lambdas properly
-keepattributes Signature
-keepattributes MethodParameters
-keepattributes InnerClasses
-keepattributes EnclosingMethod
-keepattributes Exceptions
-keepattributes LineNumberTable
-keepattributes SourceFile
-keepattributes *Annotation*

# Don't note about classes that are expected to be missing
-dontnote scala.Enumeration
-dontnote org.xml.**
-dontnote scala.concurrent.ExecutionContext$

# Fix for Lambda issues
-keep class scala.Function* { *; }
-keep class scala.runtime.** { *; }
-keep class scala.Tuple* { *; }
-keep class * implements java.io.Serializable { *; }

# Fix for sttp and upickle libraries
-keep class com.softwaremill.sttp.** { *; }
-keep class upickle.** { *; }
-keep class geny.** { *; }
-dontwarn com.softwaremill.sttp.**
-dontwarn upickle.**
-dontwarn geny.** 