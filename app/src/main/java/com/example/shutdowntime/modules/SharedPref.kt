package com.example.shutdowntime.modules

import android.content.Context
import android.content.SharedPreferences

 object SharedPref {


    fun sharedPrefManger(context: Context, data: Any, variable:String){
        val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        when (data) {
            is String -> {
                editor?.putString(variable, data)
            }
            is Float -> {
                editor?.putFloat(variable,data)
            }
            is Boolean -> {
                editor?.putBoolean(variable,data)
            }
            is Int -> {
                editor?.putInt(variable,data)
            }
            is Long -> {
                editor?.putLong(variable,data)
            }
        }
        editor?.apply()
    }

    fun getData(context: Context): SharedPreferences {
        return context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    }
}