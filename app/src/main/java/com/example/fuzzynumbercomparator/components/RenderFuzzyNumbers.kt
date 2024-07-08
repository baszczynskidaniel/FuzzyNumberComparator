package com.example.fuzzynumbercomparator.components

import android.graphics.PointF
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fuzzynumbercomparator.fuzzynumber.FuzzyNumber
import com.example.fuzzynumbercomparator.fuzzynumber.LinearAlgebra
import com.example.fuzzynumbercomparator.fuzzynumber.Point
import com.example.fuzzynumbercomparator.fuzzynumber.TrapezoidFuzzyNumber
import com.example.fuzzynumbercomparator.fuzzynumber.TriangleFuzzyNumber


data class RenderTwoFuzzyNumberColors(
    val firstFill: Color,
    val secondFill: Color,
    val firstStroke: Color,
    val secondStroke: Color,
    val intersectionPoints: Color
)



@Composable
fun RenderTwoFuzzyNumbers(
    modifier: Modifier = Modifier,
    firstNumber: FuzzyNumber,
    secondNumber: FuzzyNumber,
    showIntersectionPoints: Boolean = true,
    colors: RenderTwoFuzzyNumberColors
) {


    Canvas(modifier = modifier) {
        val startDrawXCoordinate = minOf(firstNumber.getStart(), secondNumber.getStart())
        val endDrawXCoordinate = maxOf(firstNumber.getEnd(), secondNumber.getEnd())
        val offset = endDrawXCoordinate - startDrawXCoordinate

        val canvasProperties = CanvasProperties(
            this.size.width,
            this.size.height,
            startDrawXCoordinate.toFloat(),
            offset.toFloat()
        )

        val firstNumberPoints =
            getPointsOffsetInCanvas(firstNumber.getPoints(), canvasProperties)
        val secondNumberPoints =
            getPointsOffsetInCanvas(secondNumber.getPoints(), canvasProperties)


        drawPathWithBorderAndFill(
            firstNumberPoints,
            fillColor = colors.firstFill,
            borderColor = colors.firstStroke,
            borderWidth = 4f
        )

        drawPathWithBorderAndFill(
            secondNumberPoints,
            fillColor = colors.secondFill,
            borderColor = colors.secondStroke,
            borderWidth = 4f
        )
        if (showIntersectionPoints) {
            val intersectionPoints = getPointsOffsetInCanvas(
                LinearAlgebra.getIntersectionPointsAndRange(
                    firstNumber,
                    secondNumber
                ), canvasProperties
            )
            drawPoints(
                intersectionPoints,
                pointMode = PointMode.Points,
                brush = SolidColor(colors.intersectionPoints),
                cap = StrokeCap.Round,
                strokeWidth = 8.dp.toPx()
            )
        }
    }
}


internal fun DrawScope.drawPathWithBorderAndFill(
    points: List<Offset>,
    fillColor: Color,
    borderColor: Color,
    borderWidth: Float,
) {
    val path = Path().apply {
        if (points.isNotEmpty()) {
            moveTo(points[0].x, points[0].y)
            for (i in 1 until points.size) {
                lineTo(points[i].x, points[i].y)
            }
            close()
        }
    }

    if(borderWidth > 0f) {
        drawPath(
            path = path,
            color = borderColor,
            style = Stroke(borderWidth)
        )
    }

    drawPath(
        path = path,
        color = fillColor
    )
}


data class CanvasProperties(
    val width: Float,
    val height: Float,
    val start: Float,
    val offset: Float,
)

internal fun getPointsOffsetInCanvas(
    points: List<Point<Double>>,
    properties: CanvasProperties
): List<Offset> {
    val offsets = mutableListOf<Offset>()
    points.forEach { point ->
        offsets.add(getPointInCanvas(point, properties))
    }
    if(points.isNotEmpty())
        offsets.add(offsets.first())
    return offsets
}

fun getPointInCanvas(
    point: Point<Double>,
    properties: CanvasProperties
): Offset {
    val x = (point.x - properties.start) / properties.offset * properties.width
    val y =  -point.y * properties.height + properties.height
    return Offset(x.toFloat(), y.toFloat())
}



@Preview
@Composable
fun RenderFuzzyNumberPreview(

) {
    val secondNumber = TriangleFuzzyNumber(1.0, 3.0, 5.0)
    val firstNumber = TrapezoidFuzzyNumber(0.0, 2.0, 3.0, 4.0)

    PreviewSurface(darkTheme = true, ) {
        RenderTwoFuzzyNumbers(
            firstNumber = firstNumber,
            secondNumber = secondNumber,
            modifier = Modifier.fillMaxWidth().height(300.dp).padding(14.dp),
            colors = RenderTwoFuzzyNumberColors(
                firstFill = MaterialTheme.colorScheme.primary.copy(0.2f),
                secondFill = MaterialTheme.colorScheme.secondary.copy(0.2f),
                firstStroke = MaterialTheme.colorScheme.primary,
                secondStroke = MaterialTheme.colorScheme.secondary,
                intersectionPoints = MaterialTheme.colorScheme.tertiary
            )
        )
    }
}