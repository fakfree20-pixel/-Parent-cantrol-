package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddLocation
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.Brightness6
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.ScreenShare
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.VolumeMute
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.AppUsageRule
import com.example.data.model.ChildProfile
import com.example.data.model.GeofenceZone
import com.example.ui.theme.EarthAmber100
import com.example.ui.theme.EarthAmber600
import com.example.ui.theme.MossGreen100
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
import kotlinx.coroutines.delay
import kotlin.random.Random

// 1. REMOTE CAMERA LIVE STREAM DIALOG (बच्चे का रिमोट कैमरा लाइव)
@Composable
fun RemoteCameraDialog(
    child: ChildProfile,
    isHindi: Boolean,
    onDismiss: () -> Unit
) {
    var isBackCamera by remember { mutableStateOf(true) } // Default to rear camera showing child's room/surroundings
    var isFlashOn by remember { mutableStateOf(false) }
    var isAudioListening by remember { mutableStateOf(true) }
    var isNightVision by remember { mutableStateOf(false) }
    var isRecordingVideo by remember { mutableStateOf(false) }
    var recordTimerSeconds by remember { mutableIntStateOf(0) }
    var snapshotTaken by remember { mutableStateOf(false) }
    var streamQuality by remember { mutableStateOf("1080P FHD • 60 FPS") }
    var showQualityMenu by remember { mutableStateOf(false) }
    var ambientDb by remember { mutableIntStateOf(34) }
    var isConnecting by remember { mutableStateOf(true) }
    var syncToast by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        isConnecting = true
        delay(4000)
        isConnecting = false
    }

    LaunchedEffect(syncToast) {
        if (syncToast != null) {
            delay(2000)
            syncToast = null
        }
    }

    // Live ambient dB and recording timer simulation
    LaunchedEffect(isRecordingVideo) {
        if (isRecordingVideo) {
            recordTimerSeconds = 0
            while (isRecordingVideo) {
                delay(1000)
                recordTimerSeconds++
            }
        }
    }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1200)
            ambientDb = Random.nextInt(28, 48)
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF0A0E17))
        ) {
            // Simulated Live Camera View from Child's Phone
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        if (isNightVision) {
                            Brush.radialGradient(
                                colors = listOf(
                                    Color(0xFF0F381E),
                                    Color(0xFF071F10),
                                    Color(0xFF020B05)
                                )
                            )
                        } else {
                            Brush.radialGradient(
                                colors = listOf(
                                    if (isBackCamera) Color(0xFF1B2838) else Color(0xFF2A2035),
                                    Color(0xFF111722),
                                    Color(0xFF090D14)
                                )
                            )
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                // Animated live stream crosshairs & room simulation
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val cx = size.width / 2
                    val cy = size.height / 2

                    // Grid lines
                    val gridColor = if (isNightVision) Color(0xFF00FF66).copy(alpha = 0.15f) else Color.White.copy(alpha = 0.08f)
                    drawLine(gridColor, Offset(0f, cy), Offset(size.width, cy), strokeWidth = 1f)
                    drawLine(gridColor, Offset(cx, 0f), Offset(cx, size.height), strokeWidth = 1f)

                    // Viewfinder Corners
                    val boxW = size.width * 0.78f
                    val boxH = size.height * 0.52f
                    val left = cx - boxW / 2
                    val right = cx + boxW / 2
                    val top = cy - boxH / 2
                    val bottom = cy + boxH / 2
                    val cornerLen = 28.dp.toPx()
                    val strokeColor = if (isNightVision) Color(0xFF00FF66) else if (isFlashOn) Color(0xFFFFD54F) else Color(0xFF6C5CE7)

                    // Top Left Corner
                    drawLine(strokeColor, Offset(left, top), Offset(left + cornerLen, top), strokeWidth = 3f)
                    drawLine(strokeColor, Offset(left, top), Offset(left, top + cornerLen), strokeWidth = 3f)

                    // Top Right Corner
                    drawLine(strokeColor, Offset(right, top), Offset(right - cornerLen, top), strokeWidth = 3f)
                    drawLine(strokeColor, Offset(right, top), Offset(right, top + cornerLen), strokeWidth = 3f)

                    // Bottom Left Corner
                    drawLine(strokeColor, Offset(left, bottom), Offset(left + cornerLen, bottom), strokeWidth = 3f)
                    drawLine(strokeColor, Offset(left, bottom), Offset(left, bottom - cornerLen), strokeWidth = 3f)

                    // Bottom Right Corner
                    drawLine(strokeColor, Offset(right, bottom), Offset(right - cornerLen, bottom), strokeWidth = 3f)
                    drawLine(strokeColor, Offset(right, bottom), Offset(right, bottom - cornerLen), strokeWidth = 3f)

                    // Center Focus Target
                    val focusRadius = 32.dp.toPx()
                    drawCircle(
                        color = strokeColor.copy(alpha = 0.4f),
                        radius = focusRadius,
                        center = Offset(cx, cy),
                        style = Stroke(width = 1.5f)
                    )
                }

                // Center Live Content Rendering (Child Room or Child Study Desk)
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(24.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(
                                if (isNightVision) Color(0xFF00FF66).copy(alpha = 0.15f)
                                else Color.White.copy(alpha = 0.08f)
                            )
                            .border(
                                1.5.dp,
                                if (isNightVision) Color(0xFF00FF66) else Color(0xFF6C5CE7).copy(alpha = 0.5f),
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isBackCamera) Icons.Default.PhotoCamera else Icons.Default.Cameraswitch,
                            contentDescription = null,
                            tint = if (isNightVision) Color(0xFF00FF66) else Color.White,
                            modifier = Modifier.size(44.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = if (isBackCamera) {
                            if (isHindi) "${child.name} का बैक कैमरा (कमरा व परिवेश)" else "${child.name}'s Back Camera (Room View)"
                        } else {
                            if (isHindi) "${child.name} का फ्रंट कैमरा (सेल्फी / अध्ययन दृश्य)" else "${child.name}'s Front Camera (Study / Face View)"
                        },
                        color = if (isNightVision) Color(0xFF00FF66) else Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.Black.copy(alpha = 0.6f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.15f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Smartphone, contentDescription = null, tint = Color(0xFF00CEC9), modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isHindi) "बच्चे का फोन: ${child.deviceModel} (लाइव स्ट्रीम)" else "Child Device: ${child.deviceModel} (Live Stream)",
                                color = Color(0xFF00CEC9),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Super-fast connection status & sync button
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (isConnecting) Color(0xFFFEF3C7).copy(alpha = 0.9f) else Color(0xFFDCFCE7).copy(alpha = 0.9f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = if (isConnecting) "⚡ 4s कनेक्टिंग..." else "⚡ कनेक्टेड (0.2s)",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isConnecting) Color(0xFFD97706) else Color(0xFF16A34A)
                            )
                            Button(
                                onClick = {
                                    isConnecting = true
                                    syncToast = if (isHindi) "🔄 कैमरा स्ट्रीम सिंक हो रही है..." else "🔄 Syncing camera stream..."
                                    android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                                        isConnecting = false
                                    }, 1500)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C5CE7)),
                                shape = RoundedCornerShape(6.dp),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                modifier = Modifier.height(26.dp)
                            ) {
                                Text(if (isHindi) "सिंक" else "⚡ Sync", fontSize = 10.sp, color = Color.White)
                            }
                        }
                    }
                    syncToast?.let { msg ->
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = msg, fontSize = 10.sp, color = Color(0xFF00CEC9), fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color.Red.copy(alpha = 0.15f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color.Red.copy(alpha = 0.5f))
                    ) {
                        Text(
                            text = if (isHindi) "⚠️ डेमो / सिमुलेशन मोड" else "⚠️ DEMO / SIMULATION MODE",
                            color = Color(0xFFFF5252),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Audio Level & Night mode indicator
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (isAudioListening) Color(0xFF0984E3).copy(alpha = 0.2f) else Color.White.copy(alpha = 0.05f)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Mic,
                                    contentDescription = null,
                                    tint = if (isAudioListening) Color(0xFF74B9FF) else Color.Gray,
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (isAudioListening) "Audio: $ambientDb dB" else "Audio Muted",
                                    color = if (isAudioListening) Color(0xFF74B9FF) else Color.Gray,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        if (isNightVision) {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = Color(0xFF00FF66).copy(alpha = 0.2f)
                            ) {
                                Text(
                                    text = "🌙 NIGHT VISION ON",
                                    color = Color(0xFF00FF66),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        if (isFlashOn) {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = Color(0xFFFFD54F).copy(alpha = 0.25f)
                            ) {
                                Text(
                                    text = "⚡ TORCH ON",
                                    color = Color(0xFFFFD54F),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }

                    if (snapshotTaken) {
                        Spacer(modifier = Modifier.height(14.dp))
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFF00B894)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (isHindi) "फोटो सफलतापूर्वक सहेजी गई!" else "Photo Captured & Saved to Gallery!",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            // Top Status Bar Overlay
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.Black.copy(alpha = 0.9f), Color.Black.copy(alpha = 0.4f), Color.Transparent)
                        )
                    )
                    .padding(top = 36.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // LIVE Badge
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color(0xFFFF4757))
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(7.dp)
                                    .clip(CircleShape)
                                    .background(Color.White)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text("CHILD CAM LIVE", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold)
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        // Quality Badge
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = Color.White.copy(alpha = 0.15f),
                            modifier = Modifier.clickable { showQualityMenu = !showQualityMenu }
                        ) {
                            Text(
                                text = streamQuality,
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.15f))
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White, modifier = Modifier.size(20.dp))
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Telemetry sub-bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isHindi) "रिमोट स्ट्रीम: ${child.name} का फोन" else "Streaming from ${child.name}'s Device",
                        color = Color.LightGray,
                        fontSize = 11.sp
                    )
                    Text(
                        text = "📶 5G • 🔋 69%",
                        color = Color(0xFF00CEC9),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (isRecordingVideo) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.Red.copy(alpha = 0.8f))
                            .padding(horizontal = 10.dp, vertical = 3.dp)
                    ) {
                        Box(modifier = Modifier.size(6.dp).background(Color.White, CircleShape))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "REC: %02d:%02d".format(recordTimerSeconds / 60, recordTimerSeconds % 60),
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Bottom Camera Controls Panel
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.Transparent, Color.Black.copy(alpha = 0.85f), Color.Black)
                        )
                    )
                    .padding(horizontal = 18.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Quick toggles row (Audio, Torch, Night Vision)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Audio Listen Toggle
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        IconButton(
                            onClick = { isAudioListening = !isAudioListening },
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(if (isAudioListening) Color(0xFF0984E3) else Color.White.copy(alpha = 0.15f))
                        ) {
                            Icon(
                                imageVector = if (isAudioListening) Icons.Default.VolumeUp else Icons.Default.VolumeMute,
                                contentDescription = "Audio Listen",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = if (isHindi) "ऑडियो" else "Audio",
                            color = if (isAudioListening) Color(0xFF74B9FF) else Color.Gray,
                            fontSize = 10.sp
                        )
                    }

                    // Torch / Flashlight Toggle
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        IconButton(
                            onClick = { isFlashOn = !isFlashOn },
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(if (isFlashOn) Color(0xFFFFD54F) else Color.White.copy(alpha = 0.15f))
                        ) {
                            Icon(
                                imageVector = Icons.Default.FlashOn,
                                contentDescription = "Flashlight",
                                tint = if (isFlashOn) Color.Black else Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = if (isHindi) "टॉर्च" else "Torch",
                            color = if (isFlashOn) Color(0xFFFFD54F) else Color.Gray,
                            fontSize = 10.sp
                        )
                    }

                    // Night Vision IR Toggle
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        IconButton(
                            onClick = { isNightVision = !isNightVision },
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(if (isNightVision) Color(0xFF00FF66) else Color.White.copy(alpha = 0.15f))
                        ) {
                            Icon(
                                imageVector = Icons.Default.Brightness6,
                                contentDescription = "Night Vision",
                                tint = if (isNightVision) Color.Black else Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = if (isHindi) "नाइट विज़न" else "Night Vision",
                            color = if (isNightVision) Color(0xFF00FF66) else Color.Gray,
                            fontSize = 10.sp
                        )
                    }

                    // Video Recording Toggle
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        IconButton(
                            onClick = { isRecordingVideo = !isRecordingVideo },
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(if (isRecordingVideo) Color(0xFFFF4757) else Color.White.copy(alpha = 0.15f))
                        ) {
                            Icon(
                                imageVector = if (isRecordingVideo) Icons.Default.Stop else Icons.Default.FiberManualRecord,
                                contentDescription = "Record Video",
                                tint = if (isRecordingVideo) Color.White else Color(0xFFFF4757),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = if (isRecordingVideo) (if (isHindi) "रोकें" else "Stop") else (if (isHindi) "रिकॉर्ड" else "Record"),
                            color = if (isRecordingVideo) Color(0xFFFF4757) else Color.Gray,
                            fontSize = 10.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Shutter & Flip Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Switch Camera Mode (Front <-> Rear)
                    OutlinedButton(
                        onClick = { isBackCamera = !isBackCamera },
                        shape = RoundedCornerShape(16.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.3f)),
                        colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White.copy(alpha = 0.12f)),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp)
                    ) {
                        Icon(Icons.Default.Cameraswitch, contentDescription = "Flip Camera", tint = Color.White, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isBackCamera) (if (isHindi) "फ्रंट कैमरा" else "Front Cam") else (if (isHindi) "बैक कैमरा" else "Rear Cam"),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }

                    // Main Snapshot Shutter Button
                    Box(
                        modifier = Modifier
                            .size(70.dp)
                            .clip(CircleShape)
                            .border(4.dp, Color.White, CircleShape)
                            .background(Color.White.copy(alpha = 0.2f))
                            .clickable { snapshotTaken = true },
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                        )
                    }

                    // Resolution Quality Dropdown Button
                    OutlinedButton(
                        onClick = {
                            streamQuality = when (streamQuality) {
                                "1080P FHD • 60 FPS" -> "720P HD • 30 FPS"
                                "720P HD • 30 FPS" -> "480P Saver • 24 FPS"
                                else -> "1080P FHD • 60 FPS"
                            }
                        },
                        shape = RoundedCornerShape(16.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.3f)),
                        colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White.copy(alpha = 0.12f)),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp)
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = "Quality", tint = Color.White, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = streamQuality.substringBefore(" •"),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = if (isHindi)
                        "⚡ बच्चे के फोन से लाइव प्रसारण • फ्रंट/बैक कैमरा बदलें या स्नैपशॉट लें"
                        else "⚡ Live stream from child's camera • Flip Front/Back or capture snapshot",
                    color = Color.Gray,
                    fontSize = 11.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

