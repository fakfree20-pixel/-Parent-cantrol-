package com.example.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.ActivityLogItem
import com.example.data.model.AppNotificationItem
import com.example.data.model.AppUsageRule
import com.example.data.model.CallLogItem
import com.example.data.model.ChildProfile
import com.example.data.model.GeofenceZone
import com.example.data.model.ScreenRewardTask
import com.example.data.model.SmsMessageItem
import com.example.data.model.UserAccount
import com.example.data.model.WebFilterRule
import com.example.data.model.WhatsAppConversation
import com.example.data.model.YouTubeWatchItem
import kotlinx.coroutines.flow.Flow

@Dao
interface ParentalControlDao {
    // Child Profiles
    @Query("SELECT * FROM child_profiles ORDER BY id ASC")
    fun getAllChildProfiles(): Flow<List<ChildProfile>>

    @Query("SELECT * FROM child_profiles WHERE id = :childId LIMIT 1")
    fun getChildProfileById(childId: Long): Flow<ChildProfile?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChildProfile(profile: ChildProfile): Long

    @Update
    suspend fun updateChildProfile(profile: ChildProfile)

    @Query("DELETE FROM child_profiles WHERE id = :childId")
    suspend fun deleteChildProfile(childId: Long)

    @Query("UPDATE child_profiles SET isLocked = :isLocked, lockReason = :reason, lockUntilTimestamp = :untilTime WHERE id = :childId")
    suspend fun setInstantLock(childId: Long, isLocked: Boolean, reason: String, untilTime: Long)

    @Query("UPDATE child_profiles SET blockAllApps = :blockAll WHERE id = :childId")
    suspend fun setBlockAllApps(childId: Long, blockAll: Boolean)

    @Query("UPDATE child_profiles SET antiUninstallEnabled = :enabled, preventSettingsAccess = :preventSettings, preventFactoryReset = :preventReset WHERE id = :childId")
    suspend fun setAntiUninstallProtection(childId: Long, enabled: Boolean, preventSettings: Boolean, preventReset: Boolean)

    @Query("UPDATE child_profiles SET bonusMinutesToday = bonusMinutesToday + :minutes WHERE id = :childId")
    suspend fun addBonusMinutes(childId: Long, minutes: Int)

    @Query("UPDATE child_profiles SET bonusMinutesToday = 0 WHERE id = :childId")
    suspend fun resetBonusMinutes(childId: Long)

    // App Usage & Rules
    @Query("SELECT * FROM app_usage_rules WHERE childId = :childId ORDER BY usageTodayMinutes DESC")
    fun getAppUsageRules(childId: Long): Flow<List<AppUsageRule>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAppUsageRules(rules: List<AppUsageRule>)

    @Update
    suspend fun updateAppUsageRule(rule: AppUsageRule)

    @Query("UPDATE app_usage_rules SET isBlocked = :isBlocked WHERE id = :ruleId")
    suspend fun toggleAppBlock(ruleId: Long, isBlocked: Boolean)

    @Query("UPDATE app_usage_rules SET dailyLimitMinutes = :limitMinutes WHERE id = :ruleId")
    suspend fun setAppLimit(ruleId: Long, limitMinutes: Int)

    @Query("UPDATE app_usage_rules SET isAlwaysAllowed = :isAlwaysAllowed WHERE id = :ruleId")
    suspend fun toggleAlwaysAllowed(ruleId: Long, isAlwaysAllowed: Boolean)

    @Query("UPDATE app_usage_rules SET usageTodayMinutes = usageTodayMinutes + :minutes WHERE id = :ruleId")
    suspend fun addAppUsageMinutes(ruleId: Long, minutes: Int)

    // Reward Tasks
    @Query("SELECT * FROM reward_tasks WHERE childId = :childId ORDER BY id DESC")
    fun getRewardTasks(childId: Long): Flow<List<ScreenRewardTask>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRewardTask(task: ScreenRewardTask): Long

    @Update
    suspend fun updateRewardTask(task: ScreenRewardTask)

    @Query("DELETE FROM reward_tasks WHERE id = :taskId")
    suspend fun deleteRewardTask(taskId: Long)

    // Activity Logs
    @Query("SELECT * FROM activity_logs WHERE childId = :childId ORDER BY timestamp DESC LIMIT 50")
    fun getActivityLogs(childId: Long): Flow<List<ActivityLogItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActivityLog(log: ActivityLogItem): Long

