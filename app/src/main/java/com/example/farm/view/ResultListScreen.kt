package com.example.farm.view

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.farm.R
import com.example.farm.navigation.Screen
import com.example.farm.viewmodel.CoordinateViewModel
import com.example.farm.viewmodel.FieldViewModel
import com.example.farm.viewmodel.PestViewModel
import com.example.farm.viewmodel.ResultViewModel
import com.example.farm.viewmodel.UserViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Polygon
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

/**
 * ResultListScreen.kt
 * Written By: Jing Wen Ng
 *
 * This page allow user to view the list of results and view the location of the field.
 * User able to click on the result card to view more details about the result.
 *
 *
 *@param navController
 *@param fieldViewModel
 *@param resultViewModel
 *@param coordinateViewModel
 *@param pestViewModel
 *@param fieldId  The field id
 */

@Composable
fun ResultListScreen(navController : NavController,
                     coordinateViewModel: CoordinateViewModel,
                     fieldViewModel: FieldViewModel,
                    resultViewModel : ResultViewModel,
                    pestViewModel: PestViewModel,
                        fieldId:String
){


    val results by resultViewModel.results.observeAsState()
    val listResult = results?.filter {it.fieldId==fieldId.toInt()}

    val coordinates by coordinateViewModel.coordinate.observeAsState()
    val getCoordinateById = coordinates?.filter { it.fieldId==fieldId.toInt() }

    Log.i("getCoordinateById", getCoordinateById.toString())

    val fields by fieldViewModel.fields.observeAsState()
    val getField = fields?.firstOrNull() { it.id==fieldId.toInt() }


    val pests by pestViewModel.allPests.observeAsState()


    val allPests = ArrayList<String>()
    listResult?.forEach { result ->

        var allDetectedPest = ArrayList<String>()
        var allPestsId = result.pestDetected
        for (pestId in allPestsId) {
            for (pest in pests!!) {
                if (pestId == pest.id) {
                    allDetectedPest.add(pest.name)
                }
            }
        }

        if(allDetectedPest.isNotEmpty()) {
            allDetectedPest?.forEach { name ->
                allPests.add(name)
            }
        }
    }


    var allPestText = ""
    if(allPests.isNotEmpty()) {
        var lastPestName = allPests.last()
        allPests.distinct().forEach { name ->
            if (lastPestName == name) {
                allPestText += name
            } else {
                allPestText += "$name, "
            }
        }
    }

    Log.i("allPestText",allPestText)



    var mapPoints = ArrayList<LatLng>()
    getCoordinateById?.forEach {
        mapPoints.add(LatLng(it.lat,it.long))
    }

    Log.i("getCoordinateById", mapPoints.toString())


    val pointCoroutine = rememberCoroutineScope()
    var showMap by remember { mutableStateOf(false) }

    LaunchedEffect(mapPoints){
        if(mapPoints.isNotEmpty()){
            pointCoroutine.launch{
                showMap = true
            }
        }

    }



    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp


    //show the field located at
    if(showMap) {


        TopAppBar(
            title = {
                Text(text= getField!!.name, fontSize = 20.sp,color= whiteColor )},
            navigationIcon = {
                IconButton(onClick = {
                    navController.navigate(Screen.FieldList.route)
                }) {
                    Icon(painter= painterResource(R.drawable.baseline_arrow_back_24), contentDescription = "back", tint= whiteColor)
                }
            },
            backgroundColor = primaryColor
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 55.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


        Box(Modifier.height(screenHeight / 2)) {

            val cameraPosition = getCenterPoint(mapPoints)


            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(cameraPosition, 14f)
            }
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
            )
            {
                Polygon(
                    points = mapPoints,
                    fillColor = primaryColor,
                    strokeWidth = 3f
                )

            }
        }


        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(bottom = 10.dp)
        ) {


            Card(
                modifier= Modifier
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
                            .background(color = secondaryColor),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.Top,
                    ) {
                        Text(
                            text = stringResource(R.string.detectedPest),
                            modifier = Modifier.padding(start = 20.dp, top = 5.dp, bottom = 5.dp),
                            color = whiteColor,
                            fontSize = 16.sp
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
                        Text(text = allPestText, fontSize = 14.sp)
                    }
                }
            }

            Column(modifier= Modifier.padding(10.dp)) {
                Divider(
                    modifier = Modifier.fillMaxWidth(),
                    color = secondaryColor.copy(0.2f), thickness = 1.dp
                )
            }


            listResult?.forEach { result ->
                Card(
                    elevation = 2.dp,
                    modifier = Modifier
                        .padding(top = 10.dp, start = 10.dp, end = 10.dp)
                        .width(screenWidth * 0.9f)
                        .clickable {
                            var result_id = result.id
                            navController.navigate("result_details/$fieldId/$result_id")
                        },
                ) {
                    val format = SimpleDateFormat("yyyy-MM-dd")
                    val date = format.format(result.updatedDate)
                    Column() {
                        Text(
                            text = "Record Date: $date",
                            modifier = Modifier.padding(start = 10.dp, bottom = 5.dp),
                            fontSize = 14.sp
                        )
                        var allDetectedPest = ArrayList<String>()
                        var allPestsId = result.pestDetected
                        for (pestId in allPestsId) {
                            for (pest in pests!!) {
                                if (pestId == pest.id) {
                                    allDetectedPest.add(pest.name)
                                }
                            }
                        }
                        var pestName = ""
                        if(allDetectedPest.isNotEmpty()) {
                            var lastPestName = allDetectedPest.last()
                            allDetectedPest?.forEach { name ->
                                if (name == lastPestName) {
                                    pestName += name
                                } else {
                                    pestName += "$name, "
                                }
                                allPests.add(pestName)
                            }
                        }
                        Text(
                            text = "Detected: " +pestName,
                            modifier = Modifier.padding(start = 10.dp, bottom = 5.dp),
                            fontSize = 14.sp
                        )
                        Text(
                            text = "Updated By: " + result.updatedBy,
                            modifier = Modifier.padding(start = 10.dp, bottom = 10.dp),
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
        }
    }

}


fun getCenterPoint(mapPoints: List<LatLng>): LatLng {
    var lat = 0.0
    var long = 0.0
    mapPoints.forEach {
        lat += it.latitude
        long += it.longitude
    }
    lat /= mapPoints.size
    long /= mapPoints.size

    return LatLng(lat, long)
}
