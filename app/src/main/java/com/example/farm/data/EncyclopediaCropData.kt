package com.example.farm.data

import androidx.room.*

/**
 * EncyclopediaCropData.kt
 * Written By: Jing Wen Ng
 *
 * This data class will be included all the crop names for the encyclopedia.
 *
 * @param id The id of the crop
 * @param name The name of the crop
 */

@Entity(tableName = "encyclopediaCrop", indices=[Index(value=["id"])])
data class EncyclopediaCropData (
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name="crop_name") var name: String
    )