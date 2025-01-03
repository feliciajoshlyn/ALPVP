package com.feliii.alpvp.view

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.feliii.alpvp.R
import com.feliii.alpvp.uiStates.CalendarDetailDataStatusUIState
import com.feliii.alpvp.uiStates.StringDataStatusUIState
import com.feliii.alpvp.viewmodel.CalendarDetailViewModel
import com.feliii.alpvp.viewmodel.CalendarViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TodayMood(
    token: String,
    modifier: Modifier = Modifier,
    navController: NavHostController,
    calendarDetailViewModel: CalendarDetailViewModel,
    context: Context
) {

    LaunchedEffect(calendarDetailViewModel.dataStatus) {
        val dataStatus = calendarDetailViewModel.dataStatus
        if(dataStatus is CalendarDetailDataStatusUIState.Failed) {
            Toast.makeText(context, dataStatus.errorMessage, Toast.LENGTH_SHORT).show()
        }
    }

    // Mood list
    val moods = listOf(
        R.drawable.happy_emoji,
        R.drawable.chill_emoji,
        R.drawable.neutral_emoji,
        R.drawable.sad_emoji,
        R.drawable.angry_emoji,
    )
    // Mood Color (unclicked, clicked)
    val moodColor = listOf(
        Pair(Color(0xFFF6F6D4), Color(0xFFE2C873)), //Yellow Happy
        Pair(Color(0xFFA3E3B4), Color(0xFF63C17C)), //Green Chill
        Pair(Color(0xFFE6E6E6), Color(0xFFBBBBBB)), //Gray Neutral
        Pair(Color(0xFF87C0FC), Color(0xFF4E75B0)), //Blue Sad
        Pair(Color(0xFFFF8888), Color(0xFF983939)), //Red Angry
    )

    Box(
        modifier = Modifier.fillMaxSize()
            .background(Color(0xFF5E4890))
            .padding(24.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Title
            Text(
                text = "Today's Mood",
                fontSize = 40.sp,
                color = Color(0xFF5E4890),
                fontFamily = FontFamily(Font(R.font.jua)),
                modifier = Modifier.clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFFD7C4EC))
                    .padding(16.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))

            // main
            Column(
                modifier = Modifier.clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFFD7C4EC))
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Week, Day Month 20XX
                Column (
                    modifier = Modifier.fillMaxWidth()
                ){
                    // Week
                    Text(
                        text = calendarDetailViewModel.dayOfWeek,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF5C4C9C),
                        fontFamily = FontFamily(Font(R.font.jua)),
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    // Day Month 20XX
                    Text(
                        text = calendarDetailViewModel.monthYear,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF5C4C9C),
                        fontFamily = FontFamily(Font(R.font.jua)),
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

                HorizontalDivider(
                    thickness = 3.dp,
                    color = Color(0xFF5C4C9C)
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Select mood (3)
                Text(
                    text = "Select your mood (${calendarDetailViewModel.selectableMood})",
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 18.sp,
                    color = Color(0xFF5C4C9C),
                    fontFamily = FontFamily(Font(R.font.jua))
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Mood buttons
                Column {
                    // Row of Happy, Chill, Neutral
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        moods.forEachIndexed { index, mood ->
                            if (index >= 3) return@forEachIndexed

                            MoodButton(
                                moodId = index,
                                drawableRes = mood,
                                selectedMoods = calendarDetailViewModel.moodChosen,
                                unclickedMood = moodColor.get(index).first,
                                clickedMood = moodColor.get(index).second,
                                onMoodClick = { calendarDetailViewModel.selectMood(it)}
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    // Row of Sad, Angry
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        for (i in 0..1) {
                            val iPlus3 = i + 3

                            MoodButton(
                                moodId = iPlus3,
                                drawableRes = moods.get(iPlus3),
                                selectedMoods = calendarDetailViewModel.moodChosen,
                                unclickedMood = moodColor.get(iPlus3).first,
                                clickedMood = moodColor.get(iPlus3).second,
                                onMoodClick = { calendarDetailViewModel.selectMood(it)}
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                // leave a note textfield
                TextField(
                    value = calendarDetailViewModel.note,
                    onValueChange = { calendarDetailViewModel.changeNote(it) },
                    modifier = Modifier.clip(RoundedCornerShape(12.dp))
                        .fillMaxWidth()
                        .height(100.dp),
                    placeholder = {
                        Text(
                            text = "Leave a note...",
                            color = Color(0xFF5C4C9C),
                            fontFamily = FontFamily(Font(R.font.jua)),
                            fontSize = 18.sp
                        )
                    },
                    textStyle = TextStyle(
                        fontFamily = FontFamily(Font(R.font.jua)),
                        fontSize = 16.sp,
                        color = Color.Black
                    ),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0XFFF6EDFF),
                        unfocusedContainerColor = Color(0XFFFAF4FF),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))

            }
            Spacer(modifier = Modifier.height(24.dp))

            // "Save" button
            Button(
                onClick = { calendarDetailViewModel.saveButton(token, navController) },
                modifier = Modifier.align(Alignment.End),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFD7C4EC)
                )
            ) {
                Text(
                    text = "Save",
                    fontSize = 28.sp,
                    color = Color(0xFF5E4890),
                    fontFamily = FontFamily(Font(R.font.jua)),
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
                )
            }
        }
    }
}

// Mood button
@Composable
fun MoodButton(
    moodId: Int,
    drawableRes: Int,
    unclickedMood: Color,
    clickedMood: Color,
    selectedMoods: List<Int>,
    onMoodClick: (Int) -> Unit
) {
    Image(
        painter = painterResource(drawableRes),
        contentDescription = null,
        modifier = Modifier
            .clip(CircleShape)
            .size(72.dp)
            .background(if (selectedMoods.contains(moodId)) clickedMood else unclickedMood)
            .clickable { onMoodClick(moodId) }
            .alpha(if (selectedMoods.contains(moodId)) 0.7f else 1f)
            .padding(2.dp)
    )
}

//@RequiresApi(Build.VERSION_CODES.O)
//@Preview(showSystemUi = true, showBackground = true)
//@Composable
//fun CalendarPreview() {
//    TodayMood()
//}