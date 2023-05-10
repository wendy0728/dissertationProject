package com.example.farm.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.database.sqlite.SQLiteDatabase
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.core.app.ActivityCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.farm.MyAppCompatActivity
import com.example.farm.R
import com.example.farm.model.Field
import com.example.farm.navigation.MAIN_ROUTE
import com.example.farm.navigation.ROOT_ROUTE
import com.example.farm.navigation.mainNavGraph
import com.example.farm.viewmodel.detection.CAMERA_EXECUTOR
import com.example.farm.viewmodel.detection.CAMERA_DIRECTORY
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import org.mindrot.jbcrypt.BCrypt
import java.io.File
import java.io.IOException
import java.util.Date
import java.util.Locale
import java.util.concurrent.Executors

/**
 * MainActivity.kt
 * Written By: Jing Wen Ng
 *
 * initial some data to the database
 * get user current location and their current location's weather
 *
 * some of the code is written by Luna Lou at https://github.com/PervasiveComputingGroup/FCIMCS_Mobile,
 * which is getCameraOutputDirectory() function
 */


class MainActivity() : MyAppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        pestViewModel.loadDetectionModel(this.baseContext)

        var geocoder: Geocoder
        var city = ""
        var temp = 0.0
        var iconUrl = ""

        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            geocoder = Geocoder(this, Locale.getDefault())
            locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, object :
                LocationListener {

                override fun onLocationChanged(location: Location) {

                    val location =
                        locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                    val addresses =
                        geocoder.getFromLocation(location!!.latitude, location!!.longitude, 1)

                 //if user can't get the city name, it will get its postcode
                    if (addresses[0].locality == null) {
                        city = addresses[0].postalCode
                    } else {
                        city = addresses[0].locality
                    }

                    var latitude = location.latitude.toString()
                    var longitude = location.longitude.toString()

                    val apiKey = "842ae47b290e9133d5ca3710efeab2e7"

                    val url =
                        "https://api.openweathermap.org/data/2.5/weather?lat=$latitude&lon=$longitude&appid=$apiKey"

                    val client = OkHttpClient()
                    val request = Request.Builder()
                        .url(url)
                        .build()

                    client.newCall(request).enqueue(object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            Log.i("Weather", "Fail to get data: ${e.message}")
                        }

                        override fun onResponse(call: Call, response: Response) {
                            val jsonString = response.body?.string()
                            val jsonObject = JSONObject(jsonString)

                            temp = jsonObject.getJSONObject("main").getDouble("temp")
                            var icon = jsonObject.getJSONArray("weather").getJSONObject(0)
                            iconUrl =
                                "https://openweathermap.org/img/w/${icon.getString("icon")}.png"
                            fieldViewModel.weatherTemp = temp
                            fieldViewModel.weatherIconUrl = iconUrl
                            fieldViewModel.city = city
                        }

                    }
                    )

                }

                override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
            }, null)


        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
        }

        CAMERA_DIRECTORY = getCameraOutputDirectory()
        CAMERA_EXECUTOR = Executors.newSingleThreadExecutor()

        setContent {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = MAIN_ROUTE,
                route = ROOT_ROUTE
            ) {
                mainNavGraph(
                    navController = navController,
                    encyViewModel = encyViewModel,
                    encyPotentialDiseaseViewModel = encyPotentialDiseaseViewModel,
                    fieldViewModel = fieldViewModel,
                    coordinateViewModel = coordinateViewModel,
                    pestViewModel = pestViewModel,
                    resultViewModel = resultViewModel,
                    userViewModel = userViewModel
                )
            }
        }

        val field = ArrayList<Field>()
        //add some data to the database
        fieldViewModel.fields.observe(this) {
            if(it.isEmpty()){

                fieldViewModel.insert(
                    name = "Sam Field",
                    location = "Sheffield",
                    crop = "Apple"
                )

                fieldViewModel.insert(
                    name = "Tom Field",
                    location = "Liverpool",
                    crop = "Cabbage"
                )

                coordinateViewModel.insert(
                    long = -1.72494,
                    lat = 53.350564,
                    fieldId = 1
                )
                coordinateViewModel.insert(
                    long = -1.715751,
                    lat = 53.3539,
                    fieldId = 1
                )
                coordinateViewModel.insert(
                    long = -1.718,
                    lat = 53.31,
                    fieldId = 1
                )
                coordinateViewModel.insert(
                    long = -1.73,
                    lat = 53.3,
                    fieldId = 1
                )

                coordinateViewModel.insert(
                    long = -1.72494,
                    lat = 53.350564,
                    fieldId = 2
                )
                coordinateViewModel.insert(
                    long = -1.715751,
                    lat = 53.3539,
                    fieldId = 2
                )
                coordinateViewModel.insert(
                    long = -1.718,
                    lat = 53.31,
                    fieldId = 2
                )
                coordinateViewModel.insert(
                    long = -1.73,
                    lat = 53.3,
                    fieldId = 2
                )

                encyViewModel.insert(
                    name = "apple"
                )

                encyPotentialDiseaseViewModel.insert(
                    cropId = 1,
                    disease = "Apple Scab",
                    symptoms = "Small spots on the leaves, leaves turning dull and yellowing the leaves.",
                    cause = "It caused by the fungus.It spreads by airbrone spores and can survive the winter by remaining on fallen leaves.",
                    prevention = "Provide a good air circulation for apple tress. Make sure the environment of apple tress with low humidity. Other than that,remove the fallen leaves.",
                    recommendation = "Use fungicides.Fungicides are the most effective ways to cure apple scab. Other than that, should consider using resistant varieties such as Liberty, Freedom or Enterprise.Morover, provide a good air circulation for the apple trees."
                )


                encyPotentialDiseaseViewModel.insert(
                    cropId = 1,
                    disease = "Brown rot",
                    symptoms = "Brown spots on the apple.The apple will be soft and mushy.",
                    cause = "It caused by a fungus called Monilini fructicola",
                    prevention = "Firstly, apply fungicides to prevent brown rot. It is also important that should follow the label instructions and apply appropriate times on it. Other than that,improve the air circulation and make sure have enough sunlight.",
                    recommendation = "Weather monitoring. Monitor the weather conditions every day and make sure to take some actions when conditions are favorable for the disease development. Secondly, try to pevent overwatering. In addition, have a good sanitation. Remove all the infected fruit either from the tree or ground."
                )

                encyPotentialDiseaseViewModel.insert(
                    cropId = 1,
                    disease = "Powdery Mildew",
                    symptoms = "Yellowing or browning leaves and curling or twisting of leaves.",
                    cause = "It caused by by a fungal infection",
                    prevention = "Firstly, have enough of water and fertiliser. Other than that,apply fungicides. Use a fungicide that is effective against powdery mildew",
                    recommendation = "Weather monitoring. Monitor the weather conditions every day and make sure to take some actions when conditions are favorable for the disease development. Secondly, try to pevent overwatering. In addition, remove all the leaves that are affected."
                )

                pestViewModel.insert(
                    name = "pest",
                    prevention = "Maintain good sanitation. Make sure to clean up the fallen leaves regularly. In addition, try to avoid over-watering. It is because wet soil will attract pests. Moreover, inspect plants regularly, inspect plants regularly. Make sure all the infected plants are removed.",
                    recommendation = "Choose pest-resistant plants. Make sure to follow the instruction on the pest-resistant label. In addition, make sure to monitor the plan regularly. Keep the plants healthy and well-fed to help them to resist pets. Besides that, have crop rotation at least once every month. This can prevent pest build-up in the soil."
                )

                pestViewModel.insert(
                    name = "ant",
                    prevention = "Keep plants healthy.Provide enough water, sunlight, and nutrients for the plant. Apply insecticidal soap to control aphids",
                    recommendation = "If you found out  any of the branches with a heavy infestation of aphids, remove it off to prevent the infestation from spreading to others plant."
                )

                pestViewModel.insert(
                    name = "aphid",
                    prevention = "Keep plants healthy.Provide enough water, sunlight, and nutrients for the plant. Apply insecticidal soap to control aphids",
                    recommendation = "If you found out  any of the branches with a heavy infestation of aphids, remove it off to prevent the infestation from spreading to others plant."
                )

                pestViewModel.insert(
                    name = "beetle",
                    prevention = "Prepare some natural predators of bettles. For example, ladybugs and parasitic wasps to control beetle populations. Make sure the crops is rotated regularly to avoid beetles building up in the soil.",
                    recommendation = "Use chemical control. Apply chemical insecticides on the plants. Follow all the instructions on the label of the chemical insecticides."
                )

                pestViewModel.insert(
                    name = "bollworm",
                    prevention = "Keep plants healthy.Provide enough water, sunlight, and nutrients for the plant. Apply insecticidal soap to control aphids",
                    recommendation = "If you found out  any of the branches with a heavy infestation of aphids, remove it off to prevent the infestation from spreading to others plant."
                )

                pestViewModel.insert(
                    name = "caterpillar",
                    prevention = "Prepare some natural predators of caterpillar.",
                    recommendation = "Use chemical control. Apply chemical insecticides on the plants. Follow all the instructions on the label of the chemical insecticides."
                )

                pestViewModel.insert(
                    name = "fleas",
                    prevention = "Apply the organic insecticides on the affected organic. Cover the plants, grow the plan in greenhouse, indoors or cover them with a fine mesh to avoid it laying eggs",
                    recommendation = "Put on sticky traps around the plants the environment clean. Remove all the rotten plants."
                )

                pestViewModel.insert(
                    name = "fly",
                    prevention = "Apply the organic insecticides on the affected organic. Cover the plants, grow the plan in greenhouse, indoors or cover them with a fine mesh to avoid it laying eggs",
                    recommendation = "Put on sticky traps around the plants the environment clean. Remove all the rotten plants."
                )

                pestViewModel.insert(
                    name = "leaf miner",
                    prevention = "Apply the organic insecticides on the affected organic. Cover the plants, grow the plan in greenhouse, indoors or cover them with a fine mesh to avoid it laying eggs",
                    recommendation = "Put on sticky traps around the plants the environment clean. Remove all the rotten plants."
                )

                pestViewModel.insert(
                    name = "mit",
                    prevention = "First, keep the environment of the plants clean. Monitor the plant health regularly. Provide enough water, sunlight, and nutrients for the plant",
                    recommendation = "Use some insecticidal soap. It should use directly to the plant the make sure to follow the instruction on the label. Secondly, put on sticky traps around the plants."
                )

                pestViewModel.insert(
                    name = "snail",
                    prevention = "Firstly, handpicking the snails. Secondary, make sure the plant have enough sunlight cause snail always stay in dark and damp places even during the day time",
                    recommendation = "Use diatomaceous earth on the affected plant, it can damage the snail's body and cause it to dehydrate and die."
                )

                pestViewModel.insert(
                    name = "spider",
                    prevention = "First, keep the environment of the plants clean. Monitor the plant health regularly. Provide enough water, sunlight, and nutrients for the plant",
                    recommendation = "Use some insecticidal soap. It should use directly to the plant the make sure to follow the instruction on the label. Secondly, put on sticky traps around the plants."
                )

                pestViewModel.insert(
                    name = "slug",
                    prevention = "Handpicking the slug. Besides that ,make sure the plant have enough sunlight cause slug always stay in dark and damp places even during the day time",
                    recommendation = "Use diatomaceous earth on the affected plant, it can damage the slug's body and cause it to dehydrate and die."
                )



                val salt = BCrypt.gensalt()
                val hashedPassword = BCrypt.hashpw("password123Q", salt)
                userViewModel.insert(
                    email = "tom@gmail.com",
                    password = hashedPassword,
                    name = "Tom",
                    occupation = "farmer",
                    salt = salt
                )

                val currentDate = Date()
                var pestsId = ArrayList<Int>()
                pestsId.add(1)
                pestsId.add(2)
                resultViewModel.insert(
                    fieldId = 1,
                    updatedDate = currentDate,
                    pestDetected = pestsId,
                    updatedBy = "tom@gmail.com"
                )

                var pestsId2 = ArrayList<Int>()
                pestsId2.add(2)
                pestsId2.add(3)
                resultViewModel.insert(
                    fieldId = 1,
                    updatedDate = currentDate,
                    pestDetected = pestsId2,
                    updatedBy = "tom@gmail.com"
                )

                resultViewModel.insert(
                    fieldId = 2,
                    updatedDate = currentDate,
                    pestDetected = pestsId,
                    updatedBy = "tom@gmail.com"
                )

                resultViewModel.insert(
                    fieldId = 2,
                    updatedDate = currentDate,
                    pestDetected = pestsId2,
                    updatedBy = "tom@gmail.com"
                )

            }

        }

    }

    private fun getCameraOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists()) mediaDir else filesDir
    }

}