package com.example.ui.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.*

data class ChildAppItem(
    val name: String,
    val packageName: String,
    val category: String,
    val screenTimeMinutes: Int,
    val isBlocked: Boolean,
    val icon: ImageVector,
    val color: Color
)

@Composable
fun RemoteAppLauncherDialog(
    isHindi: Boolean,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var activeLaunchedApp by remember { mutableStateOf<String?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    val childApps = remember {
        listOf(
            ChildAppItem("WhatsApp", "com.whatsapp", "Social", 45, false, Icons.Default.Chat, Color(0xFF25D366)),
            ChildAppItem("YouTube", "com.google.android.youtube", "Entertainment", 120, false, Icons.Default.PlayArrow, Color(0xFFFF0000)),
            ChildAppItem("Instagram", "com.instagram.android", "Social", 60, false, Icons.Default.CameraAlt, Color(0xFFE1306C)),
            ChildAppItem("Free Fire", "com.dts.freefireth", "Game", 95, false, Icons.Default.SportsEsports, Color(0xFFFF9f43)),
            ChildAppItem("Google Chrome", "com.android.chrome", "Browser", 30, false, Icons.Default.Public, Color(0xFF4285F4)),
            ChildAppItem("Gallery", "com.android.gallery3d", "Media", 15, false, Icons.Default.Image, Color(0xFF00B894)),
            ChildAppItem("Settings", "com.android.settings", "System", 5, true, Icons.Default.Settings, Color(0xFF636e72))
        )
    }

    val filteredApps = childApps.filter {
        it.name.contains(searchQuery, ignoreCase = true) || it.category.contains(searchQuery, ignoreCase = true)
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f)
                .shadow(12.dp, RoundedCornerShape(24.dp))
                .border(1.dp, NaturalBorder, RoundedCornerShape(24.dp)),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = NaturalSurface)
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
                                .size(42.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF6C5CE7).copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Apps,
                                contentDescription = null,
                                tint = Color(0xFF6C5CE7),
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = if (isHindi) "रिमोट ऐप लॉन्चर व कंट्रोल" else "Remote App Launcher",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = NaturalTextPrimary
                            )
                            Text(
                                text = if (isHindi) "स्क्रीन शेयर के बाद बच्चे का हर ऐप चलाएं" else "Control & open apps on child's device",
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

                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text(if (isHindi) "ऐप खोजें (Search App)..." else "Search app...", fontSize = 13.sp) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color(0xFF6C5CE7)) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF6C5CE7),
                        unfocusedBorderColor = NaturalBorder,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Active App Streaming Box (if any app is currently launched/viewed)
                if (activeLaunchedApp != null) {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xFF2D1E5E)
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(10.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFF00D2D3))
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (isHindi) "लाइव रिमोट स्क्रीन स्ट्रीमिंग: $activeLaunchedApp" else "Live Streaming: $activeLaunchedApp",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                                TextButton(onClick = { activeLaunchedApp = null }) {
                                    Text(if (isHindi) "बंद करें" else "Close", fontSize = 11.sp, color = Color(0xFFFF7675))
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(100.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0xFF1E1E2E)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (isHindi) "बच्चे के फोन पर $activeLaunchedApp सक्रिय रूप से चल रहा है..." else "Active remote session running for $activeLaunchedApp...",
                                    fontSize = 12.sp,
                                    color = Color(0xFFE2E8F0),
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(horizontal = 12.dp)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }

                // List of Apps
                Text(
                    text = if (isHindi) "बच्चे के डिवाइस के सभी इंस्टॉल ऐप्स (${filteredApps.size})" else "All Installed Apps (${filteredApps.size})",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = NaturalTextSecondary
                )
                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filteredApps) { app ->
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            color = Color.White,
                            shadowElevation = 1.dp
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(app.color.copy(alpha = 0.15f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = app.icon,
                                        contentDescription = app.name,
                                        tint = app.color,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = app.name,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = NaturalTextPrimary
                                    )
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = app.category,
                                            fontSize = 11.sp,
                                            color = NaturalTextSecondary
                                        )
                                        Text(text = " • ", fontSize = 11.sp, color = NaturalTextSecondary)
                                        Text(
                                            text = "${app.screenTimeMinutes} ${if (isHindi) "मिनट उपयोग" else "mins used"}",
                                            fontSize = 11.sp,
                                            color = Color(0xFF6C5CE7),
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }

                                Button(
                                    onClick = {
                                        activeLaunchedApp = app.name
                                        Toast.makeText(
                                            context,
                                            if (isHindi) "रिमोट कमांड भेजी गई: ${app.name} बच्चे के फोन पर खोला गया!" else "Remote Command Sent: Opened ${app.name} on child's phone!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    },
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C5CE7)),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                    modifier = Modifier.height(34.dp)
                                ) {
                                    Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(2.dp))
                                    Text(
                                        text = if (isHindi) "चलाएं" else "Launch",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2D1E5E))
                ) {
                    Text(
                        text = if (isHindi) "बंद करें (Close)" else "Close",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}
