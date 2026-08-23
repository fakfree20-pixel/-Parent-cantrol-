package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.ActivityLogItem
import com.example.data.model.AppNotificationItem
import com.example.data.model.AppUsageRule
import com.example.data.model.CallLogItem
import com.example.data.model.ChildProfile
import com.example.data.model.SmsMessageItem
import com.example.data.model.WhatsAppConversation
import com.example.data.model.YouTubeWatchItem
import com.example.ui.components.AlbumsSafetyDialog
import com.example.ui.components.AppNotificationsFeedDialog
import com.example.ui.components.BrowserSafetyDialog
import com.example.ui.components.CallHistoryDialog
import com.example.ui.components.DetailedUsageReportDialog
import com.example.ui.components.RemoteCameraDialog
import com.example.ui.components.SmsTrackingDialog
import com.example.ui.components.SocialAppDetectionDialog
import com.example.ui.components.WhatsAppChatTrackerDialog
import com.example.ui.components.YouTubeMonitoringDialog
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Notice Screen - FlashGet Kids Style Notice Tab
 * Displays the 9 safety categories matching the exact UI design:
 * 1. App Notifications
 * 2. Alerts & Request
 * 3. Browser Safety
 * 4. TikTok & YouTube History
 * 5. Snapshot & Recording
 * 6. Usage Logs
 * 7. Social App Keyword Detection
 * 8. Call & SMS Safety
 * 9. Albums Safety
 */
