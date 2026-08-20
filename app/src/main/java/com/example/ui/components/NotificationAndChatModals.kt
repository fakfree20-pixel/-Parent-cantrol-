package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.ClearAll
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shield
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
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.AppNotificationItem
import com.example.data.model.ChildProfile
import com.example.data.model.WhatsAppConversation
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

// Helper data structure for parsed chat messages
data class ChatMessage(
    val sender: String,
    val text: String,
    val time: String,
    val isChild: Boolean
)

// 1. FULL WHATSAPP CHAT TRACKER & INSPECTOR DIALOG
@Composable
fun WhatsAppChatTrackerDialog(
    child: ChildProfile,
    conversations: List<WhatsAppConversation>,
    isHindi: Boolean,
    onDismiss: () -> Unit,
    onBlockWhatsApp: () -> Unit
) {
    var selectedConversation by remember { mutableStateOf<WhatsAppConversation?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var showOnlySuspicious by remember { mutableStateOf(false) }

    val filteredList = conversations.filter { convo ->
        val matchSearch = searchQuery.isBlank() || convo.contactName.contains(searchQuery, ignoreCase = true) || convo.lastMessage.contains(searchQuery, ignoreCase = true)
        val matchFilter = !showOnlySuspicious || convo.isFlaggedSuspicious
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
                    if (selectedConversation != null) {
                        IconButton(onClick = { selectedConversation = null }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = NaturalTextPrimary)
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF25D366)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Chat, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                    }

                    Column {
                        Text(
                            text = if (selectedConversation != null) {
                                selectedConversation!!.contactName
                            } else {
                                if (isHindi) "व्हाट्सएप चैट मॉनिटर" else "WhatsApp Chat Monitor"
                            },
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = NaturalTextPrimary
                        )
                        Text(
                            text = if (selectedConversation != null) {
                                selectedConversation!!.phoneNumber
                            } else {
                                "${child.name} • ${child.deviceModel}"
                            },
                            fontSize = 11.sp,
                            color = NaturalTextSecondary
                        )
                    }
                }

                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = NaturalTextPrimary)
                }
            }

            if (selectedConversation != null) {
                // DETAILED CONVERSATION VIEW
                DetailedChatConversationView(
                    convo = selectedConversation!!,
                    childName = child.name,
                    isHindi = isHindi,
                    onBlockWhatsApp = onBlockWhatsApp
                )
            } else {
                // CONVERSATIONS LIST
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(top = 12.dp, bottom = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Search & Alert banner
                    item {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text(if (isHindi) "संपर्क या संदेश खोजें..." else "Search contacts or messages...") },
                            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = NaturalTextSecondary) },
                            singleLine = true,
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    // Suspicious filter chips
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            FilterChip(
                                selected = !showOnlySuspicious,
                                onClick = { showOnlySuspicious = false },
                                label = { Text(if (isHindi) "सभी चैट (${conversations.size})" else "All Chats (${conversations.size})") },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = NaturalGreen100,
                                    selectedLabelColor = NaturalGreen700
                                )
                            )

                            FilterChip(
                                selected = showOnlySuspicious,
                                onClick = { showOnlySuspicious = true },
                                label = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Warning, contentDescription = null, tint = Terracotta700, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(if (isHindi) "संदिग्ध अलर्ट ⚠️" else "Suspicious Alerts ⚠️")
                                    }
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Terracotta100,
                                    selectedLabelColor = Terracotta700
                                )
                            )
                        }
                    }

                    // Total stats overview card
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, NaturalBorder, RoundedCornerShape(18.dp)),
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = NaturalSurface)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = if (isHindi) "व्हाट्सएप चैट गतिविधि आज" else "WhatsApp Chat Activity Today",
                                        fontSize = 12.sp,
                                        color = NaturalTextSecondary
                                    )
                                    Text(
                                        text = if (isHindi) "${conversations.sumOf { it.totalMessagesToday }} संदेश ट्रैक किए गए" else "${conversations.sumOf { it.totalMessagesToday }} messages tracked",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = NaturalGreen900
                                    )
                                }

                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = Color(0xFF25D366).copy(alpha = 0.15f)
                                ) {
                                    Text(
                                        text = "LIVE SYNC",
                                        color = Color(0xFF1E8E3E),
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    }

                    // Conversation items
                    items(filteredList) { convo ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(1.dp, RoundedCornerShape(18.dp))
                                .border(
                                    width = if (convo.isFlaggedSuspicious) 1.5.dp else 1.dp,
                                    color = if (convo.isFlaggedSuspicious) Terracotta600 else NaturalBorder,
                                    shape = RoundedCornerShape(18.dp)
                                )
                                .clickable { selectedConversation = convo },
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (convo.isFlaggedSuspicious) Terracotta100.copy(alpha = 0.3f) else NaturalSurface
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Contact Avatar
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(CircleShape)
                                        .background(
                                            if (convo.isFlaggedSuspicious) Terracotta100 else NaturalGreen100
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = convo.contactName.take(1).uppercase(),
                                        color = if (convo.isFlaggedSuspicious) Terracotta700 else NaturalGreen700,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp
                                    )
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = convo.contactName,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            color = NaturalTextPrimary,
                                            maxLines = 1
                                        )
                                        Text(
                                            text = convo.lastMessageTime,
                                            fontSize = 11.sp,
                                            color = NaturalTextTertiary
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(2.dp))

                                    Text(
                                        text = convo.lastMessage,
                                        fontSize = 12.sp,
                                        color = if (convo.isFlaggedSuspicious) Terracotta700 else NaturalTextSecondary,
                                        fontWeight = if (convo.isFlaggedSuspicious) FontWeight.Bold else FontWeight.Normal,
                                        maxLines = 1
                                    )

                                    if (convo.isFlaggedSuspicious) {
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = Terracotta100
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Icon(Icons.Default.Warning, contentDescription = null, tint = Terracotta700, modifier = Modifier.size(12.dp))
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text(
                                                    text = if (isHindi) "संदिग्ध पासवर्ड अनुरोध" else "Suspicious: Password / Account Risk",
                                                    color = Terracotta700,
                                                    fontSize = 9.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// Sub-component: Detailed Chat Conversation view
@Composable
private fun DetailedChatConversationView(
    convo: WhatsAppConversation,
    childName: String,
    isHindi: Boolean,
    onBlockWhatsApp: () -> Unit
) {
    // Sample messages parsed for display
    val messages = listOf(
        ChatMessage(convo.contactName, "Hey $childName, did you do the science homework?", "10:30 AM", false),
        ChatMessage(childName, "Yes, I am working on question 4 now.", "10:32 AM", true),
        ChatMessage(convo.contactName, convo.lastMessage, convo.lastMessageTime, false)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Contact Warning Alert Box if flagged
        if (convo.isFlaggedSuspicious) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Terracotta600, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Terracotta100)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Warning, contentDescription = null, tint = Terracotta700, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (isHindi) "सुरक्षा चेतावनी (Safety Alert)" else "Security Warning",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = Terracotta700
                        )
                        Text(
                            text = if (isHindi) "यह संपर्क बच्चे से व्यक्तिगत या खाता पासवर्ड मांग रहा है।"
                            else "This user requested sensitive password/account credentials.",
                            fontSize = 11.sp,
                            color = NaturalTextPrimary
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }

        // Chat Bubbles
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(messages) { msg ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = if (msg.isChild) Arrangement.End else Arrangement.Start
                ) {
                    Card(
                        shape = RoundedCornerShape(
                            topStart = 16.dp,
                            topEnd = 16.dp,
                            bottomStart = if (msg.isChild) 16.dp else 4.dp,
                            bottomEnd = if (msg.isChild) 4.dp else 16.dp
                        ),
                        colors = CardDefaults.cardColors(
                            containerColor = if (msg.isChild) NaturalGreen100 else NaturalSurface
                        ),
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .border(
                                1.dp,
                                if (msg.text.contains("password", ignoreCase = true)) Terracotta600 else NaturalBorder,
                                RoundedCornerShape(16.dp)
                            )
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = if (msg.isChild) childName else msg.sender,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (msg.isChild) NaturalGreen700 else EarthAmber600
                            )
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = msg.text,
                                fontSize = 13.sp,
                                color = if (msg.text.contains("password", ignoreCase = true)) Terracotta700 else NaturalTextPrimary,
                                fontWeight = if (msg.text.contains("password", ignoreCase = true)) FontWeight.Bold else FontWeight.Normal
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = msg.time,
                                fontSize = 9.sp,
                                color = NaturalTextTertiary,
                                modifier = Modifier.align(Alignment.End)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Quick Parent Action
        Button(
            onClick = onBlockWhatsApp,
            colors = ButtonDefaults.buttonColors(containerColor = Terracotta700),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (isHindi) "व्हाट्सएप तुरंत ब्लॉक करें" else "Block WhatsApp Instantly",
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// 2. ALL APP NOTIFICATIONS REAL-TIME FEED DIALOG
@Composable
fun AppNotificationsFeedDialog(
    child: ChildProfile,
    notifications: List<AppNotificationItem>,
    isHindi: Boolean,
    onClearAll: () -> Unit,
    onOpenWhatsAppMonitor: () -> Unit,
    onDismiss: () -> Unit
) {
    var selectedCategory by remember { mutableStateOf("ALL") }

    val filteredNotifications = notifications.filter { notif ->
        when (selectedCategory) {
            "WHATSAPP" -> notif.appName.contains("WhatsApp", ignoreCase = true)
            "FLAGGED" -> notif.isSuspiciousKeyword
            "CHAT" -> notif.category == "CHAT"
            else -> true
        }
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
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(NaturalGreen100),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Notifications, contentDescription = null, tint = NaturalGreen700, modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = if (isHindi) "सभी ऐप नोटिफिकेशन" else "All App Notifications",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = NaturalTextPrimary
                        )
                        Text(
                            text = "${child.name} • ${child.deviceModel}",
                            fontSize = 11.sp,
                            color = NaturalTextSecondary
                        )
                    }
                }

                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = NaturalTextPrimary)
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(top = 12.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Filter Categories Chips & Clear All Button
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            item {
                                FilterChip(
                                    selected = selectedCategory == "ALL",
                                    onClick = { selectedCategory = "ALL" },
                                    label = { Text(if (isHindi) "सभी" else "All (${notifications.size})") },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = NaturalGreen100,
                                        selectedLabelColor = NaturalGreen700
                                    )
                                )
                            }
                            item {
                                FilterChip(
                                    selected = selectedCategory == "WHATSAPP",
                                    onClick = { selectedCategory = "WHATSAPP" },
                                    label = { Text("WhatsApp") },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = Color(0xFF25D366).copy(alpha = 0.2f),
                                        selectedLabelColor = Color(0xFF1E8E3E)
                                    )
                                )
                            }
                            item {
                                FilterChip(
                                    selected = selectedCategory == "FLAGGED",
                                    onClick = { selectedCategory = "FLAGGED" },
                                    label = { Text(if (isHindi) "चेतावनी ⚠️" else "Alerts ⚠️") },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = Terracotta100,
                                        selectedLabelColor = Terracotta700
                                    )
                                )
                            }
                        }

                        IconButton(onClick = onClearAll) {
                            Icon(Icons.Default.ClearAll, contentDescription = "Clear All", tint = NaturalTextTertiary)
                        }
                    }
                }

                // WhatsApp Shortcut Card
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onDismiss()
                                onOpenWhatsAppMonitor()
                            },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF25D366).copy(alpha = 0.12f))
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF25D366)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Chat, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = if (isHindi) "व्हाट्सएप चैट ट्रैकर खोलें" else "Open WhatsApp Chat Tracker",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = Color(0xFF1E8E3E)
                                    )
                                    Text(
                                        text = if (isHindi) "देखें कि बच्चा किससे चैट कर रहा है" else "See who your child is chatting with",
                                        fontSize = 11.sp,
                                        color = NaturalTextSecondary
                                    )
                                }
                            }
                            Text(
                                text = "OPEN >",
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                color = Color(0xFF1E8E3E)
                            )
                        }
                    }
                }

                // Notifications Feed List
                items(filteredNotifications) { notif ->
                    val appColor = when {
                        notif.appName.contains("WhatsApp", ignoreCase = true) -> Color(0xFF25D366)
                        notif.appName.contains("YouTube", ignoreCase = true) -> Color(0xFFE53935)
                        notif.appName.contains("Instagram", ignoreCase = true) -> Color(0xFFC13584)
                        else -> NaturalGreen700
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                1.dp,
                                if (notif.isSuspiciousKeyword) Terracotta600 else NaturalBorder,
                                RoundedCornerShape(16.dp)
                            ),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (notif.isSuspiciousKeyword) Terracotta100.copy(alpha = 0.3f) else NaturalSurface
                        )
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
                                            .size(28.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(appColor.copy(alpha = 0.15f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = notif.appName.take(1),
                                            color = appColor,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = notif.appName,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = NaturalTextPrimary
                                    )
                                }

                                Text(
                                    text = formatTimestamp(notif.timestamp),
                                    fontSize = 10.sp,
                                    color = NaturalTextTertiary
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = notif.senderName,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (notif.isSuspiciousKeyword) Terracotta700 else NaturalTextPrimary
                            )

                            Spacer(modifier = Modifier.height(2.dp))

                            Text(
                                text = notif.messageContent,
                                fontSize = 12.sp,
                                color = NaturalTextSecondary
                            )

                            if (notif.isSuspiciousKeyword) {
                                Spacer(modifier = Modifier.height(6.dp))
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = Terracotta100
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(Icons.Default.Warning, contentDescription = null, tint = Terracotta700, modifier = Modifier.size(12.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = if (isHindi) "संवेदनशील सामग्री / पासवर्ड अलर्ट" else "Flagged: Password or Safety Risk",
                                            color = Terracotta700,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// Simple time formatter
private fun formatTimestamp(timestamp: Long): String {
    val diff = System.currentTimeMillis() - timestamp
    val mins = diff / (1000 * 60)
    return when {
        mins < 1 -> "Just now"
        mins < 60 -> "${mins}m ago"
        mins < 1440 -> "${mins / 60}h ago"
        else -> "Yesterday"
    }
}
