![build_ci](https://github.com/mcallisto/Scala-3-Android-Studio-Java-template/actions/workflows/build_ci.yml/badge.svg)
# Android Studio Scala 3 project, translated from Java

A template project in **Scala 3** for the latest **Android Studio** translated from Java.

### What about a Kotlin template instead, with a Scala 3 module?

Android Studio is currently quite opinionated towards Kotlin,
so a Java module is not an option you get in the `New` > `New Project…` menu.

If you want to use Scala 3 together with Kotlin, starting from the standard Kotlin template,
please check https://github.com/mcallisto/Scala-3-Android-Studio-Kotlin-template.

## Aim

Helping the adoption of Scala in Android Studio.

This is the Scala project you get if:

1. in **Android Studio** _Otter | 2025.2.1_ you create an `app` module with the Fragment + ViewModel template.

2. then add a minimal Scala 3.3.7 module named `core` and call it from the `app` module

3. use [STTP](https://github.com/softwaremill/sttp) to query [ScalaDex](https://index.scala-lang.org/)
   and display the results. Credits and big thanks for this to [@keynmol](https://github.com/keynmol)

4. translate to Scala the activity, fragment and model Java code in the `app` module

## Notes

### Why Scala 3.3.7 LTS?

Because is the first LTS Scala version shipping https://github.com/scala/scala3/pull/22632,
backported from the 3.7.x lineage.
The emitted Scala code is more compatible with Android ART.

Since currently all Scala libraries are not yet published to Maven Central with this version,
the workaround is to let Android R8 minify the code,
so `minifyEnabled true` must be set even for `debug` builds.

### Building Scala with Gradle

In this project two different solutions coexist:

1. The `app` module is built using the [scala-android-plugin](https://github.com/onsqcorp/scala-android-plugin),
which is brilliantly designed to work with the official
[Android Gradle Plugin (AGP)](https://mvnrepository.com/artifact/com.android.tools.build/gradle/8.9.2).

2. The separate `core` module is built with the official Gradle
[Scala Plugin](https://docs.gradle.org/current/userguide/scala_plugin.html),
which is not compatible with AGP.
