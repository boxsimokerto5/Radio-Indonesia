package com.example.player

import android.content.Context
import android.util.Log
import androidx.annotation.OptIn
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import com.example.model.RadioStation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.sin

enum class PlaybackStatus {
    IDLE,
    BUFFERING,
    PLAYING,
    PAUSED,
    ERROR
}

enum class VintageSoundProfile(val label: String, val description: String) {
    WARM_TUBE("Tabung Hangat", "Resonansi vintage khas radio tabung"),
    CLEAR_VOCAL("Vokal Jernih", "Optimal untuk siaran wicara & berita"),
    RICH_BASS("Bass Mantap", "Nada rendah lebih hangat & bertenaga"),
    NATURAL("Hi-Fi Alami", "Karakter siaran asli tanpa penyesuaian")
}

data class PlayerState(
    val status: PlaybackStatus = PlaybackStatus.IDLE,
    val currentStation: RadioStation? = null,
    val nowPlayingTitle: String? = null,
    val errorMessage: String? = null,
    val volume: Float = 0.85f,
    val sleepTimerRemainingSeconds: Long? = null,
    val isSleepTimerActive: Boolean = false,
    val soundProfile: VintageSoundProfile = VintageSoundProfile.WARM_TUBE,
    val vuMeterLevel: Float = 0f,
    val tubeGlowLevel: Float = 0f
)

