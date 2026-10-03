package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.VintageBorderSepia
import com.example.ui.theme.VintageBorderStrong
import com.example.ui.theme.VintageGoldOchre
import com.example.ui.theme.VintageParchmentCard
import com.example.ui.theme.VintageParchmentCardElevated
import com.example.ui.theme.VintageParchmentDark
import com.example.ui.theme.VintageSuccessGreen
import com.example.ui.theme.VintageTerracotta
import com.example.ui.theme.VintageTextEspresso
import com.example.ui.theme.VintageTextMutedSepia
import com.example.ui.theme.VintageTextWarmBrown

@Composable
fun VintageTuningDial(
    tuningFraction: Float,
    isLive: Boolean,
    modifier: Modifier = Modifier
) {
    val animatedNeedle by animateFloatAsState(
        targetValue = tuningFraction.coerceIn(0.05f, 0.95f),
        animationSpec = spring(dampingRatio = 0.75f, stiffness = 80f),
        label = "TuningNeedle"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        VintageParchmentCard,
                        VintageParchmentCardElevated
                    )
                )
            )
            .border(1.5.dp, VintageBorderStrong, RoundedCornerShape(10.dp))
            .padding(horizontal = 14.dp, vertical = 9.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Header Row: Frequency Band & Indicator
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "FM 88 - 108 MHz",
                        color = VintageTextEspresso,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "• GELOMBANG PENDEK",
                        color = VintageTextMutedSepia,
                        fontSize = 9.5.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Tuning Jewel Light (Illuminates Green / Terracotta)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(9.dp)
                            .clip(CircleShape)
                            .background(
                                if (isLive) VintageSuccessGreen else VintageBorderStrong
                            )
                            .border(1.dp, VintageBorderSepia, CircleShape)
                    )
                    Text(
                        text = if (isLive) "SIARAN TERSAMBUNG" else "MENALA",
                        color = if (isLive) VintageTerracotta else VintageTextWarmBrown,
                        fontSize = 9.5.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Parchment Tuner Scale Window
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(38.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                VintageParchmentDark,
                                Color(0xFFFAF4E8),
                                VintageParchmentDark
                            )
                        )
                    )
                    .border(1.dp, VintageBorderSepia, RoundedCornerShape(6.dp))
            ) {
                // Scale markings Canvas
                Canvas(modifier = Modifier.fillMaxSize().padding(horizontal = 10.dp)) {
                    val width = size.width
                    val height = size.height

                    // Center guide line
                    drawLine(
                        color = VintageBorderSepia,
                        start = Offset(0f, height * 0.5f),
                        end = Offset(width, height * 0.5f),
                        strokeWidth = 1.2f
                    )

                    // Vertical ticks
                    val ticks = 30
                    for (i in 0..ticks) {
                        val x = (width / ticks) * i
                        val isMajor = i % 5 == 0
                        val tickHeight = if (isMajor) height * 0.44f else height * 0.22f
                        val yStart = (height - tickHeight) / 2f

                        drawLine(
                            color = if (isMajor) VintageTextEspresso else VintageBorderStrong,
                            start = Offset(x, yStart),
                            end = Offset(x, yStart + tickHeight),
                            strokeWidth = if (isMajor) 1.8f else 1f
                        )
                    }

                    // Terracotta / Red Needle
                    val needleX = width * animatedNeedle
                    drawLine(
                        color = VintageTerracotta.copy(alpha = 0.35f),
                        start = Offset(needleX, 2f),
                        end = Offset(needleX, height - 2f),
                        strokeWidth = 4f,
                        cap = StrokeCap.Round
                    )
                    drawLine(
                        color = VintageTerracotta,
                        start = Offset(needleX, 2f),
                        end = Offset(needleX, height - 2f),
                        strokeWidth = 2f,
                        cap = StrokeCap.Round
                    )
                }
            }

            // Frequency Labels at the bottom
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 3.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                listOf("88", "92", "96", "100", "104", "108").forEach { freq ->
                    Text(
                        text = freq,
                        color = VintageTextWarmBrown,
                        fontSize = 9.5.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
