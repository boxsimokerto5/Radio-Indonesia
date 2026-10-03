package com.example

import com.example.data.CuratedStations
import com.example.model.RadioStation
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun testCuratedStationsNotNullOrEmpty() {
        val stations = CuratedStations.list
        assertTrue("Curated stations should not be empty", stations.isNotEmpty())
        assertTrue("Should have at least 15 curated stations", stations.size >= 15)
        
        stations.forEach { station ->
            assertTrue("Station ${station.name} must have a valid stream URL", station.url.isNotBlank())
            assertTrue("Station ${station.name} must have a valid ID", station.id.isNotBlank())
            assertTrue("Station ${station.name} must have a non-blank display region", station.displayRegion.isNotBlank())
            assertTrue("Station ${station.name} must have a non-blank display genre", station.displayGenre.isNotBlank())
        }
    }

    @Test
    fun testStationGenreParsing() {
        val dangdutStation = RadioStation(
            id = "test_1",
            name = "Radio Dangdut Asik",
            url = "https://stream.example.com",
            tags = "dangdut, koplo"
        )
        assertEquals("Dangdut", dangdutStation.displayGenre)

        val newsStation = RadioStation(
            id = "test_2",
            name = "Radio Elshinta Berita",
            url = "https://stream.example.com",
            tags = "news, talk"
        )
        assertEquals("Berita & Info", newsStation.displayGenre)

        val dakwahStation = RadioStation(
            id = "test_3",
            name = "Radio Kajian Rodja",
            url = "https://stream.example.com",
            tags = "islam, dakwah"
        )
        assertEquals("Religi & Dakwah", dakwahStation.displayGenre)
    }

    @Test
    fun testStationRegionParsing() {
        val jakartaStation = RadioStation(
            id = "test_jkt",
            name = "Radio Jakarta",
            url = "https://stream.example.com",
            state = "DKI Jakarta"
        )
        assertEquals("DKI Jakarta", jakartaStation.displayRegion)

        val surabayaStation = RadioStation(
            id = "test_sby",
            name = "Radio Surabaya",
            url = "https://stream.example.com",
            state = "East Java"
        )
        assertEquals("Jawa Timur", surabayaStation.displayRegion)
    }
}

