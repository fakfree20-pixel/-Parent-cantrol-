package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.ScreenShare
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Sms
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.ActivityLogItem
import com.example.data.model.AppNotificationItem
import com.example.data.model.AppUsageRule
import com.example.data.model.CallLogItem
import com.example.data.model.ChildProfile
import com.example.data.model.GeofenceZone
import com.example.data.model.SmsMessageItem
import com.example.data.model.WhatsAppConversation
import com.example.data.model.YouTubeWatchItem
import com.example.ui.components.AddBonusTimeDialog
import com.example.ui.components.AntiUninstallProtectionDialog
import com.example.ui.components.AppNotificationsFeedDialog
import com.example.ui.components.CallHistoryDialog
import com.example.ui.components.ChildAvatarCircle
import com.example.ui.components.DetailedUsageReportDialog
import com.example.ui.components.FeatureActionPhotoCard
import com.example.ui.components.FeatureHeroBanner
import com.example.ui.components.InstantLockDialog
import com.example.ui.components.LiveLocationDetailDialog
import com.example.ui.components.OneWayAudioDialog
import com.example.ui.components.PairDeviceDialog
import com.example.ui.components.RemoteCameraDialog
import com.example.ui.components.ScreenMirroringDialog
import com.example.ui.components.ScreenTimeGauge
import com.example.ui.components.SmsTrackingDialog
import com.example.ui.components.WhatsAppChatTrackerDialog
import com.example.ui.components.YouTubeMonitoringDialog
import com.example.ui.components.formatMinutes
import com.example.ui.theme.EarthAmber100
import com.example.ui.theme.EarthAmber600
import com.example.ui.theme.MossGreen600
import com.example.ui.theme.NaturalBg
import com.example.ui.theme.NaturalBorder
import com.example.ui.theme.NaturalCardBg
import com.example.ui.theme.NaturalGreen100
import com.example.ui.theme.NaturalGreen700
import com.example.ui.theme.NaturalGreen900
import com.example.ui.theme.NaturalSurface
import com.example.ui.theme.NaturalSurfaceVariant
import com.example.ui.theme.NaturalTextPrimary
import com.example.ui.theme.NaturalTextSecondary
import com.example.ui.theme.NaturalTextTertiary
import com.example.ui.theme.Terracotta100
import com.example.ui.theme.Terracotta600
import com.example.ui.theme.Terracotta700

