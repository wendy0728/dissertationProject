package com.example.farm.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.farm.data.EncyclopediaCropDao
import com.example.farm.data.EncyclopediaCropData
import com.example.farm.data.FieldData
import com.example.farm.model.EncyCrop
import com.example.farm.model.EncyPotentialDisease
import com.example.farm.model.Field

/**
 * EncyRepository.kt
 * Written By: Jing Wen Ng
 *
 * This repository is instantiated in Application.kt
 *
 */

class EncyRepository(private val encyclopediaCropDao: EncyclopediaCropDao) {
    val ency: LiveData<List<EncyclopediaCropData>> = encyclopediaCropDao.getAllCrop().asLiveData()

    suspend fun insert(
        name: String): Long{
        var encyCrop = EncyCrop(
            name = name
        )
        return encyclopediaCropDao.insert(encyCrop.encyCropDatabaseEntity())
    }
}

fun List<EncyclopediaCropData>.encyDomainModels(context: Context): List<EncyCrop>{
    return map{
        EncyCrop(
            id = it.id,
            name = it.name
        )
    }
}


fun EncyCrop.encyCropDatabaseEntity(): EncyclopediaCropData {
    return EncyclopediaCropData(
        id =id,
        name = name
    )
}