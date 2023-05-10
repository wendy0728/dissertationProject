package com.example.farm.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * FieldData.kt
 * Written By: Jing Wen Ng
 *
 * This data class will be included all the fields.
 *
 * @param id The id of the field
 * @param name The name of field
 * @param location The location of field
 * @param crop The type of crop of the field
 */

@Entity(tableName = "field", indices=[Index(value=["id"])])
data class FieldData (
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name="field_name") var name: String,
    @ColumnInfo(name="field_location") var location: String,
    @ColumnInfo(name="field_crop") var crop: String

    )

