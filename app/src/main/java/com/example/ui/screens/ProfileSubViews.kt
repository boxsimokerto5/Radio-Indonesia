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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.Radio
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.VintageBorderSepia
import com.example.ui.theme.VintageBorderStrong
import com.example.ui.theme.VintageGoldOchre
import com.example.ui.theme.VintageParchmentBg
import com.example.ui.theme.VintageParchmentCard
import com.example.ui.theme.VintageParchmentCardElevated
import com.example.ui.theme.VintageParchmentDark
import com.example.ui.theme.VintageSuccessGreen
import com.example.ui.theme.VintageTerracotta
import com.example.ui.theme.VintageTerracottaSoft
import com.example.ui.theme.VintageTextDimSepia
import com.example.ui.theme.VintageTextEspresso
import com.example.ui.theme.VintageTextMutedSepia
import com.example.ui.theme.VintageTextWarmBrown

// Dialog 1: Edit Profile (Nama & Kota)
@Composable
fun EditProfileDialog(
    initialName: String,
    initialCity: String,
    onSave: (String, String) -> Unit,
    onDismiss: () -> Unit
) {
    var name by remember { mutableStateOf(initialName) }
    var city by remember { mutableStateOf(initialCity) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clip(RoundedCornerShape(16.dp))
                .border(1.5.dp, VintageBorderStrong, RoundedCornerShape(16.dp))
                .testTag("edit_profile_dialog"),
            colors = CardDefaults.cardColors(containerColor = VintageParchmentCard)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Ubah Profil Pendengar",
                        color = VintageTextEspresso,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif
                    )
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(imageVector = Icons.Filled.Close, contentDescription = "Tutup", tint = VintageTextEspresso)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Nama Anda", color = VintageTextWarmBrown, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("profile_name_input"),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = VintageParchmentDark,
                        unfocusedContainerColor = VintageParchmentDark,
                        focusedTextColor = VintageTextEspresso,
                        unfocusedTextColor = VintageTextEspresso,
                        focusedIndicatorColor = VintageTerracotta,
                        unfocusedIndicatorColor = VintageBorderStrong
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(text = "Kota Domisili", color = VintageTextWarmBrown, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = city,
                    onValueChange = { city = it },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("profile_city_input"),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = VintageParchmentDark,
                        unfocusedContainerColor = VintageParchmentDark,
                        focusedTextColor = VintageTextEspresso,
                        unfocusedTextColor = VintageTextEspresso,
                        focusedIndicatorColor = VintageTerracotta,
                        unfocusedIndicatorColor = VintageBorderStrong
                    )
                )

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = { onSave(name, city) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("save_profile_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = VintageTerracotta)
                ) {
                    Text(text = "Simpan Perubahan", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// Dialog 2: Kalender & Catatan (Google Calendar Connected)
@Composable
fun CalendarDialog(onDismiss: () -> Unit) {
    val days = (1..31).toList()
    var notes by remember {
        mutableStateOf(
            listOf(
                "08:00 - Warta Berita Pagi RRI",
                "15:30 - Siaran Sandiwara Radio Jadul",
                "20:00 - Tembang Kenangan & Jazz Malam"
            )
        )
    }
    var newNoteText by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .clip(RoundedCornerShape(18.dp))
                .border(1.5.dp, VintageBorderStrong, RoundedCornerShape(18.dp))
                .testTag("calendar_dialog"),
            colors = CardDefaults.cardColors(containerColor = VintageParchmentCard)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Filled.CalendarMonth, contentDescription = null, tint = VintageTerracotta)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Kalender & Catatan Acara",
                            color = VintageTextEspresso,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif
                        )
                    }
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(imageVector = Icons.Filled.Close, contentDescription = "Tutup", tint = VintageTextEspresso)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Google Calendar status badge
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .background(VintageParchmentDark)
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Filled.Sync, contentDescription = null, tint = VintageSuccessGreen, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "Tersinkronisasi dengan Google Kalender", color = VintageTextWarmBrown, fontSize = 10.5.sp, fontWeight = FontWeight.SemiBold)
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Month header
                Text(
                    text = "Oktober 2026",
                    color = VintageTerracotta,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Days grid mini preview
                val dayNames = listOf("M", "S", "S", "R", "K", "J", "S")
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                    dayNames.forEach {
                        Text(text = it, color = VintageTextMutedSepia, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Day numbers (5 rows of 7)
                for (row in 0..4) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                        for (col in 0..6) {
                            val dayNum = row * 7 + col + 1
                            if (dayNum <= 31) {
                                val isToday = dayNum == 4
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .background(if (isToday) VintageTerracotta else Color.Transparent),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "$dayNum",
                                        color = if (isToday) Color.White else VintageTextEspresso,
                                        fontSize = 11.sp,
                                        fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            } else {
                                Spacer(modifier = Modifier.size(24.dp))
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                Text(text = "Catatan Pengingat Siaran:", color = VintageTextEspresso, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(6.dp))

                // Notes list
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    notes.forEach { note ->
                        Text(
                            text = "• $note",
                            color = VintageTextWarmBrown,
                            fontSize = 11.5.sp,
                            modifier = Modifier.padding(vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Add note input
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = newNoteText,
                        onValueChange = { newNoteText = it },
                        placeholder = { Text("Tulis pengingat acara...", color = VintageTextDimSepia, fontSize = 11.sp) },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = VintageParchmentDark,
                            unfocusedContainerColor = VintageParchmentDark,
                            focusedTextColor = VintageTextEspresso,
                            unfocusedTextColor = VintageTextEspresso
                        )
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    IconButton(
                        onClick = {
                            if (newNoteText.isNotBlank()) {
                                notes = notes + newNoteText.trim()
                                newNoteText = ""
                            }
                        },
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(VintageTerracotta)
                    ) {
                        Icon(imageVector = Icons.Filled.Add, contentDescription = "Tambah", tint = Color.White)
                    }
                }
            }
        }
    }
}

// Dialog 3: Cuaca BMKG
data class CityWeather(val city: String, val temp: String, val condition: String, val humidity: String)

@Composable
fun WeatherDialog(userCity: String, onDismiss: () -> Unit) {
    val weatherData = remember {
        listOf(
            CityWeather("DKI Jakarta", "31°C", "Cerah Berawan", "68%"),
            CityWeather("Bandung", "24°C", "Hujan Ringan", "82%"),
            CityWeather("Surabaya", "33°C", "Cerah", "62%"),
            CityWeather("Yogyakarta", "29°C", "Berawan", "72%"),
            CityWeather("Semarang", "32°C", "Cerah Berawan", "65%"),
            CityWeather("Denpasar Bali", "30°C", "Cerah", "70%"),
            CityWeather("Medan", "31°C", "Hujan Sedang", "78%"),
            CityWeather("Makassar", "32°C", "Cerah", "66%"),
            CityWeather("Palembang", "31°C", "Berawan", "75%"),
            CityWeather("Banjarmasin", "30°C", "Hujan Petir", "80%")
        )
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
                .clip(RoundedCornerShape(18.dp))
                .border(1.5.dp, VintageBorderStrong, RoundedCornerShape(18.dp))
                .testTag("weather_dialog"),
            colors = CardDefaults.cardColors(containerColor = VintageParchmentCard)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Filled.Cloud, contentDescription = null, tint = VintageTerracotta)
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "Prakiraan Cuaca BMKG",
                                color = VintageTextEspresso,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Serif
                            )
                            Text(text = "Sumber Data: BMKG Indonesia", color = VintageTextMutedSepia, fontSize = 10.sp)
                        }
                    }
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(imageVector = Icons.Filled.Close, contentDescription = "Tutup", tint = VintageTextEspresso)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Primary city card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(VintageParchmentDark)
                        .border(1.dp, VintageBorderStrong, RoundedCornerShape(12.dp))
                        .padding(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = userCity, color = VintageTextEspresso, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(text = "Cerah Berawan • Kelembaban 68%", color = VintageTextWarmBrown, fontSize = 11.5.sp)
                        }
                        Text(text = "30°C", color = VintageTerracotta, fontSize = 28.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Serif)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                Text(text = "Kota-Kota di Indonesia Lainnya:", color = VintageTextEspresso, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(6.dp))

                // Cities list
                LazyColumn(modifier = Modifier.height(180.dp)) {
                    items(weatherData) { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(VintageParchmentCardElevated)
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(text = item.city, color = VintageTextEspresso, fontSize = 12.5.sp, fontWeight = FontWeight.SemiBold)
                                Text(text = "${item.condition} • Lembab ${item.humidity}", color = VintageTextMutedSepia, fontSize = 10.sp)
                            }
                            Text(text = item.temp, color = VintageTerracotta, fontSize = 13.5.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

// Dialog 4: Kalkulator Vintage
@Composable
fun CalculatorDialog(onDismiss: () -> Unit) {
    var display by remember { mutableStateOf("0") }
    var operand1 by remember { mutableStateOf<Double?>(null) }
    var operator by remember { mutableStateOf<String?>(null) }
    var isNewEntry by remember { mutableStateOf(true) }

    fun onDigit(d: String) {
        if (isNewEntry || display == "0") {
            display = d
            isNewEntry = false
        } else {
            if (display.length < 10) display += d
        }
    }

    fun onOp(op: String) {
        operand1 = display.toDoubleOrNull()
        operator = op
        isNewEntry = true
    }

    fun onEquals() {
        val op2 = display.toDoubleOrNull()
        val op1 = operand1
        val op = operator
        if (op1 != null && op2 != null && op != null) {
            val res = when (op) {
                "+" -> op1 + op2
                "-" -> op1 - op2
                "×" -> op1 * op2
                "÷" -> if (op2 != 0.0) op1 / op2 else 0.0
                else -> op2
            }
            display = if (res % 1.0 == 0.0) res.toLong().toString() else "%.2f".format(res)
            operand1 = null
            operator = null
            isNewEntry = true
        }
    }

    fun onClear() {
        display = "0"
        operand1 = null
        operator = null
        isNewEntry = true
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
                .clip(RoundedCornerShape(18.dp))
                .border(2.dp, VintageBorderStrong, RoundedCornerShape(18.dp))
                .testTag("calculator_dialog"),
            colors = CardDefaults.cardColors(containerColor = VintageParchmentCard)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Filled.Calculate, contentDescription = null, tint = VintageTerracotta)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Kalkulator Klasik",
                            color = VintageTextEspresso,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif
                        )
                    }
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(imageVector = Icons.Filled.Close, contentDescription = "Tutup", tint = VintageTextEspresso)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Display screen
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(VintageParchmentDark)
                        .border(1.2.dp, VintageBorderStrong, RoundedCornerShape(8.dp))
                        .padding(horizontal = 14.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Text(
                        text = display,
                        color = VintageTextEspresso,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                val buttonLayout = listOf(
                    listOf("C", "÷", "×", "-"),
                    listOf("7", "8", "9", "+"),
                    listOf("4", "5", "6", "="),
                    listOf("1", "2", "3", "0")
                )

                buttonLayout.forEach { row ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        row.forEach { btn ->
                            val isAction = btn in listOf("C", "÷", "×", "-", "+", "=")
                            val isAccent = btn == "=" || btn == "C"

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(
                                        if (isAccent) VintageTerracotta else if (isAction) VintageParchmentDark else VintageParchmentCardElevated
                                    )
                                    .border(
                                        1.dp,
                                        if (isAccent) VintageTerracotta else VintageBorderStrong,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .clickable {
                                        when (btn) {
                                            "C" -> onClear()
                                            "=" -> onEquals()
                                            "÷", "×", "-", "+" -> onOp(btn)
                                            else -> onDigit(btn)
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = btn,
                                    color = if (isAccent) Color.White else VintageTextEspresso,
                                    fontSize = 17.sp,
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

// Dialog 5: Beri Rating Aplikasi
@Composable
fun RatingDialog(onDismiss: () -> Unit) {
    var rating by remember { mutableStateOf(5) }
    var submitted by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clip(RoundedCornerShape(18.dp))
                .border(1.5.dp, VintageBorderStrong, RoundedCornerShape(18.dp))
                .testTag("rating_dialog"),
            colors = CardDefaults.cardColors(containerColor = VintageParchmentCard)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(imageVector = Icons.Filled.Close, contentDescription = "Tutup", tint = VintageTextEspresso)
                    }
                }

                Icon(imageVector = Icons.Filled.Star, contentDescription = null, tint = VintageGoldOchre, modifier = Modifier.size(48.dp))
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Beri Ulasan Radio Indonesia",
                    color = VintageTextEspresso,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Bantu kami mengembangkan siaran radio klasik terbaik untuk seluruh nusantara.",
                    color = VintageTextWarmBrown,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Stars
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    for (i in 1..5) {
                        IconButton(
                            onClick = { rating = i },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = if (i <= rating) Icons.Filled.Star else Icons.Filled.StarBorder,
                                contentDescription = "$i Bintang",
                                tint = if (i <= rating) VintageGoldOchre else VintageBorderStrong,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (submitted) {
                    Text(
                        text = "Terima kasih banyak atas apresiasi Anda! ⭐",
                        color = VintageSuccessGreen,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                } else {
                    Button(
                        onClick = { submitted = true },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = VintageTerracotta)
                    ) {
                        Text(text = "Kirim Penilaian", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

// Dialog 6: Kebijakan Privasi (Privacy Policy)
@Composable
fun PrivacyPolicyDialog(onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clip(RoundedCornerShape(18.dp))
                .border(1.5.dp, VintageBorderStrong, RoundedCornerShape(18.dp))
                .testTag("privacy_policy_dialog"),
            colors = CardDefaults.cardColors(containerColor = VintageParchmentCard)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Filled.Policy, contentDescription = null, tint = VintageTerracotta)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Kebijakan Privasi",
                            color = VintageTextEspresso,
                            fontSize = 16.5.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif
                        )
                    }
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(imageVector = Icons.Filled.Close, contentDescription = "Tutup", tint = VintageTextEspresso)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = "1. Pengumpulan Data\nAplikasi Radio Indonesia tidak mengumpulkan, menjual, atau melacak data pribadi pengguna ke pihak ketiga. Pengaturan profil (nama dan kota) disimpan secara lokal di perangkat Anda.\n\n" +
                                "2. Akses Jaringan & Audio\nAplikasi menggunakan izin Internet semata-mata untuk memuat daftar stasiun radio dan streaming transmisi audio dari server radio resmi.\n\n" +
                                "3. Hak Cipta Siaran\nSeluruh hak siar konten audio, logo, dan musik adalah milik masing-masing pemilik stasiun radio terkait.\n\n" +
                                "4. Keamanan\nKami berkomitmen menjaga pengalaman mendengarkan yang aman, bebas dari pelacakan yang tidak diinginkan.",
                        color = VintageTextWarmBrown,
                        fontSize = 12.sp,
                        lineHeight = 17.sp
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = VintageTerracotta)
                ) {
                    Text(text = "Saya Mengerti", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// Dialog 7: Tentang Aplikasi (About App)
@Composable
fun AboutAppDialog(onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clip(RoundedCornerShape(18.dp))
                .border(1.5.dp, VintageBorderStrong, RoundedCornerShape(18.dp))
                .testTag("about_app_dialog"),
            colors = CardDefaults.cardColors(containerColor = VintageParchmentCard)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(imageVector = Icons.Filled.Close, contentDescription = "Tutup", tint = VintageTextEspresso)
                    }
                }

                Icon(imageVector = Icons.Filled.Radio, contentDescription = null, tint = VintageTerracotta, modifier = Modifier.size(54.dp))
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Radio Indonesia",
                    color = VintageTextEspresso,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif
                )
                Text(text = "Versi 1.0 • Edisi Kertas Antik", color = VintageTextMutedSepia, fontSize = 11.5.sp)

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Aplikasi radio online Indonesia bernuansa analog klasik, menghadirkan koleksi siaran dari Sabang sampai Merauke berdasarkan wilayah dan genre dengan kualitas pemutaran stabil.",
                    color = VintageTextWarmBrown,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 17.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(VintageParchmentDark)
                        .padding(10.dp)
                ) {
                    Column {
                        Text(text = "• Transmisi: AndroidX Media3 ExoPlayer", color = VintageTextEspresso, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                        Text(text = "• Database: Radio Browser Community API & Terkurasi", color = VintageTextEspresso, fontSize = 11.sp)
                        Text(text = "• Data Cuaca: BMKG Indonesia", color = VintageTextEspresso, fontSize = 11.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = VintageTerracotta)
                ) {
                    Text(text = "Tutup", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
