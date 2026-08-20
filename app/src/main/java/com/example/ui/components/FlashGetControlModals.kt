package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.ScreenShare
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SmartDisplay
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
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
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.AppUsageRule
import com.example.data.model.ChildProfile
import kotlinx.coroutines.delay

// =========================================================================
// 1. LIVE PAINTING MODAL (Beta PRO - Live screen draw / annotate)
// =========================================================================
data class DrawLine(val points: List<Offset>, val color: Color, val strokeWidth: Float)

@Composable
fun LivePaintingDialog(
    child: ChildProfile,
    isHindi: Boolean,
    onDismiss: () -> Unit
) {
    val lines = remember { mutableStateListOf<DrawLine>() }
    var currentPoints by remember { mutableStateOf<List<Offset>>(emptyList()) }
    var selectedColor by remember { mutableStateOf(Color(0xFFFF3366)) }
    var strokeWidth by remember { mutableFloatStateOf(8f) }
    var isSending by remember { mutableStateOf(false) }
    var sentSuccess by remember { mutableStateOf(false) }

    val colorOptions = listOf(
        Color(0xFFFF3366), // Pink/Red
        Color(0xFF6C5CE7), // Purple
        Color(0xFF00CEC9), // Cyan
        Color(0xFFFDCB6E), // Yellow
        Color(0xFF00B894), // Emerald
        Color(0xFFFFFFFF)  // White
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.92f),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E2E))
        ) {
            Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF6C5CE7).copy(alpha = 0.2f))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text("Beta PRO", color = Color(0xFFFFB300), fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = if (isHindi) "लाइव पेंटिंग (स्क्रीन पर ड्रा करें)" else "Live Painting (Screen Annotate)",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Text(
                                text = if (isHindi) "${child.name} के स्क्रीन पर तुरंत दिखेगा" else "Real-time sync to ${child.deviceModel}",
                                color = Color(0xFFA6ACCD),
                                fontSize = 11.sp
                            )
                        }
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Canvas Area (Phone screen mockup overlay)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFF11111B))
                        .border(1.dp, Color(0xFF313244), RoundedCornerShape(16.dp))
                        .pointerInput(Unit) {
                            detectDragGestures(
                                onDragStart = { offset ->
                                    currentPoints = listOf(offset)
                                },
                                onDrag = { change, _ ->
                                    currentPoints = currentPoints + change.position
                                },
                                onDragEnd = {
                                    if (currentPoints.isNotEmpty()) {
                                        lines.add(DrawLine(currentPoints, selectedColor, strokeWidth))
                                        currentPoints = emptyList()
                                    }
                                }
                            )
                        }
                ) {
                    // Background simulated screen
                    Column(
                        modifier = Modifier.fillMaxSize().padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.SmartDisplay, contentDescription = null, tint = Color(0xFF45475A), modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (isHindi) "यहाँ अपनी उंगली से ड्रा करें" else "Draw or circle anything on screen",
                            color = Color(0xFF6C7086),
                            fontSize = 12.sp
                        )
                    }

                    // Active drawing canvas
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        lines.forEach { line ->
                            if (line.points.size > 1) {
                                val path = Path().apply {
                                    moveTo(line.points.first().x, line.points.first().y)
                                    for (i in 1 until line.points.size) {
                                        lineTo(line.points[i].x, line.points[i].y)
                                    }
                                }
                                drawPath(
                                    path = path,
                                    color = line.color,
                                    style = Stroke(
                                        width = line.strokeWidth,
                                        cap = StrokeCap.Round,
                                        join = StrokeJoin.Round
                                    )
                                )
                            }
                        }
                        if (currentPoints.size > 1) {
                            val path = Path().apply {
                                moveTo(currentPoints.first().x, currentPoints.first().y)
                                for (i in 1 until currentPoints.size) {
                                    lineTo(currentPoints[i].x, currentPoints[i].y)
                                }
                            }
                            drawPath(
                                path = path,
                                color = selectedColor,
                                style = Stroke(
                                    width = strokeWidth,
                                    cap = StrokeCap.Round,
                                    join = StrokeJoin.Round
                                )
                            )
                        }
                    }

                    if (sentSuccess) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                                .padding(top = 16.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color(0xFF00B894))
                                .padding(horizontal = 16.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = if (isHindi) "✓ ${child.name} की स्क्रीन पर भेजा गया!" else "✓ Synced to child's screen!",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Color Selector & Tools
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        colorOptions.forEach { color ->
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(color)
                                    .border(
                                        width = if (selectedColor == color) 3.dp else 1.dp,
                                        color = if (selectedColor == color) Color.White else Color.Transparent,
                                        shape = CircleShape
                                    )
                                    .clickable { selectedColor = color }
                            )
                        }
                    }

                    IconButton(
                        onClick = { lines.clear() },
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF313244))
                    ) {
                        Icon(Icons.Default.Clear, contentDescription = "Clear Canvas", tint = Color(0xFFF38BA8), modifier = Modifier.size(18.dp))
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = { lines.clear() },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text(if (isHindi) "मिटाएं" else "Clear", color = Color(0xFFA6ACCD))
                    }

                    Button(
                        onClick = {
                            isSending = true
                            sentSuccess = true
                        },
                        modifier = Modifier.weight(1.5f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C5CE7)),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(if (isHindi) "स्क्रीन पर भेजें" else "Send to Screen", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

// =========================================================================
// 2. CHECK PERMISSIONS AUDIT MODAL
// =========================================================================
data class PermissionItem(
    val name: String,
    val nameHi: String,
    val description: String,
    val isGranted: Boolean,
    val isCritical: Boolean
)

@Composable
fun CheckPermissionsDialog(
    child: ChildProfile,
    isHindi: Boolean,
    onDismiss: () -> Unit
) {
    var permissions by remember {
        mutableStateOf(
            listOf(
                PermissionItem("Accessibility Service", "एक्सेसिबिलिटी सर्विस", "Required for real-time app blocking and screen time tracking", true, true),
                PermissionItem("Usage Access", "उपयोग एक्सेस (Usage Access)", "Monitors app opening duration and daily statistics", true, true),
                PermissionItem("Display Over Other Apps", "अन्य ऐप्स के ऊपर डिस्प्ले", "Displays lock screen overlays and instant block warnings", true, true),
                PermissionItem("Notification Listener", "नोटिफिकेशन लिसनर", "Monitors WhatsApp & SMS messages for cyber safety", true, false),
                PermissionItem("Camera & Microphone", "कैमरा और माइक्रोफ़ोन", "Enables remote snapshots and ambient sound monitoring", true, false),
                PermissionItem("Device Administrator", "डिवाइस एडमिनिस्ट्रेटर", "Prevents child from uninstalling or bypassing protection", true, true),
                PermissionItem("Battery Optimization Disabled", "बैटरी ऑप्टिमाइज़ेशन बंद", "Keeps background protection alive 24/7", true, true),
                PermissionItem("Precise GPS Location", "सटीक GPS लोकेशन", "Enables live geofencing and real-time tracking", true, false)
            )
        )
    }

    var isRechecking by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.88f),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
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
                                .clip(CircleShape)
                                .background(Color(0xFF6C5CE7).copy(alpha = 0.12f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Security, contentDescription = null, tint = Color(0xFF6C5CE7), modifier = Modifier.size(24.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = if (isHindi) "अनुमतियों की जाँच (Permissions)" else "Check Permissions",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = Color(0xFF1E1E2E)
                            )
                            Text(
                                text = "${child.deviceModel} • 8/8 Active",
                                fontSize = 12.sp,
                                color = Color(0xFF00B894),
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color(0xFF6C7086))
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Status Banner
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFEBFBF5))
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF00B894), modifier = Modifier.size(28.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = if (isHindi) "सभी सुरक्षा अनुमतियाँ सक्रिय हैं" else "All Protection Permissions Active",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = Color(0xFF00755E)
                            )
                            Text(
                                text = if (isHindi) "फोन पूरी तरह से सुरक्षित और मॉनिटर हो रहा है" else "Target device is fully synchronized & safeguarded",
                                fontSize = 11.sp,
                                color = Color(0xFF2D3436)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Permission List
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(permissions) { item ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FE)),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFECEFF8))
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(if (item.isGranted) Color(0xFF00B894).copy(alpha = 0.15f) else Color(0xFFFF4757).copy(alpha = 0.15f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        if (item.isGranted) Icons.Default.Check else Icons.Default.Close,
                                        contentDescription = null,
                                        tint = if (item.isGranted) Color(0xFF00B894) else Color(0xFFFF4757),
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = if (isHindi) item.nameHi else item.name,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            color = Color(0xFF1E1E2E)
                                        )
                                        if (item.isCritical) {
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(6.dp))
                                                    .background(Color(0xFFFF4757).copy(alpha = 0.12f))
                                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                                            ) {
                                                Text(if (isHindi) "महत्वपूर्ण" else "Required", fontSize = 10.sp, color = Color(0xFFFF4757), fontWeight = FontWeight.Bold)
                                            }
                                        }
                                    }
                                    Text(
                                        text = item.description,
                                        fontSize = 11.sp,
                                        color = Color(0xFF6C7086),
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Bottom Action
                Button(
                    onClick = {
                        isRechecking = true
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C5CE7)),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (isHindi) "पुनः स्कैन करें (Re-Check)" else "Re-Check All Permissions", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
            }
        }
    }
}

