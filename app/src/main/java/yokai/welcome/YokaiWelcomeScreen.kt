/*
 * Yokai — a manhwa reader.
 *
 * First launch lands here instead of straight in the upstream onboarding: the
 * app says what it is, in its own type and its own rhythm, before it asks the
 * user to choose a theme or grant storage access.
 *
 * This file lives under `yokai/` on purpose. Upstream never has a file here, so
 * it cannot conflict on an upstream merge, and the whole screen can be replaced
 * without touching a single Komikku file.
 */
package yokai.welcome

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import eu.kanade.presentation.util.Screen
import eu.kanade.tachiyomi.ui.more.OnboardingScreen
import tachiyomi.i18n.kmk.KMR
import tachiyomi.presentation.core.i18n.stringResource
import yokai.design.MonoFontFamily
import yokai.design.YokaiMotion
import yokai.design.YokaiRadius
import yokai.design.YokaiSize
import yokai.design.YokaiSpace
import yokai.design.YokaiTone
import yokai.design.YokaiTone.Accent
import yokai.design.components.YokaiAmbientSurface
import yokai.design.content
import yokai.design.yokaiRadius

/**
 * The welcome is a gate only until the user starts: back is disabled, because
 * there is nothing behind it — the library has not been created yet.
 */
class YokaiWelcomeScreen : Screen() {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        BackHandler(enabled = true) {
            // Intentionally empty: this is the first screen of a fresh install.
        }

        YokaiWelcomeContent(
            onBegin = {
                // Hand over to the upstream onboarding, replacing this screen so
                // its own "finish" pops back to the library and not to the welcome.
                navigator.replace(OnboardingScreen())
            },
        )
    }
}

@Composable
private fun YokaiWelcomeContent(onBegin: () -> Unit) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    YokaiAmbientSurface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.86f))
                .verticalScroll(rememberScrollState())
                .padding(horizontal = YokaiSpace.gutter, vertical = YokaiSpace.gutter),
            verticalArrangement = Arrangement.Center,
        ) {
            Reveal(visible = visible, delayMillis = 0) {
                Column(modifier = Modifier.widthIn(max = YokaiSize.readingMaxWidth)) {
                    Kicker()

                    Spacer(Modifier.height(YokaiSpace.md))

                    Text(
                        text = stringResource(KMR.strings.yokai_wordmark),
                        style = MaterialTheme.typography.displayLarge.copy(
                            fontSize = 64.sp,
                            letterSpacing = 10.sp,
                        ),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                    )

                    Spacer(Modifier.height(YokaiSpace.md))

                    Box(
                        modifier = Modifier
                            .width(56.dp)
                            .height(4.dp)
                            .background(MaterialTheme.colorScheme.primary, CircleShape),
                    )

                    Spacer(Modifier.height(YokaiSpace.lg))

                    Text(
                        text = stringResource(KMR.strings.yokai_tagline),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.widthIn(max = 460.dp),
                    )
                }
            }

            Spacer(Modifier.height(YokaiSpace.xxl))

            Reveal(visible = visible, delayMillis = 120) {
                Promise(
                    index = "01",
                    title = stringResource(KMR.strings.yokai_promise_reader_title),
                    body = stringResource(KMR.strings.yokai_promise_reader_body),
                )
            }
            Reveal(visible = visible, delayMillis = 210) {
                Promise(
                    index = "02",
                    title = stringResource(KMR.strings.yokai_promise_library_title),
                    body = stringResource(KMR.strings.yokai_promise_library_body),
                )
            }
            Reveal(visible = visible, delayMillis = 300) {
                Promise(
                    index = "03",
                    title = stringResource(KMR.strings.yokai_promise_open_title),
                    body = stringResource(KMR.strings.yokai_promise_open_body),
                )
            }

            Spacer(Modifier.height(YokaiSpace.xxl))

            Reveal(visible = visible, delayMillis = 390) {
                Button(
                    onClick = onBegin,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(YokaiSize.minTouchTarget + YokaiSpace.sm),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(yokaiRadius(YokaiRadius.control)),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                    ),
                ) {
                    Text(
                        text = stringResource(KMR.strings.yokai_action_begin),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold,
                    )
                }

                Spacer(Modifier.height(YokaiSpace.lg))

                Text(
                    text = stringResource(KMR.strings.yokai_footer),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

/**
 * One staged entrance: everything fades up, but each block starts a little later
 * than the one above it, so the screen reads as being written rather than as one
 * flash. Nothing here exceeds the motion budget even with the last delay added.
 */
@Composable
private fun Reveal(
    visible: Boolean,
    delayMillis: Int,
    content: @Composable () -> Unit,
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(YokaiMotion.deliberate, delayMillis = delayMillis)) +
            slideInVertically(tween(YokaiMotion.deliberate, delayMillis = delayMillis)) { it / 12 },
    ) {
        content()
    }
}

@Composable
private fun Kicker() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .background(Accent.content(), CircleShape),
        )
        Spacer(Modifier.width(YokaiSpace.sm))
        Text(
            text = stringResource(KMR.strings.yokai_kicker),
            style = MaterialTheme.typography.labelMedium.copy(letterSpacing = 2.sp),
            color = Accent.content(),
        )
    }
}

/**
 * A promise is a claim the app can keep. Each one carries an index in mono type
 * — the same "measured, not asserted" reading the design language uses for
 * values — and a hairline rule instead of a card, so the screen breathes.
 */
@Composable
private fun Promise(index: String, title: String, body: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = YokaiSpace.md),
        verticalAlignment = Alignment.Top,
    ) {
        Text(
            text = index,
            style = MaterialTheme.typography.labelMedium.copy(fontFamily = MonoFontFamily),
            color = YokaiTone.Accent.content(),
            modifier = Modifier.padding(end = YokaiSpace.md, top = 2.dp),
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
            )
            Spacer(Modifier.height(YokaiSpace.xs))
            Text(
                text = body,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
