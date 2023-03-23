package com.example.shutdowntime

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.BatteryManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.example.shutdowntime.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val shutdownReceiver = object : BroadcastReceiver(){
        @RequiresApi(Build.VERSION_CODES.O)
        override fun onReceive(context: Context?, intent: Intent?) {
            println("Hello World Im Shutting Down")
            val sharedPreferences = context?.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences?.edit()
            val currentTime = LocalDateTime.now()
            println("Shutdown Time $currentTime.toString()")
            editor?.putString("shutdown_time", currentTime.toString())
            editor?.apply()

            context?.let { getBatteryStatus(it) }


        }

        @RequiresApi(Build.VERSION_CODES.O)
        private fun getBatteryStatus(context: Context) {

            val batteryManager = getSystemService(Context.BATTERY_SERVICE) as BatteryManager
            val batteryPercentage = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
            println("Battery Status: $batteryPercentage")




            val batteryStatus: String = when (batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_STATUS)) {
                BatteryManager.BATTERY_STATUS_CHARGING -> "Charging"
                BatteryManager.BATTERY_STATUS_DISCHARGING -> "Discharging"
                BatteryManager.BATTERY_STATUS_FULL -> "Full"
                BatteryManager.BATTERY_STATUS_NOT_CHARGING -> "Not Charging"
                else -> "Unknown"
            }

            val batteryHealth: String = when (batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_STATUS)) {
                BatteryManager.BATTERY_HEALTH_COLD -> "Cold"
                BatteryManager.BATTERY_HEALTH_DEAD -> "Dead"
                BatteryManager.BATTERY_HEALTH_GOOD -> "Good"
                BatteryManager.BATTERY_HEALTH_OVERHEAT -> "Overheat"
                BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE -> "Over Voltage"
                BatteryManager.BATTERY_HEALTH_UNKNOWN -> "Unknown"
                BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE -> "Unspecified Failure"
                else -> "Unknown"
            }

            val intent = registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
            val temperature = intent?.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1) ?: -1
            val temperatureInCelsius = temperature / 10

            println("Battery Level: $batteryPercentage")
            println("Battery Health: $batteryHealth")
            println("Battery Status: $batteryStatus")
            println("Battery Temperature: $temperatureInCelsius")

            val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences?.edit()

            editor?.putString("battery_percentage_shutdown",batteryPercentage.toString())
            editor?.putString("battery_health",batteryHealth)
            editor?.putString("battery_status",batteryStatus)
            editor?.putString("battery_temperature",temperatureInCelsius.toString())

            editor?.apply()



        }


    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val shutDownFilter = IntentFilter(Intent.ACTION_SHUTDOWN)
        registerReceiver(shutdownReceiver,shutDownFilter)

        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        val savedTime = sharedPreferences.getString("boot_time", "N/A")
        val endTime = sharedPreferences.getString("shutdown_time","N/A")
        val batteryLevelS = sharedPreferences.getString("battery_percentage_shutdown","N/A")
        val batteryHealthS = sharedPreferences.getString("battery_health","N/A")
        val batteryStatusS = sharedPreferences.getString("battery_status","N/A")
        val batteryTemperatureS = sharedPreferences.getString("battery_temperature","N/A")

        val bootDateTime =  simplifyDateTime(savedTime.toString())
        val shutdownDateTime = simplifyDateTime(endTime.toString())

        binding.apply {
            txtBootTime.text = "${bootDateTime.first} (${bootDateTime.second})"
            shutdownTime.text = "${shutdownDateTime.first} (${shutdownDateTime.second})"
            batteryLevel.text = "$batteryLevelS %"
            batteryHealth.text = batteryHealthS
            batteryStatus.text = batteryStatusS
            batteryTemperature.text = "$batteryTemperatureS °Celsius"

            if (batteryLevelS!!.toInt() <20){
                batteryLevel.background.setTint(Color.RED)
            }
            if (batteryHealthS == "Dead"){
                batteryHealth.background.setTint(Color.RED)
            }
        }
    }

    private fun simplifyDateTime(dateTime: String): Pair<String,String>{

        // Convert datetime string to Date object
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault())
        val date = inputFormat.parse(dateTime)

        // Format Date object in 12-hour format
        val outputFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val timeString = date?.let { outputFormat.format(it) }
        val outFormatDate = SimpleDateFormat("dd-MM-yyyy",Locale.getDefault())
        val dateString = date?.let { outFormatDate.format(it) }

        return Pair(dateString.toString(),timeString.toString())
    }
}