// =========================================================================
// 3. SOCIAL APP DETECTION MODAL (WhatsApp, IG, Snapchat, TikTok)
// =========================================================================
@Composable
fun SocialAppDetectionDialog(
    child: ChildProfile,
    isHindi: Boolean,
    onDismiss: () -> Unit
) {
    var filterCyberbullying by remember { mutableStateOf(true) }
    var filterAdultText by remember { mutableStateOf(true) }
    var filterPredatorPhrases by remember { mutableStateOf(true) }
    var alertOnUnknownContact by remember { mutableStateOf(true) }

    val monitoredApps = listOf(
        Pair("WhatsApp", "14 Alerts Screened"),
        Pair("Instagram", "3 Messages Checked"),
        Pair("Snapchat", "Active Monitoring"),
        Pair("TikTok", "Filter Active"),
        Pair("Telegram", "Safe Mode On")
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.88f),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
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
                                .clip(CircleShape)
                                .background(Color(0xFF25D366).copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Shield, contentDescription = null, tint = Color(0xFF25D366), modifier = Modifier.size(24.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = if (isHindi) "सोशल ऐप सुरक्षा" else "Social App Detection",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    color = Color(0xFF1E1E2E)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(Color(0xFFFFB300))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text("PRO", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 10.sp)
                                }
                            }
                            Text(
                                text = if (isHindi) "व्हाट्सएप, इंस्टाग्राम, चैट सुरक्षा" else "AI Chat & Harassment Scanner",
                                fontSize = 12.sp,
                                color = Color(0xFF6C7086)
                            )
                        }
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color(0xFF6C7086))
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Monitored Apps Chips
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(monitoredApps) { (appName, status) ->
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFF3F0FF))
                        ) {
                            Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                                Text(appName, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color(0xFF6C5CE7))
                                Text(status, fontSize = 10.sp, color = Color(0xFF2D3436))
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Safety Rules
                Text(
                    text = if (isHindi) "एआई सुरक्षा सेटिंग्स" else "AI Detection Rules",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color(0xFF1E1E2E)
                )

                Spacer(modifier = Modifier.height(10.dp))

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    item {
                        SafetyToggleCard(
                            title = if (isHindi) "साइबरबुलिंग और गाली-गलौज रोकथाम" else "Cyberbullying & Abuse Detection",
                            subtitle = if (isHindi) "आपत्तिजनक शब्दों पर तुरंत अलर्ट भेजें" else "Instant parent push alerts on toxic keywords",
                            checked = filterCyberbullying,
                            onCheckedChange = { filterCyberbullying = it }
                        )
                    }
                    item {
                        SafetyToggleCard(
                            title = if (isHindi) "अज्ञात संपर्कों से संदेश अलर्ट" else "Unknown Contacts Alert",
                            subtitle = if (isHindi) "नए या गैर-सहेजे गए नंबर से चैट आने पर सूचित करें" else "Notify parent when messaging non-contact numbers",
                            checked = alertOnUnknownContact,
                            onCheckedChange = { alertOnUnknownContact = it }
                        )
                    }
                    item {
                        SafetyToggleCard(
                            title = if (isHindi) "संवेदनशील सामग्री और प्रेडेटर शील्ड" else "Predatory Phrasing Shield",
                            subtitle = if (isHindi) "व्यक्तिगत जानकारी (पता, पासवर्ड) मांगने पर रोक" else "Flags attempts requesting child's private address/photos",
                            checked = filterPredatorPhrases,
                            onCheckedChange = { filterPredatorPhrases = it }
                        )
                    }
                    item {
                        SafetyToggleCard(
                            title = if (isHindi) "वयस्क सामग्री संदेश ब्लॉक" else "Adult & Explicit Text Blocker",
                            subtitle = if (isHindi) "18+ सामग्री को स्वचालित रूप से छुपाएं" else "Automatically blurs and reports adult message content",
                            checked = filterAdultText,
                            onCheckedChange = { filterAdultText = it }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C5CE7)),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(if (isHindi) "सेटिंग्स सहेजें (Save Rules)" else "Save Social Safety Rules", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
            }
        }
    }
}

