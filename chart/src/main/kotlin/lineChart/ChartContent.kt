package lineChart


import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.*
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import lineChart.components.chartContainer
import lineChart.lines.drawDefaultLineWithShadow
import lineChart.lines.drawQuarticLineWithShadow
import lineChart.model.BackGroundGrid
import lineChart.model.LineParameters
import lineChart.model.LineType

@OptIn(ExperimentalTextApi::class)
@Composable
internal fun ChartContent(
    modifier: Modifier,
    linesParameters: List<LineParameters>,
    backGroundColor: Color,
    xAxisData: List<String>,
    showBackgroundGrid: BackGroundGrid,
    barWidthPx: Dp,
    animateChart: Boolean,
    pathEffect: PathEffect
) {

    val textMeasure = rememberTextMeasurer()

    val animatedProgress = remember {
        if (animateChart) Animatable(0f) else Animatable(1f)
    }
    val upperValue = remember {
        linesParameters.flatMap { it.data }.maxOrNull()?.plus(1.0) ?: 0.0
    }.dp
    val lowerValue = remember {
        linesParameters.flatMap { it.data }.minOrNull() ?: 0.0
    }.dp

    Canvas(
        modifier = modifier
            .fillMaxSize()
    ) {
        val spacingX = (size.width/5f).dp
        val spacingY = (size.height/5f).dp
        chartContainer(
            xAxisData = xAxisData,
            textMeasure = textMeasure,
            upperValue = upperValue,
            lowerValue = lowerValue,
            isShowBackgroundLines = showBackgroundGrid,
            backgroundLineWidth = barWidthPx.toPx(),
            backGroundLineColor = backGroundColor,
            pathEffect = pathEffect,
            spacingX=spacingX,
            spacingY=spacingY,
        )

        linesParameters.forEach { line ->
            if (line.lineType == LineType.DEFAULT_LINE) {

                drawDefaultLineWithShadow(
                    line = line,
                    lowerValue = lowerValue,
                    upperValue = upperValue,
                    animatedProgress = animatedProgress,
                    xAxisSize = xAxisData.size,
                    spacingX=spacingX,
                    spacingY=spacingY,
                )

            } else {
                drawQuarticLineWithShadow(
                    line = line,
                    lowerValue = lowerValue,
                    upperValue = upperValue,
                    animatedProgress = animatedProgress,
                    xAxisSize = xAxisData.size,
                    spacingX=spacingX,
                    spacingY=spacingY,
                )

            }
        }
    }

    LaunchedEffect(linesParameters, animateChart) {
        if (animateChart) {
            animatedProgress.animateTo(
                targetValue = 0f,
            )
            delay(400)
            animatedProgress.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 1000, easing = LinearEasing)
            )
        }
    }
}