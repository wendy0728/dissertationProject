package com.example.farm

import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.farm.viewmodel.CoordinateViewModel
import com.example.farm.viewmodel.EncyDetailsViewModelFactory
import com.example.farm.viewmodel.EncyPotentialDiseaseViewModel
import com.example.farm.viewmodel.EncyViewModel
import com.example.farm.viewmodel.EncyViewModelFactory
import com.example.farm.viewmodel.FieldViewModel
import com.example.farm.viewmodel.FieldViewModelFactory
import com.example.farm.viewmodel.PathViewModelFactory
import com.example.farm.viewmodel.PestViewModel
import com.example.farm.viewmodel.PestViewModelFactory
import com.example.farm.viewmodel.ResultViewModel
import com.example.farm.viewmodel.ResultViewModelFactory
import com.example.farm.viewmodel.UserViewModel
import com.example.farm.viewmodel.UserViewModelFactory


/**
 * MyAppCompactActivity.kt
 * Written By: Jing Wen Ng
 *
 * It provide fieldViewModel,resultViewModel,pestViewModel,encyViewModel,encyPotentialDiseaseViewModel,coordinateViewModel and userViewModel as property.
 */
open class MyAppCompatActivity: AppCompatActivity() {


    protected val fieldViewModel: FieldViewModel by viewModels {
        FieldViewModelFactory((application as Application).fieldRepository,application)
    }

    protected val resultViewModel: ResultViewModel by viewModels {
        ResultViewModelFactory((application as Application).resultRepository,application)
    }

    protected  val pestViewModel: PestViewModel by viewModels {
        PestViewModelFactory((application as Application).pestRepository,application)
    }

    protected val encyViewModel: EncyViewModel by viewModels {
        EncyViewModelFactory((application as Application).encyRepository,application)
    }

    protected val encyPotentialDiseaseViewModel: EncyPotentialDiseaseViewModel by viewModels {
        EncyDetailsViewModelFactory((application as Application).encyPotentialDiseaseRepository,application)
    }

    protected val coordinateViewModel: CoordinateViewModel by viewModels {
        PathViewModelFactory((application as Application).coordRepository,application)
    }

    protected val userViewModel: UserViewModel by viewModels {
        UserViewModelFactory((application as Application).userRepository,application)
    }

}