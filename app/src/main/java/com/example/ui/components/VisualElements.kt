package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Games
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.AppUsageRule
import com.example.ui.theme.EarthAmber100
import com.example.ui.theme.EarthAmber500
import com.example.ui.theme.EarthAmber600
import com.example.ui.theme.MeadowSky600
import com.example.ui.theme.MossGreen100
import com.example.ui.theme.MossGreen500
import com.example.ui.theme.MossGreen600
import com.example.ui.theme.MossGreen700
import com.example.ui.theme.NaturalBorder
import com.example.ui.theme.NaturalCardBg
import com.example.ui.theme.NaturalGreen100
import com.example.ui.theme.NaturalGreen200
import com.example.ui.theme.NaturalGreen400
import com.example.ui.theme.NaturalGreen50
import com.example.ui.theme.NaturalGreen500
import com.example.ui.theme.NaturalGreen600
import com.example.ui.theme.NaturalGreen700
import com.example.ui.theme.NaturalGreen800
import com.example.ui.theme.NaturalGreen900
import com.example.ui.theme.NaturalSurface
import com.example.ui.theme.NaturalSurfaceVariant
import com.example.ui.theme.NaturalTextPrimary
import com.example.ui.theme.NaturalTextSecondary
import com.example.ui.theme.NaturalTextTertiary
import com.example.ui.theme.Terracotta100
import com.example.ui.theme.Terracotta500
import com.example.ui.theme.Terracotta600
import com.example.ui.theme.Terracotta700

// Helper to format minutes into "Xh Ym" or "X घंटे Y मिनट"
fun formatMinutes(totalMinutes: Int, isHindi: Boolean = false): String {
    val hours = totalMinutes / 60
    val mins = totalMinutes % 60
    return if (isHindi) {
        if (hours > 0 && mins > 0) "${hours}घं ${mins}मि"
        else if (hours > 0) "${hours} घंटे"
        else "${mins} मिनट"
    } else {
        if (hours > 0 && mins > 0) "${hours}h ${mins}m"
        else if (hours > 0) "${hours}h"
        else "${mins}m"
    }
}

// Avatar Colors Palette (Natural tones)
val avatarColors = listOf(
    NaturalGreen700,
    EarthAmber600,
    MossGreen600,
    Terracotta700,
    MeadowSky600,
    NaturalGreen800
)

@Composable
fun ChildAvatarCircle(
    avatarIndex: Int,
    name: String,
    size: Dp = 48.dp,
    isSelected: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    val initial = if (name.isNotEmpty()) name.first().uppercase() else "A"
    val isGirl = avatarIndex == 1 || name.contains("Ananya", ignoreCase = true) || name.contains("अनन्या", ignoreCase = true)
    val imageRes = if (isGirl) R.drawable.img_avatar_girl_1787118758802 else R.drawable.img_avatar_boy_1787118747682

    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .then(
                if (isSelected) Modifier.border(3.dp, NaturalGreen700, CircleShape)
                else Modifier.border(1.5.dp, NaturalBorder, CircleShape)
            )
            .background(NaturalGreen100)
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = name,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}

// Full-width Feature Hero Banner with Digital Photo Illustration
@Composable
fun FeatureHeroBanner(
    imageRes: Int,
    title: String,
    subtitle: String,
    badgeText: String,
    badgeColor: Color = NaturalGreen700,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(22.dp))
            .clip(RoundedCornerShape(22.dp)),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = NaturalSurface)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            // Dark gradient overlay for ultra-crisp readable text
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.85f),
                                Color.Black.copy(alpha = 0.55f),
                                Color.Transparent
                            )
                        )
                    )
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(14.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = badgeColor.copy(alpha = 0.9f)
                ) {
                    Text(
                        text = badgeText,
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = subtitle,
                    color = Color.White.copy(alpha = 0.88f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

// 2-Column Grid Feature Card with 3D Illustration Photo
@Composable
fun FeatureActionPhotoCard(
    imageRes: Int,
    title: String,
    subtitle: String,
    tagText: String,
    tagColor: Color,
    testTag: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(18.dp))
            .border(1.dp, NaturalBorder, RoundedCornerShape(18.dp))
            .clickable { onClick() }
            .testTag(testTag),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = NaturalSurface)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(84.dp)
            ) {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = tagColor,
                    modifier = Modifier
                        .padding(6.dp)
                        .align(Alignment.TopStart)
                ) {
                    Text(
                        text = tagText,
                        color = Color.White,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 8.dp)
            ) {
                Text(
                    text = title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = NaturalTextPrimary,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(1.dp))
                Text(
                    text = subtitle,
                    fontSize = 11.sp,
                    color = NaturalTextSecondary,
                    maxLines = 1
                )
            }
        }
    }
}

