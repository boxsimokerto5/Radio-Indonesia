package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.VintageBorderSepia
import com.example.ui.theme.VintageBorderStrong
import com.example.ui.theme.VintageErrorCrimson
import com.example.ui.theme.VintageParchmentBg
import com.example.ui.theme.VintageParchmentCard
import com.example.ui.theme.VintageParchmentCardElevated
import com.example.ui.theme.VintageParchmentDark
import com.example.ui.theme.VintageTerracotta
import com.example.ui.theme.VintageTextEspresso
import com.example.ui.theme.VintageTextMutedSepia
import com.example.ui.theme.VintageTextWarmBrown

@Composable
fun SleepTimerDialog(
    isActive: Boolean,
    remainingSeconds: Long?,
    onSelectMinutes: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    val presets = listOf(15, 30, 45, 60, 90)

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clip(RoundedCornerShape(18.dp))
                .border(2.dp, VintageBorderStrong, RoundedCornerShape(18.dp))
                .testTag("sleep_timer_dialog"),
            colors = CardDefaults.cardColors(containerColor = VintageParchmentCard),
            shape = RoundedCornerShape(18.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                VintageParchmentCard,
                                VintageParchmentCardElevated
                            )
                        )
                    )
                    .padding(20.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Title Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.Bedtime,
                                contentDescription = null,
                                tint = VintageTerracotta,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Pengatur Waktu Tidur",
                                color = VintageTextEspresso,
                                fontSize = 16.5.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Serif
                            )
                        }

                        IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = "Tutup",
                                tint = VintageTextEspresso
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    if (isActive && remainingSeconds != null && remainingSeconds > 0) {
                        val mins = remainingSeconds / 60
                        val secs = remainingSeconds % 60
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(VintageParchmentDark)
                                .border(1.2.dp, VintageTerracotta, RoundedCornerShape(8.dp))
                                .padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Siaran mati dalam %02d:%02d".format(mins, secs),
                                color = VintageTerracotta,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                        Spacer(modifier = Modifier.height(14.dp))
                    } else {
                        Text(
                            text = "Pilih durasi sebelum siaran radio otomatis dinonaktifkan:",
                            color = VintageTextWarmBrown,
                            fontSize = 12.5.sp,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    // Presets Grid
                    presets.chunked(3).forEach { row ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            row.forEach { min ->
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(VintageParchmentDark)
                                        .border(1.dp, VintageBorderStrong, RoundedCornerShape(8.dp))
                                        .clickable { onSelectMinutes(min) }
                                        .padding(vertical = 12.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "$min Menit",
                                        color = VintageTextEspresso,
                                        fontSize = 13.5.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    if (isActive) {
                        TextButton(
                            onClick = { onSelectMinutes(0) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Matikan Pengatur Waktu",
                                color = VintageErrorCrimson,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}
