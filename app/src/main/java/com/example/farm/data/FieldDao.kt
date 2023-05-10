package com.example.farm.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * FieldDao.kt
 * Written By: Jing Wen Ng
 *
 * Accessing FieldData data using FieldDao
 */

@Dao
interface FieldDao {

   //get all the data from field using SQL queries
   @Query("Select * from field")
   fun getAllField() : Flow<List<FieldData>>

   @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun insert(fieldData: FieldData): Long



}