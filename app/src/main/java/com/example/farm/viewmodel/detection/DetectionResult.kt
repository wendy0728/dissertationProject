package com.example.farm.viewmodel.detection

import android.graphics.Rect

/**
 * DetectionResult.kt
 * Written By: Luna Lot
 * Code From: https://github.com/PervasiveComputingGroup/FCIMCS_Mobile
 *
 * Class for DetectionResult
 */



class DetectionResult(var classIndex: Int, var score: Float, var rect: Rect) {

    override fun toString(): String {
        return "Result{" +
                "classIndex=" + classIndex +
                ", score=" + score +
                ", rect=" + rect +
                '}'
    }
}