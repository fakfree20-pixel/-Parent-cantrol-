package com.example.ui.screens

import android.accounts.AccountManager
import android.app.Activity
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.R

@Composable
fun AuthScreen(
    isHindi: Boolean,
    onLogin: (email: String, pass: String, onResult: (Boolean, String) -> Unit) -> Unit,
    onSignUp: (name: String, email: String, pass: String, phone: String, pin: String, onResult: (Boolean, String) -> Unit) -> Unit,
    onGoogleLogin: (email: String, name: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isSignUpMode by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var antiUninstallPin by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showForgotPasswordDialog by remember { mutableStateOf(false) }
    var showSupportDialog by remember { mutableStateOf(false) }
    var showMoreMenu by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    val accountPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val accountName = result.data?.getStringExtra(AccountManager.KEY_ACCOUNT_NAME)
            if (accountName != null) {
                val name = accountName.substringBefore("@").replace(".", " ")
                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
                onGoogleLogin(accountName, name)
            }
        }
    }

    if (showForgotPasswordDialog) {
        Dialog(onDismissRequest = { showForgotPasswordDialog = false }) {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Email, contentDescription = null, tint = Color(0xFF6C5CE7), modifier = Modifier.size(40.dp))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = if (isHindi) "पासवर्ड रीसेट लिंक" else "Reset Password",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color(0xFF1E1E2E)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (isHindi) "पासवर्ड रीसेट लिंक $email पर भेज दिया गया है।" else "Password reset instructions sent to $email.",
                        fontSize = 13.sp,
                        color = Color(0xFF6C7086),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { showForgotPasswordDialog = false },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C5CE7)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(if (isHindi) "ठीक है" else "Got it", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }

    if (showSupportDialog) {
        Dialog(onDismissRequest = { showSupportDialog = false }) {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Headphones, contentDescription = null, tint = Color(0xFF6C5CE7), modifier = Modifier.size(40.dp))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = if (isHindi) "24/7 कस्टमर सपोर्ट" else "Customer Support",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color(0xFF1E1E2E)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (isHindi) "सहायता के लिए हमें support@parentguard.app पर लिखें या लाइव चैट करें।" else "For immediate help, email support@parentguard.app or reach out via live chat.",
                        fontSize = 13.sp,
                        color = Color(0xFF6C7086),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { showSupportDialog = false },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C5CE7)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(if (isHindi) "बंद करें" else "Close", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        // TOP BAR: Support icon and More options
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { showSupportDialog = true }) {
                Icon(Icons.Default.Headphones, contentDescription = "Support", tint = Color(0xFF2D3436))
            }
            Box {
                IconButton(onClick = { showMoreMenu = true }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "More", tint = Color(0xFF2D3436))
                }
                DropdownMenu(expanded = showMoreMenu, onDismissRequest = { showMoreMenu = false }) {
                    DropdownMenuItem(
                        text = { Text("Terms of Service") },
                        onClick = { showMoreMenu = false }
                    )
                    DropdownMenuItem(
                        text = { Text("Privacy Policy") },
                        onClick = { showMoreMenu = false }
                    )
                }
            }
        }

        // App Logo & Brand Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .border(2.dp, Color(0xFF6C5CE7), RoundedCornerShape(16.dp))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.user_custom_logo_1787213664319),
                    contentDescription = "App Logo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column {
                Text(
                    text = "parent cantrol md",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF1E1E2E)
                )
                Text(
                    text = if (isHindi) "सुरक्षित पैरेंटल कंट्रोल शील्ड" else "Intelligent Child Protection Hub",
                    fontSize = 12.sp,
                    color = Color(0xFF6C5CE7),
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        // Large Header: "You need to sign in" (Matching Screenshot 3)
        Text(
            text = if (isSignUpMode) (if (isHindi) "नया खाता बनाएं" else "Create Account")
            else (if (isHindi) "आपको साइन इन करना होगा" else "You need to sign in"),
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1E1E2E)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Form Fields
        if (isSignUpMode) {
            // Full Name (Sign Up only)
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(if (isHindi) "पूरा नाम" else "Full Name", color = Color(0xFF4A5568)) },
                placeholder = { Text("Enter full name", color = Color(0xFFA0AEC0)) },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = Color(0xFF6C5CE7)) },
                modifier = Modifier.fillMaxWidth().testTag("auth_name_field"),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF6C5CE7),
                    unfocusedBorderColor = Color(0xFFCBD5E1),
                    focusedLabelColor = Color(0xFF6C5CE7),
                    unfocusedLabelColor = Color(0xFF4A5568),
                    focusedTextColor = Color(0xFF1E1E2E),
                    unfocusedTextColor = Color(0xFF1E1E2E),
                    focusedContainerColor = Color(0xFFF8F9FA),
                    unfocusedContainerColor = Color(0xFFF8F9FA)
                ),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(14.dp))
        }

        // Email Field
        OutlinedTextField(
            value = email,
            onValueChange = { email = it; errorMessage = null },
            label = { Text(if (isHindi) "ईमेल पता" else "Email", color = Color(0xFF4A5568)) },
            placeholder = { Text("example@gmail.com", color = Color(0xFFA0AEC0)) },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = Color(0xFF6C5CE7)) },
            modifier = Modifier.fillMaxWidth().testTag("auth_email_field"),
            shape = RoundedCornerShape(16.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF6C5CE7),
                unfocusedBorderColor = Color(0xFFCBD5E1),
                focusedLabelColor = Color(0xFF6C5CE7),
                unfocusedLabelColor = Color(0xFF4A5568),
                focusedTextColor = Color(0xFF1E1E2E),
                unfocusedTextColor = Color(0xFF1E1E2E),
                focusedContainerColor = Color(0xFFF8F9FA),
                unfocusedContainerColor = Color(0xFFF8F9FA)
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Password Field
        OutlinedTextField(
            value = password,
            onValueChange = { password = it; errorMessage = null },
            label = { Text(if (isHindi) "पासवर्ड" else "Password", color = Color(0xFF4A5568)) },
            placeholder = { Text("Enter password", color = Color(0xFFA0AEC0)) },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = Color(0xFF6C5CE7)) },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = "Toggle Password",
                        tint = Color(0xFF6C7086)
                    )
                }
            },
            modifier = Modifier.fillMaxWidth().testTag("auth_password_field"),
            shape = RoundedCornerShape(16.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = if (isSignUpMode) ImeAction.Next else ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                if (!isSignUpMode) {
                    focusManager.clearFocus()
                    onLogin(email, password) { ok, err -> if (!ok) errorMessage = err }
                }
            }),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF6C5CE7),
                unfocusedBorderColor = Color(0xFFCBD5E1),
                focusedLabelColor = Color(0xFF6C5CE7),
                unfocusedLabelColor = Color(0xFF4A5568),
                focusedTextColor = Color(0xFF1E1E2E),
                unfocusedTextColor = Color(0xFF1E1E2E),
                focusedContainerColor = Color(0xFFF8F9FA),
                unfocusedContainerColor = Color(0xFFF8F9FA)
            ),
            singleLine = true
        )

        if (isSignUpMode) {
            Spacer(modifier = Modifier.height(14.dp))
            // Custom Master PIN (FlashGet Kids style)
            OutlinedTextField(
                value = antiUninstallPin,
                onValueChange = { input ->
                    if (input.length <= 4 && input.all { it.isDigit() }) {
                        antiUninstallPin = input
                        errorMessage = null
                    }
                },
                label = { Text(if (isHindi) "अपना मनपसंद मास्टर सुरक्षा पिन (4 अंक)" else "Choose Custom Master PIN (4 digits)", color = Color(0xFF4A5568)) },
                placeholder = { Text("उदा. 4890 (e.g. 4890)", color = Color(0xFFA0AEC0)) },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = Color(0xFF6C5CE7)) },
                modifier = Modifier.fillMaxWidth().testTag("auth_anti_uninstall_pin_field"),
                shape = RoundedCornerShape(16.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword, imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    focusManager.clearFocus()
                    if (antiUninstallPin.length != 4 || !antiUninstallPin.all { it.isDigit() }) {
                        errorMessage = if (isHindi) "कृपया अपना मनपसंद 4-अंकों का मास्टर पिन दर्ज करें!" else "Please enter your custom 4-digit Master PIN!"
                    } else {
                        onSignUp(name, email, password, phone, antiUninstallPin) { ok, err -> if (!ok) errorMessage = err }
                    }
                }),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF6C5CE7),
                    unfocusedBorderColor = Color(0xFFCBD5E1),
                    focusedLabelColor = Color(0xFF6C5CE7),
                    unfocusedLabelColor = Color(0xFF4A5568),
                    focusedTextColor = Color(0xFF1E1E2E),
                    unfocusedTextColor = Color(0xFF1E1E2E),
                    focusedContainerColor = Color(0xFFF8F9FA),
                    unfocusedContainerColor = Color(0xFFF8F9FA)
                ),
                singleLine = true
            )
        }

        if (!isSignUpMode) {
            // Forgot password? Link on the right
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = if (isHindi) "पासवर्ड भूल गए?" else "Forgot password?",
                    color = Color(0xFF6C7086),
                    fontSize = 13.sp,
                    modifier = Modifier.clickable { showForgotPasswordDialog = true }
                )
            }
        }

        // Error message if any
        if (errorMessage != null) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = errorMessage ?: "",
                color = Color(0xFFFF4757),
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Sign In with Google Button (Bordered card button)
        OutlinedButton(
            onClick = {
                val intent = AccountManager.newChooseAccountIntent(
                    null, null, arrayOf("com.google"), false, null, null, null, null
                )
                accountPickerLauncher.launch(intent)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("auth_google_button"),
            shape = RoundedCornerShape(26.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE4E7F5)),
            colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                // Colorful Google 'G' Symbol
                Text(
                    text = "G",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF4285F4)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = if (isHindi) "Google से साइन इन करें" else "Sign in with Google",
                    color = Color(0xFF2D3436),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Vibrant Purple Sign In / Sign Up Button (Matching Screenshot 3)
        Button(
            onClick = {
                focusManager.clearFocus()
                if (isSignUpMode) {
                    if (antiUninstallPin.length != 4 || !antiUninstallPin.all { it.isDigit() }) {
                        errorMessage = if (isHindi) "कृपया अपना मनपसंद 4-अंकों का मास्टर पिन दर्ज करें!" else "Please enter your custom 4-digit Master PIN!"
                    } else {
                        onSignUp(name, email, password, phone, antiUninstallPin) { ok, err -> if (!ok) errorMessage = err }
                    }
                } else {
                    onLogin(email, password) { ok, err -> if (!ok) errorMessage = err }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("auth_submit_button"),
            shape = RoundedCornerShape(26.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C5CE7))
        ) {
            Text(
                text = if (isSignUpMode) (if (isHindi) "साइन अप करें" else "Sign up")
                else (if (isHindi) "साइन इन करें" else "Sign in"),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        // Footer: "Do not have an account? Sign up" / "Already have an account? Sign in"
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (isSignUpMode) {
                    if (isHindi) "पहले से खाता है? " else "Already have an account? "
                } else {
                    if (isHindi) "खाता नहीं है? " else "Do not have an account? "
                },
                color = Color(0xFF6C7086),
                fontSize = 14.sp
            )
            Text(
                text = if (isSignUpMode) {
                    if (isHindi) "साइन इन करें" else "Sign in"
                } else {
                    if (isHindi) "साइन अप करें" else "Sign up"
                },
                color = Color(0xFF6C5CE7),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier.clickable {
                    isSignUpMode = !isSignUpMode
                    errorMessage = null
                }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
