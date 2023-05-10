package com.example.farm.model

/**
 * Field.kt
 * Written By: Jing Wen Ng
 *
 * The data class representing a field.
 *
 * @param id The id of the field
 * @param name The name of field
 * @param location The location of field
 * @param crop The type of crop of the field
 */


data class Field(

    val id: Int =0,
    val name: String,
    val location: String,
    val crop: String

){

    override fun equals(other: Any?): Boolean {
        val is_equal = super.equals(other)

        if (!is_equal) {
            return false
        }
        val other_field = other as Field
        return this.name == other_field.name &&
                this.location == other_field.location &&
                this.crop == other_field.crop &&
                this.id == other_field.id
    }
}