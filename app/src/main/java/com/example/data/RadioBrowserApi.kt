package com.example.data

import com.example.model.RadioStation
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface RadioBrowserService {
    @GET("json/stations/bycountrycodeexact/ID")
    suspend fun getIndonesianStations(
        @Query("hidebroken") hideBroken: Boolean = true,
        @Query("order") order: String = "clickcount",
        @Query("reverse") reverse: Boolean = true,
        @Query("limit") limit: Int = 100
    ): List<RadioStation>
}

object RadioApiClient {
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://de1.api.radio-browser.info/")
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val service: RadioBrowserService = retrofit.create(RadioBrowserService::class.java)

    // Fallback service
    private val fallbackRetrofit = Retrofit.Builder()
        .baseUrl("https://all.api.radio-browser.info/")
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val fallbackService: RadioBrowserService = fallbackRetrofit.create(RadioBrowserService::class.java)
}
