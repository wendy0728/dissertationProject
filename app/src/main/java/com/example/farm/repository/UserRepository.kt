package com.example.farm.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.farm.data.UserDao
import com.example.farm.data.UserData
import com.example.farm.model.User

/**
 * UserRepository.kt
 * Written By: Jing Wen Ng
 *
 * This repository is instantiated in Application.kt
 *
 */


class UserRepository(private val userDao: UserDao) {
    val allUsers: LiveData<List<UserData>> = userDao.getAllUsers().asLiveData()

    suspend fun insert(
        email: String,
        name: String,
        occupation: String,
        password: String,
        salt:String): Long{
        var user = User(
            email = email,
            name = name,
            occupation = occupation,
            password = password,
            salt = salt
        )
        return userDao.insert(user.userDatabaseEntity())
    }

    suspend fun update(user: User){
        userDao.update(user.userDatabaseEntity())
    }
}

fun List<UserData>.userDomainsModel(context: Context): List<User>{
    return map{
        User(
            email = it.email,
            name = it.name,
            occupation = it.occupation,
            password = it.password,
            salt = it.salt
        )
    }
}

fun User.userDatabaseEntity(): UserData {
    return UserData(
        email = email,
        name = name,
        occupation = occupation,
        password = password,
        salt = salt
    )
}