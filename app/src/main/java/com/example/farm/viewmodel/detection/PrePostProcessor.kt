package com.example.farm.viewmodel.detection

import android.graphics.Rect
import java.util.*

/**
 * PrePostProcessor.kt
 * Written By: Luna Lot
 * Code From: https://github.com/PervasiveComputingGroup/FCIMCS_Mobile
 *
 *
 */



object PrePostProcessor {
    // for yolov5 model, no need to apply MEAN and STD
    var NO_MEAN_RGB = floatArrayOf(0.0f, 0.0f, 0.0f)
    var NO_STD_RGB = floatArrayOf(1.0f, 1.0f, 1.0f)

    // model input image size
    var mInputWidth = 640
    var mInputHeight = 640

    // model output is of size 25200*(num_of_class+5)
    private val mOutputRow = 8400 // as decided by the YOLOv5 model for input image of size 640*640

    private val mOutputColumn = 6 // left, top, right, bottom, score and 80 class probability

    private val mThreshold = 0.60f // score above which a detection is generated

    private val mNmsLimit = 25

    lateinit var mClasses: ArrayList<String>

    // The two methods nonMaxSuppression and IOU below are ported from https://github.com/hollance/YOLO-CoreML-MPSNNGraph/blob/master/Common/Helpers.swift
    // The two methods nonMaxSuppression and IOU below are ported from https://github.com/hollance/YOLO-CoreML-MPSNNGraph/blob/master/Common/Helpers.swift
    /**
     * Removes bounding boxes that overlap too much with other boxes that have
     * a higher score.
     * - Parameters:
     * - boxes: an array of bounding boxes and their scores
     * - limit: the maximum number of boxes that will be selected
     * - threshold: used to decide whether boxes overlap too much
     */
    fun nonMaxSuppression(
        boxes: ArrayList<DetectionResult>,
        limit: Int,
        threshold: Float
    ): ArrayList<DetectionResult> {

        // Do an argsort on the confidence scores, from high to low.
        boxes.sortWith(Comparator { o1, o2 -> o1.score.compareTo(o2.score) })

        val selected = ArrayList<DetectionResult>()
        val active = BooleanArray(boxes.size)
        Arrays.fill(active, true)
        var numActive = active.size

        // The algorithm is simple: Start with the box that has the highest score.
        // Remove any remaining boxes that overlap it more than the given threshold
        // amount. If there are any boxes left (i.e. these did not overlap with any
        // previous boxes), then repeat this procedure, until no more boxes remain
        // or the limit has been reached.
        var done = false
        var i = 0
        while (i < boxes.size && !done) {
            if (active[i]) {
                val boxA = boxes[i]
                selected.add(boxA)
                if (selected.size >= limit) break
                for (j in i + 1 until boxes.size) {
                    if (active[j]) {
                        val boxB = boxes[j]
                        if (IOU(boxA.rect, boxB.rect) > threshold) {
                            active[j] = false
                            numActive -= 1
                            if (numActive <= 0) {
                                done = true
                                break
                            }
                        }
                    }
                }
            }
            i++
        }
        return selected
    }

    /**
     * Computes intersection-over-union overlap between two bounding boxes.
     */
    fun IOU(a: Rect, b: Rect): Float {
        val areaA = ((a.right - a.left) * (a.bottom - a.top)).toFloat()
        if (areaA <= 0.0) return 0.0f
        val areaB = ((b.right - b.left) * (b.bottom - b.top)).toFloat()
        if (areaB <= 0.0) return 0.0f
        val intersectionMinX = Math.max(a.left, b.left).toFloat()
        val intersectionMinY = Math.max(a.top, b.top).toFloat()
        val intersectionMaxX = Math.min(a.right, b.right).toFloat()
        val intersectionMaxY = Math.min(a.bottom, b.bottom).toFloat()
        val intersectionArea = Math.max(intersectionMaxY - intersectionMinY, 0f) *
                Math.max(intersectionMaxX - intersectionMinX, 0f)
        return intersectionArea / (areaA + areaB - intersectionArea)
    }

    fun outputsToNMSPredictions(
        outputs: FloatArray,
        imgScaleX: Float,
        imgScaleY: Float,
        ivScaleX: Float,
        ivScaleY: Float,
        startX: Float,
        startY: Float
    ): ArrayList<DetectionResult> {
        val detectionResults = ArrayList<DetectionResult>()
        for (i in 0 until mOutputRow) {
            if (outputs[i * mOutputColumn + 4] > mThreshold) {
                val x = outputs[i * mOutputColumn]
                val y = outputs[i * mOutputColumn + 1]
                val w = outputs[i * mOutputColumn + 2]
                val h = outputs[i * mOutputColumn + 3]

//                float left = imgScaleX * (x - w/2);
//                float top = imgScaleY * (y - h/2);
//                float right = imgScaleX * (x + w/2);
//                float bottom = imgScaleY * (y + h/2);
                val left = imgScaleX * (x - w / 2) * mInputWidth
                val top = imgScaleY * (y - h / 2) * mInputHeight
                val right = imgScaleX * (x + w / 2) * mInputWidth
                val bottom = imgScaleY * (y + h / 2) * mInputHeight
                var max = outputs[i * mOutputColumn + 5]
                var cls = 0
                for (j in 0 until mOutputColumn - 5) {
                    if (outputs[i * mOutputColumn + 5 + j] > max) {
                        max = outputs[i * mOutputColumn + 5 + j]
                        cls = j
                    }
                }
                val rect = Rect(
                    (startX + ivScaleX * left).toInt(),
                    (startY + top * ivScaleY).toInt(),
                    (startX + ivScaleX * right).toInt(),
                    (startY + ivScaleY * bottom).toInt()
                )
                val detectionResult = DetectionResult(cls, outputs[i * mOutputColumn + 4], rect)
                detectionResults.add(detectionResult)
            }
        }
        return nonMaxSuppression(detectionResults, mNmsLimit, mThreshold)
    }
}