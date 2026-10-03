package com.example.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.CuratedStations
import com.example.data.FavoritesRepository
import com.example.data.RadioApiClient
import com.example.model.RadioStation
import com.example.player.PlaybackStatus
import com.example.player.PlayerState
import com.example.player.VintageRadioPlayer
import com.example.player.VintageSoundProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

enum class BottomNavTab(val label: String) {
    RADIO("Radio"),
    SAYA("Saya")
}

enum class MainCategoryTab(val label: String) {
    ALL("Semua"),
    FAVORITES("Favorit"),
    REGIONS("Wilayah"),
    GENRES("Genre")
}

enum class ProfileModal {
    NONE,
    EDIT_PROFILE,
    CALENDAR,
    WEATHER,
    CALCULATOR,
    RATING,
    PRIVACY,
    ABOUT
}

data class RadioUiState(
    val stations: List<RadioStation> = emptyList(),
    val filteredStations: List<RadioStation> = emptyList(),
    val availableRegions: List<String> = emptyList(),
    val availableGenres: List<String> = emptyList(),
    val selectedTab: MainCategoryTab = MainCategoryTab.ALL,
    val selectedRegion: String? = null,
    val selectedGenre: String? = null,
    val searchQuery: String = "",
    val isLoadingOnline: Boolean = false,
    val isFullPlayerVisible: Boolean = false,
    val isSleepTimerDialogVisible: Boolean = false,
    val isSoundProfileDialogVisible: Boolean = false,
    val playerState: PlayerState = PlayerState(),
    val favoriteIds: Set<String> = emptySet(),
    val activeBottomTab: BottomNavTab = BottomNavTab.RADIO,
    val userName: String = "Kolektor Radio",
    val userCity: String = "Jakarta, Indonesia",
    val activeProfileModal: ProfileModal = ProfileModal.NONE
)

class RadioViewModel(application: Application) : AndroidViewModel(application) {
    private val tag = "RadioViewModel"
    private val favoritesRepo = FavoritesRepository(application.applicationContext)
    val player = VintageRadioPlayer(application.applicationContext, viewModelScope)

    private val _rawStations = MutableStateFlow<List<RadioStation>>(CuratedStations.list)
    private val _selectedTab = MutableStateFlow(MainCategoryTab.ALL)
    private val _selectedRegion = MutableStateFlow<String?>(null)
    private val _selectedGenre = MutableStateFlow<String?>(null)
    private val _searchQuery = MutableStateFlow("")
    private val _isLoadingOnline = MutableStateFlow(false)
    private val _isFullPlayerVisible = MutableStateFlow(false)
    private val _isSleepTimerDialogVisible = MutableStateFlow(false)
    private val _isSoundProfileDialogVisible = MutableStateFlow(false)

    // Navigation and Profile states
    private val _activeBottomTab = MutableStateFlow(BottomNavTab.RADIO)
    private val _userName = MutableStateFlow(favoritesRepo.getUserName())
    private val _userCity = MutableStateFlow(favoritesRepo.getUserCity())
    private val _activeProfileModal = MutableStateFlow(ProfileModal.NONE)

