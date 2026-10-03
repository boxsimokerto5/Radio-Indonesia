package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val VintageParchmentColorScheme = lightColorScheme(
  primary = VintageTerracotta,
  onPrimary = Color.White,
  primaryContainer = VintageTerracottaSoft,
  onPrimaryContainer = VintageTerracotta,
  secondary = VintageGoldOchre,
  onSecondary = Color.White,
  secondaryContainer = VintageParchmentCardElevated,
  onSecondaryContainer = VintageTextEspresso,
  tertiary = VintageAmberWarm,
  onTertiary = Color.White,
  background = VintageParchmentBg,
  onBackground = VintageTextEspresso,
  surface = VintageParchmentCard,
  onSurface = VintageTextEspresso,
  surfaceVariant = VintageParchmentSurfaceVariant,
  onSurfaceVariant = VintageTextWarmBrown,
  outline = VintageBorderStrong,
  outlineVariant = VintageBorderSepia
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = false,
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  MaterialTheme(
    colorScheme = VintageParchmentColorScheme,
    typography = Typography,
    content = content
  )
}


