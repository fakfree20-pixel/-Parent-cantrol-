package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CallReceived
import androidx.compose.material.icons.filled.CallMade
import androidx.compose.material.icons.filled.ClearAll
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Sms
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.R
import com.example.data.model.ChildProfile
import com.example.data.model.SmsMessageItem
import com.example.data.model.YouTubeWatchItem
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// 1. FULL SMS TRACKING DIALOG (SMS ट्रैकिंग)
@Composable
fun SmsTrackingDialog(
    child: ChildProfile,
    smsMessages: List<SmsMessageItem>,
    isHindi: Boolean,
    onDismiss: () -> Unit,
    onClearSmsLogs: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("ALL") } // ALL, SUSPICIOUS, INCOMING, OUTGOING
    var showClearConfirmation by remember { mutableStateOf(false) }

    val filteredList = smsMessages.filter { msg ->
        val matchSearch = searchQuery.isBlank() ||
                msg.senderOrRecipient.contains(searchQuery, ignoreCase = true) ||
                msg.senderName.contains(searchQuery, ignoreCase = true) ||
                msg.messageBody.contains(searchQuery, ignoreCase = true)

        val matchFilter = when (selectedFilter) {
            "SUSPICIOUS" -> msg.isSuspicious
            "INCOMING" -> msg.isIncoming
            "OUTGOING" -> !msg.isIncoming
            else -> true
        }
        matchSearch && matchFilter
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
            // Header Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(NaturalSurface)
                    .padding(top = 36.dp, bottom = 12.dp, start = 16.dp, end = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = NaturalTextPrimary)
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Text(
                            text = if (isHindi) "SMS ट्रैकिंग और सुरक्षा" else "SMS Message Tracker",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = NaturalTextPrimary
                        )
                        Text(
                            text = "${child.name} • ${smsMessages.size} ${if (isHindi) "संदेश रिकॉर्ड" else "messages"}",
                            fontSize = 12.sp,
                            color = NaturalTextSecondary
                        )
                    }
                }

                if (smsMessages.isNotEmpty()) {
                    IconButton(
                        onClick = { showClearConfirmation = true },
                        modifier = Modifier.testTag("clear_sms_button")
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Clear", tint = Terracotta600)
                    }
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // 3D Digital Photo Illustration Hero
                item {
                    FeatureHeroBanner(
                        imageRes = R.drawable.img_sms_chat_monitor_1787126196512,
                        title = if (isHindi) "SMS सुरक्षा गार्ड" else "Live SMS Interceptor",
                        subtitle = if (isHindi) "संदिग्ध स्कैम, OTP और अज्ञात नंबरों की निगरानी" else "Live scanning for spam, predators & phishing links",
                        badgeText = if (isHindi) "सक्रिय स्कैनर" else "ACTIVE SHIELD",
                        badgeColor = NaturalGreen700
                    )
                }

                // Search Bar
                item {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text(if (isHindi) "SMS या नंबर खोजें..." else "Search SMS sender or content...") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = NaturalTextSecondary) },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(Icons.Default.Close, contentDescription = null, tint = NaturalTextSecondary)
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("sms_search_input"),
                        shape = RoundedCornerShape(14.dp),
                        singleLine = true
                    )
                }

                // Filter Chips
                item {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        item {
                            FilterChip(
                                selected = selectedFilter == "ALL",
                                onClick = { selectedFilter = "ALL" },
                                label = { Text(if (isHindi) "सभी (${smsMessages.size})" else "All (${smsMessages.size})") },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = NaturalGreen700,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                        item {
                            val suspiciousCount = smsMessages.count { it.isSuspicious }
                            FilterChip(
                                selected = selectedFilter == "SUSPICIOUS",
                                onClick = { selectedFilter = "SUSPICIOUS" },
                                label = { Text(if (isHindi) "⚠️ संदिग्ध स्कैम ($suspiciousCount)" else "⚠️ Suspicious ($suspiciousCount)") },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Terracotta700,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                        item {
                            val inCount = smsMessages.count { it.isIncoming }
                            FilterChip(
                                selected = selectedFilter == "INCOMING",
                                onClick = { selectedFilter = "INCOMING" },
                                label = { Text(if (isHindi) "आवक SMS ($inCount)" else "Inbox ($inCount)") },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = NaturalGreen700,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }
                }

                // Message List
                if (filteredList.isEmpty()) {
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 24.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = NaturalSurface)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(Icons.Default.Sms, contentDescription = null, tint = NaturalTextTertiary, modifier = Modifier.size(48.dp))
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = if (isHindi) "कोई SMS नहीं मिला" else "No SMS messages found",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = NaturalTextSecondary
                                )
                            }
                        }
                    }
                } else {
                    items(filteredList, key = { it.id }) { msg ->
                        SmsMessageCardItem(msg = msg, isHindi = isHindi)
                    }
                }
            }
        }
    }

    if (showClearConfirmation) {
        AlertDialog(
            onDismissRequest = { showClearConfirmation = false },
            title = { Text(if (isHindi) "SMS रिकॉर्ड साफ़ करें?" else "Clear SMS Logs?") },
            text = { Text(if (isHindi) "क्या आप सभी SMS संदेशों का इतिहास हटाना चाहते हैं?" else "Are you sure you want to clear all recorded SMS messages?") },
            confirmButton = {
                Button(
                    onClick = {
                        onClearSmsLogs()
                        showClearConfirmation = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Terracotta700)
                ) {
                    Text(if (isHindi) "हाँ, साफ़ करें" else "Clear All")
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearConfirmation = false }) {
                    Text(if (isHindi) "रद्द करें" else "Cancel")
                }
            }
        )
    }
}

