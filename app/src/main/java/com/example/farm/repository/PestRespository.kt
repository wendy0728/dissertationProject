package com.example.farm.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.farm.data.FieldData
import com.example.farm.data.PestDao
import com.example.farm.data.PestsData
import com.example.farm.model.Field
import com.example.farm.model.Pest
import com.example.farm.model.User

/**
 * PestRepository.kt
 * Written By: Jing Wen Ng
 *
 * This repository is instantiated in Application.kt
 *
 */

class PestRepository(private val pestDao: PestDao) {

    val pests: LiveData<List<PestsData>> = pestDao.getAllPests().asLiveData()

    suspend fun insert(
        name: String,
        recommendation: String,
        prevention: String): Long{
        var pest = Pest(
            name = name,
            recommendation = recommendation,
            prevention = prevention
        )
        return pestDao.insert(pest.pestDatabaseEntity())
    }


}

fun List<PestsData>.pestDomainModels(context: Context): List<Pest>{
    return map{
        Pest(
            id = it.id,
            name = it.name,
            recommendation = it.recommendation,
            prevention = it.prevention

            )
    }
}

fun Pest.pestDatabaseEntity(): PestsData {
    return PestsData(
        id =id,
        name = name,
        recommendation = recommendation,
        prevention = prevention
    )
}
