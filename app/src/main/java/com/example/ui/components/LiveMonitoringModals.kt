package com.example.ui.components

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddLocation
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.ScreenShare
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
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

// 1. REMOTE CAMERA LIVE STREAM DIALOG
@Composable
fun RemoteCameraDialog(
    child: ChildProfile,
    isHindi: Boolean,
    onDismiss: () -> Unit
) {
    var isBackCamera by remember { mutableStateOf(false) }
    var isFlashOn by remember { mutableStateOf(false) }
    var isAudioMuted by remember { mutableStateOf(false) }
    var snapshotTaken by remember { mutableStateOf(false) }
    var streamQuality by remember { mutableStateOf("720P HD • 30 FPS") }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            // Simulated Live Camera View
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                if (isBackCamera) Color(0xFF1E2D24) else Color(0xFF282F3A),
                                Color(0xFF0F1412),
                                Color.Black
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                // Animated live stream crosshairs & room simulation
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val cx = size.width / 2
                    val cy = size.height / 2

                    // Grid lines
                    drawLine(Color.White.copy(alpha = 0.1f), Offset(0f, cy), Offset(size.width, cy), strokeWidth = 1f)
                    drawLine(Color.White.copy(alpha = 0.1f), Offset(cx, 0f), Offset(cx, size.height), strokeWidth = 1f)

                    // Target corners
                    val boxSize = 220.dp.toPx()
                    val cornerLen = 24.dp.toPx()
                    val left = cx - boxSize / 2
                    val right = cx + boxSize / 2
                    val top = cy - boxSize / 2
                    val bottom = cy + boxSize / 2

                    drawArc(
                        color = if (isFlashOn) Color(0xFFFFD54F) else Color(0xFF4CAF50),
                        startAngle = 0f,
                        sweepAngle = 360f,
                        useCenter = false,
                        topLeft = Offset(left, top),
                        size = androidx.compose.ui.geometry.Size(boxSize, boxSize),
                        style = Stroke(width = 1.5f)
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.PhotoCamera,
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.6f),
                        modifier = Modifier.size(54.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = if (isBackCamera) {
                            if (isHindi) "${child.name} का रियर कैमरा लाइव" else "${child.name}'s Rear Camera Stream"
                        } else {
                            if (isHindi) "${child.name} का फ्रंट कैमरा लाइव" else "${child.name}'s Front Camera Stream"
                        },
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (isHindi) "डिवाइस: ${child.deviceModel} (लाइव कनेक्टेड)" else "Device: ${child.deviceModel} (Live Connected)",
                        color = Color(0xFFA5D6A7),
                        fontSize = 12.sp
                    )

                    if (snapshotTaken) {
                        Spacer(modifier = Modifier.height(14.dp))
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = NaturalGreen700
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (isHindi) "स्क्रीनशॉट सुरक्षित सहेजा गया!" else "Snapshot Saved Successfully!",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            // Top Status Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp, start = 16.dp, end = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.Black.copy(alpha = 0.6f))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(Color.Red)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("LIVE", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(streamQuality, color = Color.LightGray, fontSize = 10.sp)
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.6f))
                ) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                }
            }

            // Bottom Camera Controls
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(
                        Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.85f), Color.Black))
                    )
                    .padding(horizontal = 24.dp, vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Flash Toggle
                    IconButton(
                        onClick = { isFlashOn = !isFlashOn },
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(if (isFlashOn) Color(0xFFFFD54F) else Color.White.copy(alpha = 0.15f))
                    ) {
                        Icon(
                            Icons.Default.FlashOn,
                            contentDescription = "Flashlight",
                            tint = if (isFlashOn) Color.Black else Color.White
                        )
                    }

                    // Snapshot Shutter Button
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .border(4.dp, Color.White, CircleShape)
                            .background(Color.White.copy(alpha = 0.2f))
                            .clickable { snapshotTaken = true },
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(54.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                        )
                    }

                    // Flip Camera (Front / Rear)
                    IconButton(
                        onClick = { isBackCamera = !isBackCamera },
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.15f))
                    ) {
                        Icon(Icons.Default.Cameraswitch, contentDescription = "Switch Camera", tint = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = if (isHindi) "टैप करके फ्रंट/रियर कैमरा बदलें या स्नैपशॉट लें" else "Tap shutter for photo capture or flip front/rear camera",
                    color = Color.LightGray,
                    fontSize = 11.sp
                )
            }
        }
    }
}

