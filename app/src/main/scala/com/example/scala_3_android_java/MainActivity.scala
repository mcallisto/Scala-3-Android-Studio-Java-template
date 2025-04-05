package com.example.scala_3_android_java

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.scala_3_android_java.ui.main.MainFragment

class MainActivity extends AppCompatActivity:
  override protected def onCreate(savedInstanceState: Bundle): Unit =
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    if (savedInstanceState == null) getSupportFragmentManager.beginTransaction.replace(R.id.container, MainFragment.newInstance).commitNow()
