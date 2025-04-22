![build_ci](https://github.com/mcallisto/Scala-3-Android-Studio-Java-template/actions/workflows/build_ci.yml/badge.svg)
# Android Studio Java project translated to Scala 3

A template project in Java for the latest **Android Studio** translated to **Scala 3**.

### What about a Kotlin template instead, with a Scala 3 module?

Android Studio is currently quite opinionated towards Kotlin,
so this Java module is not an option you get in the `New` > `New Project…` menu.

If you want to use Scala 3 starting from the standard Kotlin template,
please check https://github.com/mcallisto/Scala-3-Android-Studio-template.

## Aim

Helping the adoption of Scala in Android Studio.

This is the Scala project you get if:

1. in **Android Studio** _Meerkat | 2024.3.1 Patch 2_ you create a module with the Fragment + ViewModel template.

2. you then add a minimal Scala 3.7.0-RC2 module named `core` and call it from the `app` module

3. you use [STTP](https://github.com/softwaremill/sttp) to query [ScalaDex](https://index.scala-lang.org/)
   and display the results. Credits and big thanks for this to [@keynmol](https://github.com/keynmol)

4. you translate to Scala the activity, fragment and model Java code in the `app` module

## Notes

### Why Scala 3.7.0?

Because is the first Scala version shipping https://github.com/scala/scala3/pull/22632.
The emitted Scala code is more compatible with Android ART.

Since currently all Scala libraries are not yet published to Maven Central with this version,
the workaround is to let Android R8 minify the code,
so `minifyEnabled true` must be set even for `debug` builds.
