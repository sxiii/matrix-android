package com.example.matrixwallpaper

import android.app.WallpaperManager
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(colorScheme = darkColorScheme()) {
                MainScreen()
            }
        }
    }
}

@Composable
private fun MainScreen() {
    val context = LocalContext.current
    var selectedId by remember { mutableStateOf(ThemePrefs.load(context)) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF050510))
            .systemBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "MATRIX WALLPAPER",
                color = Color(0xFF00FF46),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                letterSpacing = 4.sp
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = "Choose a theme",
                color = Color(0xFF88AA88),
                fontSize = 14.sp,
                fontFamily = FontFamily.Monospace
            )

            Spacer(Modifier.height(18.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(4.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(MatrixThemes.all) { theme ->
                    ThemeCard(
                        theme = theme,
                        isSelected = theme.id == selectedId,
                        onClick = {
                            selectedId = theme.id
                            ThemePrefs.save(context, theme.id)
                        }
                    )
                }
            }

            Spacer(Modifier.height(14.dp))

            Button(
                onClick = {
                    ThemePrefs.save(context, selectedId)
                    val intent = Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER).apply {
                        putExtra(
                            WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                            ComponentName(context, MatrixWallpaperService::class.java)
                        )
                    }
                    context.startActivity(intent)
                },
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF00CC3A),
                    contentColor = Color.Black
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(
                    text = "Set as Live Wallpaper",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
            }

            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
private fun ThemeCard(
    theme: MatrixTheme,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (isSelected) Color(0xFF00FF46) else Color(0xFF333333)
    val borderWidth = if (isSelected) 2.dp else 1.dp

    val transition = rememberInfiniteTransition(label = "rain_${theme.id}")
    val offset by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "offset_${theme.id}"
    )

    // Pre-generate stable column data
    val columnData = remember(theme.id) {
        val cols = 8
        List(cols) { col ->
            val chars = List(12) { theme.charSet[Random.nextInt(theme.charSet.length)] }
            val speed = 0.6f + Random.nextFloat() * 0.8f
            val startY = Random.nextFloat()
            Triple(col, chars, speed to startY)
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .border(borderWidth, borderColor, RoundedCornerShape(12.dp))
            .background(Color(theme.background))
            .clickable { onClick() }
            .padding(bottom = 10.dp)
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
        ) {
            drawMatrixPreview(theme, columnData, offset)
        }

        Spacer(Modifier.height(6.dp))

        Text(
            text = theme.displayName,
            color = Color(theme.bodyColor),
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            textAlign = TextAlign.Center
        )

        if (isSelected) {
            Text(
                text = "✓ Selected",
                color = Color(0xFF00FF46),
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace
            )
        }
    }
}

private fun DrawScope.drawMatrixPreview(
    theme: MatrixTheme,
    columnData: List<Triple<Int, List<Char>, Pair<Float, Float>>>,
    offset: Float
) {
    val w = size.width
    val h = size.height
    val cols = columnData.size
    val cellW = w / cols
    val charH = 14f

    for ((col, chars, meta) in columnData) {
        val (speed, startY) = meta
        val yOff = ((offset * speed + startY) % 1.4f) * h

        for ((i, ch) in chars.withIndex()) {
            val y = yOff + i * charH - chars.size * charH * 0.3f
            if (y < -charH || y > h + charH) continue

            val alpha = when {
                i == 0 -> 1f
                i < 3 -> 0.85f
                i < 7 -> 0.5f
                else -> 0.2f
            }
            val color = when {
                i == 0 -> Color(theme.headColor)
                i < 5 -> Color(theme.bodyColor)
                else -> Color(theme.trailColor)
            }

            drawContext.canvas.nativeCanvas.drawText(
                ch.toString(),
                col * cellW + cellW / 2f,
                y,
                android.graphics.Paint().apply {
                    this.color = android.graphics.Color.argb(
                        (alpha * 255).toInt(),
                        android.graphics.Color.red(color.hashCode()),
                        android.graphics.Color.green(color.hashCode()),
                        android.graphics.Color.blue(color.hashCode())
                    )
                    textSize = 13f
                    textAlign = android.graphics.Paint.Align.CENTER
                    isAntiAlias = true
                }
            )
        }
    }
}
