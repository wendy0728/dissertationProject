package com.example.farm.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * CoordinateDao.kt
 * Written By: Jing Wen Ng
 *
 * Accessing CoordinateData data using CoordinateDao
 */


@Dao
interface CoordinateDao {

    //get all the data from path using SQL queries
    @Query("Select * from coordinate")
    fun getAllCoordinate(): Flow<List<CoordinateData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(coordinateData: CoordinateData): Long



}