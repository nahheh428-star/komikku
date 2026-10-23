/*
 * Yokai — a manhwa reader.
 *
 * Type system ported from the MaxManager design language
 * (© 2026 Nader Magdy), relicensed here under the GNU GPL v3.
 *
 * Three roles, each picked for what it does here:
 *
 * - Display (Space Grotesk): a geometric technical face with just enough
 *   character in its letterforms to read as "designed", used for screen titles,
 *   section headings and anything that should feel like a cover label.
 * - Body (Manrope): a warmer, highly legible geometric sans for reading text —
 *   synopses, notes, settings copy.
 * - Mono (JetBrains Mono): reserved for *live values* — chapter counts, byte
 *   sizes, percentages. Digits never reflow while a number changes.
 *
 * The faces ship inside the APK as static instances (see res/font). They used
 * to be downloaded through the Play Services font provider in MaxManager; a
 * reader has to look identical on a de-Googled phone and in the `foss` build,
 * so the binaries are bundled instead of fetched.
 */
package yokai.design

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val DisplayFontFamily = FontFamily(
    Font(R.font.space_grotesk_400, weight = FontWeight.Normal),
    Font(R.font.space_grotesk_500, weight = FontWeight.Medium),
    Font(R.font.space_grotesk_600, weight = FontWeight.SemiBold),
    Font(R.font.space_grotesk_700, weight = FontWeight.Bold),
)

val BodyFontFamily = FontFamily(
    Font(R.font.manrope_400, weight = FontWeight.Normal),
    Font(R.font.manrope_500, weight = FontWeight.Medium),
    Font(R.font.manrope_600, weight = FontWeight.SemiBold),
    Font(R.font.manrope_700, weight = FontWeight.Bold),
)

val MonoFontFamily = FontFamily(
    Font(R.font.jetbrains_mono_500, weight = FontWeight.Medium),
    Font(R.font.jetbrains_mono_700, weight = FontWeight.Bold),
)

/**
 * Material's fifteen slots, addressed with the project's three families.
 *
 * Sizes and line-heights follow the standard Material 3 scale — those numbers
 * are already tuned for Android reading distances. What changes here is family,
 * weight and tracking: display slots tighten (`-0.5sp` at the largest size) so
 * the geometric face reads as engineered rather than playful.
 */
val YokaiTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = DisplayFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = (-0.5).sp,
    ),
    displayMedium = TextStyle(
        fontFamily = DisplayFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 45.sp,
        lineHeight = 52.sp,
        letterSpacing = (-0.25).sp,
    ),
    displaySmall = TextStyle(
        fontFamily = DisplayFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 36.sp,
        lineHeight = 44.sp,
    ),

    headlineLarge = TextStyle(
        fontFamily = DisplayFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
    ),
    headlineMedium = TextStyle(
        fontFamily = DisplayFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp,
        lineHeight = 36.sp,
    ),
    headlineSmall = TextStyle(
        fontFamily = DisplayFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = 32.sp,
    ),

    titleLarge = TextStyle(
        fontFamily = DisplayFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
    ),
    titleMedium = TextStyle(
        fontFamily = DisplayFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.1.sp,
    ),
    titleSmall = TextStyle(
        fontFamily = DisplayFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
    ),

    bodyLarge = TextStyle(
        fontFamily = BodyFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = BodyFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp,
    ),
    bodySmall = TextStyle(
        fontFamily = BodyFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.2.sp,
    ),

    labelLarge = TextStyle(
        fontFamily = BodyFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
    ),
    labelMedium = TextStyle(
        fontFamily = BodyFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
    ),
    labelSmall = TextStyle(
        fontFamily = BodyFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
    ),
)

/** Numeric readouts: mono, tabular, so a changing value never shifts its neighbours. */
val YokaiMetricValueStyle = TextStyle(
    fontFamily = MonoFontFamily,
    fontWeight = FontWeight.Medium,
    fontSize = YokaiMetricType.valueLarge,
    lineHeight = YokaiMetricType.valueLarge,
    fontFeatureSettings = "tnum",
)
