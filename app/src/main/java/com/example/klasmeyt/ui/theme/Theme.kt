package com.example.klasmeyt.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape

private val KlasmeytLightColors = lightColorScheme(
    primary = Color(0xFF0C2340),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFDCE7F4),
    onPrimaryContainer = Color(0xFF102B4E),
    secondary = Color(0xFF8A6A10),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFFFE9A8),
    onSecondaryContainer = Color(0xFF352600),
    tertiary = Color(0xFF315F87),
    background = Color(0xFFF7F8FA),
    onBackground = Color(0xFF1B1D21),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF1B1D21),
    surfaceVariant = Color(0xFFEDF1F6),
    onSurfaceVariant = Color(0xFF505967),
    outline = Color(0xFF778292),
    error = Color(0xFFBA1A1A)
)

private val KlasmeytDarkColors = darkColorScheme(
    primary = Color(0xFFB8CBE7),
    onPrimary = Color(0xFF183250),
    primaryContainer = Color(0xFF2D496B),
    onPrimaryContainer = Color(0xFFDCE7F4),
    secondary = Color(0xFFE8C96C),
    onSecondary = Color(0xFF3D2F00),
    secondaryContainer = Color(0xFF594500),
    onSecondaryContainer = Color(0xFFFFE9A8),
    tertiary = Color(0xFFA5C9ED),
    background = Color(0xFF111820),
    onBackground = Color(0xFFE4E8EE),
    surface = Color(0xFF171F29),
    onSurface = Color(0xFFE4E8EE),
    surfaceVariant = Color(0xFF2A3440),
    onSurfaceVariant = Color(0xFFC0C8D2),
    outline = Color(0xFF8995A4),
    error = Color(0xFFFFB4AB)
)

private val KlasmeytTypography = Typography(
    headlineMedium = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        lineHeight = 34.sp
    ),
    titleLarge = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        lineHeight = 28.sp
    ),
    titleMedium = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    bodyLarge = TextStyle(
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    bodyMedium = TextStyle(
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
    labelLarge = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 20.sp
    )
)

private val KlasmeytShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(24.dp),
    extraLarge = RoundedCornerShape(28.dp)
)

@Composable
fun KlasmeytTheme(
    darkTheme: Boolean = androidx.compose.foundation.isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) KlasmeytDarkColors else KlasmeytLightColors,
        typography = KlasmeytTypography,
        shapes = KlasmeytShapes,
        content = content
    )
}
