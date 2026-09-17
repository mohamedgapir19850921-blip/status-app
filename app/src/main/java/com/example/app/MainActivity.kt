package com.example.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    HomeScreen()
                }
            }
        }
    }
}

@Composable
fun HomeScreen() {
    val statuses = listOf(
        "الدنيا علمتني إني ماشتكيش، وإن شكيت أشكي لربنا بس.",
        "ماتقولش ظروفي صعبة، قول الحمد لله على كل حال.",
        "اللي بيروح لحد ما يجيش، مش خسارة إنك تسيبه.",
        "مش كل اللي بيضحك مبسوط، ومش كل اللي ساكت زعلان."
    )

    var index by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = statuses[index],
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = { index = (index + 1) % statuses.size }) {
            Text("حالة جديدة")
        }
    }
}
