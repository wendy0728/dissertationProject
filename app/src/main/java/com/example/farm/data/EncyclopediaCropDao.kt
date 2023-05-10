package com.example.farm.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * EncyclopediaCropDao.kt
 * Written By: Jing Wen Ng
 *
 * Accessing EncyclopediaCropData data using EncyclopediaCropDao
 */
@Dao
interface EncyclopediaCropDao {

    //get all the data from encyclopediaCrop using SQL queries
    @Query("Select * from encyclopediaCrop")
    fun getAllCrop() : Flow<List<EncyclopediaCropData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(encyclopediaCropData: EncyclopediaCropData): Long


}