@Composable
fun SmsMessageCardItem(msg: SmsMessageItem, isHindi: Boolean) {
    val timeFormat = remember { SimpleDateFormat("hh:mm a, dd MMM", Locale.getDefault()) }
    val timeStr = timeFormat.format(Date(msg.timestamp))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(16.dp))
            .border(
                1.dp,
                if (msg.isSuspicious) Terracotta600 else NaturalBorder,
                RoundedCornerShape(16.dp)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (msg.isSuspicious) Terracotta100.copy(alpha = 0.5f) else NaturalSurface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(
                                if (msg.isSuspicious) Terracotta600.copy(alpha = 0.2f)
                                else if (msg.isIncoming) NaturalGreen100
                                else EarthAmber100
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (msg.isSuspicious) Icons.Default.Warning
                            else if (msg.isIncoming) Icons.Default.CallReceived
                            else Icons.Default.CallMade,
                            contentDescription = null,
                            tint = if (msg.isSuspicious) Terracotta700
                            else if (msg.isIncoming) NaturalGreen700
                            else EarthAmber600,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = if (msg.senderName.isNotBlank()) msg.senderName else msg.senderOrRecipient,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = NaturalTextPrimary
                        )
                        if (msg.senderName.isNotBlank()) {
                            Text(
                                text = msg.senderOrRecipient,
                                fontSize = 11.sp,
                                color = NaturalTextSecondary
                            )
                        }
                    }
                }

                Text(
                    text = timeStr,
                    fontSize = 11.sp,
                    color = NaturalTextTertiary
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Body
            Text(
                text = msg.messageBody,
                fontSize = 13.sp,
                color = NaturalTextPrimary,
                lineHeight = 18.sp
            )

            // Warning reason if suspicious
            if (msg.isSuspicious && msg.suspiciousReason.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Terracotta700.copy(alpha = 0.15f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Security, contentDescription = null, tint = Terracotta700, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "${if (isHindi) "अलर्ट: " else "Alert: "}${msg.suspiciousReason}",
                            color = Terracotta700,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

// 2. FULL YOUTUBE VIDEO SCREENING & WATCH HISTORY DIALOG (यूट्यूब वीडियो)
@Composable
fun YouTubeMonitoringDialog(
    child: ChildProfile,
    watchHistory: List<YouTubeWatchItem>,
    isHindi: Boolean,
    onDismiss: () -> Unit,
    onClearHistory: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("ALL") }
    var showClearConfirmation by remember { mutableStateOf(false) }

    val filteredList = watchHistory.filter { item ->
        val matchSearch = searchQuery.isBlank() ||
                item.videoTitle.contains(searchQuery, ignoreCase = true) ||
                item.channelName.contains(searchQuery, ignoreCase = true)

        val matchCat = when (selectedCategory) {
            "FLAGGED" -> item.isFlagged
            "EDUCATION" -> item.category.contains("Edu", ignoreCase = true) || item.category.contains("Sci", ignoreCase = true)
            "GAMING" -> item.category.contains("Game", ignoreCase = true)
            "ENTERTAINMENT" -> item.category.contains("Entertain", ignoreCase = true) || item.category.contains("Anim", ignoreCase = true)
            else -> true
        }
        matchSearch && matchCat
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
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(NaturalSurface)
                    .padding(top = 36.dp, bottom = 12.dp, start = 16.dp, end = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = NaturalTextPrimary)
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Text(
                            text = if (isHindi) "यूट्यूब वीडियो मॉनिटर" else "YouTube Safety & Videos",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = NaturalTextPrimary
                        )
                        Text(
                            text = "${child.name} • ${watchHistory.size} ${if (isHindi) "वीडियो देखे गए" else "videos watched"}",
                            fontSize = 12.sp,
                            color = NaturalTextSecondary
                        )
                    }
                }

                if (watchHistory.isNotEmpty()) {
                    IconButton(
                        onClick = { showClearConfirmation = true },
                        modifier = Modifier.testTag("clear_youtube_button")
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Clear", tint = Terracotta600)
                    }
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // 3D Digital Photo Illustration Hero for YouTube
                item {
                    FeatureHeroBanner(
                        imageRes = R.drawable.img_youtube_monitor_1787118779866,
                        title = if (isHindi) "यूट्यूब वीडियो शील्ड" else "YouTube Content Watchdog",
                        subtitle = if (isHindi) "बच्चों के वीडियो, सर्च हिस्ट्री और असुरक्षित सामग्री नियंत्रण" else "Monitors watch history & screens age-inappropriate content",
                        badgeText = if (isHindi) "सक्रिय फ़िल्टर" else "SMART FILTER",
                        badgeColor = Terracotta700
                    )
                }

                // Search Bar
                item {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text(if (isHindi) "वीडियो या चैनल खोजें..." else "Search video or channel...") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = NaturalTextSecondary) },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(Icons.Default.Close, contentDescription = null, tint = NaturalTextSecondary)
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("youtube_search_input"),
                        shape = RoundedCornerShape(14.dp),
                        singleLine = true
                    )
                }

                // Category Chips
                item {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        item {
                            FilterChip(
                                selected = selectedCategory == "ALL",
                                onClick = { selectedCategory = "ALL" },
                                label = { Text(if (isHindi) "सभी (${watchHistory.size})" else "All (${watchHistory.size})") },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = NaturalGreen700,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                        item {
                            val flaggedCount = watchHistory.count { it.isFlagged }
                            FilterChip(
                                selected = selectedCategory == "FLAGGED",
                                onClick = { selectedCategory = "FLAGGED" },
                                label = { Text(if (isHindi) "⚠️ अनुपयुक्त ($flaggedCount)" else "⚠️ Flagged ($flaggedCount)") },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Terracotta700,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                        item {
                            FilterChip(
                                selected = selectedCategory == "EDUCATION",
                                onClick = { selectedCategory = "EDUCATION" },
                                label = { Text(if (isHindi) "📚 शिक्षा व ज्ञान" else "📚 Education") },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = NaturalGreen700,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                        item {
                            FilterChip(
                                selected = selectedCategory == "GAMING",
                                onClick = { selectedCategory = "GAMING" },
                                label = { Text(if (isHindi) "🎮 गेमिंग" else "🎮 Gaming") },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = EarthAmber600,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }
                }

                // Video List
                if (filteredList.isEmpty()) {
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 24.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = NaturalSurface)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(Icons.Default.Movie, contentDescription = null, tint = NaturalTextTertiary, modifier = Modifier.size(48.dp))
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = if (isHindi) "कोई वीडियो इतिहास नहीं" else "No YouTube history found",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = NaturalTextSecondary
                                )
                            }
                        }
                    }
                } else {
                    items(filteredList, key = { it.id }) { item ->
                        YouTubeVideoCardItem(item = item, isHindi = isHindi)
                    }
                }
            }
        }
    }

    if (showClearConfirmation) {
        AlertDialog(
            onDismissRequest = { showClearConfirmation = false },
            title = { Text(if (isHindi) "यूट्यूब इतिहास साफ़ करें?" else "Clear YouTube History?") },
            text = { Text(if (isHindi) "क्या आप सभी देखे गए वीडियो का इतिहास हटाना चाहते हैं?" else "Are you sure you want to clear all watched video logs?") },
            confirmButton = {
                Button(
                    onClick = {
                        onClearHistory()
                        showClearConfirmation = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Terracotta700)
                ) {
                    Text(if (isHindi) "हाँ, साफ़ करें" else "Clear All")
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearConfirmation = false }) {
                    Text(if (isHindi) "रद्द करें" else "Cancel")
                }
            }
        )
    }
}

