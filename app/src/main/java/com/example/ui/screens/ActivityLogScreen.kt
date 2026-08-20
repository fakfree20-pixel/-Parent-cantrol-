package com.example.ui.screens

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ActivityLogItem
import com.example.ui.theme.EarthAmber100
import com.example.ui.theme.EarthAmber600
import com.example.ui.theme.MossGreen100
import com.example.ui.theme.MossGreen600
import com.example.ui.theme.NaturalBg
import com.example.ui.theme.NaturalBorder
import com.example.ui.theme.NaturalCardBg
import com.example.ui.theme.NaturalGreen100
import com.example.ui.theme.NaturalGreen700
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

@Composable
fun ActivityLogScreen(
    logs: List<ActivityLogItem>,
    isHindi: Boolean,
    onClearLogs: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dateFormat = SimpleDateFormat("hh:mm a, dd MMM", Locale.getDefault())

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(NaturalBg)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 28.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = if (isHindi) "सुरक्षा और गतिविधि रिपोर्ट" else "Activity & Safety Logs",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = NaturalTextPrimary
                    )
                    Text(
                        text = if (isHindi) "रीयल-टाइम स्क्रीन और लॉक इवेंट्स" else "Real-time safety events & notifications",
                        fontSize = 11.sp,
                        color = NaturalTextSecondary
                    )
                }

                if (logs.isNotEmpty()) {
                    IconButton(onClick = onClearLogs) {
                        Icon(
                            imageVector = Icons.Default.DeleteSweep,
                            contentDescription = "Clear All Logs",
                            tint = NaturalTextSecondary
                        )
                    }
                }
            }
        }

        if (logs.isEmpty()) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, NaturalBorder, RoundedCornerShape(24.dp)),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = NaturalSurface)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(36.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = null,
                            tint = NaturalTextTertiary,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = if (isHindi) "कोई गतिविधि लॉग नहीं है।" else "No activity logs recorded yet.",
                            fontWeight = FontWeight.Bold,
                            color = NaturalTextPrimary
                        )
                        Text(
                            text = if (isHindi) "सभी लॉक, ऐप प्रतिबंध और बोनस समय यहाँ दिखेंगे" else "Locks, limits, and bonus events will appear here.",
                            fontSize = 12.sp,
                            color = NaturalTextSecondary
                        )
                    }
                }
            }
        } else {
            items(logs, key = { it.id }) { log ->
                val (icon, color, bgCol) = when (log.type) {
                    "INSTANT_LOCK" -> Triple(Icons.Default.Lock, Terracotta700, Terracotta100)
                    "LIMIT_ALERT" -> Triple(Icons.Default.Warning, EarthAmber600, EarthAmber100)
                    "APP_BLOCKED" -> Triple(Icons.Default.Lock, Terracotta600, Terracotta100)
                    "BONUS_TIME" -> Triple(Icons.Default.CheckCircle, NaturalGreen700, NaturalGreen100)
                    "BEDTIME" -> Triple(Icons.Default.Bedtime, MossGreen600, MossGreen100)
                    else -> Triple(Icons.Default.Security, NaturalGreen700, NaturalGreen100)
                }

                val formattedTime = try {
                    dateFormat.format(Date(log.timestamp))
                } catch (e: Exception) {
                    ""
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(1.dp, RoundedCornerShape(18.dp))
                        .border(1.dp, NaturalBorder, RoundedCornerShape(18.dp)),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = NaturalSurface)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(bgCol),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                tint = color,
                                modifier = Modifier.size(20.dp)
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
                                    text = if (isHindi && log.titleHindi.isNotEmpty()) log.titleHindi else log.title,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = NaturalTextPrimary
                                )
                                Text(
                                    text = formattedTime,
                                    fontSize = 10.sp,
                                    color = NaturalTextTertiary
                                )
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = if (isHindi && log.descriptionHindi.isNotEmpty()) log.descriptionHindi else log.description,
                                fontSize = 11.sp,
                                color = NaturalTextSecondary
                            )
                        }
                    }
                }
            }
        }
    }
}
