package com.example.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.data.sync.FirebaseCloudSyncManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.*

class ChildMonitoringService : Service() {

    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var syncJob: Job? = null

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel()

        val notification: Notification = NotificationCompat.Builder(this, "child_monitor_channel")
            .setContentTitle("parent cantrol md - Child Guard")
            .setContentText("Safe Child Device Connected & Monitoring Active (सुरक्षा सक्रिय है)")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)
            .build()

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                    startForeground(1001, notification, android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC)
                } else {
                    startForeground(1001, notification)
                }
            } else {
                startForeground(1001, notification)
            }
        } catch (e: Throwable) {
            Log.e("ChildMonitoringService", "Safe startForeground catch: ${e.message}")
            try {
                startForeground(1001, notification)
            } catch (ignored: Throwable) {}
        }
        startTelemetrySync()

        return START_STICKY
    }

    private fun startTelemetrySync() {
        syncJob?.cancel()
        syncJob = serviceScope.launch {
            while (isActive) {
                try {
                    val prefs = getSharedPreferences("parental_control_prefs", Context.MODE_PRIVATE)
                    val pairingCode = prefs.getString("child_pairing_code", null)
                        ?: prefs.getString("unique_pairing_code", null)

                    if (!pairingCode.isNullOrBlank()) {
                        val batteryPercent = getBatteryPercentage()
                        val deviceModel = "${Build.MANUFACTURER} ${Build.MODEL}"

                        val db = FirebaseFirestore.getInstance()
                        val updateMap = mapOf(
                            "batteryPercent" to batteryPercent,
                            "deviceName" to deviceModel,
                            "isDeviceOnline" to true,
                            "lastSeenTimestamp" to System.currentTimeMillis()
                        )

                        db.collection("paired_devices").document(pairingCode)
                            .set(updateMap, SetOptions.merge())
                        Log.d("ChildMonitoringService", "Synced telemetry to code: $pairingCode (Battery: $batteryPercent%)")
                    }
                } catch (e: Exception) {
                    Log.w("ChildMonitoringService", "Sync error: ${e.message}")
                }
                delay(30000) // Update every 30 seconds
            }
        }
    }

    private fun getBatteryPercentage(): Int {
        return try {
            val bm = getSystemService(Context.BATTERY_SERVICE) as? BatteryManager
            val level = bm?.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY) ?: -1
            if (level in 0..100) return level

            val iFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
            val batteryStatus = registerReceiver(null, iFilter)
            val rawLevel = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
            val scale = batteryStatus?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
            if (rawLevel >= 0 && scale > 0) {
                ((rawLevel / scale.toFloat()) * 100).toInt()
            } else 85
        } catch (e: Exception) {
            85
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                "child_monitor_channel",
                "Child Monitoring Service",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Runs background safety monitor for parental control"
            }
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(serviceChannel)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        syncJob?.cancel()
        serviceScope.cancel()
    }
}
