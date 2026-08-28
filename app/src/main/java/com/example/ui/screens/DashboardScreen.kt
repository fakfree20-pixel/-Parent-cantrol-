package com.example.ui.screens

import android.widget.Toast
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
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.platform.LocalContext
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
import com.example.ui.components.SubscriptionUpgradeDialog
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
    onSyncCloud: () -> Unit = {},
    onDeleteCallLog: (Long) -> Unit = {},
    onClearCallLogs: () -> Unit = {},
    onClearSmsLogs: () -> Unit = {},
    onClearYouTubeHistory: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

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
    var showSubscriptionDialog by remember { mutableStateOf(false) }

    // FlashGet New Feature Dialogs
    var showLivePaintingDialog by remember { mutableStateOf(false) }
    var showCheckPermissionsDialog by remember { mutableStateOf(false) }
    var showSocialAppDialog by remember { mutableStateOf(false) }
    var showAlbumsSafetyDialog by remember { mutableStateOf(false) }
    var showBrowserSafetyDialog by remember { mutableStateOf(false) }
    var showHiddenGuideDialog by remember { mutableStateOf(false) }

    // Gemini AI Safety Insights & Remote Camera States
    var isAiLockApplied by remember { mutableStateOf(false) }
    var aiAnalysisReport by remember {
        mutableStateOf(
            if (isHindi)
                "✨ AI विश्लेषण: बच्चे ने देर रात YouTube पर 45 मिनट और Free Fire गेमिंग पर 30 मिनट बिताए हैं। AI की सिफारिश है कि रात 9 बजे के बाद गेमिंग ऐप्स ब्लॉक करें और दैनिक स्क्रीन लिमिट 1.5 घंटे निर्धारित करें।"
            else
                "✨ AI Analysis: Child spent 45m on YouTube late evening and 30m on gaming. AI recommends restricting gaming apps after 9 PM and setting a 1.5h daily screentime limit."
        )
    }

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

    if (showSubscriptionDialog) {
        SubscriptionUpgradeDialog(
            isHindi = isHindi,
            onDismiss = { showSubscriptionDialog = false }
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
        // 1. TOP PURPLE DEVICE HEADER OR PAIRING PROMPT (Show only when connected/added)
        item {
            if (allProfiles.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                listOf(Color(0xFF7562EB), Color(0xFF5E4BD8))
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
                                            .size(8.dp)
                                            .clip(CircleShape)
                                            .background(if (child.isDeviceOnline) Color(0xFF2ED573) else Color.LightGray)
                                    )
                                    Spacer(modifier = Modifier.width(5.dp))
                                    Text(
                                        text = if (child.isDeviceOnline) "Online" else "Offline",
                                        fontSize = 12.sp,
                                        color = Color.White.copy(alpha = 0.95f),
                                        fontWeight = FontWeight.Medium
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = "🔋 69%",
                                        fontSize = 12.sp,
                                        color = Color.White.copy(alpha = 0.95f),
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
                                                    Text("${profile.deviceModel} • 🔋 69%", fontSize = 11.sp, color = Color.Gray)
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

                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            // Cloud Sync Button
                            IconButton(
                                onClick = {
                                    onSyncCloud()
                                    Toast.makeText(
                                        context,
                                        if (isHindi) "☁️ क्लाउड सिंक सफल (Cloud Synced)" else "☁️ Cloud Synced Successfully",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                },
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.2f))
                            ) {
                                Icon(Icons.Default.Sync, contentDescription = "Sync Cloud", tint = Color.White)
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
            } else {
                // No child device connected prompt with 10-digit pairing code
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFEDE9FF)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = if (isHindi) "कोई चाइल्ड डिवाइस कनेक्टेड नहीं है" else "No Child Device Connected",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF281D5E)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = if (isHindi) "अपने बच्चे के फोन को लिंक करने के लिए 10-डिजिट कनेक्शन कोड शेयर करें:" else "Share this 10-digit connection code with your child's phone:",
                            fontSize = 13.sp,
                            color = Color(0xFF6B609E),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color.White,
                            shadowElevation = 2.dp
                        ) {
                            Text(
                                text = "9839247105",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF6C5CE7),
                                modifier = Modifier.padding(horizontal = 24.dp, vertical = 10.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { showPairDeviceDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C5CE7)),
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(if (isHindi) "चाइल्ड डिवाइस जोड़ें (Pair Device)" else "Pair Child Device", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // 2. USAGE REPORT CARD (FlashGet Screenshot Card 2)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp)
                    .clickable { showUsageReportDialog = true },
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = if (isHindi) "उपयोग रिपोर्ट >" else "Usage Report >",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1E1E2E)
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = if (isHindi) "स्क्रीन टाइम: 10 min" else "Screen Time: 10 min",
                            fontSize = 13.sp,
                            color = Color(0xFF718096),
                            fontWeight = FontWeight.Medium
                        )
                    }

                    // 3D Isometric Bar Chart Illustration
                    Isometric3DChartIllustration()
                }
            }
        }

        // 3. GEMINI AI SAFETY REPORT CARD (✨ Gemini AI Safety Report & Smart Lock Controls)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFAF8FF)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE0D0F0)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Header Row with Gradient Sparkle & LIVE Badge
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "✨",
                                fontSize = 18.sp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isHindi) "Gemini AI सुरक्षा रिपोर्ट" else "Gemini AI Safety Report",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 16.sp,
                                color = Color(0xFF4A148C)
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFE1BEE7)
                        ) {
                            Text(
                                text = "LIVE",
                                color = Color(0xFF4A148C),
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(Color(0xFFE0D0F0))
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    // AI Generated Analysis Text
                    Text(
                        text = aiAnalysisReport,
                        fontSize = 13.sp,
                        lineHeight = 18.sp,
                        color = Color(0xFF2D3436),
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Safety Warning Banner
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFFFEBEE),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFCDD2)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "⚠️",
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = if (isHindi) "सुरक्षा चेतावनी: देर रात स्क्रीन उपयोग" else "Safety Warning: Late Night Screentime",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFD32F2F)
                                )
                                Text(
                                    text = if (isHindi) "YouTube और गेमिंग का उपयोग 10 PM के बाद डिटेक्ट हुआ है।" else "High night-time screen & YouTube gaming activity detected.",
                                    fontSize = 11.sp,
                                    color = Color(0xFFC62828)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Apply AI Lock Settings Button
                    Button(
                        onClick = {
                            isAiLockApplied = true
                            onInstantLockToggle(true, "AI Smart Screen Restriction Applied", 60)
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isAiLockApplied) Color(0xFF2E7D32) else Color(0xFF6A1B9A)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = if (isAiLockApplied) {
                                if (isHindi) "✓ AI सुरक्षा सेटिंग्स लागू हो गई" else "✓ AI Lock Settings Applied"
                            } else {
                                if (isHindi) "AI सुरक्षा सेटिंग्स लागू करें (Apply AI Lock)" else "Apply AI Lock Settings"
                            },
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Quick Remote Camera Snaps Control Row (Front Snap & Back Snap)
                    Text(
                        text = if (isHindi) "📷 रिमोट कैमरा त्वरित स्नैप (Remote Camera Snaps)" else "📷 Remote Camera Controls",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4A148C)
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { showCameraDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00897B)),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = if (isHindi) "📷 फ्रंट स्नैप" else "Front Snap",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Button(
                            onClick = { showCameraDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00897B)),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = if (isHindi) "📷 बैक स्नैप" else "Back Snap",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // 4. LIVE MONITORING SECTION (FlashGet Screenshot Card 3)
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
                    // Header Row: "Live Monitoring" + Settings Icon
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isHindi) "लाइव मॉनिटरिंग" else "Live Monitoring",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color(0xFF1E1E2E)
                        )
                        IconButton(
                            onClick = { showSubscriptionDialog = true },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "Settings",
                                tint = Color(0xFF718096),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // 3 Quick Monitoring Tiles (Remote Camera | Screen Mirroring | One-Way Audio)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        LiveMonitoringTile(
                            title = if (isHindi) "रिमोट कैमरा" else "Remote Camera",
                            icon = Icons.Default.PhotoCamera,
                            iconColor = Color(0xFF6C5CE7),
                            badgeText = "Trial",
                            onClick = { showCameraDialog = true },
                            modifier = Modifier.weight(1f)
                        )
                        LiveMonitoringTile(
                            title = if (isHindi) "स्क्रीन मिररिंग" else "Screen Mirroring",
                            icon = Icons.Default.PhoneAndroid,
                            iconColor = Color(0xFF00B894),
                            badgeText = "Trial",
                            onClick = { showScreenMirrorDialog = true },
                            modifier = Modifier.weight(1f)
                        )
                        LiveMonitoringTile(
                            title = if (isHindi) "वन-वे ऑडियो" else "One-Way Audio",
                            icon = Icons.Default.Headphones,
                            iconColor = Color(0xFF0984E3),
                            badgeText = "Trial",
                            onClick = { showAudioDialog = true },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        // 5. BLOCK ALL APPS CARD (FlashGet Screenshot Card 4)
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
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 3D Apps Lock Graphic
                    AppLock3DBadge()

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (isHindi) "सभी ऐप्स ब्लॉक करें" else "Block All Apps",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color(0xFF1E1E2E)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = if (isHindi) "अनुमत ऐप्स के अलावा सभी ऐप्स ब्लॉक हो जाएंगे" else "All apps except for \"Allowed Apps\" will be blocked",
                            fontSize = 11.sp,
                            color = Color(0xFF718096),
                            lineHeight = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Switch(
                        checked = child.blockAllApps,
                        onCheckedChange = { onToggleBlockAllApps(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = Color(0xFF6C5CE7),
                            uncheckedThumbColor = Color.White,
                            uncheckedTrackColor = Color(0xFFE2E8F0)
                        )
                    )
                }
            }
        }

        // 6. LIVE LOCATION CARD (FlashGet Screenshot Card 5)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp)
                    .clickable { showLocationDialog = true },
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isHindi) "लाइव लोकेशन >" else "Live Location >",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color(0xFF1E1E2E)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Map Preview View with Centered Child Pin
                    MapPreviewVectorCard(child = child)
                }
            }
        }

        // 7. SECTION: SNAPSHOT & RECORDING (FlashGet Section 1)
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

        // 8. SECTION: DEVICE ACTIVITY (FlashGet Section 2)
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

        // 9. SECTION: USAGE SAFETY (FlashGet Section 3)
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

        // 10. QUICK ACTIONS: INSTANT LOCK & GPS RADAR
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

        // 11. HOW TO OPEN HIDDEN CHILD'S APP BANNER
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
// FLASHGET SCREENSHOT SPECIALIZED COMPONENTS
// =========================================================================

