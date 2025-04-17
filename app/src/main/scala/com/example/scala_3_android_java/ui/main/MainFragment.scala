package com.example.scala_3_android_java.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.core.Foo
import com.example.scala_3_android_java.R
import scala.util.{Left, Right}
import java.util.concurrent.Executors

object MainFragment:
  def newInstance = MainFragment()

class MainFragment extends Fragment:
  private var mViewModel: MainViewModel = _
//  private var messageTextView: TextView = _
  private var inputOrgText: EditText = _
  private var inputRepoText: EditText = _
  private var submitButton: Button = _
  private var queryResultTextView: TextView = _
  private val executor = Executors.newSingleThreadExecutor()

  override def onCreate(savedInstanceState: Bundle): Unit =
    super.onCreate(savedInstanceState)
    mViewModel = ViewModelProvider(this).get(classOf[MainViewModel])
    // TODO: Use the ViewModel

  override def onCreateView(inflater: LayoutInflater,
                            container: ViewGroup,
                            savedInstanceState: Bundle): View =
    inflater.inflate(R.layout.fragment_main, container, false)

  override def onViewCreated(view: View, savedInstanceState: Bundle): Unit =
    super.onViewCreated(view, savedInstanceState)
    // Find view references
//    messageTextView = view.findViewById(R.id.message_text_view)
    inputOrgText = view.findViewById(R.id.input_organisation)
    inputRepoText = view.findViewById(R.id.input_repository)
    submitButton = view.findViewById(R.id.submit_button)
    queryResultTextView = view.findViewById(R.id.query_result_text_view)
//    // Observe the LiveData from ViewModel
//    mViewModel.getMessage.observe(getViewLifecycleOwner, (message: String) =>
//      // Update the UI when data changes
//      messageTextView.setText("\n" + message)
//    )
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
            result match
              case Left(error) =>
                queryResultTextView.setText("")
                // Optionally, you can show a Toast with the error
                Toast.makeText(getContext, s"An error occurred: $error", Toast.LENGTH_LONG).show()
              case Right(projectInfo) =>
                queryResultTextView.setText(s"${projectInfo.description}, stars: ${projectInfo.stars}")
          )
        )
        // Here you can send your user input to the ViewModel to update the data
        // mViewModel.processInput(userInput)
    })

  override def onDestroyView(): Unit =
    super.onDestroyView()
    executor.shutdown()