@Composable
fun ActivityLogScreen(
    child: ChildProfile,
    apps: List<AppUsageRule> = emptyList(),
    logs: List<ActivityLogItem> = emptyList(),
    whatsAppConversations: List<WhatsAppConversation> = emptyList(),
    smsMessages: List<SmsMessageItem> = emptyList(),
    youTubeWatchHistory: List<YouTubeWatchItem> = emptyList(),
    appNotifications: List<AppNotificationItem> = emptyList(),
    callLogs: List<CallLogItem> = emptyList(),
    isHindi: Boolean,
    onClearLogs: () -> Unit = {},
    onClearNotifications: () -> Unit = {},
    onDeleteCallLog: (Long) -> Unit = {},
    onClearCallLogs: () -> Unit = {},
    onClearSmsLogs: () -> Unit = {},
    onClearYouTubeHistory: () -> Unit = {},
    onInstantLock: (Boolean, String, Int) -> Unit = { _, _, _ -> },
    modifier: Modifier = Modifier
) {
    // Dialog control states for each of the 9 Notice categories
    var showAppNotificationsDialog by remember { mutableStateOf(false) }
    var showAlertsAndRequestDialog by remember { mutableStateOf(false) }
    var showBrowserSafetyDialog by remember { mutableStateOf(false) }
    var showYouTubeHistoryDialog by remember { mutableStateOf(false) }
    var showSnapshotRecordingDialog by remember { mutableStateOf(false) }
    var showUsageLogsDialog by remember { mutableStateOf(false) }
    var showSocialAppDetectionDialog by remember { mutableStateOf(false) }
    var showWhatsAppTrackerDialog by remember { mutableStateOf(false) }
    var showCallSmsChooserDialog by remember { mutableStateOf(false) }
    var showCallHistoryDialog by remember { mutableStateOf(false) }
    var showSmsTrackingDialog by remember { mutableStateOf(false) }
    var showAlbumsSafetyDialog by remember { mutableStateOf(false) }

    // Dialog 1: App Notifications Feed
    if (showAppNotificationsDialog) {
        AppNotificationsFeedDialog(
            child = child,
            notifications = appNotifications,
            isHindi = isHindi,
            onClearAll = onClearNotifications,
            onOpenWhatsAppMonitor = { showWhatsAppTrackerDialog = true },
            onDismiss = { showAppNotificationsDialog = false }
        )
    }

    // Dialog 2: Alerts & Request Detail Modal
    if (showAlertsAndRequestDialog) {
        AlertsAndRequestDialog(
            logs = logs,
            child = child,
            isHindi = isHindi,
            onClearLogs = onClearLogs,
            onDismiss = { showAlertsAndRequestDialog = false }
        )
    }

    // Dialog 3: Browser Safety Modal
    if (showBrowserSafetyDialog) {
        BrowserSafetyDialog(
            child = child,
            isHindi = isHindi,
            onDismiss = { showBrowserSafetyDialog = false }
        )
    }

    // Dialog 4: TikTok & YouTube History Modal
    if (showYouTubeHistoryDialog) {
        YouTubeMonitoringDialog(
            child = child,
            watchHistory = youTubeWatchHistory,
            isHindi = isHindi,
            onDismiss = { showYouTubeHistoryDialog = false },
            onClearHistory = onClearYouTubeHistory
        )
    }

    // Dialog 5: Snapshot & Recording (Remote Camera & Live Stream)
    if (showSnapshotRecordingDialog) {
        RemoteCameraDialog(
            child = child,
            isHindi = isHindi,
            onDismiss = { showSnapshotRecordingDialog = false }
        )
    }

    // Dialog 6: Usage Logs (Detailed Screen Time & Limits Report)
    if (showUsageLogsDialog) {
        DetailedUsageReportDialog(
            child = child,
            apps = apps,
            isHindi = isHindi,
            onDismiss = { showUsageLogsDialog = false }
        )
    }

    // Dialog 7: Social App Keyword Detection
    if (showSocialAppDetectionDialog) {
        SocialAppDetectionDialog(
            child = child,
            isHindi = isHindi,
            onDismiss = { showSocialAppDetectionDialog = false }
        )
    }

    if (showWhatsAppTrackerDialog) {
        WhatsAppChatTrackerDialog(
            child = child,
            conversations = whatsAppConversations,
            isHindi = isHindi,
            onDismiss = { showWhatsAppTrackerDialog = false },
            onBlockWhatsApp = {
                onInstantLock(true, "WhatsApp Locked by Parent", 60)
                showWhatsAppTrackerDialog = false
            }
        )
    }

    // Dialog 8: Call & SMS Safety Chooser / Call History & SMS Modals
    if (showCallSmsChooserDialog) {
        CallAndSmsSafetyChooserDialog(
            child = child,
            callLogsCount = callLogs.size,
            smsCount = smsMessages.size,
            isHindi = isHindi,
            onOpenCalls = {
                showCallSmsChooserDialog = false
                showCallHistoryDialog = true
            },
            onOpenSms = {
                showCallSmsChooserDialog = false
                showSmsTrackingDialog = true
            },
            onDismiss = { showCallSmsChooserDialog = false }
        )
    }

    if (showCallHistoryDialog) {
        CallHistoryDialog(
            child = child,
            callLogs = callLogs,
            isHindi = isHindi,
            onDeleteCall = onDeleteCallLog,
            onClearAll = onClearCallLogs,
            onDismiss = { showCallHistoryDialog = false }
        )
    }

    if (showSmsTrackingDialog) {
        SmsTrackingDialog(
            child = child,
            smsMessages = smsMessages,
            isHindi = isHindi,
            onDismiss = { showSmsTrackingDialog = false },
            onClearSmsLogs = onClearSmsLogs
        )
    }

    // Dialog 9: Albums Safety Modal
    if (showAlbumsSafetyDialog) {
        AlbumsSafetyDialog(
            child = child,
            isHindi = isHindi,
            onDismiss = { showAlbumsSafetyDialog = false }
        )
    }

    // MAIN NOTICE SCREEN CONTENT
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF7F8FC))
    ) {
        // Gradient Lavender Header Background
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF9E8CFB),
                            Color(0xFFB4A5FB),
                            Color(0xFFDCD6FE),
                            Color(0xFFF7F8FC)
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            // Notice Top Title Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = if (isHindi) "Notice (नोटिस)" else "Notice",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                if (logs.isNotEmpty() || appNotifications.isNotEmpty()) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White.copy(alpha = 0.25f)
                    ) {
                        Text(
                            text = if (isHindi) "${logs.size + appNotifications.size} नए अलर्ट" else "${logs.size + appNotifications.size} New",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            // Scrollable List of 9 Notice Cards
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(top = 4.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // 1. App Notifications Card
                item {
                    NoticeCategoryCard(
                        icon = Icons.Default.Notifications,
                        iconBgColor = Color(0xFF3B82F6),
                        title = if (isHindi) "App Notifications" else "App Notifications",
                        subtitle = if (appNotifications.isNotEmpty()) {
                            if (isHindi) "${appNotifications.size} नई नोटिफिकेशन्स प्राप्त हुईं" else "${appNotifications.size} new notifications received"
                        } else {
                            "No data available"
                        },
                        testTag = "notice_card_app_notifications",
                        onClick = { showAppNotificationsDialog = true }
                    )
                }

                // 2. Alerts & Request Card
                item {
                    val latestAlertDesc = if (logs.isNotEmpty()) {
                        if (isHindi && logs.first().titleHindi.isNotEmpty()) logs.first().titleHindi else logs.first().title
                    } else {
                        "Child installed a new app: Draw Home"
                    }

                    NoticeCategoryCard(
                        icon = Icons.Default.NotificationsActive,
                        iconBgColor = Color(0xFFFA8231),
                        title = if (isHindi) "Alerts & Request" else "Alerts & Request",
                        subtitle = latestAlertDesc,
                        testTag = "notice_card_alerts_request",
                        onClick = { showAlertsAndRequestDialog = true }
                    )
                }

                // 3. Browser Safety Card
                item {
                    NoticeCategoryCard(
                        icon = Icons.Default.Shield,
                        iconBgColor = Color(0xFF00A8FF),
                        title = if (isHindi) "Browser Safety" else "Browser Safety",
                        subtitle = "No data available",
                        testTag = "notice_card_browser_safety",
                        onClick = { showBrowserSafetyDialog = true }
                    )
                }

                // 4. TikTok & YouTube History Card
                item {
                    val ytSubtitle = if (youTubeWatchHistory.isNotEmpty()) {
                        if (isHindi) "${youTubeWatchHistory.size} वीडियो वॉच हिस्ट्री उपलब्ध" else "${youTubeWatchHistory.size} videos tracked"
                    } else {
                        "No data available"
                    }

                    NoticeCategoryCard(
                        icon = Icons.Default.PlayCircle,
                        iconBgColor = Color(0xFF1E272E),
                        title = if (isHindi) "TikTok & YouTube History" else "TikTok & YouTube History",
                        subtitle = ytSubtitle,
                        testTag = "notice_card_tiktok_youtube",
                        onClick = { showYouTubeHistoryDialog = true }
                    )
                }

                // 5. Snapshot & Recording Card
                item {
                    NoticeCategoryCard(
                        icon = Icons.Default.PhotoCamera,
                        iconBgColor = Color(0xFF6C5CE7),
                        title = if (isHindi) "Snapshot & Recording" else "Snapshot & Recording",
                        subtitle = "No data available",
                        testTag = "notice_card_snapshot_recording",
                        onClick = { showSnapshotRecordingDialog = true }
                    )
                }

                // 6. Usage Logs Card
                item {
                    val usageSubtitle = if (apps.isNotEmpty()) {
                        val totalMins = apps.sumOf { it.usageTodayMinutes }
                        if (isHindi) "कुल स्क्रीन उपयोग: ${totalMins / 60}h ${totalMins % 60}m" else "Total screen usage: ${totalMins / 60}h ${totalMins % 60}m"
                    } else {
                        "No data available"
                    }

                    NoticeCategoryCard(
                        icon = Icons.Default.Assessment,
                        iconBgColor = Color(0xFFFF6B81),
                        title = if (isHindi) "Usage Logs" else "Usage Logs",
                        subtitle = usageSubtitle,
                        testTag = "notice_card_usage_logs",
                        onClick = { showUsageLogsDialog = true }
                    )
                }

                // 7. Social App Keyword Detection Card
                item {
                    val socialSubtitle = if (whatsAppConversations.isNotEmpty()) {
                        if (isHindi) "${whatsAppConversations.size} सोशल चैट मॉनिटरिंग सक्रिय" else "${whatsAppConversations.size} social chats active"
                    } else {
                        "No data available"
                    }

                    NoticeCategoryCard(
                        icon = Icons.Default.Message,
                        iconBgColor = Color(0xFF8854D0),
                        title = if (isHindi) "Social App Keyword Detection" else "Social App Keyword Detection",
                        subtitle = socialSubtitle,
                        testTag = "notice_card_social_keyword",
                        onClick = { showSocialAppDetectionDialog = true }
                    )
                }

                // 8. Call & SMS Safety Card
                item {
                    val callSmsSubtitle = if (callLogs.isNotEmpty() || smsMessages.isNotEmpty()) {
                        if (isHindi) "${callLogs.size} कॉल, ${smsMessages.size} संदेश सुरक्षित" else "${callLogs.size} calls, ${smsMessages.size} SMS tracked"
                    } else {
                        "No data available"
                    }

                    NoticeCategoryCard(
                        icon = Icons.Default.Call,
                        iconBgColor = Color(0xFF2ED573),
                        title = if (isHindi) "Call & SMS Safety" else "Call & SMS Safety",
                        subtitle = callSmsSubtitle,
                        testTag = "notice_card_call_sms_safety",
                        onClick = { showCallSmsChooserDialog = true }
                    )
                }

                // 9. Albums Safety Card
                item {
                    NoticeCategoryCard(
                        icon = Icons.Default.PhotoLibrary,
                        iconBgColor = Color(0xFFFFA801),
                        title = if (isHindi) "Albums Safety" else "Albums Safety",
                        subtitle = "No data available",
                        testTag = "notice_card_albums_safety",
                        onClick = { showAlbumsSafetyDialog = true }
                    )
                }
            }
        }
    }
}

