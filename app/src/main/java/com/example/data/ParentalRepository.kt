package com.example.data

import com.example.data.dao.ParentalControlDao
import com.example.data.model.ActivityLogItem
import com.example.data.model.AppNotificationItem
import com.example.data.model.AppUsageRule
import com.example.data.model.CallLogItem
import com.example.data.model.ChildProfile
import com.example.data.model.GeofenceZone
import com.example.data.model.ScreenRewardTask
import com.example.data.model.SmsMessageItem
import com.example.data.model.WebFilterRule
import com.example.data.model.WhatsAppConversation
import com.example.data.model.YouTubeWatchItem
import com.example.data.sync.FirebaseCloudSyncManager
import kotlinx.coroutines.flow.Flow

class ParentalRepository(private val dao: ParentalControlDao) {
    val allChildProfiles: Flow<List<ChildProfile>> = dao.getAllChildProfiles()

    fun getChildProfile(childId: Long): Flow<ChildProfile?> = dao.getChildProfileById(childId)

    fun getAppUsageRules(childId: Long): Flow<List<AppUsageRule>> = dao.getAppUsageRules(childId)

    fun getRewardTasks(childId: Long): Flow<List<ScreenRewardTask>> = dao.getRewardTasks(childId)

    fun getActivityLogs(childId: Long): Flow<List<ActivityLogItem>> = dao.getActivityLogs(childId)

    fun getWebFilterRules(childId: Long): Flow<List<WebFilterRule>> = dao.getWebFilterRules(childId)

    fun getGeofenceZones(childId: Long): Flow<List<GeofenceZone>> = dao.getGeofenceZones(childId)

    fun getAppNotifications(childId: Long): Flow<List<AppNotificationItem>> = dao.getAppNotifications(childId)

    fun getWhatsAppConversations(childId: Long): Flow<List<WhatsAppConversation>> = dao.getWhatsAppConversations(childId)

    fun getCallLogs(childId: Long): Flow<List<CallLogItem>> = dao.getCallLogs(childId)

    fun getSmsMessages(childId: Long): Flow<List<SmsMessageItem>> = dao.getSmsMessages(childId)

    fun getYouTubeWatchHistory(childId: Long): Flow<List<YouTubeWatchItem>> = dao.getYouTubeWatchHistory(childId)

    suspend fun insertSmsMessage(message: SmsMessageItem) = dao.insertSmsMessage(message)

    suspend fun clearSmsMessages(childId: Long) = dao.clearSmsMessages(childId)

    suspend fun insertYouTubeWatchItem(item: YouTubeWatchItem) = dao.insertYouTubeWatchItem(item)

    suspend fun clearYouTubeWatchHistory(childId: Long) = dao.clearYouTubeWatchHistory(childId)

    suspend fun insertActivityLog(log: ActivityLogItem) = dao.insertActivityLog(log)

    suspend fun insertChildProfile(profile: ChildProfile): Long {
        val childId = dao.insertChildProfile(profile)
        // Insert standard starter apps for the new child
        val defaultApps = listOf(
            AppUsageRule(childId = childId, packageName = "com.google.android.apps.youtube.kids", appName = "YouTube Kids", category = "ENTERTAINMENT", isBlocked = false, isAlwaysAllowed = false, dailyLimitMinutes = 30, usageTodayMinutes = 0, weeklyUsageMinutes = "0,0,0,0,0,0,0"),
            AppUsageRule(childId = childId, packageName = "org.khanacademy.android", appName = "Khan Academy", category = "EDUCATION", isBlocked = false, isAlwaysAllowed = true, dailyLimitMinutes = 0, usageTodayMinutes = 0, weeklyUsageMinutes = "0,0,0,0,0,0,0"),
            AppUsageRule(childId = childId, packageName = "com.duolingo", appName = "Duolingo", category = "EDUCATION", isBlocked = false, isAlwaysAllowed = true, dailyLimitMinutes = 0, usageTodayMinutes = 0, weeklyUsageMinutes = "0,0,0,0,0,0,0"),
            AppUsageRule(childId = childId, packageName = "com.google.android.dialer", appName = "Emergency Phone", category = "UTILITY", isBlocked = false, isAlwaysAllowed = true, dailyLimitMinutes = 0, usageTodayMinutes = 0, weeklyUsageMinutes = "0,0,0,0,0,0,0")
        )
        dao.insertAppUsageRules(defaultApps)
        return childId
    }

