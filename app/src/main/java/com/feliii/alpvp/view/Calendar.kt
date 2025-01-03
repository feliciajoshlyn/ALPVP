package com.feliii.alpvp.view

import androidx.compose.foundation.border
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.feliii.alpvp.R
import com.feliii.alpvp.enums.PagesEnum
import com.feliii.alpvp.uiStates.CalendarDataStatusUIState
import com.feliii.alpvp.viewmodel.CalendarDetailViewModel
import com.feliii.alpvp.viewmodel.CalendarViewModel
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MoodCalendar(
    token: String,
    modifier: Modifier = Modifier,
    navController: NavHostController,
    calendarViewModel: CalendarViewModel,
    calendarDetailViewModel: CalendarDetailViewModel,
    context: Context
) {
    val dataStatus = calendarViewModel.dataStatus
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    val today = LocalDate.now()
    val daysInMonth = currentMonth.lengthOfMonth()
    val firstDayOfMonth = (currentMonth.atDay(1).dayOfWeek.value + 6) % 7
    val isFutureDate = selectedDate.isAfter(today)

    val daysList = List(42) { index ->
        if (index >= firstDayOfMonth && index < firstDayOfMonth + daysInMonth) {
            (index - firstDayOfMonth + 1).toString()
        } else {
            ""
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF5E4890))
            .padding(16.dp)
    ) {
        // Back button
        Image(
            painter = painterResource(R.drawable.back_button),
            contentDescription = null,
            modifier = Modifier
                .clip(CircleShape)
                .size(52.dp)
                .clickable {
                    navController.navigate(PagesEnum.Home.name){
                        popUpTo(PagesEnum.Home.name){
                            inclusive = true
                        }
                    }
                }
                .padding(2.dp)
                .align(Alignment.TopStart)
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Mood Calendar",
                fontSize = 40.sp,
                color = Color(0xFF5E4890),
                fontFamily = FontFamily(Font(R.font.jua)),
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFFD7C4EC))
                    .padding(16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            Column (
                Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFFD7C4EC))
                    .padding(16.dp)
                    .fillMaxWidth()
            ){
                MonthNavigation(currentMonth, onMonthChange = { newMonth ->
                    currentMonth = newMonth
                    //reset selected date when month changes
                    if (selectedDate.month != newMonth.month) {
                        selectedDate = LocalDate.now()
                    }
                })
                Spacer(modifier = Modifier.height(16.dp))
                CalendarGrid(
                    daysList = daysList,
                    currentMonth = currentMonth,
                    today = today,
                    selectedDate = selectedDate,
                    onDateSelected = { date -> selectedDate = date },
                    dataStatus = dataStatus
                )
            }
        }

        AddEmotionButton(
            calendarDetailViewModel = calendarDetailViewModel,
            dateChosen = selectedDate.toString(),
            navController = navController,
            token = token,
            isEnabled = !isFutureDate,
            modifier = Modifier.align(Alignment.BottomEnd)
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarGrid(
    daysList: List<String>,
    currentMonth: YearMonth,
    today: LocalDate,
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    dataStatus: CalendarDataStatusUIState
) {
    Column {
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


        daysList.chunked(7).forEach { week ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                week.forEach { day ->
                    if (day.isEmpty()) {
                        // Empty date square
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .size(40.dp)
                                .background(Color(0xFFF3EDF9)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = day,
                                fontSize = 20.sp,
                            )
                        }
                    } else {
                        val date = LocalDate.of(currentMonth.year, currentMonth.monthValue, day.toInt())
                        val isToday = date == today
                        val isSelected = date == selectedDate
                        val dayData = (dataStatus as? CalendarDataStatusUIState.Success)?.data?.find { it.date.equals(date) }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .size(40.dp)
                                .background(if (isToday) Color(0xFFFFE082) else Color.White)
                                .clickable { onDateSelected(date) }
                                .then(
                                    if (isSelected) {
                                        Modifier.border(
                                            width = 2.dp,
                                            color = Color(0xFF9370DB),
                                            shape = RoundedCornerShape(12.dp)
                                        )
                                    } else Modifier
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = day,
                                    color = Color(0xFF9370DB),
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily(Font(R.font.jua))
                                )

                                Row (
                                    verticalAlignment = Alignment.CenterVertically
                                ){
                                    dayData?.moods?.forEach { mood ->
                                        val emojiResource = when (mood) {
                                            0 -> R.drawable.happy_emoji
                                            1 -> R.drawable.chill_emoji
                                            2 -> R.drawable.neutral_emoji
                                            3 -> R.drawable.sad_emoji
                                            4 -> R.drawable.angry_emoji
                                            else -> null
                                        }
                                        emojiResource?.let { res ->
                                            Image(
                                                painter = painterResource(res),
                                                contentDescription = null,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }
                                }

                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun AddEmotionButton(
    calendarDetailViewModel: CalendarDetailViewModel,
    dateChosen: String,
    navController: NavHostController,
    token: String,
    isEnabled: Boolean,
    modifier: Modifier
) {
    Box(
        modifier = modifier
            .offset(y = -20.dp)
            .size(76.dp)
            .background(
                if (isEnabled) Color.White else Color.Gray,
                shape = CircleShape
            )
            .clip(CircleShape)
            .clickable(enabled = isEnabled) {
                calendarDetailViewModel.getCalendarDetailData(
                    navController = navController,
                    date = dateChosen,
                    token = token
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            Icons.Default.Add,
            contentDescription = null,
            tint = if (isEnabled) Color(0xFF9370DB) else Color.DarkGray,
            modifier = Modifier.size(36.dp)
        )
    }
}



@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MonthNavigation(
    currentMonth: YearMonth,
    onMonthChange: (YearMonth) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        IconButton(onClick = { onMonthChange(currentMonth.minusMonths(1)) }) {
            Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color(0xFF5E4890))
        }

        Text(
            text = "${currentMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault())} ${currentMonth.year}",
            fontSize = 24.sp,
            color = Color(0xFF5E4890),
            fontFamily = FontFamily(Font(R.font.jua))
        )

        IconButton(onClick = { onMonthChange(currentMonth.plusMonths(1)) }) {
            Icon(Icons.Default.ArrowForward, contentDescription = null, tint = Color(0xFF5E4890))
        }
    }
}


//@RequiresApi(Build.VERSION_CODES.O)
//@Preview(showSystemUi = true, showBackground = true)
//@Composable
//fun CalendarPreview() {
//    MoodCalendar()
//}