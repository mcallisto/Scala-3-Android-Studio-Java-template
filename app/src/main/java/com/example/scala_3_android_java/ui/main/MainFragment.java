package com.example.scala_3_android_java.ui.main;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.core.Description;
import com.example.core.Foo;
import com.example.scala_3_android_java.R;

import scala.jdk.javaapi.OptionConverters;
import scala.util.Either;
import scala.util.Left;
import scala.util.Right;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainFragment extends Fragment {

    private MainViewModel mViewModel;
    private TextView messageTextView;
    private EditText inputOrgText;
    private EditText inputRepoText;
    private Button submitButton;
    private TextView queryResultTextView;

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        // TODO: Use the ViewModel
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Find view references
//        messageTextView = view.findViewById(R.id.message_text_view);
        inputOrgText = view.findViewById(R.id.input_organisation);
        inputRepoText = view.findViewById(R.id.input_repository);
        submitButton = view.findViewById(R.id.submit_button);
        queryResultTextView = view.findViewById(R.id.query_result_text_view);

        // Observe the LiveData from ViewModel
//        mViewModel.getMessage().observe(getViewLifecycleOwner(), message -> {
//            // Update the UI when data changes
//            messageTextView.setText(message + " " + Foo.bar() + " " + OptionConverters.toJava(Foo.option()).get());
//        });

        // Set up the button click listener
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the input from the EditText
                String org = inputOrgText.getText().toString();
                String repo = inputRepoText.getText().toString();

                // Execute side effect on a background thread
                executor.submit(() -> {
                    Either<String, Description> result = Foo.getProjectInfo(org, repo);

                    // Update UI on the main thread
                    getActivity().runOnUiThread(() -> {
                        if (result.isRight()) {
                            // Success: Update TextView with the result
                            Right<String, Description> right = (Right<String, Description>) result;
                            Description projectInfo = right.value();
                            queryResultTextView.setText(projectInfo.description() + ", stars: " + projectInfo.stars());
                        } else {
                            // Failure: Update TextView with the error message
                            Left<String, Description> left = (Left<String, Description>) result;
                            String error = left.value();
                            queryResultTextView.setText("");
                            // Optionally, you can show a Toast with the error
                            Toast.makeText(getContext(), "An error occurred: " + error, Toast.LENGTH_LONG).show();
                        }
                    });
                });

                // Here you can send your user input to the ViewModel to update the data
                // mViewModel.processInput(userInput)
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        executor.shutdown();
    }

}