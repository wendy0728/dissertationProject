package com.example.farm.viewmodel.detection

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import org.pytorch.IValue
import org.pytorch.Module
import org.pytorch.torchvision.TensorImageUtils
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.time.LocalDateTime
import java.util.concurrent.ExecutorService

lateinit var CAMERA_DIRECTORY: File
lateinit var CAMERA_EXECUTOR: ExecutorService

/**
 * useDetectionModel.kt
 * Written By: Luna Lot
 * Code From: https://github.com/PervasiveComputingGroup/FCIMCS_Mobile
 *
 * This function will return ArrayList<DetectionResult>, by giving the some information which is
 * detectionModel, bitmap, viewHeight,viewWidth
 *
 *@param detectionModel
 *@param bitmap
 *@param viewHeight
 *@param viewWidth
 *@return ArrayList<LatLng>
 */

fun useDetectionModel(
    detectionModel: Module,
    bitmap: Bitmap,
    viewHeight: Int,
    viewWidth: Int,
): ArrayList<DetectionResult> {
    Log.i("useDetectionModel start",LocalDateTime.now().toString())


    val imgScaleX = bitmap.width.toFloat() / PrePostProcessor.mInputWidth
    val imgScaleY = bitmap.height.toFloat() / PrePostProcessor.mInputHeight
    val ivScaleX =
        if (bitmap.width > bitmap.height) viewWidth.toFloat() / bitmap.width else viewHeight.toFloat() / bitmap.height
    val ivScaleY =
        if (bitmap.height > bitmap.width) viewHeight.toFloat() / bitmap.height else viewWidth.toFloat() / bitmap.width

    val startX = 0.0.toFloat()
    val startY = 0.0.toFloat()

    Log.i("useDetectionModel load model",LocalDateTime.now().toString())

    val classes: ArrayList<String> = ArrayList()
    classes.add("pest")
    PrePostProcessor.mClasses = classes

    val resizedBitmap = Bitmap.createScaledBitmap(
        bitmap,
        PrePostProcessor.mInputWidth,
        PrePostProcessor.mInputHeight,
        true
    )

    val inputTensor = TensorImageUtils.bitmapToFloat32Tensor(
        resizedBitmap,
        PrePostProcessor.NO_MEAN_RGB,
        PrePostProcessor.NO_STD_RGB
    )

    Log.i("useDetectionModel finish image preprocess",LocalDateTime.now().toString())

    val outputTuple = detectionModel.forward(IValue.from(inputTensor)).toTensor()

    Log.i("useDetectionModel finish detection",LocalDateTime.now().toString())

    val outputs = outputTuple.dataAsFloatArray
    return PrePostProcessor.outputsToNMSPredictions(
        outputs,
        imgScaleX,
        imgScaleY,
        ivScaleX,
        ivScaleY,
        startX,
        startY
    )
}

@Throws(IOException::class)
fun assetFilePath(context: Context, assetName: String): String {
    val file = File(context.filesDir, assetName)
    if (file.exists() && file.length() > 0) {
        return file.absolutePath
    }
    context.assets.open(assetName).use { `is` ->
        FileOutputStream(file).use { os ->
            val buffer = ByteArray(4 * 1024)
            var read: Int
            while (`is`.read(buffer).also { read = it } != -1) {
                os.write(buffer, 0, read)
            }
            os.flush()
        }
        return file.absolutePath
    }
}
