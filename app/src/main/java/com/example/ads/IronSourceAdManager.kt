package com.example.ads

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.facebook.ads.AdSettings
import com.facebook.ads.AudienceNetworkAds
import com.ironsource.mediationsdk.IronSource
import com.ironsource.mediationsdk.ads.nativead.LevelPlayNativeAd
import com.ironsource.mediationsdk.ads.nativead.LevelPlayNativeAdListener
import com.ironsource.mediationsdk.adunit.adapter.utility.AdInfo
import com.ironsource.mediationsdk.logger.IronSourceError
import com.unity3d.mediation.LevelPlay
import com.unity3d.mediation.LevelPlayAdError
import com.unity3d.mediation.LevelPlayAdInfo
import com.unity3d.mediation.LevelPlayConfiguration
import com.unity3d.mediation.LevelPlayInitError
import com.unity3d.mediation.LevelPlayInitListener
import com.unity3d.mediation.LevelPlayInitRequest
import com.unity3d.mediation.interstitial.LevelPlayInterstitialAd
import com.unity3d.mediation.interstitial.LevelPlayInterstitialAdListener
import com.unity3d.mediation.rewarded.LevelPlayReward
import com.unity3d.mediation.rewarded.LevelPlayRewardedAd
import com.unity3d.mediation.rewarded.LevelPlayRewardedAdListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object IronSourceAdManager {
    private const val TAG = "IronSourceAdManager"

    // Exact App Key & Ad Unit IDs from user's ironSource LevelPlay dashboard
    const val APP_KEY = "28849403d"
    const val BANNER_AD_UNIT_ID = "76s7uqsiag1z3h4y"
    const val INTERSTITIAL_AD_UNIT_ID = "8wyu2fy0qdtbdhef"
    const val NATIVE_AD_UNIT_ID = "qy99lqfqur75u1uy"
    const val REWARDED_AD_UNIT_ID = "bceju1t1vqwf6gq7"

    // Tempat meletakkan Placement ID Meta Audience Network (Bidding) jika dibutuhkan langsung di kode
    const val META_BANNER_PLACEMENT_ID = ""
    const val META_INTERSTITIAL_PLACEMENT_ID = ""
    const val META_NATIVE_PLACEMENT_ID = ""
    const val META_REWARDED_PLACEMENT_ID = ""

    // Show interstitial after every 7 station changes
    const val INTERSTITIAL_STATION_SWITCH_THRESHOLD = 7

    private val _isInitialized = MutableStateFlow(false)
    val isInitialized: StateFlow<Boolean> = _isInitialized.asStateFlow()

    private val _isRewardedReady = MutableStateFlow(false)
    val isRewardedReady: StateFlow<Boolean> = _isRewardedReady.asStateFlow()

    private var interstitialAd: LevelPlayInterstitialAd? = null
    private var rewardedAd: LevelPlayRewardedAd? = null
    private var pendingRewardCallback: (() -> Unit)? = null
    private var rewardEarnedInSession = false

    private val mainHandler = Handler(Looper.getMainLooper())

    fun init(context: Context) {
        if (_isInitialized.value) return

        val appContext = context.applicationContext

        // 1. Inisialisasi Meta Audience Network SDK untuk Bidding ironSource LevelPlay
        try {
            if (!AudienceNetworkAds.isInitialized(appContext)) {
                AdSettings.setIntegrationErrorMode(AdSettings.IntegrationErrorMode.INTEGRATION_ERROR_CALLBACK_MODE)
                AudienceNetworkAds.buildInitSettings(appContext)
                    .withInitListener { result ->
                        Log.d(
                            TAG,
                            "Meta Audience Network SDK init: isSuccess=${result.isSuccess}, message=${result.message}"
                        )
                    }
                    .initialize()
            }
            // Aktifkan Meta Adapter Mixed Audience / Bidding metaData pada LevelPlay
            IronSource.setMetaData("Facebook_IS_CacheFlag", "ALL")
        } catch (e: Throwable) {
            Log.w(TAG, "Meta Audience Network pre-init warning: ${e.message}")
        }

        // 2. Inisialisasi Unity LevelPlay (ironSource) SDK
        try {
            val initRequest = LevelPlayInitRequest.Builder(APP_KEY)
                .withLegacyAdFormats(listOf(LevelPlay.AdFormat.NATIVE_AD))
                .build()

            LevelPlay.init(appContext, initRequest, object : LevelPlayInitListener {
                override fun onInitSuccess(configuration: LevelPlayConfiguration) {
                    Log.d(TAG, "LevelPlay SDK + Meta Bidding initialized successfully with AppKey=$APP_KEY")
                    mainHandler.post {
                        _isInitialized.value = true
                        setupInterstitialAd()
                        setupRewardedAd()
                    }
                }

                override fun onInitFailed(error: LevelPlayInitError) {
                    Log.w(TAG, "LevelPlay SDK init failed: ${error.errorCode} - ${error.errorMessage}")
                    mainHandler.post {
                        _isInitialized.value = true
                        setupInterstitialAd()
                        setupRewardedAd()
                    }
                }
            })
        } catch (e: Throwable) {
            Log.e(TAG, "Error initializing LevelPlay SDK: ${e.message}")
        }
    }

    fun onActivityResume(activity: Activity) {
        try {
            IronSource.onResume(activity)
        } catch (e: Throwable) {
            Log.w(TAG, "IronSource.onResume error: ${e.message}")
        }
    }

    fun onActivityPause(activity: Activity) {
        try {
            IronSource.onPause(activity)
        } catch (e: Throwable) {
            Log.w(TAG, "IronSource.onPause error: ${e.message}")
        }
    }

    private fun setupInterstitialAd() {
        try {
            val ad = LevelPlayInterstitialAd(INTERSTITIAL_AD_UNIT_ID)
            ad.setListener(object : LevelPlayInterstitialAdListener {
                override fun onAdLoaded(adInfo: LevelPlayAdInfo) {
                    Log.d(TAG, "Interstitial loaded: $INTERSTITIAL_AD_UNIT_ID")
                }

                override fun onAdLoadFailed(error: LevelPlayAdError) {
                    Log.w(TAG, "Interstitial load failed: $error")
                }

                override fun onAdDisplayed(adInfo: LevelPlayAdInfo) {
                    Log.d(TAG, "Interstitial displayed")
                }

                override fun onAdDisplayFailed(error: LevelPlayAdError, adInfo: LevelPlayAdInfo) {
                    Log.w(TAG, "Interstitial display failed: $error")
                    try {
                        ad.loadAd()
                    } catch (_: Throwable) {
                    }
                }

                override fun onAdClosed(adInfo: LevelPlayAdInfo) {
                    Log.d(TAG, "Interstitial closed, preloading next...")
                    try {
                        ad.loadAd()
                    } catch (_: Throwable) {
                    }
                }
            })
            interstitialAd = ad
            ad.loadAd()
        } catch (e: Throwable) {
            Log.e(TAG, "Error setting up Interstitial Ad: ${e.message}")
        }
    }

    fun showInterstitialIfReady(activity: Activity): Boolean {
        val ad = interstitialAd ?: return false
        return try {
            if (ad.isAdReady()) {
                ad.showAd(activity)
                true
            } else {
                ad.loadAd()
                false
            }
        } catch (e: Throwable) {
            Log.e(TAG, "Error showing Interstitial Ad: ${e.message}")
            false
        }
    }

    private fun setupRewardedAd() {
        try {
            val ad = LevelPlayRewardedAd(REWARDED_AD_UNIT_ID)
            ad.setListener(object : LevelPlayRewardedAdListener {
                override fun onAdLoaded(adInfo: LevelPlayAdInfo) {
                    Log.d(TAG, "Rewarded ad loaded: $REWARDED_AD_UNIT_ID")
                    _isRewardedReady.value = true
                }

                override fun onAdLoadFailed(error: LevelPlayAdError) {
                    Log.w(TAG, "Rewarded ad load failed: $error")
                    _isRewardedReady.value = false
                }

                override fun onAdDisplayed(adInfo: LevelPlayAdInfo) {
                    Log.d(TAG, "Rewarded ad displayed")
                    _isRewardedReady.value = false
                }

                override fun onAdDisplayFailed(error: LevelPlayAdError, adInfo: LevelPlayAdInfo) {
                    Log.w(TAG, "Rewarded ad display failed: $error")
                    _isRewardedReady.value = false
                    pendingRewardCallback?.invoke()
                    pendingRewardCallback = null
                    try {
                        ad.loadAd()
                    } catch (_: Throwable) {
                    }
                }

                override fun onAdRewarded(reward: LevelPlayReward, adInfo: LevelPlayAdInfo) {
                    Log.d(TAG, "User earned reward: ${reward.amount} ${reward.name}")
                    rewardEarnedInSession = true
                    pendingRewardCallback?.invoke()
                    pendingRewardCallback = null
                }

                override fun onAdClosed(adInfo: LevelPlayAdInfo) {
                    Log.d(TAG, "Rewarded ad closed")
                    if (!rewardEarnedInSession) {
                        // Ensure callback is still invoked if ad finished closing
                        pendingRewardCallback?.invoke()
                    }
                    pendingRewardCallback = null
                    rewardEarnedInSession = false
                    try {
                        ad.loadAd()
                    } catch (_: Throwable) {
                    }
                }
            })
            rewardedAd = ad
            ad.loadAd()
        } catch (e: Throwable) {
            Log.e(TAG, "Error setting up Rewarded Ad: ${e.message}")
        }
    }

    fun showRewardedAd(activity: Activity?, onRewardGranted: () -> Unit) {
        val ad = rewardedAd
        if (activity != null && ad != null) {
            try {
                if (ad.isAdReady()) {
                    rewardEarnedInSession = false
                    pendingRewardCallback = onRewardGranted
                    ad.showAd(activity)
                    return
                } else {
                    ad.loadAd()
                }
            } catch (e: Throwable) {
                Log.e(TAG, "Error showing Rewarded Ad: ${e.message}")
            }
        }
        // Fallback when ad is still loading or no fill in test environment so user is never stuck
        onRewardGranted()
    }

    fun createAndLoadNativeAd(
        onLoaded: (LevelPlayNativeAd) -> Unit,
        onFailed: () -> Unit
    ): LevelPlayNativeAd? {
        return try {
            val nativeAd = LevelPlayNativeAd.Builder()
                .withPlacementName(NATIVE_AD_UNIT_ID)
                .withListener(object : LevelPlayNativeAdListener {
                    override fun onAdLoaded(ad: LevelPlayNativeAd?, adInfo: AdInfo?) {
                        Log.d(TAG, "Native ad loaded: $NATIVE_AD_UNIT_ID")
                        if (ad != null) {
                            onLoaded(ad)
                        } else {
                            onFailed()
                        }
                    }

                    override fun onAdLoadFailed(ad: LevelPlayNativeAd?, error: IronSourceError?) {
                        Log.w(TAG, "Native ad load failed: ${error?.errorMessage}")
                        onFailed()
                    }

                    override fun onAdImpression(ad: LevelPlayNativeAd?, adInfo: AdInfo?) {}

                    override fun onAdClicked(ad: LevelPlayNativeAd?, adInfo: AdInfo?) {}
                })
                .build()
            nativeAd.loadAd()
            nativeAd
        } catch (e: Throwable) {
            Log.e(TAG, "Error creating Native Ad: ${e.message}")
            onFailed()
            null
        }
    }
}
