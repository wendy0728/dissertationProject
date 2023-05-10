package com.example.farm.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * UserData.kt
 * Written By: Jing Wen Ng
 *
 * This data class will be included all the information of the users.
 *
 * @param email The id of the user
 * @param name The name of the user
 * @param occupation The occupation of the user
 * @param password The password of the user
 * @param salt The salt in password
 */

@Entity(tableName = "user", indices=[Index(value=["email"])])
data class UserData (
    @PrimaryKey var email: String,
    @ColumnInfo(name="user_name") val name: String,
    @ColumnInfo(name="user_occupation") val occupation: String,
    @ColumnInfo(name="user_password") val password: String,
    @ColumnInfo(name="salt") val salt: String
)