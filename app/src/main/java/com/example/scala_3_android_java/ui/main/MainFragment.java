package com.example.scala_3_android_java.ui.main;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.core.Foo;
import com.example.scala_3_android_java.R;

import cats.effect.IO;
import scala.jdk.javaapi.OptionConverters;

public class MainFragment extends Fragment {

    private MainViewModel mViewModel;
    private MainViewModel viewModel;
    private TextView messageTextView;

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

        // Find the TextView in the layout
        messageTextView = view.findViewById(R.id.message_text_view);

        // Observe the LiveData from ViewModel
        mViewModel.getMessage().observe(getViewLifecycleOwner(), message -> {
            // Update the UI when data changes
            messageTextView.setText(message + " " + Foo.bar() + " " + OptionConverters.toJava(Foo.option()).get() + " " + IO.pure(42).toString());
        });
    }
}