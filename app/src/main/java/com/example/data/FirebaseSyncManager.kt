package com.example.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import android.util.Log

class FirebaseSyncManager {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    suspend fun createPairingCode(code: String) {
        val uid = auth.currentUser?.uid ?: return
        try {
            db.collection("pairing_codes").document(code).set(
                mapOf(
                    "parentUid" to uid,
                    "status" to "waiting",
                    "createdAt" to System.currentTimeMillis()
                )
            ).await()
        } catch (e: Exception) {
            Log.e("FirebaseSync", "Error creating pairing code", e)
        }
    }

    fun listenForPairingSuccess(code: String, onSuccess: (String) -> Unit) {
        db.collection("pairing_codes").document(code)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w("FirebaseSync", "Listen failed.", e)
                    return@addSnapshotListener
                }
                if (snapshot != null && snapshot.exists()) {
                    val status = snapshot.getString("status")
                    if (status == "linked") {
                        val childId = snapshot.getString("childDeviceId") ?: ""
                        onSuccess(childId)
                    }
                }
            }
    }

    suspend fun linkChildToParent(code: String, childName: String): Pair<Boolean, String> {
        return try {
            val doc = db.collection("pairing_codes").document(code).get().await()
            if (doc.exists() && doc.getString("status") == "waiting") {
                val parentUid = doc.getString("parentUid") ?: return Pair(false, "Invalid parent")
                
                // Update code status
                db.collection("pairing_codes").document(code).update(
                    mapOf(
                        "status" to "linked",
                        "childDeviceId" to childName 
                    )
                ).await()

                // Register child in parent's collection
                val childData = mapOf(
                    "name" to childName,
                    "status" to "online",
                    "battery" to 100,
                    "linkedAt" to System.currentTimeMillis()
                )
                db.collection("users").document(parentUid)
                  .collection("children").document(childName)
                  .set(childData).await()

                Pair(true, "")
            } else {
                Pair(false, "Code expired or already used")
            }
        } catch (e: Exception) {
            Log.e("FirebaseSync", "Error linking child", e)
            Pair(false, e.message ?: "Network error")
        }
    }
}
