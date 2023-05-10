package com.example.farm.view

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.farm.R
import com.example.farm.viewmodel.PestViewModel

/**
 * IdentifyShowImageScreen.kt
 * Written By: Jing Wen Ng
 *
 * The user able to view the picture that had been taken previously, they can choose
 * to take the photo again or get the identify pests result
 *
 *@param navController
 *@param pestViewModel
 *@param markerIndex
 *@param fieldId
 */


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun IdentifyShowImageScreen(navController : NavController,
                            pestViewModel : PestViewModel,
                            markerIndex: String,
                            fieldId: String) {


    Box(modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter){

            Image(
                painter = rememberAsyncImagePainter(pestViewModel.markers[markerIndex.toInt()].photoUri),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
            )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom=20.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.Bottom
                ) {

                    Column(modifier=Modifier.weight(1f)){}

                    //the user can choose to the retake the photo
                    Button(onClick = {
                        pestViewModel.markers[markerIndex.toInt()].photoUri=null
                        navController.navigate("identify_camera/${fieldId}/${markerIndex}")
                    }, colors = ButtonDefaults.buttonColors(backgroundColor = secondaryColor) ,modifier=Modifier.weight(3f)) {
                        Text(
                            text = stringResource(R.string.takeAgain),
                            color = whiteColor,
                            fontSize = 16.sp
                        )
                    }

                    Column(modifier=Modifier.weight(1f)){}

                    //press OK if the user satisfied with the result
                    Button(onClick = {
                        navController.navigate("identify_show_result/${fieldId}/${markerIndex}")
                    }, colors = ButtonDefaults.buttonColors(backgroundColor = secondaryColor) ,modifier=Modifier.weight(3f)) {
                        Text(
                            text = stringResource(R.string.ok),
                            color = whiteColor,
                            fontSize = 16.sp
                        )
                    }

                    Column(modifier=Modifier.weight(1f)){}


                }
        }


}










