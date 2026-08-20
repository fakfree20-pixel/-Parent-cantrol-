package com.example.ui.screens

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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAlert
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppUsageRule
import com.example.data.model.ChildProfile
import com.example.data.model.ScreenRewardTask
import com.example.ui.components.ChildAvatarCircle
import com.example.ui.components.PinKeypadDialog
import com.example.ui.components.formatMinutes
import com.example.ui.components.getCategoryDetails
import com.example.ui.theme.EarthAmber100
import com.example.ui.theme.EarthAmber500
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

@Composable
fun ChildModeScreen(
    child: ChildProfile,
    apps: List<AppUsageRule>,
    tasks: List<ScreenRewardTask>,
    isHindi: Boolean,
    onExitChildMode: () -> Unit,
    onRequestExtraTime: (minutes: Int) -> Unit,
    onCompleteTask: (ScreenRewardTask) -> Unit,
    onVerifyPin: (String) -> Boolean,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var showPinDialog by remember { mutableStateOf(false) }
    var requestSentSuccess by remember { mutableStateOf(false) }

    val usedMinutesToday = apps.sumOf { it.usageTodayMinutes }
    val totalAllowed = child.weekdayLimitMinutes + child.bonusMinutesToday
    val remainingMinutes = (totalAllowed - usedMinutesToday).coerceAtLeast(0)
    val isTimeOver = usedMinutesToday >= totalAllowed && totalAllowed > 0
    val isDeviceLocked = child.isLocked || isTimeOver

    if (showPinDialog) {
        PinKeypadDialog(
            title = if (isHindi) "माता-पिता का पिन दर्ज करें" else "Parent PIN Required",
            subtitle = if (isHindi) "चाइल्ड मोड से बाहर निकलने के लिए 4 अंकों का पिन दर्ज करें" else "Enter 4-digit PIN to unlock Parent Dashboard",
            onPinEntered = { pin ->
                val verified = onVerifyPin(pin)
                if (verified) {
                    showPinDialog = false
                    onExitChildMode()
                }
                verified
            },
            onDismiss = { showPinDialog = false },
            isHindi = isHindi
        )
    }

    // If device is locked or screen time is over, show fullscreen child lockout view with Natural Tones
    if (isDeviceLocked) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(listOf(Color(0xFF191D17), Color(0xFF2B3128)))
                )
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .clip(CircleShape)
                        .background(Terracotta100.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (child.isLocked) Icons.Default.Lock else Icons.Default.Bedtime,
                        contentDescription = null,
                        tint = Terracotta600,
                        modifier = Modifier.size(50.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = if (isHindi) {
                        if (child.isLocked) "स्क्रीन समय रोका गया है" else "आज का स्क्रीन समय समाप्त!"
                    } else {
                        if (child.isLocked) "Screen Time Paused" else "Daily Screen Time Limit Reached!"
                    },
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = if (child.isLocked) {
                        child.lockReason.ifEmpty {
                            if (isHindi) "माता-पिता ने इस डिवाइस को कुछ समय के लिए लॉक किया है।" else "Your parent has paused device screen time."
                        }
                    } else {
                        if (isHindi) "आपने आज का कुल समय (${formatMinutes(totalAllowed, true)}) पूरा कर लिया है।"
                        else "You have used up all ${formatMinutes(totalAllowed)} for today."
                    },
                    fontSize = 14.sp,
                    color = Color(0xFFC2C9BD),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(28.dp))

                // Request Extra Time Option
                if (!requestSentSuccess) {
                    Button(
                        onClick = {
                            onRequestExtraTime(15)
                            requestSentSuccess = true
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = NaturalGreen700),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Icon(Icons.Default.AddAlert, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(if (isHindi) "+15 मिनट के लिए अनुरोध भेजें" else "Ask Parents for +15 Minutes", fontWeight = FontWeight.Bold)
                    }
                } else {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = NaturalGreen100),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = NaturalGreen700)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isHindi) "अतिरिक्त समय का अनुरोध माता-पिता को भेज दिया गया है!" else "Request sent to parents for approval!",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = NaturalGreen900
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Parent Unlock Button
                OutlinedButton(
                    onClick = { showPinDialog = true },
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Icon(Icons.Default.Lock, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (isHindi) "पैरेंट अनलॉक पिन" else "Parent Unlock", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
        return
    }

    // Active Child Friendly View (Natural Tones)
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NaturalBg)
    ) {
        // Child Mode Top Banner in Natural Tones
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = NaturalGreen100)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        ChildAvatarCircle(
                            avatarIndex = child.avatarIndex,
                            name = child.name,
                            size = 44.dp
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = if (isHindi) "नमस्ते, ${child.name}! 👋" else "Hi, ${child.name}! 👋",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 18.sp,
                                color = NaturalGreen900
                            )
                            Text(
                                text = if (isHindi) "किड्स मोड सुरक्षित सक्रिय" else "Safe Kids Mode Active",
                                fontSize = 11.sp,
                                color = NaturalGreen700
                            )
                        }
                    }

                    // Parent Exit Pin Keypad button
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White.copy(alpha = 0.8f),
                        modifier = Modifier.clickable { showPinDialog = true }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Security, contentDescription = null, tint = NaturalGreen700, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(if (isHindi) "पैरेंट" else "Parent", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = NaturalGreen700)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Remaining Screen Time Display
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White.copy(alpha = 0.7f))
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(if (isHindi) "आज का शेष समय" else "Time Remaining", fontSize = 11.sp, color = NaturalTextSecondary)
                        Text(
                            text = formatMinutes(remainingMinutes, isHindi),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = NaturalGreen700
                        )
                    }

                    if (child.bonusMinutesToday > 0) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = NaturalGreen100
                        ) {
                            Text(
                                text = if (isHindi) "+${child.bonusMinutesToday}मि बोनस" else "+${child.bonusMinutesToday}m bonus",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = NaturalGreen700,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }

        // Tab Selector (Apps / Earn Time Rewards)
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = NaturalBg,
            contentColor = NaturalGreen700,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text(if (isHindi) "मेरी ऐप्स" else "My Apps", fontWeight = FontWeight.Bold) }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text(if (isHindi) "समय कमाएं 🏆" else "Earn Time 🏆", fontWeight = FontWeight.Bold) }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (selectedTab == 0) {
            // Apps Grid
            val allowedApps = apps.filter { !it.isBlocked }
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(allowedApps) { app ->
                    val (icon, color, _) = getCategoryDetails(app.category)
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(1.dp, RoundedCornerShape(20.dp))
                            .border(1.dp, NaturalBorder, RoundedCornerShape(20.dp)),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = NaturalSurface)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(NaturalSurfaceVariant),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(26.dp))
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = app.appName,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                maxLines = 1,
                                color = NaturalTextPrimary
                            )
                            Text(
                                text = "${app.usageTodayMinutes}m used",
                                fontSize = 10.sp,
                                color = NaturalTextSecondary
                            )
                        }
                    }
                }
            }
        } else {
            // Earn Time Tasks
            LazyVerticalGrid(
                columns = GridCells.Fixed(1),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(tasks) { task ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(1.dp, RoundedCornerShape(18.dp))
                            .border(1.dp, NaturalBorder, RoundedCornerShape(18.dp)),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (task.isApproved) NaturalGreen100.copy(alpha = 0.3f) else NaturalSurface
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                Icon(
                                    imageVector = when {
                                        task.isApproved -> Icons.Default.CheckCircle
                                        task.isCompleted -> Icons.Default.Timer
                                        else -> Icons.Default.RadioButtonUnchecked
                                    },
                                    contentDescription = null,
                                    tint = when {
                                        task.isApproved -> NaturalGreen700
                                        task.isCompleted -> EarthAmber600
                                        else -> NaturalTextTertiary
                                    }
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = if (isHindi && task.titleHindi.isNotEmpty()) task.titleHindi else task.title,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = NaturalTextPrimary
                                    )
                                    Text(
                                        text = "+${task.rewardMinutes} " + (if (isHindi) "मिनट इनाम" else "mins reward"),
                                        fontSize = 12.sp,
                                        color = NaturalGreen700,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }

                            if (!task.isCompleted) {
                                Button(
                                    onClick = { onCompleteTask(task) },
                                    colors = ButtonDefaults.buttonColors(containerColor = NaturalGreen700),
                                    shape = RoundedCornerShape(12.dp),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Text(if (isHindi) "पूर्ण करें" else "I Did It!", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            } else if (!task.isApproved) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = EarthAmber100
                                ) {
                                    Text(
                                        text = if (isHindi) "जांच बाकी" else "Pending Review",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = EarthAmber600,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
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
