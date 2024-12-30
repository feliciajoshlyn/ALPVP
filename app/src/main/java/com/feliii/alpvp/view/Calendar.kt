package com.feliii.alpvp.view

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.feliii.alpvp.R
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DynamicMoodCalendar(
    token: String,
    modifier: Modifier = Modifier,
    navController: NavHostController,
    context: Context
) {
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    val daysInMonth = currentMonth.lengthOfMonth()
    val firstDayOfMonth = (currentMonth.atDay(1).dayOfWeek.value + 6) % 7


    Box (
        modifier = Modifier .fillMaxSize()
            .background(Color(0xFF5E4890))
            .padding(16.dp),
    ){
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Title
            Text(
                text = "Mood Calendar",
                fontSize = 40.sp,
                color = Color(0xFF5E4890),
                fontFamily = FontFamily(Font(R.font.jua)),
                modifier = Modifier.clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFFD7C4EC))
                    .padding(16.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))

            Column (
                modifier = Modifier.clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFFD7C4EC))
                    .padding(16.dp)
            ){
                // Month Navigation
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(onClick = {
                        currentMonth = currentMonth.minusMonths(1) // Navigate to previous month
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color(0xFF5E4890))
                    }
                    Text(
                        text = "${currentMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault())} ${currentMonth.year}",
                        fontSize = 24.sp,
                        color = Color(0xFF5E4890),
                        fontFamily = FontFamily(Font(R.font.jua)),
                    )
                    IconButton(onClick = {
                        currentMonth = currentMonth.plusMonths(1) // Navigate to next month
                    }) {
                        Icon(Icons.Default.ArrowForward, contentDescription = null, tint = Color(0xFF5E4890))
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
                            fontSize = 20.sp,
                            color = Color(0xFF5E4890),
                            fontFamily = FontFamily(Font(R.font.jua)),
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Calendar Grids
                val daysList = List(42 /* 6 weeks x 7 days */) { index ->
                    if (index >= firstDayOfMonth && index < firstDayOfMonth + daysInMonth) {
                        (index - firstDayOfMonth + 1).toString() // Valid day
                    } else {
                        "" //Empty
                    }
                }

                Column {
                    daysList.chunked(7).forEach { week ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            week.forEach { day ->
                                Box(
                                    modifier = Modifier.clip(RoundedCornerShape(12.dp))
                                        .size(40.dp)
                                        .background(Color.White),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = day,
                                        color = Color(0xFF9370DB),
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily(Font(R.font.jua)),
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }

        // Add Emotion Button
        Box(
            modifier = Modifier
                .offset(y = -20.dp)
                .size(76.dp)
                .background(Color.White, shape = CircleShape)
                .align(Alignment.BottomEnd),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.Add,
                contentDescription = null,
                tint = Color(0xFF9370DB),
                modifier = Modifier.size(36.dp)
            )
        }
    }
}



//@RequiresApi(Build.VERSION_CODES.O)
//@Preview(showSystemUi = true, showBackground = true)
//@Composable
//fun CalendarPreview() {
//    DynamicMoodCalendar()
//}