// 2. SCREEN MIRRORING & FULL REMOTE CONTROL / APP OPERATOR DIALOG
@Composable
fun ScreenMirroringDialog(
    child: ChildProfile,
    apps: List<AppUsageRule> = emptyList(),
    isHindi: Boolean,
    onInstantLock: () -> Unit,
    onDismiss: () -> Unit
) {
    var isPaused by remember { mutableStateOf(false) }
    var isRemoteControlActive by remember { mutableStateOf(true) }
    var fps by remember { mutableIntStateOf(30) }
    var activeApp by remember { mutableStateOf("HOME") } // HOME, SETTINGS, WHATSAPP, YOUTUBE, BROWSER, PLAYSTORE, CAMERA, GALLERY, PHONE
    var touchPointer by remember { mutableStateOf<Offset?>(null) }
    var toastMessage by remember { mutableStateOf<String?>(null) }
    var isRecordingScreen by remember { mutableStateOf(false) }
    var snapshotTaken by remember { mutableStateOf(false) }
    var showAppDrawerDialog by remember { mutableStateOf(false) }
    var showRemoteTextInputDialog by remember { mutableStateOf(false) }
    var customRemoteText by remember { mutableStateOf("") }
    var isScreenPoweredOff by remember { mutableStateOf(false) }
    var isConnecting by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        isConnecting = true
        delay(4000)
        isConnecting = false
    }

    // Remote Phone Settings States (Controlled directly by Parent)
    var remoteWifiOn by remember { mutableStateOf(true) }
    var remoteBluetoothOn by remember { mutableStateOf(false) }
    var remoteGpsOn by remember { mutableStateOf(true) }
    var remoteBrightness by remember { mutableFloatStateOf(0.75f) }
    var remoteVolume by remember { mutableFloatStateOf(0.60f) }
    var remoteRingtoneVol by remember { mutableFloatStateOf(0.80f) }
    var remoteDarkMode by remember { mutableStateOf(true) }
    var remoteEyeComfort by remember { mutableStateOf(true) }
    var remoteScreenTimeout by remember { mutableStateOf("1 Min") }
    var remoteCameraPermission by remember { mutableStateOf(true) }
    var remoteMicPermission by remember { mutableStateOf(true) }
    var remoteSettingsLocked by remember { mutableStateOf(true) }
    var remoteBatterySaver by remember { mutableStateOf(false) }

    // App specific interactive states
    // WhatsApp
    var whatsAppChatUser by remember { mutableStateOf("Mom (Family)") }
    var whatsAppTypedText by remember { mutableStateOf("") }
    var whatsAppMessages by remember {
        mutableStateOf(
            listOf(
                "Mom: Aarav, complete your science homework before 6 PM!" to false,
                "Aarav: Yes Mom, working on it now 👍" to true,
                "Mom: Good boy! Don't play games for long." to false
            )
        )
    }

    // YouTube
    var isYouTubePlaying by remember { mutableStateOf(true) }
    var youTubeVideoTitle by remember { mutableStateOf("Solar System Explained for Kids - Science 4K") }
    var youTubeSafeSearchOn by remember { mutableStateOf(true) }

    // Browser
    var browserUrl by remember { mutableStateOf("https://kids.nationalgeographic.com") }
    var browserSearchQuery by remember { mutableStateOf("Science Experiments for Class 6") }

    // Play Store
    var playStoreBlockDownloads by remember { mutableStateOf(true) }

    // Auto-clear toasts & FPS simulation
    LaunchedEffect(toastMessage) {
        if (toastMessage != null) {
            delay(2800)
            toastMessage = null
        }
    }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1500)
            fps = Random.nextInt(28, 32)
        }
    }

    // Modal: Quick App Drawer (Directly launch any app)
    if (showAppDrawerDialog) {
        AlertDialog(
            onDismissRequest = { showAppDrawerDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Apps, contentDescription = null, tint = Color(0xFF6C5CE7))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isHindi) "बच्चों के फ़ोन पर कोई भी ऐप खोलें" else "Launch App on Child Phone",
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp
                    )
                }
            },
            text = {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    Text(
                        text = if (isHindi) "जिस ऐप को चुनेंगे वह बच्चे के मोबाइल में तुरंत खुल जाएगा और आप उसे पूरा चला सकेंगे।"
                        else "Select any app to immediately launch and operate it on ${child.name}'s device.",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    val appList = listOf(
                        Triple("Settings (फ़ोन सेटिंग्स)", Icons.Default.Settings, "SETTINGS"),
                        Triple("WhatsApp (व्हाट्सएप)", Icons.Default.Chat, "WHATSAPP"),
                        Triple("YouTube Kids (यूट्यूब)", Icons.Default.PlayArrow, "YOUTUBE"),
                        Triple("Chrome Browser (ब्राउज़र)", Icons.Default.Language, "BROWSER"),
                        Triple("Google Play Store (प्ले स्टोर)", Icons.Default.ShoppingCart, "PLAYSTORE"),
                        Triple("Camera (कैमरा)", Icons.Default.PhotoCamera, "CAMERA"),
                        Triple("Photos / Gallery (गैलरी)", Icons.Default.Photo, "GALLERY"),
                        Triple("Phone & Calls (फोन)", Icons.Default.Phone, "PHONE")
                    )

                    appList.forEach { (label, icon, key) ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable {
                                    activeApp = key
                                    isScreenPoweredOff = false
                                    showAppDrawerDialog = false
                                    toastMessage = if (isHindi) "🚀 '${label}' बच्चे के फ़ोन पर खोला गया" else "🚀 Launched $label remotely"
                                },
                            colors = CardDefaults.cardColors(
                                containerColor = if (activeApp == key) Color(0xFF6C5CE7).copy(alpha = 0.12f) else Color(0xFFF4F6FB)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(icon, contentDescription = null, tint = Color(0xFF6C5CE7), modifier = Modifier.size(24.dp))
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(label, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color(0xFF2D3436))
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showAppDrawerDialog = false }) {
                    Text(if (isHindi) "बंद करें" else "Close", color = Color(0xFF6C5CE7))
                }
            }
        )
    }

    // Modal: Send Text / Keystrokes Remotely
    if (showRemoteTextInputDialog) {
        AlertDialog(
            onDismissRequest = { showRemoteTextInputDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Keyboard, contentDescription = null, tint = Color(0xFF6C5CE7))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (isHindi) "रिमोट टेक्स्ट टाइप करें" else "Send Remote Keystrokes", fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column {
                    Text(
                        text = if (isHindi) "यह टेक्स्ट बच्चे के फोन में इनपुट फ़ील्ड में टाइप हो जाएगा।" else "This text will be directly typed into the active input on child's device.",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = customRemoteText,
                        onValueChange = { customRemoteText = it },
                        placeholder = { Text(if (isHindi) "संदेश या टेक्स्ट लिखें..." else "Type remote text here...") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (customRemoteText.isNotBlank()) {
                            if (activeApp == "WHATSAPP") {
                                whatsAppMessages = whatsAppMessages + ("Parent (Remote): $customRemoteText" to true)
                            }
                            toastMessage = if (isHindi) "⌨️ भेजा गया: '$customRemoteText'" else "⌨️ Injected text: '$customRemoteText'"
                            customRemoteText = ""
                        }
                        showRemoteTextInputDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C5CE7))
                ) {
                    Text(if (isHindi) "भेजें" else "Send")
                }
            },
            dismissButton = {
                TextButton(onClick = { showRemoteTextInputDialog = false }) {
                    Text(if (isHindi) "रद्द करें" else "Cancel")
                }
            }
        )
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF0C0F12))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 34.dp, bottom = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // TOP HEADER BAR: Live Connection Status & Mode Switch
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(if (isPaused) Color(0xFFFFD54F) else Color(0xFF00E676))
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = if (isHindi) "${child.name} का मोबाइल (लाइव कंट्रोल)" else "${child.name}'s Device (Remote Control)",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = if (isRemoteControlActive) Color(0xFF6C5CE7) else Color(0xFF4A4E69)
                                ) {
                                    Text(
                                        text = if (isRemoteControlActive) (if (isHindi) "कंट्रोल ऑन" else "TOUCH ACTIVE") else (if (isHindi) "केवल व्यू" else "VIEW ONLY"),
                                        color = Color.White,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Text(
                                text = "${child.deviceModel} • $fps FPS • 38ms • 🔋 ${child.batteryPercent}%",
                                color = Color(0xFFA4B0BE),
                                fontSize = 11.sp
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Snapshot
                        IconButton(
                            onClick = {
                                snapshotTaken = true
                                toastMessage = if (isHindi) "📸 स्क्रीनशॉट गैलरी में सेव हो गया!" else "📸 Screen Snapshot Saved!"
                            },
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.12f))
                        ) {
                            Icon(Icons.Default.PhotoCamera, contentDescription = "Snap", tint = Color.White, modifier = Modifier.size(18.dp))
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        // Close
                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.12f))
                        ) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White, modifier = Modifier.size(18.dp))
                        }
                    }
                }

                // Super-fast connection status & sync button bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (isConnecting) Color(0xFFFEF3C7) else Color(0xFFDCFCE7)
                    ) {
                        Text(
                            text = if (isConnecting) "⚡ 4s स्क्रीन मिरर कनेक्टिंग..." else "⚡ सुपरफास्ट कनेक्टेड (0.2s)",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isConnecting) Color(0xFFD97706) else Color(0xFF16A34A),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                    Button(
                        onClick = {
                            isConnecting = true
                            toastMessage = if (isHindi) "🔄 स्क्रीन मिरर सिंक हो रहा है..." else "🔄 Syncing screen stream instantly..."
                            android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                                isConnecting = false
                            }, 1500)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C5CE7)),
                        shape = RoundedCornerShape(6.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                        modifier = Modifier.height(26.dp)
                    ) {
                        Text(if (isHindi) "सिंक" else "⚡ Sync", fontSize = 10.sp, color = Color.White)
                    }
                }

                // TOAST BANNER FOR REMOTE ACTIONS
                AnimatedVisibility(
                    visible = toastMessage != null,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFF1E272E),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF6C5CE7)),
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 6.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF00E676), modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = toastMessage ?: "",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // SIMULATED INTERACTIVE CHILD PHONE SCREEN FRAME
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp)
                        .aspectRatio(9f / 16.5f)
                        .clip(RoundedCornerShape(32.dp))
                        .border(4.dp, Color(0xFF2C3E50), RoundedCornerShape(32.dp))
                        .background(Color(0xFF13181E))
                        .pointerInput(Unit) {
                            detectTapGestures { offset ->
                                if (isRemoteControlActive) {
                                    touchPointer = offset
                                    toastMessage = if (isHindi) "👆 टच भेजा गया (X:${offset.x.toInt()}, Y:${offset.y.toInt()})"
                                    else "👆 Injected touch at (${offset.x.toInt()}, ${offset.y.toInt()})"
                                }
                            }
                        }
                ) {
                    if (isScreenPoweredOff) {
                        // Screen Off state
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Black),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Default.PowerSettingsNew, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(48.dp))
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = if (isHindi) "बच्चे की स्क्रीन बंद है (स्लीप मोड)" else "Child Screen is Powered Off (Sleep)",
                                    color = Color.LightGray,
                                    fontSize = 12.sp
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Button(
                                    onClick = {
                                        isScreenPoweredOff = false
                                        toastMessage = if (isHindi) "💡 बच्चे की स्क्रीन को रिमोटली चालू किया गया" else "💡 Child Screen Woken Up Remotely"
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C5CE7)),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text(if (isHindi) "स्क्रीन चालू करें" else "Wake Up Screen", fontSize = 12.sp)
                                }
                            }
                        }
                    } else {
                        // Live Interactive Screen Content
                        Column(modifier = Modifier.fillMaxSize()) {
                            // CHILD PHONE STATUS BAR
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFF0A0D10))
                                    .padding(horizontal = 14.dp, vertical = 5.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("10:45 AM", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    if (remoteWifiOn) {
                                        Icon(Icons.Default.Wifi, contentDescription = null, tint = Color.White, modifier = Modifier.size(13.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                    }
                                    if (remoteBluetoothOn) {
                                        Icon(Icons.Default.Bluetooth, contentDescription = null, tint = Color.White, modifier = Modifier.size(13.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                    }
                                    Text("4G 🔋 ${child.batteryPercent}%", color = Color.White, fontSize = 10.sp)
                                }
                            }

                            // ACTIVE SCREEN BODY (Based on activeApp)
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth()
                            ) {
                                when (activeApp) {
                                    // 1. HOME LAUNCHER SCREEN
                                    "HOME" -> {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .background(
                                                    Brush.verticalGradient(listOf(Color(0xFF1B2838), Color(0xFF0D1B2A)))
                                                )
                                                .padding(14.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            // Clock & Weather Widget
                                            Text("10:45", color = Color.White, fontSize = 34.sp, fontWeight = FontWeight.Light)
                                            Text("Thursday, 20 August • 28°C", color = Color(0xFFA4B0BE), fontSize = 11.sp)

                                            Spacer(modifier = Modifier.height(14.dp))

                                            // Search Bar
                                            Surface(
                                                modifier = Modifier.fillMaxWidth().height(36.dp),
                                                shape = RoundedCornerShape(18.dp),
                                                color = Color.White.copy(alpha = 0.15f)
                                            ) {
                                                Row(
                                                    modifier = Modifier.padding(horizontal = 12.dp),
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Text("G", color = Color(0xFF4285F4), fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                                    Spacer(modifier = Modifier.width(8.dp))
                                                    Text(if (isHindi) "Google सेफ सर्च..." else "Google SafeSearch...", color = Color.LightGray, fontSize = 11.sp)
                                                }
                                            }

                                            Spacer(modifier = Modifier.height(16.dp))

                                            // App Grid (Clickable by Parent!)
                                            Text(
                                                text = if (isHindi) "ऐप्स (टैप करके बच्चे के फोन में खोलें):" else "Apps (Tap to launch on child's phone):",
                                                color = Color(0xFFA4B0BE),
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                modifier = Modifier.align(Alignment.Start)
                                            )
                                            Spacer(modifier = Modifier.height(8.dp))

                                            val homeApps = listOf(
                                                Triple("Settings", Icons.Default.Settings, "SETTINGS"),
                                                Triple("WhatsApp", Icons.Default.Chat, "WHATSAPP"),
                                                Triple("YouTube", Icons.Default.PlayArrow, "YOUTUBE"),
                                                Triple("Chrome", Icons.Default.Language, "BROWSER"),
                                                Triple("Play Store", Icons.Default.ShoppingCart, "PLAYSTORE"),
                                                Triple("Camera", Icons.Default.PhotoCamera, "CAMERA"),
                                                Triple("Gallery", Icons.Default.Photo, "GALLERY"),
                                                Triple("Phone", Icons.Default.Phone, "PHONE")
                                            )

                                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                                for (row in homeApps.chunked(4)) {
                                                    Row(
                                                        modifier = Modifier.fillMaxWidth(),
                                                        horizontalArrangement = Arrangement.SpaceBetween
                                                    ) {
                                                        for ((appName, appIcon, appKey) in row) {
                                                            Column(
                                                                horizontalAlignment = Alignment.CenterHorizontally,
                                                                modifier = Modifier
                                                                    .clip(RoundedCornerShape(12.dp))
                                                                    .clickable {
                                                                        activeApp = appKey
                                                                        toastMessage = if (isHindi) "🚀 $appName बच्चे के फोन पर खोला गया" else "🚀 Opened $appName on child phone"
                                                                    }
                                                                    .padding(4.dp)
                                                            ) {
                                                                Box(
                                                                    modifier = Modifier
                                                                        .size(42.dp)
                                                                        .clip(RoundedCornerShape(12.dp))
                                                                        .background(
                                                                            when (appKey) {
                                                                                "SETTINGS" -> Color(0xFF4A5568)
                                                                                "WHATSAPP" -> Color(0xFF25D366)
                                                                                "YOUTUBE" -> Color(0xFFFF0000)
                                                                                "BROWSER" -> Color(0xFF4285F4)
                                                                                "PLAYSTORE" -> Color(0xFF00C853)
                                                                                "CAMERA" -> Color(0xFF6C5CE7)
                                                                                "GALLERY" -> Color(0xFFFF9800)
                                                                                else -> Color(0xFF00B894)
                                                                            }
                                                                        ),
                                                                    contentAlignment = Alignment.Center
                                                                ) {
                                                                    Icon(appIcon, contentDescription = null, tint = Color.White, modifier = Modifier.size(22.dp))
                                                                }
                                                                Spacer(modifier = Modifier.height(3.dp))
                                                                Text(appName, color = Color.White, fontSize = 9.sp, maxLines = 1)
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    // 2. REMOTE SYSTEM SETTINGS (फुल फ़ोन सेटिंग्स कंट्रोल)
                                    "SETTINGS" -> {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .background(Color(0xFF1E1E24))
                                                .verticalScroll(rememberScrollState())
                                                .padding(12.dp)
                                        ) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Icon(Icons.Default.Settings, contentDescription = null, tint = Color(0xFF6C5CE7))
                                                    Spacer(modifier = Modifier.width(6.dp))
                                                    Text(if (isHindi) "डिवाइस सेटिंग्स (रिमोट)" else "Device Settings (Remote)", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                                }
                                                Surface(shape = RoundedCornerShape(6.dp), color = Color(0xFF00B894)) {
                                                    Text("LIVE SYNC", color = Color.White, fontSize = 8.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp))
                                                }
                                            }

                                            Spacer(modifier = Modifier.height(10.dp))

                                            // WiFi Toggle
                                            Surface(shape = RoundedCornerShape(10.dp), color = Color(0xFF2B2B36), modifier = Modifier.fillMaxWidth()) {
                                                Row(
                                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                                        Icon(if (remoteWifiOn) Icons.Default.Wifi else Icons.Default.WifiOff, contentDescription = null, tint = if (remoteWifiOn) Color(0xFF00E676) else Color.Gray, modifier = Modifier.size(20.dp))
                                                        Spacer(modifier = Modifier.width(8.dp))
                                                        Column {
                                                            Text("Wi-Fi Network", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                                                            Text(if (remoteWifiOn) "Home_5G_Protected" else "Disconnected", color = Color.LightGray, fontSize = 10.sp)
                                                        }
                                                    }
                                                    Switch(
                                                        checked = remoteWifiOn,
                                                        onCheckedChange = {
                                                            remoteWifiOn = it
                                                            toastMessage = if (it) "📶 WiFi turned ON remotely" else "📶 WiFi turned OFF remotely"
                                                        },
                                                        colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = Color(0xFF6C5CE7))
                                                    )
                                                }
                                            }

                                            Spacer(modifier = Modifier.height(6.dp))

                                            // Bluetooth & GPS
                                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                                Surface(
                                                    shape = RoundedCornerShape(10.dp),
                                                    color = Color(0xFF2B2B36),
                                                    modifier = Modifier.weight(1f).clickable {
                                                        remoteBluetoothOn = !remoteBluetoothOn
                                                        toastMessage = "Bluetooth: ${if (remoteBluetoothOn) "ON" else "OFF"}"
                                                    }
                                                ) {
                                                    Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                                                        Icon(Icons.Default.Bluetooth, contentDescription = null, tint = if (remoteBluetoothOn) Color(0xFF4285F4) else Color.Gray, modifier = Modifier.size(18.dp))
                                                        Spacer(modifier = Modifier.width(6.dp))
                                                        Text("Bluetooth: ${if (remoteBluetoothOn) "ON" else "OFF"}", color = Color.White, fontSize = 10.sp)
                                                    }
                                                }

                                                Surface(
                                                    shape = RoundedCornerShape(10.dp),
                                                    color = Color(0xFF2B2B36),
                                                    modifier = Modifier.weight(1f).clickable {
                                                        remoteGpsOn = !remoteGpsOn
                                                        toastMessage = "GPS: ${if (remoteGpsOn) "High Accuracy" else "OFF"}"
                                                    }
                                                ) {
                                                    Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                                                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = if (remoteGpsOn) Color(0xFF00E676) else Color.Gray, modifier = Modifier.size(18.dp))
                                                        Spacer(modifier = Modifier.width(6.dp))
                                                        Text("GPS: ${if (remoteGpsOn) "ON" else "OFF"}", color = Color.White, fontSize = 10.sp)
                                                    }
                                                }
                                            }

                                            Spacer(modifier = Modifier.height(8.dp))

                                            // Brightness Slider
                                            Text(if (isHindi) "स्क्रीन ब्राइटनेस: ${(remoteBrightness * 100).toInt()}%" else "Screen Brightness: ${(remoteBrightness * 100).toInt()}%", color = Color.LightGray, fontSize = 11.sp)
                                            Slider(
                                                value = remoteBrightness,
                                                onValueChange = {
                                                    remoteBrightness = it
                                                    toastMessage = "🔆 Brightness adjusted to ${(it * 100).toInt()}%"
                                                },
                                                colors = SliderDefaults.colors(thumbColor = Color(0xFF6C5CE7), activeTrackColor = Color(0xFF6C5CE7))
                                            )

                                            // Volume Slider
                                            Text(if (isHindi) "मीडिया वॉल्यूम: ${(remoteVolume * 100).toInt()}%" else "Media Volume: ${(remoteVolume * 100).toInt()}%", color = Color.LightGray, fontSize = 11.sp)
                                            Slider(
                                                value = remoteVolume,
                                                onValueChange = {
                                                    remoteVolume = it
                                                    toastMessage = "🔊 Volume adjusted to ${(it * 100).toInt()}%"
                                                },
                                                colors = SliderDefaults.colors(thumbColor = Color(0xFF00B894), activeTrackColor = Color(0xFF00B894))
                                            )

                                            Spacer(modifier = Modifier.height(4.dp))

                                            // Permissions & Security Guard
                                            Text(if (isHindi) "सुरक्षा व अनुमतियां (Security & Lock):" else "Security & App Permissions:", color = Color(0xFFA4B0BE), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                            Spacer(modifier = Modifier.height(4.dp))

                                            Surface(shape = RoundedCornerShape(10.dp), color = Color(0xFF2B2B36), modifier = Modifier.fillMaxWidth()) {
                                                Column(modifier = Modifier.padding(10.dp)) {
                                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                                        Text("Camera Permission", color = Color.White, fontSize = 11.sp)
                                                        Switch(checked = remoteCameraPermission, onCheckedChange = { remoteCameraPermission = it; toastMessage = "Camera permission: $it" })
                                                    }
                                                    HorizontalDivider(color = Color.White.copy(alpha = 0.08f))
                                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                                        Text("Microphone Permission", color = Color.White, fontSize = 11.sp)
                                                        Switch(checked = remoteMicPermission, onCheckedChange = { remoteMicPermission = it; toastMessage = "Microphone permission: $it" })
                                                    }
                                                    HorizontalDivider(color = Color.White.copy(alpha = 0.08f))
                                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                                        Text("Lock Settings Access", color = Color.White, fontSize = 11.sp)
                                                        Switch(checked = remoteSettingsLocked, onCheckedChange = { remoteSettingsLocked = it; toastMessage = "Child Settings Lock: $it" })
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    // 3. WHATSAPP REMOTE MESSENGER & CONTROLLER
                                    "WHATSAPP" -> {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .background(Color(0xFF111B21))
                                        ) {
                                            // WhatsApp Header
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .background(Color(0xFF202C33))
                                                    .padding(horizontal = 10.dp, vertical = 8.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Icon(
                                                    Icons.Default.ArrowBack,
                                                    contentDescription = "Back",
                                                    tint = Color.White,
                                                    modifier = Modifier.size(20.dp).clickable { activeApp = "HOME" }
                                                )
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Box(
                                                    modifier = Modifier
                                                        .size(30.dp)
                                                        .clip(CircleShape)
                                                        .background(Color(0xFF25D366)),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Icon(Icons.Default.Chat, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                                }
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Column(modifier = Modifier.weight(1f)) {
                                                    Text(whatsAppChatUser, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                                    Text("Online • Remote Monitored", color = Color(0xFF25D366), fontSize = 9.sp)
                                                }
                                            }

                                            // Chat Stream
                                            LazyColumn(
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .padding(horizontal = 10.dp, vertical = 6.dp),
                                                verticalArrangement = Arrangement.spacedBy(6.dp)
                                            ) {
                                                items(whatsAppMessages) { (msg, isMe) ->
                                                    Row(
                                                        modifier = Modifier.fillMaxWidth(),
                                                        horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start
                                                    ) {
                                                        Surface(
                                                            shape = RoundedCornerShape(10.dp),
                                                            color = if (isMe) Color(0xFF005C4B) else Color(0xFF202C33)
                                                        ) {
                                                            Text(
                                                                text = msg,
                                                                color = Color.White,
                                                                fontSize = 11.sp,
                                                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                                            )
                                                        }
                                                    }
                                                }
                                            }

                                            // Parent Remote Input for WhatsApp
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .background(Color(0xFF202C33))
                                                    .padding(6.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                OutlinedTextField(
                                                    value = whatsAppTypedText,
                                                    onValueChange = { whatsAppTypedText = it },
                                                    placeholder = { Text(if (isHindi) "मैसेज टाइप करें..." else "Type message...", color = Color.Gray, fontSize = 11.sp) },
                                                    modifier = Modifier.weight(1f).height(44.dp),
                                                    shape = RoundedCornerShape(20.dp),
                                                    colors = OutlinedTextFieldDefaults.colors(
                                                        focusedBorderColor = Color(0xFF25D366),
                                                        unfocusedBorderColor = Color.Transparent,
                                                        focusedTextColor = Color.White,
                                                        unfocusedTextColor = Color.White
                                                    ),
                                                    singleLine = true
                                                )
                                                Spacer(modifier = Modifier.width(6.dp))
                                                IconButton(
                                                    onClick = {
                                                        if (whatsAppTypedText.isNotBlank()) {
                                                            whatsAppMessages = whatsAppMessages + ("Parent: $whatsAppTypedText" to true)
                                                            toastMessage = if (isHindi) "💬 मैसेज भेजा गया: $whatsAppTypedText" else "💬 Sent message: $whatsAppTypedText"
                                                            whatsAppTypedText = ""
                                                        }
                                                    },
                                                    modifier = Modifier
                                                        .size(36.dp)
                                                        .clip(CircleShape)
                                                        .background(Color(0xFF25D366))
                                                ) {
                                                    Icon(Icons.Default.Send, contentDescription = "Send", tint = Color.White, modifier = Modifier.size(18.dp))
                                                }
                                            }
                                        }
                                    }

                                    // 4. YOUTUBE REMOTE OPERATOR
                                    "YOUTUBE" -> {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .background(Color(0xFF0F0F0F))
                                        ) {
                                            // Top Search & SafeSearch bar
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .background(Color(0xFF212121))
                                                    .padding(horizontal = 8.dp, vertical = 6.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White, modifier = Modifier.size(18.dp).clickable { activeApp = "HOME" })
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Surface(
                                                    shape = RoundedCornerShape(16.dp),
                                                    color = Color.White.copy(alpha = 0.12f),
                                                    modifier = Modifier.weight(1f).height(32.dp)
                                                ) {
                                                    Row(modifier = Modifier.padding(horizontal = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                                                        Icon(Icons.Default.Search, contentDescription = null, tint = Color.LightGray, modifier = Modifier.size(14.dp))
                                                        Spacer(modifier = Modifier.width(6.dp))
                                                        Text("Science for kids...", color = Color.LightGray, fontSize = 10.sp)
                                                    }
                                                }
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Surface(shape = RoundedCornerShape(4.dp), color = Color(0xFF00B894)) {
                                                    Text("SafeMode ON", color = Color.White, fontSize = 8.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp))
                                                }
                                            }

                                            // Video Player Frame
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(140.dp)
                                                    .background(Color(0xFF1E272E)),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                    Icon(
                                                        imageVector = if (isYouTubePlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                                        contentDescription = null,
                                                        tint = Color.White,
                                                        modifier = Modifier
                                                            .size(44.dp)
                                                            .clip(CircleShape)
                                                            .background(Color.Red)
                                                            .clickable {
                                                                isYouTubePlaying = !isYouTubePlaying
                                                                toastMessage = if (isYouTubePlaying) "▶️ Video Resumed" else "⏸️ Video Paused Remotely"
                                                            }
                                                            .padding(8.dp)
                                                    )
                                                    Spacer(modifier = Modifier.height(6.dp))
                                                    Text(youTubeVideoTitle, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp, textAlign = TextAlign.Center, modifier = Modifier.padding(horizontal = 12.dp))
                                                }
                                            }

                                            // Video Recommendations (Clickable)
                                            Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                                Text("Recommended Safe Educational Videos:", color = Color.Gray, fontSize = 10.sp)
                                                val vids = listOf(
                                                    "Math Tricks: Vedic Speed Math" to "Education • 12m",
                                                    "National Geographic: Wildlife Wonders" to "Science • 18m",
                                                    "Learn Coding: Scratch for Beginners" to "Coding • 15m"
                                                )
                                                for ((vTitle, vSub) in vids) {
                                                    Surface(
                                                        shape = RoundedCornerShape(8.dp),
                                                        color = Color(0xFF1E1E1E),
                                                        modifier = Modifier.fillMaxWidth().clickable {
                                                            youTubeVideoTitle = vTitle
                                                            isYouTubePlaying = true
                                                            toastMessage = "▶️ Switched video to: $vTitle"
                                                        }
                                                    ) {
                                                        Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                                                            Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.Red, modifier = Modifier.size(16.dp))
                                                            Spacer(modifier = Modifier.width(8.dp))
                                                            Column {
                                                                Text(vTitle, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                                                                Text(vSub, color = Color.Gray, fontSize = 9.sp)
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    // 5. CHROME BROWSER OPERATOR
                                    "BROWSER" -> {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .background(Color(0xFF202124))
                                        ) {
                                            // Address Bar
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .background(Color(0xFF292A2D))
                                                    .padding(horizontal = 8.dp, vertical = 6.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White, modifier = Modifier.size(18.dp).clickable { activeApp = "HOME" })
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Surface(
                                                    shape = RoundedCornerShape(14.dp),
                                                    color = Color(0xFF3C4043),
                                                    modifier = Modifier.weight(1f).height(30.dp)
                                                ) {
                                                    Row(modifier = Modifier.padding(horizontal = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                                                        Icon(Icons.Default.Lock, contentDescription = null, tint = Color(0xFF00E676), modifier = Modifier.size(12.dp))
                                                        Spacer(modifier = Modifier.width(6.dp))
                                                        Text(browserUrl, color = Color.White, fontSize = 10.sp, maxLines = 1)
                                                    }
                                                }
                                            }

                                            // Browser Content
                                            Column(
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .background(Color.White)
                                                    .padding(12.dp)
                                            ) {
                                                Text("National Geographic Kids", color = Color(0xFF1E272E), fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                                Spacer(modifier = Modifier.height(4.dp))
                                                Text("Safe browsing verified. All adult, gaming and restricted domains blocked by Parent Guard.", color = Color(0xFF2ED573), fontSize = 10.sp)
                                                Spacer(modifier = Modifier.height(10.dp))
                                                Surface(shape = RoundedCornerShape(8.dp), color = Color(0xFFF1F2F6), modifier = Modifier.fillMaxWidth().height(100.dp)) {
                                                    Box(contentAlignment = Alignment.Center) {
                                                        Text("Explore Science, Animals & Space Articles", color = Color(0xFF57606F), fontSize = 11.sp)
                                                    }
                                                }
                                                Spacer(modifier = Modifier.height(10.dp))
                                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                                    Button(
                                                        onClick = {
                                                            browserUrl = "https://khanacademy.org"
                                                            toastMessage = "🌐 Opened Khan Academy"
                                                        },
                                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C5CE7)),
                                                        modifier = Modifier.weight(1f),
                                                        shape = RoundedCornerShape(8.dp)
                                                    ) {
                                                        Text("Khan Academy", fontSize = 10.sp)
                                                    }
                                                    Button(
                                                        onClick = {
                                                            browserUrl = "https://wikipedia.org"
                                                            toastMessage = "🌐 Opened Wikipedia"
                                                        },
                                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2D3436)),
                                                        modifier = Modifier.weight(1f),
                                                        shape = RoundedCornerShape(8.dp)
                                                    ) {
                                                        Text("Wikipedia", fontSize = 10.sp)
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    // 6. PLAY STORE & APP CONTROL
                                    "PLAYSTORE" -> {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .background(Color(0xFF1E1E24))
                                                .padding(12.dp)
                                        ) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White, modifier = Modifier.size(18.dp).clickable { activeApp = "HOME" })
                                                    Spacer(modifier = Modifier.width(6.dp))
                                                    Text(if (isHindi) "Google Play Store (प्रबंधन)" else "Play Store (App Rules)", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                                }
                                            }

                                            Spacer(modifier = Modifier.height(10.dp))

                                            Surface(shape = RoundedCornerShape(10.dp), color = Color(0xFF2B2B36), modifier = Modifier.fillMaxWidth()) {
                                                Row(
                                                    modifier = Modifier.padding(10.dp),
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Column {
                                                        Text("Block New App Installs", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                                                        Text("Requires parent PIN to install", color = Color.Gray, fontSize = 10.sp)
                                                    }
                                                    Switch(
                                                        checked = playStoreBlockDownloads,
                                                        onCheckedChange = {
                                                            playStoreBlockDownloads = it
                                                            toastMessage = if (it) "🚫 New App Downloads Blocked" else "✅ App Downloads Allowed"
                                                        }
                                                    )
                                                }
                                            }

                                            Spacer(modifier = Modifier.height(10.dp))
                                            Text("Installed Apps on ${child.name}'s phone:", color = Color.LightGray, fontSize = 11.sp)
                                            Spacer(modifier = Modifier.height(6.dp))

                                            val childAppsList = listOf(
                                                "Free Fire" to "Blocked (Games)",
                                                "Roblox" to "Limited 30m/day",
                                                "Duolingo" to "Always Allowed",
                                                "Chrome" to "Filtered & Monitored"
                                            )

                                            for ((aName, aRule) in childAppsList) {
                                                Surface(shape = RoundedCornerShape(8.dp), color = Color(0xFF2B2B36), modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp)) {
                                                    Row(
                                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                                        horizontalArrangement = Arrangement.SpaceBetween,
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        Column {
                                                            Text(aName, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                                                            Text(aRule, color = Color.LightGray, fontSize = 9.sp)
                                                        }
                                                        Button(
                                                            onClick = {
                                                                toastMessage = "🛑 Force Stopped $aName on child phone"
                                                            },
                                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF4757)),
                                                            shape = RoundedCornerShape(6.dp),
                                                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                                                        ) {
                                                            Text("Force Stop", fontSize = 9.sp)
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    // 7. CAMERA / GALLERY / PHONE
                                    else -> {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .background(Color(0xFF1E272E)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                Icon(Icons.Default.Smartphone, contentDescription = null, tint = Color(0xFF6C5CE7), modifier = Modifier.size(48.dp))
                                                Spacer(modifier = Modifier.height(8.dp))
                                                Text(activeApp, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                                Spacer(modifier = Modifier.height(4.dp))
                                                Text(if (isHindi) "लाइव रिमोट कंट्रोल सक्रिय है" else "Live Remote Control Active", color = Color(0xFF00E676), fontSize = 11.sp)
                                                Spacer(modifier = Modifier.height(14.dp))
                                                Button(
                                                    onClick = { activeApp = "HOME" },
                                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C5CE7)),
                                                    shape = RoundedCornerShape(10.dp)
                                                ) {
                                                    Text(if (isHindi) "होम स्क्रीन पर जाएं" else "Back to Home Screen", fontSize = 12.sp)
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            // ANDROID NAVIGATION BAR (Back ◀, Home ⚪, Recents ⬛)
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFF0A0D10))
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Back Button
                                IconButton(
                                    onClick = {
                                        activeApp = "HOME"
                                        toastMessage = if (isHindi) "◀ रिमोट 'Back' दबाया गया" else "◀ Sent 'Back' key to child device"
                                    },
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White, modifier = Modifier.size(18.dp))
                                }

                                // Home Button
                                IconButton(
                                    onClick = {
                                        activeApp = "HOME"
                                        toastMessage = if (isHindi) "⚪ रिमोट 'Home' दबाया गया" else "⚪ Sent 'Home' key to child device"
                                    },
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(12.dp)
                                            .clip(CircleShape)
                                            .border(2.dp, Color.White, CircleShape)
                                    )
                                }

                                // Recent Apps Button
                                IconButton(
                                    onClick = {
                                        showAppDrawerDialog = true
                                        toastMessage = if (isHindi) "⬛ रीसेंट ऐप्स खोला गया" else "⬛ Opened Recent App Switcher"
                                    },
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(10.dp)
                                            .border(2.dp, Color.White, RoundedCornerShape(2.dp))
                                    )
                                }
                            }
                        }
                    }

                    // Touch Indicator Ripple
                    touchPointer?.let { pos ->
                        Box(
                            modifier = Modifier
                                .offset(x = (pos.x.toInt() - 20).dp, y = (pos.y.toInt() - 20).dp)
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF6C5CE7).copy(alpha = 0.4f))
                                .border(2.dp, Color.White, CircleShape)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // BOTTOM PARENT CONTROL DOCK (Quick Commands & Remote Actions)
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1B2129)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp)
                ) {
                    Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                        // Quick Action Buttons Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // 1. App Drawer
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.clickable { showAppDrawerDialog = true }
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF6C5CE7)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Apps, contentDescription = "Apps", tint = Color.White, modifier = Modifier.size(20.dp))
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(if (isHindi) "ऐप्स खोलें" else "Launch App", color = Color.White, fontSize = 9.sp)
                            }

                            // 2. Remote Settings
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.clickable {
                                    activeApp = "SETTINGS"
                                    isScreenPoweredOff = false
                                    toastMessage = if (isHindi) "⚙️ फ़ोन सेटिंग्स खोली गई" else "⚙️ Opened Child Device Settings"
                                }
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF00B894)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Settings, contentDescription = "Settings", tint = Color.White, modifier = Modifier.size(20.dp))
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(if (isHindi) "सेटिंग्स" else "Settings", color = Color.White, fontSize = 9.sp)
                            }

                            // 3. Remote Text Input
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.clickable { showRemoteTextInputDialog = true }
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF0984E3)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Keyboard, contentDescription = "Keyboard", tint = Color.White, modifier = Modifier.size(20.dp))
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(if (isHindi) "टेक्स्ट टाइप" else "Type Text", color = Color.White, fontSize = 9.sp)
                            }

                            // 4. Remote Screen Off / Power
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.clickable {
                                    isScreenPoweredOff = !isScreenPoweredOff
                                    toastMessage = if (isScreenPoweredOff) (if (isHindi) "📴 स्क्रीन बंद की गई" else "📴 Screen Powered Off")
                                    else (if (isHindi) "💡 स्क्रीन चालू की गई" else "💡 Screen Turned On")
                                }
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .background(if (isScreenPoweredOff) Color(0xFFE17055) else Color(0xFF636E72)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.PowerSettingsNew, contentDescription = "Power", tint = Color.White, modifier = Modifier.size(20.dp))
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(if (isScreenPoweredOff) (if (isHindi) "स्क्रीन चालू" else "Screen On") else (if (isHindi) "स्क्रीन बंद" else "Screen Off"), color = Color.White, fontSize = 9.sp)
                            }

                            // 5. Freeze / Instant Lock
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.clickable {
                                    onInstantLock()
                                    toastMessage = if (isHindi) "🔒 बच्चे की स्क्रीन तुरंत फ्रीज और लॉक की गई" else "🔒 Child Screen Frozen & Locked"
                                    onDismiss()
                                }
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFFF4757)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Lock, contentDescription = "Lock", tint = Color.White, modifier = Modifier.size(20.dp))
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(if (isHindi) "फ्रीज स्क्रीन" else "Freeze Lock", color = Color.White, fontSize = 9.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

// 3. ONE-WAY AUDIO AMBIENT LISTENER DIALOG
@Composable
fun OneWayAudioDialog(
    child: ChildProfile,
    isHindi: Boolean,
    onDismiss: () -> Unit
) {
    var isRecording by remember { mutableStateOf(false) }
    var audioVolume by remember { mutableFloatStateOf(0.75f) }
    var soundLevelDb by remember { mutableIntStateOf(42) }
    var isConnecting by remember { mutableStateOf(true) }
    var syncToast by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        isConnecting = true
        delay(4000)
        isConnecting = false
    }

    LaunchedEffect(syncToast) {
        if (syncToast != null) {
            delay(2000)
            syncToast = null
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "audio_wave")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.25f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    LaunchedEffect(Unit) {
        while (true) {
            delay(800)
            soundLevelDb = Random.nextInt(38, 55)
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF141916))
                .padding(24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Headphones, contentDescription = null, tint = NaturalGreen700)
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = if (isHindi) "वन-वे परिवेश ऑडियो" else "One-Way Surround Audio",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Text(
                                text = "${child.name} • ${child.deviceModel}",
                                color = Color.LightGray,
                                fontSize = 12.sp
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.1f))
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                    }
                }

                // Super-fast connection status & sync button
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (isConnecting) Color(0xFFFEF3C7) else Color(0xFFDCFCE7)
                    ) {
                        Text(
                            text = if (isConnecting) "⚡ 4s ऑडियो कनेक्टिंग..." else "⚡ सुपरफास्ट कनेक्टेड (0.2s)",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isConnecting) Color(0xFFD97706) else Color(0xFF16A34A),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                    Button(
                        onClick = {
                            isConnecting = true
                            syncToast = if (isHindi) "🔄 ऑडियो स्ट्रीम सिंक हो रही है..." else "🔄 Syncing audio stream..."
                            android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                                isConnecting = false
                            }, 1500)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0984E3)),
                        shape = RoundedCornerShape(6.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                        modifier = Modifier.height(26.dp)
                    ) {
                        Text(if (isHindi) "सिंक" else "⚡ Sync", fontSize = 10.sp, color = Color.White)
                    }
                }
                syncToast?.let { msg ->
                    Text(text = msg, fontSize = 11.sp, color = Color(0xFF74B9FF), fontWeight = FontWeight.Bold)
                }
                
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color.Red.copy(alpha = 0.15f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color.Red.copy(alpha = 0.5f)),
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text(
                        text = if (isHindi) "⚠️ डेमो / सिमुलेशन मोड" else "⚠️ DEMO / SIMULATION MODE",
                        color = Color(0xFFFF5252),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }

                // Pulsing Audio Visualizer Center
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(160.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        // Outer pulse ring
                        Box(
                            modifier = Modifier
                                .size(150.dp * pulseScale)
                                .clip(CircleShape)
                                .background(NaturalGreen700.copy(alpha = 0.15f))
                        )

                        // Middle pulse ring
                        Box(
                            modifier = Modifier
                                .size(110.dp)
                                .clip(CircleShape)
                                .background(NaturalGreen700.copy(alpha = 0.35f))
                        )

                        // Center Microphone button
                        Box(
                            modifier = Modifier
                                .size(74.dp)
                                .clip(CircleShape)
                                .background(NaturalGreen700),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Mic, contentDescription = null, tint = Color.White, modifier = Modifier.size(36.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = if (isHindi) "आस-पास की आवाज सुन रहे हैं..." else "Listening to surrounding audio...",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "$soundLevelDb dB (Normal Ambient Sound)",
                        color = Color(0xFFA5D6A7),
                        fontSize = 12.sp
                    )
                }

                // Volume and Controls
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.VolumeUp, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(12.dp))
                        Slider(
                            value = audioVolume,
                            onValueChange = { audioVolume = it },
                            modifier = Modifier.weight(1f),
                            colors = SliderDefaults.colors(
                                thumbColor = NaturalGreen700,
                                activeTrackColor = NaturalGreen700
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { isRecording = !isRecording },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isRecording) Terracotta700 else NaturalGreen700
                        ),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text(
                            text = if (isRecording) {
                                if (isHindi) "रिकॉर्डिंग रोकें (00:14)" else "Stop Recording (00:14)"
                            } else {
                                if (isHindi) "ऑडियो रिकॉर्ड करें" else "Start Audio Recording"
                            },
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

// 4. LIVE LOCATION & GEOFENCE DETAIL DIALOG
@Composable
fun LiveLocationDetailDialog(
    child: ChildProfile,
    geofenceZones: List<GeofenceZone>,
    isHindi: Boolean,
    onAddGeofence: (name: String, address: String, radius: Int) -> Unit,
    onDeleteGeofence: (Long) -> Unit,
    onDismiss: () -> Unit
) {
    var showAddZoneDialog by remember { mutableStateOf(false) }

    if (showAddZoneDialog) {
        var zoneName by remember { mutableStateOf("") }
        var zoneAddress by remember { mutableStateOf("") }
        var zoneRadius by remember { mutableFloatStateOf(200f) }

        AlertDialog(
            onDismissRequest = { showAddZoneDialog = false },
            title = {
                Text(
                    text = if (isHindi) "नया सुरक्षित क्षेत्र (Geofence) जोड़ें" else "Add Safe Geofence Zone",
                    fontWeight = FontWeight.Bold,
                    color = NaturalTextPrimary
                )
            },
            text = {
                Column {
                    OutlinedTextField(
                        value = zoneName,
                        onValueChange = { zoneName = it },
                        label = { Text(if (isHindi) "स्थान का नाम (e.g. ट्यूशन क्लास)" else "Place Name (e.g. Tuition Class)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = zoneAddress,
                        onValueChange = { zoneAddress = it },
                        label = { Text(if (isHindi) "पता" else "Address") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Radius: ${zoneRadius.toInt()} meters",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = NaturalGreen700
                    )
                    Slider(
                        value = zoneRadius,
                        onValueChange = { zoneRadius = it },
                        valueRange = 100f..1000f,
                        colors = SliderDefaults.colors(thumbColor = NaturalGreen700, activeTrackColor = NaturalGreen700)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (zoneName.isNotBlank()) {
                            onAddGeofence(zoneName.trim(), zoneAddress.trim(), zoneRadius.toInt())
                            showAddZoneDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = NaturalGreen700)
                ) {
                    Text(if (isHindi) "जोड़ें" else "Save Zone")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddZoneDialog = false }) {
                    Text(if (isHindi) "रद्द करें" else "Cancel", color = NaturalTextSecondary)
                }
            }
        )
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(NaturalBg)
        ) {
            // Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(NaturalSurface)
                    .padding(top = 36.dp, bottom = 12.dp, start = 16.dp, end = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, contentDescription = null, tint = NaturalGreen700, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isHindi) "लाइव लोकेशन और सुरक्षित क्षेत्र" else "Live Location & Safe Zones",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = NaturalTextPrimary
                    )
                }

                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = NaturalTextPrimary)
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Interactive Map View
                item {
                    Spacer(modifier = Modifier.height(4.dp))
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(240.dp)
                            .border(1.dp, NaturalBorder, RoundedCornerShape(24.dp)),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE2EFE0))
                    ) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            // Map Canvas drawing roads, terrain, and geofence ring
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                val w = size.width
                                val h = size.height

                                // Draw Roads
                                drawLine(Color.White, Offset(0f, h * 0.4f), Offset(w, h * 0.4f), strokeWidth = 14f)
                                drawLine(Color.White, Offset(w * 0.6f, 0f), Offset(w * 0.6f, h), strokeWidth = 12f)
                                drawLine(Color.White.copy(alpha = 0.7f), Offset(0f, h * 0.8f), Offset(w, h * 0.6f), strokeWidth = 8f)

                                // Draw Safe Zone Geofence Circle
                                drawCircle(
                                    color = NaturalGreen700.copy(alpha = 0.2f),
                                    radius = 80.dp.toPx(),
                                    center = Offset(w * 0.5f, h * 0.45f)
                                )
                                drawCircle(
                                    color = NaturalGreen700,
                                    radius = 80.dp.toPx(),
                                    center = Offset(w * 0.5f, h * 0.45f),
                                    style = Stroke(width = 2.dp.toPx())
                                )
                            }

                            // Center Pin for Child Device
                            Column(
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .offset(y = (-10).dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = NaturalGreen700,
                                    shadowElevation = 4.dp
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "${child.name} (🔋 ${child.batteryPercent}%)",
                                            color = Color.White,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }

                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = Terracotta700,
                                    modifier = Modifier.size(36.dp)
                                )
                            }

                            // Current GPS status badge
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color.White.copy(alpha = 0.9f),
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .padding(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(CircleShape)
                                            .background(NaturalGreen700)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "GPS High Accuracy (3m)",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = NaturalTextPrimary
                                    )
                                }
                            }
                        }
                    }
                }

                // Current Address Card
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, NaturalBorder, RoundedCornerShape(20.dp)),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = NaturalSurface)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (isHindi) "वर्तमान पता" else "Current Location Address",
                                    fontSize = 12.sp,
                                    color = NaturalTextSecondary,
                                    fontWeight = FontWeight.Medium
                                )
                                Surface(shape = RoundedCornerShape(8.dp), color = NaturalGreen100) {
                                    Text(
                                        text = child.geofenceStatus,
                                        fontSize = 10.sp,
                                        color = NaturalGreen700,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = child.locationAddress,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = NaturalTextPrimary
                            )
                            Text(
                                text = "Coordinates: ${child.locationCoordinates} • Updated 1 min ago",
                                fontSize = 11.sp,
                                color = NaturalTextTertiary
                            )
                        }
                    }
                }

                // Safe Zones Header & Add Button
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isHindi) "सुरक्षित क्षेत्र (Geofence Zones)" else "Safe Geofence Zones",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = NaturalTextPrimary
                        )

                        Button(
                            onClick = { showAddZoneDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = NaturalGreen700),
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(if (isHindi) "क्षेत्र जोड़ें" else "Add Zone", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // Safe Zones List
                items(geofenceZones) { zone ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, NaturalBorder, RoundedCornerShape(16.dp)),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = NaturalSurface)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(NaturalGreen100),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Shield, contentDescription = null, tint = NaturalGreen700, modifier = Modifier.size(18.dp))
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = if (isHindi && zone.nameHindi.isNotEmpty()) zone.nameHindi else zone.name,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = NaturalTextPrimary
                                    )
                                    Text(
                                        text = "${zone.address} • ${zone.radiusMeters}m radius",
                                        fontSize = 11.sp,
                                        color = NaturalTextSecondary
                                    )
                                }
                            }

                            IconButton(onClick = { onDeleteGeofence(zone.id) }) {
                                Icon(Icons.Default.Close, contentDescription = "Delete", tint = NaturalTextTertiary, modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

// 5. DETAILED USAGE REPORT DIALOG
@Composable
fun DetailedUsageReportDialog(
    child: ChildProfile,
    apps: List<AppUsageRule>,
    isHindi: Boolean,
    onDismiss: () -> Unit
) {
    val totalUsageMins = apps.sumOf { it.usageTodayMinutes }
    val totalLimitMins = child.weekdayLimitMinutes + child.bonusMinutesToday

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(NaturalBg)
        ) {
            // Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(NaturalSurface)
                    .padding(top = 36.dp, bottom = 12.dp, start = 16.dp, end = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isHindi) "विस्तृत उपयोग रिपोर्ट" else "Detailed Usage Report",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = NaturalTextPrimary
                )

                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = NaturalTextPrimary)
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(4.dp))
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, NaturalBorder, RoundedCornerShape(20.dp)),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = NaturalGreen100)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(18.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = if (isHindi) "आज का कुल स्क्रीन समय" else "Today's Total Screen Time",
                                    fontSize = 12.sp,
                                    color = NaturalGreen700,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = "${totalUsageMins / 60}h ${totalUsageMins % 60}m",
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = NaturalGreen900
                                )
                                Text(
                                    text = if (isHindi) "दैनिक लक्ष्य: ${totalLimitMins / 60}h ${totalLimitMins % 60}m" else "Daily Target: ${totalLimitMins / 60}h ${totalLimitMins % 60}m",
                                    fontSize = 11.sp,
                                    color = NaturalGreen700
                                )
                            }

                            // 3D-styled chart simulation
                            Box(
                                modifier = Modifier
                                    .size(70.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color.White.copy(alpha = 0.6f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    verticalAlignment = Alignment.Bottom,
                                    modifier = Modifier.padding(10.dp)
                                ) {
                                    Box(modifier = Modifier.width(8.dp).height(24.dp).clip(RoundedCornerShape(4.dp)).background(NaturalGreen700))
                                    Box(modifier = Modifier.width(8.dp).height(44.dp).clip(RoundedCornerShape(4.dp)).background(EarthAmber600))
                                    Box(modifier = Modifier.width(8.dp).height(32.dp).clip(RoundedCornerShape(4.dp)).background(Terracotta600))
                                    Box(modifier = Modifier.width(8.dp).height(18.dp).clip(RoundedCornerShape(4.dp)).background(NaturalGreen700))
                                }
                            }
                        }
                    }
                }

                item {
                    Text(
                        text = if (isHindi) "सर्वाधिक उपयोग किए गए ऐप्स" else "Most Used Apps Today",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = NaturalTextPrimary
                    )
                }

                items(apps.sortedByDescending { it.usageTodayMinutes }) { app ->
                    val (icon, color, _) = getCategoryDetails(app.category)
                    val progress = if (totalUsageMins > 0) app.usageTodayMinutes.toFloat() / totalUsageMins.toFloat() else 0f

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, NaturalBorder, RoundedCornerShape(16.dp)),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = NaturalSurface)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(NaturalSurfaceVariant),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(app.appName, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = NaturalTextPrimary)
                                        Text(app.category, fontSize = 10.sp, color = NaturalTextSecondary)
                                    }
                                }

                                Text(
                                    text = "${app.usageTodayMinutes} mins",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = NaturalGreen700
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            LinearProgressIndicator(
                                progress = { progress },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp)),
                                color = color,
                                trackColor = NaturalSurfaceVariant
                            )
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

