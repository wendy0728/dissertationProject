package com.example.farm.repository

import android.content.Context
import androidx.lifecycle.asLiveData
import com.example.farm.data.CoordinateDao
import com.example.farm.data.CoordinateData
import com.example.farm.data.UserData
import com.example.farm.model.Coordinate
import com.example.farm.model.User

/**
 * CoordinateRepository.kt
 * Written By: Jing Wen Ng
 *
 * This repository is instantiated in Application.kt
 *
 */

class CoordinateRepository(private val coordinateDao: CoordinateDao) {
    fun getAllCoordinate() = coordinateDao.getAllCoordinate().asLiveData()

    suspend fun insert(
        long: Double,
        lat: Double,
        fieldId: Int): Long{
        var coordinate = Coordinate(
            long = long,
            lat = lat,
            fieldId = fieldId
        )
        return coordinateDao.insert(coordinate.coordinateDatabaseEntity())
    }

}


fun List<CoordinateData>.coordinateDomainModels(context: Context): List<Coordinate>{
    return map{
        Coordinate(
            id = it.id,
            long = it.long,
            lat = it.lat,
            fieldId = it.fieldId
        )
    }
}

fun Coordinate.coordinateDatabaseEntity(): CoordinateData {
    return CoordinateData(
        id = id,
        long = long,
        lat = lat,
        fieldId = fieldId
    )
}