// =========================================================================
// 4. ALBUMS SAFETY MODAL (AI Gallery Scanner)
// =========================================================================
@Composable
fun AlbumsSafetyDialog(
    child: ChildProfile,
    isHindi: Boolean,
    onDismiss: () -> Unit
) {
    var autoScanDaily by remember { mutableStateOf(true) }
    var blurSensitiveImages by remember { mutableStateOf(true) }
    var alertOnScreenshot by remember { mutableStateOf(true) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.85f),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
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
                                .clip(CircleShape)
                                .background(Color(0xFFE84393).copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.PhotoLibrary, contentDescription = null, tint = Color(0xFFE84393), modifier = Modifier.size(24.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = if (isHindi) "एल्बम और गैलरी सुरक्षा" else "Albums Safety",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    color = Color(0xFF1E1E2E)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(Color(0xFFFFB300))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text("PRO", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 10.sp)
                                }
                            }
                            Text(
                                text = if (isHindi) "फ़ोटो व वीडियो एआई सुरक्षा स्कैन" else "AI Photo Vault & Sensitive Content Filter",
                                fontSize = 12.sp,
                                color = Color(0xFF6C7086)
                            )
                        }
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color(0xFF6C7086))
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Stats Banner
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF0F6))
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (isHindi) "स्कैन की गई फ़ोटो: 428" else "Scanned Photos: 428",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = Color(0xFFD63031)
                            )
                            Text(
                                text = if (isHindi) "0 असुरक्षित फ़ोटो मिली • गैलरी सुरक्षित है" else "0 Sensitive Images Found • Gallery Clean",
                                fontSize = 12.sp,
                                color = Color(0xFF2D3436)
                            )
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFF00B894))
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text("SAFE", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Settings
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    item {
                        SafetyToggleCard(
                            title = if (isHindi) "दैनिक गैलरी ऑटो-स्कैन" else "Daily Auto-Scan for Inappropriate Media",
                            subtitle = if (isHindi) "नई डाउनलोड या क्लिक की गई तस्वीरों का स्वतः विश्लेषण" else "AI automatically scans new camera & downloaded photos",
                            checked = autoScanDaily,
                            onCheckedChange = { autoScanDaily = it }
                        )
                    }
                    item {
                        SafetyToggleCard(
                            title = if (isHindi) "संवेदनशील तस्वीरों को ब्लर करें" else "Auto-Blur Sensitive Media",
                            subtitle = if (isHindi) "बच्चे के फोन में अनुचित फ़ोटो दिखने से पहले ब्लर करें" else "Prevents viewing until parent authorization",
                            checked = blurSensitiveImages,
                            onCheckedChange = { blurSensitiveImages = it }
                        )
                    }
                    item {
                        SafetyToggleCard(
                            title = if (isHindi) "स्क्रीनशॉट लेने पर अलर्ट" else "Alert on Screen Captures",
                            subtitle = if (isHindi) "बच्चा जब स्क्रीनशॉट ले तो पैरेंट को अलर्ट मिले" else "Sends notification when child takes phone screenshots",
                            checked = alertOnScreenshot,
                            onCheckedChange = { alertOnScreenshot = it }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE84393)),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (isHindi) "गैलरी अभी स्कैन करें" else "Scan Gallery Now", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
            }
        }
    }
}

