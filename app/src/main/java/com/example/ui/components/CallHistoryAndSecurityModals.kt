package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material.icons.filled.CallMade
import androidx.compose.material.icons.filled.CallMissed
import androidx.compose.material.icons.filled.CallReceived
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PhoneDisabled
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.CallLogItem
import com.example.data.model.ChildProfile
import com.example.ui.theme.EarthAmber100
import com.example.ui.theme.EarthAmber600
import com.example.ui.theme.NaturalBg
import com.example.ui.theme.NaturalBorder
import com.example.ui.theme.NaturalGreen100
import com.example.ui.theme.NaturalGreen700
import com.example.ui.theme.NaturalGreen900
import com.example.ui.theme.NaturalSurface
import com.example.ui.theme.NaturalSurfaceVariant
import com.example.ui.theme.NaturalTextPrimary
import com.example.ui.theme.NaturalTextSecondary
import com.example.ui.theme.NaturalTextTertiary
import com.example.ui.theme.Terracotta100
import com.example.ui.theme.Terracotta700

/**
 * 1. CALL HISTORY DIALOG (कोल हिस्ट्री व रिकॉर्डिंग ट्रैकर)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CallHistoryDialog(
    child: ChildProfile,
    callLogs: List<CallLogItem>,
    isHindi: Boolean,
    onDeleteCall: (Long) -> Unit,
    onClearAll: () -> Unit,
    onDismiss: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableIntStateOf(0) } // 0: All, 1: Incoming, 2: Outgoing, 3: Missed, 4: Flagged
    var playingAudioCallId by remember { mutableStateOf<Long?>(null) }

    val filteredCalls = remember(callLogs, searchQuery, selectedFilter) {
        callLogs.filter { item ->
            val matchesSearch = item.contactName.contains(searchQuery, ignoreCase = true) ||
                    item.phoneNumber.contains(searchQuery, ignoreCase = true)
            val matchesFilter = when (selectedFilter) {
                1 -> item.callType == "INCOMING"
                2 -> item.callType == "OUTGOING"
                3 -> item.callType == "MISSED"
                4 -> item.isSuspicious
                else -> true
            }
            matchesSearch && matchesFilter
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(NaturalBg),
            color = NaturalBg
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
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
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF0284C7)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Call,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = if (isHindi) "कॉल हिस्ट्री ट्रैकर" else "Call History Tracker",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = NaturalTextPrimary
                            )
                            Text(
                                text = if (isHindi) "${child.name} का फोन लॉग (${child.deviceModel})" else "${child.name}'s Phone Logs (${child.deviceModel})",
                                fontSize = 12.sp,
                                color = NaturalTextSecondary
                            )
                        }
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = NaturalTextPrimary)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Stats Overview Bar
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(1.dp, RoundedCornerShape(16.dp))
                        .border(1.dp, NaturalBorder, RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = NaturalSurface)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CallStatItem(
                            title = if (isHindi) "कुल कॉल" else "Total Calls",
                            value = callLogs.size.toString(),
                            color = NaturalTextPrimary
                        )
                        CallStatItem(
                            title = if (isHindi) "इनकमिंग" else "Incoming",
                            value = callLogs.count { it.callType == "INCOMING" }.toString(),
                            color = NaturalGreen700
                        )
                        CallStatItem(
                            title = if (isHindi) "आउटगोइंग" else "Outgoing",
                            value = callLogs.count { it.callType == "OUTGOING" }.toString(),
                            color = Color(0xFF0284C7)
                        )
                        CallStatItem(
                            title = if (isHindi) "संदिग्ध" else "Suspicious",
                            value = callLogs.count { it.isSuspicious }.toString(),
                            color = Terracotta700
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text(if (isHindi) "नाम या फोन नंबर खोजें..." else "Search contact or phone number...", fontSize = 13.sp) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = NaturalTextSecondary) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Close, contentDescription = "Clear", tint = NaturalTextSecondary)
                            }
                        }
                    },
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = NaturalSurface,
                        unfocusedContainerColor = NaturalSurface,
                        focusedBorderColor = NaturalGreen700,
                        unfocusedBorderColor = NaturalBorder
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Filter Chips Row
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val filterLabels = listOf(
                        if (isHindi) "सभी (${callLogs.size})" else "All (${callLogs.size})",
                        if (isHindi) "इनकमिंग" else "Incoming",
                        if (isHindi) "आउटगोइंग" else "Outgoing",
                        if (isHindi) "मिस्ड कॉल" else "Missed",
                        if (isHindi) "⚠️ संदिग्ध अलर्ट" else "⚠️ Suspicious"
                    )

                    items(filterLabels.size) { idx ->
                        FilterChip(
                            selected = selectedFilter == idx,
                            onClick = { selectedFilter = idx },
                            label = { Text(filterLabels[idx], fontSize = 12.sp, fontWeight = if (selectedFilter == idx) FontWeight.Bold else FontWeight.Normal) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = if (idx == 4) Terracotta100 else NaturalGreen100,
                                selectedLabelColor = if (idx == 4) Terracotta700 else NaturalGreen900
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Call Logs List
                if (filteredCalls.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.PhoneDisabled, contentDescription = null, tint = NaturalTextTertiary, modifier = Modifier.size(48.dp))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = if (isHindi) "कोई कॉल रिकॉर्ड नहीं मिला" else "No call logs found",
                                color = NaturalTextSecondary,
                                fontSize = 14.sp
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        items(filteredCalls, key = { it.id }) { call ->
                            CallLogCard(
                                call = call,
                                isHindi = isHindi,
                                isPlayingAudio = playingAudioCallId == call.id,
                                onTogglePlayAudio = {
                                    playingAudioCallId = if (playingAudioCallId == call.id) null else call.id
                                },
                                onDelete = { onDeleteCall(call.id) }
                            )
                        }
                    }
                }

                // Footer Actions
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (callLogs.isNotEmpty()) {
                        Button(
                            onClick = onClearAll,
                            colors = ButtonDefaults.buttonColors(containerColor = Terracotta100),
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = null, tint = Terracotta700, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(if (isHindi) "लॉग साफ़ करें" else "Clear History", color = Terracotta700, fontSize = 12.sp)
                        }
                    }

                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = NaturalGreen700),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f, fill = false)
                    ) {
                        Text(if (isHindi) "बंद करें" else "Done", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun CallStatItem(title: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = color)
        Text(text = title, fontSize = 10.sp, color = NaturalTextSecondary)
    }
}

@Composable
private fun CallLogCard(
    call: CallLogItem,
    isHindi: Boolean,
    isPlayingAudio: Boolean,
    onTogglePlayAudio: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(1.dp, RoundedCornerShape(16.dp))
            .border(
                1.dp,
                if (call.isSuspicious) Terracotta700 else NaturalBorder,
                RoundedCornerShape(16.dp)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (call.isSuspicious) Terracotta100.copy(alpha = 0.3f) else NaturalSurface
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    // Call type icon bubble
                    val (icon, iconTint, bgTint) = when (call.callType) {
                        "INCOMING" -> Triple(Icons.Default.CallReceived, NaturalGreen700, NaturalGreen100)
                        "OUTGOING" -> Triple(Icons.Default.CallMade, Color(0xFF0284C7), Color(0xFFE0F2FE))
                        "MISSED" -> Triple(Icons.Default.CallMissed, Terracotta700, Terracotta100)
                        else -> Triple(Icons.Default.CallEnd, Color.Gray, NaturalSurfaceVariant)
                    }

                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(bgTint),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(icon, contentDescription = call.callType, tint = iconTint, modifier = Modifier.size(20.dp))
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = call.contactName,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = NaturalTextPrimary
                            )

                            if (call.isSuspicious) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = Terracotta700
                                ) {
                                    Text(
                                        text = if (isHindi) "चेतावनी ⚠️" else "FLAGGED ⚠️",
                                        color = Color.White,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                    )
                                }
                            }
                        }

                        Text(
                            text = call.phoneNumber,
                            fontSize = 11.sp,
                            color = NaturalTextSecondary
                        )
                    }
                }

                // Call Duration and Time
                Column(horizontalAlignment = Alignment.End) {
                    val durationText = if (call.durationSeconds > 0) {
                        val m = call.durationSeconds / 60
                        val s = call.durationSeconds % 60
                        if (m > 0) "${m}m ${s}s" else "${s}s"
                    } else {
                        if (isHindi) "मिस्ड" else "Missed"
                    }

                    Text(
                        text = durationText,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (call.callType == "MISSED") Terracotta700 else NaturalTextPrimary
                    )

                    Text(
                        text = formatRelativeTime(call.timestamp, isHindi),
                        fontSize = 10.sp,
                        color = NaturalTextTertiary
                    )
                }
            }

            // Simulated Audio Recording Snippet (if available)
            if (call.hasRecording) {
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = NaturalSurfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(
                                    onClick = onTogglePlayAudio,
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(if (isPlayingAudio) Terracotta700 else NaturalGreen700)
                                ) {
                                    Icon(
                                        imageVector = if (isPlayingAudio) Icons.Default.Pause else Icons.Default.PlayArrow,
                                        contentDescription = "Play call recording",
                                        tint = Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(8.dp))

                                Column {
                                    Text(
                                        text = if (isHindi) "कॉल ऑडियो रिकॉर्डिंग (सुरक्षा स्निपेट)" else "Call Audio Recording (Safety Snippet)",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = NaturalTextPrimary
                                    )
                                    Text(
                                        text = if (isPlayingAudio) {
                                            if (isHindi) "चल रहा है... (सुरक्षित एन्क्रिप्शन)" else "Playing securely..."
                                        } else {
                                            if (isHindi) "टैप करके सुनें" else "Tap to listen"
                                        },
                                        fontSize = 10.sp,
                                        color = NaturalTextSecondary
                                    )
                                }
                            }

                            Text(
                                text = "🎙️ HQ",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = NaturalGreen700
                            )
                        }

                        if (isPlayingAudio) {
                            Spacer(modifier = Modifier.height(6.dp))
                            LinearProgressIndicator(
                                progress = { 0.45f },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(4.dp)
                                    .clip(RoundedCornerShape(2.dp)),
                                color = NaturalGreen700,
                                trackColor = NaturalBorder
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * 2. ANTI-UNINSTALL & TAMPER PROTECTION DIALOG (अनइंस्टॉल सुरक्षा व ऐप लॉक)
 */