// 2. SCREEN MIRRORING LIVE STREAM DIALOG
@Composable
fun ScreenMirroringDialog(
    child: ChildProfile,
    isHindi: Boolean,
    onInstantLock: () -> Unit,
    onDismiss: () -> Unit
) {
    var isPaused by remember { mutableStateOf(false) }
    var fps by remember { mutableIntStateOf(30) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1500)
            fps = Random.nextInt(28, 32)
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF0F1210))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 40.dp, bottom = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.ScreenShare, contentDescription = null, tint = Color(0xFF69F0AE))
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = if (isHindi) "${child.name} की लाइव स्क्रीन" else "${child.name}'s Live Screen",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                            Text(
                                text = "${child.deviceModel} • $fps FPS • 42ms ping",
                                color = Color.LightGray,
                                fontSize = 11.sp
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

                Spacer(modifier = Modifier.height(16.dp))

                // Simulated Child Device Frame
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 24.dp)
                        .aspectRatio(9f / 16f)
                        .clip(RoundedCornerShape(32.dp))
                        .border(4.dp, Color(0xFF2C3E30), RoundedCornerShape(32.dp))
                        .background(Color(0xFF1B231D)),
                    contentAlignment = Alignment.Center
                ) {
                    // Simulated Screen Content (e.g. YouTube Kids / Learning App)
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Phone Status bar
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("10:45 AM", color = Color.White, fontSize = 10.sp)
                            Text("📶 🔋 ${child.batteryPercent}%", color = Color.White, fontSize = 10.sp)
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // App in use representation
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFE53935))
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(Icons.Default.ScreenShare, contentDescription = null, tint = Color.White, modifier = Modifier.size(36.dp))
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("YouTube Kids Playing", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text("Educational Science Video", color = Color.White.copy(alpha = 0.8f), fontSize = 11.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Sub content
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(70.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color.White.copy(alpha = 0.08f))
                            )
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(70.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color.White.copy(alpha = 0.08f))
                            )
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        Text(
                            text = if (isHindi) "स्क्रीन मिररिंग सुरक्षित और एन्क्रिप्टेड है" else "Live Screen Streaming (Encrypted)",
                            color = Color.LightGray,
                            fontSize = 10.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Bottom Action buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = { isPaused = !isPaused },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text(if (isPaused) "Resume" else "Pause Sync", color = Color.White)
                    }

                    Button(
                        onClick = {
                            onInstantLock()
                            onDismiss()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Terracotta700),
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(if (isHindi) "स्क्रीन फ्रीज करें" else "Freeze Screen", fontWeight = FontWeight.Bold)
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
    isHindi: Boolean
) {
    val pairingCode = "782-901"

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.QrCode, contentDescription = null, tint = NaturalGreen700, modifier = Modifier.size(28.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isHindi) "नया फोन / डिवाइस जोड़ें" else "Pair Child's Device",
                    fontWeight = FontWeight.Bold,
                    color = NaturalTextPrimary
                )
            }
        },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = if (isHindi) "बच्चे के फोन पर ParentGuard Kids ऐप खोलें और यह 6 अंकों का कोड दर्ज करें:" else "Open ParentGuard Kids on your child's phone and enter this 6-digit code:",
                    fontSize = 13.sp,
                    color = NaturalTextSecondary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = NaturalGreen100,
                    border = androidx.compose.foundation.BorderStroke(1.dp, NaturalGreen700)
                ) {
                    Text(
                        text = pairingCode,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 4.sp,
                        color = NaturalGreen900,
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = if (isHindi) "या QR कोड स्कैन करें (स्वचालित पेयरिंग)" else "Or scan QR code with child's camera",
                    fontSize = 11.sp,
                    color = NaturalTextTertiary
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = NaturalGreen700)
            ) {
                Text(if (isHindi) "पूर्ण" else "Done")
            }
        }
    )
}
