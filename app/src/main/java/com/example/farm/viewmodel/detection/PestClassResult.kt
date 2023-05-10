package com.example.farm.viewmodel.detection

/**
 * PestClassResult.kt
 * Written By: Luna Lot
 * Code From: https://github.com/PervasiveComputingGroup/FCIMCS_Mobile
 *
 * There will be a class named PestClassResult which record the result.
 * The classIndex which is represent type of pests
 * The classNum is the number for a specific type of pests
 */


class PestClassResult(
    var classIndex: Int,
    var classNum: Int,
)

val pestClass = arrayListOf<String>("pest", "ant", "aphid", "beetle", "bollworm", "caterpillar", "fleas","fly","leaf miner","mit","snail","spider","slug")

fun findPestClassIndex(className: String): Int {
    pestClass.forEachIndexed { index, cln ->
        if (cln == className) {
            return index
        }
    }
    return -1
}