    suspend fun updateChildProfile(profile: ChildProfile) = dao.updateChildProfile(profile)

    suspend fun deleteChildProfile(childId: Long) = dao.deleteChildProfile(childId)

    suspend fun setInstantLock(childId: Long, isLocked: Boolean, reason: String = "Parent Lock", lockDurationMinutes: Int = 0) {
        val untilTime = if (lockDurationMinutes > 0) System.currentTimeMillis() + (lockDurationMinutes * 60 * 1000L) else 0L
        dao.setInstantLock(childId, isLocked, reason, untilTime)
        
        val title = if (isLocked) "Device Remote Locked" else "Device Unlocked"
        val titleHi = if (isLocked) "डिवाइस रिमोट लॉक किया गया" else "डिवाइस अनलॉक किया गया"
        val desc = if (isLocked) "Parent activated remote lock: $reason" else "Parent unlocked the device"
        val descHi = if (isLocked) "माता-पिता ने रिमोट लॉक लगाया: $reason" else "माता-पिता ने डिवाइस अनलॉक किया"
        val log = ActivityLogItem(
            childId = childId,
            type = "INSTANT_LOCK",
            title = title,
            titleHindi = titleHi,
            description = desc,
            descriptionHindi = descHi
        )
        dao.insertActivityLog(log)
        FirebaseCloudSyncManager.sendRemoteCommand(
            commandType = if (isLocked) "REMOTE_LOCK" else "REMOTE_UNLOCK",
            payload = mapOf("reason" to reason, "untilTime" to untilTime)
        )
        FirebaseCloudSyncManager.uploadActivityLog(log)
    }

    suspend fun setBlockAllApps(childId: Long, blockAll: Boolean) {
        dao.setBlockAllApps(childId, blockAll)
        val title = if (blockAll) "Block All Apps Activated" else "Block All Apps Deactivated"
        val titleHi = if (blockAll) "सभी ऐप्स ब्लॉक चालू" else "सभी ऐप्स ब्लॉक बंद"
        val desc = if (blockAll) "Only whitelisted Allowed Apps can be opened" else "Normal app restrictions restored"
        val descHi = if (blockAll) "केवल स्वीकृत ऐप्स ही खोले जा सकेंगे" else "सामान्य ऐप नियम पुनः बहाल"
        val log = ActivityLogItem(
            childId = childId,
            type = "APP_BLOCKED",
            title = title,
            titleHindi = titleHi,
            description = desc,
            descriptionHindi = descHi
        )
        dao.insertActivityLog(log)
        FirebaseCloudSyncManager.sendRemoteCommand(
            commandType = if (blockAll) "BLOCK_ALL_APPS" else "UNBLOCK_ALL_APPS"
        )
        FirebaseCloudSyncManager.uploadActivityLog(log)
    }

    suspend fun setAntiUninstallProtection(childId: Long, enabled: Boolean, preventSettings: Boolean, preventReset: Boolean) {
        dao.setAntiUninstallProtection(childId, enabled, preventSettings, preventReset)
        dao.insertActivityLog(
            ActivityLogItem(
                childId = childId,
                type = "ANTI_UNINSTALL",
                title = if (enabled) "Anti-Uninstall Lock Activated" else "Anti-Uninstall Lock Disabled",
                titleHindi = if (enabled) "अनइंस्टॉल सुरक्षा लॉक सक्रिय" else "अनइंस्टॉल सुरक्षा बंद",
                description = if (enabled) "Device Admin and Settings protection enabled" else "Uninstall protection turned off",
                descriptionHindi = if (enabled) "डिवाइस एडमिन व सेटिंग्स सुरक्षा चालू है, बच्चा ऐप नहीं हटा सकता" else "अनइंस्टॉल सुरक्षा निष्क्रिय की गई"
            )
        )
    }

