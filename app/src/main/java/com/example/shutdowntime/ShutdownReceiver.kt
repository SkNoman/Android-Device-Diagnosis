package com.example.shutdowntime

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime

class ShutdownReceiver : BroadcastReceiver() {


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED){
            println("Hello world im booted up")
            val sharedPreferences = context?.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences?.edit()
            val currentTime = LocalDateTime.now()
            println(currentTime.toString())
            editor?.putString("boot_time", currentTime.toString())
            editor?.apply()
        }
    }





}
