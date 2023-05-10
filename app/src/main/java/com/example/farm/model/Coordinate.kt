package com.example.farm.model

/**
 * Coordinate.kt
 * Written By: Jing Wen Ng
 *
 * The data class representing a field's path.
 *
 * @param id The id of the field
 * @param long The longitude value
 * @param lat The latitude value
 * @param fieldId The id of the field
 */

data class Coordinate(

    val id: Int =0,
    val long: Double,
    val lat: Double,
    val fieldId: Int

){
    override fun equals(other: Any?): Boolean {
        val is_equal = super.equals(other)

        if (!is_equal) {
            return false
        }
        val other_coordinate = other as Coordinate
        return this.long == other_coordinate.long &&
                this.lat == other_coordinate.lat &&
                this.fieldId == other_coordinate.fieldId &&
                this.id == other_coordinate.id
    }
}