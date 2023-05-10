package com.example.farm.view

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSizeIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.BottomNavigation
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.farm.R
import com.example.farm.viewmodel.FieldViewModel

/**
 * IdentifyMainScreen.kt
 * Written By: Jing Wen Ng
 *
 * This page is allow user to choose a field to do the pests identification.
 *
 *@param navController
 *@param fieldViewModel
 */

@Composable
fun IdentifyMainScreen(
    navController: NavController,
    fieldViewModel: FieldViewModel,
){

    val fields by fieldViewModel.fields.observeAsState()
    var expand by remember { mutableStateOf(false) }

    var enable = false
    var choosenField by remember {mutableStateOf("")}
    var textTitle by remember {mutableStateOf("")}
    var icon: Painter
    var fieldId by remember { mutableStateOf(0) }

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val context = LocalContext.current

    if (choosenField.isBlank()){
        textTitle = stringResource(R.string.chooseField)
    }else{
        textTitle = choosenField
        enable = true
    }

    if (expand){
        icon = painterResource(R.drawable.baseline_keyboard_arrow_down_24)
    }else{
        icon = painterResource(R.drawable.baseline_keyboard_arrow_right_24)
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            var field_id = fieldId.toString()
            navController.navigate("identify_map/$field_id")
        } else {
            Toast.makeText(context, "Permission denied!", Toast.LENGTH_SHORT).show()
        }
    }
    TopAppBar(
        title = {
            Text(text= stringResource(R.string.identifyPests), fontSize = 20.sp,color= whiteColor) },
        backgroundColor = primaryColor
    )


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top=230.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
            Column(modifier = Modifier
                .padding(10.dp)) {
                    Text(text = stringResource(R.string.selectField), fontSize=36.sp)
            }

            Column() {
                Card(
                    modifier = Modifier
                        .width(screenWidth * 0.9f)
                        .wrapContentHeight()
                        .padding(10.dp),
                    shape = MaterialTheme.shapes.small,
                    elevation = 2.dp,
                ) {
                    TextButton(
                        modifier = Modifier.background(primaryColor),
                        onClick = {
                            expand = true
                        },
                    ) {
                        Text(
                            text = textTitle,
                            color = whiteColor,
                            fontSize = 14.sp)
                        Icon(
                            icon,
                            contentDescription = "icon drop down box",
                            tint = whiteColor
                        )
                    }

                    DropdownMenu(
                        expanded = expand,
                        onDismissRequest = { expand = false },
                        modifier = Modifier
                            .requiredSizeIn(maxHeight = 200.dp)
                            .width(screenWidth * 0.85f)
                            .wrapContentHeight()
                    ) {
                        fields?.forEach {i->
                            DropdownMenuItem(onClick = {
                                choosenField = i.name
                                expand = false
                                fieldId = i.id
                            }) {
                                Text(
                                    text = i.name,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }
            }


            // the button will only be enable if onf of the field has been selected
            Column(modifier = Modifier.padding(top = 5.dp)) {
                    Button(
                        enabled = enable,
                        onClick = {
                            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                            enable = false
                        },
                        colors = ButtonDefaults.buttonColors(backgroundColor = secondaryColor)
                    ) {
                        Text(
                            stringResource(R.string.start_detect),
                            color = whiteColor,
                            fontSize = 14.sp
                        )
                    }
            }


    }

    //navigation bar

    var clickColor = primaryColor
    var unClickColor = greyColor

    Column(modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom) {
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
                        tint = clickColor,
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
                    Text("Encyclopedia", fontSize = 10.sp, color = unClickColor)
                    Text("Identify", fontSize = 10.sp, color = clickColor, modifier = Modifier.padding(end=5.dp))
                    Text("Setting", fontSize = 10.sp, color = unClickColor)
                }

            }
        }
    }

}