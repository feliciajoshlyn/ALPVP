package com.feliii.alpvp.view

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.feliii.alpvp.viewmodel.WAMViewModel
import com.feliii.alpvp.R

@Composable
fun MusicPlayerBar(
    wamViewModel: WAMViewModel,
    context: Context
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Color(0xFFD7C4EC),
                shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "🎵",
            fontSize = 24.sp
        )

        //song name
        Text(
            text = "Playing - ${wamViewModel.chosenSong.name}",
            fontSize = 16.sp,
            fontFamily = FontFamily(Font(R.font.jua)),
            color = Color.Black,
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
        )

        //skip button
        Button(
            onClick = { wamViewModel.skipSong(context) },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5E4890))
        ) {
            Text(text = "⏭", color = Color.White)
        }

        Spacer(modifier = Modifier.width(8.dp))

        //play pause button
        Button(
            onClick = { wamViewModel.onToggleSong() },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5E4890))
        ) {
            Text(
                text = if (wamViewModel.boolIsPlaying) "🔇 Pause" else "🔊 Play",
                color = Color.White
            )
        }
    }
}
