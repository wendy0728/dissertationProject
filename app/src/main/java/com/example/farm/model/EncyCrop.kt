package com.example.farm.model


/**
 * EncyCrop.kt
 * Written By: Jing Wen Ng
 *
 * The data class representing a crop.
 *
 * @param id The id of the crop
 * @param name The name of the crop
 */

data class EncyCrop(

    val id: Int =0,
    val name: String,

){

    override fun equals(other: Any?): Boolean {
        val is_equal = super.equals(other)

        if (!is_equal) {
            return false
        }
        val other_encyclopediaCrop = other as Field
        return this.name == other_encyclopediaCrop.name &&
                this.id == other_encyclopediaCrop.id
    }
}