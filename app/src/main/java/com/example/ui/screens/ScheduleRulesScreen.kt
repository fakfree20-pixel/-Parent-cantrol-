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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ChildProfile
import com.example.data.model.WebFilterRule
import com.example.ui.components.formatMinutes
import com.example.ui.theme.EarthAmber100
import com.example.ui.theme.EarthAmber500
import com.example.ui.theme.EarthAmber600
import com.example.ui.theme.MeadowSky600
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

@Composable
fun ScheduleRulesScreen(
    child: ChildProfile,
    webRules: List<WebFilterRule>,
    isHindi: Boolean,
    onUpdateChild: (ChildProfile) -> Unit,
    onAddWebRule: (domain: String, category: String) -> Unit,
    onDeleteWebRule: (ruleId: Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var showAddWebDialog by remember { mutableStateOf(false) }

    if (showAddWebDialog) {
        var domainText by remember { mutableStateOf("") }
        var categoryText by remember { mutableStateOf("Custom Block") }

        AlertDialog(
            onDismissRequest = { showAddWebDialog = false },
            title = {
                Text(
                    text = if (isHindi) "वेबसाइट ब्लॉक करें" else "Block Website",
                    fontWeight = FontWeight.Bold,
                    color = NaturalTextPrimary
                )
            },
            text = {
                Column {
                    Text(
                        text = if (isHindi) "प्रतिबंधित करने के लिए डोमेन लिखें:" else "Enter domain to restrict:",
                        fontSize = 13.sp,
                        color = NaturalTextSecondary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = domainText,
                        onValueChange = { domainText = it },
                        placeholder = { Text("e.g. reddit.com", color = NaturalTextTertiary) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (domainText.isNotBlank()) {
                            onAddWebRule(domainText.trim(), categoryText)
                            showAddWebDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Terracotta700)
                ) {
                    Text(if (isHindi) "ब्लॉक करें" else "Block")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddWebDialog = false }) {
                    Text(if (isHindi) "रद्द करें" else "Cancel", color = NaturalTextSecondary)
                }
            }
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(NaturalBg)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 28.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Daily Screen Time Budget Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(1.dp, RoundedCornerShape(24.dp))
                    .border(1.dp, NaturalBorder, RoundedCornerShape(24.dp)),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = NaturalSurface)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(NaturalGreen100),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Timer, contentDescription = null, tint = NaturalGreen700, modifier = Modifier.size(20.dp))
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = if (isHindi) "दैनिक स्क्रीन टाइम बजट" else "Daily Screen Time Budget",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = NaturalTextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Weekday slider
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isHindi) "कार्यदिवस (सोम - शुक्र):" else "Weekdays (Mon - Fri):",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = NaturalTextSecondary
                        )
                        Text(
                            text = formatMinutes(child.weekdayLimitMinutes, isHindi),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = NaturalGreen700
                        )
                    }
                    Slider(
                        value = child.weekdayLimitMinutes.toFloat(),
                        onValueChange = { onUpdateChild(child.copy(weekdayLimitMinutes = it.toInt())) },
                        valueRange = 30f..360f,
                        steps = 10,
                        colors = SliderDefaults.colors(thumbColor = NaturalGreen700, activeTrackColor = NaturalGreen700, inactiveTrackColor = NaturalBorder)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Weekend slider
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isHindi) "सप्ताहांत (शनि - रवि):" else "Weekends (Sat - Sun):",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = NaturalTextSecondary
                        )
                        Text(
                            text = formatMinutes(child.weekendLimitMinutes, isHindi),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = EarthAmber600
                        )
                    }
                    Slider(
                        value = child.weekendLimitMinutes.toFloat(),
                        onValueChange = { onUpdateChild(child.copy(weekendLimitMinutes = it.toInt())) },
                        valueRange = 30f..480f,
                        steps = 14,
                        colors = SliderDefaults.colors(thumbColor = EarthAmber600, activeTrackColor = EarthAmber600, inactiveTrackColor = NaturalBorder)
                    )
                }
            }
        }

        // 2. Bedtime / Downtime Schedule Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(1.dp, RoundedCornerShape(24.dp))
                    .border(1.dp, NaturalBorder, RoundedCornerShape(24.dp)),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = NaturalSurface)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(MossGreen100),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Bedtime, contentDescription = null, tint = MossGreen600, modifier = Modifier.size(20.dp))
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = if (isHindi) "सोने का समय (Bedtime)" else "Bedtime Downtime",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = NaturalTextPrimary
                                )
                                Text(
                                    text = if (isHindi) "रात को फोन स्वतः बंद होगा" else "Blocks distracting apps automatically",
                                    fontSize = 11.sp,
                                    color = NaturalTextSecondary
                                )
                            }
                        }

                        Switch(
                            checked = child.bedtimeEnabled,
                            onCheckedChange = { onUpdateChild(child.copy(bedtimeEnabled = it)) },
                            colors = SwitchDefaults.colors(checkedThumbColor = NaturalGreen700, checkedTrackColor = NaturalGreen100)
                        )
                    }

                    if (child.bedtimeEnabled) {
                        Spacer(modifier = Modifier.height(14.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .background(NaturalSurfaceVariant)
                                .padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(if (isHindi) "शुरू समय" else "Starts", fontSize = 11.sp, color = NaturalTextSecondary)
                                Text(
                                    text = "${child.bedtimeStartHour}:00 PM",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MossGreen600
                                )
                            }
                            Text(text = "➔", fontSize = 16.sp, color = NaturalTextTertiary)
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(if (isHindi) "समाप्त समय" else "Ends", fontSize = 11.sp, color = NaturalTextSecondary)
                                Text(
                                    text = "${child.bedtimeEndHour}:00 AM",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = NaturalGreen700
                                )
                            }
                        }
                    }
                }
            }
        }

        // 3. School Hours / Study Mode Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(1.dp, RoundedCornerShape(24.dp))
                    .border(1.dp, NaturalBorder, RoundedCornerShape(24.dp)),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = NaturalSurface)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(EarthAmber100),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.School, contentDescription = null, tint = EarthAmber600, modifier = Modifier.size(20.dp))
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = if (isHindi) "स्कूल / अध्ययन मोड" else "School & Study Mode",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = NaturalTextPrimary
                                )
                                Text(
                                    text = if (isHindi) "सिर्फ शैक्षणिक ऐप्स सक्रिय" else "Only educational apps allowed",
                                    fontSize = 11.sp,
                                    color = NaturalTextSecondary
                                )
                            }
                        }

                        Switch(
                            checked = child.schoolModeEnabled,
                            onCheckedChange = { onUpdateChild(child.copy(schoolModeEnabled = it)) },
                            colors = SwitchDefaults.colors(checkedThumbColor = EarthAmber600, checkedTrackColor = EarthAmber100)
                        )
                    }

                    if (child.schoolModeEnabled) {
                        Spacer(modifier = Modifier.height(14.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .background(NaturalSurfaceVariant)
                                .padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(if (isHindi) "स्कूल शुरू" else "School Starts", fontSize = 11.sp, color = NaturalTextSecondary)
                                Text("${child.schoolStartHour}:00 AM", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = EarthAmber600)
                            }
                            Text(text = "➔", fontSize = 16.sp, color = NaturalTextTertiary)
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(if (isHindi) "स्कूल खत्म" else "School Ends", fontSize = 11.sp, color = NaturalTextSecondary)
                                Text("${child.schoolEndHour}:00 PM", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = EarthAmber600)
                            }
                        }
                    }
                }
            }
        }

        // 4. Web & Content Safety Filters
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(1.dp, RoundedCornerShape(24.dp))
                    .border(1.dp, NaturalBorder, RoundedCornerShape(24.dp)),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = NaturalSurface)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(NaturalGreen100),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Shield, contentDescription = null, tint = NaturalGreen700, modifier = Modifier.size(20.dp))
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = if (isHindi) "वेब व सामग्री सुरक्षा" else "Web & Content Safety Filters",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = NaturalTextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // SafeSearch
                    FilterToggleRowNatural(
                        title = if (isHindi) "गूगल और यूट्यूब SafeSearch" else "Google & YouTube SafeSearch",
                        subtitle = if (isHindi) "असुरक्षित सर्च परिणाम स्वतः छिपाएं" else "Hides explicit/inappropriate content",
                        checked = child.webSafeSearch,
                        onCheckedChange = { onUpdateChild(child.copy(webSafeSearch = it)) }
                    )

                    // Adult Content Blocker
                    FilterToggleRowNatural(
                        title = if (isHindi) "वयस्क सामग्री ब्लॉक करें" else "Block Adult Content",
                        subtitle = if (isHindi) "18+ वेबसाइटों पर पूर्ण प्रतिबंध" else "Strict filter against mature websites",
                        checked = child.webBlockAdult,
                        onCheckedChange = { onUpdateChild(child.copy(webBlockAdult = it)) }
                    )

                    // Social Media Filter
                    FilterToggleRowNatural(
                        title = if (isHindi) "सोशल मीडिया वेब ब्लॉक" else "Block Web Social Media",
                        subtitle = if (isHindi) "ब्राउज़र में सोशल प्लेटफॉर्म प्रतिबंधित" else "Restricts TikTok, Reddit, Instagram web",
                        checked = child.webBlockSocial,
                        onCheckedChange = { onUpdateChild(child.copy(webBlockSocial = it)) }
                    )

                    // Online Gaming Web
                    FilterToggleRowNatural(
                        title = if (isHindi) "ऑनलाइन गेमिंग वेब ब्लॉक" else "Block Web Gaming Sites",
                        subtitle = if (isHindi) "ब्राउज़र में गेमिंग साइट्स ब्लॉक" else "Restricts browser game portals",
                        checked = child.webBlockGaming,
                        onCheckedChange = { onUpdateChild(child.copy(webBlockGaming = it)) }
                    )
                }
            }
        }

        // 5. Custom Blocked Websites Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isHindi) "कस्टम ब्लॉक वेबसाइट्स (${webRules.size})" else "Custom Blocked Websites (${webRules.size})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = NaturalTextPrimary
                )
                Button(
                    onClick = { showAddWebDialog = true },
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = NaturalGreen700)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(if (isHindi) "साइट जोड़ें" else "Add Site", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        if (webRules.isEmpty()) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, NaturalBorder, RoundedCornerShape(20.dp)),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = NaturalSurface)
                ) {
                    Box(modifier = Modifier.padding(24.dp), contentAlignment = Alignment.Center) {
                        Text(
                            text = if (isHindi) "कोई कस्टम ब्लॉक की गई वेबसाइट नहीं है।" else "No custom blocked websites added.",
                            color = NaturalTextSecondary,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        } else {
            items(webRules) { rule ->
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
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Language, contentDescription = null, tint = Terracotta700, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(rule.domain, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = NaturalTextPrimary)
                                Text(rule.category, fontSize = 11.sp, color = NaturalTextSecondary)
                            }
                        }
                        IconButton(onClick = { onDeleteWebRule(rule.id) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = NaturalTextTertiary, modifier = Modifier.size(20.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FilterToggleRowNatural(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.Medium, fontSize = 13.sp, color = NaturalTextPrimary)
            Text(subtitle, fontSize = 11.sp, color = NaturalTextSecondary)
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(checkedThumbColor = NaturalGreen700, checkedTrackColor = NaturalGreen100)
        )
    }
}
