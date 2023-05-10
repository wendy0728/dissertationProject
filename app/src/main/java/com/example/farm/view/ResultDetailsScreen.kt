package com.example.farm.view

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.farm.R
import com.example.farm.model.Pest
import com.example.farm.viewmodel.PestViewModel
import com.example.farm.viewmodel.ResultViewModel

/**
 * ResultDetailsScreen.kt
 * Written By: Jing Wen Ng
 *
 * This page allow user to view the previous detection result's details.
 *
 *
 *@param navController
 *@param resultViewModel
 *@param pestViewModel
 *@param resultId
 *@param fieldId
 */

@Composable
fun ResultDetailsScreen(navController : NavController,
                        resultViewModel : ResultViewModel,
                        pestViewModel:PestViewModel,
                        resultId :String,
                        fieldId: String){


    val results by resultViewModel.results.observeAsState()
    val result = results?.firstOrNull() {it.id==resultId.toInt()}

    val pests by pestViewModel.allPests.observeAsState()

    var allPestsId = result?.pestDetected

    var allDetectedPest = ArrayList<Pest>()

    if (allPestsId != null) {
        for (pestId in allPestsId) {
            for(pest in pests!!){
                Log.i("allDetectedPest",pestId.toString())
                Log.i("allDetectedPests",pest.id.toString())
                if(pestId==pest.id){
                    allDetectedPest.add(pest)
                }
            }
        }
    }

    TopAppBar(
        title = {
            Text(text = stringResource(R.string.result),fontSize = 20.sp,color= whiteColor)},
        navigationIcon = {
            IconButton(onClick = {
                navController.navigate("result_list/${fieldId}")
            }) {
                Icon(painter= painterResource(R.drawable.baseline_arrow_back_24), contentDescription = "back", tint= whiteColor)
            }
        },
        backgroundColor = primaryColor
    )


    Column(modifier = Modifier
        .padding(top=55.dp)
        .verticalScroll(rememberScrollState())) {

        allDetectedPest?.forEach {

            Card(
                modifier = Modifier
                    .padding(15.dp)
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
                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .background(MaterialTheme.colors.surface)
                        .padding(start = 20.dp, top = 10.dp, bottom = 10.dp),
                        horizontalAlignment = Alignment.Start) {
                        Text(text = stringResource(R.string.prevention), color = secondaryColor)
                        Text(text = it.prevention, modifier = Modifier.padding(top = 5.dp,end=5.dp),color = blackColor)
                    }
                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .background(MaterialTheme.colors.surface)
                        .padding(start = 20.dp, top = 10.dp, bottom = 10.dp),
                        horizontalAlignment = Alignment.Start) {
                        Text(text = stringResource(R.string.recommendation), color = secondaryColor)
                        Text(text = it.recommendation, modifier = Modifier.padding(top = 5.dp,end=5.dp),color = blackColor)
                    }
                }

            }
        }
    }

}