/**
 * Reusable Notice Category Card matching FlashGet Kids visual aesthetics
 */
@Composable
fun NoticeCategoryCard(
    icon: ImageVector,
    iconBgColor: Color,
    title: String,
    subtitle: String,
    testTag: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(18.dp))
            .clickable { onClick() }
            .testTag(testTag),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Rounded Squarish Colorful Icon
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(iconBgColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Title & Subtitle
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E272E),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = Color(0xFF808E9B),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Right Chevron Arrow
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Open $title",
                tint = Color(0xFFB2BEC3),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

/**
 * Alerts & Request Full Screen Modal Dialog
 */
@Composable
fun AlertsAndRequestDialog(
    logs: List<ActivityLogItem>,
    child: ChildProfile,
    isHindi: Boolean,
    onClearLogs: () -> Unit,
    onDismiss: () -> Unit
) {
    val dateFormat = SimpleDateFormat("hh:mm a, dd MMM", Locale.getDefault())
    var selectedFilterIndex by remember { mutableIntStateOf(0) }
    val filters = if (isHindi) {
        listOf("सभी अलर्ट्स", "ऐप इंस्टॉल", "लिमिट व लॉक", "सुरक्षा")
    } else {
        listOf("All Alerts", "App Installs", "Limits & Locks", "Security")
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF7F8FC))
        ) {
            // Header
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.White,
                shadowElevation = 3.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = onDismiss) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Column {
                            Text(
                                text = if (isHindi) "अलर्ट्स और अनुरोध (Alerts & Request)" else "Alerts & Request",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2D3436)
                            )
                            Text(
                                text = "${child.name} • ${logs.size} recorded events",
                                fontSize = 11.sp,
                                color = Color(0xFF636E72)
                            )
                        }
                    }

                    if (logs.isNotEmpty()) {
                        IconButton(onClick = onClearLogs) {
                            Icon(
                                imageVector = Icons.Default.DeleteSweep,
                                contentDescription = "Clear All",
                                tint = Color(0xFFFF4757)
                            )
                        }
                    }
                }
            }

            // Filter Tabs
            TabRow(
                selectedTabIndex = selectedFilterIndex,
                containerColor = Color.White,
                contentColor = Color(0xFF6C5CE7),
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedFilterIndex]),
                        color = Color(0xFF6C5CE7),
                        height = 3.dp
                    )
                }
            ) {
                filters.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedFilterIndex == index,
                        onClick = { selectedFilterIndex = index },
                        text = {
                            Text(
                                text = title,
                                fontSize = 12.sp,
                                fontWeight = if (selectedFilterIndex == index) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
            }

            // List of Alerts
            if (logs.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = null,
                                tint = Color(0xFFB2BEC3),
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = if (isHindi) "कोई अलर्ट या अनुरोध नहीं है।" else "No alerts or requests recorded.",
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2D3436)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (isHindi) "ऐप इंस्टॉल, सीमा अलर्ट और लॉक इवेंट्स यहाँ दिखेंगे।" else "App installs, limit alerts, and lock requests will appear here.",
                                fontSize = 12.sp,
                                color = Color(0xFF636E72)
                            )
                        }
                    }
                }
            } else {
                val filteredLogs = when (selectedFilterIndex) {
                    1 -> logs.filter { it.type == "APP_INSTALL" || it.title.contains("install", ignoreCase = true) || it.title.contains("Draw Home", ignoreCase = true) }
                    2 -> logs.filter { it.type == "LIMIT_ALERT" || it.type == "INSTANT_LOCK" || it.type == "APP_BLOCKED" }
                    3 -> logs.filter { it.type == "SECURITY" || it.type == "BEDTIME" }
                    else -> logs
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(filteredLogs, key = { it.id }) { log ->
                        val (icon, color, bgCol) = when (log.type) {
                            "INSTANT_LOCK" -> Triple(Icons.Default.Lock, Color(0xFFD63031), Color(0xFFFFECEC))
                            "LIMIT_ALERT" -> Triple(Icons.Default.Warning, Color(0xFFE17055), Color(0xFFFFECE6))
                            "APP_BLOCKED" -> Triple(Icons.Default.Lock, Color(0xFFD63031), Color(0xFFFFECEC))
                            "BONUS_TIME" -> Triple(Icons.Default.CheckCircle, Color(0xFF00B894), Color(0xFFE3FCEF))
                            "BEDTIME" -> Triple(Icons.Default.Bedtime, Color(0xFF6C5CE7), Color(0xFFEDE9FE))
                            else -> Triple(Icons.Default.Security, Color(0xFF0984E3), Color(0xFFE6F3FF))
                        }

                        val formattedTime = try {
                            dateFormat.format(Date(log.timestamp))
                        } catch (e: Exception) {
                            ""
                        }

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(1.dp, RoundedCornerShape(16.dp)),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(42.dp)
                                        .clip(CircleShape)
                                        .background(bgCol),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = null,
                                        tint = color,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = if (isHindi && log.titleHindi.isNotEmpty()) log.titleHindi else log.title,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            color = Color(0xFF2D3436)
                                        )
                                        Text(
                                            text = formattedTime,
                                            fontSize = 11.sp,
                                            color = Color(0xFFB2BEC3)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = if (isHindi && log.descriptionHindi.isNotEmpty()) log.descriptionHindi else log.description,
                                        fontSize = 12.sp,
                                        color = Color(0xFF636E72)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Call & SMS Safety Choice Dialog
 */
@Composable
fun CallAndSmsSafetyChooserDialog(
    child: ChildProfile,
    callLogsCount: Int,
    smsCount: Int,
    isHindi: Boolean,
    onOpenCalls: () -> Unit,
    onOpenSms: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF2ED573)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Call,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = if (isHindi) "कॉल और एसएमएस सुरक्षा" else "Call & SMS Safety",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2D3436)
                            )
                            Text(
                                text = "${child.name} • Monitoring Active",
                                fontSize = 11.sp,
                                color = Color(0xFF636E72)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Option 1: Call Logs
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onOpenCalls() },
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F2F6))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Call,
                            contentDescription = null,
                            tint = Color(0xFF2ED573),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (isHindi) "कॉल इतिहास और रिकॉर्ड (Call History)" else "Call History & Recordings",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = Color(0xFF2D3436)
                            )
                            Text(
                                text = if (isHindi) "$callLogsCount कॉल लॉग्स ट्रैक किए गए" else "$callLogsCount calls tracked",
                                fontSize = 11.sp,
                                color = Color(0xFF636E72)
                            )
                        }
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = null,
                            tint = Color(0xFFB2BEC3)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Option 2: SMS Safety
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onOpenSms() },
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F2F6))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Message,
                            contentDescription = null,
                            tint = Color(0xFF0984E3),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (isHindi) "एसएमएस संदेश सुरक्षा (SMS Safety)" else "SMS Safety & Messages",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = Color(0xFF2D3436)
                            )
                            Text(
                                text = if (isHindi) "$smsCount संदेश और स्पैम सुरक्षा" else "$smsCount SMS messages tracked",
                                fontSize = 11.sp,
                                color = Color(0xFF636E72)
                            )
                        }
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = null,
                            tint = Color(0xFFB2BEC3)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C5CE7)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (isHindi) "बंद करें" else "Close", color = Color.White)
                }
            }
        }
    }
}
