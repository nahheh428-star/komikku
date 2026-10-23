/*
 * Yokai — a manhwa reader.
 *
 * Token layer ported from the MaxManager design language
 * (© 2026 Nader Magdy), relicensed here under the GNU GPL v3.
 *
 * Why a token layer at all: screens that write their own dp/alpha values drift.
 * Two cards with different corners, two "gaps" that are not the same gap, a
 * warning colour that changes meaning per screen — that is how a product starts
 * reading as a pile of screens. Every value below has one name and one home, so
 * changing the app's rhythm is a change in this file, not a sweep of literals.
 *
 * Numbers and colour resolution only — no composable UI lives here.
 */
package yokai.design

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * A 4dp-based spacing scale.
 *
 * `gutter`, `section`, `row` and `pageBottom` are the only values a screen body
 * should need; the raw steps exist for component internals.
 */
object YokaiSpace {
    val hairline: Dp = 2.dp
    val xs: Dp = 4.dp
    val sm: Dp = 8.dp
    val md: Dp = 12.dp
    val lg: Dp = 16.dp
    val xl: Dp = 20.dp
    val xxl: Dp = 28.dp

    /** Horizontal page gutter shared by every scrollable screen body. */
    val gutter: Dp = 20.dp

    /** Vertical gap between two sections of a screen. */
    val section: Dp = 28.dp

    /** Gap between sibling rows inside one section. */
    val row: Dp = 8.dp

    /** Inner padding of a single row. */
    val rowPaddingHorizontal: Dp = 14.dp
    val rowPaddingVertical: Dp = 12.dp

    /** Breathing room so the bottom navigation never covers the last row. */
    val pageBottom: Dp = 40.dp
}

/**
 * Shape language: rows are calm (14dp), groups frame them (22dp), sheets and
 * dialogs are the softest (28dp). A pill means "this is a state", never a
 * primary action.
 */
object YokaiRadius {
    val control: Dp = 12.dp
    val row: Dp = 14.dp
    val group: Dp = 22.dp
    val sheet: Dp = 28.dp
    val pill: Dp = 100.dp
}

object YokaiSize {
    /** Accessibility floor for anything clickable. Never go below this. */
    val minTouchTarget: Dp = 48.dp

    val iconGlyph: Dp = 20.dp
    val iconGlyphSmall: Dp = 16.dp

    /** Icon container inside a list row. */
    val rowIconContainer: Dp = 34.dp

    /** Icon container in headers and notices. */
    val iconContainer: Dp = 40.dp

    val border: Dp = 1.dp

    /** Keeps long explanatory copy readable on tablets and in landscape. */
    val readingMaxWidth: Dp = 560.dp

    /** Height of a fractional bar (download progress, chapter progress). */
    val barHeight: Dp = 8.dp
}

/**
 * Opacity vocabulary. Borders stay hairline-quiet and tone containers stay low,
 * so content is always the brightest thing on screen.
 */
object YokaiAlpha {
    val border = 0.16f
    val borderStrong = 0.28f

    /** An accent used only to wash a surface, never to mark an element. */
    val toneWash = 0.045f

    val toneContainer = 0.10f
    val toneContainerStrong = 0.18f
    val disabledContent = 0.38f
    val supportingText = 0.80f

    /** Inner highlight along a panel's leading edge. */
    val edgeLight = 0.22f

    /** Ambient glow behind an accented panel. */
    val haloGlow = 0.07f
}

/**
 * Motion budget. Motion exists to explain a transition or confirm a state
 * change; anything longer than [deliberate] is decoration and is not allowed.
 */
object YokaiMotion {
    val instant = 90
    val quick = 160
    val standard = 240
    val deliberate = 360
}

/**
 * The one card contract.
 *
 * Every rule lives here as a number so a card cannot drift by being written in
 * a different file: radius, border, padding, icon container, the icon→title→
 * description gaps, the reserved title lines that keep two cards in a row
 * aligned, the height floor, and the grid gutters.
 */
object YokaiCardSpec {
    val radius: Dp = YokaiRadius.group
    val borderWidth: Dp = YokaiSize.border

    /** Internal padding of a card. */
    val padding: Dp = YokaiSpace.lg

    /** The tinted square behind a card's glyph. */
    val iconContainer: Dp = YokaiSize.iconContainer

    /** The glyph itself; smaller than its container so the tint reads as a frame. */
    val iconGlyph: Dp = YokaiSize.iconGlyph

    val gap: Dp = YokaiSpace.sm
    val gapTight: Dp = YokaiSpace.xs

    /** Height floor so a one-word card does not collapse next to a wordy neighbour. */
    val minHeight: Dp = 92.dp

    val gridSpacing: Dp = YokaiSpace.md

    /**
     * A title always reserves this many lines, so the description starts at the
     * same height in every card of a row even when one title wraps.
     */
    val titleLines: Int = 2

    /** Description budget. Wrapping first; ellipsis only when genuinely exceeded. */
    val descriptionLines: Int = 2

    /**
     * Narrowest a card may become before a grid falls back to fewer columns.
     * Below roughly this width an Arabic label or a long word has nowhere to wrap.
     */
    val minColumnWidth: Dp = 148.dp
}

