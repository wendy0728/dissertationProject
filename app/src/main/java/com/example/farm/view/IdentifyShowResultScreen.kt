package com.example.farm.view

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.farm.R
import com.example.farm.viewmodel.PestViewModel
import com.example.farm.viewmodel.detection.PestClassResult
import com.example.farm.viewmodel.detection.findPestClassIndex
import com.example.farm.viewmodel.detection.pestClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalTime

/**
 * IdentifyShowResultScreen.kt
 * Written By: Jing Wen Ng
 *
 * The user can view the identify pests result, the user can edit or add more result as well
 * if they press submit, the result will be recorded
 * User are able to review their previous result after submit it, but not able to edit it.
 *
 * That is a function showing that if pests are detected on the image, a box can be drawn around them
 * this part of code is written by Luna Lou at https://github.com/PervasiveComputingGroup/FCIMCS_Mobile
 * The code from Luna Lou at https://github.com/PervasiveComputingGroup/FCIMCS_Mobile,
 * it shows that user can't review the result for a specific marker after submit the result.
 * However, this functional requirements have been done in this mobile application.
 *
 *@param navController
 *@param pestViewModel
 *@param markerIndex
 *@param fieldId
 */

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun IdentifyShowResultScreen(navController : NavController,
                            pestViewModel : PestViewModel,
                            markerIndex: String,
                            fieldId: String) {

    val pestCoroutine = rememberCoroutineScope()
    val context = LocalContext.current
    val localConfiguration = LocalConfiguration.current
    val scale = context.resources.displayMetrics.density
    val screenWidth = localConfiguration.screenWidthDp * scale + 0.5f
    var detectionClassResult :  MutableList<PestClassResult>? = mutableListOf()

    var loading by remember { mutableStateOf(true) }

    val numList = arrayListOf<Int>()
    for (i in 0..100) {
        numList.add(i)
    }


    val startTime = LocalTime.now()

    //get result
    LaunchedEffect(Unit) {
        pestCoroutine.launch(Dispatchers.IO) {
            if(!pestViewModel.markers[markerIndex.toInt()].processed) {
                pestViewModel.getDetectionResult(
                    context = context,
                    uri = pestViewModel.markers[markerIndex.toInt()].photoUri!!,
                    screenWidth = screenWidth.toInt()
                ) {
                    loading = false
                    //get the duration time between the user has submit the photos and get the identify results
                    val currentTime = LocalTime.now()
                    val differenceTime =  Duration.between(currentTime, startTime).abs()
                    val differenceTimeInSec = differenceTime.seconds
                    Log.i("differenceTimeInSec" ,differenceTimeInSec.toString())
                }
            }else{
                loading = false
            }
        }
    }


    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    if (!loading) {

        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {

            //show the image
            Image(
                painter = rememberAsyncImagePainter(pestViewModel.markers[markerIndex.toInt()].photoUri),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(screenHeight * 0.7f)
            )


            Column(modifier= Modifier
                .verticalScroll(rememberScrollState())
                .padding(top = 5.dp)) {

                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier=Modifier.fillMaxWidth(),){
                    Text(text = stringResource(R.string.editIncorrectInfo), fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }

                //able to edit the result
                Column() {

                    var markerProcessed = pestViewModel.markers[markerIndex.toInt()].processed
                    if(markerProcessed){
                        detectionClassResult= pestViewModel.markers[markerIndex.toInt()].detectionClassResult
                    }else{
                        detectionClassResult= pestViewModel.detectionClassResult
                    }

                    detectionClassResult?.forEach { pestClassResult ->

                        var choosenPests by remember { mutableStateOf(pestClass[pestClassResult.classIndex]) }
                        var choosenNumber by remember { mutableStateOf(pestClassResult.classNum.toString()) }
                        Row(modifier = Modifier.padding(top = 10.dp)) {}

                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Text(text = stringResource(R.string.pestName))
                            Text(text = stringResource(R.string.numberPests))
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {


                            Card(modifier = Modifier
                                .height(40.dp)
                                .width(120.dp)) {
                                val expanded = remember { mutableStateOf(false) }
                                TextButton(
                                    onClick = {
                                        expanded.value = true
                                    }
                                ) {
                                    Text(text = choosenPests, color = blackColor)
                                    Icon(
                                        painter = painterResource(R.drawable.baseline_keyboard_arrow_down_24),
                                        contentDescription = "DropDown",
                                        tint = blackColor
                                    )
                                }
                                DropdownMenu(
                                    modifier = Modifier.requiredSizeIn(maxHeight = 200.dp),
                                    expanded = expanded.value,
                                    onDismissRequest = { expanded.value = false }) {
                                    pestClass.forEachIndexed { _, i ->
                                        DropdownMenuItem(onClick = {
                                            choosenPests = i
                                            expanded.value = false
                                        }) {
                                            Text(
                                                text = i,
                                                color = blackColor
                                            )
                                        }
                                    }
                                }
                            }



                            Card(modifier = Modifier
                                .height(40.dp)
                                .width(120.dp)) {
                                val expanded = remember { mutableStateOf(false) }

                                TextButton(
                                    onClick = {
                                        expanded.value = true
                                    }
                                ) {
                                    Text(text = choosenNumber, color = blackColor)
                                    Icon(
                                        painter = painterResource(R.drawable.baseline_keyboard_arrow_down_24),
                                        contentDescription = "DropDown",
                                        tint = blackColor
                                    )
                                }
                                DropdownMenu(
                                    modifier = Modifier.requiredSizeIn(maxHeight = 200.dp),
                                    expanded = expanded.value,
                                    onDismissRequest = { expanded.value = false }) {
                                    numList.forEachIndexed { _, i ->
                                        DropdownMenuItem(onClick = {
                                            choosenNumber = i.toString()
                                            expanded.value = false
                                        }) {
                                            Text(
                                                text = i.toString(),
                                                color = blackColor
                                            )

                                        }
                                    }
                                }
                            }
                        }
                        pestClassResult.classNum = choosenNumber.toInt()
                        pestClassResult.classIndex = findPestClassIndex(choosenPests)
                    }


                    if(!markerProcessed) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp,bottom=20.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {

                            //can add more result
                            Button(
                                onClick = {
                                    pestViewModel.detectionClassResult.add(PestClassResult(0, 1))
                                },
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = secondaryColor,
                                ),
                            ) {
                                Text(
                                    text = stringResource(R.string.addResult),
                                    color = whiteColor
                                )
                            }

                            //back to the map and record down the result for the specific marker
                            Button(
                                onClick = {
                                    var detection = mutableStateListOf<PestClassResult>()
                                    detectionClassResult?.forEach { it ->
                                        detection.add(PestClassResult(it.classIndex, it.classNum))
                                    }
                                    pestViewModel.markers[markerIndex.toInt()].detectionClassResult =
                                        detection
                                    pestViewModel.finishDetection(markerIndex.toInt())
                                    navController.navigate("identify_map/${fieldId}")
                                },
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = secondaryColor
                                ),
                                modifier = Modifier
                                    .padding(end = 20.dp),
                                enabled = !loading
                            ) {
                                Text(
                                    text = if (loading) stringResource(R.string.loading) else stringResource(
                                        R.string.submitMarker
                                    ),
                                    color = whiteColor
                                )
                            }


                        }
                    }else{
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp,bottom=20.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            //back to the map
                            Button(
                                onClick = {
                                    var detection = mutableStateListOf<PestClassResult>()
                                    detectionClassResult?.forEach { it ->
                                        detection.add(PestClassResult(it.classIndex, it.classNum))
                                    }
                                    pestViewModel.markers[markerIndex.toInt()].detectionClassResult =
                                        detection
                                    pestViewModel.finishDetection(markerIndex.toInt())
                                    navController.navigate("identify_map/${fieldId}")
                                },
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = secondaryColor
                                ),
                                modifier = Modifier
                                    .padding(end = 20.dp),
                                enabled = !loading
                            ) {
                                Text(
                                    text = stringResource(R.string.backMap),
                                    color = whiteColor
                                )
                            }
                        }
                    }
                }
            }
        }

    }

    if (loading) {
        AnimatedVisibility(
            modifier = Modifier
                .fillMaxSize(),
            visible = loading,
            enter = slideInVertically(),
            exit = fadeOut()
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .background(MaterialTheme.colors.surface)
                    .wrapContentSize(),
                color = primaryColor,
                strokeWidth = 4.dp
            )
        }

    }else{
        //the code below are from Luna Lou's code at https://github.com/PervasiveComputingGroup/FCIMCS_Mobile
        Canvas(modifier = Modifier.fillMaxSize()) {
            pestViewModel.detectionResult.forEach {
                drawLine(
                    start = Offset(y = it.rect.top.toFloat(), x = it.rect.left.toFloat()),
                    end = Offset(y = it.rect.bottom.toFloat(), x = it.rect.left.toFloat()),
                    color = Color.Blue,
                    strokeWidth = 10.0f
                )

                drawLine(
                    start = Offset(y = it.rect.top.toFloat(), x = it.rect.left.toFloat()),
                    end = Offset(y = it.rect.top.toFloat(), x = it.rect.right.toFloat()),
                    color = Color.Blue,
                    strokeWidth = 10.0f
                )

                drawLine(
                    start = Offset(y = it.rect.bottom.toFloat(), x = it.rect.left.toFloat()),
                    end = Offset(y = it.rect.bottom.toFloat(), x = it.rect.right.toFloat()),
                    color = Color.Blue,
                    strokeWidth = 10.0f
                )

                drawLine(
                    start = Offset(y = it.rect.top.toFloat(), x = it.rect.right.toFloat()),
                    end = Offset(y = it.rect.bottom.toFloat(), x = it.rect.right.toFloat()),
                    color = Color.Blue,
                    strokeWidth = 10.0f
                )
            }

        }

    }


}










