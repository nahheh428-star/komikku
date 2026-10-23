/*
 * Yokai — a manhwa reader.
 *
 * Section and metric contracts ported from the MaxManager design language
 * (© 2026 Nader Magdy), relicensed here under the GNU GPL v3.
 *
 * The heading order is binding, from largest to smallest, and no two layers may
 * share a size: page title (headlineSmall and up) → section heading
 * (titleMedium, bold) → card title (titleSmall) → supporting line (bodySmall).
 * A reader that cannot tell what is a page and what is a group reads as one
 * undifferentiated list.
 */
package yokai.design.components

import androidx.compose.foundation.background
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import yokai.design.YokaiHairlineRadius
import yokai.design.YokaiMetricType
import yokai.design.YokaiSectionSpec
import yokai.design.YokaiSpace
import yokai.design.YokaiTone
import yokai.design.YokaiTone.Accent
import yokai.design.content
import yokai.design.yokaiRadius

/**
 * The one section heading in the app: a short horizontal rule, the title, and
 * an optional supporting line.
 *
 * The rule is horizontal on purpose — drawn *above* the title it introduces it;
 * drawn *beside* it, it reads as a stray column and costs 30dp of vertical
 * space on every section of every screen.
 */
@Composable
fun YokaiSectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    supporting: String? = null,
    tone: YokaiTone = Accent,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = YokaiSectionSpec.spaceBefore, bottom = YokaiSectionSpec.spaceAfter),
        verticalArrangement = Arrangement.spacedBy(YokaiSectionSpec.titleGap),
    ) {
        Box(
            modifier = Modifier
                .width(YokaiSectionSpec.accentRuleWidth)
                .height(YokaiSectionSpec.accentRuleHeight)
                .background(tone.content(), androidx.compose.foundation.shape.RoundedCornerShape(YokaiHairlineRadius)),
        )
        Spacer(Modifier.height(YokaiSectionSpec.accentRuleGap))
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
        )
        if (!supporting.isNullOrBlank()) {
            Text(
                text = supporting,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

/**
 * A numeric readout: mono digits, a small label under them.
 *
 * Values that are not known render as an em dash ([yokai.design.YOKAI_VALUE_UNAVAILABLE]),
 * never as a plausible zero — a measure that reads "0" is a statement.
 */
@Composable
fun YokaiMetric(
    value: String,
    label: String,
    modifier: Modifier = Modifier,
    tone: YokaiTone = YokaiTone.Neutral,
    valueSize: androidx.compose.ui.unit.TextUnit = YokaiMetricType.valueMedium,
) {
    Column(modifier = modifier) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium.copy(
                fontFamily = yokai.design.MonoFontFamily,
                fontSize = valueSize,
                lineHeight = valueSize,
            ),
            color = if (tone == YokaiTone.Neutral) MaterialTheme.colorScheme.onSurface else tone.content(),
        )
        Spacer(Modifier.height(YokaiSpace.xs))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

/** Inline icon + label pill used for state ("مكتمل", "غير متاح"). */
@Composable
fun YokaiStatusChip(
    label: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    tone: YokaiTone = YokaiTone.Neutral,
    compact: Boolean = true,
) {
    Row(
        modifier = modifier
            .background(tone.content().copy(alpha = yokai.design.YokaiAlpha.toneContainer), yokai.design.yokaiPillShape())
            .padding(
                horizontal = if (compact) YokaiSpace.sm else YokaiSpace.md,
                vertical = if (compact) YokaiSpace.xs else YokaiSpace.sm,
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(YokaiSpace.xs),
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = tone.content(),
                modifier = Modifier.size(yokai.design.YokaiSize.iconGlyphSmall),
            )
        }
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = tone.content(),
        )
    }
}

/** Vertical gap helper used between sections of a scrolling body. */
@Composable
fun YokaiSectionGap(height: Dp = YokaiSpace.section) {
    Spacer(Modifier.height(height))
}

/** Horizontal page gutters, so no screen invents its own padding. */
val yokaiGutter = YokaiSpace.gutter
