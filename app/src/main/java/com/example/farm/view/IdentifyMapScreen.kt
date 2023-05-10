package com.example.farm.view

import android.Manifest
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.farm.R
import com.example.farm.navigation.Screen
import com.example.farm.viewmodel.FieldViewModel
import com.example.farm.viewmodel.PestViewModel
import com.example.farm.viewmodel.CoordinateViewModel
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polygon
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch

/**
 * IdentifyMapScreen.kt
 * Written By: Jing Wen Ng
 *
 * This page will show the path of a specific field on the Google Map with 5 auto-generated marker in red colour.
 * If user click on the red marker, it will be navigate to the IdentifyCameraScreen.
 * If the marker has been processed(means that already upload image to the specific marker), the marker will turn green marker,
 * if click on it, it able to review the submitted result and will navigate to the IdentifyShowResultScreen.
 *
 *@param navController
 *@param coordinateViewModel
 *@param fieldViewModel
 *@param pestViewModel
 *@param fieldId
 */

@Composable
fun IdentifyMapScreen(navController : NavController,
                      coordinateViewModel : CoordinateViewModel,
                      fieldViewModel : FieldViewModel,
                      pestViewModel : PestViewModel,
                      fieldId: String){



    val coordinates by coordinateViewModel.coordinate.observeAsState()
    val getCoordinateByFieldId = coordinates?.filter { it.fieldId==fieldId.toInt() }

    var mapPoint = ArrayList<LatLng>()
    getCoordinateByFieldId?.forEach {
        mapPoint.add(LatLng(it.lat,it.long))
    }

    val fields by fieldViewModel.fields.observeAsState()
    val getField = fields?.firstOrNull() { it.id==fieldId.toInt() }

    Log.i("mapPoint",mapPoint.toString())

    val pointCoroutine = rememberCoroutineScope()
    var isFieldMarkersLoaded by remember { mutableStateOf(false) }

    //generate 5 marker on the map
    LaunchedEffect(mapPoint){
        if(mapPoint.isNotEmpty() && pestViewModel.markers.isEmpty()){
            pointCoroutine.launch{
                pestViewModel.getDecisionMarkers(mapPoint)
                isFieldMarkersLoaded = true
            }
        }

    }


    var markerIndex = -1

    
    if(mapPoint !=null&& pestViewModel.markers.isNotEmpty() ) {


            val cameraPosition = getCenterPoint(mapPoint)

            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(cameraPosition, 14f)
            }
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
            )
            {
                Polygon(
                    points = mapPoint,
                    fillColor = primaryColor,
                    strokeWidth = 3f
                )

                //get camera permission
                val context = LocalContext.current
                val permission = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestPermission()
                ) { granted: Boolean ->
                    if (granted) {
                        if (markerIndex != -1) {
                            if (pestViewModel.markers[markerIndex].processed) {
                                navController.navigate("identify_show_result/${fieldId}/${markerIndex}")
                            } else {
                                navController.navigate("identify_camera/${fieldId}/${markerIndex}")
                            }
                        }
                    } else {
                        Toast.makeText(context, "Permission Denied!", Toast.LENGTH_SHORT).show()
                    }
                }

                pestViewModel.markers.forEachIndexed { index, mapMarkInfo ->
                    // draw marker in the map
                    Marker(state = MarkerState(mapMarkInfo.point),
                        icon = BitmapDescriptorFactory.defaultMarker(if (mapMarkInfo.processed) BitmapDescriptorFactory.HUE_GREEN else BitmapDescriptorFactory.HUE_RED),
                        onClick = {
                            markerIndex = index
                            permission.launch(Manifest.permission.CAMERA)
                            true
                        })
                }

            }

            TopAppBar(
                title = {
                    Text(text = getField!!.name, fontSize = 20.sp,color= whiteColor)},
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate(Screen.IdentifyMain.route)
                    }) {
                        Icon(painter= painterResource(R.drawable.baseline_arrow_back_24), contentDescription = "back", tint= whiteColor)
                    }
                },
                backgroundColor = primaryColor
            )

            //the user able to view the result after upload all the images for each marker
            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Bottom) {
                if (pestViewModel.markers.filter { it.processed }.size == pestViewModel.markers.size) {
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp), contentAlignment = Alignment.Center) {
                        Button(onClick = {
                            navController.navigate("identify_final_result/${fieldId}")
                        }, colors = ButtonDefaults.buttonColors(backgroundColor = secondaryColor)) {
                            Text (text = stringResource(R.string.viewResult), color= whiteColor, fontSize = 16.sp)
                        }
                    }
                }


        }
    }

}