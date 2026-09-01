package com.example.ui.components

import android.Manifest
import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.example.ui.theme.*

data class SetupPermissionItem(
    val id: String,
    val titleHi: String,
    val titleEn: String,
    val descHi: String,
    val descEn: String,
    val icon: ImageVector,
    val iconColor: Color,
    val isGranted: Boolean,
    val isCrucial: Boolean = true,
    val actionType: PermissionActionType
)

enum class PermissionActionType {
    RUNTIME_AUDIO,
    RUNTIME_CAMERA,
    RUNTIME_LOCATION,
    SYSTEM_ACCESSIBILITY,
    SYSTEM_OVERLAY,
    SYSTEM_USAGE_STATS,
    SYSTEM_BATTERY,
    SYSTEM_NOTIFICATION_LISTENER,
    APP_SETTINGS
}

fun checkAllPermissions(context: Context): Map<String, Boolean> {
    val result = mutableMapOf<String, Boolean>()

    // 1. Audio / Mic
    result["audio"] = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.RECORD_AUDIO
    ) == PackageManager.PERMISSION_GRANTED

    // 2. Camera
    result["camera"] = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED

    // 3. Location
    result["location"] = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    // 4. Overlay / Draw over other apps
    result["overlay"] = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        Settings.canDrawOverlays(context)
    } else {
        true
    }

    // 5. Usage Stats
    result["usage_stats"] = try {
        val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            appOps.unsafeCheckOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(),
                context.packageName
            )
        } else {
            appOps.checkOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(),
                context.packageName
            )
        }
        mode == AppOpsManager.MODE_ALLOWED
    } catch (e: Exception) {
        false
    }

    // 6. Battery Optimization Ignored
    result["battery"] = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val powerManager = context.getSystemService(Context.POWER_SERVICE) as? PowerManager
        powerManager?.isIgnoringBatteryOptimizations(context.packageName) ?: false
    } else {
        true
    }

    // 7. Accessibility
    result["accessibility"] = try {
        val enabledServices = Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        ) ?: ""
        enabledServices.contains(context.packageName, ignoreCase = true)
    } catch (e: Exception) {
        false
    }

    // 8. Notification Listener
    result["notification_listener"] = try {
        val flat = Settings.Secure.getString(
            context.contentResolver,
            "enabled_notification_listeners"
        ) ?: ""
        flat.contains(context.packageName, ignoreCase = true)
    } catch (e: Exception) {
        false
    }

    return result
}

