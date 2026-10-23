/*
 * Yokai — a manhwa reader.
 *
 * Card contract ported from the MaxManager design language
 * (© 2026 Nader Magdy), relicensed here under the GNU GPL v3.
 *
 * One card, not thirty-eight. The shell, the border, the icon frame and the
 * gaps are fixed by the tokens; call sites only choose content and tone, which
 * is what makes two neighbouring cards look like they belong to one product.
 */
package yokai.design.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import yokai.design.LocalYokaiCardStyle
import yokai.design.YokaiCardSpec
import yokai.design.YokaiCardStyle
import yokai.design.YokaiSpace
import yokai.design.YokaiTone
import yokai.design.border
import yokai.design.container
import yokai.design.content
import yokai.design.yokaiRadius
import yokai.design.yokaiShape

/**
 * A grouped surface: icon frame, title, supporting line, and an optional
 * trailing slot for a value or a switch.
 *
 * [tone] colours the glyph frame and the border — never the body text, so a
 * warning card is still readable in greyscale. [onClick] makes the whole card
 * one touch target (the 48dp floor applies to its height, not to its icon).
 */
@Composable
fun YokaiCard(
    title: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    icon: ImageVector? = null,
    tone: YokaiTone = YokaiTone.Neutral,
    style: YokaiCardStyle = LocalYokaiCardStyle.current,
    onClick: (() -> Unit)? = null,
    trailing: (@Composable () -> Unit)? = null,
    content: (@Composable ColumnScope.() -> Unit)? = null,
) {
    val shape = yokaiShape()
    val scheme = MaterialTheme.colorScheme
    val background = when (style) {
        YokaiCardStyle.OUTLINED -> scheme.surfaceContainerLow
        YokaiCardStyle.FILLED -> tone.container()
        YokaiCardStyle.FLAT -> Color.Transparent
    }
    val clickModifier = if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier

    Column(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = YokaiCardSpec.minHeight)
            .background(background, shape)
            .then(
                if (style == YokaiCardStyle.FLAT) {
                    Modifier
                } else {
                    Modifier.border(YokaiCardSpec.borderWidth, tone.border(), shape)
                },
            )
            .then(clickModifier)
            .padding(YokaiCardSpec.padding),
        verticalArrangement = Arrangement.spacedBy(YokaiCardSpec.gap),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (icon != null) {
                Box(
                    modifier = Modifier
                        .size(YokaiCardSpec.iconContainer)
                        .background(tone.container(strong = true), yokaiShape(yokaiRadius(YokaiCardSpec.radius / 2))),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = tone.content(),
                        modifier = Modifier.size(YokaiCardSpec.iconGlyph),
                    )
                }
                Spacer(Modifier.width(YokaiSpace.md))
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = YokaiCardSpec.titleLines,
                    overflow = TextOverflow.Ellipsis,
                )
                if (!description.isNullOrBlank()) {
                    Spacer(Modifier.height(YokaiCardSpec.gapTight))
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodySmall,
                        color = scheme.onSurfaceVariant,
                        maxLines = YokaiCardSpec.descriptionLines,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }

            if (trailing != null) {
                Spacer(Modifier.width(YokaiSpace.sm))
                trailing()
            }
        }

        content?.invoke(this)
    }
}

/**
 * The compact variant: no icon frame, one line of supporting copy, used in
 * dense lists where a full card would waste a third of the row.
 */
@Composable
fun YokaiCompactCard(
    title: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    tone: YokaiTone = YokaiTone.Neutral,
    icon: ImageVector? = null,
    onClick: (() -> Unit)? = null,
    trailing: (@Composable () -> Unit)? = null,
) {
    YokaiCard(
        title = title,
        modifier = modifier,
        description = description,
        icon = icon,
        tone = tone,
        onClick = onClick,
        trailing = trailing,
    )
}
