package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.theme.*

/**
 * 4-Digit Security PIN & Administrative Lock Manager for Parents
 * (मास्टर सुरक्षा पिन व एडमिनिस्ट्रेटिव लॉक सेटिंग्स)
 */
@Composable
fun SecurityPinManagementDialog(
    masterPin: String,
    isHindi: Boolean,
    onChangePin: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var newPin by remember { mutableStateOf("") }
    var confirmPin by remember { mutableStateOf("") }
    var isCurrentPinRevealed by remember { mutableStateOf(false) }
    var isNewPinRevealed by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Set/Change PIN, 1: Test PIN, 2: Protected Features

    val focusManager = LocalFocusManager.current

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.90f)
                .shadow(12.dp, RoundedCornerShape(24.dp))
                .border(1.dp, Color(0xFFECEFF8), RoundedCornerShape(24.dp)),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
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
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.linearGradient(
                                        listOf(Color(0xFF6C5CE7), Color(0xFF5B48D9))
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Key,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = if (isHindi) "4-अंकीय सुरक्षा पिन प्रबंधन" else "4-Digit Security PIN",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp,
                                    color = Color(0xFF1E1E2E)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(Color(0xFF00B894).copy(alpha = 0.15f))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text("PARENT ONLY", color = Color(0xFF00B894), fontWeight = FontWeight.Bold, fontSize = 9.sp)
                                }
                            }
                            Text(
                                text = if (isHindi) "एडमिन सेटिंग्स व चाइल्ड लॉक कोड" else "Administrative Lock & Kid Mode Security",
                                fontSize = 11.sp,
                                color = Color(0xFF6C7086)
                            )
                        }
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color(0xFF6C7086))
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Tabs: 0: Change PIN, 1: Test PIN, 2: Protected Rules
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFF1F3F9))
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TabButton(
                        title = if (isHindi) "पिन सेट करें" else "Set / Change PIN",
                        isSelected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        modifier = Modifier.weight(1f)
                    )
                    TabButton(
                        title = if (isHindi) "पिन टेस्ट करें" else "Test PIN",
                        isSelected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        modifier = Modifier.weight(1f)
                    )
                    TabButton(
                        title = if (isHindi) "सुरक्षित फीचर्स" else "Protected Features",
                        isSelected = selectedTab == 2,
                        onClick = { selectedTab = 2 },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Main Tab Contents
                when (selectedTab) {
                    0 -> SetPinTabContent(
                        masterPin = masterPin,
                        newPin = newPin,
                        confirmPin = confirmPin,
                        isCurrentPinRevealed = isCurrentPinRevealed,
                        isNewPinRevealed = isNewPinRevealed,
                        errorMessage = errorMessage,
                        successMessage = successMessage,
                        isHindi = isHindi,
                        onNewPinChange = {
                            if (it.length <= 4 && it.all { ch -> ch.isDigit() }) {
                                newPin = it
                                errorMessage = null
                                successMessage = null
                            }
                        },
                        onConfirmPinChange = {
                            if (it.length <= 4 && it.all { ch -> ch.isDigit() }) {
                                confirmPin = it
                                errorMessage = null
                                successMessage = null
                            }
                        },
                        onToggleCurrentPinReveal = { isCurrentPinRevealed = !isCurrentPinRevealed },
                        onToggleNewPinReveal = { isNewPinRevealed = !isNewPinRevealed },
                        onSavePin = {
                            focusManager.clearFocus()
                            if (newPin.length != 4) {
                                errorMessage = if (isHindi) "पिन ठीक 4 अंकों का होना चाहिए" else "PIN must be exactly 4 digits"
                            } else if (newPin != confirmPin) {
                                errorMessage = if (isHindi) "दोनों पिन मेल नहीं खाते! पुनः जांचें" else "PINs do not match! Please check again"
                            } else {
                                onChangePin(newPin)
                                errorMessage = null
                                successMessage = if (isHindi) "नया सुरक्षा पिन सफलतापूर्वक अपडेट हो गया! (PIN: $newPin)" else "Security PIN updated successfully! (PIN: $newPin)"
                                newPin = ""
                                confirmPin = ""
                            }
                        }
                    )
                    1 -> TestPinKeypadTabContent(
                        masterPin = masterPin,
                        isHindi = isHindi
                    )
                    2 -> ProtectedFeaturesTabContent(
                        isHindi = isHindi
                    )
                }
            }
        }
    }
}

@Composable
private fun TabButton(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (isSelected) Color.White else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            fontSize = 12.sp,
            color = if (isSelected) Color(0xFF6C5CE7) else Color(0xFF6C7086)
        )
    }
}

