package com.example.farm.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.farm.model.Coordinate
import com.example.farm.repository.CoordinateRepository
import com.example.farm.repository.coordinateDomainModels
import kotlinx.coroutines.launch

/**
 * CoordinateViewModel.kt
 * Written By: Jing Wen Ng
 *
 * view model for field's coordinate
 */


class CoordinateViewModel(private val repository: CoordinateRepository, private val applicationContext: Application) : AndroidViewModel(applicationContext) {

    var coordinate: LiveData<List<Coordinate>> = getCoordinate()

    //get all the information from Coordinate table
    private fun getCoordinate() : MutableLiveData<List<Coordinate>> = Transformations.map(repository.getAllCoordinate()){
        it.coordinateDomainModels(applicationContext)
    } as MutableLiveData<List<Coordinate>>

    //insert information to coordinate table
    fun insert(
        long: Double,
        lat: Double,
        fieldId: Int
    ) = viewModelScope.launch{
        repository.insert(
            long = long,
            lat = lat,
            fieldId = fieldId
        )
    }


}

class PathViewModelFactory(private val repository: CoordinateRepository, private val applicationContext: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CoordinateViewModel::class.java)) {
            return CoordinateViewModel(repository, applicationContext) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}