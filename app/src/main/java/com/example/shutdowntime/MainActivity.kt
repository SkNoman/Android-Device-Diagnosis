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
import com.example.shutdowntime.databinding.ActivityMainBinding
import java.time.LocalDateTime

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val shutdownReceiver = object : BroadcastReceiver(){
        @RequiresApi(Build.VERSION_CODES.O)
        override fun onReceive(context: Context?, intent: Intent?) {
            println("Hello world im shutting down")
            val sharedPreferences = context?.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences?.edit()
            val currentTime = LocalDateTime.now()
            println(currentTime.toString())
            editor?.putString("shutdown_time", currentTime.toString())
            editor?.apply()
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val shutDownFilter = IntentFilter(Intent.ACTION_SHUTDOWN)
        registerReceiver(shutdownReceiver,shutDownFilter)

        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        val savedTime = sharedPreferences.getString("boot_time", "")
        val endTime = sharedPreferences.getString("shutdown_time","")

        println("Saved time: $savedTime,$endTime")

        binding.txtBootTime.text = "Boot Time: " +savedTime.toString()
        binding.shutdownTime.text = "ShutDown Time: "+endTime.toString()


    }
}