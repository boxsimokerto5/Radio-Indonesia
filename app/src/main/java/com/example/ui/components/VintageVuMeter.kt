package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.ui.theme.VintageBorderSepia
import com.example.ui.theme.VintageBorderStrong
import com.example.ui.theme.VintageGoldOchre
import com.example.ui.theme.VintageParchmentCardElevated
import com.example.ui.theme.VintageParchmentDark
import com.example.ui.theme.VintageTerracotta
import com.example.ui.theme.VintageTextEspresso
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun VintageVuMeter(
    level: Float,
    isLive: Boolean,
    modifier: Modifier = Modifier
) {
    val animatedLevel by animateFloatAsState(
        targetValue = if (isLive) level.coerceIn(0f, 1f) else 0f,
        animationSpec = spring(dampingRatio = 0.55f, stiffness = 120f),
        label = "VuMeterNeedle"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(72.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        VintageParchmentCardElevated,
                        VintageParchmentDark
                    )
                )
            )
            .border(1.5.dp, VintageBorderStrong, RoundedCornerShape(10.dp))
            .padding(4.dp)
    ) {
        // Parchment Face
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(8.dp))
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Color(0xFFFFFDF8),
                            Color(0xFFF3E8D3)
                        )
                    )
                )
                .border(1.dp, VintageBorderSepia, RoundedCornerShape(8.dp))
        ) {
            Canvas(modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp, vertical = 6.dp)) {
                val w = size.width
                val h = size.height
                val pivotX = w * 0.5f
                val pivotY = h * 1.35f
                val radius = h * 1.15f

                val startAngle = 215f
                val sweepAngle = 110f

                // Base arc line
                drawArc(
                    color = VintageBorderStrong,
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    topLeft = Offset(pivotX - radius, pivotY - radius),
                    size = androidx.compose.ui.geometry.Size(radius * 2, radius * 2),
                    style = Stroke(width = 2.5f)
                )

                // Terracotta peak zone (last 20%)
                drawArc(
                    color = VintageTerracotta,
                    startAngle = startAngle + sweepAngle * 0.8f,
                    sweepAngle = sweepAngle * 0.2f,
                    useCenter = false,
                    topLeft = Offset(pivotX - radius, pivotY - radius),
                    size = androidx.compose.ui.geometry.Size(radius * 2, radius * 2),
                    style = Stroke(width = 3.5f)
                )

                // Scale ticks
                val tickCount = 14
                for (i in 0..tickCount) {
                    val angleDeg = startAngle + (sweepAngle / tickCount) * i
                    val angleRad = (angleDeg * PI / 180.0).toFloat()
                    val isPeak = i >= tickCount * 0.8
                    val tickLen = if (i % 2 == 0) 10f else 6f

                    val innerR = radius - tickLen
                    val outerR = radius

                    val x1 = pivotX + innerR * cos(angleRad)
                    val y1 = pivotY + innerR * sin(angleRad)
                    val x2 = pivotX + outerR * cos(angleRad)
                    val y2 = pivotY + outerR * sin(angleRad)

                    drawLine(
                        color = if (isPeak) VintageTerracotta else VintageTextEspresso,
                        start = Offset(x1, y1),
                        end = Offset(x2, y2),
                        strokeWidth = if (i % 2 == 0) 2f else 1.2f
                    )
                }

                // Needle
                val needleAngleDeg = startAngle + sweepAngle * animatedLevel
                val needleAngleRad = (needleAngleDeg * PI / 180.0).toFloat()
                val needleLen = radius - 4f

                val needleEndX = pivotX + needleLen * cos(needleAngleRad)
                val needleEndY = pivotY + needleLen * sin(needleAngleRad)

                // Shadow
                drawLine(
                    color = Color(0x22000000),
                    start = Offset(pivotX + 2f, pivotY + 2f),
                    end = Offset(needleEndX + 2f, needleEndY + 2f),
                    strokeWidth = 2.5f,
                    cap = StrokeCap.Round
                )

                // Needle body
                drawLine(
                    color = VintageTextEspresso,
                    start = Offset(pivotX, pivotY),
                    end = Offset(needleEndX, needleEndY),
                    strokeWidth = 2f,
                    cap = StrokeCap.Round
                )

                // Red indicator tip
                val tipLen = 14f
                val tipStartX = pivotX + (needleLen - tipLen) * cos(needleAngleRad)
                val tipStartY = pivotY + (needleLen - tipLen) * sin(needleAngleRad)
                drawLine(
                    color = VintageTerracotta,
                    start = Offset(tipStartX, tipStartY),
                    end = Offset(needleEndX, needleEndY),
                    strokeWidth = 2.5f,
                    cap = StrokeCap.Round
                )

                // Pivot Cap
                drawCircle(
                    color = VintageTextEspresso,
                    radius = 7f,
                    center = Offset(pivotX, h * 0.98f)
                )
                drawCircle(
                    color = VintageGoldOchre,
                    radius = 3.5f,
                    center = Offset(pivotX, h * 0.98f)
                )
            }
        }
    }
}
