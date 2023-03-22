package com.example.shutdowntime

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_SHUTDOWN
import android.content.IntentFilter
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.startActivity
import java.time.LocalDateTime

class ShutdownReceiver : BroadcastReceiver() {


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context?, intent: Intent?) {
       // val action = intent?.action
      //  Log.e("hello",action.toString())
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED){
            println("Hello world im booted up")
            Log.e("hello","See im booted")

            val sharedPreferences = context?.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences?.edit()
            val currentTime = LocalDateTime.now()
            Log.e("helloTime",currentTime.toString())
            editor?.putString("time", currentTime.toString())
            editor?.apply()

        }
//        else if(intent?.action == ACTION_SHUTDOWN){
//            println("Hello world im shutting down")
//            Log.e("hello","See im down")
//            val sharedPreferences = context?.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
//            val editor = sharedPreferences?.edit()
//            val currentTime = LocalDateTime.now()
//            Log.e("helloTime",currentTime.toString())
//            editor?.putString("time2", currentTime.toString())
//            editor?.apply()
//        }

    }





}
