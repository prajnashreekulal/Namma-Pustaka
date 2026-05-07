package com.nammapustaka.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nammapustaka.data.Student
import com.nammapustaka.ui.BookViewModel

@Composable
fun LeaderboardScreen(viewModel: BookViewModel) {
    val leaderboard by viewModel.leaderboard.collectAsState()
    val myRank by viewModel.currentStudentRank.collectAsState()
    val totalUsers by viewModel.totalUsers.collectAsState()
    val currentStudent by viewModel.currentStudent.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Leaderboard", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(16.dp))

        // Personal Stats Panel
        Surface(
            color = MaterialTheme.colorScheme.primaryContainer,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(modifier = Modifier.padding(16.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text("Your Rank", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onPrimaryContainer)
                    val rankDisp = if (myRank > 0) "#$myRank / $totalUsers" else "Unranked"
                    Text(rankDisp, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("Your Score", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onPrimaryContainer)
                    Text("${currentStudent?.score ?: 0} pts", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (leaderboard.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No data yet. Start reading!")
            }
        } else {
            // Podium Framework
            Row(
                modifier = Modifier.fillMaxWidth().height(200.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom
            ) {
                if (leaderboard.size >= 2) PodiumItem(student = leaderboard[1], rank = 2, height = 120, color = Color(0xFFC0C0C0))
                if (leaderboard.isNotEmpty()) PodiumItem(student = leaderboard[0], rank = 1, height = 160, color = Color(0xFFFFD700))
                if (leaderboard.size >= 3) PodiumItem(student = leaderboard[2], rank = 3, height = 90, color = Color(0xFFCD7F32))
            }
            Spacer(modifier = Modifier.height(24.dp))
            
            // Scalable List for others
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                itemsIndexed(leaderboard.drop(3)) { index, student ->
                    StudentRankRow(student = student, rank = index + 4, isMe = (student.id == currentStudent?.id))
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                }
            }
        }
    }
}

@Composable
fun PodiumItem(student: Student, rank: Int, height: Int, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(90.dp).padding(horizontal = 4.dp)) {
        Text(
            text = student.name,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyMedium
        )
        Text("${student.score} pts", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.height(8.dp))
        Surface(
            modifier = Modifier
                .width(80.dp)
                .height(height.dp),
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            color = color,
            shadowElevation = 8.dp
        ) {
            Box(contentAlignment = Alignment.TopCenter) {
                Text("$rank", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold, modifier = Modifier.padding(top = 12.dp), color = Color.White)
            }
        }
    }
}

@Composable
fun StudentRankRow(student: Student, rank: Int, isMe: Boolean) {
    val rowBg = if (isMe) MaterialTheme.colorScheme.secondaryContainer else Color.Transparent
    val onRowBg = if (isMe) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onSurface

    Row(
        modifier = Modifier.fillMaxWidth().background(rowBg, RoundedCornerShape(8.dp)).padding(horizontal = 8.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(MaterialTheme.colorScheme.surfaceVariant), contentAlignment = Alignment.Center) {
            Text("$rank", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(student.name + if (isMe) " (You)" else "", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = onRowBg)
            Text("Books Read: ${student.booksRead}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Text("${student.score} pts", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.titleMedium)
    }
}
