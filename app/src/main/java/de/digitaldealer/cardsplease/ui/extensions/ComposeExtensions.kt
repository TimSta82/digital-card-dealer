package de.digitaldealer.cardsplease.ui.extensions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Velocity
import androidx.core.graphics.toColorInt
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

@Composable
fun <T> rememberFlow(
    flow: Flow<T>,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
): Flow<T> {
    return remember(key1 = flow, key2 = lifecycleOwner) { flow.flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.STARTED) }
}

@Composable
fun <T : R, R> Flow<T>.collectAsStateLifecycleAware(
    initial: R,
    context: CoroutineContext = EmptyCoroutineContext
): State<R> {
    val lifecycleAwareFlow = rememberFlow(flow = this)
    return lifecycleAwareFlow.collectAsState(initial = initial, context = context)
}

@Suppress("StateFlowValueCalledInComposition")
@Composable
fun <T> StateFlow<T>.collectAsStateLifecycleAware(
    context: CoroutineContext = EmptyCoroutineContext
): State<T> = collectAsStateLifecycleAware(initial = value, context = context)

/**
 * WORKAROUND FOR DISABLING USER SWIPE UNTIL ACCOMPANIST 0.24.1 is live --> https://stackoverflow.com/questions/69924509/how-to-disable-drag-compose-pager-after-upgrade-to-v0-19-0
 */
private val VerticalScrollConsumer = object : NestedScrollConnection {
    override fun onPreScroll(available: Offset, source: NestedScrollSource) = available.copy(x = 0f)
    override suspend fun onPreFling(available: Velocity) = available.copy(x = 0f)
}

private val HorizontalScrollConsumer = object : NestedScrollConnection {
    override fun onPreScroll(available: Offset, source: NestedScrollSource) = available.copy(y = 0f)
    override suspend fun onPreFling(available: Velocity) = available.copy(y = 0f)
}

fun Modifier.disabledVerticalPointerInputScroll(disabled: Boolean = true) =
    if (disabled) this.nestedScroll(VerticalScrollConsumer) else this

fun Modifier.disabledHorizontalPointerInputScroll(disabled: Boolean = true) =
    if (disabled) this.nestedScroll(HorizontalScrollConsumer) else this

private fun String.toMarkUpAnnotatedString(tag: Char, spannableStyle: SpanStyle): AnnotatedString {

    val findFatTagRegex: Regex = "<\\s*$tag[^>]*>(.*?)<\\s*/\\s*$tag>".toRegex()

    val matches: Sequence<MatchResult> = findFatTagRegex.findAll(this)

    val startTagLength = "<$tag>".length
    val endTagLength = "</$tag>".length

    val spanStyles = matches.mapIndexed { index, matchResult ->
        AnnotatedString.Range(
            item = spannableStyle,
            start = matchResult.range.first - ((index * startTagLength + index * endTagLength)),
            end = (matchResult.range.last + 1) - ((index * startTagLength + index * endTagLength) + startTagLength + endTagLength)
        )
    }

    return AnnotatedString(
        spanStyles = spanStyles.toList(),
        text = this
            .replace("<$tag>", "")
            .replace("</$tag>", "")
    )
}

fun String.formatFatMarkUpAnnotatedString() = this.toMarkUpAnnotatedString('b', spannableStyle = SpanStyle(fontWeight = FontWeight.Bold))
fun String.formatUnderlinedMarkUpAnnotatedString() = this.toMarkUpAnnotatedString('u', spannableStyle = SpanStyle(textDecoration = TextDecoration.Underline))

fun String.toColor(): Color? = kotlin.runCatching { toColorInt().let(::Color) }.getOrNull()
