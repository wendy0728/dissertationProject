package com.example.farm.model

import java.util.*

/**
 * Result.kt
 * Written By: Jing Wen Ng
 *
 * The data class representing a field's result.
 *
 * @param id The id of the result
 * @param fieldId The id of the field
 * @param updatedDate The updated date of the result
 * @param pestDetected The pests that detected
 * @param updatedBy The people's email who update this result
 */


data class Result(

    val id: Int =0,
    val fieldId: Int =0,
    val updatedDate: Date,
    val pestDetected: ArrayList<Int>,
    val updatedBy: String


){

    override fun equals(other: Any?): Boolean {
        val is_equal = super.equals(other)

        if (!is_equal) {
            return false
        }
        val other_result = other as Result
        return this.fieldId == other_result.fieldId &&
                this.pestDetected == other_result.pestDetected &&
                this.updatedDate == other_result.updatedDate &&
                this.updatedBy == other_result.updatedBy &&
                this.id == other_result.id
    }
}
