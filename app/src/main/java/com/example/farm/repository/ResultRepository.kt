package com.example.farm.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.farm.data.ResultDao
import com.example.farm.data.ResultData
import com.example.farm.model.Result
import java.util.Date

/**
 * ResultRepository.kt
 * Written By: Jing Wen Ng
 *
 * This repository is instantiated in Application.kt
 *
 */

class ResultRepository(private val resultDao: ResultDao) {

    val result: LiveData<List<ResultData>> = resultDao.getAllResult().asLiveData()

    suspend fun insert(
        fieldId: Int,
        updatedDate: Date,
        pestDetected: ArrayList<Int>,
        updatedBy:String): Long{
        var result = Result(
            fieldId = fieldId,
            updatedDate = updatedDate,
            pestDetected = pestDetected,
            updatedBy=updatedBy
        )
        return resultDao.insert(result.resultDatabaseEntity())
    }

}

fun List<ResultData>.resultDomainModels(context: Context): List<Result>{
    return map{
        Result(
            id = it.id,
            fieldId = it.fieldId,
            updatedDate = it.updatedDate,
            pestDetected = it.pestDetected,
            updatedBy = it.updatedBy
        )
    }
}

fun Result.resultDatabaseEntity(): ResultData {
    return ResultData(
        id =id,
        fieldId = fieldId,
        pestDetected = pestDetected,
        updatedDate = updatedDate,
        updatedBy=updatedBy
    )
}