package com.example.scala_3_android_java;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scala_3_android_java.ui.main.MainFragment;
import com.example.core.Foo;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        try {
            // Access the core module's Scala value
            int coreValue = Foo.bar();
            Log.d(TAG, "Value from core module: " + coreValue);
            
            // Access the app module's Scala code directly
            String scalaMessage = Bar.foo();
            Log.d(TAG, "Message from Scala in app module: " + scalaMessage);
            
            // Call a method that accesses core from Scala in app
            int valueFromCore = Bar.getValueFromCore();
            Log.d(TAG, "Value from core via app Scala: " + valueFromCore);
            
            // Create a toast with the message
            Toast.makeText(this, scalaMessage, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Log.e(TAG, "Error accessing Scala code", e);
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow();
        }
    }
}