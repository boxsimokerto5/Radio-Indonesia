package com.example.ui.screens

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayCircleFilled
import androidx.compose.material.icons.filled.Radio
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Radio
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ads.IronSourceAdManager
import com.example.player.PlaybackStatus
import com.example.ui.components.IronSourceBannerAd
import com.example.ui.components.IronSourceNativeAdCard
import com.example.ui.components.SleepTimerDialog
import com.example.ui.components.SoundProfileDialog
import com.example.ui.components.VintageFullPlayer
import com.example.ui.components.VintageMiniPlayer
import com.example.ui.components.VintageStationCard
import com.example.ui.components.VintageTuningDial
import com.example.ui.theme.VintageBorderSepia
import com.example.ui.theme.VintageBorderStrong
import com.example.ui.theme.VintageGoldOchre
import com.example.ui.theme.VintageParchmentBg
import com.example.ui.theme.VintageParchmentCard
import com.example.ui.theme.VintageParchmentCardElevated
import com.example.ui.theme.VintageParchmentDark
import com.example.ui.theme.VintageTerracotta
import com.example.ui.theme.VintageTerracottaSoft
import com.example.ui.theme.VintageTextDimSepia
import com.example.ui.theme.VintageTextEspresso
import com.example.ui.theme.VintageTextMutedSepia
import com.example.ui.theme.VintageTextWarmBrown
import com.example.viewmodel.BottomNavTab
import com.example.viewmodel.MainCategoryTab
import com.example.viewmodel.ProfileModal
import com.example.viewmodel.RadioViewModel

