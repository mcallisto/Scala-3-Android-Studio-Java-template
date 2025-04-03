package com.example.scala_3_android_java.ui.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {
    private MutableLiveData<String> message = new MutableLiveData<>();

    public MainViewModel() {
        // Initialize with Hello World message
        message.setValue("Hello, Scala 3 Android!");
    }

    // Getter for the LiveData
    public LiveData<String> getMessage() {
        return message;
    }

    // Method to update the message
    public void setMessage(String newMessage) {
        message.setValue(newMessage);
    }
}