// Circular / Hero Screen Time Gauge in Natural Tones
@Composable
fun ScreenTimeGauge(
    usedMinutes: Int,
    limitMinutes: Int,
    bonusMinutes: Int,
    isLocked: Boolean,
    isHindi: Boolean,
    modifier: Modifier = Modifier
) {
    val totalAllowed = limitMinutes + bonusMinutes
    val progressFraction = if (totalAllowed > 0) (usedMinutes.toFloat() / totalAllowed.toFloat()).coerceIn(0f, 1f) else 0f
    val animatedProgress by animateFloatAsState(
        targetValue = progressFraction,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
        label = "progress"
    )

    val remainingMinutes = (totalAllowed - usedMinutes).coerceAtLeast(0)
    val isOverLimit = usedMinutes >= totalAllowed && totalAllowed > 0

    // Arc / Progress colors in Natural Tones
    val strokeGradient = when {
        isLocked -> Brush.sweepGradient(listOf(Terracotta700, Terracotta500, Terracotta700))
        isOverLimit -> Brush.sweepGradient(listOf(Terracotta700, Terracotta500))
        progressFraction > 0.8f -> Brush.sweepGradient(listOf(EarthAmber600, Terracotta600, EarthAmber600))
        else -> Brush.sweepGradient(listOf(NaturalGreen700, NaturalGreen500, NaturalGreen700))
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(28.dp))
            .border(1.dp, NaturalBorder, RoundedCornerShape(28.dp))
            .testTag("screen_time_gauge_card"),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = NaturalGreen100)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(22.dp)
        ) {
            // Header Row of the Card
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = if (isHindi) "आज का स्क्रीन टाइम" else "TODAY'S SCREEN TIME",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = NaturalGreen700,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = String.format("%02d:%02d", usedMinutes / 60, usedMinutes % 60),
                            fontSize = 32.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (isLocked || isOverLimit) Terracotta700 else NaturalGreen900
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "/ " + String.format("%02d:%02d", totalAllowed / 60, totalAllowed % 60),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = NaturalGreen700,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White.copy(alpha = 0.6f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isLocked) Icons.Default.Lock else Icons.Default.Timer,
                        contentDescription = null,
                        tint = if (isLocked) Terracotta700 else NaturalGreen700,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Gauge Arc in Center with Natural Tones
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.size(160.dp)) {
                    val strokeWidth = 14.dp.toPx()
                    val arcSize = size.width - strokeWidth
                    val topLeft = Offset(strokeWidth / 2, strokeWidth / 2)

                    // Track Arc (Natural Tones white/translucent)
                    drawArc(
                        color = Color.White.copy(alpha = 0.5f),
                        startAngle = 140f,
                        sweepAngle = 260f,
                        useCenter = false,
                        topLeft = topLeft,
                        size = Size(arcSize, arcSize),
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )

                    // Active Progress Arc
                    val sweep = 260f * animatedProgress
                    if (sweep > 0) {
                        drawArc(
                            brush = strokeGradient,
                            startAngle = 140f,
                            sweepAngle = sweep,
                            useCenter = false,
                            topLeft = topLeft,
                            size = Size(arcSize, arcSize),
                            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                        )
                    }
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "${(animatedProgress * 100).toInt()}%",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = NaturalGreen900
                    )
                    Text(
                        text = if (isHindi) "उपयोग" else "used",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = NaturalGreen700
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Horizontal Progress Bar indicator
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .background(Color.White.copy(alpha = 0.5f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(fraction = animatedProgress)
                        .height(10.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .background(if (isLocked || isOverLimit) Terracotta700 else NaturalGreen700)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Status message
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = when {
                        isLocked -> if (isHindi) "डिवाइस तुरंत लॉक है" else "Device paused by parent"
                        isOverLimit -> if (isHindi) "आज का समय समाप्त हो गया है!" else "Daily screen limit exceeded!"
                        else -> if (isHindi) "${formatMinutes(remainingMinutes, true)} शेष हैं" else "${formatMinutes(remainingMinutes)} remaining today"
                    },
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isLocked || isOverLimit) Terracotta700 else NaturalGreen700
                )

                if (bonusMinutes > 0) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color.White.copy(alpha = 0.7f)
                    ) {
                        Text(
                            text = if (isHindi) "+${bonusMinutes}मि बोनस" else "+${bonusMinutes}m bonus",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = NaturalGreen700,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        }
    }
}

