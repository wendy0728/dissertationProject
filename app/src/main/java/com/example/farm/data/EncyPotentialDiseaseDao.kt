package com.example.farm.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * EncyPotentialDiseaseDao.kt
 * Written By: Jing Wen Ng
 *
 * Accessing EncyDetailsData data using EncyDetailsDao
 */

@Dao
interface EncyPotentialDiseaseDao {

    //get all the data from encyDetails using SQL queries
    @Query("Select * from encyDetails")
    fun getAllEncyDetails() : Flow<List<EncyPotentialDiseaseData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(encyPotentialDiseaseData: EncyPotentialDiseaseData): Long


}