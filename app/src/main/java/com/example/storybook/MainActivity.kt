package com.example.storybook

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.storybook.ui.theme.StoryBookTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StoryBookTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    val pagerState = rememberPagerState(pageCount = { 3 })
    HorizontalPager(state = pagerState, modifier = modifier) { page ->
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            val imageRes = when(page) {
               0 -> R.drawable.scene1
               1 -> R.drawable.scene2
               else -> R.drawable.scene3
            }

            val text = when(page) {
                0 -> stringResource(R.string.scene1)
                1 -> stringResource(R.string.scene2)
                else -> stringResource(R.string.scene3)
            }

            val isVisible = pagerState.currentPage == page

            Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(animationSpec = tween(800)) + scaleIn(
                        initialScale = 0.9f,
                        animationSpec = tween(800)
                    )
                ) {
                    Image(
                        painter = painterResource(imageRes),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    WordByWordText(
                        text = text,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp)
                    )
                }
            }

        }
    }
}

@Composable
fun WordByWordText(text: String, modifier: Modifier = Modifier) {
    val letters = remember(text) { text.toCharArray() }
    val alphas = remember { letters.map { Animatable(0f) } }

    val perLetterStagger = 20L;

    LaunchedEffect(text) {
        letters.indices.forEach { i ->
            launch {
                delay(i * perLetterStagger) // staggered per letter
                alphas[i].animateTo(1f, tween(300))
            }
        }
    }

    Text(
        buildAnnotatedString {
            letters.forEachIndexed { i, c ->
                withStyle(
                    SpanStyle(
                        color = Color.White.copy(alpha = alphas[i].value),
                        shadow = Shadow(
                            color = Color.Black.copy(alpha = alphas[i].value * 0.8f),
                            offset = Offset(2f, 2f),
                            blurRadius = 6f
                        ))
                ) { append(c) }
            }
        },
        fontSize = 20.sp,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    StoryBookTheme {
        Greeting("Android")
    }
}