package com.example.farm.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * PestData.kt
 * Written By: Jing Wen Ng
 *
 * This data class will be included all the pests.
 *
 * @param id The id of the pest
 * @param name The name of the pest
 * @param recommendation The recommendation to control the pest
 * @param prevention The way to prevent the pest
 */


@Entity(tableName = "pests", indices=[Index(value=["id"])])
data class PestsData(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name="pest_name") val name: String,
    @ColumnInfo(name="pest_recommendation") val recommendation: String,
    @ColumnInfo(name="pest_prevention") val prevention: String
    )