/*
 * Yokai — a manhwa reader.
 *
 * Strip and tab components ported from the MaxManager design language
 * (© 2026 Nader Magdy), relicensed here under the GNU GPL v3.
 *
 * State is a pill; actions are not. Keeping that rule means a reader can tell a
 * filter that is active from a button that will do something without reading a
 * single label twice.
 */
package yokai.design.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import yokai.design.YokaiMotion
import yokai.design.YokaiSpace
import yokai.design.yokaiPillShape

/**
 * A horizontally scrolling tab strip where the selected tab is a filled pill.
 *
 * [onSelected] receives the index, never the label: labels are localized, so
 * matching on text breaks the moment the app is translated.
 */
@Composable
fun YokaiTabStrip(
    tabs: List<String>,
    selectedIndex: Int,
    onSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(YokaiSpace.sm),
    ) {
        itemsIndexed(tabs) { index, label ->
            YokaiTab(
                label = label,
                selected = index == selectedIndex,
                onClick = { onSelected(index) },
            )
        }
    }
}

@Composable
private fun YokaiTab(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val scheme = MaterialTheme.colorScheme
    val container by animateColorAsState(
        targetValue = if (selected) scheme.primary.copy(alpha = 0.16f) else Color.Transparent,
        animationSpec = tween(YokaiMotion.quick),
        label = "yokai-tab-container",
    )
    val content by animateColorAsState(
        targetValue = if (selected) scheme.primary else scheme.onSurfaceVariant,
        animationSpec = tween(YokaiMotion.quick),
        label = "yokai-tab-content",
    )

    Box(
        modifier = Modifier
            .widthIn(min = 48.dp)
            .background(container, yokaiPillShape())
            .clickable(onClick = onClick)
            .padding(horizontal = YokaiSpace.md, vertical = YokaiSpace.sm),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
            color = content,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}
