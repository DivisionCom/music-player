package com.example.musicplayer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.musicplayer.ui.theme.MusicPlayerTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    private val colors = listOf(
        Color(0xFFFF5A5A),
        Color(0xFFFF9C5A),
        Color(0xFFFFDB5A),
        Color(0xFFCEFF5A),
        Color(0xFF73FF5A),
        Color(0xFF5AFFBD),
        Color(0xFF5A9CFF),
        Color(0xFFC55AFF),
        Color(0xFFFF5AAF),
    )
    private val darkColors = listOf(
        Color(0xFF8F2020),
        Color(0xFF8D491C),
        Color(0xFF8D741C),
        Color(0xFF648319),
        Color(0xFF2D861D),
        Color(0xFF188559),
        Color(0xFF183E77),
        Color(0xFF4E146D),
        Color(0xFF721545),
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MusicPlayerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val uiController = rememberSystemUiController()

                    val colorIndex = remember {
                        mutableIntStateOf(0)
                    }
                    LaunchedEffect(Unit) {
                        colorIndex.intValue += 1
                    }
                    LaunchedEffect(colorIndex.intValue) {
                        delay(2100)
                        if (colorIndex.intValue < darkColors.lastIndex) {
                            colorIndex.intValue += 1
                        } else {
                            colorIndex.intValue = 0
                        }
                    }
                    val animatedColor by animateColorAsState(
                        targetValue = colors[colorIndex.intValue],
                        animationSpec = tween(2000),
                        label = ""
                    )
                    val animatedDarkColor by animateColorAsState(
                        targetValue = darkColors[colorIndex.intValue],
                        animationSpec = tween(2000),
                        label = ""
                    )
                    uiController.setStatusBarColor(animatedColor, darkIcons = false)
                    uiController.setNavigationBarColor(animatedColor)

                    Box(modifier = Modifier
                        .fillMaxSize()
                        .background(Brush.linearGradient(
                            listOf(
                                animatedColor,
                                animatedDarkColor
                            )
                        )), contentAlignment = Alignment.Center){

                    }
                }
            }
        }
    }
}