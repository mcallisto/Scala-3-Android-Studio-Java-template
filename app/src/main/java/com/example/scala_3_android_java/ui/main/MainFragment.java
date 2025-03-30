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

import com.example.core.Foo;
import com.example.scala_3_android_java.Bar;
import com.example.scala_3_android_java.R;

public class MainFragment extends Fragment {

    private MainViewModel mViewModel;
    private TextView messageTextView;
    private TextView queryResultTextView;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
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
        queryResultTextView = view.findViewById(R.id.query_result_text_view);
        
        // Display values from our Scala code
        int coreValue = Foo.bar();
        String appValue = Bar.foo();
        int valueFromCore = Bar.getValueFromCore();
        
        String message = String.format(
            "Core module value: %d\nApp module value: %s\nValue from core via app: %d",
            coreValue, appValue, valueFromCore);
            
        queryResultTextView.setText(message);
    }
}