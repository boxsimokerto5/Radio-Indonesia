package com.example.data

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FavoritesRepository(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("vintage_radio_prefs", Context.MODE_PRIVATE)

    private val _favoriteIds = MutableStateFlow<Set<String>>(loadFavorites())
    val favoriteIds: StateFlow<Set<String>> = _favoriteIds.asStateFlow()

    private val _recentIds = MutableStateFlow<List<String>>(loadRecents())
    val recentIds: StateFlow<List<String>> = _recentIds.asStateFlow()

    private fun loadFavorites(): Set<String> {
        return prefs.getStringSet("favorite_station_ids", emptySet()) ?: emptySet()
    }

    private fun loadRecents(): List<String> {
        val raw = prefs.getString("recent_station_ids", "") ?: ""
        return if (raw.isBlank()) emptyList() else raw.split(",")
    }

    fun toggleFavorite(stationId: String): Boolean {
        val current = _favoriteIds.value.toMutableSet()
        val isNowFav = if (current.contains(stationId)) {
            current.remove(stationId)
            false
        } else {
            current.add(stationId)
            true
        }
        prefs.edit().putStringSet("favorite_station_ids", current).apply()
        _favoriteIds.value = current
        return isNowFav
    }

    fun isFavorite(stationId: String): Boolean {
        return _favoriteIds.value.contains(stationId)
    }

    fun addRecent(stationId: String) {
        val current = _recentIds.value.toMutableList()
        current.remove(stationId)
        current.add(0, stationId)
        val trimmed = current.take(20)
        prefs.edit().putString("recent_station_ids", trimmed.joinToString(",")).apply()
        _recentIds.value = trimmed
    }

    fun getUserName(): String {
        return prefs.getString("user_profile_name", "Kolektor Radio") ?: "Kolektor Radio"
    }

    fun saveUserName(name: String) {
        prefs.edit().putString("user_profile_name", name).apply()
    }

    fun getUserCity(): String {
        return prefs.getString("user_profile_city", "Jakarta, Indonesia") ?: "Jakarta, Indonesia"
    }

    fun saveUserCity(city: String) {
        prefs.edit().putString("user_profile_city", city).apply()
    }
}
