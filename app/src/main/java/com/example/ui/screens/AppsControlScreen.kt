package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
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
import com.example.data.model.AppUsageRule
import com.example.ui.components.SetAppLimitDialog
import com.example.ui.components.formatMinutes
import com.example.ui.components.getCategoryDetails
import com.example.ui.theme.EarthAmber100
import com.example.ui.theme.EarthAmber500
import com.example.ui.theme.EarthAmber600
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
fun AppsControlScreen(
    apps: List<AppUsageRule>,
    isHindi: Boolean,
    onToggleBlock: (AppUsageRule) -> Unit,
    onSetAppLimit: (ruleId: Long, limitMinutes: Int) -> Unit,
    onToggleAlwaysAllowed: (ruleId: Long, isAlwaysAllowed: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("ALL") }
    var appForLimitDialog by remember { mutableStateOf<AppUsageRule?>(null) }

    if (appForLimitDialog != null) {
        SetAppLimitDialog(
            appName = appForLimitDialog!!.appName,
            currentLimitMinutes = appForLimitDialog!!.dailyLimitMinutes,
            onSaveLimit = { limit ->
                onSetAppLimit(appForLimitDialog!!.id, limit)
                appForLimitDialog = null
            },
            onDismiss = { appForLimitDialog = null },
            isHindi = isHindi
        )
    }

    val categories = listOf(
        Pair("ALL", if (isHindi) "सभी ऐप्स" else "All Apps"),
        Pair("GAMES", if (isHindi) "गेम्स" else "Games"),
        Pair("ENTERTAINMENT", if (isHindi) "मनोरंजन" else "Entertainment"),
        Pair("SOCIAL", if (isHindi) "सोशल" else "Social"),
        Pair("EDUCATION", if (isHindi) "शिक्षा" else "Education"),
        Pair("ALLOWED", if (isHindi) "हमेशा अनुमति" else "Always Allowed"),
        Pair("BLOCKED", if (isHindi) "ब्लॉक किए गए" else "Blocked")
    )

    val filteredApps = apps.filter { app ->
        val matchesSearch = app.appName.contains(searchQuery, ignoreCase = true) ||
                app.packageName.contains(searchQuery, ignoreCase = true)
        val matchesCategory = when (selectedCategory) {
            "ALL" -> true
            "ALLOWED" -> app.isAlwaysAllowed
            "BLOCKED" -> app.isBlocked
            else -> app.category.equals(selectedCategory, ignoreCase = true)
        }
        matchesSearch && matchesCategory
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(NaturalBg)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 28.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Search Bar in Natural Tones
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text(if (isHindi) "ऐप खोजें..." else "Search apps by name...", color = NaturalTextTertiary) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = NaturalTextSecondary) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear", tint = NaturalTextSecondary)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("app_search_field"),
                shape = RoundedCornerShape(20.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NaturalGreen700,
                    unfocusedBorderColor = NaturalBorder,
                    focusedContainerColor = NaturalSurface,
                    unfocusedContainerColor = NaturalSurface
                )
            )
        }

        // Category Filter Chips Row
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categories.forEach { (catKey, catLabel) ->
                    val isSelected = selectedCategory == catKey
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedCategory = catKey },
                        label = { Text(catLabel, fontSize = 12.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = NaturalGreen100,
                            selectedLabelColor = NaturalGreen700,
                            containerColor = NaturalSurface,
                            labelColor = NaturalTextSecondary
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isSelected,
                            borderColor = if (isSelected) NaturalGreen700 else NaturalBorder
                        ),
                        shape = RoundedCornerShape(14.dp)
                    )
                }
            }
        }

        // Apps Count Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isHindi) "टॉप ऐप्स उपयोग (${filteredApps.size})" else "App Usage Rules (${filteredApps.size})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = NaturalTextPrimary
                )
                Text(
                    text = if (isHindi) "टैप करके समय सीमा तय करें" else "Tap to adjust daily limits",
                    fontSize = 11.sp,
                    color = NaturalTextSecondary
                )
            }
        }

        // Empty state
        if (filteredApps.isEmpty()) {
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
                        Icon(Icons.Default.Widgets, contentDescription = null, tint = NaturalTextTertiary, modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = if (isHindi) "कोई ऐप नहीं मिला" else "No matching apps found",
                            fontWeight = FontWeight.Bold,
                            color = NaturalTextPrimary
                        )
                    }
                }
            }
        } else {
            items(filteredApps, key = { it.id }) { app ->
                AppControlItemCard(
                    app = app,
                    isHindi = isHindi,
                    onToggleBlock = { onToggleBlock(app) },
                    onSetLimitClick = { appForLimitDialog = app },
                    onToggleAlwaysAllowed = { onToggleAlwaysAllowed(app.id, !app.isAlwaysAllowed) }
                )
            }
        }
    }
}