    @Query("DELETE FROM activity_logs WHERE childId = :childId")
    suspend fun clearActivityLogs(childId: Long)

    // Web Filter Rules
    @Query("SELECT * FROM web_filter_rules WHERE childId = :childId")
    fun getWebFilterRules(childId: Long): Flow<List<WebFilterRule>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWebFilterRule(rule: WebFilterRule): Long

    @Query("DELETE FROM web_filter_rules WHERE id = :ruleId")
    suspend fun deleteWebFilterRule(ruleId: Long)

    // Geofence Zones
    @Query("SELECT * FROM geofence_zones WHERE childId = :childId")
    fun getGeofenceZones(childId: Long): Flow<List<GeofenceZone>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGeofenceZone(zone: GeofenceZone): Long

    @Query("DELETE FROM geofence_zones WHERE id = :zoneId")
    suspend fun deleteGeofenceZone(zoneId: Long)

    // App Notifications Feed
    @Query("SELECT * FROM app_notifications WHERE childId = :childId ORDER BY timestamp DESC LIMIT 100")
    fun getAppNotifications(childId: Long): Flow<List<AppNotificationItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAppNotification(notification: AppNotificationItem): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAppNotifications(notifications: List<AppNotificationItem>)

    @Query("DELETE FROM app_notifications WHERE childId = :childId")
    suspend fun clearAppNotifications(childId: Long)

    // WhatsApp Conversations Tracker
    @Query("SELECT * FROM whatsapp_conversations WHERE childId = :childId ORDER BY id ASC")
    fun getWhatsAppConversations(childId: Long): Flow<List<WhatsAppConversation>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWhatsAppConversation(conversation: WhatsAppConversation): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWhatsAppConversations(conversations: List<WhatsAppConversation>)

    @Update
    suspend fun updateWhatsAppConversation(conversation: WhatsAppConversation)

    @Query("DELETE FROM whatsapp_conversations WHERE id = :convoId")
    suspend fun deleteWhatsAppConversation(convoId: Long)

    // Call Logs Tracker
    @Query("SELECT * FROM call_logs WHERE childId = :childId ORDER BY timestamp DESC LIMIT 100")
    fun getCallLogs(childId: Long): Flow<List<CallLogItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCallLog(callLog: CallLogItem): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCallLogs(callLogs: List<CallLogItem>)

    @Query("DELETE FROM call_logs WHERE childId = :childId")
    suspend fun clearCallLogs(childId: Long)

    @Query("DELETE FROM call_logs WHERE id = :callLogId")
    suspend fun deleteCallLog(callLogId: Long)

    // User Accounts & Authentication (ईमेल लॉगिन सिस्टम)
    @Query("SELECT * FROM user_accounts WHERE isLoggedIn = 1 LIMIT 1")
    fun getActiveUserAccount(): Flow<UserAccount?>

    @Query("SELECT * FROM user_accounts WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): UserAccount?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserAccount(user: UserAccount): Long

    @Update
    suspend fun updateUserAccount(user: UserAccount)

    @Query("UPDATE user_accounts SET isLoggedIn = 0")
    suspend fun logOutAllUsers()

    @Query("UPDATE user_accounts SET isLoggedIn = 1 WHERE id = :userId")
    suspend fun setLoggedInUser(userId: Long)

    // SMS Messages Tracker
    @Query("SELECT * FROM sms_messages WHERE childId = :childId ORDER BY timestamp DESC LIMIT 100")
    fun getSmsMessages(childId: Long): Flow<List<SmsMessageItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSmsMessage(message: SmsMessageItem): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSmsMessages(messages: List<SmsMessageItem>)

    @Query("DELETE FROM sms_messages WHERE childId = :childId")
    suspend fun clearSmsMessages(childId: Long)

    // YouTube Watch History Tracker
    @Query("SELECT * FROM youtube_watch_items WHERE childId = :childId ORDER BY timestamp DESC LIMIT 100")
    fun getYouTubeWatchHistory(childId: Long): Flow<List<YouTubeWatchItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertYouTubeWatchItem(item: YouTubeWatchItem): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertYouTubeWatchItems(items: List<YouTubeWatchItem>)

    @Query("DELETE FROM youtube_watch_items WHERE childId = :childId")
    suspend fun clearYouTubeWatchHistory(childId: Long)
}
