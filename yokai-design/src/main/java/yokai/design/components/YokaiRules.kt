/*
 * Yokai — a manhwa reader.
 *
 * Rules are the design language's way of separating two layers: a hairline, not a
 * shadow. Material's elevation tint is deliberately unused, because a reader that
 * floats its chrome on shadows ends up looking like a stack of paper.
 */
package yokai.design.components

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** Draws a hairline along the bottom edge of whatever this modifier is applied to. */
fun Modifier.yokaiBottomRule(
    color: Color,
    thickness: Dp = 1.dp,
): Modifier = drawWithContent {
    drawContent()
    val stroke = thickness.toPx()
    val y = size.height - stroke / 2f
    drawLine(
        color = color,
        start = Offset(0f, y),
        end = Offset(size.width, y),
        strokeWidth = stroke,
    )
}

/** The same rule along the top edge, for chrome that sits under the content. */
fun Modifier.yokaiTopRule(
    color: Color,
    thickness: Dp = 1.dp,
): Modifier = drawWithContent {
    drawContent()
    val stroke = thickness.toPx()
    drawLine(
        color = color,
        start = Offset(0f, stroke / 2f),
        end = Offset(size.width, stroke / 2f),
        strokeWidth = stroke,
    )
}
