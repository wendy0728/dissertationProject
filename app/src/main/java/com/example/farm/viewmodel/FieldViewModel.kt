package com.example.farm.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.farm.model.Field
import com.example.farm.repository.FieldRepository
import com.example.farm.repository.fieldDomainModels
import kotlinx.coroutines.launch

/**
 * FieldViewModel.kt
 * Written By: Jing Wen Ng
 *
 *
 *view model for the field
 */

class FieldViewModel(private val repository: FieldRepository, private val applicationContext: Application) : AndroidViewModel(applicationContext) {

    var fields: LiveData<List<Field>> = getFields()

    //record the weather, temperature and city of the user
    var weatherTemp = 0.0
    var weatherIconUrl = ""
    var city = ""

    //get all the information from the field
    fun getFields() : MutableLiveData<List<Field>> = Transformations.map(repository.field){
        it.fieldDomainModels(applicationContext)
    } as MutableLiveData<List<Field>>

    //insert data
    fun insert(
        name: String,
        location: String,
        crop: String
    ) = viewModelScope.launch{
        repository.insert(
            name = name,
            location = location,
            crop = crop
        )
    }


}

class FieldViewModelFactory(private val repository: FieldRepository, private val applicationContext: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FieldViewModel::class.java)) {
            return FieldViewModel(repository, applicationContext) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

