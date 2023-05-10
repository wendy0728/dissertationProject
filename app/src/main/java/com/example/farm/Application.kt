package com.example.farm

import android.app.Application
import com.example.farm.data.AppDatabase
import com.example.farm.repository.CoordinateRepository
import com.example.farm.repository.EncyPotentialDiseaseRepository
import com.example.farm.repository.EncyRepository
import com.example.farm.repository.FieldRepository
import com.example.farm.repository.PestRepository
import com.example.farm.repository.ResultRepository
import com.example.farm.repository.UserRepository


/**
 * Application.kt
 * Written By: Jing Wen Ng
 *
 * Application inherits the Application() to update fieldRepository,resultRepository,pestRepository,encyRepository
 * encyPotentialDiseaseRepository,coordRepository and userRepository.
 *
 */

class Application: Application() {
    val databaseObj: AppDatabase by lazy { AppDatabase.getInstance(this) }

    val fieldRepository: FieldRepository by lazy { FieldRepository(databaseObj.fieldDao()) }

    val resultRepository: ResultRepository by lazy { ResultRepository(databaseObj.resultDao()) }

    val pestRepository: PestRepository by lazy { PestRepository(databaseObj.pestDao())}

    val encyRepository: EncyRepository by lazy { EncyRepository(databaseObj.encyclopediaCropDao())}

    val encyPotentialDiseaseRepository: EncyPotentialDiseaseRepository by lazy { EncyPotentialDiseaseRepository(databaseObj.encyPotentialDiseaseDao()) }

    val coordRepository: CoordinateRepository by lazy { CoordinateRepository(databaseObj.coordinateDao()) }

    val userRepository: UserRepository by lazy { UserRepository(databaseObj.userDao()) }
}