@Composable
fun LiveMonitoringTile(
    title: String,
    icon: ImageVector,
    iconColor: Color,
    badgeText: String = "Trial",
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(contentAlignment = Alignment.Center) {
            // Icon Background
            Box(
                modifier = Modifier
                    .size(56.dp)
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

            // Cyan/Blue "Trial" Badge Pill (matching screenshot)
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 6.dp, y = (-4).dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color(0xFF00D2D3), Color(0xFF0984E3))
                        )
                    )
                    .padding(horizontal = 5.dp, vertical = 1.5.dp)
            ) {
                Text(
                    text = badgeText,
                    color = Color.White,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 0.5.sp
                )
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
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun Vip3DStarIllustration(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.size(56.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(52.dp)) {
            val center = Offset(size.width * 0.5f, size.height * 0.52f)
            val starRadius = size.width * 0.38f

            // Outer golden glow
            drawCircle(
                brush = Brush.radialGradient(
                    listOf(Color(0xFFFFEAA7).copy(alpha = 0.8f), Color.Transparent),
                    center = center,
                    radius = starRadius * 1.3f
                ),
                radius = starRadius * 1.2f,
                center = center
            )

            // Sparkle 1 (Top Left)
            val sp1Center = Offset(size.width * 0.2f, size.height * 0.2f)
            drawCircle(Color(0xFFFFD700), radius = 3.dp.toPx(), center = sp1Center)
            drawLine(Color.White, Offset(sp1Center.x - 6.dp.toPx(), sp1Center.y), Offset(sp1Center.x + 6.dp.toPx(), sp1Center.y), strokeWidth = 1.5.dp.toPx())
            drawLine(Color.White, Offset(sp1Center.x, sp1Center.y - 6.dp.toPx()), Offset(sp1Center.x, sp1Center.y + 6.dp.toPx()), strokeWidth = 1.5.dp.toPx())

            // Sparkle 2 (Bottom Right)
            val sp2Center = Offset(size.width * 0.82f, size.height * 0.78f)
            drawCircle(Color(0xFFFFD700), radius = 2.dp.toPx(), center = sp2Center)
            drawLine(Color.White, Offset(sp2Center.x - 4.dp.toPx(), sp2Center.y), Offset(sp2Center.x + 4.dp.toPx(), sp2Center.y), strokeWidth = 1.dp.toPx())
            drawLine(Color.White, Offset(sp2Center.x, sp2Center.y - 4.dp.toPx()), Offset(sp2Center.x, sp2Center.y + 4.dp.toPx()), strokeWidth = 1.dp.toPx())

            // Main 3D Gold Star
            val path = androidx.compose.ui.graphics.Path()
            val points = 5
            val innerRadius = starRadius * 0.45f
            for (i in 0 until points * 2) {
                val r = if (i % 2 == 0) starRadius else innerRadius
                val angle = (i * Math.PI / points) - (Math.PI / 2.0)
                val x = (center.x + r * Math.cos(angle)).toFloat()
                val y = (center.y + r * Math.sin(angle)).toFloat()
                if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
            }
            path.close()

            // 3D Star Gradient
            drawPath(
                path = path,
                brush = Brush.linearGradient(
                    listOf(Color(0xFFFFF099), Color(0xFFFFC048), Color(0xFFFF9F1A)),
                    start = Offset(0f, 0f),
                    end = Offset(size.width, size.height)
                )
            )

            // Inner facet highlight
            val highlightPath = androidx.compose.ui.graphics.Path()
            highlightPath.moveTo(center.x, center.y - starRadius)
            highlightPath.lineTo(center.x, center.y)
            highlightPath.lineTo(center.x + starRadius * 0.8f, center.y)
            highlightPath.close()
            drawPath(
                path = highlightPath,
                color = Color.White.copy(alpha = 0.35f)
            )
        }
    }
}