    suspend fun addBonusMinutes(childId: Long, minutes: Int, reason: String = "Bonus Screen Time") {
        dao.addBonusMinutes(childId, minutes)
        dao.insertActivityLog(
            ActivityLogItem(
                childId = childId,
                type = "BONUS_TIME",
                title = "+$minutes Mins Added",
                titleHindi = "+$minutes मिनट अतिरिक्त मिले",
                description = "Parent granted extra time: $reason",
                descriptionHindi = "माता-पिता ने अतिरिक्त समय दिया: $reason"
            )
        )
    }

    suspend fun toggleAppBlock(ruleId: Long, childId: Long, appName: String, isBlocked: Boolean) {
        dao.toggleAppBlock(ruleId, isBlocked)
        dao.insertActivityLog(
            ActivityLogItem(
                childId = childId,
                type = "APP_BLOCKED",
                title = if (isBlocked) "$appName Blocked" else "$appName Unblocked",
                titleHindi = if (isBlocked) "$appName को ब्लॉक किया गया" else "$appName को अनब्लॉक किया गया",
                description = if (isBlocked) "Access restricted for child" else "Access restored for child",
                descriptionHindi = if (isBlocked) "बच्चे के लिए पहुंच प्रतिबंधित" else "बच्चे के लिए पहुंच बहाल"
            )
        )
    }

    suspend fun setAppLimit(ruleId: Long, limitMinutes: Int) {
        dao.setAppLimit(ruleId, limitMinutes)
    }

    suspend fun toggleAlwaysAllowed(ruleId: Long, isAlwaysAllowed: Boolean) {
        dao.toggleAlwaysAllowed(ruleId, isAlwaysAllowed)
    }

    suspend fun addAppUsageMinutes(ruleId: Long, minutes: Int) {
        dao.addAppUsageMinutes(ruleId, minutes)
    }

    suspend fun insertRewardTask(task: ScreenRewardTask) = dao.insertRewardTask(task)

    suspend fun completeRewardTaskByChild(task: ScreenRewardTask) {
        val updated = task.copy(isCompleted = true, completedDate = "Today")
        dao.updateRewardTask(updated)
        dao.insertActivityLog(
            ActivityLogItem(
                childId = task.childId,
                type = "REQUEST_TIME",
                title = "Task Marked Completed: ${task.title}",
                titleHindi = "कार्य पूरा चिह्नित: ${if (task.titleHindi.isNotEmpty()) task.titleHindi else task.title}",
                description = "Child requested +${task.rewardMinutes} mins reward approval",
                descriptionHindi = "बच्चे ने +${task.rewardMinutes} मिनट इनाम की स्वीकृति मांगी"
            )
        )
    }

    suspend fun approveRewardTask(task: ScreenRewardTask) {
        val updated = task.copy(isApproved = true)
        dao.updateRewardTask(updated)
        dao.addBonusMinutes(task.childId, task.rewardMinutes)
        dao.insertActivityLog(
            ActivityLogItem(
                childId = task.childId,
                type = "BONUS_TIME",
                title = "+${task.rewardMinutes} Mins Reward Approved",
                titleHindi = "+${task.rewardMinutes} मिनट इनाम स्वीकृत",
                description = "Approved reward for: ${task.title}",
                descriptionHindi = "कार्य के लिए इनाम स्वीकृत: ${if (task.titleHindi.isNotEmpty()) task.titleHindi else task.title}"
            )
        )
    }

    suspend fun deleteRewardTask(taskId: Long) = dao.deleteRewardTask(taskId)

