package com.example.farm.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.farm.model.EncyPotentialDisease
import com.example.farm.repository.EncyPotentialDiseaseRepository
import com.example.farm.repository.encyDetailsDomainModels
import kotlinx.coroutines.launch

/**
 * EncyPotentialDiseaseViewModel.kt
 * Written By: Jing Wen Ng
 *
 *view model for potential disease for encyclopedia
 */


class EncyPotentialDiseaseViewModel(private val repository: EncyPotentialDiseaseRepository, private val applicationContext: Application) : AndroidViewModel(applicationContext) {

    var allDetails: LiveData<List<EncyPotentialDisease>> = getAllDetails()

    //get all the information from EncyPotentialDisease Table
    private fun getAllDetails() : MutableLiveData<List<EncyPotentialDisease>> = Transformations.map(repository.getAllEncyDetails()){
        it.encyDetailsDomainModels(applicationContext)
    }as MutableLiveData<List<EncyPotentialDisease>>

    //insert the information to database
    fun insert(
        cropId: Int,
        disease: String,
        symptoms: String,
        cause: String,
        prevention: String,
        recommendation:String
    ) = viewModelScope.launch{
        repository.insert(
            cropId = cropId,
            disease = disease,
            symptoms = symptoms,
            cause = cause,
            prevention= prevention,
            recommendation= recommendation
        )
    }


}

class EncyDetailsViewModelFactory(private val repository: EncyPotentialDiseaseRepository, private val applicationContext: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EncyPotentialDiseaseViewModel::class.java)) {
            return EncyPotentialDiseaseViewModel(repository, applicationContext) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}