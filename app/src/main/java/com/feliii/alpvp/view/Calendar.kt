package com.feliii.alpvp.view

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DynamicMoodCalendar() {
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    val daysInMonth = currentMonth.lengthOfMonth()
    val firstDayOfMonth = currentMonth.atDay(1).dayOfWeek.value % 7 // Monday is 0, Sunday is 6
    val totalGridCells = 42 // 6 weeks x 7 days for consistent grid size

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF9370DB)) // Purple background
            .padding(16.dp)
    ) {
        // Header
        Text(
            text = "Mood Calendar",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Calendar Header (Month Navigation)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = {
                currentMonth = currentMonth.minusMonths(1) // Navigate to previous month
            }) {
                Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
            }
            Text(
                text = "${currentMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault())} ${currentMonth.year}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
            IconButton(onClick = {
                currentMonth = currentMonth.plusMonths(1) // Navigate to next month
            }) {
                Icon(Icons.Default.ArrowForward, contentDescription = null, tint = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Weekday Labels
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            listOf("Mo", "Tu", "We", "Th", "Fr", "Sa", "Su").forEach { day ->
                Text(
                    text = day,
                    fontSize = 14.sp,
                    color = Color.White,
                    modifier = Modifier.weight(1f),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Calendar Grid
        val daysList = List(totalGridCells) { index ->
            if (index >= firstDayOfMonth && index < firstDayOfMonth + daysInMonth) {
                (index - firstDayOfMonth + 1).toString() // Valid day
            } else {
                "" // Empty day
            }
        }

        Column {
            daysList.chunked(7).forEach { week ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    week.forEach { day ->
                        if (day.isNotEmpty()) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(Color.White, shape = CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = day, color = Color(0xFF9370DB))
                            }
                        } else {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(Color.White, shape = CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = day, color = Color.Transparent)
                            }
                            //Spacer(modifier = Modifier.size(40.dp)) // space for blank day
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Add Button
        Box(
            modifier = Modifier
                .size(56.dp)
                .background(Color.White, shape = CircleShape)
                .align(Alignment.CenterHorizontally),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Add, contentDescription = null, tint = Color(0xFF9370DB))
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showSystemUi = true, showBackground = true)
@Composable
fun CalendarPreview() {
    DynamicMoodCalendar()
}