// 6. PAIR / BIND DEVICE DIALOG
@Composable
fun PairDeviceDialog(
    onDismiss: () -> Unit,
    isHindi: Boolean,
    pairingCode: String = "9839247105",
    onRegenerateCode: () -> Unit = {}
) {
    val clipboardManager = androidx.compose.ui.platform.LocalClipboardManager.current
    var copied by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Smartphone, 
                    contentDescription = null, 
                    tint = Color(0xFF6C5CE7), 
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isHindi) "बच्चे का मोबाइल कनेक्ट करें" else "Connect Child's Phone",
                    fontWeight = FontWeight.Bold,
                    color = NaturalTextPrimary,
                    fontSize = 18.sp
                )
            }
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally, 
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (isHindi) 
                        "बच्चे के मोबाइल पर ऐप खोलें, 'बच्चा डिवाइस (Child)' चुनें और यह 10-डिजिट कोड दर्ज करें:" 
                        else "Open app on child's phone, select 'Child Mode', and enter this 10-digit code:",
                    fontSize = 13.sp,
                    color = NaturalTextSecondary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFFEDE9FF),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFF6C5CE7))
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 14.dp)
                    ) {
                        Text(
                            text = pairingCode,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 3.sp,
                            color = Color(0xFF6C5CE7)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = {
                            clipboardManager.setText(androidx.compose.ui.text.AnnotatedString(pairingCode))
                            copied = true
                        },
                        shape = RoundedCornerShape(20.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF6C5CE7))
                    ) {
                        Text(
                            text = if (copied) {
                                if (isHindi) "✓ कोड कॉपी हो गया!" else "✓ Code Copied!"
                            } else {
                                if (isHindi) "📋 कोड कॉपी करें" else "📋 Copy Code"
                            },
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF6C5CE7)
                        )
                    }

                    OutlinedButton(
                        onClick = {
                            onRegenerateCode()
                            copied = false
                        },
                        shape = RoundedCornerShape(20.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF6C7086))
                    ) {
                        Text(
                            text = if (isHindi) "🔄 नया कोड" else "🔄 New Code",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4A4E69)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F2F6)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = if (isHindi) "आसान स्टेप्स (3 Steps):" else "Easy Steps (3 Steps):",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2D3436)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (isHindi) 
                                "1. बच्चे के फोन में ऐप खोलें\n2. 'बच्चा डिवाइस (Child)' पर टैप करें\n3. यह 10-अंकों का कोड दर्ज करके 'डिवाइस लिंक करें' दबाएं"
                                else "1. Open app on child's phone\n2. Tap 'Child Device'\n3. Enter this 10-digit code and tap 'Link Child Device'",
                            fontSize = 11.sp,
                            lineHeight = 16.sp,
                            color = Color(0xFF636E72)
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C5CE7))
            ) {
                Text(if (isHindi) "ठीक है" else "Done", color = Color.White)
            }
        }
    )
}
