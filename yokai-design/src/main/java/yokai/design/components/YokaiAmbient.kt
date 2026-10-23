/*
 * Yokai — a manhwa reader.
 *
 * Ambient surface ported from the MaxManager design language
 * (© 2026 Nader Magdy), relicensed here under the GNU GPL v3.
 *
 * A quiet, slowly drifting pair of radial glows behind a screen's content. It
 * exists so a launch or a library header has depth without a photograph, a
 * gradient strip or a shadow stack — the design language separates layers with
 * colour and rules, not with elevation. Intensity stays low enough that text
 * contrast is never in question.
 */
package yokai.design.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/**
 * Draws [content] over a dark-first surface with two slow glows.
 *
 * [intensity] scales both glows (0f disables them entirely, which is what a
 * reduced-motion setting should pass). The drift period is deliberately far
 * above the motion budget: this is ambient light, not a transition, and it must
 * never compete with what the user is reading.
 */
@Composable
fun YokaiAmbientSurface(
    modifier: Modifier = Modifier,
    intensity: Float = 1f,
    content: @Composable BoxScope.() -> Unit,
) {
    val scheme = MaterialTheme.colorScheme
    val primary = scheme.primary
    val tertiary = scheme.tertiary

    val transition = rememberInfiniteTransition(label = "yokai-ambient")
    val phase by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 24_000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "yokai-ambient-phase",
    )

    Box(modifier = modifier) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        primary.copy(alpha = 0.20f * intensity),
                        Color.Transparent,
                    ),
                    center = Offset(w * (0.18f + 0.10f * phase), h * (0.12f + 0.04f * phase)),
                    radius = size.minDimension * 0.85f,
                ),
                radius = size.minDimension * 0.85f,
                center = Offset(w * (0.18f + 0.10f * phase), h * (0.12f + 0.04f * phase)),
            )

            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        tertiary.copy(alpha = 0.14f * intensity),
                        Color.Transparent,
                    ),
                    center = Offset(w * (0.86f - 0.08f * phase), h * (0.78f - 0.05f * phase)),
                    radius = size.minDimension * 0.70f,
                ),
                radius = size.minDimension * 0.70f,
                center = Offset(w * (0.86f - 0.08f * phase), h * (0.78f - 0.05f * phase)),
            )
        }
        content()
    }
}