// Category Icon & Color Helper in Natural Tones
fun getCategoryDetails(category: String): Triple<ImageVector, Color, String> {
    return when (category.uppercase()) {
        "GAMES" -> Triple(Icons.Default.Games, Terracotta600, "Games")
        "ENTERTAINMENT" -> Triple(Icons.Default.Movie, EarthAmber600, "Entertainment")
        "EDUCATION" -> Triple(Icons.Default.School, NaturalGreen700, "Education")
        "SOCIAL" -> Triple(Icons.Default.Share, MossGreen600, "Social")
        "PRODUCTIVITY" -> Triple(Icons.Default.Widgets, MeadowSky600, "Browsing")
        "UTILITY" -> Triple(Icons.Default.Phone, NaturalGreen600, "Utility")
        else -> Triple(Icons.Default.Widgets, MossGreen500, "Other")
    }
}

// Category Breakdown Card in Natural Tones
@Composable
fun CategoryBreakdownCard(
    apps: List<AppUsageRule>,
    isHindi: Boolean,
    modifier: Modifier = Modifier
) {
    val categoryTotals = apps.groupBy { it.category }.mapValues { entry ->
        entry.value.sumOf { it.usageTodayMinutes }
    }.filter { it.value > 0 }

    val totalUsage = categoryTotals.values.sum().coerceAtLeast(1)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(1.dp, RoundedCornerShape(24.dp))
            .border(1.dp, NaturalBorder, RoundedCornerShape(24.dp)),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = NaturalSurface)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = if (isHindi) "श्रेणी अनुसार स्क्रीन समय" else "Category Breakdown",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = NaturalTextPrimary
            )
            Spacer(modifier = Modifier.height(14.dp))

            if (categoryTotals.isEmpty()) {
                Text(
                    text = if (isHindi) "आज कोई स्क्रीन उपयोग दर्ज नहीं हुआ है।" else "No usage recorded yet today.",
                    fontSize = 13.sp,
                    color = NaturalTextSecondary
                )
            } else {
                categoryTotals.entries.sortedByDescending { it.value }.forEach { (category, minutes) ->
                    val (icon, color, catName) = getCategoryDetails(category)
                    val percentage = ((minutes.toFloat() / totalUsage.toFloat()) * 100).toInt()

                    Column(modifier = Modifier.padding(vertical = 6.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(NaturalSurfaceVariant),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = null,
                                        tint = color,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = if (isHindi) {
                                        when (category) {
                                            "GAMES" -> "गेम्स"
                                            "ENTERTAINMENT" -> "मनोरंजन / वीडियो"
                                            "EDUCATION" -> "शिक्षा और पढ़ाई"
                                            "SOCIAL" -> "सोशल मीडिया"
                                            "PRODUCTIVITY" -> "ब्राउज़िंग व उत्पादकता"
                                            else -> catName
                                        }
                                    } else catName,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = NaturalTextPrimary
                                )
                            }
                            Text(
                                text = "${formatMinutes(minutes, isHindi)} ($percentage%)",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = color
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(NaturalSurfaceVariant)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(fraction = (minutes.toFloat() / totalUsage.toFloat()).coerceIn(0f, 1f))
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(color)
                            )
                        }
                    }
                }
            }
        }
    }
}

// Weekly Usage Chart in Natural Tones
@Composable
fun WeeklyUsageChartCard(
    isHindi: Boolean,
    modifier: Modifier = Modifier
) {
    val weekDays = if (isHindi) {
        listOf("सोम", "मंगल", "बुध", "गुरु", "शुक्र", "शनि", "रवि")
    } else {
        listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    }

    val dayMinutes = listOf(95, 120, 80, 110, 135, 160, 105)
    val maxMinutes = dayMinutes.maxOrNull()?.toFloat() ?: 180f

    Card(
        modifier = modifier
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
                Text(
                    text = if (isHindi) "साप्ताहिक उपयोग ट्रेंड" else "Weekly Usage Trend",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = NaturalTextPrimary
                )
                Text(
                    text = if (isHindi) "औसत: 1घं 52मि / दिन" else "Avg: 1h 52m / day",
                    fontSize = 11.sp,
                    color = NaturalTextSecondary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                weekDays.forEachIndexed { index, day ->
                    val mins = dayMinutes[index]
                    val fraction = (mins.toFloat() / maxMinutes).coerceIn(0.15f, 1f)
                    val isToday = index == 6

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "${mins}m",
                            fontSize = 9.sp,
                            fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal,
                            color = if (isToday) NaturalGreen700 else NaturalTextTertiary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(
                            modifier = Modifier
                                .width(18.dp)
                                .height((90 * fraction).dp)
                                .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                                .background(
                                    if (isToday) NaturalGreen700
                                    else NaturalGreen100
                                )
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = day,
                            fontSize = 10.sp,
                            fontWeight = if (isToday) FontWeight.Bold else FontWeight.Medium,
                            color = if (isToday) NaturalGreen700 else NaturalTextSecondary
                        )
                    }
                }
            }
        }
    }
}