class VintageRadioPlayer(
    private val context: Context,
    private val scope: CoroutineScope
) {
    private val tag = "VintageRadioPlayer"
    private var exoPlayer: ExoPlayer? = null

    private val _state = MutableStateFlow(PlayerState())
    val state: StateFlow<PlayerState> = _state.asStateFlow()

    private var sleepTimerJob: Job? = null
    private var visualizerJob: Job? = null
    private var retryCount = 0
    private val maxRetries = 2

    private val playerListener = object : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            when (playbackState) {
                Player.STATE_BUFFERING -> {
                    _state.value = _state.value.copy(
                        status = PlaybackStatus.BUFFERING,
                        errorMessage = null
                    )
                }
                Player.STATE_READY -> {
                    val isPlaying = exoPlayer?.playWhenReady == true
                    _state.value = _state.value.copy(
                        status = if (isPlaying) PlaybackStatus.PLAYING else PlaybackStatus.PAUSED,
                        errorMessage = null
                    )
                }
                Player.STATE_ENDED -> {
                    _state.value = _state.value.copy(status = PlaybackStatus.PAUSED)
                }
                Player.STATE_IDLE -> {
                    if (_state.value.status != PlaybackStatus.ERROR) {
                        _state.value = _state.value.copy(status = PlaybackStatus.IDLE)
                    }
                }
            }
        }

        override fun onIsPlayingChanged(isPlaying: Boolean) {
            if (isPlaying) {
                _state.value = _state.value.copy(status = PlaybackStatus.PLAYING, errorMessage = null)
            } else if (exoPlayer?.playbackState == Player.STATE_READY) {
                _state.value = _state.value.copy(status = PlaybackStatus.PAUSED)
            }
        }

        override fun onPlayerError(error: PlaybackException) {
            Log.e(tag, "ExoPlayer error: ${error.errorCodeName} (${error.errorCode})", error)
            val currentSt = _state.value.currentStation
            if (retryCount < maxRetries && currentSt != null) {
                retryCount++
                _state.value = _state.value.copy(status = PlaybackStatus.BUFFERING)
                scope.launch(Dispatchers.Main) {
                    delay(1500)
                    playStation(currentSt)
                }
            } else {
                _state.value = _state.value.copy(
                    status = PlaybackStatus.ERROR,
                    errorMessage = "Koneksi stasiun terputus. Silakan coba beberapa saat lagi."
                )
            }
        }

        override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
            val title = mediaMetadata.title?.toString()
                ?: mediaMetadata.displayTitle?.toString()
            if (!title.isNullOrBlank()) {
                _state.value = _state.value.copy(nowPlayingTitle = title)
            }
        }
    }

    @OptIn(UnstableApi::class)
    private fun initPlayer() {
        try {
            val httpDataSourceFactory = DefaultHttpDataSource.Factory()
                .setUserAgent("Mozilla/5.0 (Linux; Android 14; Mobile) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 RadioNusantara/1.0")
                .setConnectTimeoutMs(15000)
                .setReadTimeoutMs(20000)
                .setAllowCrossProtocolRedirects(true)

            val mediaSourceFactory = DefaultMediaSourceFactory(context)
                .setDataSourceFactory(httpDataSourceFactory)

            val audioAttributes = AudioAttributes.Builder()
                .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
                .setUsage(C.USAGE_MEDIA)
                .build()

            exoPlayer = ExoPlayer.Builder(context)
                .setMediaSourceFactory(mediaSourceFactory)
                .setAudioAttributes(audioAttributes, /* handleAudioFocus= */ true)
                .setWakeMode(C.WAKE_MODE_NETWORK)
                .build()
                .apply {
                    volume = _state.value.volume
                    addListener(playerListener)
                }
        } catch (e: Exception) {
            Log.e(tag, "Failed to initialize ExoPlayer", e)
        }
    }

    init {
        initPlayer()
        startVisualizerDriver()
    }

    fun playStation(station: RadioStation) {
        val player = exoPlayer ?: run {
            initPlayer()
            exoPlayer
        } ?: return

        retryCount = 0
        _state.value = _state.value.copy(
            currentStation = station,
            nowPlayingTitle = null,
            status = PlaybackStatus.BUFFERING,
            errorMessage = null
        )

        try {
            val mediaItem = MediaItem.Builder()
                .setUri(station.url)
                .setMediaMetadata(
                    MediaMetadata.Builder()
                        .setTitle(station.name)
                        .setArtist(station.displayRegion)
                        .build()
                )
                .build()

            player.setMediaItem(mediaItem)
            player.prepare()
            player.playWhenReady = true
        } catch (e: Exception) {
            Log.e(tag, "Failed to play station ${station.name}", e)
            _state.value = _state.value.copy(
                status = PlaybackStatus.ERROR,
                errorMessage = "Gagal memutar siaran radio"
            )
        }
    }

    fun togglePlayPause() {
        val player = exoPlayer ?: return
        when (_state.value.status) {
            PlaybackStatus.PLAYING -> pause()
            PlaybackStatus.PAUSED -> resume()
            PlaybackStatus.ERROR, PlaybackStatus.IDLE -> {
                _state.value.currentStation?.let { playStation(it) }
            }
            PlaybackStatus.BUFFERING -> pause()
        }
    }

    fun pause() {
        exoPlayer?.playWhenReady = false
        _state.value = _state.value.copy(status = PlaybackStatus.PAUSED)
    }

    fun resume() {
        val player = exoPlayer ?: return
        if (player.playbackState == Player.STATE_IDLE) {
            _state.value.currentStation?.let { playStation(it) }
        } else {
            player.playWhenReady = true
            _state.value = _state.value.copy(status = PlaybackStatus.PLAYING)
        }
    }

    fun stop() {
        exoPlayer?.stop()
        _state.value = _state.value.copy(status = PlaybackStatus.IDLE)
    }

    fun setVolume(volume: Float) {
        val clamped = volume.coerceIn(0f, 1f)
        _state.value = _state.value.copy(volume = clamped)
        exoPlayer?.volume = clamped
    }

    fun setSoundProfile(profile: VintageSoundProfile) {
        _state.value = _state.value.copy(soundProfile = profile)
    }

    fun setSleepTimer(minutes: Int) {
        sleepTimerJob?.cancel()
        if (minutes <= 0) {
            _state.value = _state.value.copy(
                sleepTimerRemainingSeconds = null,
                isSleepTimerActive = false
            )
            return
        }

        val totalSeconds = minutes * 60L
        _state.value = _state.value.copy(
            sleepTimerRemainingSeconds = totalSeconds,
            isSleepTimerActive = true
        )

        sleepTimerJob = scope.launch(Dispatchers.Default) {
            var remaining = totalSeconds
            while (isActive && remaining > 0) {
                delay(1000)
                remaining--
                _state.value = _state.value.copy(sleepTimerRemainingSeconds = remaining)
            }
            if (isActive && remaining <= 0) {
                launch(Dispatchers.Main) {
                    stop()
                    _state.value = _state.value.copy(
                        sleepTimerRemainingSeconds = null,
                        isSleepTimerActive = false
                    )
                }
            }
        }
    }

    private fun startVisualizerDriver() {
        visualizerJob?.cancel()
        visualizerJob = scope.launch(Dispatchers.Default) {
            var t = 0.0
            while (isActive) {
                delay(50)
                t += 0.15
                val isPlaying = _state.value.status == PlaybackStatus.PLAYING
                if (isPlaying) {
                    val base = 0.55f + 0.35f * sin(t).toFloat() * sin(t * 1.8).toFloat()
                    val flutter = 0.10f * sin(t * 4.3).toFloat()
                    val targetVu = (base + flutter).coerceIn(0.15f, 0.98f)
                    val targetTube = (0.7f + 0.3f * sin(t * 0.8).toFloat()).coerceIn(0.4f, 1.0f)
                    _state.value = _state.value.copy(
                        vuMeterLevel = targetVu,
                        tubeGlowLevel = targetTube
                    )
                } else {
                    val currentVu = _state.value.vuMeterLevel
                    val currentTube = _state.value.tubeGlowLevel
                    if (currentVu > 0.01f || currentTube > 0.1f) {
                        _state.value = _state.value.copy(
                            vuMeterLevel = (currentVu * 0.8f).coerceAtLeast(0f),
                            tubeGlowLevel = (currentTube * 0.9f).coerceAtLeast(0.1f)
                        )
                    }
                }
            }
        }
    }

    fun destroy() {
        sleepTimerJob?.cancel()
        visualizerJob?.cancel()
        try {
            exoPlayer?.removeListener(playerListener)
            exoPlayer?.release()
        } catch (e: Exception) {
            Log.e(tag, "Error releasing ExoPlayer", e)
        } finally {
            exoPlayer = null
        }
    }
}
