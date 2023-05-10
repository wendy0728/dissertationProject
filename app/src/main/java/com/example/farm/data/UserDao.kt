package com.example.farm.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * UserDao.kt
 * Written By: Jing Wen Ng
 *
 * Accessing UserData data using UserDao
 */

@Dao
interface UserDao {

    //get all the data from user using SQL queries
    @Query("Select * from user")
    fun getAllUsers() : Flow<List<UserData>>

    //insert new user using SQL queries
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(userData: UserData): Long

    //update user data using SQL queries
    @Update
    suspend fun update(userData: UserData)

}