@Composable
fun AntiUninstallProtectionDialog(
    child: ChildProfile,
    isHindi: Boolean,
    onToggleAntiUninstall: (enabled: Boolean, preventSettings: Boolean, preventReset: Boolean) -> Unit,
    onDismiss: () -> Unit
) {
    var antiUninstall by remember { mutableStateOf(child.antiUninstallEnabled) }
    var preventSettings by remember { mutableStateOf(child.preventSettingsAccess) }
    var preventReset by remember { mutableStateOf(child.preventFactoryReset) }
    var showSavedToast by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(6.dp, RoundedCornerShape(24.dp))
                .border(1.dp, NaturalBorder, RoundedCornerShape(24.dp)),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = NaturalSurface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
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
                                .size(42.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(NaturalGreen100),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Shield,
                                contentDescription = null,
                                tint = NaturalGreen700,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = if (isHindi) "एंटी-अनइंस्टॉल सुरक्षा" else "Anti-Uninstall Lock",
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.sp,
                                color = NaturalTextPrimary
                            )
                            Text(
                                text = if (isHindi) "बच्चा ऐप डिलीट नहीं कर सकता" else "Tamper-Proof Device Admin",
                                fontSize = 11.sp,
                                color = NaturalGreen700,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = NaturalTextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Main Master Switch Banner
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = if (antiUninstall) NaturalGreen100 else NaturalSurfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                            Icon(
                                imageVector = if (antiUninstall) Icons.Default.Lock else Icons.Default.LockOpen,
                                contentDescription = null,
                                tint = if (antiUninstall) NaturalGreen700 else Color.Gray,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = if (isHindi) "अनइंस्टॉल लॉक सक्रिय रखें" else "Active Uninstall Protection",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = NaturalTextPrimary
                                )
                                Text(
                                    text = if (antiUninstall) {
                                        if (isHindi) "ऐप हटाने के लिए पैरेंट पिन ज़रूरी है" else "Parent PIN required to remove app"
                                    } else {
                                        if (isHindi) "सुरक्षा बंद है" else "Protection disabled"
                                    },
                                    fontSize = 11.sp,
                                    color = NaturalTextSecondary
                                )
                            }
                        }

                        Switch(
                            checked = antiUninstall,
                            onCheckedChange = {
                                antiUninstall = it
                                onToggleAntiUninstall(it, preventSettings, preventReset)
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = NaturalGreen700
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Security Features Check List
                Text(
                    text = if (isHindi) "सुरक्षा सुविधाएं व पाबंदियां" else "Security Controls & Restrictions",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = NaturalTextPrimary
                )

                Spacer(modifier = Modifier.height(8.dp))

                // 1. Device Admin
                SecuritySettingToggleRow(
                    icon = Icons.Default.AdminPanelSettings,
                    title = if (isHindi) "डिवाइस एडमिनिस्ट्रेटर अनुमति" else "Device Administrator Rights",
                    subtitle = if (isHindi) "सिस्टम लेवल प्रोटेक्शन ऑन है" else "System level protection active",
                    checked = antiUninstall,
                    onCheckedChange = {
                        antiUninstall = it
                        onToggleAntiUninstall(it, preventSettings, preventReset)
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                // 2. Prevent Settings Access
                SecuritySettingToggleRow(
                    icon = Icons.Default.Security,
                    title = if (isHindi) "फ़ोन सेटिंग्स लॉक" else "Block Phone Settings",
                    subtitle = if (isHindi) "बच्चा सेटिंग्स में जाकर परमिशन नहीं बदल सकता" else "Prevents disabling accessibility/data",
                    checked = preventSettings,
                    onCheckedChange = {
                        preventSettings = it
                        onToggleAntiUninstall(antiUninstall, it, preventReset)
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                // 3. Prevent Factory Reset
                SecuritySettingToggleRow(
                    icon = Icons.Default.VpnKey,
                    title = if (isHindi) "फ़ैक्टरी रीसेट लॉक" else "Prevent Factory Reset",
                    subtitle = if (isHindi) "बिना अनुमति फ़ोन फ़ॉर्मेट नहीं होगा" else "Blocks unauthorized hard resets",
                    checked = preventReset,
                    onCheckedChange = {
                        preventReset = it
                        onToggleAntiUninstall(antiUninstall, preventSettings, it)
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // PIN Reminder Note
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = EarthAmber100.copy(alpha = 0.5f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Key, contentDescription = null, tint = EarthAmber600, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isHindi) "डिफ़ॉल्ट पैरेंट मास्टर पिन: 1234 (इसे केवल आप जानते हैं)" else "Default Parent Master PIN: 1234",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = NaturalTextPrimary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        onToggleAntiUninstall(antiUninstall, preventSettings, preventReset)
                        onDismiss()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = NaturalGreen700)
                ) {
                    Text(
                        text = if (isHindi) "सुरक्षा सेटिंग्स सहेजें" else "Save Protection Settings",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
private fun SecuritySettingToggleRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(NaturalSurfaceVariant)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
            Icon(icon, contentDescription = null, tint = NaturalGreen700, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(text = title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = NaturalTextPrimary)
                Text(text = subtitle, fontSize = 10.sp, color = NaturalTextSecondary)
            }
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = NaturalGreen700
            )
        )
    }
}

private fun formatRelativeTime(timestamp: Long, isHindi: Boolean): String {
    val diff = System.currentTimeMillis() - timestamp
    val mins = (diff / (1000 * 60)).toInt()
    val hours = (diff / (1000 * 60 * 60)).toInt()

    return when {
        mins < 1 -> if (isHindi) "अभी-अभी" else "Just now"
        mins < 60 -> if (isHindi) "$mins मिनट पहले" else "${mins}m ago"
        hours < 24 -> if (isHindi) "$hours घंटे पहले" else "${hours}h ago"
        else -> if (isHindi) "कल" else "Yesterday"
    }
}