@Composable
fun Isometric3DChartIllustration(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.size(62.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(58.dp)) {
            val w = size.width
            val h = size.height

            // Base Isometric Platform
            val basePath = androidx.compose.ui.graphics.Path().apply {
                moveTo(w * 0.5f, h * 0.72f)
                lineTo(w * 0.88f, h * 0.84f)
                lineTo(w * 0.5f, h * 0.98f)
                lineTo(w * 0.12f, h * 0.84f)
                close()
            }
            drawPath(
                path = basePath,
                brush = Brush.linearGradient(
                    listOf(Color(0xFFE0E7FF), Color(0xFFC7D2FE))
                )
            )

            // Column 1 (Left - Green)
            drawIsometricColumn(
                centerX = w * 0.32f,
                baseY = h * 0.76f,
                barWidth = 11.dp.toPx(),
                barHeight = 22.dp.toPx(),
                topColor = Color(0xFF55EFC4),
                leftColor = Color(0xFF00B894),
                rightColor = Color(0xFF008B6B)
            )

            // Column 2 (Center - Purple)
            drawIsometricColumn(
                centerX = w * 0.52f,
                baseY = h * 0.82f,
                barWidth = 12.dp.toPx(),
                barHeight = 32.dp.toPx(),
                topColor = Color(0xFFA29BFE),
                leftColor = Color(0xFF6C5CE7),
                rightColor = Color(0xFF5B48D9)
            )

            // Column 3 (Right - Orange/Yellow)
            drawIsometricColumn(
                centerX = w * 0.72f,
                baseY = h * 0.78f,
                barWidth = 11.dp.toPx(),
                barHeight = 18.dp.toPx(),
                topColor = Color(0xFFFFEAA7),
                leftColor = Color(0xFFFDCB6E),
                rightColor = Color(0xFFE17055)
            )

            // Connector node dot
            drawCircle(Color(0xFF6C5CE7), radius = 2.5.dp.toPx(), center = Offset(w * 0.52f, h * 0.82f - 32.dp.toPx()))
        }
    }
}

