package com.example.data.sync

import android.util.Log
import com.example.data.model.ActivityLogItem
import com.example.data.model.AppNotificationItem
import com.example.data.model.AppUsageRule
import com.example.data.model.ChildProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await

/**
 * Firebase Cloud Sync Manager
 * Provides real-time bidirectional synchronization between Parent & Child devices
 * via Firebase Cloud Firestore with automatic offline fallback and local persistence.
 */
object FirebaseCloudSyncManager {

    private const val TAG = "FirebaseCloudSync"
    private const val COLLECTION_DEVICES = "paired_devices"
    private const val SUB_COLLECTION_LOGS = "activity_logs"
    private const val SUB_COLLECTION_NOTIFS = "notifications"
    private const val SUB_COLLECTION_COMMANDS = "remote_commands"

    private val firestore: FirebaseFirestore? by lazy {
        try {
            FirebaseFirestore.getInstance()
        } catch (e: Exception) {
            Log.w(TAG, "Firestore initialization fallback: ${e.message}")
            null
        }
    }

    private var activeDeviceListener: ListenerRegistration? = null
    private var activeCommandListener: ListenerRegistration? = null

    private suspend fun ensureAuthenticated() {
        try {
            val auth = FirebaseAuth.getInstance()
            if (auth.currentUser == null) {
                auth.signInAnonymously().await()
                Log.d(TAG, "Signed in anonymously to Firebase")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to sign in anonymously", e)
        }
    }

    /**
     * Pushes current Child profile & lock status to Cloud Firestore
     */
    suspend fun syncChildProfileToCloud(child: ChildProfile, pairingCode: String = "9839247105") {
        ensureAuthenticated()
        val db = firestore ?: return
        try {
            val deviceData = hashMapOf(
                "childId" to child.id,
                "childName" to child.name,
                "deviceName" to child.deviceModel,
                "pairingCode" to pairingCode,
                "batteryPercent" to child.batteryPercent,
                "isDeviceOnline" to child.isDeviceOnline,
                "isLocked" to child.isLocked,
                "lockReason" to child.lockReason,
                "lockUntilTimestamp" to child.lockUntilTimestamp,
                "blockAllApps" to child.blockAllApps,
                "antiUninstallEnabled" to child.antiUninstallEnabled,
                "preventSettingsAccess" to child.preventSettingsAccess,
                "preventFactoryReset" to child.preventFactoryReset,
                "weekdayLimitMinutes" to child.weekdayLimitMinutes,
                "weekendLimitMinutes" to child.weekendLimitMinutes,
                "bonusMinutesToday" to child.bonusMinutesToday,
                "locationAddress" to child.locationAddress,
                "locationCoordinates" to child.locationCoordinates,
                "geofenceStatus" to child.geofenceStatus,
                "lastSeenTimestamp" to System.currentTimeMillis()
            )

            db.collection(COLLECTION_DEVICES)
                .document(pairingCode)
                .set(deviceData, SetOptions.merge())
                .await()
            Log.d(TAG, "Successfully synced child profile to cloud for pair code: $pairingCode")
        } catch (e: Exception) {
            Log.w(TAG, "Cloud sync profile notice: ${e.message}")
        }
    }

    /**
     * Pushes a Remote Action Command to the child device (e.g. LOCK, UNLOCK, BONUS_TIME, TAKE_SNAP)
     */
    suspend fun sendRemoteCommand(
        pairingCode: String = "9839247105",
        commandType: String,
        payload: Map<String, Any> = emptyMap()
    ) {
        ensureAuthenticated()
        val db = firestore ?: return
        try {
            val commandData = hashMapOf(
                "command" to commandType,
                "timestamp" to System.currentTimeMillis(),
                "status" to "PENDING",
                "payload" to payload
            )
            db.collection(COLLECTION_DEVICES)
                .document(pairingCode)
                .collection(SUB_COLLECTION_COMMANDS)
                .document()
                .set(commandData)
                .await()
            Log.d(TAG, "Sent remote command: $commandType to device $pairingCode")
        } catch (e: Exception) {
            Log.w(TAG, "Failed to send remote command: ${e.message}")
        }
    }

    /**
     * Real-time listener for Child Status updates from Cloud
     */
    fun startListeningToChildDevice(
        pairingCode: String = "9839247105",
        onDeviceUpdate: (Map<String, Any>) -> Unit
    ) {
        // Run auth in background since this isn't a suspend function, 
        // but it doesn't matter much for snapshot listener which reconnects.
        val db = firestore ?: return
        stopListening()
        
        try {
            FirebaseAuth.getInstance().signInAnonymously()
        } catch(e: Exception) {}

        try {
            activeDeviceListener = db.collection(COLLECTION_DEVICES)
                .document(pairingCode)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        Log.w(TAG, "Listen error: ${error.message}")
                        return@addSnapshotListener
                    }
                    if (snapshot != null && snapshot.exists()) {
                        val data = snapshot.data
                        if (data != null) {
                            onDeviceUpdate(data)
                        }
                    }
                }
        } catch (e: Exception) {
            Log.w(TAG, "Could not attach snapshot listener: ${e.message}")
        }
    }

    /**
     * Uploads an Activity Log item to Cloud
     */
    suspend fun uploadActivityLog(log: ActivityLogItem, pairingCode: String = "9839247105") {
        ensureAuthenticated()
        val db = firestore ?: return
        try {
            val logData = hashMapOf(
                "title" to log.title,
                "titleHindi" to log.titleHindi,
                "description" to log.description,
                "descriptionHindi" to log.descriptionHindi,
                "type" to log.type,
                "timestamp" to log.timestamp
            )
            db.collection(COLLECTION_DEVICES)
                .document(pairingCode)
                .collection(SUB_COLLECTION_LOGS)
                .document()
                .set(logData)
                .await()
        } catch (e: Exception) {
            Log.w(TAG, "Failed to upload activity log: ${e.message}")
        }
    }

    /**
     * Uploads an App Notification item to Cloud
     */
    suspend fun uploadNotification(notif: AppNotificationItem, pairingCode: String = "9839247105") {
        ensureAuthenticated()
        val db = firestore ?: return
        try {
            val notifData = hashMapOf(
                "appName" to notif.appName,
                "packageName" to notif.packageName,
                "senderName" to notif.senderName,
                "messageContent" to notif.messageContent,
                "timestamp" to notif.timestamp,
                "isSuspiciousKeyword" to notif.isSuspiciousKeyword,
                "category" to notif.category
            )
            db.collection(COLLECTION_DEVICES)
                .document(pairingCode)
                .collection(SUB_COLLECTION_NOTIFS)
                .document()
                .set(notifData)
                .await()
        } catch (e: Exception) {
            Log.w(TAG, "Failed to upload notification: ${e.message}")
        }
    }

    /**
     * Stop active listeners
     */
    fun stopListening() {
        activeDeviceListener?.remove()
        activeDeviceListener = null
        activeCommandListener?.remove()
        activeCommandListener = null
    }
}
