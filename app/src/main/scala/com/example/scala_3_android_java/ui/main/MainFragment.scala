package com.example.scala_3_android_java.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.example.scala_3_android_java.R
import androidx.lifecycle.Observer
import scala.compiletime.uninitialized

object MainFragment:
  def newInstance = MainFragment()

class MainFragment extends Fragment:
  private var viewModel: MainViewModel = uninitialized
  private var inputOrgText: EditText = uninitialized
  private var inputRepoText: EditText = uninitialized
  private var submitButton: Button = uninitialized
  private var queryResultTextView: TextView = uninitialized
  private var progressBar: ProgressBar = uninitialized

  override def onCreate(savedInstanceState: Bundle): Unit =
    super.onCreate(savedInstanceState)
    viewModel = ViewModelProvider(this).get(classOf[MainViewModel])

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
    progressBar = view.findViewById(R.id.progress_bar)

//    // Observe the LiveData from ViewModel
//    viewModel.getMessage.observe(getViewLifecycleOwner, (message: String) =>
//      // Update the UI when data changes
//      messageTextView.setText("\n" + message)
//    )

    // Observe the ProjectInfoState LiveData
    viewModel.getProjectInfoState.observe(getViewLifecycleOwner, {
      case Initial =>
        progressBar.setVisibility(View.GONE)
        queryResultTextView.setText("Enter organization and repository to search")

      case Loading =>
        progressBar.setVisibility(View.VISIBLE)
        queryResultTextView.setText("Loading...")

      case Error(message) =>
        progressBar.setVisibility(View.GONE)
        queryResultTextView.setText("")
        Toast.makeText(getContext, s"An error occurred: $message", Toast.LENGTH_LONG).show()

      case Success(projectInfo) =>
        progressBar.setVisibility(View.GONE)
        queryResultTextView.setText(
          s"""
             |Project: ${inputOrgText.getText}/${inputRepoText.getText}
             |Description: ${projectInfo.description}
             |Stars: ${projectInfo.stars}
             |${if projectInfo.topic.isEmpty then "" else s"Topics: ${projectInfo.topic.mkString(", ")}"}
                """.stripMargin)
    })

    // Set up the button click listener
    submitButton.setOnClickListener(new View.OnClickListener() {
      override def onClick(v: View): Unit =
        // Get the input from the EditText
        val org = inputOrgText.getText.toString.trim
        val repo = inputRepoText.getText.toString.trim

        // Validate input
        if org.isEmpty || repo.isEmpty then
          Toast.makeText(getContext, "Please enter both organization and repository", Toast.LENGTH_SHORT).show()
        else
          // Use the ViewModel to fetch project info
          viewModel.fetchProjectInfo(org, repo)
    })