    suspend fun addActivityLog(log: ActivityLogItem) = dao.insertActivityLog(log)

    suspend fun clearLogs(childId: Long) = dao.clearActivityLogs(childId)

    suspend fun insertWebFilterRule(rule: WebFilterRule) = dao.insertWebFilterRule(rule)

    suspend fun deleteWebFilterRule(ruleId: Long) = dao.deleteWebFilterRule(ruleId)

    suspend fun insertGeofenceZone(zone: GeofenceZone) = dao.insertGeofenceZone(zone)

    suspend fun deleteGeofenceZone(zoneId: Long) = dao.deleteGeofenceZone(zoneId)

    suspend fun insertAppNotification(notification: AppNotificationItem) = dao.insertAppNotification(notification)

    suspend fun clearAppNotifications(childId: Long) = dao.clearAppNotifications(childId)

    suspend fun insertWhatsAppConversation(conversation: WhatsAppConversation) = dao.insertWhatsAppConversation(conversation)

    suspend fun deleteWhatsAppConversation(convoId: Long) = dao.deleteWhatsAppConversation(convoId)

    suspend fun insertCallLog(callLog: CallLogItem) = dao.insertCallLog(callLog)

    suspend fun clearCallLogs(childId: Long) = dao.clearCallLogs(childId)

    suspend fun deleteCallLog(callLogId: Long) = dao.deleteCallLog(callLogId)

    // User Authentication & Email Login System
    val currentUserAccount: Flow<com.example.data.model.UserAccount?> = dao.getActiveUserAccount()

    suspend fun loginWithEmail(email: String, password: String): Boolean {
        dao.logOutAllUsers()
        val existing = dao.getUserByEmail(email.trim().lowercase())
        if (existing != null) {
            if (existing.passwordHash.isNotBlank() && password.isNotBlank() && existing.passwordHash != password) {
                return false
            }
            dao.updateUserAccount(existing.copy(isLoggedIn = true))
            return true
        } else {
            return false
        }
    }

    suspend fun signUpWithEmail(name: String, email: String, password: String, phone: String = "", antiUninstallPin: String = "1234"): Boolean {
        dao.logOutAllUsers()
        val existing = dao.getUserByEmail(email.trim().lowercase())
        if (existing != null) {
            dao.updateUserAccount(existing.copy(name = name, passwordHash = password, isLoggedIn = true, phoneNumber = phone, antiUninstallPin = antiUninstallPin.ifBlank { existing.antiUninstallPin }))
        } else {
            dao.insertUserAccount(
                com.example.data.model.UserAccount(
                    name = name.ifBlank { "Parent" },
                    email = email.trim().lowercase(),
                    passwordHash = password,
                    isLoggedIn = true,
                    phoneNumber = phone,
                    antiUninstallPin = antiUninstallPin.ifBlank { "1234" },
                    role = "PARENT",
                    authProvider = "EMAIL"
                )
            )
        }
        return true
    }



    suspend fun loginWithGoogle(email: String, name: String, photoUrl: String = ""): Boolean {
        dao.logOutAllUsers()
        val existing = dao.getUserByEmail(email.trim().lowercase())
        if (existing != null) {
            dao.updateUserAccount(existing.copy(name = name, photoUrl = photoUrl, isLoggedIn = true, authProvider = "GOOGLE"))
        } else {
            dao.insertUserAccount(
                com.example.data.model.UserAccount(
                    name = name,
                    email = email.trim().lowercase(),
                    photoUrl = photoUrl,
                    isLoggedIn = true,
                    role = "PARENT",
                    authProvider = "GOOGLE"
                )
            )
        }
        return true
    }

    suspend fun updateActiveUserPin(newPin: String): Boolean {
        dao.updateActiveUserPin(newPin)
        dao.updateAllUserPin(newPin)
        return true
    }

    suspend fun logoutUser() {
        dao.logOutAllUsers()
    }
}