fun androidx.compose.ui.graphics.drawscope.DrawScope.drawIsometricColumn(
    centerX: Float,
    baseY: Float,
    barWidth: Float,
    barHeight: Float,
    topColor: Color,
    leftColor: Color,
    rightColor: Color
) {
    val halfW = barWidth / 2f
    val depth = halfW * 0.55f
    val topY = baseY - barHeight

    // Left Face
    val leftPath = androidx.compose.ui.graphics.Path().apply {
        moveTo(centerX - halfW, topY)
        lineTo(centerX, topY + depth)
        lineTo(centerX, baseY + depth)
        lineTo(centerX - halfW, baseY)
        close()
    }
    drawPath(leftPath, leftColor)

    // Right Face
    val rightPath = androidx.compose.ui.graphics.Path().apply {
        moveTo(centerX, topY + depth)
        lineTo(centerX + halfW, topY)
        lineTo(centerX + halfW, baseY)
        lineTo(centerX, baseY + depth)
        close()
    }
    drawPath(rightPath, rightColor)

    // Top Diamond Face
    val topPath = androidx.compose.ui.graphics.Path().apply {
        moveTo(centerX, topY - depth)
        lineTo(centerX + halfW, topY)
        lineTo(centerX, topY + depth)
        lineTo(centerX - halfW, topY)
        close()
    }
    drawPath(topPath, topColor)
}