// =========================================================================
// 5. BROWSER SAFETY MODAL (SafeSearch & Web Protection)
// =========================================================================
@Composable
fun BrowserSafetyDialog(
    child: ChildProfile,
    isHindi: Boolean,
    onDismiss: () -> Unit
) {
    var safeSearchEnforced by remember { mutableStateOf(child.webSafeSearch) }
    var blockAdultWebsites by remember { mutableStateOf(child.webBlockAdult) }
    var blockGamblingSites by remember { mutableStateOf(true) }
    var blockMaliciousSites by remember { mutableStateOf(true) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.88f),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
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
                                .clip(CircleShape)
                                .background(Color(0xFF0984E3).copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Public, contentDescription = null, tint = Color(0xFF0984E3), modifier = Modifier.size(24.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = if (isHindi) "ब्राउज़र सुरक्षा" else "Browser Safety",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    color = Color(0xFF1E1E2E)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(Color(0xFFFFB300))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text("PRO", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 10.sp)
                                }
                            }
                            Text(
                                text = if (isHindi) "सुरक्षित सर्च और वेबसाइट ब्लॉकर" else "Google SafeSearch & Web Shield",
                                fontSize = 12.sp,
                                color = Color(0xFF6C7086)
                            )
                        }
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color(0xFF6C7086))
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Safety Rules
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    item {
                        SafetyToggleCard(
                            title = if (isHindi) "गूगल और बिंग SafeSearch अनिवार्य" else "Enforce Google / Bing SafeSearch",
                            subtitle = if (isHindi) "सर्च इंजनों में अनुचित परिणाम हमेशा ब्लॉक रहेंगे" else "Locks adult search filters permanently on child browser",
                            checked = safeSearchEnforced,
                            onCheckedChange = { safeSearchEnforced = it }
                        )
                    }
                    item {
                        SafetyToggleCard(
                            title = if (isHindi) "वयस्क व 18+ वेबसाइटें ब्लॉक करें" else "Block 18+ Adult Domains",
                            subtitle = if (isHindi) "10 लाख से अधिक अनुपयुक्त वेबसाइटों की स्वचालित रोकथाम" else "Database of over 1.2M explicit URLs blocked instantly",
                            checked = blockAdultWebsites,
                            onCheckedChange = { blockAdultWebsites = it }
                        )
                    }
                    item {
                        SafetyToggleCard(
                            title = if (isHindi) "सट्टेबाजी व जुए की साइटें बंद" else "Block Gambling & Betting Portals",
                            subtitle = if (isHindi) "ऑनलाइन कैसीनो और सट्टेबाजी लिंक रोकें" else "Blocks real-money betting and lottery websites",
                            checked = blockGamblingSites,
                            onCheckedChange = { blockGamblingSites = it }
                        )
                    }
                    item {
                        SafetyToggleCard(
                            title = if (isHindi) "मैलवेयर और फ़िशिंग शील्ड" else "Anti-Phishing & Malware Protection",
                            subtitle = if (isHindi) "खतरनाक डाउनलोड और फेक वेबसाइट लिंक ब्लॉक करें" else "Prevents APK malware downloads from untrusted links",
                            checked = blockMaliciousSites,
                            onCheckedChange = { blockMaliciousSites = it }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0984E3)),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(if (isHindi) "ब्राउज़र शील्ड सक्रिय करें" else "Apply Browser Protection", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
            }
        }
    }
}

