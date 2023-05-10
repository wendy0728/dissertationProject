package com.example.farm.model

/**
 * Path.kt
 * Written By: Jing Wen Ng
 *
 * The data class representing a pest.
 *
 * @param id The id of the pest
 * @param name The name of the pest
 * @param recommendation The recommendation to control the pest
 * @param prevention The way to prevent the pest
 */

data class Pest(

    val id: Int =0,
    val name: String,
    val recommendation: String,
    val prevention: String

){

    override fun equals(other: Any?): Boolean {
        val is_equal = super.equals(other)

        if (!is_equal) {
            return false
        }
        val other_pest = other as Pest
        return this.name == other_pest.name &&
                this.recommendation == other_pest.recommendation &&
                this.prevention == other_pest.prevention &&
                this.id == other_pest.id
    }
}