package bob.colbaskin.cookly.navigation.bottom_bar

import android.graphics.PointF
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import bob.colbaskin.cookly.common.design_system.theme.UfoodTheme

private const val CURVE_CIRCLE_RADIUS = 85

private val mFirstCurveStartPoint = PointF()
private val mFirstCurveControlPoint1 = PointF()
private val mFirstCurveControlPoint2 = PointF()
private val mFirstCurveEndPoint = PointF()

private val mSecondCurveControlPoint1 = PointF()
private val mSecondCurveControlPoint2 = PointF()
private var mSecondCurveStartPoint = PointF()
private var mSecondCurveEndPoint = PointF()

class BottomNavCurve: Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Generic(path = Path().apply {
            val curveDepth = CURVE_CIRCLE_RADIUS + (CURVE_CIRCLE_RADIUS / 8f)

            mFirstCurveStartPoint.set(
                (size.width / 2) - (CURVE_CIRCLE_RADIUS * 2) - (CURVE_CIRCLE_RADIUS / 4) - 8,
                curveDepth
            )
            mFirstCurveEndPoint.set(
                size.width / 2,
                0F
            )

            mSecondCurveStartPoint = mFirstCurveEndPoint
            mSecondCurveEndPoint.set(
                (size.width / 2) + (CURVE_CIRCLE_RADIUS * 2) + (CURVE_CIRCLE_RADIUS / 4) + 8,
                curveDepth
            )

            mFirstCurveControlPoint1.set(
                mFirstCurveStartPoint.x + curveDepth,
                mFirstCurveStartPoint.y
            )
            mFirstCurveControlPoint2.set(
                mFirstCurveEndPoint.x - (CURVE_CIRCLE_RADIUS * 2) + CURVE_CIRCLE_RADIUS,
                mFirstCurveEndPoint.y
            )

            mSecondCurveControlPoint1.set(
                mSecondCurveStartPoint.x + (CURVE_CIRCLE_RADIUS * 2) - CURVE_CIRCLE_RADIUS,
                mSecondCurveStartPoint.y
            )
            mSecondCurveControlPoint2.set(
                mSecondCurveEndPoint.x - (curveDepth),
                mSecondCurveEndPoint.y
            )

            moveTo(0f, curveDepth)
            lineTo(mFirstCurveStartPoint.x, mFirstCurveStartPoint.y)
            cubicTo(
                x1 = mFirstCurveControlPoint1.x, y1 = mFirstCurveControlPoint1.y,
                x2 = mFirstCurveControlPoint2.x, y2 = mFirstCurveControlPoint2.y,
                x3 = mFirstCurveEndPoint.x, y3 = mFirstCurveEndPoint.y
            )
            cubicTo(
                x1 = mSecondCurveControlPoint1.x, y1 = mSecondCurveControlPoint1.y,
                x2 = mSecondCurveControlPoint2.x, y2 = mSecondCurveControlPoint2.y,
                x3 = mSecondCurveEndPoint.x, y3 = mSecondCurveEndPoint.y
            )
            lineTo(size.width, curveDepth)
            lineTo(size.width, size.height)
            lineTo(0F, size.height)
        })
    }
}

@Preview
@Composable
private fun BottomNavBarPreview() {
    UfoodTheme {
        val navController: NavHostController = rememberNavController()
        BottomNavBar(navController = navController)
    }
}
