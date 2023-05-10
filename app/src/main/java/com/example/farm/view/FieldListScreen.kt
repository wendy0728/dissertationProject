package com.example.farm.view

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomNavigation
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.farm.R
import com.example.farm.viewmodel.FieldViewModel
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import java.time.LocalDate
import java.util.Locale

/**
 * FieldListScreen.kt
 * Written By: Jing Wen Ng
 *
 * In this page user able to view a list of field and the temperature, weather and date of current location.
 *
 *
 *@param navController
 *@param fieldViewModel
 */

@Composable
fun FieldListScreen(
    navController: NavController,
    fieldViewModel: FieldViewModel
) {

    var temp = fieldViewModel.weatherTemp
    var iconUrl = fieldViewModel.weatherIconUrl
    var city = fieldViewModel.city


    //get all the fields
    val fields by fieldViewModel.fields.observeAsState()

    //get the height and width of the screen
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    var textColor = blackColor


    var loading by remember { mutableStateOf(true) }

    TopAppBar(
        title = {
            Text(text=stringResource(R.string.yourField), fontSize = 20.sp,color= whiteColor) },
        backgroundColor = primaryColor
    )

    if(temp!=0.0  || iconUrl.isNotEmpty()){
        loading = false
    }


    Column(modifier = Modifier
        .padding(start=20.dp,bottom=60.dp,end=20.dp,top=70.dp)
        .fillMaxWidth(0.9f)
        .verticalScroll(rememberScrollState())) {

        //weather text
        Column(modifier = Modifier.padding(bottom = 10.dp)){
            Text(
                text = stringResource(R.string.weather),
                color = textColor,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }





        //weather card
        Card(modifier = Modifier
            .fillMaxWidth()
            .height(screenHeight / 7)
            .padding(bottom=10.dp)
            .drawShadows(
                color = secondaryColor,
                cornersRadius = 4.dp,
                shadowBlurRadius = 3.dp,
                offsetX = 2.dp,
                offsetY = 2.dp
            ),
            shape = MaterialTheme.shapes.medium,
            elevation = 2.dp,
            border = BorderStroke(0.2.dp, secondaryColor),) {

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
                        color = secondaryColor,
                        strokeWidth = 2.dp
                    )
                }}else {


                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(5.dp),
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        Column(
                            modifier = Modifier
                                .fillMaxHeight()
                                .padding(start = 10.dp),
                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Row() {
                                Text(
                                    text = city,
                                    modifier = Modifier.padding(bottom = 5.dp),
                                    color = textColor,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 12.sp
                                )
                                Icon(
                                    painter = painterResource(R.drawable.baseline_location_on_24),
                                    tint = blackColor,
                                    modifier = Modifier
                                        .size(16.dp)
                                        .padding(start=5.dp,top=2.dp),
                                    contentDescription = "location"
                                )
                            }
                            Text(
                                text = LocalDate.now().dayOfMonth.toString() + " " +
                                        shortFormMonth(LocalDate.now().month.name),
                                color = textColor,
                                fontWeight = FontWeight.Normal,
                                fontSize = 14.sp
                            )
                            Row() {
                                Text(
                                    //change to celsius
                                    text = (temp - 273.15).toInt().toString(),
                                    color = textColor,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 30.sp
                                )
                                Text(
                                    text = "°C",
                                    modifier = Modifier.padding(top = 8.dp),
                                    color = textColor,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp
                                )
                            }
                        }

                    }


                    Box(modifier = Modifier.weight(1f)) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            Image(
                                painter = rememberImagePainter(iconUrl),
                                contentDescription = "weather_icon",
                                modifier = Modifier.size(180.dp)
                            )
                        }
                    }


                }
            }


        }


        //field title
        Box(
            contentAlignment = Alignment.CenterStart,
            modifier= Modifier.padding(top=10.dp, bottom=10.dp)
        ) {
            Text(
                text = stringResource(R.string.fields),
                color = MaterialTheme.colors.onSurface,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            )

        }


        // show field list
        fields?.forEach {
            val permissionLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    var field_id = it.id
                    navController.navigate("result_list/$field_id")
                } else {
                    Toast.makeText(context, "Permission denied!", Toast.LENGTH_SHORT).show()
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Card(
                    elevation = 2.dp,
                    border = BorderStroke(0.2.dp, primaryColor),
                    modifier = Modifier
                        .width(screenWidth * 0.9f)
                        .padding(top = 5.dp,bottom=10.dp)
                        .drawShadows(
                            color = primaryColor,
                            cornersRadius = 4.dp,
                            shadowBlurRadius = 3.dp,
                            offsetX = 2.dp,
                            offsetY = 2.dp
                        )
                        .clickable {
                            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                        }) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = it.name,
                            color = blackColor,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = stringResource(R.string.location) + ": " + it.location,
                            color = blackColor,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal
                        )

                        Text(
                            text = stringResource(R.string.crop) + ": " + it.crop,
                            color = blackColor,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal
                        )

                    }

                }

            }


        }

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
                        tint = clickColor,
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
                        .padding(start = 30.dp, end = 30.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Home", fontSize = 10.sp, color = clickColor)
                    Text("Encyclopedia", fontSize = 10.sp, color = unClickColor)
                    Text(
                        "Identify",
                        fontSize = 10.sp,
                        color = unClickColor,
                        modifier = Modifier.padding(end = 5.dp)
                    )
                    Text("Setting", fontSize = 10.sp, color = unClickColor)
                }

            }
        }
    }
}

//draw shadow for the card
@Composable
fun Modifier.drawShadows(
    color: Color = Color.Black,
    cornersRadius: Dp = 0.dp,
    shadowBlurRadius: Dp = 0.dp,
    offsetY: Dp = 0.dp,
    offsetX: Dp = 0.dp
) = drawBehind {
    drawIntoCanvas {
        val paint = Paint()
        val frameworkPaint = paint.asFrameworkPaint()
        frameworkPaint.color =  color.copy(0f).toArgb()
        frameworkPaint.setShadowLayer(
            shadowBlurRadius.toPx(),
            offsetX.toPx(),
            offsetY.toPx(),
            color.copy(1f).toArgb()
        )
        it.drawRoundRect(
            0f,
            0f,
            this.size.width,
            this.size.height,
            cornersRadius.toPx(),
            cornersRadius.toPx(),
            paint
        )
    }
}


// represent the month in short form
fun shortFormMonth(month: String): String {
    return when (month.toLowerCase()) {
        "january" -> "Jan"
        "february" -> "Feb"
        "march" -> "Mar"
        "april" -> "Apr"
        "may" -> "May"
        "june" -> "Jun"
        "july" -> "Jul"
        "august" -> "Aug"
        "september" -> "Sep"
        "october" -> "Oct"
        "november" -> "Nov"
        "december" -> "Dec"
        else -> throw IllegalArgumentException("Invalid month: $month")
    }
}


