package com.example.farm.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * ResultDao.kt
 * Written By: Jing Wen Ng
 *
 * Accessing ResultData data using ResultDao
 */

@Dao
interface ResultDao {

    //get all the data from result using SQL queries
    @Query("Select * from result")
    fun getAllResult() : Flow<List<ResultData>>

    //insert data into result using SQL queries
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(resultData: ResultData): Long


}