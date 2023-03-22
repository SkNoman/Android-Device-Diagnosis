package com.example.shutdowntime

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.annotation.RequiresApi
import java.time.LocalDateTime

class MainActivity : AppCompatActivity() {

    private val shutdownReceiver = object : BroadcastReceiver(){
        @RequiresApi(Build.VERSION_CODES.O)
        override fun onReceive(context: Context?, intent: Intent?) {
            println("Hello world im shutting down")
            Log.e("hello","See im down")
            val sharedPreferences = context?.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences?.edit()
            val currentTime = LocalDateTime.now()
            Log.e("helloTime",currentTime.toString())
            editor?.putString("time2", currentTime.toString())
            editor?.apply()
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val shutDownFilter = IntentFilter(Intent.ACTION_SHUTDOWN)
        registerReceiver(shutdownReceiver,shutDownFilter)

        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val savedTime = sharedPreferences.getString("time", "time")
        val endTime = sharedPreferences.getString("time2","time2")


        // Use the savedTime value as needed
         Log.d("MainActivity", "Saved time: $savedTime")

        //hooks
        val txtBootTime = findViewById<TextView>(R.id.txtBootTime)
        val txtShutdownTime = findViewById<TextView>(R.id.shutdownTime)
        txtBootTime.text = "Boot Time: " +savedTime.toString()
        txtShutdownTime.text = "ShutDown Time: "+endTime.toString()


    }
}