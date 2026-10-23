/*
 * Yokai — a manhwa reader.
 *
 * Shape and theme layer ported from the MaxManager design language
 * (© 2026 Nader Magdy), relicensed here under the GNU GPL v3.
 */
package yokai.design

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.unit.dp

/**
 * Material's five shape slots, expressed in the project's own radius scale.
 *
 * A slot names the token it stands for, so a card and the dialog it opens can
 * never disagree about how soft the app is:
 *   extraSmall → [YokaiRadius.control]  (a chip, a text field, a tag)
 *   small      → [YokaiRadius.row]      (a list row, an action row)
 *   medium     → [YokaiRadius.group]    (a grouped container, a card)
 *   large      → [YokaiRadius.sheet]    (a sheet, a dialog)
 *   extraLarge → [YokaiRadius.sheet]    (nothing in the app should be softer)
 *
 * [scale] is the roundness setting: the same values multiplied by it, so the
 * five panels move together instead of the dialog shape drifting away from the
 * card shape on the first change.
 */
fun yokaiShapes(scale: Float = 1f): Shapes = Shapes(
    extraSmall = RoundedCornerShape(YokaiRadius.control * scale),
    small = RoundedCornerShape(YokaiRadius.row * scale),
    medium = RoundedCornerShape(YokaiRadius.group * scale),
    large = RoundedCornerShape(YokaiRadius.sheet * scale),
    extraLarge = RoundedCornerShape(YokaiRadius.sheet * scale),
)

/**
 * Applies the Yokai type and shape language *on top of* the colour scheme that
 * is already in force.
 *
 * Why it wraps instead of replacing: Komikku ships two dozen colour schemes
 * plus Material You, and every one of them is the user's choice. The design
 * language here owns typography, geometry and rhythm — never the palette — so
 * a theme switch keeps working and this layer stays mergeable with upstream.
 */
@Composable
fun YokaiDesign(
    roundness: Float = 1f,
    density: Float = 1f,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalYokaiRadiusScale provides roundness,
        LocalYokaiSpaceScale provides density,
    ) {
        MaterialTheme(
            colorScheme = MaterialTheme.colorScheme,
            typography = YokaiTypography,
            shapes = yokaiShapes(roundness),
            content = content,
        )
    }
}

/** Corner shape for a token radius, measured by the active roundness. */
@Composable
fun yokaiShape(base: androidx.compose.ui.unit.Dp = YokaiRadius.group) =
    RoundedCornerShape(yokaiRadius(base))

/** Smallest soft shape used by inline controls (chips, tags, icon frames). */
@Composable
fun yokaiControlShape() = RoundedCornerShape(yokaiRadius(YokaiRadius.control))

/** The pill: reserved for state, never for a primary action. */
@Composable
fun yokaiPillShape() = RoundedCornerShape(YokaiRadius.pill)

/** 2dp is the smallest step in the scale; named so it never appears as a literal. */
val YokaiHairlineRadius = 2.dp
