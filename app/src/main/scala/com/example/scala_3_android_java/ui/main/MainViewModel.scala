package com.example.scala_3_android_java.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.core.{Description, Foo}
import java.util.concurrent.Executors

// Sealed trait for UI state
sealed trait ProjectInfoState
case object Loading extends ProjectInfoState
case class Error(message: String) extends ProjectInfoState
case class Success(projectInfo: Description) extends ProjectInfoState
case object Initial extends ProjectInfoState

class MainViewModel extends ViewModel:
  private val message = new MutableLiveData[String]
  private val projectInfoState = new MutableLiveData[ProjectInfoState]
  private val executor = Executors.newSingleThreadExecutor()

  // Initialize with Hello World message and Initial state
  message.setValue("Hello, Scala 3 Android!")
  projectInfoState.setValue(Initial)

  // Getters for the LiveData
  def getMessage: LiveData[String] = message
  def getProjectInfoState: LiveData[ProjectInfoState] = projectInfoState

  // Method to update the message
  def setMessage(newMessage: String): Unit =
    message.setValue(newMessage)

  // Method to fetch project information
  def fetchProjectInfo(org: String, repo: String): Unit =
    projectInfoState.setValue(Loading)
    executor.execute(() =>
      val state =
        Foo.getProjectInfo(org, repo) match
          case Left(error)        => Error(error)
          case Right(projectInfo) => Success(projectInfo)
      projectInfoState.postValue(state)
    )

  override def onCleared(): Unit =
    super.onCleared()
    executor.shutdown()
