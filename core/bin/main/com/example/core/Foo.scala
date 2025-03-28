package com.example.core

import sttp.client4.upicklejson.default.asJson

case class Description(
                        description: String,
                        stars: Int,
                        topic: List[String] = List.empty
                      ) derives upickle.default.ReadWriter

object Foo:

  val bar: Int = 42

  val option: Option[Int] = Some(42)

  import sttp.client4.okhttp.OkHttpSyncBackend
  import sttp.client4.*

  val backend: WebSocketSyncBackend = OkHttpSyncBackend()

  def getProjectInfo(org: String, repo: String): Either[String, Description] =
    basicRequest
      .get(uri"https://index.scala-lang.org/api/v1/projects/$org/$repo")
      .response(asJson[Description])
      .send(backend)
      .body
      .left
      .map(_.toString)