@Composable
private fun SetPinTabContent(
    masterPin: String,
    newPin: String,
    confirmPin: String,
    isCurrentPinRevealed: Boolean,
    isNewPinRevealed: Boolean,
    errorMessage: String?,
    successMessage: String?,
    isHindi: Boolean,
    onNewPinChange: (String) -> Unit,
    onConfirmPinChange: (String) -> Unit,
    onToggleCurrentPinReveal: () -> Unit,
    onToggleNewPinReveal: () -> Unit,
    onSavePin: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Current Active PIN Banner
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F8FE)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE4E7F5))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF6C5CE7).copy(alpha = 0.12f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = Color(0xFF6C5CE7), modifier = Modifier.size(18.dp))
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = if (isHindi) "वर्तमान सक्रिय सुरक्षा पिन:" else "Active Security PIN:",
                                fontSize = 12.sp,
                                color = Color(0xFF6C7086)
                            )
                            Text(
                                text = if (isCurrentPinRevealed) masterPin else "••••",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 18.sp,
                                color = Color(0xFF1E1E2E),
                                letterSpacing = if (isCurrentPinRevealed) 4.sp else 2.sp
                            )
                        }
                    }

                    IconButton(onClick = onToggleCurrentPinReveal) {
                        Icon(
                            imageVector = if (isCurrentPinRevealed) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = "Reveal Current PIN",
                            tint = Color(0xFF6C5CE7)
                        )
                    }
                }
            }
        }

        // Security Notice Info Box
        item {
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = Color(0xFFFFF9E6),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFEAA7)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        Icons.Default.Security,
                        contentDescription = null,
                        tint = Color(0xFFD35400),
                        modifier = Modifier.size(20.dp).padding(top = 2.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = if (isHindi) "पैरेंट सुरक्षा कोड गाइड" else "Strict Parent Confidentiality",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = Color(0xFFD35400)
                        )
                        Text(
                            text = if (isHindi)
                                "यह 4 अंकों का कोड बच्चे के फोन पर कभी नहीं दिखेगा। जब भी आप बच्चे के फोन पर चाइल्ड मोड से बाहर आना चाहें या ऐप को अनइंस्टॉल/कस्टमाइज़ करना चाहें, यह कोड दर्ज करें।"
                            else
                                "This 4-digit code is never displayed on the child's screen. Use it whenever you need to exit Kid Safe Mode, unlock admin permissions, or adjust device protections.",
                            fontSize = 11.sp,
                            color = Color(0xFF5A4300),
                            lineHeight = 15.sp
                        )
                    }
                }
            }
        }

        // Enter New PIN
        item {
            Column {
                Text(
                    text = if (isHindi) "नया 4-अंकीय पिन दर्ज करें" else "Enter New 4-Digit Security PIN",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
                    color = Color(0xFF1E1E2E)
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = newPin,
                    onValueChange = onNewPinChange,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    placeholder = { Text(if (isHindi) "उदा. 4829" else "e.g. 4829", fontSize = 13.sp) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.NumberPassword,
                        imeAction = ImeAction.Next
                    ),
                    visualTransformation = if (isNewPinRevealed) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = onToggleNewPinReveal) {
                            Icon(
                                imageVector = if (isNewPinRevealed) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = null,
                                tint = Color(0xFF6C7086)
                            )
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF6C5CE7),
                        unfocusedBorderColor = Color(0xFFCBD5E1)
                    )
                )
            }
        }

        // Confirm New PIN
        item {
            Column {
                Text(
                    text = if (isHindi) "नए पिन की पुष्टि करें (Re-enter PIN)" else "Confirm New Security PIN",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
                    color = Color(0xFF1E1E2E)
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = confirmPin,
                    onValueChange = onConfirmPinChange,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    placeholder = { Text(if (isHindi) "वही 4 अंक दोबारा दर्ज करें" else "Re-enter the same 4 digits", fontSize = 13.sp) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.NumberPassword,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = { onSavePin() }),
                    visualTransformation = if (isNewPinRevealed) VisualTransformation.None else PasswordVisualTransformation(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF6C5CE7),
                        unfocusedBorderColor = Color(0xFFCBD5E1)
                    )
                )
            }
        }

        // Error & Success Feedback
        if (errorMessage != null) {
            item {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFFFF4757).copy(alpha = 0.1f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.ErrorOutline, contentDescription = null, tint = Color(0xFFFF4757), modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(errorMessage, color = Color(0xFFFF4757), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        if (successMessage != null) {
            item {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFF00B894).copy(alpha = 0.12f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF00B894), modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(successMessage, color = Color(0xFF00B894), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Save Button
        item {
            Spacer(modifier = Modifier.height(4.dp))
            Button(
                onClick = onSavePin,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C5CE7)),
                enabled = newPin.length == 4 && confirmPin.length == 4
            ) {
                Icon(Icons.Default.LockReset, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isHindi) "नया सुरक्षा पिन सहेजें" else "Save New Security PIN",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
private fun TestPinKeypadTabContent(
    masterPin: String,
    isHindi: Boolean
) {
    var enteredPin by remember { mutableStateOf("") }
    var testResult by remember { mutableStateOf<Boolean?>(null) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (isHindi) "पिन सत्यापन टेस्ट" else "Test Security Code Keypad",
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = Color(0xFF1E1E2E)
        )
        Text(
            text = if (isHindi) "अपने वर्तमान मास्टर पिन को टेस्ट करके जांचें" else "Verify that your 4-digit PIN unlocks administrative access",
            fontSize = 11.sp,
            color = Color(0xFF6C7086)
        )

        Spacer(modifier = Modifier.height(14.dp))

        // PIN Indicator Dots
        Row(
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.padding(vertical = 10.dp)
        ) {
            repeat(4) { idx ->
                val isFilled = idx < enteredPin.length
                Box(
                    modifier = Modifier
                        .size(18.dp)
                        .clip(CircleShape)
                        .background(
                            when {
                                testResult == true -> Color(0xFF00B894)
                                testResult == false -> Color(0xFFFF4757)
                                isFilled -> Color(0xFF6C5CE7)
                                else -> Color(0xFFCBD5E1)
                            }
                        )
                )
            }
        }

        if (testResult == true) {
            Text(
                text = if (isHindi) "✅ पिन सही है! एडमिन एक्सेस अनलॉक हो गया" else "✅ PIN Verified! Administrative Access Granted",
                color = Color(0xFF00B894),
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
            )
        } else if (testResult == false) {
            Text(
                text = if (isHindi) "❌ गलत पिन! सही 4-अंकीय कोड दर्ज करें" else "❌ Incorrect PIN! Try again",
                color = Color(0xFFFF4757),
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Keypad Grid
        val rows = listOf(
            listOf("1", "2", "3"),
            listOf("4", "5", "6"),
            listOf("7", "8", "9"),
            listOf("C", "0", "DEL")
        )

        rows.forEach { row ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 3.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                row.forEach { key ->
                    Surface(
                        shape = CircleShape,
                        color = Color(0xFFF4F6FB),
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .clickable {
                                when (key) {
                                    "C" -> {
                                        enteredPin = ""
                                        testResult = null
                                    }
                                    "DEL" -> {
                                        if (enteredPin.isNotEmpty()) {
                                            enteredPin = enteredPin.dropLast(1)
                                            testResult = null
                                        }
                                    }
                                    else -> {
                                        if (enteredPin.length < 4) {
                                            val next = enteredPin + key
                                            enteredPin = next
                                            if (next.length == 4) {
                                                testResult = (next == masterPin)
                                            }
                                        }
                                    }
                                }
                            }
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            if (key == "DEL") {
                                Icon(Icons.Default.Backspace, contentDescription = null, tint = Color(0xFF1E1E2E), modifier = Modifier.size(18.dp))
                            } else {
                                Text(
                                    text = key,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (key == "C") Color(0xFFFF4757) else Color(0xFF1E1E2E)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProtectedFeaturesTabContent(
    isHindi: Boolean
) {
    val features = listOf(
        ProtectedFeatureItem(
            icon = Icons.Default.ExitToApp,
            titleHi = "1. चाइल्ड सेफ मोड से बाहर निकलना (Exit Kid Safe Mode)",
            titleEn = "1. Exit Kid Safe Mode",
            descHi = "बच्चा पैरेंट पिन डाले बिना चाइल्ड मोड स्क्रीन से बाहर नहीं आ सकता।",
            descEn = "Prevents the child from quitting restricted Kid Mode or accessing standard launcher."
        ),
        ProtectedFeatureItem(
            icon = Icons.Default.DeleteForever,
            titleHi = "2. ऐप अनइंस्टॉल व डेटा रीसेट सुरक्षा (Anti-Uninstall)",
            titleEn = "2. Anti-Uninstall & Tamper Guard",
            descHi = "सिस्टम सेटिंग्स में जाकर ऐप हटाने या डेटा मिटाने के लिए यह कोड अनिवार्य है।",
            descEn = "Blocks unauthorized uninstallation, data clearing, or stopping services."
        ),
        ProtectedFeatureItem(
            icon = Icons.Default.AdminPanelSettings,
            titleHi = "3. डिवाइस एडमिनिस्ट्रेटर सेटिंग्स (Admin Permissions)",
            titleEn = "3. Device Administrator Permissions",
            descHi = "एक्सेसिबिलिटी व बैकग्राउंड बैटरी परमिशन में बदलाव करने से रोकता है।",
            descEn = "Protects system accessibility, overlay rights, and background telemetry."
        ),
        ProtectedFeatureItem(
            icon = Icons.Default.LockOpen,
            titleHi = "4. ब्लॉक किए गए ऐप्स का तात्कालिक अनलॉक (App Bypass)",
            titleEn = "4. Instant App Override & Bonus Time",
            descHi = "अभिभावक मौके पर बच्चे के डिवाइस पर पिन डालकर किसी भी ऐप को खोल सकते हैं।",
            descEn = "Allows parents on-the-spot overrides for blocked apps directly on the child device."
        )
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(features.size) { idx ->
            val item = features[idx]
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F8FE)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE4E7F5))
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF6C5CE7).copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(item.icon, contentDescription = null, tint = Color(0xFF6C5CE7), modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = if (isHindi) item.titleHi else item.titleEn,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = Color(0xFF1E1E2E)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = if (isHindi) item.descHi else item.descEn,
                            fontSize = 11.sp,
                            color = Color(0xFF6C7086),
                            lineHeight = 15.sp
                        )
                    }
                }
            }
        }
    }
}

private data class ProtectedFeatureItem(
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val titleHi: String,
    val titleEn: String,
    val descHi: String,
    val descEn: String
)
