package com.example.shutdowntime

import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import androidx.core.app.NotificationCompat

class MyForegroundService : Service() {

    private lateinit var shutdownReceiver: ShutdownReceiver

    override fun onCreate() {
        super.onCreate()

        // Create an instance of the ShutdownReceiver class
        shutdownReceiver = ShutdownReceiver()

        // Register the ShutdownReceiver dynamically
        val intentFilter = IntentFilter(Intent.ACTION_SHUTDOWN)
        registerReceiver(shutdownReceiver, intentFilter)
    }

    override fun onDestroy() {
        super.onDestroy()

        // Unregister the ShutdownReceiver when the service is destroyed
        unregisterReceiver(shutdownReceiver)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {


        val notification = NotificationCompat.Builder(this, "7008")
            .setContentTitle("My Foreground Service")
            .setContentText("Running in the background")
            .setSmallIcon(R.drawable.ic_baseline_notifications_24)
            .build()

        // Start the foreground service
        startForeground(NOTIFICATION_ID, notification)

        // Return START_STICKY to indicate that the service should be restarted if it is killed
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    companion object {
        const val NOTIFICATION_ID = 1
    }
}
