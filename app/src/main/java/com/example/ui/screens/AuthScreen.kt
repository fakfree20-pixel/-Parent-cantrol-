package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.EarthAmber100
import com.example.ui.theme.EarthAmber600
import com.example.ui.theme.NaturalBg
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
fun AuthScreen(
    isHindi: Boolean,
    onLogin: (email: String, pass: String, onResult: (Boolean, String) -> Unit) -> Unit,
    onSignUp: (name: String, email: String, pass: String, phone: String, onResult: (Boolean, String) -> Unit) -> Unit,
    onGoogleLogin: (email: String, name: String) -> Unit,
    onGuestLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isSignUpMode by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("Musahid Raza") }
    var email by remember { mutableStateOf("musahidraza78600@gmail.com") }
    var password by remember { mutableStateOf("123456") }
    var phone by remember { mutableStateOf("+91 98765 43210") }
    var passwordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showForgotPasswordDialog by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current

    if (showForgotPasswordDialog) {
        Dialog(onDismissRequest = { showForgotPasswordDialog = false }) {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = NaturalSurface),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Email, contentDescription = null, tint = NaturalGreen700, modifier = Modifier.size(40.dp))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = if (isHindi) "पासवर्ड रीसेट लिंक" else "Reset Password",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = NaturalTextPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (isHindi) "पासवर्ड रीसेट लिंक $email पर भेज दिया गया है।" else "Password reset instructions sent to $email.",
                        fontSize = 13.sp,
                        color = NaturalTextSecondary,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { showForgotPasswordDialog = false },
                        colors = ButtonDefaults.buttonColors(containerColor = NaturalGreen700),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(if (isHindi) "ठीक है" else "Got it", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(NaturalBg)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // Logo & Branding Header
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(22.dp))
                    .background(NaturalGreen100),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Shield,
                    contentDescription = "ParentGuard Logo",
                    tint = NaturalGreen700,
                    modifier = Modifier.size(42.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = if (isHindi) "ParentGuard पैरेंटगार्ड" else "ParentGuard",
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = NaturalGreen900
            )

            Text(
                text = if (isHindi) "सुरक्षित माता-पिता व बाल संरक्षण प्रणाली" else "Smart Parental Control & Child Safety",
                fontSize = 13.sp,
                color = NaturalTextSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Main Auth Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(3.dp, RoundedCornerShape(24.dp))
                    .border(1.dp, NaturalBorder, RoundedCornerShape(24.dp)),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = NaturalSurface)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    // Sign In / Sign Up Tab Switcher
                    TabRow(
                        selectedTabIndex = if (isSignUpMode) 1 else 0,
                        containerColor = NaturalSurfaceVariant,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp)),
                        indicator = { tabPositions ->
                            TabRowDefaults.SecondaryIndicator(
                                Modifier.tabIndicatorOffset(tabPositions[if (isSignUpMode) 1 else 0]),
                                color = NaturalGreen700,
                                height = 3.dp
                            )
                        }
                    ) {
                        Tab(
                            selected = !isSignUpMode,
                            onClick = {
                                isSignUpMode = false
                                errorMessage = null
                            },
                            text = {
                                Text(
                                    text = if (isHindi) "लॉगिन करें" else "Sign In",
                                    fontWeight = if (!isSignUpMode) FontWeight.Bold else FontWeight.Normal,
                                    color = if (!isSignUpMode) NaturalGreen700 else NaturalTextSecondary,
                                    fontSize = 14.sp
                                )
                            }
                        )
                        Tab(
                            selected = isSignUpMode,
                            onClick = {
                                isSignUpMode = true
                                errorMessage = null
                            },
                            text = {
                                Text(
                                    text = if (isHindi) "नया खाता बनाएं" else "Sign Up",
                                    fontWeight = if (isSignUpMode) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSignUpMode) NaturalGreen700 else NaturalTextSecondary,
                                    fontSize = 14.sp
                                )
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Error Banner (if any)
                    AnimatedVisibility(visible = errorMessage != null) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Terracotta100,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp)
                        ) {
                            Text(
                                text = errorMessage ?: "",
                                color = Terracotta700,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                            )
                        }
                    }

                    // Sign Up Name Field
                    if (isSignUpMode) {
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text(if (isHindi) "माता-पिता का नाम" else "Parent's Full Name") },
                            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = NaturalGreen700) },
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = NaturalSurface,
                                unfocusedContainerColor = NaturalSurfaceVariant,
                                focusedBorderColor = NaturalGreen700,
                                unfocusedBorderColor = NaturalBorder
                            ),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("auth_name_field")
                        )

                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    // Email Field
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text(if (isHindi) "ईमेल आईडी (Email ID)" else "Email Address") },
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = NaturalGreen700) },
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = NaturalSurface,
                            unfocusedContainerColor = NaturalSurfaceVariant,
                            focusedBorderColor = NaturalGreen700,
                            unfocusedBorderColor = NaturalBorder
                        ),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("auth_email_field")
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Sign Up Phone Field
                    if (isSignUpMode) {
                        OutlinedTextField(
                            value = phone,
                            onValueChange = { phone = it },
                            label = { Text(if (isHindi) "मोबाइल नंबर" else "Phone Number") },
                            leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = NaturalGreen700) },
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = NaturalSurface,
                                unfocusedContainerColor = NaturalSurfaceVariant,
                                focusedBorderColor = NaturalGreen700,
                                unfocusedBorderColor = NaturalBorder
                            ),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Next),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("auth_phone_field")
                        )

                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    // Password Field
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text(if (isHindi) "पासवर्ड" else "Password") },
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = NaturalGreen700) },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = "Toggle password visibility",
                                    tint = NaturalTextSecondary
                                )
                            }
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = NaturalSurface,
                            unfocusedContainerColor = NaturalSurfaceVariant,
                            focusedBorderColor = NaturalGreen700,
                            unfocusedBorderColor = NaturalBorder
                        ),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("auth_password_field")
                    )

                    // Forgot password link (for Login)
                    if (!isSignUpMode) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(onClick = { showForgotPasswordDialog = true }) {
                                Text(
                                    text = if (isHindi) "पासवर्ड भूल गए?" else "Forgot Password?",
                                    fontSize = 12.sp,
                                    color = NaturalGreen700,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    } else {
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Primary Submit Button
                    Button(
                        onClick = {
                            focusManager.clearFocus()
                            if (isSignUpMode) {
                                onSignUp(name, email, password, phone) { success, msg ->
                                    if (!success) errorMessage = msg
                                }
                            } else {
                                onLogin(email, password) { success, msg ->
                                    if (!success) errorMessage = msg
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = NaturalGreen700),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("auth_submit_button")
                    ) {
                        Text(
                            text = if (isSignUpMode) {
                                if (isHindi) "खाता बनाएं व जारी रखें" else "Create Account & Continue"
                            } else {
                                if (isHindi) "सुरक्षित लॉगिन करें" else "Secure Sign In"
                            },
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Divider(modifier = Modifier.weight(1f), color = NaturalBorder)
                        Text(
                            text = if (isHindi) " या " else " OR ",
                            fontSize = 12.sp,
                            color = NaturalTextTertiary,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                        Divider(modifier = Modifier.weight(1f), color = NaturalBorder)
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Google Login Button
                    OutlinedButton(
                        onClick = {
                            onGoogleLogin(email, if (name.isNotBlank()) name else "Musahid Raza")
                        },
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                    ) {
                        Text(text = "🌐 ", fontSize = 16.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isHindi) "Google से तुरंत जारी रखें" else "Continue with Google",
                            color = NaturalTextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Demo Shortcut
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = EarthAmber100.copy(alpha = 0.5f),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onGuestLogin() }
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                        Icon(Icons.Default.Security, contentDescription = null, tint = EarthAmber600, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = if (isHindi) "डेमो पैरेंट के रूप में देखें" else "Trial Demo Parent Account",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = NaturalTextPrimary
                            )
                            Text(
                                text = "musahidraza78600@gmail.com (Default Master PIN: 1234)",
                                fontSize = 10.sp,
                                color = NaturalTextSecondary
                            )
                        }
                    }

                    Icon(Icons.Default.ArrowForward, contentDescription = null, tint = EarthAmber600, modifier = Modifier.size(18.dp))
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