@Composable
fun AppLock3DBadge(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.size(46.dp),
        contentAlignment = Alignment.Center
    ) {
        // App tiles stack
        Box(
            modifier = Modifier
                .size(36.dp)
                .offset(x = (-4).dp, y = (-2).dp)
                .clip(RoundedCornerShape(10.dp))
                .background(
                    Brush.linearGradient(
                        listOf(Color(0xFF74B9FF), Color(0xFF0984E3))
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Apps, contentDescription = null, tint = Color.White.copy(alpha = 0.85f), modifier = Modifier.size(20.dp))
        }

        // Secondary subtle tile
        Box(
            modifier = Modifier
                .size(34.dp)
                .offset(x = 6.dp, y = 4.dp)
                .clip(RoundedCornerShape(9.dp))
                .background(
                    Brush.linearGradient(
                        listOf(Color(0xFFFF7675), Color(0xFFD63031))
                    )
                )
        )

        // Shiny Gold Padlock Badge in foreground
        Box(
            modifier = Modifier
                .size(26.dp)
                .align(Alignment.BottomEnd)
                .offset(x = 4.dp, y = 2.dp)
                .shadow(3.dp, CircleShape)
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(
                        listOf(Color(0xFFFFEAA7), Color(0xFFF39C12))
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(15.dp)
            )
        }
    }
}