@Composable
fun RadioHomeScreen(
    viewModel: RadioViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val activity = context as? Activity
    val uiState by viewModel.uiState.collectAsState()
    val playerState = uiState.playerState
    val currentStation = playerState.currentStation
    val isPlaying = playerState.status == PlaybackStatus.PLAYING
    val isBuffering = playerState.status == PlaybackStatus.BUFFERING

    // Tampilkan iklan Interstitial (8wyu2fy0qdtbdhef) setiap 7 kali berpindah channel radio
    LaunchedEffect(uiState.shouldTriggerInterstitial) {
        if (uiState.shouldTriggerInterstitial) {
            if (activity != null) {
                IronSourceAdManager.showInterstitialIfReady(activity)
            }
            viewModel.consumeInterstitialTrigger()
        }
    }

    // Handle back button on "Saya" tab
    BackHandler(enabled = uiState.activeBottomTab == BottomNavTab.SAYA) {
        viewModel.setBottomNavTab(BottomNavTab.RADIO)
    }

    val tuningFraction = if (currentStation != null) {
        val total = uiState.stations.size.coerceAtLeast(1)
        val idx = uiState.stations.indexOfFirst { it.id == currentStation.id }.coerceAtLeast(0)
        (idx.toFloat() / total.toFloat()).coerceIn(0.1f, 0.9f)
    } else 0.5f

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding(),
        containerColor = VintageParchmentBg,
        bottomBar = {
            // 2 Bilah Menu di Bawah: Menu 1 "Radio", Menu 2 "Saya"
            NavigationBar(
                containerColor = VintageParchmentDark,
                contentColor = VintageTerracotta,
                tonalElevation = 6.dp,
                modifier = Modifier
                    .border(width = 1.dp, color = VintageBorderSepia)
                    .testTag("bottom_navigation_bar")
            ) {
                // Tab 1: Radio
                NavigationBarItem(
                    selected = uiState.activeBottomTab == BottomNavTab.RADIO,
                    onClick = { viewModel.setBottomNavTab(BottomNavTab.RADIO) },
                    icon = {
                        Icon(
                            imageVector = if (uiState.activeBottomTab == BottomNavTab.RADIO) Icons.Filled.Radio else Icons.Outlined.Radio,
                            contentDescription = "Menu Radio"
                        )
                    },
                    label = {
                        Text(
                            text = "Radio",
                            fontFamily = FontFamily.Serif,
                            fontWeight = if (uiState.activeBottomTab == BottomNavTab.RADIO) FontWeight.Bold else FontWeight.Medium
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = VintageTerracotta,
                        selectedTextColor = VintageTerracotta,
                        indicatorColor = VintageTerracottaSoft,
                        unselectedIconColor = VintageTextWarmBrown,
                        unselectedTextColor = VintageTextWarmBrown
                    ),
                    modifier = Modifier.testTag("tab_nav_radio")
                )

                // Tab 2: Saya
                NavigationBarItem(
                    selected = uiState.activeBottomTab == BottomNavTab.SAYA,
                    onClick = { viewModel.setBottomNavTab(BottomNavTab.SAYA) },
                    icon = {
                        Icon(
                            imageVector = if (uiState.activeBottomTab == BottomNavTab.SAYA) Icons.Filled.Person else Icons.Outlined.Person,
                            contentDescription = "Menu Saya"
                        )
                    },
                    label = {
                        Text(
                            text = "Saya",
                            fontFamily = FontFamily.Serif,
                            fontWeight = if (uiState.activeBottomTab == BottomNavTab.SAYA) FontWeight.Bold else FontWeight.Medium
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = VintageTerracotta,
                        selectedTextColor = VintageTerracotta,
                        indicatorColor = VintageTerracottaSoft,
                        unselectedIconColor = VintageTextWarmBrown,
                        unselectedTextColor = VintageTextWarmBrown
                    ),
                    modifier = Modifier.testTag("tab_nav_saya")
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Konten Utama berdasarkan tab terpilih
            if (uiState.activeBottomTab == BottomNavTab.SAYA) {
                // Halaman "Saya" (Profil, Kalender, Cuaca BMKG, Kalkulator, Rating, Share, Privacy, About)
                ProfileScreen(
                    viewModel = viewModel,
                    userName = uiState.userName,
                    userCity = uiState.userCity,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                // Halaman "Radio" (Daftar Radio Saat Ini)
                Column(modifier = Modifier.fillMaxSize()) {
                    // Header Area with Vintage Typography & High Contrast
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(id = R.drawable.img_radio_indonesia_logo),
                                contentDescription = "Logo Radio Indonesia",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .border(1.dp, VintageBorderStrong, RoundedCornerShape(10.dp))
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "RADIO INDONESIA",
                                        color = VintageTextEspresso,
                                        fontSize = 19.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Serif,
                                        letterSpacing = 1.2.sp
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Box(
                                        modifier = Modifier
                                            .size(9.dp)
                                            .clip(CircleShape)
                                            .background(if (isPlaying) VintageTerracotta else VintageBorderStrong)
                                    )
                                }
                                Text(
                                    text = "Suara Nusantara • Gelombang Klasik",
                                    color = VintageTextWarmBrown,
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Medium,
                                    fontFamily = FontFamily.Serif
                                )
                            }
                        }

                        // Online sync status & refresh button
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (uiState.isLoadingOnline) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    color = VintageTerracotta,
                                    strokeWidth = 2.dp
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                            }
                            IconButton(
                                onClick = { viewModel.fetchOnlineStations() },
                                modifier = Modifier
                                    .size(36.dp)
                                    .testTag("refresh_stations_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Refresh,
                                    contentDescription = "Perbarui Stasiun",
                                    tint = VintageTextEspresso,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                    }

                    // Vintage Analog Tuning Dial
                    VintageTuningDial(
                        tuningFraction = tuningFraction,
                        isLive = isPlaying,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Vintage Search Bar in Parchment
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(VintageParchmentCard)
                            .border(1.2.dp, VintageBorderSepia, RoundedCornerShape(10.dp))
                    ) {
                        TextField(
                            value = uiState.searchQuery,
                            onValueChange = { viewModel.setSearchQuery(it) },
                            placeholder = {
                                Text(
                                    text = "Cari nama radio, frekuensi, atau kota…",
                                    color = VintageTextDimSepia,
                                    fontSize = 13.sp
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Filled.Search,
                                    contentDescription = "Cari",
                                    tint = VintageTerracotta,
                                    modifier = Modifier.size(20.dp)
                                )
                            },
                            trailingIcon = {
                                if (uiState.searchQuery.isNotEmpty()) {
                                    IconButton(onClick = { viewModel.setSearchQuery("") }) {
                                        Icon(
                                            imageVector = Icons.Filled.Clear,
                                            contentDescription = "Hapus Pencarian",
                                            tint = VintageTextWarmBrown,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                disabledContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                cursorColor = VintageTerracotta,
                                focusedTextColor = VintageTextEspresso,
                                unfocusedTextColor = VintageTextEspresso
                            ),
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("search_text_field")
                        )
                    }

                    // Iklan Banner ironSource (76s7uqsiag1z3h4y) tepat di bawah kotak pencarian & di atas Tab
                    IronSourceBannerAd()

                    // Primary Category Tabs: Semua, Favorit, Wilayah, Genre
                    TabRow(
                        selectedTabIndex = uiState.selectedTab.ordinal,
                        containerColor = VintageParchmentDark,
                        contentColor = VintageTerracotta,
                        indicator = { tabPositions ->
                            TabRowDefaults.SecondaryIndicator(
                                modifier = Modifier.tabIndicatorOffset(tabPositions[uiState.selectedTab.ordinal]),
                                height = 3.dp,
                                color = VintageTerracotta
                            )
                        },
                        divider = {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(1.dp)
                                    .background(VintageBorderSepia)
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        MainCategoryTab.values().forEach { tab ->
                            val isSelected = uiState.selectedTab == tab
                            Tab(
                                selected = isSelected,
                                onClick = { viewModel.setTab(tab) },
                                text = {
                                    Text(
                                        text = tab.label,
                                        fontSize = 13.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isSelected) VintageTerracotta else VintageTextWarmBrown,
                                        fontFamily = FontFamily.Serif
                                    )
                                },
                                modifier = Modifier.testTag("tab_${tab.name.lowercase()}")
                            )
                        }
                    }

                    // Secondary Sub-filter row if Wilayah or Genre tab is selected
                    if (uiState.selectedTab == MainCategoryTab.REGIONS) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState())
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            VintageFilterChip(
                                label = "Semua Wilayah",
                                isSelected = uiState.selectedRegion == null,
                                onClick = { viewModel.selectRegion(null) }
                            )

                            uiState.availableRegions.forEach { region ->
                                VintageFilterChip(
                                    label = region,
                                    isSelected = uiState.selectedRegion == region,
                                    onClick = { viewModel.selectRegion(region) }
                                )
                            }
                        }
                    } else if (uiState.selectedTab == MainCategoryTab.GENRES) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState())
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            VintageFilterChip(
                                label = "Semua Genre",
                                isSelected = uiState.selectedGenre == null,
                                onClick = { viewModel.selectGenre(null) }
                            )

                            uiState.availableGenres.forEach { genre ->
                                VintageFilterChip(
                                    label = genre,
                                    isSelected = uiState.selectedGenre == genre,
                                    onClick = { viewModel.selectGenre(genre) }
                                )
                            }
                        }
                    }

                    // Grid of Radio Stations (Kotak-kotak logo radio)
                    if (uiState.filteredStations.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                if (uiState.selectedTab == MainCategoryTab.FAVORITES) {
                                    Icon(
                                        imageVector = Icons.Filled.Favorite,
                                        contentDescription = null,
                                        tint = VintageBorderStrong,
                                        modifier = Modifier.size(54.dp)
                                    )
                                } else {
                                    Image(
                                        painter = painterResource(id = R.drawable.img_radio_indonesia_logo),
                                        contentDescription = "Logo Radio Indonesia",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .size(68.dp)
                                            .clip(RoundedCornerShape(14.dp))
                                    )
                                }
                                Spacer(modifier = Modifier.height(14.dp))
                                Text(
                                    text = if (uiState.selectedTab == MainCategoryTab.FAVORITES) "Belum ada stasiun favorit" else "Tidak ada stasiun radio yang cocok",
                                    color = VintageTextEspresso,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Serif,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = if (uiState.selectedTab == MainCategoryTab.FAVORITES) "Ketuk ikon hati pada logo radio untuk menyimpan stasiun favorit Anda." else "Coba cari dengan kata kunci lain atau pilih kategori Semua.",
                                    color = VintageTextMutedSepia,
                                    fontSize = 12.5.sp,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    } else {
                        val stationChunks = uiState.visibleStations.chunked(6)

                        LazyVerticalGrid(
                            columns = GridCells.Adaptive(minSize = 155.dp),
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .testTag("stations_grid"),
                            contentPadding = PaddingValues(
                                start = 14.dp,
                                end = 14.dp,
                                top = 10.dp,
                                bottom = if (currentStation != null) 90.dp else 24.dp
                            ),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            stationChunks.forEachIndexed { chunkIndex, chunk ->
                                items(chunk, key = { it.id }) { station ->
                                    val isThisPlaying = isPlaying && currentStation?.id == station.id
                                    val isThisBuffering = isBuffering && currentStation?.id == station.id

                                    VintageStationCard(
                                        station = station,
                                        isPlaying = isThisPlaying,
                                        isBuffering = isThisBuffering,
                                        onStationClick = {
                                            viewModel.playStation(station, openPlayer = true)
                                        },
                                        onFavoriteToggle = {
                                            viewModel.toggleFavorite(station.id)
                                        }
                                    )
                                }

                                // Sematkan Iklan Native ironSource (qy99lqfqur75u1uy) di sela-sela channel radio
                                item(
                                    key = "native_ad_slot_$chunkIndex",
                                    span = { GridItemSpan(maxLineSpan) }
                                ) {
                                    IronSourceNativeAdCard(slotIndex = chunkIndex)
                                }
                            }

                            // Tombol "Lihat Lebih Banyak Channel (Tonton Iklan)" untuk Rewarded Ad (bceju1t1vqwf6gq7)
                            if (uiState.hasMoreLockedStations) {
                                item(
                                    key = "unlock_more_channels_reward_button",
                                    span = { GridItemSpan(maxLineSpan) }
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 8.dp)
                                            .clip(RoundedCornerShape(14.dp))
                                            .background(VintageParchmentCardElevated)
                                            .border(1.5.dp, VintageTerracotta, RoundedCornerShape(14.dp))
                                            .padding(16.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = "Masih Ada +${uiState.remainingLockedCount} Channel Radio Nusantara Lainnya",
                                            color = VintageTextEspresso,
                                            fontSize = 13.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = FontFamily.Serif,
                                            textAlign = TextAlign.Center
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "Menampilkan 20 channel pertama. Tonton iklan singkat untuk membuka seluruh stasiun radio.",
                                            color = VintageTextWarmBrown,
                                            fontSize = 11.5.sp,
                                            textAlign = TextAlign.Center
                                        )
                                        Spacer(modifier = Modifier.height(10.dp))
                                        Button(
                                            onClick = {
                                                IronSourceAdManager.showRewardedAd(activity) {
                                                    viewModel.unlockMoreStationsByReward()
                                                }
                                            },
                                            colors = ButtonDefaults.buttonColors(containerColor = VintageTerracotta),
                                            shape = RoundedCornerShape(10.dp),
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .testTag("reward_unlock_channels_button")
                                        ) {
                                            Icon(
                                                imageVector = Icons.Filled.PlayCircleFilled,
                                                contentDescription = null,
                                                tint = Color.White,
                                                modifier = Modifier.size(20.dp)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = "Lihat Lebih Banyak Channel (Tonton Iklan)",
                                                color = Color.White,
                                                fontSize = 12.5.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Docked Mini Player at Bottom (Sits above the bottom bar)
            if (currentStation != null && !uiState.isFullPlayerVisible) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                ) {
                    VintageMiniPlayer(
                        currentStation = currentStation,
                        playbackStatus = playerState.status,
                        onTogglePlayPause = { viewModel.togglePlayPause() },
                        onPlayNext = { viewModel.playNext() },
                        onOpenFullPlayer = { viewModel.openFullPlayer() }
                    )
                }
            }

            // Full Player Screen (Overlaid with smooth slide animation)
            AnimatedVisibility(
                visible = uiState.isFullPlayerVisible && currentStation != null,
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
            ) {
                currentStation?.let { st ->
                    VintageFullPlayer(
                        station = st,
                        playerState = playerState,
                        onClose = { viewModel.closeFullPlayer() },
                        onTogglePlayPause = { viewModel.togglePlayPause() },
                        onPlayNext = { viewModel.playNext() },
                        onPlayPrevious = { viewModel.playPrevious() },
                        onFavoriteToggle = { viewModel.toggleFavorite(st.id) },
                        onVolumeChange = { viewModel.setVolume(it) },
                        onOpenSleepTimer = { viewModel.setSleepTimerDialogVisible(true) },
                        onOpenSoundProfile = { viewModel.setSoundProfileDialogVisible(true) }
                    )
                }
            }

            // Dialogs for Player
            if (uiState.isSleepTimerDialogVisible) {
                SleepTimerDialog(
                    isActive = playerState.isSleepTimerActive,
                    remainingSeconds = playerState.sleepTimerRemainingSeconds,
                    onSelectMinutes = { viewModel.setSleepTimer(it) },
                    onDismiss = { viewModel.setSleepTimerDialogVisible(false) }
                )
            }

            if (uiState.isSoundProfileDialogVisible) {
                SoundProfileDialog(
                    currentProfile = playerState.soundProfile,
                    onSelectProfile = { viewModel.setSoundProfile(it) },
                    onDismiss = { viewModel.setSoundProfileDialogVisible(false) }
                )
            }

            // Dialogs for "Saya" Screen
            when (uiState.activeProfileModal) {
                ProfileModal.EDIT_PROFILE -> {
                    EditProfileDialog(
                        initialName = uiState.userName,
                        initialCity = uiState.userCity,
                        onSave = { name, city -> viewModel.updateProfile(name, city) },
                        onDismiss = { viewModel.closeProfileModal() }
                    )
                }
                ProfileModal.CALENDAR -> {
                    CalendarDialog(onDismiss = { viewModel.closeProfileModal() })
                }
                ProfileModal.WEATHER -> {
                    WeatherDialog(userCity = uiState.userCity, onDismiss = { viewModel.closeProfileModal() })
                }
                ProfileModal.CALCULATOR -> {
                    CalculatorDialog(onDismiss = { viewModel.closeProfileModal() })
                }
                ProfileModal.RATING -> {
                    RatingDialog(onDismiss = { viewModel.closeProfileModal() })
                }
                ProfileModal.PRIVACY -> {
                    PrivacyPolicyDialog(onDismiss = { viewModel.closeProfileModal() })
                }
                ProfileModal.ABOUT -> {
                    AboutAppDialog(onDismiss = { viewModel.closeProfileModal() })
                }
                ProfileModal.NONE -> {}
            }
        }
    }
}

@Composable
fun VintageFilterChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(if (isSelected) VintageTerracotta else VintageParchmentCard)
            .border(
                width = if (isSelected) 1.5.dp else 1.dp,
                color = if (isSelected) VintageTerracotta else VintageBorderSepia,
                shape = RoundedCornerShape(6.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = if (isSelected) Color.White else VintageTextWarmBrown,
            fontSize = 11.5.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
        )
    }
}
