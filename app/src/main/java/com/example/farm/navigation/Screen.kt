package com.example.farm.navigation

const val MAIN_ROUTE = "main"
const val ROOT_ROUTE = "root"

/**
 * Screen.kt
 * Written By: Jing Wen Ng
 *
 * Assign a route for each screen.
 * Some of the routes with arguments.
 *
 */
sealed class Screen (val route: String){


    //Login
    object Login: Screen(route="login")
    object ForgotPassword: Screen(route="forgot_password")

    //Register
    object Register: Screen(route="register")

    //Home
    object FieldList: Screen(route = "field_list_screen")
    object ResultList: Screen(route="result_list/{field_id}")
    object ResultDetails: Screen(route="result_details/{field_id}/{result_id}")

    //Encyclopedia
    object EncyclopediaMain: Screen(route = "encyclopedia_main")
    object CropPotentialDisease: Screen(route = "crop_potential_disease/{crop_name}")
    object EncyPotentialDiseaseDetails: Screen(route="potential_disease_details/{crop_name}/{id}")

    //Identify Pests
    object IdentifyMain: Screen(route="identify_main")
    object IdentifyMap: Screen(route="identify_map/{field_id}")
    object IdentifyCamera: Screen(route="identify_camera/{field_id}/{marker_index}")
    object IdentifyShowImage: Screen(route="identify_show_image/{field_id}/{marker_index}")
    object IdentifyShowResult: Screen(route="identify_show_result/{field_id}/{marker_index}")
    object IdentifyFinalResult: Screen(route="identify_final_result/{field_id}")

    //Setting
    object Setting: Screen(route="setting")
    object Profile: Screen(route="profile")


}