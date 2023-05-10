package com.example.farm.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

/**
 * AppDatabase.kt
 * Written By: Jing Wen Ng
 *
 * The rooms database
 *
 * This database is included [EncyclopediaCropData],[EncyPotentialDiseaseData],[FieldData],[CoordinateData],[PestsData],[ResultData],[UserData].
 */

@Database(entities = [EncyclopediaCropData::class, EncyPotentialDiseaseData::class, FieldData::class, CoordinateData::class, PestsData::class, ResultData::class, UserData::class], version = 64, exportSchema = false)
@TypeConverters(Converters::class)

abstract class AppDatabase: RoomDatabase() {

    abstract fun fieldDao(): FieldDao

    abstract fun resultDao(): ResultDao

    abstract fun pestDao(): PestDao

    abstract fun encyclopediaCropDao(): EncyclopediaCropDao

    abstract fun encyPotentialDiseaseDao(): EncyPotentialDiseaseDao

    abstract fun coordinateDao(): CoordinateDao

    abstract fun userDao(): UserDao


    //Database is named as Farm
    companion object{
        private val DATABASE_NAME = "Farm"
        @Volatile private var db_instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return db_instance ?: synchronized(this) {
                db_instance ?: buildDatabase(context).also { db_instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}