package com.example.farm.view

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.farm.R
import com.example.farm.viewmodel.EncyPotentialDiseaseViewModel
import com.example.farm.viewmodel.EncyViewModel

/**
 * EncyCropPotentialDiseaseScreen.kt
 * Written By: Jing Wen Ng
 *
 * In the previous page, user will type a crop name and click search.
 * In this page it will display a list of potential diseases that are associated with the crop that the user searching for.
 * For example, if the user are searching for apple. In this page, user are able to review a list of apple potential diseases.
 * However, if the can't find the [crop_name] in database, it will display as no data.
 * User can click on the card to view more details about the potential disease.
 *
 *@param navController
 *@param encyViewModel
 *@param encyPotentialDiseaseViewModel
 *@param crop_name The crop that user is searching for
 */


@Composable
fun EncyCropPotentialDiseaseScreen(navController: NavController, encyViewModel: EncyViewModel, encyPotentialDiseaseViewModel: EncyPotentialDiseaseViewModel, crop_name:String) {


    //search whether the [crop_name] matches with the
    val allCrops by encyViewModel.crops.observeAsState()
    Log.i("allCrops",allCrops.toString())
    val crop = allCrops?.firstOrNull() { it.name == crop_name }

    //get all the [crop_name] potential disease that related to the
    val encyDetails by encyPotentialDiseaseViewModel.allDetails.observeAsState()
    Log.i("allDiseaseDetail",crop?.id.toString())
    Log.i("allDiseaseDetail",encyDetails.toString())
    val cropDetails = encyDetails?.filter { it.cropId == crop?.id }

    //get the width of the screen
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    TopAppBar(
        title = {
            Text(text = crop_name, fontSize = 20.sp, color = whiteColor)
        },
        navigationIcon = {
            IconButton(onClick = {
                navController.navigate("encyclopedia_main")
            }) {
                Icon(
                    painter = painterResource(R.drawable.baseline_arrow_back_24),
                    contentDescription = "back",
                    tint = whiteColor
                )
            }
        },
        backgroundColor = primaryColor
    )


    if (crop == null) {
        dataNotFound()
    } else {
        Column(
            modifier = Modifier
                .padding(top = 60.dp)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            cropDetails?.forEach {
                    Column(
                        modifier = Modifier.clickable {
                            var id = it.id.toString()
                            navController.navigate("potential_disease_details/$crop_name/$id")
                        }
                    ) {
                        Text(
                            text = it.disease,
                            modifier = Modifier.padding(10.dp),
                            fontSize = 16.sp
                        )
                    }
                Divider(modifier = Modifier.fillMaxWidth(),
                    color = blackColor.copy(0.1f), thickness = 1.dp)
            }
        }

    }
}

@Composable
fun dataNotFound(){

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally){
        Box(modifier = Modifier
            .wrapContentHeight()
            .background(Color.Black.copy(0.7f))) {
            Text(stringResource(R.string.noData),fontSize = 20.sp,color = whiteColor,
                modifier = Modifier
                    .padding(10.dp)
            )
        }
    }

}