package com.example.farm.model

/**
 * User.kt
 * Written By: Jing Wen Ng
 *
 * The data class representing a user.
 *
 * @param email The id of the user
 * @param name The name of the user
 * @param occupation The occupation of the user
 * @param password The password of the user
 * @param salt The salt in password
 */


data class User(

    val email: String,
    val name: String,
    val occupation: String,
    var password: String,
    val salt: String

){

    override fun equals(other: Any?): Boolean {
        val is_equal = super.equals(other)

        if (!is_equal) {
            return false
        }
        val other_user = other as User
        return this.email == other_user.email &&
                this.name == other_user.name &&
                this.occupation == other_user.occupation &&
                this.password == other_user.password &&
                this.salt == other_user.salt
    }

}