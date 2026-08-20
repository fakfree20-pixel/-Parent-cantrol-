package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

// 1. PIN Keypad Verification Dialog
@Composable
fun PinKeypadDialog(
    title: String,
    subtitle: String,
    onPinEntered: (String) -> Boolean,
    onDismiss: () -> Unit,
    isHindi: Boolean
) {
    var enteredPin by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Icon(
                    imageVector = Icons.Default.Security,
                    contentDescription = null,
                    tint = NaturalGreen700,
                    modifier = Modifier.size(36.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = NaturalTextPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = NaturalTextSecondary,
                    textAlign = TextAlign.Center
                )
            }
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // PIN Dots Display
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.padding(vertical = 16.dp)
                ) {
                    repeat(4) { index ->
                        val isFilled = index < enteredPin.length
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .clip(CircleShape)
                                .background(
                                    if (isError) Terracotta700
                                    else if (isFilled) NaturalGreen700
                                    else NaturalBorder
                                )
                        )
                    }
                }

                if (isError) {
                    Text(
                        text = if (isHindi) "गलत पिन! पुनः प्रयास करें" else "Incorrect PIN! Try again",
                        color = Terracotta700,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // 3x4 Keypad Grid in Natural Tones
                val keys = listOf(
                    listOf("1", "2", "3"),
                    listOf("4", "5", "6"),
                    listOf("7", "8", "9"),
                    listOf("C", "0", "DEL")
                )

                keys.forEach { row ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        row.forEach { key ->
                            Surface(
                                shape = CircleShape,
                                color = if (key.isNotEmpty()) NaturalSurfaceVariant else Color.Transparent,
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(CircleShape)
                                    .clickable {
                                        when (key) {
                                            "C" -> {
                                                enteredPin = ""
                                                isError = false
                                            }
                                            "DEL" -> {
                                                if (enteredPin.isNotEmpty()) {
                                                    enteredPin = enteredPin.dropLast(1)
                                                    isError = false
                                                }
                                            }
                                            else -> {
                                                if (enteredPin.length < 4) {
                                                    val newPin = enteredPin + key
                                                    enteredPin = newPin
                                                    if (newPin.length == 4) {
                                                        val valid = onPinEntered(newPin)
                                                        if (!valid) {
                                                            isError = true
                                                            enteredPin = ""
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    if (key == "DEL") {
                                        Icon(
                                            Icons.Default.Backspace,
                                            contentDescription = "Delete",
                                            tint = NaturalTextPrimary,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    } else {
                                        Text(
                                            text = key,
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = NaturalTextPrimary
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(if (isHindi) "रद्द करें" else "Cancel", color = NaturalTextSecondary)
            }
        }
    )
}

// 2. Instant Lock Dialog
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun InstantLockDialog(
    childName: String,
    onConfirmLock: (reason: String, durationMins: Int) -> Unit,
    onDismiss: () -> Unit,
    isHindi: Boolean
) {
    var selectedDuration by remember { mutableIntStateOf(30) }
    var selectedReason by remember { mutableStateOf("Dinner Time") }

    val presetDurations = listOf(
        Pair(15, if (isHindi) "15 मिनट" else "15 mins"),
        Pair(30, if (isHindi) "30 मिनट" else "30 mins"),
        Pair(60, if (isHindi) "1 घंटा" else "1 hour"),
        Pair(120, if (isHindi) "2 घंटे" else "2 hours"),
        Pair(0, if (isHindi) "जब तक मैं न खोलूं" else "Until I unlock")
    )

    val presetReasons = if (isHindi) {
        listOf("भोजन का समय", "पढ़ाई का समय", "सोने का समय", "परिवार का समय", "स्क्रीन अनुशासन")
    } else {
        listOf("Dinner Time", "Study Hour", "Bedtime", "Family Time", "Screen Time Out")
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Lock, contentDescription = null, tint = Terracotta700, modifier = Modifier.size(28.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isHindi) "$childName की डिवाइस फ्रीज़ / लॉक करें" else "Lock $childName's Device",
                    fontWeight = FontWeight.Bold,
                    color = NaturalTextPrimary
                )
            }
        },
        text = {
            Column {
                Text(
                    text = if (isHindi) "लॉक की अवधि चुनें:" else "Select Lock Duration:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = NaturalTextPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    presetDurations.forEach { (mins, label) ->
                        FilterChip(
                            selected = selectedDuration == mins,
                            onClick = { selectedDuration = mins },
                            label = { Text(label, fontSize = 12.sp) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = if (isHindi) "कारण (बच्चे की स्क्रीन पर दिखेगा):" else "Reason (Shown to Child):",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = NaturalTextPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    presetReasons.forEach { reason ->
                        FilterChip(
                            selected = selectedReason == reason,
                            onClick = { selectedReason = reason },
                            label = { Text(reason, fontSize = 11.sp) }
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirmLock(selectedReason, selectedDuration) },
                colors = ButtonDefaults.buttonColors(containerColor = Terracotta700),
                modifier = Modifier.testTag("confirm_lock_button")
            ) {
                Text(if (isHindi) "अभी लॉक करें" else "Lock Now")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(if (isHindi) "रद्द करें" else "Cancel", color = NaturalTextSecondary)
            }
        }
    )
}

// 3. Add Bonus Time Dialog
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddBonusTimeDialog(
    childName: String,
    onConfirmBonus: (minutes: Int, reason: String) -> Unit,
    onDismiss: () -> Unit,
    isHindi: Boolean
) {
    var selectedMinutes by remember { mutableIntStateOf(15) }
    var selectedReason by remember { mutableStateOf("Good Behavior") }

    val presetMinutes = listOf(
        Pair(15, "+15 min"),
        Pair(30, "+30 min"),
        Pair(45, "+45 min"),
        Pair(60, "+1 hour")
    )

    val presetReasons = if (isHindi) {
        listOf("अच्छा व्यवहार", "गृहकार्य पूरा किया", "कमरा साफ किया", "सप्ताहांत उपहार")
    } else {
        listOf("Good Behavior", "Homework Done", "Cleaned Room", "Weekend Treat")
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Timer, contentDescription = null, tint = NaturalGreen700, modifier = Modifier.size(28.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isHindi) "$childName को बोनस स्क्रीन टाइम दें" else "Grant Bonus Time to $childName",
                    fontWeight = FontWeight.Bold,
                    color = NaturalTextPrimary
                )
            }
        },
        text = {
            Column {
                Text(
                    text = if (isHindi) "बोनस समय चुनें:" else "Select Bonus Time:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = NaturalTextPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    presetMinutes.forEach { (mins, label) ->
                        FilterChip(
                            selected = selectedMinutes == mins,
                            onClick = { selectedMinutes = mins },
                            label = { Text(label, fontSize = 12.sp) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = if (isHindi) "कारण चुनें:" else "Reason for Reward:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = NaturalTextPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    presetReasons.forEach { reason ->
                        FilterChip(
                            selected = selectedReason == reason,
                            onClick = { selectedReason = reason },
                            label = { Text(reason, fontSize = 11.sp) }
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirmBonus(selectedMinutes, selectedReason) },
                colors = ButtonDefaults.buttonColors(containerColor = NaturalGreen700),
                modifier = Modifier.testTag("confirm_bonus_button")
            ) {
                Text(if (isHindi) "समय जोड़ें" else "Add Minutes")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(if (isHindi) "रद्द करें" else "Cancel", color = NaturalTextSecondary)
            }
        }
    )
}

// 4. Add Child Profile Dialog
@Composable
fun AddChildDialog(
    onAddChild: (name: String, age: Int, avatarIndex: Int, dailyLimit: Int) -> Unit,
    onDismiss: () -> Unit,
    isHindi: Boolean
) {
    var name by remember { mutableStateOf("") }
    var ageText by remember { mutableStateOf("9") }
    var avatarIndex by remember { mutableIntStateOf(0) }
    var dailyLimitMins by remember { mutableFloatStateOf(120f) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.PersonAdd, contentDescription = null, tint = NaturalGreen700, modifier = Modifier.size(28.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isHindi) "नया बच्चा प्रोफाइल जोड़ें" else "Add New Child Profile",
                    fontWeight = FontWeight.Bold,
                    color = NaturalTextPrimary
                )
            }
        },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(if (isHindi) "बच्चे का नाम" else "Child Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = ageText,
                    onValueChange = { ageText = it },
                    label = { Text(if (isHindi) "उम्र (वर्ष)" else "Age (Years)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = if (isHindi) "दैनिक स्क्रीन टाइम सीमा: ${dailyLimitMins.toInt()} मिनट" else "Daily Limit: ${dailyLimitMins.toInt()} mins",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = NaturalGreen700
                )
                Slider(
                    value = dailyLimitMins,
                    onValueChange = { dailyLimitMins = it },
                    valueRange = 30f..360f,
                    steps = 10,
                    colors = SliderDefaults.colors(thumbColor = NaturalGreen700, activeTrackColor = NaturalGreen700)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = if (isHindi) "अवतार रंग चुनें:" else "Select Avatar Color:",
                    fontSize = 12.sp,
                    color = NaturalTextSecondary
                )
                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    repeat(6) { index ->
                        ChildAvatarCircle(
                            avatarIndex = index,
                            name = if (name.isNotEmpty()) name else "K",
                            size = 36.dp,
                            isSelected = avatarIndex == index,
                            onClick = { avatarIndex = index }
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        val age = ageText.toIntOrNull() ?: 8
                        onAddChild(name.trim(), age, avatarIndex, dailyLimitMins.toInt())
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = NaturalGreen700)
            ) {
                Text(if (isHindi) "जोड़ें" else "Add Profile")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(if (isHindi) "रद्द करें" else "Cancel", color = NaturalTextSecondary)
            }
        }
    )
}

// 5. Set App Limit Dialog
@Composable
fun SetAppLimitDialog(
    appName: String,
    currentLimitMinutes: Int,
    onSaveLimit: (limitMinutes: Int) -> Unit,
    onDismiss: () -> Unit,
    isHindi: Boolean
) {
    var limitMins by remember {
        mutableFloatStateOf(if (currentLimitMinutes > 0) currentLimitMinutes.toFloat() else 30f)
    }
    var isNoLimit by remember { mutableStateOf(currentLimitMinutes == 0) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (isHindi) "$appName के लिए समय सीमा" else "App Limit for $appName",
                fontWeight = FontWeight.Bold,
                color = NaturalTextPrimary
            )
        },
        text = {
            Column {
                if (isNoLimit) {
                    Text(
                        text = if (isHindi) "कोई दैनिक सीमा नहीं (असीमित)" else "No specific limit (Unlimited)",
                        color = NaturalTextSecondary,
                        fontSize = 14.sp
                    )
                } else {
                    Text(
                        text = if (isHindi) "दैनिक सीमा: ${limitMins.toInt()} मिनट" else "Daily Limit: ${limitMins.toInt()} minutes",
                        fontWeight = FontWeight.Bold,
                        color = NaturalGreen700,
                        fontSize = 16.sp
                    )
                    Slider(
                        value = limitMins,
                        onValueChange = { limitMins = it },
                        valueRange = 5f..180f,
                        steps = 34,
                        colors = SliderDefaults.colors(thumbColor = NaturalGreen700, activeTrackColor = NaturalGreen700)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { isNoLimit = !isNoLimit }
                        .padding(vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (isNoLimit) Icons.Default.Check else Icons.Default.Add,
                        contentDescription = null,
                        tint = if (isNoLimit) NaturalGreen700 else NaturalTextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isHindi) "सीमा हटाएं (असीमित उपयोग)" else "Remove limit (Unlimited usage)",
                        fontSize = 13.sp,
                        color = NaturalTextPrimary
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSaveLimit(if (isNoLimit) 0 else limitMins.toInt())
                },
                colors = ButtonDefaults.buttonColors(containerColor = NaturalGreen700)
            ) {
                Text(if (isHindi) "सेव करें" else "Save Limit")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(if (isHindi) "रद्द करें" else "Cancel", color = NaturalTextSecondary)
            }
        }
    )
}

// 6. Add Reward Task Dialog
@Composable
fun AddTaskDialog(
    onAddTask: (title: String, titleHindi: String, rewardMinutes: Int) -> Unit,
    onDismiss: () -> Unit,
    isHindi: Boolean
) {
    var title by remember { mutableStateOf("") }
    var rewardMins by remember { mutableIntStateOf(15) }

    val presetRewards = listOf(10, 15, 20, 30, 45)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (isHindi) "नया इनाम कार्य जोड़ें" else "Create Reward Task",
                fontWeight = FontWeight.Bold,
                color = NaturalTextPrimary
            )
        },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text(if (isHindi) "कार्य का नाम (e.g. गणित का होमवर्क)" else "Task Name (e.g. Math Homework)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = if (isHindi) "बोनस समय इनाम चुनें:" else "Reward Screen Time:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = NaturalTextPrimary
                )
                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    presetRewards.forEach { mins ->
                        FilterChip(
                            selected = rewardMins == mins,
                            onClick = { rewardMins = mins },
                            label = { Text("+${mins}m", fontSize = 11.sp) }
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        onAddTask(title.trim(), title.trim(), rewardMins)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = NaturalGreen700)
            ) {
                Text(if (isHindi) "कार्य जोड़ें" else "Create Task")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(if (isHindi) "रद्द करें" else "Cancel", color = NaturalTextSecondary)
            }
        }
    )
}

// Subscription & VIP Upgrade Dialog (FlashGet Kids Style)
@Composable
fun SubscriptionUpgradeDialog(
    isHindi: Boolean,
    onDismiss: () -> Unit
) {
    var selectedPlan by remember { mutableIntStateOf(1) } // 0 = Monthly, 1 = Yearly (Best Value), 2 = Lifetime
    var isSubscribed by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = null,
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header with VIP Badge
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(
                            androidx.compose.ui.graphics.Brush.linearGradient(
                                listOf(Color(0xFFFFD700), Color(0xFFFFA500))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text("⭐", fontSize = 28.sp)
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = if (isHindi) "प्रीमियम वीआईपी अनलॉक करें" else "Unlock VIP Full Access",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp,
                    color = Color(0xFF1E1E2E),
                    textAlign = TextAlign.Center
                )

                Text(
                    text = if (isHindi) "रिमोट कैमरा, स्क्रीन मिररिंग और वन-वे ऑडियो असीमित उपयोग करें" else "Unlimited Remote Camera, Screen Mirroring & One-Way Audio",
                    fontSize = 12.sp,
                    color = Color(0xFF6C7086),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Plan Selector Cards
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Plan 1: 1 Month
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .border(
                                width = if (selectedPlan == 0) 2.dp else 1.dp,
                                color = if (selectedPlan == 0) Color(0xFF6C5CE7) else Color(0xFFE2E8F0),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable { selectedPlan = 0 },
                        colors = CardDefaults.cardColors(
                            containerColor = if (selectedPlan == 0) Color(0xFFF3F0FF) else Color.White
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(if (isHindi) "1 महीना" else "1 Month", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E1E2E))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("₹399", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF6C5CE7))
                            Text(if (isHindi) "प्रति माह" else "/mo", fontSize = 9.sp, color = Color.Gray)
                        }
                    }

                    // Plan 2: 1 Year (Best Value)
                    Card(
                        modifier = Modifier
                            .weight(1.1f)
                            .clip(RoundedCornerShape(12.dp))
                            .border(
                                width = if (selectedPlan == 1) 2.dp else 1.dp,
                                color = if (selectedPlan == 1) Color(0xFF6C5CE7) else Color(0xFFE2E8F0),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable { selectedPlan = 1 },
                        colors = CardDefaults.cardColors(
                            containerColor = if (selectedPlan == 1) Color(0xFFF3F0FF) else Color.White
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(Color(0xFFFF4757))
                                    .padding(horizontal = 4.dp, vertical = 1.dp)
                            ) {
                                Text(if (isHindi) "लोकप्रिय" else "BEST VALUE", fontSize = 7.sp, color = Color.White, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(if (isHindi) "1 वर्ष" else "1 Year", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E1E2E))
                            Text("₹1,999", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF6C5CE7))
                            Text(if (isHindi) "₹166/माह" else "₹166/mo", fontSize = 9.sp, color = Color(0xFF2ED573), fontWeight = FontWeight.SemiBold)
                        }
                    }

                    // Plan 3: Lifetime
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .border(
                                width = if (selectedPlan == 2) 2.dp else 1.dp,
                                color = if (selectedPlan == 2) Color(0xFF6C5CE7) else Color(0xFFE2E8F0),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable { selectedPlan = 2 },
                        colors = CardDefaults.cardColors(
                            containerColor = if (selectedPlan == 2) Color(0xFFF3F0FF) else Color.White
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(if (isHindi) "लाइफटाइम" else "Lifetime", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E1E2E))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("₹3,499", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF6C5CE7))
                            Text(if (isHindi) "एक बार" else "One-time", fontSize = 9.sp, color = Color.Gray)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Feature Highlights
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFF8F9FA))
                        .padding(10.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val features = if (isHindi) listOf(
                        "✔ लाइव रिमोट फ्रंट व बैक कैमरा मॉनिटरिंग",
                        "✔ एचडी स्क्रीन मिररिंग और रियल-टाइम व्यू",
                        "✔ वन-वे और टू-वे ऑडियो लिसनिंग",
                        "✔ जियोफेंस अलर्ट्स और अनइंस्टॉल प्रोटेक्शन"
                    ) else listOf(
                        "✔ Live Remote Front & Back Camera Access",
                        "✔ HD Screen Mirroring & Real-Time Sync",
                        "✔ One-Way & Two-Way High-Clear Audio",
                        "✔ Unlimited Geofence & Anti-Uninstall Shield"
                    )
                    features.forEach { feat ->
                        Text(feat, fontSize = 11.sp, color = Color(0xFF2D3436), fontWeight = FontWeight.Medium)
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    isSubscribed = true
                    onDismiss()
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C5CE7))
            ) {
                Text(
                    text = if (isHindi) "अभी सब्सक्राइब करें (तुरंत चालू)" else "Subscribe Now & Unlock",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (isHindi) "बाद में देखें" else "Maybe Later", color = Color(0xFF6C7086), fontSize = 12.sp)
            }
        }
    )
}