@Composable
fun ChildPermissionSetupDialog(
    isHindi: Boolean,
    onDismiss: () -> Unit,
    onAllPermissionsGranted: (() -> Unit)? = null
) {
    val context = LocalContext.current
    var permissionStatusMap by remember { mutableStateOf(checkAllPermissions(context)) }
    val lifecycleOwner = LocalLifecycleOwner.current

    // Automatically refresh permission statuses when user returns from phone settings to the app!
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

    // Runtime Permission Launchers
    val audioPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        permissionStatusMap = checkAllPermissions(context)
        if (granted) {
            Toast.makeText(context, if (isHindi) "ऑडियो अनुमति स्वीकृत!" else "Audio Permission Granted!", Toast.LENGTH_SHORT).show()
        }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        permissionStatusMap = checkAllPermissions(context)
        if (granted) {
            Toast.makeText(context, if (isHindi) "कैमरा अनुमति स्वीकृत!" else "Camera Permission Granted!", Toast.LENGTH_SHORT).show()
        }
    }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        permissionStatusMap = checkAllPermissions(context)
        val isFine = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
        if (isFine) {
            Toast.makeText(context, if (isHindi) "जीपीएस लोकेशन अनुमति स्वीकृत!" else "GPS Location Permission Granted!", Toast.LENGTH_SHORT).show()
        }
    }

    val permissionsList = listOf(
        SetupPermissionItem(
            id = "audio",
            titleHi = "1. वन-वे ऑडियो व लाइव लिसनिंग",
            titleEn = "1. One-Way Audio & Live Listening",
            descHi = "पैरेंट को बच्चे के आसपास की आवाज़ सुनने व सुरक्षा जांचने के लिए",
            descEn = "Allows parent to listen to child surroundings for safety",
            icon = Icons.Default.Mic,
            iconColor = Color(0xFF0984E3),
            isGranted = permissionStatusMap["audio"] == true,
            actionType = PermissionActionType.RUNTIME_AUDIO
        ),
        SetupPermissionItem(
            id = "camera",
            titleHi = "2. रिमोट कैमरा (Remote Camera)",
            titleEn = "2. Remote Camera View",
            descHi = "आपात स्थिति में रिमोटली कैमरा ऑन करके माहौल देखने के लिए",
            descEn = "Enables parents to view live surroundings in emergency",
            icon = Icons.Default.CameraAlt,
            iconColor = Color(0xFF6C5CE7),
            isGranted = permissionStatusMap["camera"] == true,
            actionType = PermissionActionType.RUNTIME_CAMERA
        ),
        SetupPermissionItem(
            id = "location",
            titleHi = "3. लाइव जीपीएस लोकेशन ट्रैकिंग",
            titleEn = "3. Live GPS Location Tracking",
            descHi = "बच्चे की 24/7 सटीक लोकेशन और सेफ ज़ोन अलर्ट्स के लिए",
            descEn = "Real-time location monitoring and geofence alerts",
            icon = Icons.Default.LocationOn,
            iconColor = Color(0xFF00B894),
            isGranted = permissionStatusMap["location"] == true,
            actionType = PermissionActionType.RUNTIME_LOCATION
        ),
        SetupPermissionItem(
            id = "accessibility",
            titleHi = "4. एक्सेसिबिलिटी सेवा (Accessibility Service)",
            titleEn = "4. Accessibility Service (Crucial)",
            descHi = "रिमोट स्क्रीन देखने, कीलॉगर व अनचाहे ऐप्स को रोकने के लिए सबसे ज़रूरी",
            descEn = "Essential for remote screen mirroring & automatic app blocking",
            icon = Icons.Default.AccessibilityNew,
            iconColor = Color(0xFFE17055),
            isGranted = permissionStatusMap["accessibility"] == true,
            actionType = PermissionActionType.SYSTEM_ACCESSIBILITY
        ),
        SetupPermissionItem(
            id = "overlay",
            titleHi = "5. अन्य ऐप्स के ऊपर दिखाएं (Overlay Access)",
            titleEn = "5. Display Over Other Apps",
            descHi = "समय खत्म होने पर स्क्रीन लॉक व ऐप ब्लॉक करने के लिए",
            descEn = "Needed to lock child screen and show block screens",
            icon = Icons.Default.Layers,
            iconColor = Color(0xFFD63031),
            isGranted = permissionStatusMap["overlay"] == true,
            actionType = PermissionActionType.SYSTEM_OVERLAY
        ),
        SetupPermissionItem(
            id = "usage_stats",
            titleHi = "6. ऐप उपयोग डेटा (Usage Access)",
            titleEn = "6. App Usage & Screen Time Access",
            descHi = "बच्चे द्वारा उपयोग किए गए सभी ऐप्स का स्क्रीन टाइम रिकॉर्ड करने के लिए",
            descEn = "Records daily app usage and screen time stats",
            icon = Icons.Default.BarChart,
            iconColor = Color(0xFFFD9644),
            isGranted = permissionStatusMap["usage_stats"] == true,
            actionType = PermissionActionType.SYSTEM_USAGE_STATS
        ),
        SetupPermissionItem(
            id = "battery",
            titleHi = "7. 24/7 बैकग्राउंड रनिंग (Battery No-Restriction)",
            titleEn = "7. Unrestricted Background Running",
            descHi = "फोन लॉक होने पर भी ऐप को बंद होने से बचाने के लिए",
            descEn = "Prevents Android from killing monitoring service when locked",
            icon = Icons.Default.BatteryChargingFull,
            iconColor = Color(0xFF26DE81),
            isGranted = permissionStatusMap["battery"] == true,
            actionType = PermissionActionType.SYSTEM_BATTERY
        ),
        SetupPermissionItem(
            id = "notification_listener",
            titleHi = "8. नोटिफिकेशन व सोशल मैसेज अलर्ट",
            titleEn = "8. Notification & Social Message Sync",
            descHi = "व्हाट्सएप, यूट्यूब व चैट नोटिफिकेशन को सिंक करने के लिए",
            descEn = "Syncs WhatsApp, YouTube, and SMS notification alerts",
            icon = Icons.Default.NotificationsActive,
            iconColor = Color(0xFF4B7BEC),
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
                    audioPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                }
                PermissionActionType.RUNTIME_CAMERA -> {
                    cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                }
                PermissionActionType.RUNTIME_LOCATION -> {
                    locationPermissionLauncher.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
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
                        if (isHindi) "Accessibility / Downloaded apps में 'parent cantrol md - Child Guardian' को ON करें" else "In Accessibility / Downloaded apps, turn ON 'parent cantrol md - Child Guardian'",
                        Toast.LENGTH_LONG
                    ).show()
                }
                PermissionActionType.SYSTEM_OVERLAY -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        val intent = Intent(
                            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:${context.packageName}")
                        ).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        }
                        context.startActivity(intent)
                    }
                }
                PermissionActionType.SYSTEM_USAGE_STATS -> {
                    val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                    context.startActivity(intent)
                    Toast.makeText(
                        context,
                        if (isHindi) "सूची में 'parent cantrol md' को खोजकर 'Allow' करें" else "Select 'parent cantrol md' and allow Usage Access",
                        Toast.LENGTH_LONG
                    ).show()
                }
                PermissionActionType.SYSTEM_BATTERY -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        try {
                            val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).apply {
                                data = Uri.parse("package:${context.packageName}")
                                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            }
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            val intent = Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS).apply {
                                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            }
                            context.startActivity(intent)
                        }
                    }
                }
                PermissionActionType.SYSTEM_NOTIFICATION_LISTENER -> {
                    val intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                    context.startActivity(intent)
                    Toast.makeText(
                        context,
                        if (isHindi) "सूची में 'parent cantrol md - Social Alert Sync' को Allow करें" else "In list, allow 'parent cantrol md - Social Alert Sync'",
                        Toast.LENGTH_LONG
                    ).show()
                }
                PermissionActionType.APP_SETTINGS -> {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.parse("package:${context.packageName}")
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                    context.startActivity(intent)
                }
            }
        } catch (e: Exception) {
            // Fallback to App Details Settings
            try {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.parse("package:${context.packageName}")
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                context.startActivity(intent)
            } catch (ex: Exception) {
                Toast.makeText(context, "Cannot open settings directly: ${ex.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.92f)
                .shadow(16.dp, RoundedCornerShape(24.dp))
                .border(1.dp, NaturalBorder, RoundedCornerShape(24.dp)),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FE))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(18.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF6C5CE7).copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Tune,
                                contentDescription = null,
                                tint = Color(0xFF6C5CE7),
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = if (isHindi) "चाइल्ड डिवाइस ऑल-इन-वन सेटिंग्स" else "Child Device Master Setup",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 15.sp,
                                color = NaturalTextPrimary
                            )
                            Text(
                                text = if (isHindi) "कहीं अलग जाने की जरूरत नहीं, यहीं से सब चालू करें" else "Enable all monitoring permissions with 1-click",
                                fontSize = 11.sp,
                                color = NaturalTextSecondary
                            )
                        }
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = NaturalTextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Progress Card
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = if (allGranted) Color(0xFFE8F8F5) else Color(0xFFF3E5F5)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = if (allGranted) Icons.Default.CheckCircle else Icons.Default.Info,
                                    contentDescription = null,
                                    tint = if (allGranted) Color(0xFF00B894) else Color(0xFF6C5CE7),
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (isHindi) {
                                        if (allGranted) "सभी अनुमतियां सक्रिय हैं! डिवाइस 100% तैयार"
                                        else "सक्रिय स्थिति: $grantedCount / $totalCount अनुमतियां चालू"
                                    } else {
                                        if (allGranted) "All Permissions Granted! 100% Ready"
                                        else "Status: $grantedCount of $totalCount permissions active"
                                    },
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = if (allGranted) Color(0xFF00B894) else Color(0xFF6C5CE7)
                                )
                            }
                            Text(
                                text = "${((grantedCount.toFloat() / totalCount) * 100).toInt()}%",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 13.sp,
                                color = if (allGranted) Color(0xFF00B894) else Color(0xFF6C5CE7)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        LinearProgressIndicator(
                            progress = { grantedCount.toFloat() / totalCount },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = if (allGranted) Color(0xFF00B894) else Color(0xFF6C5CE7),
                            trackColor = Color(0xFFE2E8F0)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Scrollable List of Permissions
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(permissionsList) { item ->
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .border(
                                    1.dp,
                                    if (item.isGranted) Color(0xFF00B894).copy(alpha = 0.4f) else Color(0xFFCBD5E1),
                                    RoundedCornerShape(14.dp)
                                ),
                            shape = RoundedCornerShape(14.dp),
                            color = Color.White
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(item.iconColor.copy(alpha = 0.12f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = item.icon,
                                        contentDescription = null,
                                        tint = item.iconColor,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(10.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = if (isHindi) item.titleHi else item.titleEn,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = NaturalTextPrimary
                                    )
                                    Text(
                                        text = if (isHindi) item.descHi else item.descEn,
                                        fontSize = 11.sp,
                                        color = NaturalTextSecondary,
                                        lineHeight = 14.sp
                                    )
                                }

                                Spacer(modifier = Modifier.width(8.dp))

                                if (item.isGranted) {
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = Color(0xFFE8F8F5)
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                Icons.Default.Check,
                                                contentDescription = null,
                                                tint = Color(0xFF00B894),
                                                modifier = Modifier.size(14.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = if (isHindi) "सक्रिय" else "Active",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF00B894)
                                            )
                                        }
                                    }
                                } else {
                                    Button(
                                        onClick = { handlePermissionAction(item) },
                                        shape = RoundedCornerShape(10.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = item.iconColor),
                                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                        modifier = Modifier.height(34.dp)
                                    ) {
                                        Text(
                                            text = if (isHindi) "चालू करें" else "Enable",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Bottom Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            // Open main app details settings directly
                            try {
                                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                    data = Uri.parse("package:${context.packageName}")
                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                }
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                Toast.makeText(context, "Unable to open phone settings", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Settings, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(if (isHindi) "फोन सेटिंग्स" else "All Settings", fontSize = 12.sp)
                    }

                    Button(
                        onClick = {
                            if (allGranted) {
                                onAllPermissionsGranted?.invoke()
                            }
                            onDismiss()
                        },
                        modifier = Modifier.weight(1.2f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (allGranted) Color(0xFF00B894) else Color(0xFF6C5CE7)
                        )
                    ) {
                        Text(
                            text = if (allGranted) {
                                if (isHindi) "सब तैयार! आगे बढ़ें" else "All Done! Proceed"
                            } else {
                                if (isHindi) "सेव करें व बंद करें" else "Done & Close"
                            },
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}
