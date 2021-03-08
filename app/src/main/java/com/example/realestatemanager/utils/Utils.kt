package com.example.realestatemanager.utils

import android.content.Context
import android.net.ConnectivityManager

class Utils {

    fun isInternetAvailable(context: Context): Boolean{
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnected
    }
}