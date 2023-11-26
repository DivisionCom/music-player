package com.example.musicplayer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.fontscaling.MathUtils.lerp
import androidx.compose.ui.unit.sp
import com.example.musicplayer.ui.theme.MusicPlayerTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.delay
import kotlin.math.absoluteValue

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
    private val musics = listOf(
        Music(
            name = "See you again",
            cover = R.drawable.cover1,
            music = R.raw.music1
        ),
        Music(
            name = "Heat waves",
            cover = R.drawable.cover2,
            music = R.raw.music2
        ),
        Music(
            name = "Close eyes",
            cover = R.drawable.cover3,
            music = R.raw.music3
        ),
    )


    @OptIn(ExperimentalFoundationApi::class)
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

                    val pagerState = rememberPagerState(pageCount = { musics.count() })
                    val playingIndex = remember {
                        mutableIntStateOf(0)
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.linearGradient(
                                    listOf(
                                        animatedColor,
                                        animatedDarkColor
                                    )
                                )
                            ), contentAlignment = Alignment.Center
                    ) {
                        val configuration = LocalConfiguration.current


                        Column(horizontalAlignment = Alignment.CenterHorizontally) {

                            Text(text = musics[playingIndex.intValue].name, fontSize = 52.sp,
                                color = Color.White)
                            Spacer(modifier = Modifier.height(32.dp))

                            HorizontalPager(
                                modifier = Modifier.fillMaxWidth(),
                                state = pagerState,
                                pageSize = PageSize.Fixed((configuration.screenWidthDp / (1.7)).dp),
                                contentPadding = PaddingValues(horizontal = 85.dp)
                            ) { page ->
                                Card(
                                    modifier = Modifier
                                        .size((configuration.screenWidthDp / (1.7)).dp)
                                        .graphicsLayer {
                                            val pageOffset = (
                                                    (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
                                                    ).absoluteValue
                                            val alphaLerp = lerp(
                                                start = 0.4f,
                                                stop = 1f,
                                                amount = 1f - pageOffset.coerceIn(0f, 1f)
                                            )
                                            val scaleLerp = lerp(
                                                start = 0.5f,
                                                stop = 1f,
                                                amount = 1f - pageOffset.coerceIn(0f, .5f)
                                            )
                                            alpha = alphaLerp
                                            scaleX = scaleLerp
                                            scaleY = scaleLerp
                                        }
                                        .border(2.dp, Color.White, CircleShape)
                                        .padding(6.dp),
                                    shape = CircleShape
                                ) {
                                    Image(
                                        painter = painterResource(id = musics[page].cover),
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }


                            }

                        }

                    }
                }
            }
        }
    }
}