/** The one section-header contract: accents, gaps and the title/description rhythm. */
object YokaiSectionSpec {
    /**
     * The rule drawn above a title is horizontal, 28×4dp: it introduces the
     * heading rather than standing beside it.
     */
    val accentRuleWidth: Dp = YokaiSpace.xxl
    val accentRuleHeight: Dp = YokaiSpace.xs

    /** Gap between the horizontal rule and the title under it. */
    val accentRuleGap: Dp = YokaiSpace.sm

    /** Gap inside the title column: between the heading and its supporting line. */
    val titleGap: Dp = YokaiSpace.xs

    /** Space before a section heading (separating sections) and after it. */
    val spaceBefore: Dp = YokaiSpace.xl
    val spaceAfter: Dp = YokaiSpace.md
}

/**
 * Semantic tone.
 *
 * Tone is never the only carrier of meaning: every component that renders a
 * tone also renders an icon and a text label, so state survives colour
 * blindness, greyscale screenshots and high-contrast modes.
 */
enum class YokaiTone {
    /** Ordinary information. */
    Neutral,

    /** The screen's own accent — "this is the thing you came here for". */
    Accent,

    /** Confirmed good: downloaded, in sync, within limits. */
    Positive,

    /** Needs attention but still working. */
    Caution,

    /** Failed, blocked or unsafe. */
    Critical,

    /** Off, unsupported or not available. */
    Inactive,
}

/*
 * Positive / Caution / Critical are intentionally NOT taken from the dynamic
 * colour scheme: Material You can turn a semantic hue into a pastel that reads
 * as decoration. These hues are fixed and contrast-checked against both light
 * and dark surfaces; only Accent follows the user's theme.
 */
private val PositiveOnLight = Color(0xFF0B6B4F)
private val PositiveOnDark = Color(0xFF5FD9AC)
private val CautionOnLight = Color(0xFF8A5200)
private val CautionOnDark = Color(0xFFFFB86B)
private val CriticalOnLight = Color(0xFF9A1B1B)
private val CriticalOnDark = Color(0xFFFF9A90)

/** True when the active scheme is dark, derived from the surface itself. */
@Composable
fun yokaiIsDarkSurface(): Boolean = MaterialTheme.colorScheme.surface.luminance() < 0.5f

/** Foreground colour for a tone (text, icon, stroke). */
@Composable
fun YokaiTone.content(): Color {
    val scheme = MaterialTheme.colorScheme
    val dark = yokaiIsDarkSurface()
    return when (this) {
        YokaiTone.Neutral -> scheme.onSurfaceVariant
        YokaiTone.Accent -> scheme.primary
        YokaiTone.Positive -> if (dark) PositiveOnDark else PositiveOnLight
        YokaiTone.Caution -> if (dark) CautionOnDark else CautionOnLight
        YokaiTone.Critical -> if (dark) CriticalOnDark else CriticalOnLight
        YokaiTone.Inactive -> scheme.outline
    }
}

/** Low-emphasis container behind a tone's content. */
@Composable
fun YokaiTone.container(strong: Boolean = false): Color =
    content().copy(alpha = if (strong) YokaiAlpha.toneContainerStrong else YokaiAlpha.toneContainer)

/** Hairline border for a tone. */
@Composable
fun YokaiTone.border(strong: Boolean = false): Color =
    content().copy(alpha = if (strong) YokaiAlpha.borderStrong else YokaiAlpha.border)

/**
 * Placeholder rendered instead of a value when there is nothing real to show.
 * Deliberately not "0", not "--%": a zero would be read as a measurement.
 */
const val YOKAI_VALUE_UNAVAILABLE = "—"

/** Tabular figure sizes used by metric readouts so digits never reflow. */
object YokaiMetricType {
    val valueLarge: TextUnit = 34.sp
    val valueMedium: TextUnit = 22.sp
    val valueSmall: TextUnit = 16.sp
}

/**
 * Live roundness scale, provided by [YokaiDesign] from the theme's own
 * roundness preference. The default 1 keeps every context that does not pass
 * through the theme (tests, previews) unchanged.
 */
val LocalYokaiRadiusScale = compositionLocalOf { 1f }

/**
 * A radius measured by the active theme: the symbolic value times the live
 * roundness scale. This is the single conversion point — call it where a shape
 * is built (card, row, dialog), never multiply by hand at random sites.
 */
@Composable
fun yokaiRadius(base: Dp): Dp = base * LocalYokaiRadiusScale.current

/** Vertical density scale, provided by [YokaiDesign]. Never shrinks touch targets. */
val LocalYokaiSpaceScale = compositionLocalOf { 1f }

/** A space measured by the active density — the single conversion point, as [yokaiRadius]. */
@Composable
fun yokaiSpace(base: Dp): Dp = base * LocalYokaiSpaceScale.current

/** How a card's shell is drawn. The content never changes. */
enum class YokaiCardStyle {
    OUTLINED,
    FILLED,
    FLAT,
}

val LocalYokaiCardStyle = compositionLocalOf { YokaiCardStyle.OUTLINED }

/** Snapshot of the card metrics in force, so call sites read one object. */
@Immutable
data class YokaiCardMetrics(
    val padding: Dp,
    val iconContainer: Dp,
    val iconGlyph: Dp,
    val minHeight: Dp,
)
