package com.example.ui.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Radio
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.model.RadioStation
import com.example.ui.theme.VintageAmberWarm
import com.example.ui.theme.VintageBorderSepia
import com.example.ui.theme.VintageBorderStrong
import com.example.ui.theme.VintageGoldOchre
import com.example.ui.theme.VintageParchmentBg
import com.example.ui.theme.VintageParchmentCard
import com.example.ui.theme.VintageParchmentCardElevated
import com.example.ui.theme.VintageParchmentDark
import com.example.ui.theme.VintageSuccessGreen
import com.example.ui.theme.VintageTerracotta
import com.example.ui.theme.VintageTerracottaSoft
import com.example.ui.theme.VintageTextDimSepia
import com.example.ui.theme.VintageTextEspresso
import com.example.ui.theme.VintageTextMutedSepia
import com.example.ui.theme.VintageTextWarmBrown

@Composable
fun VintageStationCard(
    station: RadioStation,
    isPlaying: Boolean,
    isBuffering: Boolean,
    onStationClick: () -> Unit,
    onFavoriteToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "card_pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(800),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("station_card_${station.id}")
            .shadow(
                elevation = if (isPlaying) 6.dp else 2.dp,
                shape = RoundedCornerShape(14.dp),
                ambientColor = if (isPlaying) VintageTerracotta else Color(0x33443322),
                spotColor = if (isPlaying) VintageTerracotta else Color(0x33443322)
            )
            .clip(RoundedCornerShape(14.dp))
            .border(
                width = if (isPlaying) 2.dp else 1.2.dp,
                color = if (isPlaying) VintageTerracotta else VintageBorderSepia,
                shape = RoundedCornerShape(14.dp)
            )
            .clickable(onClick = onStationClick),
        colors = CardDefaults.cardColors(
            containerColor = VintageParchmentCard
        ),
        shape = RoundedCornerShape(14.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            // Soft Parchment Texture Gradient
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                VintageParchmentCard,
                                if (isPlaying) VintageTerracottaSoft.copy(alpha = 0.35f) else VintageParchmentCardElevated
                            )
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top Row: Location Pill + Favorite Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Region badge in parchment pill
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(VintageParchmentDark)
                            .border(0.8.dp, VintageBorderStrong, RoundedCornerShape(6.dp))
                            .padding(horizontal = 7.dp, vertical = 2.5.dp)
                    ) {
                        Text(
                            text = station.displayRegion,
                            color = VintageTextWarmBrown,
                            fontSize = 9.5.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    // Favorite Button
                    IconButton(
                        onClick = onFavoriteToggle,
                        modifier = Modifier
                            .size(32.dp)
                            .minimumInteractiveComponentSize()
                            .testTag("favorite_button_${station.id}")
                    ) {
                        Icon(
                            imageVector = if (station.isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Simpan ke favorit",
                            tint = if (station.isFavorite) VintageTerracotta else VintageTextDimSepia,
                            modifier = Modifier.size(19.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Circular Frame for Station Logo
                Box(
                    modifier = Modifier
                        .size(86.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    Color.White,
                                    VintageParchmentCardElevated
                                )
                            )
                        )
                        .border(
                            width = if (isPlaying) 2.5.dp else 1.5.dp,
                            color = if (isPlaying) VintageTerracotta else VintageBorderStrong,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(5.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (station.favicon.isNotBlank()) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(station.favicon)
                                .crossfade(true)
                                .build(),
                            contentDescription = "Logo ${station.name}",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(12.dp))
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(12.dp))
                                .background(VintageParchmentDark),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Radio,
                                contentDescription = "Logo Radio",
                                tint = VintageTextWarmBrown,
                                modifier = Modifier.size(42.dp)
                            )
                        }
                    }

                    // Playing / Buffering Badge
                    if (isPlaying) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .size(14.dp)
                                .clip(CircleShape)
                                .background(VintageSuccessGreen.copy(alpha = pulseAlpha))
                                .border(1.5.dp, Color.White, CircleShape)
                        )
                    } else if (isBuffering) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .size(14.dp)
                                .clip(CircleShape)
                                .background(VintageAmberWarm.copy(alpha = pulseAlpha))
                                .border(1.5.dp, Color.White, CircleShape)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Station Name in High Contrast Espresso
                Text(
                    text = station.name,
                    color = VintageTextEspresso,
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(3.dp))

                // Genre and Bitrate Tag
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = station.displayGenre,
                        color = VintageTextMutedSepia,
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = " • ",
                        color = VintageBorderStrong,
                        fontSize = 10.sp
                    )
                    Text(
                        text = station.bitrateFormatted,
                        color = VintageGoldOchre,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // If currently playing: On Air Vintage Indicator Bar
                if (isPlaying) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(6.dp))
                            .background(VintageTerracotta)
                            .padding(vertical = 3.5.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(Color.White)
                            )
                            Text(
                                text = "ON AIR • MEMUTAR",
                                color = Color.White,
                                fontSize = 8.5.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
