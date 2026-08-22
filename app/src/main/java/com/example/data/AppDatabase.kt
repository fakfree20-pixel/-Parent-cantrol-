package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.dao.ParentalControlDao
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        ChildProfile::class,
        AppUsageRule::class,
        ScreenRewardTask::class,
        ActivityLogItem::class,
        WebFilterRule::class,
        GeofenceZone::class,
        AppNotificationItem::class,
        WhatsAppConversation::class,
        CallLogItem::class,
        UserAccount::class,
        SmsMessageItem::class,
        YouTubeWatchItem::class
    ],
    version = 6,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun parentalControlDao(): ParentalControlDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "parent_guard_db"
                ).fallbackToDestructiveMigration()
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        CoroutineScope(Dispatchers.IO).launch {
                            INSTANCE?.let { database ->
                                populateInitialData(database.parentalControlDao())
                            }
                        }
                    }
                }).build()
                INSTANCE = instance
                instance
            }
        }

        suspend fun populateInitialData(dao: ParentalControlDao) {
            // Child 1: Infinix X6823C (Aarav - Age 9)
            val aaravId = dao.insertChildProfile(
                ChildProfile(
                    name = "Aarav",
                    age = 9,
                    avatarIndex = 0,
                    deviceModel = "Infinix X6823C",
                    batteryPercent = 86,
                    isDeviceOnline = true,
                    blockAllApps = false,
                    locationAddress = "Green Valley International School, Sector 14",
                    locationCoordinates = "28.6139° N, 77.2090° E",
                    geofenceStatus = "Inside School Safe Zone",
                    weekdayLimitMinutes = 120, // 2h 00m
                    weekendLimitMinutes = 180, // 3h 00m
                    isLocked = false,
                    bedtimeEnabled = true,
                    bedtimeStartHour = 21,
                    bedtimeStartMinute = 0,
                    bedtimeEndHour = 7,
                    bedtimeEndMinute = 0,
                    schoolModeEnabled = false,
                    schoolStartHour = 8,
                    schoolEndHour = 14,
                    webSafeSearch = true,
                    webBlockAdult = true,
                    webBlockGaming = false,
                    webBlockSocial = true,
                    bonusMinutesToday = 15,
                    antiUninstallEnabled = true,
                    deviceAdminActive = true,
                    preventSettingsAccess = true,
                    preventFactoryReset = true
                )
            )

            // Child 2: Ananya (Galaxy Tab A8)
            val ananyaId = dao.insertChildProfile(
                ChildProfile(
                    name = "Ananya",
                    age = 6,
                    avatarIndex = 1,
                    deviceModel = "Galaxy Tab A8",
                    batteryPercent = 74,
                    isDeviceOnline = true,
                    blockAllApps = false,
                    locationAddress = "Home, 42 Palm Meadows",
                    locationCoordinates = "28.6120° N, 77.2050° E",
                    geofenceStatus = "Inside Home Safe Zone",
                    weekdayLimitMinutes = 60, // 1h 00m
                    weekendLimitMinutes = 90, // 1h 30m
                    isLocked = false,
                    bedtimeEnabled = true,
                    bedtimeStartHour = 20,
                    bedtimeStartMinute = 0,
                    bedtimeEndHour = 7,
                    bedtimeEndMinute = 30,
                    schoolModeEnabled = false,
                    schoolStartHour = 9,
                    schoolEndHour = 13,
                    webSafeSearch = true,
                    webBlockAdult = true,
                    webBlockGaming = false,
                    webBlockSocial = true,
                    bonusMinutesToday = 0,
                    antiUninstallEnabled = true,
                    deviceAdminActive = true,
                    preventSettingsAccess = true,
                    preventFactoryReset = true
                )
            )

            // Seed Apps for Aarav
            val aaravApps = listOf(
                AppUsageRule(childId = aaravId, packageName = "com.google.android.apps.youtube.kids", appName = "YouTube Kids", category = "ENTERTAINMENT", isBlocked = false, isAlwaysAllowed = false, dailyLimitMinutes = 45, usageTodayMinutes = 35, weeklyUsageMinutes = "40,45,30,50,45,60,55"),
                AppUsageRule(childId = aaravId, packageName = "com.roblox.client", appName = "Roblox", category = "GAMES", isBlocked = false, isAlwaysAllowed = false, dailyLimitMinutes = 30, usageTodayMinutes = 28, weeklyUsageMinutes = "30,25,20,30,30,45,50"),
                AppUsageRule(childId = aaravId, packageName = "com.kiloo.subwaysurf", appName = "Subway Surfers", category = "GAMES", isBlocked = false, isAlwaysAllowed = false, dailyLimitMinutes = 20, usageTodayMinutes = 15, weeklyUsageMinutes = "15,20,10,15,20,25,30"),
                AppUsageRule(childId = aaravId, packageName = "com.duolingo", appName = "Duolingo Kids", category = "EDUCATION", isBlocked = false, isAlwaysAllowed = true, dailyLimitMinutes = 0, usageTodayMinutes = 20, weeklyUsageMinutes = "20,20,20,20,20,20,20"),
                AppUsageRule(childId = aaravId, packageName = "org.khanacademy.android", appName = "Khan Academy Kids", category = "EDUCATION", isBlocked = false, isAlwaysAllowed = true, dailyLimitMinutes = 0, usageTodayMinutes = 25, weeklyUsageMinutes = "30,25,25,30,20,15,20"),
                AppUsageRule(childId = aaravId, packageName = "com.android.chrome", appName = "Chrome Browser", category = "PRODUCTIVITY", isBlocked = false, isAlwaysAllowed = false, dailyLimitMinutes = 30, usageTodayMinutes = 12, weeklyUsageMinutes = "15,20,10,12,18,25,20"),
                AppUsageRule(childId = aaravId, packageName = "com.mojang.minecraftpe", appName = "Minecraft", category = "GAMES", isBlocked = true, isAlwaysAllowed = false, dailyLimitMinutes = 30, usageTodayMinutes = 0, weeklyUsageMinutes = "0,0,0,0,0,30,40"),
                AppUsageRule(childId = aaravId, packageName = "com.whatsapp", appName = "WhatsApp", category = "SOCIAL", isBlocked = false, isAlwaysAllowed = false, dailyLimitMinutes = 20, usageTodayMinutes = 10, weeklyUsageMinutes = "10,12,8,15,10,15,14"),
                AppUsageRule(childId = aaravId, packageName = "com.google.android.dialer", appName = "Phone & Emergency", category = "UTILITY", isBlocked = false, isAlwaysAllowed = true, dailyLimitMinutes = 0, usageTodayMinutes = 4, weeklyUsageMinutes = "5,3,4,6,2,5,4"),
                AppUsageRule(childId = aaravId, packageName = "com.google.android.calculator", appName = "Calculator", category = "UTILITY", isBlocked = false, isAlwaysAllowed = true, dailyLimitMinutes = 0, usageTodayMinutes = 3, weeklyUsageMinutes = "5,2,4,3,5,2,1")
            )
            dao.insertAppUsageRules(aaravApps)

            // Seed Apps for Ananya
            val ananyaApps = listOf(
                AppUsageRule(childId = ananyaId, packageName = "com.google.android.apps.youtube.kids", appName = "YouTube Kids", category = "ENTERTAINMENT", isBlocked = false, isAlwaysAllowed = false, dailyLimitMinutes = 30, usageTodayMinutes = 22, weeklyUsageMinutes = "20,25,30,20,25,35,40"),
                AppUsageRule(childId = ananyaId, packageName = "org.khanacademy.android", appName = "Khan Academy Kids", category = "EDUCATION", isBlocked = false, isAlwaysAllowed = true, dailyLimitMinutes = 0, usageTodayMinutes = 18, weeklyUsageMinutes = "15,20,20,15,20,15,15"),
                AppUsageRule(childId = ananyaId, packageName = "com.tocaboca.tocalifeworld", appName = "Toca Life World", category = "GAMES", isBlocked = false, isAlwaysAllowed = false, dailyLimitMinutes = 20, usageTodayMinutes = 15, weeklyUsageMinutes = "20,15,15,20,15,25,30"),
                AppUsageRule(childId = ananyaId, packageName = "com.google.android.dialer", appName = "Phone & Emergency", category = "UTILITY", isBlocked = false, isAlwaysAllowed = true, dailyLimitMinutes = 0, usageTodayMinutes = 2, weeklyUsageMinutes = "2,1,3,2,1,2,3")
            )
            dao.insertAppUsageRules(ananyaApps)

            // Seed Tasks for Aarav
            val tasks = listOf(
                ScreenRewardTask(childId = aaravId, title = "Complete Math Homework", titleHindi = "गणित का होमवर्क पूरा करें", rewardMinutes = 20, isCompleted = true, isApproved = true, completedDate = "Today"),
                ScreenRewardTask(childId = aaravId, title = "Read English Storybook for 20 mins", titleHindi = "20 मिनट किताब पढ़ें", rewardMinutes = 15, isCompleted = true, isApproved = false, completedDate = "Today"),
                ScreenRewardTask(childId = aaravId, title = "Clean Study Table & School Bag", titleHindi = "स्टडी टेबल और बैग साफ करें", rewardMinutes = 10, isCompleted = false, isApproved = false, completedDate = ""),
                ScreenRewardTask(childId = aaravId, title = "Evening Cycling / 30m Outdoor Play", titleHindi = "शाम का आउटडोर खेल (30 मिनट)", rewardMinutes = 30, isCompleted = false, isApproved = false, completedDate = "")
            )
            tasks.forEach { dao.insertRewardTask(it) }

            // Seed Activity Logs for Aarav
            val logs = listOf(
                ActivityLogItem(childId = aaravId, timestamp = System.currentTimeMillis() - 1000 * 60 * 15, type = "BONUS_TIME", title = "+15 min Bonus Approved", titleHindi = "+15 मिनट बोनस स्वीकृत", description = "Parent approved bonus for Math Homework", descriptionHindi = "गणित होमवर्क के लिए माता-पिता ने समय बढ़ाया"),
                ActivityLogItem(childId = aaravId, timestamp = System.currentTimeMillis() - 1000 * 60 * 35, type = "GEOFENCE", title = "Entered School Zone", titleHindi = "स्कूल क्षेत्र में पहुंचे", description = "Aarav arrived safely at Green Valley School", descriptionHindi = "आरव ग्रीन वैली स्कूल सुरक्षित पहुंचे"),
                ActivityLogItem(childId = aaravId, timestamp = System.currentTimeMillis() - 1000 * 60 * 60, type = "LIMIT_ALERT", title = "Roblox Daily Limit Reached", titleHindi = "Roblox की दैनिक सीमा समाप्त", description = "Reached 28m / 30m daily allowance", descriptionHindi = "दैनिक सीमा 30 मिनट में से 28 मिनट उपयोग हुआ"),
                ActivityLogItem(childId = aaravId, timestamp = System.currentTimeMillis() - 1000 * 60 * 120, type = "WEB_FILTER", title = "SafeSearch Protected", titleHindi = "सुरक्षित खोज सक्रिय", description = "Blocked 1 flagged search query automatically", descriptionHindi = "असुरक्षित वेब खोज को स्वतः ब्लॉक किया गया"),
                ActivityLogItem(childId = aaravId, timestamp = System.currentTimeMillis() - 1000 * 60 * 240, type = "ANTI_UNINSTALL", title = "Anti-Uninstall Lock Active", titleHindi = "अनइंस्टॉल सुरक्षा लॉक सक्रिय", description = "Device Admin prevented unauthorized removal", descriptionHindi = "डिवाइस एडमिन द्वारा ऐप हटाने से रोका गया")
            )
            logs.forEach { dao.insertActivityLog(it) }

            // Seed Web Rules
            val webRules = listOf(
                WebFilterRule(childId = aaravId, domain = "tiktok.com", isBlocked = true, category = "Social Media"),
                WebFilterRule(childId = aaravId, domain = "twitch.tv", isBlocked = true, category = "Streaming"),
                WebFilterRule(childId = aaravId, domain = "discord.com", isBlocked = true, category = "Social Media")
            )
            webRules.forEach { dao.insertWebFilterRule(it) }

            // Seed Geofence Safe Zones
            val geofenceZones = listOf(
                GeofenceZone(childId = aaravId, name = "School", nameHindi = "स्कूल", address = "Green Valley School, Sector 14", radiusMeters = 300, isSafeZone = true),
                GeofenceZone(childId = aaravId, name = "Home", nameHindi = "घर", address = "42 Palm Meadows, Block C", radiusMeters = 200, isSafeZone = true),
                GeofenceZone(childId = aaravId, name = "Tuition / Sports Club", nameHindi = "ट्यूशन / स्पोर्ट्स क्लब", address = "City Sports Complex, Arena 2", radiusMeters = 250, isSafeZone = true)
            )
            geofenceZones.forEach { dao.insertGeofenceZone(it) }

            // Seed WhatsApp Chat Conversations for Aarav
            val whatsAppChats = listOf(
                WhatsAppConversation(
                    childId = aaravId,
                    contactName = "Rohan (Class 8 Friend)",
                    phoneNumber = "+91 98112 34567",
                    lastMessage = "Did you finish Chapter 5 Science notes?",
                    lastMessageTime = "10:35 AM",
                    unreadCount = 0,
                    avatarColorIndex = 0,
                    isFlaggedSuspicious = false,
                    totalMessagesToday = 14,
                    messagesJson = "[{\"sender\":\"Rohan\",\"text\":\"Hey Aarav, did you do the science homework?\",\"time\":\"10:30 AM\",\"isChild\":false},{\"sender\":\"Aarav\",\"text\":\"Yes, doing question 4 now.\",\"time\":\"10:32 AM\",\"isChild\":true},{\"sender\":\"Rohan\",\"text\":\"Did you finish Chapter 5 Science notes?\",\"time\":\"10:35 AM\",\"isChild\":false}]"
                ),
                WhatsAppConversation(
                    childId = aaravId,
                    contactName = "Class 8 Science Study Group",
                    phoneNumber = "Group (24 members)",
                    lastMessage = "Amit Sir: Tomorrow test on Plants & Photosynthesis",
                    lastMessageTime = "09:48 AM",
                    unreadCount = 2,
                    avatarColorIndex = 1,
                    isFlaggedSuspicious = false,
                    totalMessagesToday = 32,
                    messagesJson = "[{\"sender\":\"Amit Sir\",\"text\":\"Tomorrow test on Plants & Photosynthesis\",\"time\":\"09:48 AM\",\"isChild\":false}]"
                ),
                WhatsAppConversation(
                    childId = aaravId,
                    contactName = "Priya (Sister / Cousin)",
                    phoneNumber = "+91 97234 56789",
                    lastMessage = "Are you coming for chess this Sunday?",
                    lastMessageTime = "Yesterday",
                    unreadCount = 0,
                    avatarColorIndex = 2,
                    isFlaggedSuspicious = false,
                    totalMessagesToday = 4,
                    messagesJson = "[{\"sender\":\"Priya\",\"text\":\"Are you coming for chess this Sunday?\",\"time\":\"Yesterday\",\"isChild\":false}]"
                ),
                WhatsAppConversation(
                    childId = aaravId,
                    contactName = "Unknown (+91 98999 12345)",
                    phoneNumber = "+91 98999 12345",
                    lastMessage = "Send your gaming account password for free diamonds",
                    lastMessageTime = "08:15 AM",
                    unreadCount = 1,
                    avatarColorIndex = 3,
                    isFlaggedSuspicious = true,
                    totalMessagesToday = 2,
                    messagesJson = "[{\"sender\":\"Unknown\",\"text\":\"Send your gaming account password for free diamonds\",\"time\":\"08:15 AM\",\"isChild\":false}]"
                )
            )
            dao.insertWhatsAppConversations(whatsAppChats)

            // Seed App Notifications Feed for Aarav
            val appNotifications = listOf(
                AppNotificationItem(
                    childId = aaravId,
                    appName = "WhatsApp",
                    packageName = "com.whatsapp",
                    senderName = "Rohan (Class 8 Friend)",
                    messageContent = "Did you finish Chapter 5 Science notes?",
                    timestamp = System.currentTimeMillis() - 1000 * 60 * 12,
                    isSuspiciousKeyword = false,
                    category = "CHAT"
                ),
                AppNotificationItem(
                    childId = aaravId,
                    appName = "WhatsApp",
                    packageName = "com.whatsapp",
                    senderName = "Unknown (+91 98999 12345)",
                    messageContent = "Send your gaming account password for free diamonds",
                    timestamp = System.currentTimeMillis() - 1000 * 60 * 45,
                    isSuspiciousKeyword = true,
                    category = "CHAT"
                ),
                AppNotificationItem(
                    childId = aaravId,
                    appName = "YouTube Kids",
                    packageName = "com.google.android.apps.youtube.kids",
                    senderName = "National Geographic Kids",
                    messageContent = "New video uploaded: Wild Ocean Animals Documentary",
                    timestamp = System.currentTimeMillis() - 1000 * 60 * 90,
                    isSuspiciousKeyword = false,
                    category = "ENTERTAINMENT"
                ),
                AppNotificationItem(
                    childId = aaravId,
                    appName = "Duolingo Kids",
                    packageName = "com.duolingo",
                    senderName = "Duo the Owl",
                    messageContent = "Keep your 7-day learning streak going today!",
                    timestamp = System.currentTimeMillis() - 1000 * 60 * 180,
                    isSuspiciousKeyword = false,
                    category = "EDUCATION"
                ),
                AppNotificationItem(
                    childId = aaravId,
                    appName = "Instagram",
                    packageName = "com.instagram.android",
                    senderName = "Direct Message Alert",
                    messageContent = "New message request from user_gaming99",
                    timestamp = System.currentTimeMillis() - 1000 * 60 * 300,
                    isSuspiciousKeyword = true,
                    category = "SOCIAL"
                )
            )
            dao.insertAppNotifications(appNotifications)

            // Seed Call Logs for Aarav
            val callLogs = listOf(
                CallLogItem(
                    childId = aaravId,
                    contactName = "Dad (Papa)",
                    phoneNumber = "+91 98111 22233",
                    callType = "INCOMING",
                    durationSeconds = 145, // 2m 25s
                    timestamp = System.currentTimeMillis() - 1000 * 60 * 25,
                    isSuspicious = false,
                    hasRecording = true
                ),
                CallLogItem(
                    childId = aaravId,
                    contactName = "Mom (Mummy)",
                    phoneNumber = "+91 98111 44455",
                    callType = "OUTGOING",
                    durationSeconds = 80, // 1m 20s
                    timestamp = System.currentTimeMillis() - 1000 * 60 * 90,
                    isSuspicious = false,
                    hasRecording = true
                ),
                CallLogItem(
                    childId = aaravId,
                    contactName = "Amit Sir (Math Tutor)",
                    phoneNumber = "+91 98450 11223",
                    callType = "INCOMING",
                    durationSeconds = 210, // 3m 30s
                    timestamp = System.currentTimeMillis() - 1000 * 60 * 240,
                    isSuspicious = false,
                    hasRecording = true
                ),
                CallLogItem(
                    childId = aaravId,
                    contactName = "Unknown Telemarketer / Game Bot",
                    phoneNumber = "+91 14099 88776",
                    callType = "MISSED",
                    durationSeconds = 0,
                    timestamp = System.currentTimeMillis() - 1000 * 60 * 420,
                    isSuspicious = true,
                    hasRecording = false
                ),
                CallLogItem(
                    childId = aaravId,
                    contactName = "Rohan (School Friend)",
                    phoneNumber = "+91 98112 34567",
                    callType = "OUTGOING",
                    durationSeconds = 320, // 5m 20s
                    timestamp = System.currentTimeMillis() - 1000 * 60 * 600,
                    isSuspicious = false,
                    hasRecording = true
                )
            )
            dao.insertCallLogs(callLogs)

            // Seed Default Parent Account (ईमेल लॉगिन सिस्टम - logged out by default)
            dao.insertUserAccount(
                UserAccount(
                    name = "Parent User",
                    email = "",
                    isLoggedIn = false,
                    role = "PARENT",
                    phoneNumber = "",
                    authProvider = "EMAIL"
                )
            )

            // Seed SMS Messages Tracker
            val smsList = listOf(
                SmsMessageItem(
                    childId = aaravId,
                    senderOrRecipient = "+91 98220 54321",
                    senderName = "Rohan (School Friend)",
                    messageBody = "Hey Aarav, bring your science project notebook tomorrow!",
                    isIncoming = true,
                    timestamp = System.currentTimeMillis() - 1000 * 60 * 45,
                    isSuspicious = false
                ),
                SmsMessageItem(
                    childId = aaravId,
                    senderOrRecipient = "+91 14099 33211",
                    senderName = "Unknown Game Promo",
                    messageBody = "Claim 10,000 FREE ROBUX now! Click bit.ly/free-robux-hack and enter your password!",
                    isIncoming = true,
                    timestamp = System.currentTimeMillis() - 1000 * 60 * 120,
                    isSuspicious = true,
                    suspiciousReason = "Phishing link & password request scam detected"
                ),
                SmsMessageItem(
                    childId = aaravId,
                    senderOrRecipient = "VM-SCHBUS",
                    senderName = "Green Valley School Bus",
                    messageBody = "Bus #14 departed Sector 14 at 2:35 PM. ETA 25 minutes.",
                    isIncoming = true,
                    timestamp = System.currentTimeMillis() - 1000 * 60 * 300,
                    isSuspicious = false
                ),
                SmsMessageItem(
                    childId = aaravId,
                    senderOrRecipient = "+91 98765 43210",
                    senderName = "Dad (Parent)",
                    messageBody = "Finish your homework before opening Minecraft.",
                    isIncoming = true,
                    timestamp = System.currentTimeMillis() - 1000 * 60 * 450,
                    isSuspicious = false
                )
            )
            dao.insertSmsMessages(smsList)

            // Seed YouTube Video Watch History
            val youtubeList = listOf(
                YouTubeWatchItem(
                    childId = aaravId,
                    videoTitle = "How Black Holes Work in 5 Minutes! 🌌",
                    channelName = "Kurzgesagt – In a Nutshell",
                    category = "Science & Education",
                    durationMinutes = 9,
                    timestamp = System.currentTimeMillis() - 1000 * 60 * 50,
                    isFlagged = false
                ),
                YouTubeWatchItem(
                    childId = aaravId,
                    videoTitle = "Roblox 100 Days Survival in Hardcore Mode Ep 4",
                    channelName = "GamingBeast Hindi",
                    category = "Gaming",
                    durationMinutes = 22,
                    timestamp = System.currentTimeMillis() - 1000 * 60 * 180,
                    isFlagged = false
                ),
                YouTubeWatchItem(
                    childId = aaravId,
                    videoTitle = "Dangerous Prank Gone Wrong! DO NOT TRY AT HOME",
                    channelName = "CrazyStunts101",
                    category = "Entertainment",
                    durationMinutes = 14,
                    timestamp = System.currentTimeMillis() - 1000 * 60 * 380,
                    isFlagged = true,
                    flagReason = "Dangerous stunts & non-child friendly behavior flagged"
                ),
                YouTubeWatchItem(
                    childId = aaravId,
                    videoTitle = "Fun Math Tricks for Class 4 & 5 Students",
                    channelName = "Khan Academy Kids",
                    category = "Education",
                    durationMinutes = 15,
                    timestamp = System.currentTimeMillis() - 1000 * 60 * 700,
                    isFlagged = false
                )
            )
            dao.insertYouTubeWatchItems(youtubeList)
        }
    }
}
