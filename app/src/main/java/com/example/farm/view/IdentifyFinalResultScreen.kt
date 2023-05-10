package com.example.farm.view

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.farm.R
import com.example.farm.model.Pest
import com.example.farm.navigation.Screen
import com.example.farm.viewmodel.PestViewModel
import com.example.farm.viewmodel.ResultViewModel
import com.example.farm.view.*
import com.example.farm.viewmodel.PestMapMarkInfo
import com.example.farm.viewmodel.UserViewModel
import java.util.Date

/**
 * IdentifyFinalResultScreen.kt
 * Written By: Jing Wen Ng
 *
 * The page shows the final result, user can choose to save or don't save the result
 * If they choose to don't save, the data wouldn't save to the database
 * It they choose to save the data, the data will be save into the database
 * I have referenced some of the code from Luna Lou at https://github.com/PervasiveComputingGroup/FCIMCS_Mobile,
 * but it shows that this part of code haven't been successfully implement.
 *
 *@param navController
 *@param pestViewModel
 *@param resultViewModel
 *@param userViewModel
 *@param fieldId
 */

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun IdentifyFinalResultScreen(
    navController : NavController,
    pestViewModel : PestViewModel,
    resultViewModel: ResultViewModel,
    userViewModel: UserViewModel,
    fieldId : String
){



    var getPestId = ArrayList<Int>()
    pestViewModel.markers.forEach(){marker->
        marker.detectionClassResult?.forEach(){
            getPestId.add(it.classIndex+1)
        }
    }

    val pests by pestViewModel.allPests.observeAsState()
    var detectedPestsDetails =  ArrayList<Pest>()

    pests?.forEach() {
        Log.i("pests",pests.toString())
    }

    var pestsId = ArrayList<Int>()

    //get all the identified name of pests
    getPestId.distinct().forEach { detectedPest ->
        pests?.forEach {pest->
            if (detectedPest==pest.id){
                detectedPestsDetails.add(pest)
            }

        }
    }


    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        detectedPestsDetails.forEach {
            pestsId.add(it.id)
            Card(
                modifier = Modifier
                    .padding(10.dp)
                    .wrapContentHeight(),
                elevation = 5.dp,
                shape = MaterialTheme.shapes.small,
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .background(color = primaryColor),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.Top,
                    ) {
                        Text(
                            text = it.name,
                            modifier = Modifier.padding(start = 20.dp, top = 5.dp, bottom = 5.dp),
                            color = whiteColor
                        )
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .background(MaterialTheme.colors.surface)
                            .padding(start = 20.dp, top = 10.dp, bottom = 10.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(text = stringResource(R.string.prevention), color = secondaryColor)
                        Text(
                            text = it.prevention,
                            modifier = Modifier.padding(top = 5.dp),
                            color = blackColor
                        )
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .background(MaterialTheme.colors.surface)
                            .padding(start = 20.dp, top = 10.dp, bottom = 10.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(text = stringResource(R.string.recommendation), color = secondaryColor)
                        Text(
                            text = it.recommendation,
                            modifier = Modifier.padding(top = 5.dp),
                            color = blackColor
                        )
                    }
                }

            }
        }

        Row(modifier = Modifier
            .fillMaxSize(),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceAround) {

            //save the result to database
            Button(
                onClick = {
                    val currentDate = Date()
                    resultViewModel.insert(
                        fieldId = fieldId.toInt(),
                        updatedDate = currentDate,
                        pestDetected = pestsId,
                        updatedBy = userViewModel.getUser
                    )
                    pestViewModel.markers.forEach{
                        it.detectionClassResult?.clear()
                    }
                    pestViewModel.markers = mutableListOf<PestMapMarkInfo>()
                    navController.navigate(Screen.IdentifyMain.route)
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = secondaryColor),
                modifier = Modifier.padding(20.dp)
            ) {
                Text(text = stringResource(R.string.submit), color = whiteColor)

            }

            //don't make any further action and back to IdentifyMainScreen
            Button(
                onClick = {
                    pestViewModel.markers.forEach{
                        it.detectionClassResult?.clear()
                    }
                    pestViewModel.markers = mutableListOf<PestMapMarkInfo>()
                    navController.navigate(Screen.IdentifyMain.route)
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = secondaryColor),
                modifier = Modifier.padding(20.dp)
            ) {
                Text(text = stringResource(R.string.deleteResult), color = whiteColor)

            }
        }
    }



}