@Composable
fun MapPreviewVectorCard(
    child: ChildProfile,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(130.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFEBF5EB))
    ) {
        // Map Vector Canvas
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // Park / Green zones
            drawRect(
                color = Color(0xFFD8F3DC),
                topLeft = Offset(w * 0.1f, h * 0.15f),
                size = androidx.compose.ui.geometry.Size(w * 0.35f, h * 0.45f)
            )
            drawRect(
                color = Color(0xFFD8F3DC),
                topLeft = Offset(w * 0.6f, h * 0.5f),
                size = androidx.compose.ui.geometry.Size(w * 0.3f, h * 0.4f)
            )

            // River / Blue curve
            val riverPath = androidx.compose.ui.graphics.Path().apply {
                moveTo(0f, h * 0.85f)
                cubicTo(w * 0.3f, h * 0.8f, w * 0.7f, h * 0.95f, w, h * 0.75f)
                lineTo(w, h)
                lineTo(0f, h)
                close()
            }
            drawPath(riverPath, Color(0xFFBEE3F8))

            // Road Network
            val roadColor = Color.White
            val roadBorder = Color(0xFFD0D7DE)

            // Horizontal Road 1
            drawLine(roadBorder, Offset(0f, h * 0.48f), Offset(w, h * 0.48f), strokeWidth = 14.dp.toPx())
            drawLine(roadColor, Offset(0f, h * 0.48f), Offset(w, h * 0.48f), strokeWidth = 11.dp.toPx())

            // Horizontal Road 2
            drawLine(roadBorder, Offset(0f, h * 0.22f), Offset(w, h * 0.22f), strokeWidth = 10.dp.toPx())
            drawLine(roadColor, Offset(0f, h * 0.22f), Offset(w, h * 0.22f), strokeWidth = 7.dp.toPx())

            // Vertical Road 1
            drawLine(roadBorder, Offset(w * 0.42f, 0f), Offset(w * 0.42f, h), strokeWidth = 14.dp.toPx())
            drawLine(roadColor, Offset(w * 0.42f, 0f), Offset(w * 0.42f, h), strokeWidth = 11.dp.toPx())

            // Diagonal Road
            drawLine(roadBorder, Offset(w * 0.7f, 0f), Offset(w * 0.85f, h), strokeWidth = 10.dp.toPx())
            drawLine(roadColor, Offset(w * 0.7f, 0f), Offset(w * 0.85f, h), strokeWidth = 7.dp.toPx())

            // Geofence Safe Radius circle around child
            drawCircle(
                color = Color(0xFF6C5CE7).copy(alpha = 0.12f),
                radius = 38.dp.toPx(),
                center = Offset(w * 0.42f, h * 0.48f)
            )
            drawCircle(
                color = Color(0xFF6C5CE7).copy(alpha = 0.4f),
                radius = 38.dp.toPx(),
                center = Offset(w * 0.42f, h * 0.48f),
                style = androidx.compose.ui.graphics.drawscope.Stroke(
                    width = 1.5.dp.toPx(),
                    pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                )
            )
        }

        // Center Location Pin Marker with Child Avatar
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = (-10).dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Outer Pin Bubble
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .shadow(6.dp, CircleShape)
                        .clip(CircleShape)
                        .background(Color(0xFF6C5CE7))
                        .padding(2.dp),
                    contentAlignment = Alignment.Center
                ) {
                    ChildAvatarCircle(
                        avatarIndex = child.avatarIndex,
                        name = child.name,
                        size = 36.dp
                    )
                }
                // Pin Point Triangle
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .offset(y = (-2).dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(Color(0xFF6C5CE7))
                )
            }
        }

        // Bottom Info Pill overlay: Address / Geofence info
        Surface(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(8.dp),
            shape = RoundedCornerShape(8.dp),
            color = Color.White.copy(alpha = 0.92f),
            shadowElevation = 2.dp
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF2ED573))
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = child.locationAddress.ifEmpty { "Green Park Avenue, Block 4" },
                    fontSize = 10.sp,
                    color = Color(0xFF1E1E2E),
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
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

