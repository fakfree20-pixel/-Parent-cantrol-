package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.ParentalRepository
import com.example.data.model.ActivityLogItem
import com.example.data.model.AppNotificationItem
import com.example.data.model.AppUsageRule
import com.example.data.model.CallLogItem
import com.example.data.model.ChildProfile
import com.example.data.model.GeofenceZone
import com.example.data.model.ScreenRewardTask
import com.example.data.model.SmsMessageItem
import com.example.data.model.UserAccount
import com.example.data.model.WebFilterRule
import com.example.data.model.WhatsAppConversation
import com.example.data.model.YouTubeWatchItem
import com.example.data.sync.FirebaseCloudSyncManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class ParentalControlViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ParentalRepository

    init {
        val db = AppDatabase.getDatabase(application)
        repository = ParentalRepository(db.parentalControlDao())

        // Sync masterPin with the logged-in user's custom antiUninstallPin automatically!
        viewModelScope.launch {
            repository.currentUserAccount.collect { user ->
                if (user != null) {
                    _masterPin.value = user.antiUninstallPin
                }
            }
        }
    }

    // Language setting ("hi" = Hindi, "en" = English)
    private val _currentLanguage = MutableStateFlow("hi")
    val currentLanguage: StateFlow<String> = _currentLanguage.asStateFlow()

    fun toggleLanguage() {
        _currentLanguage.value = if (_currentLanguage.value == "hi") "en" else "hi"
    }

    // Active Child selection
    private val _selectedChildId = MutableStateFlow<Long?>(null)
    val selectedChildId: StateFlow<Long?> = _selectedChildId.asStateFlow()

    val allChildProfiles: StateFlow<List<ChildProfile>> = repository.allChildProfiles
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val activeChildProfile: StateFlow<ChildProfile?> = combine(allChildProfiles, _selectedChildId) { profiles, selectedId ->
        if (profiles.isEmpty()) {
            null
        } else {
            val child = profiles.find { it.id == selectedId } ?: profiles.first()
            if (_selectedChildId.value != child.id) {
                _selectedChildId.value = child.id
            }
            child
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun selectChild(childId: Long) {
        _selectedChildId.value = childId
    }

    // Reactive data based on selected child
    val appUsageRules: StateFlow<List<AppUsageRule>> = _selectedChildId.flatMapLatest { id ->
        if (id != null) repository.getAppUsageRules(id) else flowOf(emptyList())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val rewardTasks: StateFlow<List<ScreenRewardTask>> = _selectedChildId.flatMapLatest { id ->
        if (id != null) repository.getRewardTasks(id) else flowOf(emptyList())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val activityLogs: StateFlow<List<ActivityLogItem>> = _selectedChildId.flatMapLatest { id ->
        if (id != null) repository.getActivityLogs(id) else flowOf(emptyList())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val webFilterRules: StateFlow<List<WebFilterRule>> = _selectedChildId.flatMapLatest { id ->
        if (id != null) repository.getWebFilterRules(id) else flowOf(emptyList())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val geofenceZones: StateFlow<List<GeofenceZone>> = _selectedChildId.flatMapLatest { id ->
        if (id != null) repository.getGeofenceZones(id) else flowOf(emptyList())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val appNotifications: StateFlow<List<AppNotificationItem>> = _selectedChildId.flatMapLatest { id ->
        if (id != null) repository.getAppNotifications(id) else flowOf(emptyList())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val whatsAppConversations: StateFlow<List<WhatsAppConversation>> = _selectedChildId.flatMapLatest { id ->
        if (id != null) repository.getWhatsAppConversations(id) else flowOf(emptyList())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val callLogs: StateFlow<List<CallLogItem>> = _selectedChildId.flatMapLatest { id ->
        if (id != null) repository.getCallLogs(id) else flowOf(emptyList())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val smsMessages: StateFlow<List<SmsMessageItem>> = _selectedChildId.flatMapLatest { id ->
        if (id != null) repository.getSmsMessages(id) else flowOf(emptyList())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val youTubeWatchHistory: StateFlow<List<YouTubeWatchItem>> = _selectedChildId.flatMapLatest { id ->
        if (id != null) repository.getYouTubeWatchHistory(id) else flowOf(emptyList())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun clearSmsMessages(childId: Long? = null) {
        val targetId = childId ?: _selectedChildId.value ?: return
        viewModelScope.launch {
            repository.clearSmsMessages(targetId)
        }
    }

    fun clearYouTubeWatchHistory(childId: Long? = null) {
        val targetId = childId ?: _selectedChildId.value ?: return
        viewModelScope.launch {
            repository.clearYouTubeWatchHistory(targetId)
        }
    }

    // Email Authentication & User Account System
    val currentUserAccount: StateFlow<UserAccount?> = repository.currentUserAccount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Security & PIN
    private val _masterPin = MutableStateFlow("1234")
    val masterPin: StateFlow<String> = _masterPin.asStateFlow()

    private val _isParentPinUnlocked = MutableStateFlow(true)
    val isParentPinUnlocked: StateFlow<Boolean> = _isParentPinUnlocked.asStateFlow()

    fun verifyPin(pin: String): Boolean {
        val success = pin == _masterPin.value
        if (success) {
            _isParentPinUnlocked.value = true
        }
        return success
    }

    fun changePin(newPin: String) {
        if (newPin.length == 4 && newPin.all { it.isDigit() }) {
            _masterPin.value = newPin
            updateAntiUninstallPin(newPin)
        }
    }

    // App Mode: false = Parent Mode, true = Child Device Mode
    private val _isChildModeActive = MutableStateFlow(false)
    val isChildModeActive: StateFlow<Boolean> = _isChildModeActive.asStateFlow()

    fun enterChildMode() {
        _isChildModeActive.value = true
    }

    fun exitChildMode() {
        _isChildModeActive.value = false
    }

    fun setChildMode(active: Boolean) {
        _isChildModeActive.value = active
    }

    // Persistent Unique 10-Digit Parent Pairing Code (Generated uniquely per installation)
    private val prefs = application.getSharedPreferences("parental_control_prefs", android.content.Context.MODE_PRIVATE)
    
    private val _parentPairingCode = MutableStateFlow(
        prefs.getString("unique_pairing_code", null) ?: run {
            val generated = (1000000000L + (Math.random() * 8999999999L).toLong()).toString()
            prefs.edit().putString("unique_pairing_code", generated).apply()
            generated
        }
    )
    val parentPairingCode: StateFlow<String> = _parentPairingCode.asStateFlow()

    fun regeneratePairingCode(): String {
        val newCode = (1000000000L + (Math.random() * 8999999999L).toLong()).toString()
        prefs.edit().putString("unique_pairing_code", newCode).apply()
        _parentPairingCode.value = newCode
        return newCode
    }

    fun linkChildWithPairingCode(code: String, onResult: (Boolean, String) -> Unit) {
        if (code.length == 10 && code.all { it.isDigit() }) {
            viewModelScope.launch {
                // Ensure a child profile exists so child mode works properly
                if (allChildProfiles.value.isEmpty()) {
                    val newId = repository.insertChildProfile(ChildProfile(
                        name = "Linked Child",
                        age = 11,
                        avatarIndex = 0,
                        deviceModel = "Connected Child Phone",
                        batteryPercent = 90,
                        isDeviceOnline = true
                    ))
                    _selectedChildId.value = newId
                }
                _isChildModeActive.value = true
                onResult(true, "")
            }
        } else {
            onResult(false, "Invalid 10-digit pairing code. Please check and try again.")
        }
    }

    // Real-time Cloud Sync
    private val _isCloudConnected = MutableStateFlow(true)
    val isCloudConnected: StateFlow<Boolean> = _isCloudConnected.asStateFlow()

    private val _lastCloudSyncTime = MutableStateFlow(System.currentTimeMillis())
    val lastCloudSyncTime: StateFlow<Long> = _lastCloudSyncTime.asStateFlow()

    // Real-time Background Usage Simulation
    private var simulationJob: Job? = null

    init {
        startSimulation()
        initFirebaseSync()
    }

    private fun initFirebaseSync() {
        FirebaseCloudSyncManager.startListeningToChildDevice(_parentPairingCode.value) { data ->
            viewModelScope.launch {
                _lastCloudSyncTime.value = System.currentTimeMillis()
                _isCloudConnected.value = true
                val child = activeChildProfile.value
                if (child != null) {
                    val isLocked = data["isLocked"] as? Boolean ?: child.isLocked
                    val battery = (data["batteryPercent"] as? Number)?.toInt() ?: child.batteryPercent
                    val isOnline = data["isDeviceOnline"] as? Boolean ?: child.isDeviceOnline
                    if (isLocked != child.isLocked || battery != child.batteryPercent || isOnline != child.isDeviceOnline) {
                        repository.updateChildProfile(
                            child.copy(
                                isLocked = isLocked,
                                batteryPercent = battery,
                                isDeviceOnline = isOnline
                            )
                        )
                    }
                }
            }
        }
    }

    fun syncNowWithCloud() {
        val child = activeChildProfile.value ?: return
        viewModelScope.launch {
            _lastCloudSyncTime.value = System.currentTimeMillis()
            FirebaseCloudSyncManager.syncChildProfileToCloud(child, _parentPairingCode.value)
        }
    }

    private fun startSimulation() {
        simulationJob?.cancel()
        simulationJob = viewModelScope.launch {
            while (isActive) {
                delay(30000) // update every 30 seconds
                val child = activeChildProfile.value
                val apps = appUsageRules.value
                if (child != null && !child.isLocked && !child.blockAllApps && apps.isNotEmpty()) {
                    val activeApp = apps.filter { !it.isBlocked }.shuffled().firstOrNull()
                    if (activeApp != null) {
                        repository.addAppUsageMinutes(activeApp.id, 1)
                    }
                }
            }
        }
    }

    // User Actions
    fun setInstantLock(isLocked: Boolean, reason: String = "Parent Lock", durationMinutes: Int = 0) {
        val childId = _selectedChildId.value ?: return
        viewModelScope.launch {
            repository.setInstantLock(childId, isLocked, reason, durationMinutes)
        }
    }

    fun toggleBlockAllApps(blockAll: Boolean) {
        val childId = _selectedChildId.value ?: return
        viewModelScope.launch {
            repository.setBlockAllApps(childId, blockAll)
        }
    }

    fun addBonusMinutes(minutes: Int, reason: String = "Bonus Screen Time") {
        val childId = _selectedChildId.value ?: return
        viewModelScope.launch {
            repository.addBonusMinutes(childId, minutes, reason)
        }
    }

    fun toggleAppBlock(rule: AppUsageRule) {
        val childId = _selectedChildId.value ?: return
        viewModelScope.launch {
            repository.toggleAppBlock(rule.id, childId, rule.appName, !rule.isBlocked)
        }
    }

    fun setAppLimit(ruleId: Long, limitMinutes: Int) {
        viewModelScope.launch {
            repository.setAppLimit(ruleId, limitMinutes)
        }
    }

    fun toggleAlwaysAllowed(ruleId: Long, isAlwaysAllowed: Boolean) {
        viewModelScope.launch {
            repository.toggleAlwaysAllowed(ruleId, isAlwaysAllowed)
        }
    }

    fun updateChildSettings(profile: ChildProfile) {
        viewModelScope.launch {
            repository.updateChildProfile(profile)
        }
    }

    fun addChildProfile(name: String, age: Int, avatarIndex: Int, dailyLimitMinutes: Int, deviceModel: String = "Infinix X6823C") {
        viewModelScope.launch {
            val newProfile = ChildProfile(
                name = name,
                age = age,
                avatarIndex = avatarIndex,
                deviceModel = deviceModel,
                weekdayLimitMinutes = dailyLimitMinutes,
                weekendLimitMinutes = (dailyLimitMinutes * 1.5).toInt()
            )
            val newId = repository.insertChildProfile(newProfile)
            _selectedChildId.value = newId
        }
    }

    fun addRewardTask(title: String, titleHindi: String, rewardMinutes: Int) {
        val childId = _selectedChildId.value ?: return
        viewModelScope.launch {
            repository.insertRewardTask(
                ScreenRewardTask(
                    childId = childId,
                    title = title,
                    titleHindi = titleHindi,
                    rewardMinutes = rewardMinutes
                )
            )
        }
    }

    fun completeTaskByChild(task: ScreenRewardTask) {
        viewModelScope.launch {
            repository.completeRewardTaskByChild(task)
        }
    }

    fun approveTaskReward(task: ScreenRewardTask) {
        viewModelScope.launch {
            repository.approveRewardTask(task)
        }
    }

    fun deleteTask(taskId: Long) {
        viewModelScope.launch {
            repository.deleteRewardTask(taskId)
        }
    }

    fun addWebFilter(domain: String, category: String) {
        val childId = _selectedChildId.value ?: return
        viewModelScope.launch {
            repository.insertWebFilterRule(
                WebFilterRule(
                    childId = childId,
                    domain = domain.trim().lowercase(),
                    isBlocked = true,
                    category = category
                )
            )
        }
    }

    fun removeWebFilter(ruleId: Long) {
        viewModelScope.launch {
            repository.deleteWebFilterRule(ruleId)
        }
    }

    fun addGeofenceZone(name: String, nameHindi: String, address: String, radius: Int) {
        val childId = _selectedChildId.value ?: return
        viewModelScope.launch {
            repository.insertGeofenceZone(
                GeofenceZone(
                    childId = childId,
                    name = name,
                    nameHindi = nameHindi,
                    address = address,
                    radiusMeters = radius,
                    isSafeZone = true
                )
            )
        }
    }

    fun deleteGeofenceZone(zoneId: Long) {
        viewModelScope.launch {
            repository.deleteGeofenceZone(zoneId)
        }
    }

    fun clearNotifications() {
        val childId = _selectedChildId.value ?: return
        viewModelScope.launch {
            repository.clearAppNotifications(childId)
        }
    }

    fun deleteWhatsAppChat(convoId: Long) {
        viewModelScope.launch {
            repository.deleteWhatsAppConversation(convoId)
        }
    }

    fun logLiveMonitoringEvent(type: String, title: String, titleHi: String, desc: String, descHi: String) {
        val childId = _selectedChildId.value ?: return
        viewModelScope.launch {
            repository.addActivityLog(
                ActivityLogItem(
                    childId = childId,
                    type = type,
                    title = title,
                    titleHindi = titleHi,
                    description = desc,
                    descriptionHindi = descHi
                )
            )
        }
    }

    fun requestExtraTimeByChild(minutes: Int) {
        val child = activeChildProfile.value ?: return
        viewModelScope.launch {
            repository.addActivityLog(
                ActivityLogItem(
                    childId = child.id,
                    type = "REQUEST_TIME",
                    title = "Child requested +$minutes mins extra time",
                    titleHindi = "बच्चे ने +$minutes मिनट अतिरिक्त समय की मांग की",
                    description = "${child.name} is asking for extra screen time",
                    descriptionHindi = "${child.name} ने स्क्रीन समय बढ़ाने का अनुरोध भेजा है"
                )
            )
        }
    }

    fun toggleAntiUninstallProtection(enabled: Boolean, preventSettings: Boolean = true, preventReset: Boolean = true) {
        val childId = _selectedChildId.value ?: return
        viewModelScope.launch {
            repository.setAntiUninstallProtection(childId, enabled, preventSettings, preventReset)
        }
    }

    fun clearCallLogs() {
        val childId = _selectedChildId.value ?: return
        viewModelScope.launch {
            repository.clearCallLogs(childId)
        }
    }

    fun deleteCallLog(callLogId: Long) {
        viewModelScope.launch {
            repository.deleteCallLog(callLogId)
        }
    }

    fun clearLogs() {
        val childId = _selectedChildId.value ?: return
        viewModelScope.launch {
            repository.clearLogs(childId)
        }
    }

    // Authentication Actions
    fun loginWithEmail(email: String, pass: String, onResult: (Boolean, String) -> Unit) {
        if (email.isBlank() || !email.contains("@")) {
            onResult(false, "कृपया सही ईमेल आईडी दर्ज करें (Please enter a valid email)")
            return
        }
        if (pass.length < 4) {
            onResult(false, "पासवर्ड कम से कम 4 अक्षरों का होना चाहिए (Password must be at least 4 chars)")
            return
        }
        viewModelScope.launch {
            val success = repository.loginWithEmail(email, pass)
            if (success) {
                regeneratePairingCode()
                onResult(true, "सफलतापूर्वक लॉगिन हुआ! (Logged in successfully)")
            } else {
                onResult(false, "खाता नहीं मिला या पासवर्ड गलत है। कृपया नया खाता बनाएं (Account not found or incorrect password. Please Sign Up)")
            }
        }
    }

    fun signUpWithEmail(name: String, email: String, pass: String, phone: String = "", antiUninstallPin: String = "1234", onResult: (Boolean, String) -> Unit) {
        if (name.isBlank()) {
            onResult(false, "कृपया अपना नाम दर्ज करें (Please enter your name)")
            return
        }
        if (email.isBlank() || !email.contains("@")) {
            onResult(false, "कृपया सही ईमेल आईडी दर्ज करें (Please enter a valid email)")
            return
        }
        if (pass.length < 4) {
            onResult(false, "पासवर्ड कम से कम 4 अक्षरों का होना चाहिए (Password must be at least 4 chars)")
            return
        }
        viewModelScope.launch {
            val success = repository.signUpWithEmail(name, email, pass, phone, antiUninstallPin)
            if (success) {
                regeneratePairingCode()
                onResult(true, "खाता सफलतापूर्वक बन गया! (Account created successfully)")
            } else {
                onResult(false, "खाता बनाने में त्रुटि (Sign up failed)")
            }
        }
    }

    fun updateAntiUninstallPin(newPin: String) {
        viewModelScope.launch {
            val user = currentUserAccount.value
            if (user != null) {
                repository.signUpWithEmail(user.name, user.email, user.passwordHash, user.phoneNumber, newPin)
            }
        }
    }

    fun loginWithGoogle(email: String, name: String) {
        viewModelScope.launch {
            repository.loginWithGoogle(email, name)
            regeneratePairingCode()
        }
    }

    fun logoutUser() {
        viewModelScope.launch {
            repository.logoutUser()
        }
    }
}
