package com.feliii.alpvp.view

import android.widget.Space
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun WhackAMoleMenu() {
    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Text at the top
        Text(
            text = "Whack-A-Mole",
            modifier = Modifier
                .padding(top = 64.dp, bottom = 270.dp) // Adjust the padding for spacing from the top
        )
        // Buttons in the middle
        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { /*TODO*/ },
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Timed Mode")
                    Text(text = "0")
                }
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { /*TODO*/ },
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Endless Mode")
                    Text(text = "0")
                }
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { /*TODO*/ },
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Intense Mode")
                    Text(text = "0")
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f)) // Push content up from the bottom
    }
}




@Composable
@Preview(showBackground = true, showSystemUi = true)
fun WhackAMoleMenuPreview() {
    WhackAMoleMenu()
}