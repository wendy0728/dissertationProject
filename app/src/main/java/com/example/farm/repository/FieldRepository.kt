package com.example.farm.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.farm.data.FieldDao
import com.example.farm.data.FieldData
import com.example.farm.data.UserData
import com.example.farm.model.Field
import com.example.farm.model.User

/**
 * FieldRepository.kt
 * Written By: Jing Wen Ng
 *
 * This repository is instantiated in Application.kt
 *
 */

class FieldRepository(private val fieldDao: FieldDao) {

    val field: LiveData<List<FieldData>> = fieldDao.getAllField().asLiveData()

    suspend fun insert(
        name: String,
        location: String,
        crop: String): Long{
        var field = Field(
            name = name,
            location = location,
            crop = crop
        )
        return fieldDao.insert(field.fieldDatabaseEntity())
    }


}


fun Field.fieldDatabaseEntity(): FieldData {
    return FieldData(
        id =id,
        name = name,
        location = location,
        crop = crop
    )
}

fun List<FieldData>.fieldDomainModels(context: Context): List<Field>{
    return map{
        Field(
            id = it.id,
            name = it.name,
            location = it.location,
            crop = it.crop
        )
    }
}