// =========================================================================
// 6. HOW TO OPEN HIDDEN CHILD APP GUIDE MODAL
// =========================================================================
@Composable
fun HiddenChildAppGuideDialog(
    isHindi: Boolean,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.85f),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
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
                                .clip(CircleShape)
                                .background(Color(0xFFFDCB6E).copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("💡", fontSize = 22.sp)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = if (isHindi) "छिपे हुए ऐप को कैसे खोलें?" else "How to open hidden child's app?",
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.sp,
                                color = Color(0xFF1E1E2E)
                            )
                            Text(
                                text = if (isHindi) "चाइल्ड मोड अनहाइड गाइड" else "Secret Dial Codes & Launcher Steps",
                                fontSize = 12.sp,
                                color = Color(0xFF6C7086)
                            )
                        }
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color(0xFF6C7086))
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    item {
                        GuideStepCard(
                            stepNumber = "1",
                            title = if (isHindi) "फोन डायलर कोड (Dialer Method)" else "Method 1: Phone Dialer Code",
                            description = if (isHindi) "बच्चे के फोन का डायलर खोलें और यह कोड डायल करें:" else "Open Phone dialer on child's device and enter code:",
                            codeText = "#*#*9999#*#*"
                        )
                    }
                    item {
                        GuideStepCard(
                            stepNumber = "2",
                            title = if (isHindi) "कैलकुलेटर डिस्गाइज़ (Secret Calculator)" else "Method 2: Secret Calculator Passcode",
                            description = if (isHindi) "यदि ऐप कैलकुलेटर के रूप में छुपा है, तो कैलकुलेटर खोलें और दर्ज करें:" else "If disguised as Calculator, open it and calculate:",
                            codeText = "8888 ="
                        )
                    }
                    item {
                        GuideStepCard(
                            stepNumber = "3",
                            title = if (isHindi) "पैरेंट सुरक्षा पिन" else "Parent Master PIN",
                            description = if (isHindi) "सेटिंग्स बदलने या ऐप बंद करने के लिए डिफ़ॉल्ट पिन:" else "Master security PIN to exit Child Lock mode:",
                            codeText = "1234"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C5CE7)),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(if (isHindi) "समझ गया (Got it)" else "Understood", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
            }
        }
    }
}

// =========================================================================
// HELPER COMPONENTS
// =========================================================================
@Composable
fun SafetyToggleCard(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FE)),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFECEFF8))
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF1E1E2E))
                Spacer(modifier = Modifier.height(2.dp))
                Text(subtitle, fontSize = 11.sp, color = Color(0xFF6C7086), lineHeight = 15.sp)
            }
            Spacer(modifier = Modifier.width(10.dp))
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = Color(0xFF6C5CE7)
                )
            )
        }
    }
}

@Composable
fun GuideStepCard(
    stepNumber: String,
    title: String,
    description: String,
    codeText: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FE)),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE4E7F5))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF6C5CE7)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(stepNumber, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF1E1E2E))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(description, fontSize = 12.sp, color = Color(0xFF6C7086))
            Spacer(modifier = Modifier.height(10.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF2D3436))
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = codeText,
                    color = Color(0xFF00CEC9),
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    letterSpacing = 2.sp
                )
            }
        }
    }
}
