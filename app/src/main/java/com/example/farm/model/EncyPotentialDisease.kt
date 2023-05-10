package com.example.farm.model

/**
 * EncyPotentialDisease.kt
 * Written By: Jing Wen Ng
 *
 * The data class representing a crop's potential disease.
 *
 * @param id The id of the crop
 * @param cropId The crop id
 * @param disease Crop potential disease's name
 * @param symptoms Crop potential disease's symptoms
 * @param cause Crop potential disease's cause
 * @param prevention Ways to prevent crop's getting this potential disease
 * @param recommendation Recommendation to cure the the crop's potential disease
 */

data class EncyPotentialDisease(

    val id: Int =0,
    val cropId: Int,
    val disease: String,
    val symptoms: String,
    val cause: String,
    val prevention: String,
    val recommendation: String
    ){

    override fun equals(other: Any?): Boolean {
        val is_equal = super.equals(other)

        if (!is_equal) {
            return false
        }
        val other_encyDetails= other as EncyPotentialDisease
        return this.cropId == other_encyDetails.cropId &&
                this.symptoms == other_encyDetails.symptoms &&
                this.disease == other_encyDetails.disease &&
                this.cause == other_encyDetails.cause &&
                this.prevention == other_encyDetails.prevention &&
                this.recommendation == other_encyDetails.recommendation &&
                this.id == other_encyDetails.id
    }
}