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
    version = 7,
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
            // No default mock child profiles or fake devices (like Infinix X6823C) pre-seeded.
            // Device info, battery status, and activity logs will only appear when a real child device is connected or paired.
        }
    }
}
