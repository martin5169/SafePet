package com.example.myapplication.helper

import android.content.Context
import android.location.Address
import android.location.Geocoder
import com.google.android.gms.maps.model.LatLng
import java.util.Locale

class GeocodingHelper(private val context: Context) {

    fun getAddressFromLatLng(latLng: LatLng): String {
        val geocoder = Geocoder(context, Locale.getDefault())
        var addressText = ""

        try {
            val addresses = geocoder.getFromLocation(
                latLng.latitude,
                latLng.longitude,
                1
            )

            if (addresses!!.isNotEmpty()) {
                val address: Address = addresses[0]
                addressText = address.getAddressLine(0)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return addressText
    }
}