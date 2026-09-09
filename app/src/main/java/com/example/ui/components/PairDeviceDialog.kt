package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.*

@Composable
fun PairDeviceDialog(
    pairingCode: String,
    isHindi: Boolean,
    onDismiss: () -> Unit,
    onRegenerateCode: (() -> Unit)? = null,
    onPairWithCode: ((String) -> Unit)? = null
) {
    val context = LocalContext.current
    val downloadLink = "https://ais-dev-c6tplr6aasw3eq4nllohfm-257389990740.europe-west2.run.app/?code=$pairingCode"
    var copiedCode by remember { mutableStateOf(false) }
    var copiedLink by remember { mutableStateOf(false) }
    var inputCode by remember { mutableStateOf("") }
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Share / QR, 1: Enter Code

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(8.dp, RoundedCornerShape(24.dp))
                .border(1.dp, NaturalBorder, RoundedCornerShape(24.dp)),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = NaturalSurface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
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
                                .clip(RoundedCornerShape(12.dp))
                                .background(NaturalGreen100),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Download,
                                contentDescription = null,
                                tint = NaturalGreen700,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = if (isHindi) "चाइल्ड डिवाइस सेटअप व लिंक" else "Pair Child Device & Link",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = NaturalTextPrimary
                        )
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = NaturalTextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Mode Tabs
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFEDE9FF))
                        .padding(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (selectedTab == 0) Color(0xFF6C5CE7) else Color.Transparent)
                            .clickable { selectedTab = 0 }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (isHindi) "QR व कोड शेयर" else "Share Code",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = if (selectedTab == 0) Color.White else Color(0xFF2D1E5E)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (selectedTab == 1) Color(0xFF6C5CE7) else Color.Transparent)
                            .clickable { selectedTab = 1 }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (isHindi) "कोड डालें व लिंक करें" else "Enter Code",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = if (selectedTab == 1) Color.White else Color(0xFF2D1E5E)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                if (selectedTab == 1) {
                    // Enter 10-digit Code Directly
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xFFF4F1FD),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(Icons.Default.Share, contentDescription = null, tint = Color(0xFF6C5CE7), modifier = Modifier.size(36.dp))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = if (isHindi) "बच्चे का कनेक्शन कोड डालें" else "Enter Child Pairing Code",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = Color(0xFF2D1E5E)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (isHindi) "कोड डालते ही डैशबोर्ड तुरंत लाइव हो जाएगा और असली डिवाइस जुड़ जाएगा।" else "Entering code connects the device immediately and goes LIVE.",
                                fontSize = 12.sp,
                                color = Color.Gray,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            OutlinedTextField(
                                value = inputCode,
                                onValueChange = { if (it.length <= 10 && it.all { ch -> ch.isDigit() }) inputCode = it },
                                label = { Text(if (isHindi) "10-डिजिट कोड" else "10-digit code") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFF6C5CE7),
                                    unfocusedBorderColor = Color(0xFFCBD5E1)
                                )
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                            Button(
                                onClick = {
                                    if (inputCode.length == 10) {
                                        onPairWithCode?.invoke(inputCode)
                                        onDismiss()
                                    } else {
                                        Toast.makeText(context, if (isHindi) "कृपया पूरा 10-अंकों का कोड लिखें" else "Please enter full 10-digit code", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2ED573))
                            ) {
                                Icon(Icons.Default.Check, contentDescription = null, tint = Color.White)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (isHindi) "⚡ अभी लिंक करें व लाइव हों" else "⚡ Connect & Go Live",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = Color.White
                                )
                            }
                        }
                    }
                } else {
                    Text(
                        text = if (isHindi) 
                            "Parent Control MD ऐप से अपने बच्चे के फोन को जोड़ने के लिए नीचे दिए गए QR कोड को स्कैन करें या 10-अंकों का कोड डालें:" 
                            else "Scan the QR code or enter the 10-digit code on the child's phone to connect:",
                        fontSize = 13.sp,
                        color = NaturalTextSecondary,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // FlashGet Style QR Code Box
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White,
                    border = androidx.compose.foundation.BorderStroke(2.dp, Color(0xFF6C5CE7).copy(alpha = 0.3f)),
                    modifier = Modifier.size(160.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        // QR Code Visual Simulation Box
                        Box(
                            modifier = Modifier
                                .size(110.dp)
                                .background(Color.White)
                                .border(2.dp, Color(0xFF2D1E5E), RoundedCornerShape(8.dp))
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                verticalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Box(modifier = Modifier.size(24.dp).background(Color(0xFF2D1E5E), RoundedCornerShape(4.dp)).padding(4.dp)) {
                                        Box(modifier = Modifier.fillMaxSize().background(Color.White, RoundedCornerShape(2.dp)).padding(2.dp)) {
                                            Box(modifier = Modifier.fillMaxSize().background(Color(0xFF2D1E5E)))
                                        }
                                    }
                                    Box(modifier = Modifier.size(24.dp).background(Color(0xFF2D1E5E), RoundedCornerShape(4.dp)).padding(4.dp)) {
                                        Box(modifier = Modifier.fillMaxSize().background(Color.White, RoundedCornerShape(2.dp)).padding(2.dp)) {
                                            Box(modifier = Modifier.fillMaxSize().background(Color(0xFF2D1E5E)))
                                        }
                                    }
                                }
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                                    Text("⚡ PARENT MD ⚡", fontSize = 7.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF6C5CE7))
                                }
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Box(modifier = Modifier.size(24.dp).background(Color(0xFF2D1E5E), RoundedCornerShape(4.dp)).padding(4.dp)) {
                                        Box(modifier = Modifier.fillMaxSize().background(Color.White, RoundedCornerShape(2.dp)).padding(2.dp)) {
                                            Box(modifier = Modifier.fillMaxSize().background(Color(0xFF2D1E5E)))
                                        }
                                    }
                                    Box(modifier = Modifier.size(16.dp).background(Color(0xFF6C5CE7), RoundedCornerShape(3.dp)))
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(if (isHindi) "बच्चे के कैमरे से स्कैन करें" else "Scan with Child's Camera", fontSize = 9.sp, color = Color(0xFF6C5CE7), fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Pairing Code Card
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFFF0EDFF),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = if (isHindi) "कनेक्शन कोड (Pairing Code)" else "Connection Pairing Code",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF6C5CE7)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = pairingCode,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 2.sp,
                            color = Color(0xFF2D1E5E)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        OutlinedButton(
                            onClick = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("Pairing Code", pairingCode)
                                clipboard.setPrimaryClip(clip)
                                copiedCode = true
                                Toast.makeText(context, if (isHindi) "कोड कॉपी हो गया!" else "Code copied!", Toast.LENGTH_SHORT).show()
                            },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White)
                        ) {
                            Icon(if (copiedCode) Icons.Default.Check else Icons.Default.ContentCopy, contentDescription = null, tint = Color(0xFF6C5CE7), modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = if (copiedCode) "कॉपी किया गया" else "कोड कॉपी करें", fontSize = 12.sp, color = Color(0xFF6C5CE7))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Download Link Card
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = NaturalSurfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = if (isHindi) "चाइल्ड ऐप डाउनलोड लिंक (Child APK Link)" else "Child App Download Link",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = NaturalTextSecondary
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = downloadLink,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = NaturalGreen900,
                            textAlign = TextAlign.Center,
                            maxLines = 2
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedButton(
                                onClick = {
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    val clip = ClipData.newPlainText("Download Link", downloadLink)
                                    clipboard.setPrimaryClip(clip)
                                    copiedLink = true
                                    Toast.makeText(context, if (isHindi) "लिंक कॉपी हो गया!" else "Link copied!", Toast.LENGTH_SHORT).show()
                                },
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White)
                            ) {
                                Icon(if (copiedLink) Icons.Default.Check else Icons.Default.ContentCopy, contentDescription = null, tint = NaturalGreen700, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = if (copiedLink) "कॉपी हुआ" else "लिंक कॉपी", fontSize = 12.sp, color = NaturalGreen700)
                            }

                            Button(
                                onClick = {
                                    val sendIntent = android.content.Intent().apply {
                                        action = android.content.Intent.ACTION_SEND
                                        putExtra(android.content.Intent.EXTRA_TEXT, "Parent Control MD ऐप डाउनलोड करें और यह कोड दर्ज करें: $pairingCode \nलिंक: $downloadLink")
                                        type = "text/plain"
                                    }
                                    val shareIntent = android.content.Intent.createChooser(sendIntent, null)
                                    context.startActivity(shareIntent)
                                },
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = NaturalGreen700)
                            ) {
                                Icon(Icons.Default.Share, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = if (isHindi) "व्हाट्सएप पर भेजें" else "Share via WhatsApp", fontSize = 12.sp, color = Color.White)
                            }
                        }
                    }
                }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C5CE7))
                ) {
                    Text(text = if (isHindi) "समझ गया (Done)" else "Done", fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}
