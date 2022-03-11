package de.digitaldealer.cardsplease.core.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.exifinterface.media.ExifInterface
import androidx.exifinterface.media.ExifInterface.*
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File

class ImageUtils {

    private companion object {
        const val MAX_SIZE = 1600
        const val DEFAULT_QUALITY_PERCENTAGE = 90
        const val ROTATION_90_DEGREES = 90f
        const val ROTATION_180_DEGREES = 180f
        const val ROTATION_270_DEGREES = 270f
    }

    fun getProcessedBitmap(context: Context, fileName: String) = getBitmapInPortraitMode(getFile(context, fileName))
    fun processBitmap(bitmap: Bitmap): Bitmap = resizeAndCompress(bitmap)

    private fun getBitmapInPortraitMode(file: File) = resizeAndCompress(rotateBitmap(BitmapFactory.decodeFile(file.absolutePath), ExifInterface(file.absolutePath)))

    private fun resizeAndCompress(bitmap: Bitmap): Bitmap {
        val ratioBitmap = bitmap.width.toFloat() / bitmap.height.toFloat()

        var finalWidth = MAX_SIZE
        var finalHeight = MAX_SIZE
        if (1 > ratioBitmap) {
            finalWidth = (MAX_SIZE.toFloat() * ratioBitmap).toInt()
        } else {
            finalHeight = (MAX_SIZE.toFloat() / ratioBitmap).toInt()
        }
        ByteArrayOutputStream().use { stream ->
            Bitmap.createScaledBitmap(bitmap, finalWidth, finalHeight, false).compress(Bitmap.CompressFormat.JPEG, DEFAULT_QUALITY_PERCENTAGE, stream)
            return BitmapFactory.decodeStream(ByteArrayInputStream(stream.toByteArray()))
        }
    }

    private fun rotateBitmap(bitmap: Bitmap, exifInterface: ExifInterface): Bitmap = when (exifInterface.getAttributeInt(TAG_ORIENTATION, ORIENTATION_NORMAL)) {
        ORIENTATION_ROTATE_90 -> rotateImage(bitmap, ROTATION_90_DEGREES)
        ORIENTATION_ROTATE_180 -> rotateImage(bitmap, ROTATION_180_DEGREES)
        ORIENTATION_ROTATE_270 -> rotateImage(bitmap, ROTATION_270_DEGREES)
        else -> bitmap
    }

    private fun rotateImage(source: Bitmap, angle: Float): Bitmap {
        val matrix = Matrix()
        if (angle > 0) matrix.postRotate(angle)
        return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
    }

    private fun getFile(context: Context, fileName: String) = File(context.filesDir, fileName)
}
