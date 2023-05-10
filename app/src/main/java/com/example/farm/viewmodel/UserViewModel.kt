package com.example.farm.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.farm.model.User
import com.example.farm.repository.UserRepository
import com.example.farm.repository.userDomainsModel
import kotlinx.coroutines.launch


/**
 * UserViewModel.kt
 * Written By: Jing Wen Ng
 *
 * view model for the user
 */

class UserViewModel(private val repository: UserRepository, private val applicationContext: Application) : AndroidViewModel(applicationContext) {

    var users: LiveData<List<User>> = getUsers()

    var getUser = ""

    //get all the data from the user table
    private fun getUsers() : MutableLiveData<List<User>> = Transformations.map(repository.allUsers){
        it.userDomainsModel(applicationContext)
    } as MutableLiveData<List<User>>

    //insert all the data to database
    fun insert(
        email: String,
        name: String,
        occupation: String,
        password: String,
        salt: String
    ) = viewModelScope.launch{
        repository.insert(
            email = email,
            name = name,
            occupation = occupation,
            password = password,
            salt = salt
        )
    }

    //update information
    fun update(user: User) = viewModelScope.launch {
        repository.update(user)
    }


}

class UserViewModelFactory(private val repository: UserRepository, private val applicationContext: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            return UserViewModel(repository, applicationContext) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