    val uiState: StateFlow<RadioUiState> = combine(
        combine(
            _rawStations,
            _selectedTab,
            _selectedRegion,
            _selectedGenre,
            _searchQuery
        ) { raw, tab, reg, gen, query ->
            StationFilters(raw, tab, reg, gen, query)
        },
        combine(
            _isLoadingOnline,
            _isFullPlayerVisible,
            _isSleepTimerDialogVisible,
            _isSoundProfileDialogVisible,
            player.state
        ) { loading, fullPlayer, sleep, sound, pState ->
            PlayerAndDialogState(loading, fullPlayer, sleep, sound, pState)
        },
        combine(
            favoritesRepo.favoriteIds,
            _activeBottomTab,
            _userName,
            _userCity,
            _activeProfileModal
        ) { favIds, bottomTab, name, city, modal ->
            ProfileAndNavState(favIds, bottomTab, name, city, modal)
        }
    ) { filters, playerInfo, profileInfo ->
        val annotatedStations = filters.rawStations.map { station ->
            station.copy(isFavorite = profileInfo.favIds.contains(station.id))
        }

        val regions = listOf(
            "DKI Jakarta",
            "Jawa Barat",
            "Jawa Tengah",
            "DIY Yogyakarta",
            "Jawa Timur",
            "Bali",
            "Sumatera",
            "Sulawesi",
            "Kalimantan",
            "Nasional"
        )

        val genres = listOf(
            "Pop & Hits",
            "Dangdut",
            "Berita & Info",
            "Religi & Dakwah",
            "Budaya Daerah",
            "Rock & Alternatif",
            "Nostalgia & Jazz",
            "Variasi Musik"
        )

        val filtered = annotatedStations.filter { station ->
            val matchesQuery = if (filters.query.isBlank()) true else {
                val q = filters.query.trim().lowercase()
                station.name.lowercase().contains(q) ||
                        station.tags.lowercase().contains(q) ||
                        station.displayRegion.lowercase().contains(q) ||
                        station.displayGenre.lowercase().contains(q)
            }

            val matchesTab = when (filters.tab) {
                MainCategoryTab.ALL -> true
                MainCategoryTab.FAVORITES -> station.isFavorite
                MainCategoryTab.REGIONS -> if (filters.region == null) true else station.displayRegion.equals(filters.region, ignoreCase = true)
                MainCategoryTab.GENRES -> if (filters.genre == null) true else station.displayGenre.equals(filters.genre, ignoreCase = true)
            }

            matchesQuery && matchesTab
        }

        RadioUiState(
            stations = annotatedStations,
            filteredStations = filtered,
            availableRegions = regions,
            availableGenres = genres,
            selectedTab = filters.tab,
            selectedRegion = filters.region,
            selectedGenre = filters.genre,
            searchQuery = filters.query,
            isLoadingOnline = playerInfo.isLoading,
            isFullPlayerVisible = playerInfo.isFullPlayer,
            isSleepTimerDialogVisible = playerInfo.isSleepTimer,
            isSoundProfileDialogVisible = playerInfo.isSoundProfile,
            playerState = playerInfo.playerState,
            favoriteIds = profileInfo.favIds,
            activeBottomTab = profileInfo.bottomTab,
            userName = profileInfo.userName,
            userCity = profileInfo.userCity,
            activeProfileModal = profileInfo.activeModal
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = RadioUiState()
    )

    private data class StationFilters(
        val rawStations: List<RadioStation>,
        val tab: MainCategoryTab,
        val region: String?,
        val genre: String?,
        val query: String
    )

    private data class PlayerAndDialogState(
        val isLoading: Boolean,
        val isFullPlayer: Boolean,
        val isSleepTimer: Boolean,
        val isSoundProfile: Boolean,
        val playerState: PlayerState
    )

    private data class ProfileAndNavState(
        val favIds: Set<String>,
        val bottomTab: BottomNavTab,
        val userName: String,
        val userCity: String,
        val activeModal: ProfileModal
    )

    init {
        fetchOnlineStations()
    }

    fun setBottomNavTab(tab: BottomNavTab) {
        _activeBottomTab.value = tab
    }

    fun openProfileModal(modal: ProfileModal) {
        _activeProfileModal.value = modal
    }

    fun closeProfileModal() {
        _activeProfileModal.value = ProfileModal.NONE
    }

    fun updateProfile(name: String, city: String) {
        val trimmedName = name.trim().ifBlank { "Kolektor Radio" }
        val trimmedCity = city.trim().ifBlank { "Indonesia" }
        _userName.value = trimmedName
        _userCity.value = trimmedCity
        favoritesRepo.saveUserName(trimmedName)
        favoritesRepo.saveUserCity(trimmedCity)
        _activeProfileModal.value = ProfileModal.NONE
    }

    fun fetchOnlineStations() {
        viewModelScope.launch {
            _isLoadingOnline.value = true
            try {
                val remoteStations = withContext(Dispatchers.IO) {
                    try {
                        RadioApiClient.service.getIndonesianStations(limit = 120)
                    } catch (e: Exception) {
                        Log.w(tag, "Primary RadioBrowser server failed, trying fallback: ${e.message}")
                        RadioApiClient.fallbackService.getIndonesianStations(limit = 120)
                    }
                }

                if (remoteStations.isNotEmpty()) {
                    val currentMap = CuratedStations.list.associateBy { it.id }.toMutableMap()
                    for (remote in remoteStations) {
                        if (remote.url.isNotBlank() && !currentMap.containsKey(remote.id)) {
                            val alreadyHas = currentMap.values.any {
                                it.url.equals(remote.url, ignoreCase = true) ||
                                        it.name.equals(remote.name, ignoreCase = true)
                            }
                            if (!alreadyHas) {
                                currentMap[remote.id] = remote
                            }
                        }
                    }
                    _rawStations.value = currentMap.values.toList()
                }
            } catch (e: Exception) {
                Log.e(tag, "Failed to load online stations, keeping curated catalog: ${e.message}")
            } finally {
                _isLoadingOnline.value = false
            }
        }
    }

    fun playStation(station: RadioStation, openPlayer: Boolean = true) {
        favoritesRepo.addRecent(station.id)
        player.playStation(station)
        if (openPlayer) {
            _isFullPlayerVisible.value = true
        }
    }

    fun togglePlayPause() {
        val current = player.state.value.currentStation
        if (current == null) {
            val first = uiState.value.filteredStations.firstOrNull() ?: uiState.value.stations.firstOrNull()
            first?.let { playStation(it, openPlayer = false) }
        } else {
            player.togglePlayPause()
        }
    }

    fun playNext() {
        val list = uiState.value.filteredStations.ifEmpty { uiState.value.stations }
        if (list.isEmpty()) return
        val current = player.state.value.currentStation
        val currentIndex = list.indexOfFirst { it.id == current?.id }
        val nextIndex = if (currentIndex in 0 until list.lastIndex) currentIndex + 1 else 0
        playStation(list[nextIndex], openPlayer = false)
    }

    fun playPrevious() {
        val list = uiState.value.filteredStations.ifEmpty { uiState.value.stations }
        if (list.isEmpty()) return
        val current = player.state.value.currentStation
        val currentIndex = list.indexOfFirst { it.id == current?.id }
        val prevIndex = if (currentIndex > 0) currentIndex - 1 else list.lastIndex
        playStation(list[prevIndex], openPlayer = false)
    }

    fun toggleFavorite(stationId: String) {
        favoritesRepo.toggleFavorite(stationId)
    }

    fun setTab(tab: MainCategoryTab) {
        _selectedTab.value = tab
    }

    fun selectRegion(region: String?) {
        _selectedRegion.value = if (_selectedRegion.value == region) null else region
    }

    fun selectGenre(genre: String?) {
        _selectedGenre.value = if (_selectedGenre.value == genre) null else genre
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun openFullPlayer() {
        _isFullPlayerVisible.value = true
    }

    fun closeFullPlayer() {
        _isFullPlayerVisible.value = false
    }

    fun setSleepTimerDialogVisible(visible: Boolean) {
        _isSleepTimerDialogVisible.value = visible
    }

    fun setSoundProfileDialogVisible(visible: Boolean) {
        _isSoundProfileDialogVisible.value = visible
    }

    fun setVolume(vol: Float) {
        player.setVolume(vol)
    }

    fun setSleepTimer(minutes: Int) {
        player.setSleepTimer(minutes)
        _isSleepTimerDialogVisible.value = false
    }

    fun setSoundProfile(profile: VintageSoundProfile) {
        player.setSoundProfile(profile)
        _isSoundProfileDialogVisible.value = false
    }

    override fun onCleared() {
        super.onCleared()
        player.destroy()
    }
}
