package com.example.farm.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * CoordinateData.kt
 * Written By: Jing Wen Ng
 *
 * This data class will be included all the fields' path.
 *
 * @param id The id of the field
 * @param long The longitude value
 * @param lat The latitude value
 * @param fieldId The id of the field
 */

@Entity(tableName = "coordinate", indices=[Index(value=["id"])])
data class CoordinateData(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name="long") var long: Double,
    @ColumnInfo(name="lat") var lat: Double,
    @ColumnInfo(name="fieldId") var fieldId: Int
){
    constructor() : this(0, 0.0,0.0,0)
}