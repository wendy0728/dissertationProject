package com.example.farm.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.BottomNavigation
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.farm.R
import com.example.farm.view.*

/**
 * EncyclopediaMainScreen.kt
 * Written By: Jing Wen Ng
 *
 * This is the first page of the encyclopedia.
 * User can search a crop's name and it'll get more details about it after that.
 *
 *@param navController
 */

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EncyclopediaMainScreen(navController: NavController){

    var text by remember { mutableStateOf("") }

    val keyboard = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    //get the width of the screen
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    //color for navigation bar's icon and text
    var clickColor = primaryColor
    var unClickColor = greyColor


    TopAppBar(
        title = {
            Text(text= stringResource(R.string.encyclopedia), fontSize = 20.sp,color= whiteColor) },
        backgroundColor = primaryColor
    )


    Column(modifier = Modifier
        .fillMaxSize()
        .padding(top = 230.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text= stringResource(R.string.searchCrop),
            color = blackColor,
            fontSize = 36.sp,
            modifier= Modifier.padding(20.dp),
            fontWeight = FontWeight.Normal
        )
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            modifier = Modifier
                .width(screenWidth * 0.9f)
                .focusRequester(focusRequester),
            trailingIcon = {
                Icon(painter = painterResource(R.drawable.baseline_search_24),
                    contentDescription="search",
                    tint = primaryColor,
                    modifier = Modifier.clickable {
                        navController.navigate("crop_potential_disease/$text")
                    })
            },
            singleLine = true,
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboard?.hide()
                    focusManager.clearFocus()
                }
            ),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = primaryColor,
                unfocusedBorderColor = Color.Gray,
                cursorColor = primaryColor,
                focusedLabelColor = primaryColor
            )
        )

    }


    //navigation bar
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
                        tint = clickColor,
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
                        tint = unClickColor,
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
                    Text("Encyclopedia", fontSize = 10.sp, color = clickColor)
                    Text("Identify", fontSize = 10.sp, color = unClickColor,modifier = Modifier.padding(end=5.dp))
                    Text("Setting", fontSize = 10.sp, color = unClickColor)
                }

            }
        }
    }
}