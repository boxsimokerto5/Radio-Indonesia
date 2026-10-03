package com.example.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RadioStation(
    @Json(name = "stationuuid") val id: String,
    @Json(name = "name") val name: String,
    @Json(name = "url_resolved") val url: String,
    @Json(name = "homepage") val homepage: String = "",
    @Json(name = "favicon") val favicon: String = "",
    @Json(name = "tags") val tags: String = "",
    @Json(name = "country") val country: String = "Indonesia",
    @Json(name = "countrycode") val countryCode: String = "ID",
    @Json(name = "state") val state: String = "",
    @Json(name = "bitrate") val bitrate: Int = 128,
    @Json(name = "codec") val codec: String = "MP3",
    @Json(name = "clickcount") val clickCount: Int = 0,
    val isFavorite: Boolean = false,
    val customGenre: String? = null,
    val customRegion: String? = null
) {
    val displayRegion: String
        get() {
            if (!customRegion.isNullOrBlank()) return customRegion
            val s = state.trim()
            return when {
                s.contains("Jakarta", ignoreCase = true) -> "DKI Jakarta"
                s.contains("Jawa Barat", ignoreCase = true) || s.contains("West Java", ignoreCase = true) || s.contains("Bandung", ignoreCase = true) -> "Jawa Barat"
                s.contains("Jawa Tengah", ignoreCase = true) || s.contains("Central Java", ignoreCase = true) || s.contains("Semarang", ignoreCase = true) || s.contains("Solo", ignoreCase = true) -> "Jawa Tengah"
                s.contains("Yogyakarta", ignoreCase = true) || s.contains("Jogja", ignoreCase = true) -> "DIY Yogyakarta"
                s.contains("Jawa Timur", ignoreCase = true) || s.contains("East Java", ignoreCase = true) || s.contains("Surabaya", ignoreCase = true) || s.contains("Malang", ignoreCase = true) -> "Jawa Timur"
                s.contains("Bali", ignoreCase = true) || s.contains("Denpasar", ignoreCase = true) -> "Bali"
                s.contains("Sumatera", ignoreCase = true) || s.contains("Sumatra", ignoreCase = true) || s.contains("Medan", ignoreCase = true) || s.contains("Palembang", ignoreCase = true) -> "Sumatera"
                s.contains("Kalimantan", ignoreCase = true) -> "Kalimantan"
                s.contains("Sulawesi", ignoreCase = true) || s.contains("Makassar", ignoreCase = true) -> "Sulawesi"
                s.isNotBlank() -> s
                else -> "Nasional"
            }
        }

    val displayGenre: String
        get() {
            if (!customGenre.isNullOrBlank()) return customGenre
            val t = (tags + " " + name).lowercase()
            return when {
                t.contains("dangdut") || t.contains("campursari") -> "Dangdut"
                t.contains("dakwah") || t.contains("islam") || t.contains("quran") || t.contains("rodja") || t.contains("syiar") || t.contains("religi") -> "Religi & Dakwah"
                t.contains("news") || t.contains("berita") || t.contains("talk") || t.contains("informasi") || t.contains("elshinta") || t.contains("sonora") -> "Berita & Info"
                t.contains("rock") || t.contains("metal") || t.contains("indie") -> "Rock & Alternatif"
                t.contains("jazz") || t.contains("oldies") || t.contains("nostalgia") || t.contains("kenangan") || t.contains("classic") -> "Nostalgia & Jazz"
                t.contains("budaya") || t.contains("tradisi") || t.contains("jawa") || t.contains("sunda") -> "Budaya Daerah"
                t.contains("top 40") || t.contains("hits") || t.contains("pop") || t.contains("muda") || t.contains("music") -> "Pop & Hits"
                else -> "Variasi Musik"
            }
        }

    val bitrateFormatted: String
        get() = if (bitrate > 0) "$bitrate kbps" else "128 kbps"
}