@Composable
fun AppControlItemCard(
    app: AppUsageRule,
    isHindi: Boolean,
    onToggleBlock: () -> Unit,
    onSetLimitClick: () -> Unit,
    onToggleAlwaysAllowed: () -> Unit,
    modifier: Modifier = Modifier
) {
    val (icon, color, catName) = getCategoryDetails(app.category)
    val hasLimit = app.dailyLimitMinutes > 0
    val isLimitExceeded = hasLimit && app.usageTodayMinutes >= app.dailyLimitMinutes

    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(1.dp, RoundedCornerShape(20.dp))
            .border(1.dp, NaturalBorder, RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (app.isBlocked) NaturalSurface.copy(alpha = 0.6f) else NaturalSurface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Left: App Icon & Name
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(if (app.isBlocked) Terracotta100 else NaturalSurfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (app.isBlocked) Icons.Default.Block else icon,
                            contentDescription = null,
                            tint = if (app.isBlocked) Terracotta700 else color,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = app.appName,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = if (app.isBlocked) NaturalTextTertiary else NaturalTextPrimary
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = catName,
                                fontSize = 11.sp,
                                color = NaturalTextSecondary
                            )
                            if (app.isAlwaysAllowed) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    color = NaturalGreen100,
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = if (isHindi) "हमेशा चालू" else "Always Allowed",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = NaturalGreen700,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // Right: Block Switch & Star button
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onToggleAlwaysAllowed,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = if (app.isAlwaysAllowed) Icons.Default.Star else Icons.Default.StarBorder,
                            contentDescription = "Always Allowed",
                            tint = if (app.isAlwaysAllowed) EarthAmber600 else NaturalTextTertiary,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    // Natural Tones Switch: Active = NaturalGreen100 track with NaturalGreen700 thumb
                    Switch(
                        checked = !app.isBlocked,
                        onCheckedChange = { onToggleBlock() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = NaturalGreen700,
                            checkedTrackColor = NaturalGreen100,
                            uncheckedThumbColor = NaturalTextTertiary,
                            uncheckedTrackColor = NaturalBorder
                        ),
                        modifier = Modifier.testTag("block_switch_${app.appName.replace(" ", "_")}")
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Usage & Limit Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(NaturalSurfaceVariant)
                    .clickable { onSetLimitClick() }
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Timer,
                        contentDescription = null,
                        tint = if (isLimitExceeded) Terracotta700 else NaturalGreen700,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isHindi) "आज का उपयोग: ${formatMinutes(app.usageTodayMinutes, true)}" else "Today: ${formatMinutes(app.usageTodayMinutes)}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = NaturalTextPrimary
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = if (hasLimit) {
                            if (isHindi) "सीमा: ${app.dailyLimitMinutes}मि" else "Limit: ${app.dailyLimitMinutes}m"
                        } else {
                            if (isHindi) "सीमा तय करें" else "Set Limit"
                        },
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (hasLimit) EarthAmber600 else NaturalGreen700
                    )
                }
            }

            // Progress bar if limit exists
            if (hasLimit) {
                Spacer(modifier = Modifier.height(6.dp))
                val limitFraction = (app.usageTodayMinutes.toFloat() / app.dailyLimitMinutes.toFloat()).coerceIn(0f, 1f)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(NaturalBorder)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(fraction = limitFraction)
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(if (isLimitExceeded) Terracotta700 else EarthAmber500)
                    )
                }
            }
        }
    }
}
