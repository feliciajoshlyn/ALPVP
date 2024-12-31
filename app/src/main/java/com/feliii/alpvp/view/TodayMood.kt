package com.feliii.alpvp.view

import android.os.Build
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.feliii.alpvp.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TodayMood(
    
) {
    var selectedMoods by remember { mutableStateOf(mutableListOf<Int>()) }
    var note by remember { mutableStateOf("") }

    // Get the current date
    val currentDate = LocalDate.now()
    val formattedDate = currentDate.format(DateTimeFormatter.ofPattern("EEEE\nd MMMM yyyy"))

    //Emotion list
    val moods = listOf(
        R.drawable.angry_emoji,
        R.drawable.happy_emoji,
        R.drawable.sad_emoji,
        R.drawable.chill_emoji,
        R.drawable.neutral_emoji
    )

    Box(
        modifier = Modifier.fillMaxSize()
            .background(Color(0xFF5E4890))
            .padding(16.dp),
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

            Column(
                modifier = Modifier.clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFFD7C4EC))
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "$formattedDate",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF5C4C9C),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Select your mood (${3 - selectedMoods.size})",
                    fontSize = 14.sp,
                    color = Color(0xFF5C4C9C)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        moods.forEachIndexed { index, mood ->
                            if (index >= 3) return@forEachIndexed

                            MoodImage(
                                moodId = index,
                                drawableRes = mood,
                                selectedMoods = selectedMoods,
                                onMoodClick = { clickedMoodId ->
                                    if (selectedMoods.contains(clickedMoodId)) {
                                        selectedMoods = selectedMoods.toMutableList().apply { remove(clickedMoodId) }
                                    } else if (selectedMoods.size < 3) {
                                        selectedMoods = selectedMoods.toMutableList().apply { add(clickedMoodId) }
                                    }
                                }
                            )
                        }
                    }
                    Row(

                    ) {

                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                TextField(
                    value = note,
                    onValueChange = { note = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    placeholder = { Text("Leave a note") },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0XFFF6EDFF),
                        unfocusedContainerColor = Color(0XFFFAF4FF),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))

            }

            Button(
                onClick = {
                    /* Save logic here... */
                },
                modifier = Modifier.align(Alignment.End),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFFFFF))
            ) {
                Text(
                    text = "Save",
                    fontSize = 16.sp,
                    color = Color(0xFFE6DFFF)
                )
            }
        }
    }
}

@Composable
fun MoodImage(
    moodId: Int,
    drawableRes: Int,
    selectedMoods: List<Int>,
    onMoodClick: (Int) -> Unit
) {
    Image(
        painter = painterResource(drawableRes),
        contentDescription = null,
        modifier = Modifier
            .clip(CircleShape)
            .size(72.dp)
            .background(if (selectedMoods.contains(moodId)) Color(0xFF983939) else Color(0xFFFF8888))
            .clickable { onMoodClick(moodId) }
            .alpha(if (selectedMoods.contains(moodId)) 0.7f else 1f)
            .padding(2.dp)
    )
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showSystemUi = true, showBackground = true)
@Composable
fun CalendarPreview() {
    TodayMood()
}