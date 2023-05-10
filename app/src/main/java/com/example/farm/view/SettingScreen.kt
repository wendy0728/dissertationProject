package com.example.farm.view

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.ViewGroup
import android.widget.FrameLayout
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.BottomNavigation
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.example.farm.R
import com.example.farm.navigation.Screen
import com.example.farm.viewmodel.UserViewModel
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem


/**
 * SettingScreen.kt
 * Written By: Jing Wen Ng
 *
 * This is a setting page, user able to the log out, view the video for guidance to use this app and view their profile through this page.
 *
 *
 *@param navController
 *@param userViewModel
 */

@Composable
fun SettingScreen(navController : NavController,
                  userViewModel : UserViewModel
){

    var userEmail = userViewModel.getUser

    val users by userViewModel.users.observeAsState()
    var userDetails = users?.firstOrNull(){it.email==userEmail}


    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp


    var playing by remember { mutableStateOf(true) }

    TopAppBar(
        title = {
            Text(text=stringResource(R.string.setting), fontSize = 20.sp,color= whiteColor) },
        backgroundColor = primaryColor
    )

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 55.dp)){
        Row(modifier= Modifier.padding(20.dp)){
            Icon(
                painter = painterResource(R.drawable.baseline_person_pin_24),
                contentDescription = "person icon",
                modifier = Modifier
                    .padding(start = 20.dp)
                    .size(80.dp),
                tint = primaryColor
            )
            Text(text= userDetails?.name.toString(),modifier=Modifier.padding(start=30.dp, top=20.dp),fontSize=28.sp)
        }

        //view profile
        Row(
            modifier = Modifier
                .wrapContentHeight()
                .padding(20.dp)
                .clickable {
                    navController.navigate(Screen.Profile.route)
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painterResource(R.drawable.baseline_person_24),
                contentDescription = "profile",
                tint = secondaryColor,
                modifier = Modifier.size(30.dp)
            )
            Text(
                text = stringResource(R.string.viewProfile),
                modifier = Modifier.padding(start = 20.dp),
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp
            )
        }

        Divider(modifier = Modifier.fillMaxWidth(),
            color = blackColor.copy(0.1f), thickness = 1.dp)

        //video
        Row(
            modifier = Modifier
                .wrapContentHeight()
                .padding(start = 20.dp, end = 20.dp, top = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painterResource(R.drawable.baseline_videocam_24),
                contentDescription = "video",
                tint = secondaryColor,
                modifier = Modifier.size(30.dp)
            )
            Text(
                text = stringResource(R.string.guide),
                modifier = Modifier.padding(start = 20.dp),
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp
            )
        }

        val context = LocalContext.current
        val intent = remember { Intent(Intent.ACTION_VIEW, Uri.parse("https://drive.google.com/file/d/15LpfysnkDBJMOlRk21pzctmyV-Wb0gVl/view?usp=sharing")) }

        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 70.dp, bottom = 15.dp)) {
            Button(
                onClick = { context.startActivity(intent) },
                colors = ButtonDefaults.buttonColors(backgroundColor = secondaryColor)
            ) {
                Text(text = stringResource(R.string.viewNow),color= whiteColor)
            }
        }

        Divider(modifier = Modifier.fillMaxWidth(),
            color = blackColor.copy(0.1f), thickness = 1.dp)

        //log out
        Row(
            modifier = Modifier
                .wrapContentHeight()
                .padding(20.dp)
                .clickable {
                    navController.navigate(Screen.Login.route)
                    userViewModel.getUser = ""
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painterResource(R.drawable.baseline_logout_24),
                contentDescription = "log out",
                tint = secondaryColor,
                modifier = Modifier.size(30.dp)
            )
            Text(
                text = stringResource(R.string.logOut),
                modifier = Modifier.padding(start = 20.dp),
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp
            )
        }

        Divider(modifier = Modifier.fillMaxWidth(),
            color = blackColor.copy(0.1f), thickness = 1.dp)



    }


    //navigation bar
    var clickColor = primaryColor
    var unClickColor = greyColor

    Column(modifier = Modifier.fillMaxSize(),verticalArrangement = Arrangement.Bottom) {
        BottomNavigation(backgroundColor = lightBlueColor) {
            Column() {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(start = 30.dp, top = 10.dp, end = 35.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Icon(
                        painterResource(R.drawable.baseline_home_24),
                        contentDescription = "Home",
                        tint = unClickColor,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {
                                navController.navigate("field_list_screen")
                            })
                    Icon(
                        painterResource(R.drawable.baseline_library_books_24),
                        contentDescription = "Encyclopedia",
                        tint = unClickColor,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {
                                navController.navigate("encyclopedia_main")
                            })
                    Icon(
                        painterResource(R.drawable.baseline_search_24),
                        contentDescription = "Identify",
                        tint = unClickColor,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {
                                navController.navigate("identify_main")
                            })
                    Icon(
                        painterResource(R.drawable.baseline_settings_24),
                        contentDescription = "Setting",
                        tint = clickColor,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {
                                navController.navigate("setting")
                            })
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(start = 30.dp, end = 30.dp)
                    , horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Home", fontSize = 10.sp, color = unClickColor)
                    Text("Encyclopedia", fontSize = 10.sp, color = unClickColor)
                    Text("Identify", fontSize = 10.sp, color = unClickColor, modifier = Modifier.padding(end=5.dp))
                    Text("Setting", fontSize = 10.sp, color = clickColor)
                }

            }
        }
    }





}