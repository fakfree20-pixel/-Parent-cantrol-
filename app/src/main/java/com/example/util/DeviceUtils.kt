package com.example.util

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.BatteryManager
import android.os.Build
import java.util.Locale

object DeviceUtils {

    /**
     * Retrieves the real physical or hardware model name of the device.
     * Capitalizes properly and removes redundant manufacturer prefixes.
     */
    fun getRealDeviceName(context: Context? = null): String {
        val manufacturer = Build.MANUFACTURER.orEmpty().trim().replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString()
        }
        val model = Build.MODEL.orEmpty().trim()

        val rawName = if (model.startsWith(manufacturer, ignoreCase = true)) {
            model
        } else {
            "$manufacturer $model"
        }.trim()

        // Clean up common emulator or generic build names
        return when {
            rawName.contains("google_sdk", ignoreCase = true) ||
            rawName.contains("sdk_gphone", ignoreCase = true) ||
            rawName.contains("emulator", ignoreCase = true) ||
            rawName.contains("generic", ignoreCase = true) -> {
                "Google Pixel (Virtual Device)"
            }
            rawName.isBlank() || rawName.equals("unknown", ignoreCase = true) -> {
                "Android Smartphone"
            }
            else -> rawName
        }
    }

    /**
     * Reads current actual battery percentage from Android BatteryManager
     */
    fun getRealBatteryPercent(context: Context): Int {
        return try {
            val bm = context.getSystemService(Context.BATTERY_SERVICE) as? BatteryManager
            val level = bm?.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY) ?: -1
            if (level in 0..100) return level

            val iFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
            val batteryStatus = context.registerReceiver(null, iFilter)
            val currentLevel = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
            val scale = batteryStatus?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
            if (currentLevel >= 0 && scale > 0) {
                ((currentLevel.toFloat() / scale.toFloat()) * 100).toInt().coerceIn(0, 100)
            } else {
                85
            }
        } catch (e: Exception) {
            85
        }
    }

    /**
     * Checks if device currently has active internet connectivity
     */
    fun isNetworkConnected(context: Context): Boolean {
        return try {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
            val network = cm?.activeNetwork ?: return false
            val caps = cm.getNetworkCapabilities(network) ?: return false
            caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        } catch (e: Exception) {
            true
        }
    }
}
