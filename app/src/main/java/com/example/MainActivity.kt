package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ChildCare
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.ChildProfile
import com.example.ui.components.AccountProfileDialog
import com.example.ui.components.AddChildDialog
import com.example.ui.components.ChildAvatarCircle
import com.example.ui.components.PinKeypadDialog
import com.example.ui.screens.ActivityLogScreen
import com.example.ui.screens.AppsControlScreen
import com.example.ui.screens.AuthScreen
import com.example.ui.screens.ChildModeScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.RewardsScreen
import com.example.ui.screens.ScheduleRulesScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.ParentalControlViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: ParentalControlViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                ParentGuardMainApp(viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParentGuardMainApp(viewModel: ParentalControlViewModel) {
    val activeChild by viewModel.activeChildProfile.collectAsStateWithLifecycle()
    val allChildren by viewModel.allChildProfiles.collectAsStateWithLifecycle()
    val apps by viewModel.appUsageRules.collectAsStateWithLifecycle()
    val tasks by viewModel.rewardTasks.collectAsStateWithLifecycle()
    val logs by viewModel.activityLogs.collectAsStateWithLifecycle()
    val webRules by viewModel.webFilterRules.collectAsStateWithLifecycle()
    val geofenceZones by viewModel.geofenceZones.collectAsStateWithLifecycle()
    val whatsAppConversations by viewModel.whatsAppConversations.collectAsStateWithLifecycle()
    val smsMessages by viewModel.smsMessages.collectAsStateWithLifecycle()
    val youTubeWatchHistory by viewModel.youTubeWatchHistory.collectAsStateWithLifecycle()
    val appNotifications by viewModel.appNotifications.collectAsStateWithLifecycle()
    val callLogs by viewModel.callLogs.collectAsStateWithLifecycle()
    val currentUser by viewModel.currentUserAccount.collectAsStateWithLifecycle()
    val masterPin by viewModel.masterPin.collectAsStateWithLifecycle()
    val currentLang by viewModel.currentLanguage.collectAsStateWithLifecycle()
    val isChildMode by viewModel.isChildModeActive.collectAsStateWithLifecycle()

    val isHindi = currentLang == "hi"

    var isGuestAuthenticated by remember { mutableStateOf(false) }
    var showAccountProfileDialog by remember { mutableStateOf(false) }
    var selectedNavTab by remember { mutableIntStateOf(1) } // Default to Tab 1: Device (FlashGet Hub)
    var showAddChildDialog by remember { mutableStateOf(false) }
    var showChildDropdown by remember { mutableStateOf(false) }
    var showPinDialogForChildMode by remember { mutableStateOf(false) }

    // If no parent user logged in, show AuthScreen (Email ID Login / Sign Up)
    if (currentUser == null && !isGuestAuthenticated) {
        AuthScreen(
            isHindi = isHindi,
            onLogin = { email, pass, onResult ->
                viewModel.loginWithEmail(email, pass, onResult)
            },
            onSignUp = { name, email, pass, phone, onResult ->
                viewModel.signUpWithEmail(name, email, pass, phone, onResult)
            },
            onGoogleLogin = { email, name ->
                viewModel.loginWithGoogle(email, name)
            },
            onGuestLogin = {
                viewModel.loginWithEmail("musahidraza78600@gmail.com", "123456") { _, _ -> }
                isGuestAuthenticated = true
            }
        )
        return
    }

    if (showAccountProfileDialog) {
        AccountProfileDialog(
            user = currentUser,
            masterPin = masterPin,
            isHindi = isHindi,
            onLogout = {
                viewModel.logoutUser()
                isGuestAuthenticated = false
                showAccountProfileDialog = false
            },
            onDismiss = { showAccountProfileDialog = false }
        )
    }

    if (showAddChildDialog) {
        AddChildDialog(
            onAddChild = { name, age, avatarIdx, limitMins ->
                viewModel.addChildProfile(name, age, avatarIdx, limitMins)
                showAddChildDialog = false
            },
            onDismiss = { showAddChildDialog = false },
            isHindi = isHindi
        )
    }

    if (showPinDialogForChildMode) {
        PinKeypadDialog(
            title = if (isHindi) "पैरेंट मास्टर पिन दर्ज करें" else "Enter Parent Master PIN",
            subtitle = if (isHindi) "चाइल्ड मोड से बाहर निकलने के लिए 4 अंकों का पिन दर्ज करें" else "Enter 4-digit PIN to exit Kid Safe Mode",
            isHindi = isHindi,
            onPinEntered = { enteredPin ->
                val ok = viewModel.verifyPin(enteredPin)
                if (ok) {
                    viewModel.setChildMode(false)
                    showPinDialogForChildMode = false
                }
                ok
            },
            onDismiss = { showPinDialogForChildMode = false }
        )
    }

    val child = activeChild ?: ChildProfile(
        id = 1,
        name = "Aarav",
        age = 10,
        avatarIndex = 0,
        deviceModel = "Infinix X6823C",
        batteryPercent = 25,
        isDeviceOnline = true,
        weekdayLimitMinutes = 120
    )

    // Child Mode View
    if (isChildMode) {
        ChildModeScreen(
            child = child,
            apps = apps,
            tasks = tasks,
            isHindi = isHindi,
            onExitChildMode = {
                showPinDialogForChildMode = true
            },
            onRequestExtraTime = { mins ->
                viewModel.addBonusMinutes(mins, "Child Request")
            },
            onCompleteTask = { task ->
                viewModel.completeTaskByChild(task)
            },
            onVerifyPin = { pin ->
                viewModel.verifyPin(pin)
            }
        )
        return
    }

    // PARENT MODE FLASHGET LAYOUT
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4F6FB)),
        bottomBar = {
            // FLASHGET BOTTOM NAVIGATION: Notice | Device (Elevated center) | Me
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.White,
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .windowInsetsPadding(WindowInsets.navigationBars)
                        .height(64.dp)
                        .padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // TAB 0: NOTICE
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clickable { selectedNavTab = 0 }
                            .padding(8.dp)
                            .testTag("nav_notice")
                    ) {
                        BadgedBox(
                            badge = {
                                if (logs.isNotEmpty()) {
                                    Badge(containerColor = Color(0xFFFF4757)) {
                                        Text("${logs.size.coerceAtMost(9)}", fontSize = 9.sp, color = Color.White)
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Notice",
                                tint = if (selectedNavTab == 0) Color(0xFF6C5CE7) else Color(0xFFA4B0BE),
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = if (isHindi) "नोटिस" else "Notice",
                            fontSize = 11.sp,
                            fontWeight = if (selectedNavTab == 0) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedNavTab == 0) Color(0xFF6C5CE7) else Color(0xFFA4B0BE)
                        )
                    }

                    // TAB 1: DEVICE (Center Elevated Purple Floating Button)
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clickable { selectedNavTab = 1 }
                            .offset(y = (-10).dp)
                            .testTag("nav_device")
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .shadow(6.dp, CircleShape)
                                .clip(CircleShape)
                                .background(
                                    Brush.linearGradient(
                                        listOf(Color(0xFF6C5CE7), Color(0xFF5B48D9))
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Smartphone,
                                contentDescription = "Device",
                                tint = Color.White,
                                modifier = Modifier.size(26.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = if (isHindi) "डिवाइस" else "Device",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (selectedNavTab == 1) Color(0xFF6C5CE7) else Color(0xFFA4B0BE)
                        )
                    }

                    // TAB 2: ME
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clickable { selectedNavTab = 2 }
                            .padding(8.dp)
                            .testTag("nav_me")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Me",
                            tint = if (selectedNavTab == 2) Color(0xFF6C5CE7) else Color(0xFFA4B0BE),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = if (isHindi) "मैं (Me)" else "Me",
                            fontSize = 11.sp,
                            fontWeight = if (selectedNavTab == 2) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedNavTab == 2) Color(0xFF6C5CE7) else Color(0xFFA4B0BE)
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Crossfade(
            targetState = selectedNavTab,
            label = "tab_crossfade",
            modifier = Modifier.padding(paddingValues)
        ) { tab ->
            when (tab) {
                // Tab 0: Notice (Activity logs, alarms, notifications feed)
                0 -> ActivityLogScreen(
                    logs = logs,
                    isHindi = isHindi,
                    onClearLogs = { viewModel.clearLogs() }
                )

                // Tab 1: Device (FlashGet Control Hub)
                1 -> DashboardScreen(
                    child = child,
                    allProfiles = allChildren,
                    apps = apps,
                    logs = logs,
                    geofenceZones = geofenceZones,
                    whatsAppConversations = whatsAppConversations,
                    smsMessages = smsMessages,
                    youTubeWatchHistory = youTubeWatchHistory,
                    appNotifications = appNotifications,
                    callLogs = callLogs,
                    isHindi = isHindi,
                    onSelectChild = { viewModel.selectChild(it) },
                    onInstantLockToggle = { isLock, reason, duration ->
                        viewModel.setInstantLock(isLock, reason, duration)
                    },
                    onToggleBlockAllApps = { viewModel.toggleBlockAllApps(it) },
                    onAddBonusMinutes = { mins, reason ->
                        viewModel.addBonusMinutes(mins, reason)
                    },
                    onAddGeofence = { name, address, radius ->
                        viewModel.addGeofenceZone(name, name, address, radius)
                    },
                    onDeleteGeofence = { zoneId ->
                        viewModel.deleteGeofenceZone(zoneId)
                    },
                    onClearNotifications = { viewModel.clearNotifications() },
                    onToggleAntiUninstall = { enabled, preventSettings, preventReset ->
                        viewModel.toggleAntiUninstallProtection(enabled, preventSettings, preventReset)
                    },
                    onDeleteCallLog = { callLogId ->
                        viewModel.deleteCallLog(callLogId)
                    },
                    onClearCallLogs = { viewModel.clearCallLogs() },
                    onClearSmsLogs = { viewModel.clearSmsMessages() },
                    onClearYouTubeHistory = { viewModel.clearYouTubeWatchHistory() }
                )

                // Tab 2: Me (Settings, Parent Account, Child Mode, Pin, Language)
                2 -> Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFF4F6FB))
                        .padding(16.dp)
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(CircleShape)
                                    .border(2.dp, Color(0xFF6C5CE7), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.user_custom_logo_1787213664319),
                                    contentDescription = "User Photo Logo",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                            Spacer(modifier = Modifier.width(14.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = currentUser?.name ?: "Musahid Raza",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp,
                                    color = Color(0xFF1E1E2E)
                                )
                                Text(
                                    text = currentUser?.email ?: "musahidraza78600@gmail.com",
                                    fontSize = 12.sp,
                                    color = Color(0xFF6C7086)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                            // Language switch
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { viewModel.toggleLanguage() }
                                    .padding(vertical = 12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Language, contentDescription = null, tint = Color(0xFF6C5CE7))
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(if (isHindi) "भाषा (Language)" else "Language", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                                }
                                Text(if (isHindi) "हिन्दी" else "English", color = Color(0xFF6C5CE7), fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }

                            HorizontalDivider(color = Color(0xFFECEFF8))

                            // Enter Kid Safe Mode
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { viewModel.setChildMode(true) }
                                    .padding(vertical = 12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.ChildCare, contentDescription = null, tint = Color(0xFF00B894))
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(if (isHindi) "चाइल्ड सेफ मोड चालू करें" else "Switch to Kid Mode", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                                }
                                Text(">", color = Color.Gray)
                            }

                            HorizontalDivider(color = Color(0xFFECEFF8))

                            // Account Profile
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { showAccountProfileDialog = true }
                                    .padding(vertical = 12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Settings, contentDescription = null, tint = Color(0xFF6C5CE7))
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(if (isHindi) "खाता और सुरक्षा सेटिंग्स" else "Account & Security Settings", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                                }
                                Text(">", color = Color.Gray)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // App Logo & Version Card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .border(1.5.dp, Color(0xFF6C5CE7), RoundedCornerShape(12.dp))
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.user_custom_logo_1787213664319),
                                    contentDescription = "App Icon",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("parent cantrol md", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color(0xFF1E1E2E))
                                Text(if (isHindi) "कस्टम ऐप लोगो एक्टिव • v1.0" else "Custom App Logo Active • v1.0", fontSize = 11.sp, color = Color(0xFF6C5CE7))
                            }
                        }
                    }
                }
            }
        }
    }
}
