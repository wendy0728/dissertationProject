package com.example.farm.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.farm.model.Result
import com.example.farm.repository.ResultRepository
import com.example.farm.repository.resultDomainModels
import kotlinx.coroutines.launch
import java.util.Date

/**
 * ResultViewModel.kt
 * Written By: Jing Wen Ng
 *
 *
 *view model for the result
 */


class ResultViewModel(private val repository: ResultRepository, private val applicationContext: Application) : AndroidViewModel(applicationContext) {

    var results: LiveData<List<Result>> = getResults()

    //get all the result from result table
    private fun getResults() : MutableLiveData<List<Result>> = Transformations.map(repository.result){
        it.resultDomainModels(applicationContext)
    } as MutableLiveData<List<Result>>


    //insert data to the database
    fun insert(
        fieldId: Int,
        updatedDate: Date,
        pestDetected: ArrayList<Int>,
        updatedBy: String
    ) = viewModelScope.launch{
        repository.insert(
            fieldId = fieldId,
            updatedDate = updatedDate,
            pestDetected = pestDetected,
            updatedBy = updatedBy
        )
    }


}

class ResultViewModelFactory(private val repository: ResultRepository, private val applicationContext: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ResultViewModel::class.java)) {
            return ResultViewModel(repository, applicationContext) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}