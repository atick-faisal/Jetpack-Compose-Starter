/*
 * Copyright 2023 Atick Faisal
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.atick.core.ui.components

// ... Author fbilarino@github
// ... https://gist.github.com/fvilarino/7dc026b8a590ef65744180b984587ede

// import androidx.compose.animation.core.*
import androidx.compose.animation.core.DurationBasedAnimationSpec
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

enum class AnimationType {
    Bounce,
    LazyBounce,
    Fade,
}

private const val NumIndicators = 3
private const val IndicatorSize = 12
private const val BounceAnimationDurationMillis = 300
private const val FadeAnimationDurationMillis = 600

@Composable
fun LoadingButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    animationType: AnimationType = AnimationType.Bounce,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    indicatorSpacing: Dp = 4.dp,
    content: @Composable () -> Unit,
) {
    val contentAlpha by animateFloatAsState(targetValue = if (loading) 0f else 1f)
    val loadingAlpha by animateFloatAsState(targetValue = if (loading) 1f else 0f)
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = colors,
    ) {
        Box(
            contentAlignment = Alignment.Center,
        ) {
            LoadingIndicator(
                animating = loading,
                modifier = Modifier.graphicsLayer { alpha = loadingAlpha },
                color = MaterialTheme.colorScheme.onPrimary,
                indicatorSpacing = indicatorSpacing,
                animationType = animationType,
            )
            Box(
                modifier = Modifier.graphicsLayer { alpha = contentAlpha },
            ) {
                content()
            }
        }
    }
}

private val AnimationType.animationSpec: DurationBasedAnimationSpec<Float>
    get() = when (this) {
        AnimationType.Bounce,
        AnimationType.Fade,
        -> tween(durationMillis = animationDuration)

        AnimationType.LazyBounce -> keyframes {
            durationMillis = animationDuration
            initialValue at 0
            0f at animationDuration / 4
            targetValue / 2f at animationDuration / 2
            targetValue / 2f at animationDuration
        }
    }

private val AnimationType.animationDuration: Int
    get() = when (this) {
        AnimationType.Bounce,
        AnimationType.LazyBounce,
        -> BounceAnimationDurationMillis

        AnimationType.Fade -> FadeAnimationDurationMillis
    }

private val AnimationType.animationDelay: Int
    get() = animationDuration / NumIndicators

private val AnimationType.initialValue: Float
    get() = when (this) {
        AnimationType.Bounce -> IndicatorSize / 2f
        AnimationType.LazyBounce -> -IndicatorSize / 2f
        AnimationType.Fade -> 1f
    }

private val AnimationType.targetValue: Float
    get() = when (this) {
        AnimationType.Bounce -> -IndicatorSize / 2f
        AnimationType.LazyBounce -> IndicatorSize / 2f
        AnimationType.Fade -> .2f
    }

@Stable
interface LoadingIndicatorState {
    operator fun get(index: Int): Float

    fun start(animationType: AnimationType, scope: CoroutineScope)
}

class LoadingIndicatorStateImpl : LoadingIndicatorState {
    private val animatedValues = List(NumIndicators) { mutableStateOf(0f) }

    override fun get(index: Int): Float = animatedValues[index].value

    override fun start(animationType: AnimationType, scope: CoroutineScope) {
        repeat(NumIndicators) { index ->
            scope.launch {
                animate(
                    initialValue = animationType.initialValue,
                    targetValue = animationType.targetValue,
                    animationSpec = infiniteRepeatable(
                        animation = animationType.animationSpec,
                        repeatMode = RepeatMode.Reverse,
                        initialStartOffset = StartOffset(animationType.animationDelay * index),
                    ),
                ) { value, _ -> animatedValues[index].value = value }
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LoadingIndicatorStateImpl

        if (animatedValues != other.animatedValues) return false

        return true
    }

    override fun hashCode(): Int {
        return animatedValues.hashCode()
    }
}

@Composable
fun rememberLoadingIndicatorState(
    animating: Boolean,
    animationType: AnimationType,
): LoadingIndicatorState {
    val state = remember {
        LoadingIndicatorStateImpl()
    }
    // ... https://gist.github.com/fvilarino/7dc026b8a590ef65744180b984587ede?permalink_comment_id=4263900#gistcomment-4263900
    LaunchedEffect(key1 = animating) {
        if (animating) {
            state.start(animationType, this)
        }
    }
    return state
}

@Composable
private fun LoadingIndicator(
    animating: Boolean,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onPrimary,
    indicatorSpacing: Dp = 2.dp,
    animationType: AnimationType,
) {
    val state = rememberLoadingIndicatorState(animating, animationType)
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        repeat(NumIndicators) { index ->
            LoadingDot(
                modifier = Modifier
                    .padding(horizontal = indicatorSpacing)
                    .width(IndicatorSize.dp)
                    .aspectRatio(1f)
                    .then(
                        when (animationType) {
                            AnimationType.Bounce,
                            AnimationType.LazyBounce,
                            -> Modifier.offset(
                                y = state[index].coerceAtMost(
                                    IndicatorSize / 2f,
                                ).dp,
                            )

                            AnimationType.Fade -> Modifier.graphicsLayer { alpha = state[index] }
                        },
                    ),
                color = color,
            )
        }
    }
}

@Composable
private fun LoadingDot(
    color: Color,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clip(shape = CircleShape)
            .background(color = color),
    )
}
