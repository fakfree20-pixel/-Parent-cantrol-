package com.example.ui.components

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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.R
import com.example.data.model.UserAccount
import com.example.ui.theme.EarthAmber100
import com.example.ui.theme.NaturalBorder
import com.example.ui.theme.NaturalGreen100
import com.example.ui.theme.NaturalGreen700
import com.example.ui.theme.NaturalGreen900
import com.example.ui.theme.NaturalSurface
import com.example.ui.theme.NaturalSurfaceVariant
import com.example.ui.theme.NaturalTextPrimary
import com.example.ui.theme.NaturalTextSecondary
import com.example.ui.theme.NaturalTextTertiary
import com.example.ui.theme.Terracotta100
import com.example.ui.theme.Terracotta700

@Composable
fun AccountProfileDialog(
    user: UserAccount?,
    masterPin: String,
    isHindi: Boolean,
    onLogout: () -> Unit,
    onDismiss: () -> Unit,
    onChangePin: ((String) -> Unit)? = null,
    onOpenPinManager: (() -> Unit)? = null
) {
    val userName = user?.name ?: if (isHindi) "पैरेंट यूज़र" else "Parent User"
    val userEmail = user?.email ?: if (isHindi) "लॉगिन नहीं है" else "Not logged in"
    val userPhone = user?.phoneNumber ?: "—"

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(4.dp, RoundedCornerShape(24.dp))
                .border(1.dp, NaturalBorder, RoundedCornerShape(24.dp)),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = NaturalSurface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
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
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(NaturalGreen100),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.VerifiedUser,
                                contentDescription = null,
                                tint = NaturalGreen700,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = if (isHindi) "पैरेंट प्रोफ़ाइल व खाता" else "Parent Profile & Account",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = NaturalTextPrimary
                            )
                            Text(
                                text = if (isHindi) "सक्रिय लॉगिन आईडी" else "Active Authenticated User",
                                fontSize = 11.sp,
                                color = NaturalGreen700
                            )
                        }
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = NaturalTextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // User Info Card
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = NaturalSurfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(52.dp)
                                    .clip(CircleShape)
                                    .border(2.dp, NaturalGreen700, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.user_custom_logo_1787213664319),
                                    contentDescription = "User Avatar",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = userName,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = NaturalTextPrimary
                                )
                                Text(
                                    text = userEmail,
                                    fontSize = 12.sp,
                                    color = NaturalTextSecondary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        Divider(color = NaturalBorder)
                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(if (isHindi) "भूमिका (Role)" else "Account Role", fontSize = 10.sp, color = NaturalTextTertiary)
                                Text(if (isHindi) "मुख्य अभिभावक (Parent)" else "Primary Guardian", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = NaturalGreen900)
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(if (isHindi) "डिवाइस सिंक स्थिति" else "Cloud Sync", fontSize = 10.sp, color = NaturalTextTertiary)
                                Text("🟢 Online & Synced", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = NaturalGreen700)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Master PIN Info with Edit option
                var isEditingPin by remember { mutableStateOf(false) }
                var newPinInput by remember { mutableStateOf("") }
                var pinError by remember { mutableStateOf<String?>(null) }

                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = EarthAmber100.copy(alpha = 0.5f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                Icon(Icons.Default.Key, contentDescription = null, tint = NaturalGreen700, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = if (isHindi) "सेटिंग लॉक और अनइंस्टॉल सुरक्षा पिन:" else "Master / Anti-Uninstall PIN:",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = NaturalTextPrimary
                                    )
                                    Text(
                                        text = "${user?.antiUninstallPin ?: masterPin}",
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 14.sp,
                                        color = NaturalGreen900
                                    )
                                }
                            }
                            if (onChangePin != null) {
                                IconButton(
                                    onClick = {
                                        isEditingPin = !isEditingPin
                                        newPinInput = ""
                                        pinError = null
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "Edit PIN",
                                        tint = Color(0xFF6C5CE7),
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }

                        if (isEditingPin) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedTextField(
                                    value = newPinInput,
                                    onValueChange = { input ->
                                        if (input.length <= 4 && input.all { it.isDigit() }) {
                                            newPinInput = input
                                            pinError = null
                                        }
                                    },
                                    placeholder = { Text("उदा. 5678", fontSize = 11.sp) },
                                    label = { Text(if (isHindi) "नया पिन (4 अंक)" else "New PIN (4 digit)", fontSize = 11.sp) },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    modifier = Modifier.weight(1f),
                                    singleLine = true,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Color(0xFF6C5CE7),
                                        unfocusedBorderColor = Color(0xFFCBD5E1)
                                    )
                                )
                                Button(
                                    onClick = {
                                        if (newPinInput.length == 4 && newPinInput.all { it.isDigit() }) {
                                            onChangePin?.invoke(newPinInput)
                                            isEditingPin = false
                                        } else {
                                            pinError = if (isHindi) "कृपया 4 अंक दर्ज करें" else "Must be 4 digits"
                                        }
                                    },
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C5CE7)),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                                    modifier = Modifier.height(38.dp)
                                ) {
                                    Text(if (isHindi) "बदलें" else "Save", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }
                            if (pinError != null) {
                                Text(pinError!!, color = Color.Red, fontSize = 10.sp, modifier = Modifier.padding(start = 4.dp, top = 4.dp))
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // App Package Details Info Card
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = Color(0xFFEFF6FF),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Security, contentDescription = null, tint = Color(0xFF2563EB), modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isHindi) "ऐप पैकेज विवरण (Package Name)" else "App Package Name & Info",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = Color(0xFF1E40AF)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "com.aistudio.parentguard.screentime",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp,
                            color = Color(0xFF1E3A8A)
                        )
                        Text(
                            text = if (isHindi) "वर्जन: 1.0 (parent cantrol md)" else "Version: 1.0 (parent cantrol md)",
                            fontSize = 10.sp,
                            color = Color(0xFF3B82F6)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Logout Action
                Button(
                    onClick = {
                        onLogout()
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Terracotta100),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.ExitToApp, contentDescription = null, tint = Terracotta700, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isHindi) "लॉगआउट करें (Sign Out)" else "Sign Out of Account",
                        color = Terracotta700,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}
