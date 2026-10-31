package com.example.animatedhello

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.systemBarsPadding

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(colorScheme = darkColorScheme()) {
                AnimatedHelloScreen()
            }
        }
    }
}

@Composable
private fun AnimatedHelloScreen() {
    var replayCount by remember { mutableIntStateOf(0) }
    val motion = rememberInfiniteTransition(label = "ambient motion")
    val colorProgress by motion.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 5000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "background colors"
    )
    val floatOffset by motion.animateFloat(
        initialValue = -10f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "floating emblem"
    )
    val starRotation by motion.animateFloat(
        initialValue = -12f,
        targetValue = 12f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "star rotation"
    )

    val topColor = lerp(Color(0xFF111B3D), Color(0xFF31205C), colorProgress)
    val bottomColor = lerp(Color(0xFF101928), Color(0xFF123B52), colorProgress)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(topColor, bottomColor)))
            .systemBarsPadding()
            .padding(horizontal = 28.dp, vertical = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "A LITTLE HELLO",
                color = Color(0xFFB8C7F5),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 3.sp
            )

            Spacer(Modifier.height(38.dp))

            Box(
                modifier = Modifier
                    .size(174.dp)
                    .offset(y = floatOffset.dp)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(Color(0xFF5969D9), Color(0xFF353B83), Color(0x00353B83))
                        ),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(116.dp)
                        .background(Color(0xFF222B58), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "✦",
                        modifier = Modifier
                            .scale(1.08f)
                            .rotate(starRotation),
                        color = Color(0xFFFFD979),
                        fontSize = 66.sp,
                        lineHeight = 72.sp
                    )
                }
            }

            Spacer(Modifier.height(34.dp))

            AnimatedContent(
                targetState = replayCount,
                transitionSpec = {
                    (fadeIn(tween(350)) + scaleIn(initialScale = 0.82f, animationSpec = tween(350))) togetherWith
                        (fadeOut(tween(180)) + scaleOut(targetScale = 0.88f, animationSpec = tween(180)))
                },
                label = "hello replay"
            ) { _ ->
                Text(
                    text = "Hello, World!",
                    color = Color.White,
                    fontSize = 34.sp,
                    lineHeight = 42.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(Modifier.height(12.dp))
            Text(
                text = "A tiny moment of brightness,\njust for you.",
                color = Color(0xFFCCD3E8),
                fontSize = 16.sp,
                lineHeight = 24.sp,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(36.dp))
            Button(
                onClick = { replayCount++ },
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFD979),
                    contentColor = Color(0xFF242445)
                ),
                modifier = Modifier.height(54.dp)
            ) {
                Text(
                    text = "Say hello again  ✨",
                    modifier = Modifier.padding(horizontal = 12.dp),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
