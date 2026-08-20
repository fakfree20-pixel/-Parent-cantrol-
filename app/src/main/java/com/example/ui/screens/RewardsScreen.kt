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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import com.example.data.model.ScreenRewardTask
import com.example.ui.components.AddTaskDialog
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
import com.example.ui.theme.Terracotta700

@Composable
fun RewardsScreen(
    tasks: List<ScreenRewardTask>,
    childName: String,
    bonusEarnedToday: Int,
    isHindi: Boolean,
    onAddTask: (title: String, titleHindi: String, rewardMinutes: Int) -> Unit,
    onApproveTask: (ScreenRewardTask) -> Unit,
    onDeleteTask: (taskId: Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var showAddTaskDialog by remember { mutableStateOf(false) }

    if (showAddTaskDialog) {
        AddTaskDialog(
            onAddTask = { title, titleHi, mins ->
                onAddTask(title, titleHi, mins)
                showAddTaskDialog = false
            },
            onDismiss = { showAddTaskDialog = false },
            isHindi = isHindi
        )
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddTaskDialog = true },
                containerColor = NaturalGreen700,
                contentColor = Color.White,
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier.testTag("add_reward_task_fab")
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Task")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(NaturalBg)
                .padding(padding)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 12.dp, bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Reward Stats Banner (Natural Tones)
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(1.dp, RoundedCornerShape(24.dp))
                        .border(1.dp, NaturalBorder, RoundedCornerShape(24.dp)),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = NaturalGreen100)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (isHindi) "आज का बोनस स्क्रीन टाइम" else "BONUS SCREEN TIME EARNED",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = NaturalGreen700,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "+$bonusEarnedToday ${if (isHindi) "मिनट" else "minutes"}",
                                fontSize = 26.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = NaturalGreen900
                            )
                            Text(
                                text = if (isHindi) "$childName ने कार्य पूरे करके बोनस पाया" else "Earned by completing productive tasks",
                                fontSize = 12.sp,
                                color = NaturalGreen700
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .clip(RoundedCornerShape(18.dp))
                                .background(Color.White.copy(alpha = 0.7f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.EmojiEvents,
                                contentDescription = null,
                                tint = NaturalGreen700,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    }
                }
            }

            // 2. Pending Approval Section
            val pendingApproval = tasks.filter { it.isCompleted && !it.isApproved }
            if (pendingApproval.isNotEmpty()) {
                item {
                    Text(
                        text = if (isHindi) "स्वीकृति की प्रतीक्षा (${pendingApproval.size})" else "Awaiting Parent Approval (${pendingApproval.size})",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = EarthAmber600
                    )
                }

                items(pendingApproval) { task ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, EarthAmber500.copy(alpha = 0.3f), RoundedCornerShape(20.dp)),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = EarthAmber100.copy(alpha = 0.4f))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(EarthAmber100),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.ThumbUp, contentDescription = null, tint = EarthAmber600, modifier = Modifier.size(20.dp))
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = if (isHindi && task.titleHindi.isNotEmpty()) task.titleHindi else task.title,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = NaturalTextPrimary
                                    )
                                    Text(
                                        text = if (isHindi) "इनाम: +${task.rewardMinutes} मिनट स्क्रीन समय" else "Reward: +${task.rewardMinutes} min screen time",
                                        fontSize = 12.sp,
                                        color = EarthAmber600,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }

                            Button(
                                onClick = { onApproveTask(task) },
                                colors = ButtonDefaults.buttonColors(containerColor = NaturalGreen700),
                                shape = RoundedCornerShape(12.dp),
                                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                            ) {
                                Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(if (isHindi) "स्वीकारें" else "Approve", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // 3. Active Tasks Section
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isHindi) "उपलब्ध कार्य सूची" else "Reward Tasks List",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = NaturalTextPrimary
                    )
                    Text(
                        text = if (isHindi) "बच्चा इन्हें पूरा करके समय कमा सकता है" else "Child completes tasks to earn time",
                        fontSize = 11.sp,
                        color = NaturalTextSecondary
                    )
                }
            }

            val availableTasks = tasks.filter { !it.isCompleted }
            if (availableTasks.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, NaturalBorder, RoundedCornerShape(20.dp)),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = NaturalSurface)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(Icons.Default.Stars, contentDescription = null, tint = NaturalTextTertiary, modifier = Modifier.size(44.dp))
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = if (isHindi) "कोई कार्य नहीं है। '+' बटन से नया कार्य जोड़ें!" else "No tasks right now. Tap '+' to create one!",
                                fontWeight = FontWeight.Medium,
                                fontSize = 13.sp,
                                color = NaturalTextSecondary
                            )
                        }
                    }
                }
            } else {
                items(availableTasks) { task ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(1.dp, RoundedCornerShape(18.dp))
                            .border(1.dp, NaturalBorder, RoundedCornerShape(18.dp)),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = NaturalSurface)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(NaturalGreen100),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        Icons.Default.RadioButtonUnchecked,
                                        contentDescription = null,
                                        tint = NaturalGreen700,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = if (isHindi && task.titleHindi.isNotEmpty()) task.titleHindi else task.title,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = NaturalTextPrimary
                                    )
                                    Text(
                                        text = if (isHindi) "+${task.rewardMinutes} मिनट बोनस" else "+${task.rewardMinutes} mins reward",
                                        fontSize = 12.sp,
                                        color = NaturalGreen700,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }

                            IconButton(onClick = { onDeleteTask(task.id) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = NaturalTextTertiary, modifier = Modifier.size(20.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}