@Composable
fun DashboardScreen(
    child: ChildProfile,
    allProfiles: List<ChildProfile>,
    apps: List<AppUsageRule>,
    logs: List<ActivityLogItem>,
    geofenceZones: List<GeofenceZone>,
    whatsAppConversations: List<WhatsAppConversation> = emptyList(),
    smsMessages: List<SmsMessageItem> = emptyList(),
    youTubeWatchHistory: List<YouTubeWatchItem> = emptyList(),
    appNotifications: List<AppNotificationItem> = emptyList(),
    callLogs: List<CallLogItem> = emptyList(),
    isHindi: Boolean,
    onSelectChild: (Long) -> Unit,
    onInstantLockToggle: (isLock: Boolean, reason: String, durationMins: Int) -> Unit,
    onToggleBlockAllApps: (Boolean) -> Unit,
    onAddBonusMinutes: (mins: Int, reason: String) -> Unit,
    onAddGeofence: (name: String, address: String, radius: Int) -> Unit,
    onDeleteGeofence: (Long) -> Unit,
    onClearNotifications: () -> Unit = {},
    onToggleAntiUninstall: (enabled: Boolean, preventSettings: Boolean, preventReset: Boolean) -> Unit = { _, _, _ -> },
    onDeleteCallLog: (Long) -> Unit = {},
    onClearCallLogs: () -> Unit = {},
    onClearSmsLogs: () -> Unit = {},
    onClearYouTubeHistory: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var showLockDialog by remember { mutableStateOf(false) }
    var showBonusDialog by remember { mutableStateOf(false) }
    var showCameraDialog by remember { mutableStateOf(false) }
    var showScreenMirrorDialog by remember { mutableStateOf(false) }
    var showAudioDialog by remember { mutableStateOf(false) }
    var showLocationDialog by remember { mutableStateOf(false) }
    var showUsageReportDialog by remember { mutableStateOf(false) }
    var showPairDeviceDialog by remember { mutableStateOf(false) }
    var showProfileDropdown by remember { mutableStateOf(false) }
    var showWhatsAppDialog by remember { mutableStateOf(false) }
    var showSmsDialog by remember { mutableStateOf(false) }
    var showYouTubeDialog by remember { mutableStateOf(false) }
    var showNotificationsDialog by remember { mutableStateOf(false) }
    var showCallHistoryDialog by remember { mutableStateOf(false) }
    var showAntiUninstallDialog by remember { mutableStateOf(false) }

    val usedMinutesToday = apps.sumOf { it.usageTodayMinutes }
    val totalLimitMinutes = child.weekdayLimitMinutes + child.bonusMinutesToday
    val flaggedWhatsAppCount = whatsAppConversations.count { it.isFlaggedSuspicious }
    val flaggedSmsCount = smsMessages.count { it.isSuspicious }
    val flaggedYouTubeCount = youTubeWatchHistory.count { it.isFlagged }
    val flaggedCallsCount = callLogs.count { it.isSuspicious }

    // Modals
    if (showSmsDialog) {
        SmsTrackingDialog(
            child = child,
            smsMessages = smsMessages,
            isHindi = isHindi,
            onDismiss = { showSmsDialog = false },
            onClearSmsLogs = onClearSmsLogs
        )
    }

    if (showYouTubeDialog) {
        YouTubeMonitoringDialog(
            child = child,
            watchHistory = youTubeWatchHistory,
            isHindi = isHindi,
            onDismiss = { showYouTubeDialog = false },
            onClearHistory = onClearYouTubeHistory
        )
    }
    if (showLockDialog) {
        InstantLockDialog(
            childName = child.name,
            onConfirmLock = { reason, duration ->
                onInstantLockToggle(true, reason, duration)
                showLockDialog = false
            },
            onDismiss = { showLockDialog = false },
            isHindi = isHindi
        )
    }

    if (showBonusDialog) {
        AddBonusTimeDialog(
            childName = child.name,
            onConfirmBonus = { mins, reason ->
                onAddBonusMinutes(mins, reason)
                showBonusDialog = false
            },
            onDismiss = { showBonusDialog = false },
            isHindi = isHindi
        )
    }

    if (showCameraDialog) {
        RemoteCameraDialog(
            child = child,
            isHindi = isHindi,
            onDismiss = { showCameraDialog = false }
        )
    }

    if (showScreenMirrorDialog) {
        ScreenMirroringDialog(
            child = child,
            isHindi = isHindi,
            onInstantLock = {
                onInstantLockToggle(true, "Remote Lock from Screen Mirror", 30)
            },
            onDismiss = { showScreenMirrorDialog = false }
        )
    }

    if (showAudioDialog) {
        OneWayAudioDialog(
            child = child,
            isHindi = isHindi,
            onDismiss = { showAudioDialog = false }
        )
    }

    if (showLocationDialog) {
        LiveLocationDetailDialog(
            child = child,
            geofenceZones = geofenceZones,
            isHindi = isHindi,
            onAddGeofence = onAddGeofence,
            onDeleteGeofence = onDeleteGeofence,
            onDismiss = { showLocationDialog = false }
        )
    }

    if (showUsageReportDialog) {
        DetailedUsageReportDialog(
            child = child,
            apps = apps,
            isHindi = isHindi,
            onDismiss = { showUsageReportDialog = false }
        )
    }

    if (showPairDeviceDialog) {
        PairDeviceDialog(
            onDismiss = { showPairDeviceDialog = false },
            isHindi = isHindi
        )
    }

    if (showWhatsAppDialog) {
        WhatsAppChatTrackerDialog(
            child = child,
            conversations = whatsAppConversations,
            isHindi = isHindi,
            onDismiss = { showWhatsAppDialog = false },
            onBlockWhatsApp = {
                onInstantLockToggle(true, "WhatsApp Locked by Parent", 60)
                showWhatsAppDialog = false
            }
        )
    }

    if (showNotificationsDialog) {
        AppNotificationsFeedDialog(
            child = child,
            notifications = appNotifications,
            isHindi = isHindi,
            onClearAll = onClearNotifications,
            onOpenWhatsAppMonitor = {
                showWhatsAppDialog = true
            },
            onDismiss = { showNotificationsDialog = false }
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

    if (showAntiUninstallDialog) {
        AntiUninstallProtectionDialog(
            child = child,
            isHindi = isHindi,
            onToggleAntiUninstall = onToggleAntiUninstall,
            onDismiss = { showAntiUninstallDialog = false }
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(NaturalBg)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 10.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // 1. DEVICE TOP BAR & LIVE STATUS
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(3.dp, RoundedCornerShape(24.dp))
                    .border(1.dp, NaturalBorder, RoundedCornerShape(24.dp)),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = NaturalSurface)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clickable { showProfileDropdown = true }
                            .weight(1f)
                    ) {
                        ChildAvatarCircle(
                            avatarIndex = child.avatarIndex,
                            name = child.name,
                            size = 44.dp
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = child.deviceModel.ifEmpty { "${child.name}'s Phone" },
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = NaturalTextPrimary
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = "Switch Device",
                                    tint = NaturalTextSecondary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(2.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                // Live Online Status Indicator with animated pulse feel
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(if (child.isDeviceOnline) NaturalGreen700 else Color.Gray)
                                )

                                Text(
                                    text = if (child.isDeviceOnline) {
                                        if (isHindi) "कनेक्टेड (Online)" else "Connected (Online)"
                                    } else {
                                        if (isHindi) "ऑफलाइन" else "Offline"
                                    },
                                    fontSize = 11.sp,
                                    color = if (child.isDeviceOnline) NaturalGreen700 else NaturalTextSecondary,
                                    fontWeight = FontWeight.SemiBold
                                )

                                Text(text = "•", fontSize = 11.sp, color = NaturalTextTertiary)

                                // Battery indicator with colored text
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = if (child.batteryPercent <= 20) Terracotta100 else NaturalGreen100
                                ) {
                                    Text(
                                        text = "🔋 ${child.batteryPercent}%",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (child.batteryPercent <= 20) Terracotta700 else NaturalGreen700,
                                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }

                        // Child selector dropdown
                        DropdownMenu(
                            expanded = showProfileDropdown,
                            onDismissRequest = { showProfileDropdown = false }
                        ) {
                            allProfiles.forEach { profile ->
                                DropdownMenuItem(
                                    text = {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            ChildAvatarCircle(avatarIndex = profile.avatarIndex, name = profile.name, size = 30.dp)
                                            Spacer(modifier = Modifier.width(10.dp))
                                            Column {
                                                Text(profile.name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                                Text("${profile.deviceModel} • 🔋 ${profile.batteryPercent}%", fontSize = 11.sp, color = NaturalTextSecondary)
                                            }
                                        }
                                    },
                                    onClick = {
                                        onSelectChild(profile.id)
                                        showProfileDropdown = false
                                    }
                                )
                            }
                        }
                    }

                    // Pair New Device Action button
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = NaturalGreen100,
                        modifier = Modifier.clickable { showPairDeviceDialog = true }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add Device",
                                tint = NaturalGreen700,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (isHindi) "+ डिवाइस" else "+ Device",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = NaturalGreen700
                            )
                        }
                    }
                }
            }
        }

        // 2. HERO FAMILY DIGITAL BANNER & SECURITY PULSE
        item {
            FeatureHeroBanner(
                imageRes = R.drawable.family_screen_hero_1787112118626,
                title = if (isHindi) "स्मार्ट पैरेंटल सुरक्षा" else "ParentGuard AI Protection",
                subtitle = if (isHindi) "SMS, यूट्यूब, लाइव चैट व लोकेशन की रीयल-टाइम सुरक्षा" else "24/7 Shield for SMS, YouTube, WhatsApp & GPS Radar",
                badgeText = if (isHindi) "लाइव सुरक्षा 24/7" else "24/7 LIVE SHIELD",
                badgeColor = NaturalGreen700
            )
        }

        // 3. KEY DIGITAL FEATURES WITH 3D PHOTOS (SMS ट्रैकिंग, यूट्यूब वीडियो)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // SMS Tracker 3D Card
                FeatureActionPhotoCard(
                    imageRes = R.drawable.img_sms_chat_monitor_1787126196512,
                    title = if (isHindi) "SMS ट्रैकिंग" else "SMS Tracker",
                    subtitle = if (isHindi) "${smsMessages.size} संदेश • स्कैम गार्ड" else "${smsMessages.size} SMS • Scam Guard",
                    tagText = if (flaggedSmsCount > 0) "! Alert" else "LIVE",
                    tagColor = if (flaggedSmsCount > 0) Terracotta700 else NaturalGreen700,
                    testTag = "dashboard_sms_tracker_card",
                    onClick = { showSmsDialog = true },
                    modifier = Modifier.weight(1f)
                )

                // YouTube Video Monitoring 3D Card
                FeatureActionPhotoCard(
                    imageRes = R.drawable.img_youtube_monitor_1787118779866,
                    title = if (isHindi) "यूट्यूब वीडियो" else "YouTube Shield",
                    subtitle = if (isHindi) "${youTubeWatchHistory.size} वीडियो • सेफ किड्स" else "${youTubeWatchHistory.size} Videos Screened",
                    tagText = if (flaggedYouTubeCount > 0) "! Flagged" else "ACTIVE",
                    tagColor = if (flaggedYouTubeCount > 0) Terracotta700 else Terracotta600,
                    testTag = "dashboard_youtube_card",
                    onClick = { showYouTubeDialog = true },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // 4. SECOND ROW 3D DIGITAL PHOTO CARDS (सुरक्षा लॉक & लोकेशन रडार)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Anti-Uninstall Security Lock 3D Card
                FeatureActionPhotoCard(
                    imageRes = R.drawable.img_hero_security_1787118769835,
                    title = if (isHindi) "सुरक्षा एडमिन लॉक" else "Anti-Uninstall Lock",
                    subtitle = if (isHindi) (if (child.antiUninstallEnabled) "सुरक्षा सक्रिय है" else "असुरक्षित • चालू करें")
                    else (if (child.antiUninstallEnabled) "Tamper Protected" else "Tap to Enable"),
                    tagText = if (child.antiUninstallEnabled) "LOCKED" else "OFF",
                    tagColor = if (child.antiUninstallEnabled) NaturalGreen700 else Color.Gray,
                    testTag = "dashboard_anti_uninstall_card",
                    onClick = { showAntiUninstallDialog = true },
                    modifier = Modifier.weight(1f)
                )

                // GPS Location & Radar Safe Zones 3D Card
                FeatureActionPhotoCard(
                    imageRes = R.drawable.img_location_radar_1787126216872,
                    title = if (isHindi) "लोकेशन रडार" else "GPS Radar & Zones",
                    subtitle = if (isHindi) "${geofenceZones.size} सुरक्षित ज़ोन • लाइव" else "${geofenceZones.size} Geofence Zones",
                    tagText = "GPS ON",
                    tagColor = Color(0xFF0284C7),
                    testTag = "dashboard_location_radar_card",
                    onClick = { showLocationDialog = true },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // 3. WHATSAPP CHAT & APP NOTIFICATIONS TRACKER ROW
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // WhatsApp Chat Monitor Card
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .shadow(2.dp, RoundedCornerShape(22.dp))
                        .border(
                            1.dp,
                            if (flaggedWhatsAppCount > 0) Terracotta600 else NaturalBorder,
                            RoundedCornerShape(22.dp)
                        )
                        .clickable { showWhatsAppDialog = true },
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = NaturalSurface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(
                                        Brush.linearGradient(
                                            listOf(Color(0xFF128C7E), Color(0xFF25D366))
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Chat, contentDescription = "WhatsApp", tint = Color.White, modifier = Modifier.size(22.dp))
                            }

                            if (flaggedWhatsAppCount > 0) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = Terracotta100
                                ) {
                                    Text(
                                        text = "! Alert",
                                        color = Terracotta700,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                    )
                                }
                            } else {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = Color(0xFFE8F8F0)
                                ) {
                                    Text(
                                        text = "LIVE",
                                        color = Color(0xFF128C7E),
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = if (isHindi) "व्हाट्सएप चैट" else "WhatsApp Chats",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = NaturalTextPrimary
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = if (isHindi) "${whatsAppConversations.size} संपर्क • मैसेजेस" else "${whatsAppConversations.size} Contacts Tracked",
                            fontSize = 11.sp,
                            color = NaturalTextSecondary
                        )
                    }
                }

                // All App Notifications Card
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .shadow(2.dp, RoundedCornerShape(22.dp))
                        .border(1.dp, NaturalBorder, RoundedCornerShape(22.dp))
                        .clickable { showNotificationsDialog = true },
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = NaturalSurface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(
                                        Brush.linearGradient(
                                            listOf(Color(0xFF4F46E5), Color(0xFF7C3AED))
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Notifications, contentDescription = "Notifications", tint = Color.White, modifier = Modifier.size(22.dp))
                            }

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFFEEF2FF)
                            ) {
                                Text(
                                    text = "${appNotifications.size} New",
                                    color = Color(0xFF4F46E5),
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = if (isHindi) "ऐप नोटिफिकेशन" else "App Notifications",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = NaturalTextPrimary
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = if (isHindi) "रियल-टाइम फ़ीड सक्रिय" else "Real-time alerts active",
                            fontSize = 11.sp,
                            color = NaturalTextSecondary
                        )
                    }
                }
            }
        }

        // 4. CALL HISTORY TRACKER & ANTI-UNINSTALL TAMPER PROTECTION ROW
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Call History Tracker Card
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .shadow(2.dp, RoundedCornerShape(22.dp))
                        .border(
                            1.dp,
                            if (flaggedCallsCount > 0) Terracotta600 else NaturalBorder,
                            RoundedCornerShape(22.dp)
                        )
                        .clickable { showCallHistoryDialog = true },
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = NaturalSurface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(
                                        Brush.linearGradient(
                                            listOf(Color(0xFF0284C7), Color(0xFF0EA5E9))
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Call, contentDescription = "Call History", tint = Color.White, modifier = Modifier.size(22.dp))
                            }

                            if (flaggedCallsCount > 0) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = Terracotta100
                                ) {
                                    Text(
                                        text = "! Alert",
                                        color = Terracotta700,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                    )
                                }
                            } else {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = Color(0xFFE0F2FE)
                                ) {
                                    Text(
                                        text = "${callLogs.size} Calls",
                                        color = Color(0xFF0284C7),
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = if (isHindi) "कॉल हिस्ट्री" else "Call History",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = NaturalTextPrimary
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = if (isHindi) "ऑडियो रिकॉर्डिंग उपलब्ध" else "Audio Logs & Duration",
                            fontSize = 11.sp,
                            color = NaturalTextSecondary
                        )
                    }
                }

                // Anti-Uninstall Lock Card
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .shadow(2.dp, RoundedCornerShape(22.dp))
                        .border(1.dp, NaturalBorder, RoundedCornerShape(22.dp))
                        .clickable { showAntiUninstallDialog = true },
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = NaturalSurface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(
                                        Brush.linearGradient(
                                            listOf(Color(0xFFD97706), Color(0xFFF59E0B))
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Security, contentDescription = "Security Lock", tint = Color.White, modifier = Modifier.size(22.dp))
                            }

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (child.antiUninstallEnabled) NaturalGreen100 else NaturalSurfaceVariant
                            ) {
                                Text(
                                    text = if (child.antiUninstallEnabled) "LOCKED" else "OFF",
                                    color = if (child.antiUninstallEnabled) NaturalGreen700 else NaturalTextSecondary,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = if (isHindi) "अनइंस्टॉल सुरक्षा" else "Anti-Uninstall Lock",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = NaturalTextPrimary
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = if (isHindi) "एडमिन लॉक सक्रिय" else "Tamper Protected",
                            fontSize = 11.sp,
                            color = if (child.antiUninstallEnabled) NaturalGreen700 else NaturalTextSecondary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        // 5. FLASHGET USAGE REPORT SECTION WITH 3D BAR GRAPHIC
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(1.dp, RoundedCornerShape(20.dp))
                    .border(1.dp, NaturalBorder, RoundedCornerShape(20.dp))
                    .clickable { showUsageReportDialog = true },
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = NaturalSurface)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = if (isHindi) "उपयोग रिपोर्ट" else "Usage Report",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = NaturalTextPrimary
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(Terracotta700)
                            )
                            Icon(
                                imageVector = Icons.Default.ChevronRight,
                                contentDescription = null,
                                tint = NaturalTextSecondary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (isHindi) "विस्तृत डेटा देखें (${formatMinutes(usedMinutesToday, isHindi)} आज उपयोग)"
                            else "View Detailed Data (${formatMinutes(usedMinutesToday)} today)",
                            fontSize = 12.sp,
                            color = NaturalTextSecondary
                        )
                    }

                    // 3D Chart Illustration Graphic
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(NaturalGreen100),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.Bottom,
                            modifier = Modifier.padding(10.dp)
                        ) {
                            Box(modifier = Modifier.width(6.dp).height(18.dp).clip(RoundedCornerShape(3.dp)).background(NaturalGreen700))
                            Box(modifier = Modifier.width(6.dp).height(34.dp).clip(RoundedCornerShape(3.dp)).background(Color(0xFF8B5CF6)))
                            Box(modifier = Modifier.width(6.dp).height(24.dp).clip(RoundedCornerShape(3.dp)).background(EarthAmber600))
                            Box(modifier = Modifier.width(6.dp).height(14.dp).clip(RoundedCornerShape(3.dp)).background(Color(0xFF06B6D4)))
                        }
                    }
                }
            }
        }

        // 6. FLASHGET LIVE MONITORING SECTION (REMOTE CAMERA, SCREEN MIRRORING, ONE-WAY AUDIO)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(1.dp, RoundedCornerShape(22.dp))
                    .border(1.dp, NaturalBorder, RoundedCornerShape(22.dp)),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = NaturalSurface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isHindi) "लाइव निगरानी" else "Live Monitoring",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = NaturalTextPrimary
                        )

                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Monitoring Settings",
                            tint = NaturalTextTertiary,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        // 1. Remote Camera
                        LiveMonitoringButton(
                            icon = Icons.Default.PhotoCamera,
                            label = if (isHindi) "रिमोट कैमरा" else "Remote Camera",
                            iconTint = Color(0xFF3B82F6),
                            bgTint = Color(0xFFEFF6FF),
                            onClick = { showCameraDialog = true }
                        )

                        // 2. Screen Mirroring
                        LiveMonitoringButton(
                            icon = Icons.Default.ScreenShare,
                            label = if (isHindi) "स्क्रीन मिररिंग" else "Screen Mirroring",
                            iconTint = Color(0xFF8B5CF6),
                            bgTint = Color(0xFFF5F3FF),
                            onClick = { showScreenMirrorDialog = true }
                        )

                        // 3. One-Way Audio
                        LiveMonitoringButton(
                            icon = Icons.Default.Headphones,
                            label = if (isHindi) "वन-वे ऑडियो" else "One-Way Audio",
                            iconTint = Color(0xFF10B981),
                            bgTint = Color(0xFFECFDF5),
                            onClick = { showAudioDialog = true }
                        )
                    }
                }
            }
        }

        // 7. FLASHGET BLOCK ALL APPS SWITCH
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(1.dp, RoundedCornerShape(20.dp))
                    .border(1.dp, NaturalBorder, RoundedCornerShape(20.dp)),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = NaturalSurface)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        // App Lock Cluster Icon
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (child.blockAllApps) Terracotta100 else NaturalSurfaceVariant),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (child.blockAllApps) Icons.Default.Lock else Icons.Default.LockOpen,
                                contentDescription = null,
                                tint = if (child.blockAllApps) Terracotta700 else NaturalGreen700,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = if (isHindi) "सभी ऐप्स ब्लॉक करें" else "Block All Apps",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = NaturalTextPrimary
                            )
                            Text(
                                text = if (isHindi) "स्वीकृत ऐप्स के अलावा सभी ऐप बंद रहेंगे"
                                else "All apps except for \"Allowed Apps\" will be blocked",
                                fontSize = 11.sp,
                                color = NaturalTextSecondary
                            )
                        }
                    }

                    Switch(
                        checked = child.blockAllApps,
                        onCheckedChange = { onToggleBlockAllApps(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = Terracotta700,
                            uncheckedThumbColor = Color.White,
                            uncheckedTrackColor = NaturalBorder
                        ),
                        modifier = Modifier.testTag("block_all_apps_switch")
                    )
                }
            }
        }

        // 8. FLASHGET LIVE LOCATION & MAP SNIPPET
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(1.dp, RoundedCornerShape(22.dp))
                    .border(1.dp, NaturalBorder, RoundedCornerShape(22.dp))
                    .clickable { showLocationDialog = true },
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = NaturalSurface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isHindi) "लाइव लोकेशन" else "Live Location",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = NaturalTextPrimary
                        )

                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = "View Map",
                            tint = NaturalTextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Simulated Map Container
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFFE2EFE0)),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val w = size.width
                            val h = size.height
                            drawLine(Color.White, Offset(0f, h * 0.5f), Offset(w, h * 0.5f), strokeWidth = 10f)
                            drawLine(Color.White, Offset(w * 0.65f, 0f), Offset(w * 0.65f, h), strokeWidth = 8f)
                            drawCircle(NaturalGreen700.copy(alpha = 0.2f), radius = 40.dp.toPx(), center = Offset(w * 0.5f, h * 0.5f))
                        }

                        // Child marker pin
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.offset(y = (-4).dp)
                        ) {
                            ChildAvatarCircle(avatarIndex = child.avatarIndex, name = child.name, size = 26.dp)
                            Icon(Icons.Default.LocationOn, contentDescription = null, tint = Terracotta700, modifier = Modifier.size(24.dp))
                        }

                        // Address overlay badge
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color.White.copy(alpha = 0.9f),
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(8.dp)
                        ) {
                            Text(
                                text = child.locationAddress,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = NaturalTextPrimary,
                                maxLines = 1,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }
                    }
                }
            }
        }

        // 9. SCREEN TIME GAUGE & INSTANT LOCK CONTROLS
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                ScreenTimeGauge(
                    usedMinutes = usedMinutesToday,
                    limitMinutes = child.weekdayLimitMinutes,
                    bonusMinutes = child.bonusMinutesToday,
                    isLocked = child.isLocked,
                    isHindi = isHindi
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Instant Lock / Unlock button
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .clickable {
                                if (child.isLocked) {
                                    onInstantLockToggle(false, "", 0)
                                } else {
                                    showLockDialog = true
                                }
                            },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (child.isLocked) Terracotta100 else NaturalSurfaceVariant
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp, horizontal = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = if (child.isLocked) Icons.Default.LockOpen else Icons.Default.Lock,
                                contentDescription = null,
                                tint = if (child.isLocked) Terracotta700 else NaturalGreen700,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (child.isLocked) {
                                    if (isHindi) "डिवाइस अनलॉक" else "Unlock Device"
                                } else {
                                    if (isHindi) "तुरंत फ्रीज करें" else "Freeze Device"
                                },
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = if (child.isLocked) Terracotta700 else NaturalTextPrimary
                            )
                        }
                    }

                    // +15m Bonus time button
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .clickable { showBonusDialog = true },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = NaturalGreen100)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp, horizontal = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Timer,
                                contentDescription = null,
                                tint = NaturalGreen700,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isHindi) "+ समय बढ़ाएं" else "+ Bonus Time",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = NaturalGreen900
                            )
                        }
                    }
                }
            }
        }
    }
}

// Sub-component for Live Monitoring 3-item buttons
@Composable
private fun LiveMonitoringButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    iconTint: Color,
    bgTint: Color,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(54.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(bgTint),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = iconTint,
                modifier = Modifier.size(28.dp)
            )

            // "Trial" Badge at top corner
            Surface(
                shape = RoundedCornerShape(4.dp),
                color = Color(0xFF3B82F6),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(2.dp)
            ) {
                Text(
                    text = "Trial",
                    color = Color.White,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.padding(horizontal = 3.dp, vertical = 1.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = NaturalTextPrimary
        )
    }
}
