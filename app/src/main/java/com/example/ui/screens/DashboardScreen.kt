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
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Rule
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.ScreenShare
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SmartDisplay
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.Sms
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Videocam
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
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
import com.example.ui.components.AlbumsSafetyDialog
import com.example.ui.components.AntiUninstallProtectionDialog
import com.example.ui.components.AppNotificationsFeedDialog
import com.example.ui.components.BrowserSafetyDialog
import com.example.ui.components.CallHistoryDialog
import com.example.ui.components.CheckPermissionsDialog
import com.example.ui.components.ChildAvatarCircle
import com.example.ui.components.DetailedUsageReportDialog
import com.example.ui.components.HiddenChildAppGuideDialog
import com.example.ui.components.InstantLockDialog
import com.example.ui.components.LiveLocationDetailDialog
import com.example.ui.components.LivePaintingDialog
import com.example.ui.components.OneWayAudioDialog
import com.example.ui.components.PairDeviceDialog
import com.example.ui.components.RemoteCameraDialog
import com.example.ui.components.ScreenMirroringDialog
import com.example.ui.components.SmsTrackingDialog
import com.example.ui.components.SocialAppDetectionDialog
import com.example.ui.components.WhatsAppChatTrackerDialog
import com.example.ui.components.YouTubeMonitoringDialog

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
    // Dialog States
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

    // FlashGet New Feature Dialogs
    var showLivePaintingDialog by remember { mutableStateOf(false) }
    var showCheckPermissionsDialog by remember { mutableStateOf(false) }
    var showSocialAppDialog by remember { mutableStateOf(false) }
    var showAlbumsSafetyDialog by remember { mutableStateOf(false) }
    var showBrowserSafetyDialog by remember { mutableStateOf(false) }
    var showHiddenGuideDialog by remember { mutableStateOf(false) }

    // Active Dialogs Integration
    if (showLivePaintingDialog) {
        LivePaintingDialog(
            child = child,
            isHindi = isHindi,
            onDismiss = { showLivePaintingDialog = false }
        )
    }

    if (showCheckPermissionsDialog) {
        CheckPermissionsDialog(
            child = child,
            isHindi = isHindi,
            onDismiss = { showCheckPermissionsDialog = false }
        )
    }

    if (showSocialAppDialog) {
        SocialAppDetectionDialog(
            child = child,
            isHindi = isHindi,
            onDismiss = { showSocialAppDialog = false }
        )
    }

    if (showAlbumsSafetyDialog) {
        AlbumsSafetyDialog(
            child = child,
            isHindi = isHindi,
            onDismiss = { showAlbumsSafetyDialog = false }
        )
    }

    if (showBrowserSafetyDialog) {
        BrowserSafetyDialog(
            child = child,
            isHindi = isHindi,
            onDismiss = { showBrowserSafetyDialog = false }
        )
    }

    if (showHiddenGuideDialog) {
        HiddenChildAppGuideDialog(
            isHindi = isHindi,
            onDismiss = { showHiddenGuideDialog = false }
        )
    }

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
            apps = apps,
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
            onOpenWhatsAppMonitor = { showWhatsAppDialog = true },
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

    // MAIN FLASHGET CONTROL DASHBOARD
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF4F6FB)),
        contentPadding = PaddingValues(bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // 1. TOP PURPLE DEVICE HEADER (FlashGet Style)
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0xFF6C5CE7), Color(0xFF5B48D9))
                        )
                    )
                    .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clickable { showProfileDropdown = true }
                            .weight(1f)
                    ) {
                        // User Avatar
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.25f)),
                            contentAlignment = Alignment.Center
                        ) {
                            ChildAvatarCircle(
                                avatarIndex = child.avatarIndex,
                                name = child.name,
                                size = 40.dp
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = child.deviceModel.ifEmpty { "Infinix X6823C" },
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = "Switch Device",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(2.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(7.dp)
                                        .clip(CircleShape)
                                        .background(if (child.isDeviceOnline) Color(0xFFF39C12) else Color.LightGray)
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    text = if (child.isDeviceOnline) "Unknown" else "Offline",
                                    fontSize = 12.sp,
                                    color = Color.White.copy(alpha = 0.9f)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "🔋 ${child.batteryPercent}%",
                                    fontSize = 12.sp,
                                    color = Color.White.copy(alpha = 0.9f),
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        // Dropdown
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
                                                Text("${profile.deviceModel} • 🔋 ${profile.batteryPercent}%", fontSize = 11.sp, color = Color.Gray)
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

                    // Plus (+) Button to Add / Bind new device
                    IconButton(
                        onClick = { showPairDeviceDialog = true },
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.2f))
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add Device", tint = Color.White)
                    }
                }
            }
        }

        // 2. SECTION: SNAPSHOT & RECORDING (FlashGet Section 1)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = if (isHindi) "स्नैपशॉट और रिकॉर्डिंग" else "Snapshot & Recording",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color(0xFF1E1E2E)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Row 1: Camera Recording, Screen Recording, Ambient Recording
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        FlashGetGridItem(
                            title = if (isHindi) "कैमरा रिकॉर्डिंग" else "Camera Recording",
                            icon = Icons.Default.Videocam,
                            iconColor = Color(0xFF6C5CE7),
                            hasPro = true,
                            onClick = { showCameraDialog = true },
                            modifier = Modifier.weight(1f)
                        )
                        FlashGetGridItem(
                            title = if (isHindi) "स्क्रीन रिकॉर्डिंग" else "Screen Recording",
                            icon = Icons.Default.SmartDisplay,
                            iconColor = Color(0xFF6C5CE7),
                            hasPro = true,
                            onClick = { showScreenMirrorDialog = true },
                            modifier = Modifier.weight(1f)
                        )
                        FlashGetGridItem(
                            title = if (isHindi) "एम्बिएंट रिकॉर्डिंग" else "Ambient Recording",
                            icon = Icons.Default.Mic,
                            iconColor = Color(0xFF6C5CE7),
                            hasPro = true,
                            onClick = { showAudioDialog = true },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Row 2: Camera Snapshot, Screen Snapshot
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        FlashGetGridItem(
                            title = if (isHindi) "कैमरा स्नैपशॉट" else "Camera Snapshot",
                            icon = Icons.Default.PhotoCamera,
                            iconColor = Color(0xFF6C5CE7),
                            hasPro = true,
                            onClick = { showCameraDialog = true },
                            modifier = Modifier.weight(1f)
                        )
                        FlashGetGridItem(
                            title = if (isHindi) "स्क्रीन स्नैपशॉट" else "Screen Snapshot",
                            icon = Icons.Default.PhoneAndroid,
                            iconColor = Color(0xFF6C5CE7),
                            hasPro = true,
                            onClick = { showScreenMirrorDialog = true },
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.weight(1f)) // Alignment placeholder
                    }
                }
            }
        }

        // 3. SECTION: DEVICE ACTIVITY (FlashGet Section 2)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = if (isHindi) "डिवाइस गतिविधि" else "Device Activity",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color(0xFF1E1E2E)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Row 1: Screen Time Limits, App Time Limits, App Rules
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        FlashGetGridItem(
                            title = if (isHindi) "स्क्रीन टाइम सीमा" else "Screen Time Limits",
                            icon = Icons.Default.HourglassBottom,
                            iconColor = Color(0xFF4A69BD),
                            hasPro = false,
                            onClick = { showUsageReportDialog = true },
                            modifier = Modifier.weight(1f)
                        )
                        FlashGetGridItem(
                            title = if (isHindi) "ऐप टाइम सीमा" else "App Time Limits",
                            icon = Icons.Default.Widgets,
                            iconColor = Color(0xFF4A69BD),
                            hasPro = false,
                            onClick = { showUsageReportDialog = true },
                            modifier = Modifier.weight(1f)
                        )
                        FlashGetGridItem(
                            title = if (isHindi) "ऐप नियम" else "App Rules",
                            icon = Icons.Default.Rule,
                            iconColor = Color(0xFF4A69BD),
                            hasPro = false,
                            onClick = { showAntiUninstallDialog = true },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Row 2: Usage Logs, Live Painting, Check Permissions
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        FlashGetGridItem(
                            title = if (isHindi) "उपयोग लॉग्स" else "Usage Logs",
                            icon = Icons.Default.History,
                            iconColor = Color(0xFF6C5CE7),
                            hasPro = true,
                            onClick = { showUsageReportDialog = true },
                            modifier = Modifier.weight(1f)
                        )
                        FlashGetGridItem(
                            title = if (isHindi) "लाइव पेंटिंग" else "Live Painting",
                            icon = Icons.Default.Brush,
                            iconColor = Color(0xFFE84393),
                            hasPro = true,
                            isBeta = true,
                            onClick = { showLivePaintingDialog = true },
                            modifier = Modifier.weight(1f)
                        )
                        FlashGetGridItem(
                            title = if (isHindi) "अनुमतियाँ जांचें" else "Check Permissions",
                            icon = Icons.Default.Security,
                            iconColor = Color(0xFF4A69BD),
                            hasPro = false,
                            onClick = { showCheckPermissionsDialog = true },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        // 4. SECTION: USAGE SAFETY (FlashGet Section 3)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = if (isHindi) "उपयोग सुरक्षा" else "Usage Safety",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color(0xFF1E1E2E)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Row 1: Social App Detection, Call & SMS Safety, Albums Safety
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        FlashGetGridItem(
                            title = if (isHindi) "सोशल ऐप डिटेक्शन" else "Social App Detection",
                            icon = Icons.Default.Chat,
                            iconColor = Color(0xFF25D366),
                            hasPro = true,
                            onClick = { showSocialAppDialog = true },
                            modifier = Modifier.weight(1f)
                        )
                        FlashGetGridItem(
                            title = if (isHindi) "कॉल व SMS सुरक्षा" else "Call & SMS Safety",
                            icon = Icons.Default.Call,
                            iconColor = Color(0xFF6C5CE7),
                            hasPro = true,
                            onClick = { showCallHistoryDialog = true },
                            modifier = Modifier.weight(1f)
                        )
                        FlashGetGridItem(
                            title = if (isHindi) "एल्बम व गैलरी सुरक्षा" else "Albums Safety",
                            icon = Icons.Default.PhotoLibrary,
                            iconColor = Color(0xFF6C5CE7),
                            hasPro = true,
                            onClick = { showAlbumsSafetyDialog = true },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Row 2: Browser Safety
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        FlashGetGridItem(
                            title = if (isHindi) "ब्राउज़र सुरक्षा" else "Browser Safety",
                            icon = Icons.Default.Public,
                            iconColor = Color(0xFF0984E3),
                            hasPro = true,
                            onClick = { showBrowserSafetyDialog = true },
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }

        // 5. QUICK ACTIONS: INSTANT LOCK & GPS RADAR
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Instant Lock
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { showLockDialog = true },
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (child.isLocked) Color(0xFFFF4757).copy(alpha = 0.1f) else Color(0xFFF3F0FF)
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                if (child.isLocked) Icons.Default.Lock else Icons.Default.LockOpen,
                                contentDescription = null,
                                tint = if (child.isLocked) Color(0xFFFF4757) else Color(0xFF6C5CE7),
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = if (child.isLocked) (if (isHindi) "फोन लॉक है" else "Phone Locked") else (if (isHindi) "तुरंत लॉक" else "Instant Lock"),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = if (child.isLocked) Color(0xFFFF4757) else Color(0xFF1E1E2E)
                                )
                                Text(
                                    text = if (isHindi) "रिमोट कंट्रोल" else "Remote",
                                    fontSize = 10.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    }

                    // GPS Radar
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { showLocationDialog = true },
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color(0xFF1976D2), modifier = Modifier.size(22.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = if (isHindi) "लोकेशन रडार" else "Live GPS Radar",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = Color(0xFF1E1E2E)
                                )
                                Text(
                                    text = if (isHindi) "${geofenceZones.size} सुरक्षित ज़ोन" else "${geofenceZones.size} Safe Zones",
                                    fontSize = 10.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                }
            }
        }

        // 6. HOW TO OPEN HIDDEN CHILD'S APP BANNER
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp)
                    .clickable { showHiddenGuideDialog = true },
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("💡", fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = if (isHindi) "छिपे हुए चाइल्ड ऐप को कैसे खोलें?" else "How to open the hidden child's app?",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = Color(0xFF1E1E2E)
                        )
                    }
                    Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color(0xFF6C7086))
                }
            }
        }
    }
}

// =========================================================================
// FLASHGET GRID ITEM COMPONENT (With PRO / Beta PRO Badges)
// =========================================================================
@Composable
fun FlashGetGridItem(
    title: String,
    icon: ImageVector,
    iconColor: Color,
    hasPro: Boolean = false,
    isBeta: Boolean = false,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(contentAlignment = Alignment.Center) {
            // Icon container
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(iconColor.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = iconColor,
                    modifier = Modifier.size(28.dp)
                )
            }

            // PRO or Beta PRO Badge
            if (hasPro) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .offset(x = 6.dp, y = 4.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(
                            if (isBeta) Brush.horizontalGradient(listOf(Color(0xFF6C5CE7), Color(0xFFFFB300)))
                            else Brush.horizontalGradient(listOf(Color(0xFFFFB300), Color(0xFFF39C12)))
                        )
                        .padding(horizontal = 4.dp, vertical = 1.dp)
                ) {
                    Text(
                        text = if (isBeta) "Beta PRO" else "PRO",
                        color = Color.White,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = title,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF2D3436),
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            lineHeight = 14.sp
        )
    }
}
