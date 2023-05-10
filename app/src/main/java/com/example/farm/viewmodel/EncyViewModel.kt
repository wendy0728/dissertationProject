package com.example.farm.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.farm.model.EncyCrop
import com.example.farm.repository.EncyRepository
import com.example.farm.repository.encyDomainModels
import kotlinx.coroutines.launch

/**
 * EncyViewModel.kt
 * Written By: Jing Wen Ng
 *
 *
 *view model for crop's for encyclopedia
 */


class EncyViewModel(private val repository: EncyRepository, private val applicationContext: Application) : AndroidViewModel(applicationContext) {

    var crops: LiveData<List<EncyCrop>> = getEncyCrops()

    //get all the information from EncyCrop table
    private fun getEncyCrops() : MutableLiveData<List<EncyCrop>> = Transformations.map(repository.ency){
        it.encyDomainModels(applicationContext)
    } as MutableLiveData<List<EncyCrop>>

    //insert data
    fun insert(
        name: String
    ) = viewModelScope.launch{
        repository.insert(
            name = name
        )
    }


}

class EncyViewModelFactory(private val repository: EncyRepository, private val applicationContext: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EncyViewModel::class.java)) {
            return EncyViewModel(repository, applicationContext) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

