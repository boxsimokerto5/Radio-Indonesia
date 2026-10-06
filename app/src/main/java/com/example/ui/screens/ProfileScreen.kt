package com.example.ui.screens

import android.content.Intent
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.example.viewmodel.ProfileModal
import com.example.viewmodel.RadioViewModel

@Composable
fun ProfileScreen(
    viewModel: RadioViewModel,
    userName: String,
    userCity: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(VintageParchmentBg)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .testTag("profile_screen"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Title
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "PROFIL SAYA",
                color = VintageTextEspresso,
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif,
                letterSpacing = 1.2.sp
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // 1. PALING ATAS: Custom Nama dan Kota (Header Profil)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(elevation = 2.dp, shape = RoundedCornerShape(16.dp))
                .clip(RoundedCornerShape(16.dp))
                .border(1.2.dp, VintageBorderStrong, RoundedCornerShape(16.dp))
                .clickable { viewModel.openProfileModal(ProfileModal.EDIT_PROFILE) }
                .testTag("profile_header_card"),
            colors = CardDefaults.cardColors(containerColor = VintageParchmentCard)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Classic Avatar
                Box(
                    modifier = Modifier
                        .size(58.dp)
                        .clip(CircleShape)
                        .background(VintageTerracotta)
                        .border(2.dp, VintageBorderStrong, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = "Avatar Pengguna",
                        tint = Color.White,
                        modifier = Modifier.size(36.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                // Name & City
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = userName,
                        color = VintageTextEspresso,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.LocationOn,
                            contentDescription = null,
                            tint = VintageTerracotta,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = userCity,
                            color = VintageTextWarmBrown,
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.Medium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                // Edit Button
                IconButton(
                    onClick = { viewModel.openProfileModal(ProfileModal.EDIT_PROFILE) },
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(VintageParchmentDark)
                        .testTag("edit_profile_button")
                ) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Ubah Profil",
                        tint = VintageTerracotta,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Section Title: Layanan & Utilitas
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Layanan & Fitur Pembantu",
                color = VintageTextWarmBrown,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 2. DIBAWAHNYA KOTAK-KOTAK MENU: Kalender, Cuaca Hari Ini (BMKG), Kalkulator
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Kotak 1: Kalender
            UtilityBoxCard(
                title = "Kalender",
                subtitle = "Google Cal",
                icon = Icons.Filled.CalendarMonth,
                iconTint = VintageTerracotta,
                modifier = Modifier
                    .weight(1f)
                    .testTag("menu_kalender"),
                onClick = { viewModel.openProfileModal(ProfileModal.CALENDAR) }
            )

            // Kotak 2: Cuaca Hari Ini (BMKG)
            UtilityBoxCard(
                title = "Cuaca BMKG",
                subtitle = "Seluruh Kota",
                icon = Icons.Filled.Cloud,
                iconTint = VintageGoldOchre,
                modifier = Modifier
                    .weight(1f)
                    .testTag("menu_cuaca"),
                onClick = { viewModel.openProfileModal(ProfileModal.WEATHER) }
            )

            // Kotak 3: Kalkulator
            UtilityBoxCard(
                title = "Kalkulator",
                subtitle = "Hitung Cepat",
                icon = Icons.Filled.Calculate,
                iconTint = VintageTextWarmBrown,
                modifier = Modifier
                    .weight(1f)
                    .testTag("menu_kalkulator"),
                onClick = { viewModel.openProfileModal(ProfileModal.CALCULATOR) }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Section Title: Informasi & Pengaturan
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Informasi & Dukungan",
                color = VintageTextWarmBrown,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 3. DIBAWAHNYA: Kebijakan Privasi & Tentang Aplikasi
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(elevation = 2.dp, shape = RoundedCornerShape(14.dp))
                .clip(RoundedCornerShape(14.dp))
                .border(1.2.dp, VintageBorderSepia, RoundedCornerShape(14.dp)),
            colors = CardDefaults.cardColors(containerColor = VintageParchmentCard)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Item 1: Kebijakan Privasi
                ProfileListItem(
                    icon = Icons.Filled.Policy,
                    iconTint = VintageTextWarmBrown,
                    title = "Kebijakan Privasi",
                    subtitle = "Informasi perlindungan privasi pendengar",
                    onClick = { viewModel.openProfileModal(ProfileModal.PRIVACY) },
                    testTag = "menu_privacy"
                )

                Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(VintageBorderSepia))

                // Item 2: Tentang Aplikasi
                ProfileListItem(
                    icon = Icons.Filled.Info,
                    iconTint = VintageTerracotta,
                    title = "Tentang Aplikasi",
                    subtitle = "Radio Indonesia v1.0",
                    onClick = { viewModel.openProfileModal(ProfileModal.ABOUT) },
                    testTag = "menu_about"
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))
    }
}

@Composable
fun UtilityBoxCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconTint: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .shadow(elevation = 2.dp, shape = RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .border(1.2.dp, VintageBorderStrong, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = VintageParchmentCard)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(VintageParchmentDark)
                    .border(1.dp, VintageBorderSepia, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = iconTint,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = title,
                color = VintageTextEspresso,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = subtitle,
                color = VintageTextMutedSepia,
                fontSize = 9.5.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun ProfileListItem(
    icon: ImageVector,
    iconTint: Color,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    testTag: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 12.dp)
            .testTag(testTag),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(VintageParchmentDark),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = title, tint = iconTint, modifier = Modifier.size(20.dp))
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = VintageTextEspresso,
                fontSize = 13.5.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                color = VintageTextWarmBrown,
                fontSize = 11.sp
            )
        }

        Icon(
            imageVector = Icons.Filled.ChevronRight,
            contentDescription = null,
            tint = VintageTextDimSepia,
            modifier = Modifier.size(20.dp)
        )
    }
}
