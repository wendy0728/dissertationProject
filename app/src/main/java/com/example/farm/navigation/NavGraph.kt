package com.example.farm.navigation

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.navigation.*
import androidx.navigation.compose.composable
import androidx.navigation.NavGraphBuilder
import com.example.farm.view.*
import com.example.farm.viewmodel.CoordinateViewModel
import com.example.farm.viewmodel.EncyPotentialDiseaseViewModel
import com.example.farm.viewmodel.EncyViewModel
import com.example.farm.viewmodel.FieldViewModel
import com.example.farm.viewmodel.PestViewModel
import com.example.farm.viewmodel.ResultViewModel
import com.example.farm.viewmodel.UserViewModel

/**
 * NavGraph.kt
 * Written By: Jing Wen Ng
 *
 * This file contains all the information about the destinations and actions.
 * This file helps users able to navigate from one page to another.
 *
 */

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.mainNavGraph(
    navController: NavController,
    encyViewModel: EncyViewModel,
    encyPotentialDiseaseViewModel: EncyPotentialDiseaseViewModel,
    fieldViewModel: FieldViewModel,
    coordinateViewModel: CoordinateViewModel,
    pestViewModel: PestViewModel,
    resultViewModel: ResultViewModel,
    userViewModel: UserViewModel

){
    //start destination is Login Screen
    navigation(
        startDestination = Screen.Login.route,
        route = MAIN_ROUTE
    ){

        //list of field and display weather page
        composable(Screen.FieldList.route){
            FieldListScreen(
                navController =navController,
                fieldViewModel= fieldViewModel
            )
        }

        //showing field's previous result page
        composable(Screen.ResultList.route, arguments = listOf(navArgument("field_id"){
            type = NavType.StringType
            defaultValue = "no field id"
            nullable = false
        })){
            val fieldId = it.arguments?.getString("field_id")?: ""
            ResultListScreen(
                navController = navController,
                coordinateViewModel = coordinateViewModel,
                fieldViewModel = fieldViewModel,
                resultViewModel = resultViewModel,
                pestViewModel = pestViewModel,
                fieldId = fieldId
            )
        }

        //showing result's detail page
        composable(Screen.ResultDetails.route, arguments = listOf(
            navArgument("result_id"){
            type = NavType.StringType
            defaultValue = "no result id"
            nullable = false },
            navArgument("field_id"){
                type = NavType.StringType
                defaultValue = "no field id"
                nullable = false
            }
        )){
            val resultId = it.arguments?.getString("result_id")?: ""
            val fieldId = it.arguments?.getString("field_id")?: ""
            ResultDetailsScreen(
                navController = navController,
                resultViewModel = resultViewModel,
                pestViewModel = pestViewModel,
                resultId = resultId,
                fieldId = fieldId
            )
        }


        //encyclopedia main page
        composable(Screen.EncyclopediaMain.route) {
            EncyclopediaMainScreen(
                navController =navController
            )
        }


        //crop's potential disease list for encyclopedia
        composable(
            Screen.CropPotentialDisease.route,
            arguments = listOf(navArgument("crop_name") {
                type = NavType.StringType
                defaultValue = "No data"
                nullable = false
            })
        ) {
            EncyCropPotentialDiseaseScreen(
                navController = navController,
                encyViewModel = encyViewModel,
                encyPotentialDiseaseViewModel = encyPotentialDiseaseViewModel,
                crop_name = it.arguments?.getString("crop_name") ?: "")
        }

        //crop's potential disease details for encyclopedia
        composable(
            Screen.EncyPotentialDiseaseDetails.route,
            arguments = listOf(navArgument("id") {
                type = NavType.StringType
                defaultValue = "Id is empty"
                nullable = false
            },
            navArgument("crop_name") {
                type = NavType.StringType
                defaultValue = "No data"
                nullable = false
            })
        ) {
            EncyPotentialDiseaseDetailsScreen(
                navController = navController,
                encyPotentialDiseaseViewModel = encyPotentialDiseaseViewModel,
                cropDetailsId = it.arguments?.getString("id") ?: "",
                cropName = it.arguments?.getString("crop_name") ?: ""
            )
        }

        //identify pests main page
        composable(Screen.IdentifyMain.route) {
            IdentifyMainScreen(
                navController =navController,
                fieldViewModel= fieldViewModel
            )
        }


        //map page for identify pests
        composable(
            Screen.IdentifyMap.route,
            arguments = listOf(navArgument("field_id") {
                type = NavType.StringType
                defaultValue = "no field id"
                nullable = false
            })
        ) {
            IdentifyMapScreen(
                navController = navController,
                coordinateViewModel = coordinateViewModel,
                fieldViewModel = fieldViewModel,
                pestViewModel = pestViewModel,
                fieldId = it.arguments?.getString("field_id") ?: "")
        }

        //camera page when identifying pests
        composable(
            Screen.IdentifyCamera.route,
            arguments = listOf(
                navArgument("marker_index") {
                type = NavType.StringType
                defaultValue = "no marker index"
                nullable = false
                },
                navArgument("field_id") {
                    type = NavType.StringType
                    defaultValue = "no field id"
                    nullable = false
                })
        ) {
            IdentifyCameraScreen(
                navController = navController,
                pestViewModel = pestViewModel,
                markerIndex = it.arguments?.getString("marker_index") ?: "",
                fieldId = it.arguments?.getString("field_id") ?: "")
        }

        //display the image after the detection page
        composable(
            Screen.IdentifyShowImage.route,
            arguments = listOf(
                navArgument("marker_index") {
                    type = NavType.StringType
                    defaultValue = "no marker index"
                    nullable = false
                },
                navArgument("field_id") {
                    type = NavType.StringType
                    defaultValue = "no field id"
                    nullable = false
                })
        ) {
            IdentifyShowImageScreen(
                navController = navController,
                pestViewModel = pestViewModel,
                markerIndex = it.arguments?.getString("marker_index") ?: "",
                fieldId = it.arguments?.getString("field_id") ?: "")
        }

        //display the result after the detection page
        composable(
            Screen.IdentifyShowResult.route,
            arguments = listOf(
                navArgument("marker_index") {
                    type = NavType.StringType
                    defaultValue = "no marker index"
                    nullable = false
                },
                navArgument("field_id") {
                    type = NavType.StringType
                    defaultValue = "no field id"
                    nullable = false
                })
        ) {
            IdentifyShowResultScreen(
                navController = navController,
                pestViewModel = pestViewModel,
                markerIndex = it.arguments?.getString("marker_index") ?: "",
                fieldId = it.arguments?.getString("field_id") ?: "")
        }


        //showing the result page after all the detection page
        composable(
            Screen.IdentifyFinalResult.route,
            arguments = listOf(
                navArgument("field_id") {
                    type = NavType.StringType
                    defaultValue = "no field id"
                    nullable = false
                })
        ) {
            IdentifyFinalResultScreen(
                navController = navController,
                pestViewModel = pestViewModel,
                resultViewModel= resultViewModel,
                userViewModel = userViewModel,
                fieldId = it.arguments?.getString("field_id") ?: "")
        }


        //setting page
        composable(Screen.Setting.route){
            SettingScreen(
                navController = navController,
                userViewModel = userViewModel
            )
        }

        //profile details page
        composable(Screen.Profile.route) {
            ProfileDetailsScreen(
                navController = navController,
                userViewModel = userViewModel
            )
        }

        //login page
        composable(Screen.Login.route){
            LoginScreen(
                navController = navController,
                userViewModel = userViewModel
            )
        }

        //forget password page
        composable(Screen.ForgotPassword.route){
            ForgotPasswordScreen(
                navController = navController,
                userViewModel = userViewModel,
            )
        }

        //register page
        composable(Screen.Register.route){
            RegisterScreen(
                navController = navController,
                userViewModel = userViewModel,
            )
        }

    }

}

