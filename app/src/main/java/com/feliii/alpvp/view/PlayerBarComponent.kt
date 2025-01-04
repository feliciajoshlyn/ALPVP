package com.feliii.alpvp.view

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MusicPlayerBar(
    currentSong: String,
    isMuted: Boolean,
    onSkip: () -> Unit,
    onMuteToggle: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFD7C4EC), shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Music Icon
        Text(
            text = "🎵",
            fontSize = 24.sp
        )

        // Current song name
        Text(
            text = if (currentSong.isEmpty()) "No Song Playing" else "Playing - $currentSong",
            fontSize = 16.sp,
            color = Color.Black,
            modifier = Modifier.weight(1f).padding(start = 8.dp)
        )

        // Skip Button
        Button(
            onClick = onSkip,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5E4890))
        ) {
            Text(text = "⏭ Skip", color = Color.White)
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Mute/Unmute Button
        Button(
            onClick = onMuteToggle,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5E4890))
        ) {
            Text(
                text = if (isMuted) "🔇 Mute" else "🔊 Unmute",
                color = Color.White
            )
        }
    }
}
