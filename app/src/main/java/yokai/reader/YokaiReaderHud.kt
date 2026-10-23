/*
 * Yokai — a manhwa reader.
 *
 * A reading HUD for vertical/webtoon reading: the chapter name on one side, the
 * position inside the chapter on the other, in the mono face that the design
 * language reserves for live values.
 *
 * Why it exists: in webtoon mode the bars are hidden while reading, so the only
 * way to know where you are is to open the menu and cover the page — which is
 * exactly the moment you stop reading. The HUD appears for a moment when the
 * page changes and fades away again; it never sits on the artwork permanently.
 *
 * It lives in `yokai/` and is attached with three lines from ReaderActivity, so
 * an upstream merge only has to re-check those three lines.
 */
package yokai.reader

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.AbstractComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.text.style.TextOverflow
import eu.kanade.presentation.theme.TachiyomiTheme
import yokai.design.MonoFontFamily
import yokai.design.YokaiMotion
import yokai.design.YokaiRadius
import yokai.design.YokaiSpace
import yokai.design.yokaiShape

/**
 * Overlay that reports "chapter · 12/48" while the user is scrolling.
 *
 * It renders nothing in paged modes: those already show a page number in the
 * bottom bar, and a second copy at the top would be noise.
 */
class YokaiReaderHud @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : AbstractComposeView(context, attrs, defStyleAttr) {

    private var verticalMode by mutableStateOf(false)
    private var chapterTitle by mutableStateOf("")
    private var page by mutableIntStateOf(0)
    private var total by mutableIntStateOf(0)
    private var visible by mutableStateOf(false)

    private val hide = Runnable { visible = false }

    init {
        layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT, Gravity.TOP)
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnDetachedFromWindowOrReleasedFromPool)
        // Purely informational: it must never steal a tap or a screen reader stop.
        isClickable = false
        importantForAccessibility = IMPORTANT_FOR_ACCESSIBILITY_NO
    }

    /** Only webtoon/continuous-vertical reading gets the HUD. */
    fun setVertical(vertical: Boolean) {
        verticalMode = vertical
        if (!vertical) {
            removeCallbacks(hide)
            visible = false
        }
    }

    /** New chapter: reset the counter so a stale "12/48" never shows over page 1. */
    fun setChapter(title: String) {
        chapterTitle = title
        page = 0
        total = 0
        removeCallbacks(hide)
        visible = false
    }

    /** Called from the viewer whenever a page becomes active. */
    fun onPageChanged(pageNumber: Int, pageCount: Int) {
        page = pageNumber
        total = pageCount
        if (!verticalMode || pageCount <= 0) return
        visible = true
        removeCallbacks(hide)
        postDelayed(hide, HUD_LINGER_MS)
    }

    override fun onDetachedFromWindow() {
        removeCallbacks(hide)
        super.onDetachedFromWindow()
    }

    @Composable
    override fun Content() {
        TachiyomiTheme {
            HudContent(
                visible = visible && verticalMode && total > 0,
                chapterTitle = chapterTitle,
                page = page,
                total = total,
            )
        }
    }

    private companion object {
        /** How long the HUD stays after a page change: long enough to read, short enough to forget. */
        const val HUD_LINGER_MS = 2400L
    }
}

@Composable
private fun HudContent(
    visible: Boolean,
    chapterTitle: String,
    page: Int,
    total: Int,
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(YokaiMotion.quick)),
        exit = fadeOut(tween(YokaiMotion.deliberate)),
    ) {
        Row(
            modifier = Modifier
                .statusBarsPadding()
                .padding(horizontal = YokaiSpace.md, vertical = YokaiSpace.sm)
                .background(
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.88f),
                    shape = yokaiShape(YokaiRadius.control),
                )
                .padding(horizontal = YokaiSpace.md, vertical = YokaiSpace.sm),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
        ) {
            Text(
                text = chapterTitle,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f),
            )
            Spacer(Modifier.width(YokaiSpace.sm))
            Text(
                text = "$page / $total",
                style = MaterialTheme.typography.labelMedium.copy(fontFamily = MonoFontFamily),
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
            )
        }
    }
}
