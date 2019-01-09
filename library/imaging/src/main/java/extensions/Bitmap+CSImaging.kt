package renetik.android.imaging.extensions

import android.graphics.Bitmap
import android.graphics.Bitmap.createScaledBitmap
import android.graphics.Matrix
import android.graphics.RectF

fun Bitmap.scale(maxTargetWidth: Int, maxTargetHeight: Int): Bitmap {
    val matrix = Matrix()
    val inRect = RectF(0f, 0f, width.toFloat(), height.toFloat())
    val outRect = RectF(0f, 0f, maxTargetWidth.toFloat(), maxTargetHeight.toFloat())
    matrix.setRectToRect(inRect, outRect, Matrix.ScaleToFit.CENTER)
    val values = FloatArray(9)
    matrix.getValues(values)
    val scaled = createScaledBitmap(this, (width * values[0]).toInt(),
            (height * values[4]).toInt(), true)
    recycle()
    return scaled;
}