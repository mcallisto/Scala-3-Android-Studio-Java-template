package com.example.scala_3_android_java.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.core.Description
import com.example.core.Foo
import com.example.scala_3_android_java.R
import com.example.scala_3_android_java.Bar
import scala.jdk.javaapi.OptionConverters
import scala.util.Either
import scala.util.Left
import scala.util.Right
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


object MainFragment:
  def newInstance = new MainFragment

class MainFragment extends Fragment:
  private var mViewModel: MainViewModel = null
  private var messageTextView: TextView = null
  private var inputOrgText: EditText = null
  private var inputRepoText: EditText = null
  private var submitButton: Button = null
  private var queryResultTextView: TextView = null
  final private val executor = Executors.newSingleThreadExecutor

  override def onCreate(@Nullable savedInstanceState: Bundle): Unit =
    super.onCreate(savedInstanceState)
    mViewModel = new ViewModelProvider(this).get(classOf[MainViewModel])
    // TODO: Use the ViewModel

  @Nullable override def onCreateView(@NonNull inflater: LayoutInflater,
                                      @Nullable container: ViewGroup,
                                      @Nullable savedInstanceState: Bundle): View =
    inflater.inflate(R.layout.fragment_main, container, false)

  override def onViewCreated(@NonNull view: View, @Nullable savedInstanceState: Bundle): Unit =
    super.onViewCreated(view, savedInstanceState)
    // Find view references
    messageTextView = view.findViewById(R.id.message_text_view)
    inputOrgText = view.findViewById(R.id.input_organisation)
    inputRepoText = view.findViewById(R.id.input_repository)
    submitButton = view.findViewById(R.id.submit_button)
    queryResultTextView = view.findViewById(R.id.query_result_text_view)
    // Observe the LiveData from ViewModel
    mViewModel.getMessage.observe(getViewLifecycleOwner, (message: String) =>
      // Update the UI when data changes
      messageTextView.setText("\n" + message + "\nwith Scala code from core module: " + Foo.bar + " " + OptionConverters.toJava(Foo.option).get + "\nwith Scala code from app module: " + Bar.foo)
    )
    // Set up the button click listener
    submitButton.setOnClickListener(new View.OnClickListener() {
      override def onClick(v: View): Unit =
        // Get the input from the EditText
        val org = inputOrgText.getText.toString
        val repo = inputRepoText.getText.toString
        // Execute side effect on a background thread
        executor.execute(() =>
          val result = Foo.getProjectInfo(org, repo)
          // Update UI on the main thread
          getActivity.runOnUiThread(() =>
            if (result.isRight) then
              // Success: Update TextView with the result
              val right = result.asInstanceOf[Right[String, Description]]
              val projectInfo = right.value
              queryResultTextView.setText(projectInfo.description + ", stars: " + projectInfo.stars)
            else
              // Failure: Update TextView with the error message
              val left = result.asInstanceOf[Left[String, Description]]
              val error = left.value
              queryResultTextView.setText("")
              // Optionally, you can show a Toast with the error
              Toast.makeText(getContext, "An error occurred: " + error, Toast.LENGTH_LONG).show()
          )
        )
        // Here you can send your user input to the ViewModel to update the data
        // mViewModel.processInput(userInput)
    })

  override def onDestroyView(): Unit =
    super.onDestroyView()
    executor.shutdown()
