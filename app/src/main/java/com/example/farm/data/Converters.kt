package com.example.farm.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

/**
 * Converters.kt
 * Written By: Jing Wen Ng
 *
 * This class help to convert complex data.
 *
 * [Converters] is helping to convert complex data types such as ArrayList<Int> and Date and Used by [AppDatabase.kt].
 */

class Converters {

    /**
     * Converts a Long into a Date
     *
     * @param value in Long type
     * @return a value in Date type
     */
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    /**
     * Converts a Date into a Long
     *
     * @param date in Date type
     * @return a value in Long type
     */
    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }


    /**
     * Converts a Date into a Long
     *
     * @param value in String type
     * @return a value in ArrayList<Int> type
     */
    @TypeConverter
    fun fromStringToListInt(value: String): ArrayList<Int> {
        val listType = object : TypeToken<ArrayList<Int>>() {}.type
        return Gson().fromJson(value, listType)
    }

    /**
     * Converts a Date into a Long
     *
     * @param list in ArrayList<Int> type
     * @return a value in String type
     */
    @TypeConverter
    fun fromArrayList(list: ArrayList<Int>): String {
        return Gson().toJson(list)
    }


}
