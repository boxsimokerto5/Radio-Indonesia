package com.example.ui.components

import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.ads.IronSourceAdManager
import com.example.ui.theme.VintageBorderSepia
import com.example.ui.theme.VintageBorderStrong
import com.example.ui.theme.VintageGoldOchre
import com.example.ui.theme.VintageParchmentCard
import com.example.ui.theme.VintageParchmentCardElevated
import com.example.ui.theme.VintageParchmentDark
import com.example.ui.theme.VintageTerracotta
import com.example.ui.theme.VintageTextDimSepia
import com.example.ui.theme.VintageTextEspresso
import com.example.ui.theme.VintageTextMutedSepia
import com.example.ui.theme.VintageTextWarmBrown
import com.ironsource.mediationsdk.ads.nativead.LevelPlayNativeAd
import com.unity3d.mediation.LevelPlayAdError
import com.unity3d.mediation.LevelPlayAdInfo
import com.unity3d.mediation.LevelPlayAdSize
import com.unity3d.mediation.banner.LevelPlayBannerAdView
import com.unity3d.mediation.banner.LevelPlayBannerAdViewListener

/**
 * Komponen Banner Iklan ironSource LevelPlay (Ad Unit ID: 76s7uqsiag1z3h4y)
 * Ditempatkan tepat di antara kolom pencarian dan tab kategori.
 */
@Composable
fun IronSourceBannerAd(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val activity = context as? android.app.Activity
    val isInitialized by IronSourceAdManager.isInitialized.collectAsState()
    var isAdLoaded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(VintageParchmentCard)
            .border(1.dp, VintageBorderSepia, RoundedCornerShape(10.dp))
            .testTag("ironsource_banner_container"),
        contentAlignment = Alignment.Center
    ) {
        if (isInitialized && activity != null) {
            AndroidView(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                factory = { ctx ->
                    FrameLayout(ctx).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        )
                        try {
                            val bannerView = LevelPlayBannerAdView(
                                activity,
                                IronSourceAdManager.BANNER_AD_UNIT_ID
                            )
                            bannerView.setAdSize(LevelPlayAdSize.BANNER)
                            bannerView.setBannerListener(object : LevelPlayBannerAdViewListener {
                                override fun onAdLoaded(adInfo: LevelPlayAdInfo) {
                                    isAdLoaded = true
                                }

                                override fun onAdLoadFailed(error: LevelPlayAdError) {
                                    isAdLoaded = false
                                }
                            })
                            val lp = FrameLayout.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                Gravity.CENTER
                            )
                            addView(bannerView, lp)
                            bannerView.loadAd()
                        } catch (_: Throwable) {
                        }
                    }
                },
                onRelease = { frame ->
                    for (i in 0 until frame.childCount) {
                        val child = frame.getChildAt(i)
                        if (child is LevelPlayBannerAdView) {
                            try {
                                child.destroy()
                            } catch (_: Throwable) {
                            }
                        }
                    }
                    frame.removeAllViews()
                }
            )
        }

        if (!isAdLoaded) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(VintageGoldOchre)
                            .padding(horizontal = 5.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "AD",
                            color = Color.White,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "Radio Indonesia • Suara Nusantara",
                            color = VintageTextEspresso,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "Banner ironSource (${IronSourceAdManager.BANNER_AD_UNIT_ID})",
                            color = VintageTextMutedSepia,
                            fontSize = 9.5.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                Icon(
                    imageVector = Icons.Filled.Campaign,
                    contentDescription = null,
                    tint = VintageTerracotta,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

/**
 * Komponen Iklan Native ironSource LevelPlay (Ad Unit ID: qy99lqfqur75u1uy)
 * Disematkan di antara daftar channel radio.
 */
@Composable
fun IronSourceNativeAdCard(
    slotIndex: Int,
    modifier: Modifier = Modifier
) {
    val isInitialized by IronSourceAdManager.isInitialized.collectAsState()
    var nativeAd by remember { mutableStateOf<LevelPlayNativeAd?>(null) }

    DisposableEffect(isInitialized, slotIndex) {
        var adInstance: LevelPlayNativeAd? = null
        if (isInitialized) {
            adInstance = IronSourceAdManager.createAndLoadNativeAd(
                onLoaded = { loadedAd -> nativeAd = loadedAd },
                onFailed = { nativeAd = null }
            )
        }
        onDispose {
            try {
                adInstance?.destroyAd()
            } catch (_: Exception) {
            }
        }
    }

    val titleText = nativeAd?.title?.takeIf { it.isNotBlank() } ?: "Sponsor Radio Indonesia"
    val bodyText = nativeAd?.body?.takeIf { it.isNotBlank() }
        ?: "Nikmati streaming radio nusantara jernih sepanjang hari bersama mitra pilihan kami."
    val advertiserText = nativeAd?.advertiser?.takeIf { it.isNotBlank() }
        ?: "ironSource Native • ${IronSourceAdManager.NATIVE_AD_UNIT_ID}"
    val ctaText = nativeAd?.callToAction?.takeIf { it.isNotBlank() } ?: "Selengkapnya"

    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = 2.dp, shape = RoundedCornerShape(14.dp))
            .clip(RoundedCornerShape(14.dp))
            .border(1.2.dp, VintageBorderStrong, RoundedCornerShape(14.dp))
            .testTag("ironsource_native_ad_$slotIndex"),
        colors = CardDefaults.cardColors(containerColor = VintageParchmentCardElevated),
        shape = RoundedCornerShape(14.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(VintageParchmentDark)
                    .border(1.dp, VintageBorderStrong, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Campaign,
                    contentDescription = "Iklan Native",
                    tint = VintageTerracotta,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(VintageGoldOchre)
                            .padding(horizontal = 5.dp, vertical = 1.5.dp)
                    ) {
                        Text(
                            text = "IKLAN SPONSOR",
                            color = Color.White,
                            fontSize = 8.5.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = advertiserText,
                        color = VintageTextDimSepia,
                        fontSize = 9.5.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = titleText,
                    color = VintageTextEspresso,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = bodyText,
                    color = VintageTextWarmBrown,
                    fontSize = 11.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(containerColor = VintageTerracotta),
                shape = RoundedCornerShape(8.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(
                    horizontal = 10.dp,
                    vertical = 6.dp
                )
            ) {
                Text(
                    text = ctaText,
                    color = Color.White,
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
