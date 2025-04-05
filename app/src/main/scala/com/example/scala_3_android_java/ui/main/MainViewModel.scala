package com.example.scala_3_android_java.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel extends ViewModel:
  private val message = new MutableLiveData[String]
  // Initialize with Hello World message
  message.setValue("Hello, Scala 3 Android!")

  // Getter for the LiveData
  def getMessage: LiveData[String] = message

  // Method to update the message
  def setMessage(newMessage: String): Unit =
    message.setValue(newMessage)
