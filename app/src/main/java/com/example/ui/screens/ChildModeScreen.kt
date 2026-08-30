package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.example.R
import com.example.data.model.AppUsageRule
import com.example.data.model.ChildProfile
import com.example.data.model.ScreenRewardTask
import com.example.ui.components.HiddenChildAppGuideDialog
import com.example.ui.components.PinKeypadDialog
import com.example.ui.components.PermissionActionType
import com.example.ui.components.SetupPermissionItem
import com.example.ui.components.checkAllPermissions
import com.example.ui.components.formatMinutes
import com.example.ui.theme.*

@Composable
fun ChildModeScreen(
    child: ChildProfile,
    apps: List<AppUsageRule>,
    tasks: List<ScreenRewardTask>,
    isHindi: Boolean,
    onExitChildMode: () -> Unit,
    onRequestExtraTime: (minutes: Int) -> Unit,
    onCompleteTask: (ScreenRewardTask) -> Unit,
    onVerifyPin: (String) -> Boolean,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var showPinDialog by remember { mutableStateOf(false) }
    var showHideAppGuideDialog by remember { mutableStateOf(false) }
    var showHideConfirmDialog by remember { mutableStateOf(false) }
    var isAppHiddenByParent by remember { mutableStateOf(false) }
    var requestSentSuccess by remember { mutableStateOf(false) }
    var permissionStatusMap by remember { mutableStateOf(checkAllPermissions(context)) }

    // Re-check permissions on resume (when returning from phone settings)
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                permissionStatusMap = checkAllPermissions(context)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val audioPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) {
        permissionStatusMap = checkAllPermissions(context)
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) {
        permissionStatusMap = checkAllPermissions(context)
    }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) {
        permissionStatusMap = checkAllPermissions(context)
    }

    val permissionsList = listOf(
        SetupPermissionItem(
            id = "accessibility",
            titleHi = "1. एक्सेसिबिलिटी सेवा (Accessibility Service)",
            titleEn = "1. Accessibility Service (Crucial)",
            descHi = "रिमोट कंट्रोल, लाइव स्क्रीन देखने और ऐप ब्लॉकर के लिए सबसे आवश्यक।",
            descEn = "Crucial for remote control, screen mirroring, and auto app blocking.",
            icon = Icons.Default.AccessibilityNew,
            iconColor = Color(0xFF6C5CE7),
            isGranted = permissionStatusMap["accessibility"] == true,
            actionType = PermissionActionType.SYSTEM_ACCESSIBILITY
        ),
        SetupPermissionItem(
            id = "audio",
            titleHi = "2. वन-वे ऑडियो व लाइव लिसनिंग (Audio)",
            titleEn = "2. One-Way Audio & Live Listening",
            descHi = "पैरेंट को बच्चे के आसपास की लाइव आवाज़ सुनने की सुविधा देता है।",
            descEn = "Allows parents to listen to real-time ambient surroundings.",
            icon = Icons.Default.Mic,
            iconColor = Color(0xFFFF7675),
            isGranted = permissionStatusMap["audio"] == true,
            actionType = PermissionActionType.RUNTIME_AUDIO
        ),
        SetupPermissionItem(
            id = "camera",
            titleHi = "3. रिमोट कैमरा (Remote Camera)",
            titleEn = "3. Remote Camera View",
            descHi = "पैरेंट को आगे या पीछे का कैमरा लाइव देखने की अनुमति देता है।",
            descEn = "Enables parents to view live front or rear camera streams safely.",
            icon = Icons.Default.Videocam,
            iconColor = Color(0xFF0984E3),
            isGranted = permissionStatusMap["camera"] == true,
            actionType = PermissionActionType.RUNTIME_CAMERA
        ),
        SetupPermissionItem(
            id = "location",
            titleHi = "4. लाइव जीपीएस लोकेशन (GPS Location)",
            titleEn = "4. Live GPS Location Tracking",
            descHi = "सटीक रियल-टाइम स्थान और जियोफेंस अलर्ट के लिए।",
            descEn = "Provides precise live location tracking and geofence safe-zones.",
            icon = Icons.Default.LocationOn,
            iconColor = Color(0xFF00B894),
            isGranted = permissionStatusMap["location"] == true,
            actionType = PermissionActionType.RUNTIME_LOCATION
        ),
        SetupPermissionItem(
            id = "overlay",
            titleHi = "5. अन्य ऐप्स के ऊपर दिखाएं (Overlay Access)",
            titleEn = "5. Display Over Other Apps",
            descHi = "स्क्रीन टाइम ख़त्म होने पर तुरंत लॉक स्क्रीन दिखाने के लिए।",
            descEn = "Required to display instant lockout screens and limits over blocked apps.",
            icon = Icons.Default.Layers,
            iconColor = Color(0xFFE17055),
            isGranted = permissionStatusMap["overlay"] == true,
            actionType = PermissionActionType.SYSTEM_OVERLAY
        ),
        SetupPermissionItem(
            id = "usage_stats",
            titleHi = "6. ऐप उपयोग डेटा (Usage Access)",
            titleEn = "6. App Usage & Screen Time Access",
            descHi = "प्रतिदिन किस ऐप पर कितना समय बिताया गया, ट्रैक करने के लिए।",
            descEn = "Monitors daily screen time and individual app usage minutes.",
            icon = Icons.Default.PieChart,
            iconColor = Color(0xFFFD79A8),
            isGranted = permissionStatusMap["usage_stats"] == true,
            actionType = PermissionActionType.SYSTEM_USAGE_STATS
        ),
        SetupPermissionItem(
            id = "battery",
            titleHi = "7. 24/7 बैकग्राउंड रनिंग (Battery No-Restriction)",
            titleEn = "7. Unrestricted Background Running",
            descHi = "सिस्टम द्वारा ऐप को बैकग्राउंड में बंद होने से रोकने के लिए।",
            descEn = "Prevents Android battery optimization from killing background monitoring.",
            icon = Icons.Default.BatteryChargingFull,
            iconColor = Color(0xFFFDCB6E),
            isGranted = permissionStatusMap["battery"] == true,
            actionType = PermissionActionType.SYSTEM_BATTERY
        ),
        SetupPermissionItem(
            id = "notification_listener",
            titleHi = "8. नोटिफिकेशन व सोशल अलर्ट सिंक (Notifications)",
            titleEn = "8. Notification & Social Message Sync",
            descHi = "WhatsApp, YouTube, SMS की हानिकारक सूचनाओं को तुरंत डिटेक्ट करने के लिए।",
            descEn = "Syncs suspicious social media notifications & keywords to parents.",
            icon = Icons.Default.NotificationsActive,
            iconColor = Color(0xFF6C5CE7),
            isGranted = permissionStatusMap["notification_listener"] == true,
            actionType = PermissionActionType.SYSTEM_NOTIFICATION_LISTENER
        )
    )

    val grantedCount = permissionsList.count { it.isGranted }
    val totalCount = permissionsList.size
    val allGranted = grantedCount == totalCount

    fun handlePermissionAction(item: SetupPermissionItem) {
        try {
            when (item.actionType) {
                PermissionActionType.RUNTIME_AUDIO -> {
                    audioPermissionLauncher.launch(android.Manifest.permission.RECORD_AUDIO)
                }
                PermissionActionType.RUNTIME_CAMERA -> {
                    cameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
                }
                PermissionActionType.RUNTIME_LOCATION -> {
                    locationPermissionLauncher.launch(
                        arrayOf(
                            android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    )
                }
                PermissionActionType.SYSTEM_ACCESSIBILITY -> {
                    val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                    context.startActivity(intent)
                    Toast.makeText(
                        context,
                        if (isHindi) "कृपया 'ParentGuard / FlashGet Kids' सेवा ढूंढें और चालू करें" else "Please find 'ParentGuard / FlashGet Kids' and turn ON",
                        Toast.LENGTH_LONG
                    ).show()
                }
                PermissionActionType.SYSTEM_OVERLAY -> {
                    val intent = Intent(
                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:${context.packageName}")
                    ).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                    context.startActivity(intent)
                }
                PermissionActionType.SYSTEM_USAGE_STATS -> {
                    val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                    context.startActivity(intent)
                    Toast.makeText(
                        context,
                        if (isHindi) "सूची में 'ParentGuard' पर टैप करके 'Allow' करें" else "Tap 'ParentGuard' and enable 'Permit usage access'",
                        Toast.LENGTH_LONG
                    ).show()
                }
                PermissionActionType.SYSTEM_BATTERY -> {
                    val intent = Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                    context.startActivity(intent)
                }
                PermissionActionType.SYSTEM_NOTIFICATION_LISTENER -> {
                    val intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                    context.startActivity(intent)
                }
                PermissionActionType.APP_SETTINGS -> {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", context.packageName, null)
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                    context.startActivity(intent)
                }
            }
        } catch (e: Exception) {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", context.packageName, null)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        }
    }

    if (showHideAppGuideDialog) {
        HiddenChildAppGuideDialog(
            isHindi = isHindi,
            onDismiss = { showHideAppGuideDialog = false }
        )
    }

    if (showHideConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showHideConfirmDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (!isAppHiddenByParent) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = null,
                        tint = Color(0xFF6C5CE7)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isHindi) {
                            if (!isAppHiddenByParent) "ऐप आइकॉन छिपाएं (Hide App)?" else "ऐप आइकॉन पुनः दिखाएं (Unhide)?"
                        } else {
                            if (!isAppHiddenByParent) "Hide App Icon from Child?" else "Unhide App Icon?"
                        },
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = if (isHindi) {
                            if (!isAppHiddenByParent)
                                "यह विकल्प चालू करने पर बच्चे के फोन की होम स्क्रीन और ऐप ड्रॉअर से ऐप का आइकॉन गायब हो जाएगा।\n\nऐप को बाद में खोलने के लिए पैरेंट ऐप से एक्सेस करें।"
                            else
                                "ऐप आइकॉन वापस बच्चे के फोन की होम स्क्रीन पर दिखाई देने लगेगा।"
                        } else {
                            if (!isAppHiddenByParent)
                                "Once enabled, the app icon will disappear from your child's home screen & app drawer.\n\nTo reopen the app anytime, manage from Parent Dashboard."
                            else
                                "The app icon will reappear on the child's home screen."
                        },
                        fontSize = 13.sp,
                        color = Color(0xFF4A4E69)
                    )
                    if (!isAppHiddenByParent) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFFEDE9FF),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                TextButton(
                                    onClick = { showHideAppGuideDialog = true },
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Icon(Icons.Default.HelpOutline, contentDescription = null, tint = Color(0xFF6C5CE7), modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = if (isHindi) "छिपा ऐप कैसे खोलें?" else "How to Open Guide",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF6C5CE7)
                                    )
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        isAppHiddenByParent = !isAppHiddenByParent
                        showHideConfirmDialog = false
                        Toast.makeText(
                            context,
                            if (isHindi) {
                                if (isAppHiddenByParent) "ऐप आइकॉन छिपा दिया गया है (Stealth Mode Active)" else "ऐप आइकॉन पुनः सक्रिय है"
                            } else {
                                if (isAppHiddenByParent) "App icon hidden from child's device!" else "App icon unhidden successfully!"
                            },
                            Toast.LENGTH_LONG
                        ).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C5CE7)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = if (isHindi) (if (!isAppHiddenByParent) "हाँ, छिपाएं" else "हाँ, दिखाएं") else (if (!isAppHiddenByParent) "Yes, Hide Icon" else "Yes, Show Icon"),
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showHideConfirmDialog = false }) {
                    Text(if (isHindi) "रद्द करें" else "Cancel", color = Color(0xFF6C7086))
                }
            },
            shape = RoundedCornerShape(20.dp),
            containerColor = Color.White
        )
    }

    if (showPinDialog) {
        PinKeypadDialog(
            title = if (isHindi) "माता-पिता का पिन दर्ज करें" else "Parent PIN Required",
            subtitle = if (isHindi) "चाइल्ड मोड से बाहर निकलने या अनबाइंड करने के लिए मास्टर पिन दर्ज करें" else "Enter Master PIN to exit or unbind Child Device",
            onPinEntered = { pin ->
                val verified = onVerifyPin(pin)
                if (verified) {
                    showPinDialog = false
                    onExitChildMode()
                }
                verified
            },
            onDismiss = { showPinDialog = false },
            isHindi = isHindi
        )
    }

    // FlashGet Kids for child Pure Screen
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF4F6FB))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            // FlashGet Kids Style Top Brand Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .border(2.dp, Color(0xFF6C5CE7), RoundedCornerShape(14.dp))
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.user_custom_logo_1787213664319),
                            contentDescription = "App Logo",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "FlashGet Kids for child",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 17.sp,
                            color = Color(0xFF1E1E2E)
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(8.dp).background(Color(0xFF00B894), CircleShape))
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = if (isHindi) "पैरेंट से जुड़ा हुआ (Connected)" else "Connected & Active",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF00B894)
                            )
                        }
                    }
                }

                // Unbind / Exit Button (PIN Protected)
                OutlinedButton(
                    onClick = { showPinDialog = true },
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFF4757)),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Icon(Icons.Default.Lock, contentDescription = null, tint = Color(0xFFFF4757), modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(if (isHindi) "अनबाइंड / बाहर" else "Exit / PIN", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF4757))
                }
            }
        }

        // Device & Protection Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = if (isHindi) "लिंक किया गया डिवाइस:" else "Linked Device Profile:",
                                fontSize = 11.sp,
                                color = Color(0xFF6C7086)
                            )
                            Text(
                                text = "${child.name} (${child.deviceModel})",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1E1E2E)
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFFE8F5E9)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Shield, contentDescription = null, tint = Color(0xFF2E7D32), modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(if (isHindi) "सुरक्षा सक्रिय" else "Protected", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    HorizontalDivider(color = Color(0xFFF1F3F9))
                    Spacer(modifier = Modifier.height(14.dp))

                    // Permission Completion Status Bar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isHindi) "सिस्टम अनुमतियां (Setup Progress)" else "Permission Setup Progress",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E1E2E)
                        )
                        Text(
                            text = "$grantedCount / $totalCount ${if (isHindi) "स्वीकृत" else "Granted"}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (allGranted) Color(0xFF00B894) else Color(0xFFE17055)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    LinearProgressIndicator(
                        progress = { grantedCount.toFloat() / totalCount.toFloat() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = if (allGranted) Color(0xFF00B894) else Color(0xFF6C5CE7),
                        trackColor = Color(0xFFECEEF8)
                    )

                    if (allGranted) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF00B894), modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isHindi) "सभी आवश्यक अनुमतियां सक्रिय हैं। यह ऐप बैकग्राउंड में सुरक्षित रूप से चल रहा है।" else "All permissions active! Running silently in background.",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF00B894)
                            )
                        }
                    }
                }
            }
        }

        // Permission Wizard Section Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isHindi) "आवश्यक सुरक्षा अनुमतियां (8 Steps)" else "Required Child Permissions (8 Steps)",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2D3436)
                )
                Text(
                    text = if (isHindi) "FlashGet Setup" else "FlashGet Setup",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF6C5CE7)
                )
            }
        }

        // 8 Permission Items List
        items(permissionsList) { item ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(1.dp, RoundedCornerShape(18.dp))
                    .border(
                        1.dp,
                        if (item.isGranted) Color(0xFFE8F5E9) else Color(0xFFFFECEB),
                        RoundedCornerShape(18.dp)
                    ),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (item.isGranted) Color.White else Color(0xFFFFFBFB)
                )
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
                            .background(item.iconColor.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = null,
                            tint = item.iconColor,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (isHindi) item.titleHi else item.titleEn,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E1E2E)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = if (isHindi) item.descHi else item.descEn,
                            fontSize = 10.sp,
                            color = Color(0xFF6C7086),
                            lineHeight = 13.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    if (item.isGranted) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFFE8F5E9)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Check, contentDescription = null, tint = Color(0xFF2E7D32), modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = if (isHindi) "चालू है" else "ON",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF2E7D32)
                                )
                            }
                        }
                    } else {
                        Button(
                            onClick = { handlePermissionAction(item) },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C5CE7)),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                            modifier = Modifier.height(34.dp)
                        ) {
                            Text(
                                text = if (isHindi) "चालू करें" else "Turn ON",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }

        // FlashGet Kids Stealth Mode: Hide App Icon Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(2.dp, RoundedCornerShape(22.dp))
                    .border(
                        1.5.dp,
                        if (isAppHiddenByParent) Color(0xFF6C5CE7) else Color(0xFFDFE6E9),
                        RoundedCornerShape(22.dp)
                    ),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isAppHiddenByParent) Color(0xFFFAF9FF) else Color.White
                )
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(if (isAppHiddenByParent) Color(0xFF6C5CE7).copy(alpha = 0.15f) else Color(0xFFDFE6E9)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (isAppHiddenByParent) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = null,
                                    tint = if (isAppHiddenByParent) Color(0xFF6C5CE7) else Color(0xFF636E72),
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = if (isHindi) "ऐप आइकॉन छिपाएं (Hide App Icon)" else "Hide App Icon from Child",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1E1E2E)
                                )
                                Text(
                                    text = if (isAppHiddenByParent)
                                        (if (isHindi) "🟢 गुप्त मोड चालू है (आइकॉन छिपा हुआ है)" else "🟢 Stealth Mode ON (Hidden)")
                                    else
                                        (if (isHindi) "⚪ होम स्क्रीन पर दिखाई दे रहा है" else "⚪ Visible on Child's phone"),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (isAppHiddenByParent) Color(0xFF6C5CE7) else Color(0xFF6C7086)
                                )
                            }
                        }

                        Switch(
                            checked = isAppHiddenByParent,
                            onCheckedChange = { showHideConfirmDialog = true },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = Color(0xFF6C5CE7),
                                uncheckedThumbColor = Color.White,
                                uncheckedTrackColor = Color(0xFFDFE6E9)
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = if (isHindi)
                            "जैसे ही यह विकल्प चालू होगा, बच्चे के फोन की होम स्क्रीन से यह ऐप गायब हो जाएगा ताकि बच्चा सेटिंग्स न बदल सके।"
                            else "When enabled, this app icon disappears from child's launcher and home screen for stealth monitoring.",
                        fontSize = 11.sp,
                        color = Color(0xFF6C7086),
                        lineHeight = 15.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Secret Dial Guide and How to open button
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(
                            onClick = { showHideAppGuideDialog = true },
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(Icons.Default.HelpOutline, contentDescription = null, tint = Color(0xFF6C5CE7), modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (isHindi) "छिपा ऐप कैसे खोलें?" else "How to Open Guide",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF6C5CE7)
                            )
                        }
                    }
                }
            }
        }

        // Anti-Uninstall / Background Keep-Alive Guide
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFEDE9FF))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Security, contentDescription = null, tint = Color(0xFF6C5CE7), modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isHindi) "एंटी-अनइंस्टॉल और बैकग्राउंड गार्ड" else "Anti-Uninstall & Background Guardian",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2D1E5E)
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = if (isHindi)
                            "यह चाइल्ड ऐप बैकग्राउंड में हमेशा सक्रिय रहता है ताकि पैरेंट लाइव लोकेशन, कैमरा और ऐप मॉनिटरिंग देख सकें। इसे बंद या अनइंस्टॉल करने के लिए पैरेंट मास्टर पिन की आवश्यकता होती है।"
                            else "This Child App operates in the background to ensure parent monitoring (GPS, Camera, App Limits). Master PIN required to unbind.",
                        fontSize = 11.sp,
                        color = Color(0xFF5A4EB3),
                        lineHeight = 15.sp
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
