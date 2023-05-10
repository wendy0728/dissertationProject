package com.example.farm.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.farm.R
import com.example.farm.viewmodel.EncyPotentialDiseaseViewModel

/**
 * EncyPotentialDiseaseDetailsScreen.kt
 * Written By: Jing Wen Ng
 *
 * In the previous page,displaying a list of potential diseases.
 * In this page, user can view more details about the potential disease.
 *
 *
 *@param navController
 *@param encyPotentialDiseaseViewModel
 *@param cropDetailsId The id of the crop details
 *@param cropName
 */

@Composable
fun EncyPotentialDiseaseDetailsScreen(navController: NavController, encyPotentialDiseaseViewModel : EncyPotentialDiseaseViewModel, cropDetailsId: String, cropName:String ){

    //get the details of the potential disease through [cropDetailsId]
    val allDiseaseDetail by encyPotentialDiseaseViewModel.allDetails.observeAsState()
    var cropDiseaseDetail = allDiseaseDetail?.firstOrNull(){it.id==cropDetailsId.toInt()}

    TopAppBar(
        title = {
            Text(text = cropDiseaseDetail!!.disease, fontSize = 20.sp, color= whiteColor) },
        navigationIcon = {
            IconButton(onClick = {
                navController.navigate("crop_potential_disease/${cropName}")
            }) {
                Icon(painter= painterResource(R.drawable.baseline_arrow_back_24), contentDescription = "back", tint= whiteColor)
            }
        },
        backgroundColor = primaryColor
    )

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(top=60.dp)
        .verticalScroll(rememberScrollState())
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(start = 24.dp, end = 20.dp, top = 20.dp, bottom = 10.dp),
            shape = MaterialTheme.shapes.small,
            elevation = 5.dp,
        )
        {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(start = 10.dp, end = 20.dp, top = 10.dp, bottom = 10.dp)
            ) {
                Text(text = stringResource(R.string.potentialDisease),
                    modifier = Modifier.padding(0.dp, 2.dp),
                    color = blackColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = cropDiseaseDetail!!.disease,
                    color = blackColor,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp
                )
            }

        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(start = 24.dp, end = 20.dp, top = 10.dp, bottom = 10.dp),
            shape = MaterialTheme.shapes.small,
            elevation = 5.dp,
        )
        {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(start = 10.dp, end = 20.dp, top = 10.dp, bottom = 10.dp)
            ) {
                Text(
                    text = stringResource(R.string.symptoms),
                    modifier = Modifier.padding(0.dp, 2.dp),
                    color = blackColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = cropDiseaseDetail!!.symptoms,
                    color = blackColor,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp
                )
            }

        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(start = 24.dp, end = 20.dp, top = 10.dp, bottom = 10.dp),
            shape = MaterialTheme.shapes.small,
            elevation = 5.dp,
        )
        {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(start = 10.dp, end = 20.dp, top = 10.dp, bottom = 10.dp)
            ) {
                Text(
                    text = stringResource(R.string.cause),
                    modifier = Modifier.padding(0.dp, 2.dp),
                    color = blackColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = cropDiseaseDetail!!.cause,
                    color = blackColor,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp
                )
            }

        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(start = 24.dp, end = 20.dp, top = 10.dp, bottom = 10.dp),
            shape = MaterialTheme.shapes.small,
            elevation = 5.dp,
        )
        {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(start = 10.dp, end = 20.dp, top = 10.dp, bottom = 10.dp)
            ) {
                Text(
                    text = stringResource(R.string.prevention),
                    modifier = Modifier.padding(0.dp, 2.dp),
                    color = blackColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = cropDiseaseDetail!!.prevention,
                    color = blackColor,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp
                )
            }

        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(start = 24.dp, end = 20.dp, top = 10.dp, bottom = 10.dp),
            shape = MaterialTheme.shapes.small,
            elevation = 5.dp,
        )
        {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(start = 10.dp, end = 20.dp, top = 10.dp, bottom = 10.dp)
            ) {
                Text(
                    text = stringResource(R.string.recommendation),
                    modifier = Modifier.padding(0.dp, 2.dp),
                    color = blackColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = cropDiseaseDetail!!.recommendation,
                    color = blackColor,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp
                )
            }

        }
    }

}