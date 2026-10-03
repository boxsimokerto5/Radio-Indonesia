package com.example.ui.components

import android.content.Intent
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Radio
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.filled.VolumeDown
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
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
import com.example.player.PlaybackStatus
import com.example.player.PlayerState
import com.example.ui.theme.VintageAmberWarm
import com.example.ui.theme.VintageBorderSepia
import com.example.ui.theme.VintageBorderStrong
import com.example.ui.theme.VintageGoldOchre
import com.example.ui.theme.VintageParchmentBg
import com.example.ui.theme.VintageParchmentCard
import com.example.ui.theme.VintageParchmentCardElevated
import com.example.ui.theme.VintageParchmentDark
import com.example.ui.theme.VintageParchmentSurfaceVariant
import com.example.ui.theme.VintageSuccessGreen
import com.example.ui.theme.VintageTerracotta
import com.example.ui.theme.VintageTerracottaSoft
import com.example.ui.theme.VintageTextEspresso
import com.example.ui.theme.VintageTextMutedSepia
import com.example.ui.theme.VintageTextWarmBrown

@Composable
fun VintageFullPlayer(
    station: RadioStation,
    playerState: PlayerState,
    onClose: () -> Unit,
    onTogglePlayPause: () -> Unit,
    onPlayNext: () -> Unit,
    onPlayPrevious: () -> Unit,
    onFavoriteToggle: () -> Unit,
    onVolumeChange: (Float) -> Unit,
    onOpenSleepTimer: () -> Unit,
    onOpenSoundProfile: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    BackHandler { onClose() }

    val isPlaying = playerState.status == PlaybackStatus.PLAYING
    val isBuffering = playerState.status == PlaybackStatus.BUFFERING
    val isError = playerState.status == PlaybackStatus.ERROR

    val infiniteTransition = rememberInfiniteTransition(label = "tube_glow")
    val tubePulse by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200),
            repeatMode = RepeatMode.Reverse
        ),
        label = "tubePulse"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        VintageParchmentBg,
                        VintageParchmentDark,
                        VintageParchmentBg
                    )
                )
            )
            .statusBarsPadding()
            .navigationBarsPadding()
            .testTag("full_player_screen")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header Bar: Minimize, Title Emblem, Share, Favorite
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onClose,
                    modifier = Modifier
                        .size(44.dp)
                        .minimumInteractiveComponentSize()
                        .testTag("full_player_close_button")
                ) {
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowDown,
                        contentDescription = "Tutup Pemutar",
                        tint = VintageTextEspresso,
                        modifier = Modifier.size(32.dp)
                    )
                }

                // Vintage Plate Emblem
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(VintageParchmentCardElevated)
                        .border(1.2.dp, VintageBorderStrong, RoundedCornerShape(8.dp))
                        .padding(horizontal = 14.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = "RADIO INDONESIA • 1958",
                        color = VintageTextEspresso,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif,
                        letterSpacing = 1.sp
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Share Button
                    IconButton(
                        onClick = {
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(
                                    Intent.EXTRA_TEXT,
                                    "Mendengarkan siaran radio ${station.name} (${station.displayRegion}) melalui Radio Indonesia: ${station.url}"
                                )
                            }
                            context.startActivity(Intent.createChooser(shareIntent, "Bagikan Stasiun Radio"))
                        },
                        modifier = Modifier
                            .size(40.dp)
                            .minimumInteractiveComponentSize()
                            .testTag("share_button")
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Share,
                            contentDescription = "Bagikan",
                            tint = VintageTextWarmBrown,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    // Favorite Button
                    IconButton(
                        onClick = onFavoriteToggle,
                        modifier = Modifier
                            .size(40.dp)
                            .minimumInteractiveComponentSize()
                            .testTag("full_player_favorite_button")
                    ) {
                        Icon(
                            imageVector = if (station.isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Favorit",
                            tint = if (station.isFavorite) VintageTerracotta else VintageTextWarmBrown,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Analog VU Meter Component
            VintageVuMeter(
                level = playerState.vuMeterLevel,
                isLive = isPlaying,
                modifier = Modifier.padding(horizontal = 6.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Big Centerpiece: Station Logo in Parchment Bezel Frame
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .shadow(
                        elevation = 10.dp,
                        shape = CircleShape,
                        ambientColor = Color(0x33443322),
                        spotColor = Color(0x33443322)
                    )
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                Color.White,
                                VintageParchmentCardElevated
                            )
                        )
                    )
                    .border(
                        width = 4.dp,
                        color = if (isPlaying) VintageTerracotta else VintageBorderStrong,
                        shape = CircleShape
                    )
                    .padding(14.dp),
                contentAlignment = Alignment.Center
            ) {
                if (station.favicon.isNotBlank()) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(station.favicon)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Logo Stasiun ${station.name}",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Filled.Radio,
                        contentDescription = "Logo Radio",
                        tint = VintageTextWarmBrown,
                        modifier = Modifier.size(90.dp)
                    )
                }

                if (isPlaying) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        drawCircle(
                            color = VintageTerracotta.copy(alpha = 0.12f * tubePulse),
                            radius = size.minDimension / 2f
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Station Name in Deep Espresso
            Text(
                text = station.name,
                color = VintageTextEspresso,
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Region, Genre, & Bitrate Badges
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(VintageParchmentDark)
                        .border(1.dp, VintageBorderStrong, RoundedCornerShape(6.dp))
                        .padding(horizontal = 9.dp, vertical = 3.5.dp)
                ) {
                    Text(
                        text = station.displayRegion,
                        color = VintageTextWarmBrown,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(VintageParchmentCardElevated)
                        .border(1.dp, VintageBorderSepia, RoundedCornerShape(6.dp))
                        .padding(horizontal = 9.dp, vertical = 3.5.dp)
                ) {
                    Text(
                        text = station.displayGenre,
                        color = VintageTextMutedSepia,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(VintageParchmentCardElevated)
                        .border(1.dp, VintageBorderSepia, RoundedCornerShape(6.dp))
                        .padding(horizontal = 9.dp, vertical = 3.5.dp)
                ) {
                    Text(
                        text = station.bitrateFormatted,
                        color = VintageGoldOchre,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Live On-Air Banner with Equalizer Waves
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (isPlaying) VintageTerracotta else VintageParchmentCardElevated)
                    .border(1.dp, if (isPlaying) VintageTerracotta else VintageBorderStrong, RoundedCornerShape(8.dp))
                    .padding(horizontal = 14.dp, vertical = 9.dp),
                contentAlignment = Alignment.Center
            ) {
                if (isBuffering) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = VintageTerracotta,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Menghubungkan gelombang siaran…",
                            color = VintageTextEspresso,
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                } else if (isError) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.ErrorOutline,
                            contentDescription = null,
                            tint = VintageTerracotta,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = playerState.errorMessage ?: "Siaran tidak dapat diakses",
                            color = VintageTerracotta,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else if (isPlaying) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // 5-bar animated equalizer wave
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            for (i in 0..4) {
                                val barHeight = 8.dp + ((playerState.vuMeterLevel * (i + 1) * 3).toInt() % 16).dp
                                Box(
                                    modifier = Modifier
                                        .width(3.dp)
                                        .height(barHeight)
                                        .clip(RoundedCornerShape(1.5.dp))
                                        .background(Color.White)
                                )
                            }
                        }

                        Text(
                            text = "SIARAN LANGSUNG (LIVE ON AIR)",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            letterSpacing = 0.5.sp
                        )
                    }
                } else {
                    Text(
                        text = "Siaran Dijeda • Siap Mengudara",
                        color = VintageTextWarmBrown,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Main Playback Controls: Previous, Big Center Knob (Play/Pause), Next
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Previous Station Button
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(VintageParchmentCard)
                        .border(1.5.dp, VintageBorderStrong, CircleShape)
                        .clickable(onClick = onPlayPrevious)
                        .testTag("full_player_previous_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.SkipPrevious,
                        contentDescription = "Stasiun Sebelumnya",
                        tint = VintageTextEspresso,
                        modifier = Modifier.size(28.dp)
                    )
                }

                // Big Tactile Center Knob: Play / Pause
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .shadow(
                            elevation = 8.dp,
                            shape = CircleShape,
                            ambientColor = VintageTerracotta,
                            spotColor = VintageTerracotta
                        )
                        .clip(CircleShape)
                        .background(VintageTerracotta)
                        .border(3.dp, Color.White, CircleShape)
                        .clickable(onClick = onTogglePlayPause)
                        .testTag("full_player_play_pause_button"),
                    contentAlignment = Alignment.Center
                ) {
                    if (isBuffering) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(36.dp),
                            color = Color.White,
                            strokeWidth = 3.5.dp
                        )
                    } else {
                        Icon(
                            imageVector = if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                            contentDescription = if (isPlaying) "Jeda" else "Putar",
                            tint = Color.White,
                            modifier = Modifier.size(44.dp)
                        )
                    }
                }

                // Next Station Button
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(VintageParchmentCard)
                        .border(1.5.dp, VintageBorderStrong, CircleShape)
                        .clickable(onClick = onPlayNext)
                        .testTag("full_player_next_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.SkipNext,
                        contentDescription = "Stasiun Berikutnya",
                        tint = VintageTextEspresso,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Volume Control Slider
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(VintageParchmentCard)
                    .border(1.2.dp, VintageBorderSepia, RoundedCornerShape(10.dp))
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Pengatur Volume",
                        color = VintageTextWarmBrown,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${(playerState.volume * 100).toInt()}%",
                        color = VintageTerracotta,
                        fontSize = 11.5.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.VolumeDown,
                        contentDescription = null,
                        tint = VintageTextMutedSepia,
                        modifier = Modifier.size(20.dp)
                    )
                    Slider(
                        value = playerState.volume,
                        onValueChange = onVolumeChange,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp)
                            .testTag("volume_slider"),
                        colors = SliderDefaults.colors(
                            thumbColor = VintageTerracotta,
                            activeTrackColor = VintageTerracotta,
                            inactiveTrackColor = VintageBorderSepia
                        )
                    )
                    Icon(
                        imageVector = Icons.Filled.VolumeUp,
                        contentDescription = null,
                        tint = VintageTerracotta,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Bottom Auxiliary Features: Sleep Timer and Sound Profile Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Sleep Timer Button
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(VintageParchmentCard)
                        .border(
                            1.2.dp,
                            if (playerState.isSleepTimerActive) VintageTerracotta else VintageBorderStrong,
                            RoundedCornerShape(8.dp)
                        )
                        .clickable(onClick = onOpenSleepTimer)
                        .padding(vertical = 10.dp, horizontal = 10.dp)
                        .testTag("sleep_timer_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Bedtime,
                            contentDescription = null,
                            tint = if (playerState.isSleepTimerActive) VintageTerracotta else VintageTextWarmBrown,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = if (playerState.isSleepTimerActive && playerState.sleepTimerRemainingSeconds != null) {
                                "%02d:%02d".format(
                                    playerState.sleepTimerRemainingSeconds / 60,
                                    playerState.sleepTimerRemainingSeconds % 60
                                )
                            } else "Waktu Tidur",
                            color = if (playerState.isSleepTimerActive) VintageTerracotta else VintageTextEspresso,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Sound Character / Equalizer Profile Button
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(VintageParchmentCard)
                        .border(1.2.dp, VintageBorderStrong, RoundedCornerShape(8.dp))
                        .clickable(onClick = onOpenSoundProfile)
                        .padding(vertical = 10.dp, horizontal = 10.dp)
                        .testTag("sound_profile_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.GraphicEq,
                            contentDescription = null,
                            tint = VintageGoldOchre,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = playerState.soundProfile.label,
                            color = VintageTextEspresso,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
