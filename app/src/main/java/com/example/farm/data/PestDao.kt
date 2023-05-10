package com.example.farm.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * PestDao.kt
 * Written By: Jing Wen Ng
 *
 * Accessing PestData data using PestDao
 */

@Dao
interface PestDao {


    //get all the data from pests using SQL queries
    @Query("Select * from pests")
    fun getAllPests():  Flow<List<PestsData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pestsData: PestsData): Long

}