package com.example.farm.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.farm.R
import com.example.farm.viewmodel.UserViewModel

/**
 * ProfileDetailsScreen.kt
 * Written By: Jing Wen Ng
 *
 * User able to view her/his profile details.
 *
 *
 *@param navController
 *@param userViewModel
 */

@Composable
fun ProfileDetailsScreen(navController: NavController
                         ,userViewModel: UserViewModel){

    var userEmail = userViewModel.getUser

    //get user details
    val users by userViewModel.users.observeAsState()
    var userDetails = users?.firstOrNull(){it.email==userEmail}

    //get current screen width
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    TopAppBar(
        title = {
            Text(text = stringResource(R.string.profile),fontSize = 20.sp,color= whiteColor)},
        navigationIcon = {
            IconButton(onClick = {
                navController.navigate("setting")
            }) {
                Icon(painter= painterResource(R.drawable.baseline_arrow_back_24), contentDescription = "back", tint= whiteColor)
            }
        },
        backgroundColor = primaryColor
    )


    Column(modifier = Modifier
        .width(screenWidth*0.9f)
        .padding(top=80.dp, start=30.dp)){

        Column() {
            Text(
                text = stringResource(R.string.name),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Row(modifier=Modifier.padding(top=10.dp,bottom= 10.dp) ) {
                Icon(
                    painterResource(R.drawable.baseline_person_24),
                    contentDescription = "profile",
                    tint = secondaryColor,
                    modifier = Modifier.size(30.dp)
                )
                Text(
                    text = userDetails!!.name,
                    modifier = Modifier.padding(vertical = 5.dp,horizontal=20.dp),
                    color = blackColor,
                    fontSize = 16.sp
                )
            }

            Divider(modifier = Modifier.fillMaxWidth(),
                color = blackColor.copy(0.1f), thickness = 1.dp)
        }

        Column(modifier = Modifier.padding(top=10.dp)) {
            Text(
                text = stringResource(R.string.email),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Row(modifier=Modifier.padding(top=10.dp,bottom= 10.dp) ) {
                Icon(
                    painterResource(R.drawable.baseline_email_24),
                    contentDescription = "email",
                    tint = secondaryColor,
                    modifier = Modifier
                        .size(30.dp)
                )
                Text(
                    text = userDetails!!.email,
                    modifier = Modifier.padding(vertical = 5.dp,horizontal=20.dp),
                    color = blackColor,
                    fontSize = 16.sp
                )
            }

            Divider(modifier = Modifier.fillMaxWidth(),
                color = blackColor.copy(0.1f), thickness = 1.dp)
        }

        Column(modifier = Modifier.padding(top=10.dp)) {
            Text(
                text = stringResource(R.string.occupation),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Row(modifier=Modifier.padding(top=10.dp,bottom= 10.dp) ) {
                Icon(
                    painterResource(R.drawable.baseline_card_travel_24),
                    contentDescription = "occupation",
                    tint = secondaryColor,
                    modifier = Modifier
                        .size(30.dp)
                )
                Text(
                    text = userDetails!!.occupation,
                    modifier = Modifier.padding(vertical = 5.dp,horizontal=20.dp),
                    color = blackColor,
                    fontSize = 16.sp
                )
            }
            Divider(modifier = Modifier.fillMaxWidth(),
                color = blackColor.copy(0.1f), thickness = 1.dp)
        }

    }

}