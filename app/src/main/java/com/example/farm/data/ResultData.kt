package com.example.farm.data

import androidx.room.*
import java.util.*
import kotlin.collections.ArrayList

/**
 * ResultData.kt
 * Written By: Jing Wen Ng
 *
 * This data class will be included all the results.
 *
 * @param id The id of the result
 * @param fieldId The id of the field
 * @param updatedDate The updated date of the result
 * @param pestDetected The pests that detected
 * @param updatedBy The email address of the people who update this result
 */


@Entity(tableName = "result", indices=[Index(value=["id"])])
data class ResultData (
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name="fieldId") var fieldId: Int,
    @ColumnInfo(name="update_time") var updatedDate: Date,
    @ColumnInfo(name="pest_detected") val pestDetected: ArrayList<Int>,
    @ColumnInfo(name="update_by") val updatedBy: String

    )

