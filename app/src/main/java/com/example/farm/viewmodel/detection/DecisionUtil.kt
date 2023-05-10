package com.example.farm.viewmodel.detection


import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson

/**
 * generateDetectionPoint.kt
 * Written By: Luna Lot
 * Code From: https://github.com/PervasiveComputingGroup/FCIMCS_Mobile
 *
 * This function are help to generate the marker on the Google Map
 * Generate detection point for decision making
 * Decision making process:
 * Generate detection point --> Take picture in each point and do image detection --> Use detection result to quantify --> do decision
 *
 *
 *@param fieldPoint
 *@return ArrayList<LatLng>
 */


fun generateDetectionPoint(fieldPoint: List<LatLng>): ArrayList<LatLng> {

    var maxLat: Double? = null
    var minLat: Double? = null
    var maxLng: Double? = null
    var minLng: Double? = null
//  Get the maximum and minimum values of latitude and longitude
    for (point in fieldPoint) {
        if (maxLat == null) {
            maxLat = point.latitude
            minLat = point.latitude
            maxLng = point.longitude
            minLng = point.longitude
            continue
        }

        if (point.latitude > maxLat) {
            maxLat = point.latitude
        } else if (point.latitude < minLat!!) {
            minLat = point.latitude
        }

        if (point.longitude > maxLng!!) {
            maxLng = point.longitude
        } else if (point.longitude < minLng!!) {
            minLng = point.longitude
        }
    }

//    Determine which is longer in latitude and longitude
    var isLatLonger = true
    if ((maxLng!! - minLng!!) > (maxLat!! - minLat!!)) {
        isLatLonger = false
    }

    val generatedPoint: ArrayList<LatLng> = ArrayList<LatLng>()

    repeat(5) {
        val pointOfIntersection = arrayListOf<LatLng>()
        val ray = if (isLatLonger) {
            (it + 1) * (maxLat - minLat) / 6 + minLat
        } else {
            (it + 1) * (maxLng - minLng) / 6 + minLng
        }

        repeat(fieldPoint.size) { index ->
            if (index + 1 == fieldPoint.size) {
                val intersection =
                    getIntersection(arrayListOf(fieldPoint[index], fieldPoint[0]), ray, isLatLonger)
                if (intersection != null) {
                    pointOfIntersection.add(intersection)
                }
            } else {
                val intersection =
                    getIntersection(arrayListOf(fieldPoint[index], fieldPoint[index + 1]),
                        ray,
                        isLatLonger)
                if (intersection != null) {
                    pointOfIntersection.add(intersection)
                }
            }
        }
        var index = 0
        while (index + 1 < pointOfIntersection.size) {
            val lat =
                (pointOfIntersection[index].latitude + pointOfIntersection[index + 1].latitude) / 2.0
            val lng =
                (pointOfIntersection[index].longitude + pointOfIntersection[index + 1].longitude) / 2.0
            generatedPoint.add(LatLng(lat, lng))

            index += 2
        }
    }

    return generatedPoint
}

// Determining whether a ray intersects a line segment
fun getIntersection(line: ArrayList<LatLng>, ray: Double, isLatLonger: Boolean): LatLng? {
    var result: LatLng? = null

    val maxPoint: LatLng
    val minPoint: LatLng

    if (isLatLonger) {
        if (line[0].latitude > line[1].latitude) {
            maxPoint = line[0]
            minPoint = line[1]
        } else {
            minPoint = line[0]
            maxPoint = line[1]
        }

        if (ray < maxPoint.latitude && ray > minPoint.latitude) {
            val scale = (ray - minPoint.latitude) / (maxPoint.latitude - minPoint.latitude)
            val lng = minPoint.longitude + (maxPoint.longitude - minPoint.longitude) * scale
            result = LatLng(ray, lng)
        }
    } else {
        if (line[0].longitude > line[1].longitude) {
            maxPoint = line[0]
            minPoint = line[1]
        } else {
            minPoint = line[0]
            maxPoint = line[1]
        }

        if (ray < maxPoint.longitude && ray > minPoint.longitude) {
            val scale = (ray - minPoint.longitude) / (maxPoint.longitude - minPoint.longitude)
            val lat = minPoint.latitude + (maxPoint.latitude - minPoint.latitude) * scale
            result = LatLng(lat, ray)
        }
    }

    return result
}


fun Any.toJson(): String = Gson().toJson(this)