@Composable
fun YouTubeVideoCardItem(item: YouTubeWatchItem, isHindi: Boolean) {
    val timeFormat = remember { SimpleDateFormat("hh:mm a, dd MMM", Locale.getDefault()) }
    val timeStr = timeFormat.format(Date(item.timestamp))

    val categoryColor = when {
        item.isFlagged -> Terracotta700
        item.category.contains("Edu", ignoreCase = true) || item.category.contains("Sci", ignoreCase = true) -> NaturalGreen700
        item.category.contains("Game", ignoreCase = true) -> EarthAmber600
        else -> MossGreen600
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(16.dp))
            .border(
                1.dp,
                if (item.isFlagged) Terracotta600 else NaturalBorder,
                RoundedCornerShape(16.dp)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (item.isFlagged) Terracotta100.copy(alpha = 0.5f) else NaturalSurface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Thumbnail / Play Box
            Box(
                modifier = Modifier
                    .size(width = 86.dp, height = 64.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (item.isFlagged) Terracotta700 else NaturalGreen900),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (item.isFlagged) Icons.Default.Warning else Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
                // Duration overlay badge
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = Color.Black.copy(alpha = 0.8f),
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(4.dp)
                ) {
                    Text(
                        text = "${item.durationMinutes}m",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = categoryColor.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = item.category,
                            color = categoryColor,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    Text(
                        text = timeStr,
                        fontSize = 10.sp,
                        color = NaturalTextTertiary
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = item.videoTitle,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = NaturalTextPrimary,
                    maxLines = 2
                )

                Text(
                    text = item.channelName,
                    fontSize = 11.sp,
                    color = NaturalTextSecondary,
                    maxLines = 1
                )

                if (item.isFlagged && item.flagReason.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "⚠️ ${item.flagReason}",
                        color = Terracotta700,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}
