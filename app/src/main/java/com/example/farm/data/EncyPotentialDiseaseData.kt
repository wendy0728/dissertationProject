package com.example.farm.data

import androidx.room.*

/**
 * EncyDetailsData.kt
 * Written By: Jing Wen Ng
 *
 * This data class will be included all the crops' potential diseases for the encyclopedia.
 *
 * @param id The id of the potential disease
 * @param cropId The id of the crop
 * @param disease Crop potential disease's name
 * @param symptoms Crop potential disease's symptoms
 * @param cause Crop potential disease's cause
 * @param prevention Ways to prevent crop's getting this potential disease
 * @param recommendation Recommendation to cure the the crop's potential disease
 */
@Entity(tableName = "encyDetails", indices=[Index(value=["id"])])
data class EncyPotentialDiseaseData (

    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name="crop_id") var cropId: Int,
    @ColumnInfo(name="disease") var disease: String,
    @ColumnInfo(name="symptoms") var symptoms: String,
    @ColumnInfo(name="cause") var cause: String,
    @ColumnInfo(name="prevention") var prevention: String,
    @ColumnInfo(name="recommendation") var recommendation: String,




)