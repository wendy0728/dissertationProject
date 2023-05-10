package com.example.farm.repository

import android.content.Context
import androidx.lifecycle.asLiveData
import com.example.farm.data.EncyPotentialDiseaseDao
import com.example.farm.data.EncyPotentialDiseaseData
import com.example.farm.data.UserData
import com.example.farm.model.Coordinate
import com.example.farm.model.EncyPotentialDisease
import com.example.farm.model.User

/**
 * EncyPotentialDiseaseRepository.kt
 * Written By: Jing Wen Ng
 *
 * This repository is instantiated in Application.kt
 *
 */

class EncyPotentialDiseaseRepository(private val encyPotentialDiseaseDao: EncyPotentialDiseaseDao) {
    fun getAllEncyDetails() = encyPotentialDiseaseDao.getAllEncyDetails().asLiveData()

    suspend fun insert(
        cropId: Int,
        disease: String,
        symptoms: String,
        cause: String,
        prevention:String,
        recommendation:String): Long{
        var encyPotentialDisease = EncyPotentialDisease(
            cropId = cropId,
            disease = disease,
            symptoms = symptoms,
            cause = cause,
            prevention = prevention,
            recommendation = recommendation

        )
        return encyPotentialDiseaseDao.insert(encyPotentialDisease.encyPotentialDiseaseDatabaseEntity())
        }
}

fun List<EncyPotentialDiseaseData>.encyDetailsDomainModels(context: Context): List<EncyPotentialDisease>{
    return map{
        EncyPotentialDisease(
            id = it.id,
            cropId = it.cropId,
            disease = it.disease,
            symptoms = it.symptoms,
            cause = it.cause,
            prevention = it.prevention,
            recommendation = it.recommendation
        )
    }
}

fun EncyPotentialDisease.encyPotentialDiseaseDatabaseEntity(): EncyPotentialDiseaseData {
    return EncyPotentialDiseaseData(
        id = id,
        cropId = cropId,
        disease = disease,
        symptoms = symptoms,
        cause = cause,
        prevention = prevention,
        recommendation = recommendation
    )
}