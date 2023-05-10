package com.example.farm.viewmodel

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.*
import com.example.farm.model.Pest
import com.example.farm.repository.PestRepository
import com.example.farm.repository.pestDomainModels
import com.example.farm.viewmodel.detection.DetectionResult
import com.example.farm.viewmodel.detection.PestClassResult
import com.example.farm.viewmodel.detection.assetFilePath
import com.example.farm.viewmodel.detection.generateDetectionPoint
import com.example.farm.viewmodel.detection.useDetectionModel
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch
import org.pytorch.LiteModuleLoader
import org.pytorch.Module

/**
 * PestViewModel.kt
 * Written By: Jing Wen Ng, Luna Lou
 * Some of the code are from Luna Lou at https://github.com/PervasiveComputingGroup/FCIMCS_Mobile
 *
 *view model for the pests
 */


class PestViewModel(private val repository: PestRepository, private val applicationContext: Application) : AndroidViewModel(applicationContext) {

    var allPests: LiveData<List<Pest>> = getPests()

    //get all information from pests table
    fun getPests() : MutableLiveData<List<Pest>> = Transformations.map(repository.pests){
        it.pestDomainModels(applicationContext)
    } as MutableLiveData<List<Pest>>


    //insert information
    fun insert(
        name: String,
        prevention: String,
        recommendation: String
    ) = viewModelScope.launch{
        repository.insert(
            name = name,
            prevention = prevention,
            recommendation= recommendation
        )
    }

    //code below are from Luna Lou at https://github.com/PervasiveComputingGroup/FCIMCS_Mobile
    private val modelPath = "pest.uk.model.ptl"

    var markers = mutableListOf<PestMapMarkInfo>()

    fun getDecisionMarkers(pointList: List<LatLng>) {
        val decisionPointResult = generateDetectionPoint(pointList)
        decisionPointResult.forEach {
            markers.add(PestMapMarkInfo(it, false))
        }
    }

    private lateinit var detectionModel: Module

    fun loadDetectionModel(context: Context) {
        Log.e("model", "load")
        detectionModel = LiteModuleLoader.load(
            assetFilePath(
                context,
                modelPath
            )
        )
    }


    var detectionResult: ArrayList<DetectionResult> = arrayListOf()
    var detectionClassResult: MutableList<PestClassResult> = mutableListOf()

    fun getDetectionResult(
        context: Context,
        uri: Uri,
        screenWidth: Int,
        finish: () -> Unit,
    ) {

        val imageBitmap = uriToBitmap(context, uri)
        val scaleWidth = screenWidth.toFloat() / imageBitmap.width.toFloat()
        val viewHeight = (imageBitmap.height.toFloat() * scaleWidth).toInt()

        detectionResult = useDetectionModel(detectionModel, imageBitmap, viewHeight, screenWidth)
        detectionClassResult = countDetectionResult(detectionResult)
        finish()
    }


    fun finishDetection(index: Int) {
        markers[index].processed = true
        detectionResult.clear()
        detectionClassResult.clear()
    }


}

class PestViewModelFactory(private val repository: PestRepository, private val applicationContext: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PestViewModel::class.java)) {
            return PestViewModel(repository, applicationContext) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

//code below are from Luna Lou at https://github.com/PervasiveComputingGroup/FCIMCS_Mobile
//get the information for each marker
class PestMapMarkInfo(
    var point: LatLng,
    var processed: Boolean,
    var photoUri: Uri?=null,
    var detectionClassResult: MutableList<PestClassResult>? = mutableListOf(),
)

//code below are from Luna Lou at https://github.com/PervasiveComputingGroup/FCIMCS_Mobile
fun uriToBitmap(context: Context, uri: Uri): Bitmap {
    return MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
}

//code below are from Luna Lou at https://github.com/PervasiveComputingGroup/FCIMCS_Mobile
//gather all the result for a specific marker
fun countDetectionResult(resultList: ArrayList<DetectionResult>): MutableList<PestClassResult> {
    val classList = mutableStateListOf<PestClassResult>()
    resultList.forEach { detectionResult ->
        var idx = detectionResult.classIndex
        if (classList.isEmpty()) {
            classList.add(PestClassResult(idx, 1))
        } else {
            classList.forEachIndexed { index, pestClassResult ->
                if (pestClassResult.classIndex == idx) {
                    classList[index].classNum += 1
                } else {
                    classList.add(PestClassResult(idx, 1))
                }
            }
        }
    }
    return classList
}