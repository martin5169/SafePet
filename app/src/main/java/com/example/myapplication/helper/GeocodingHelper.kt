package com.example.myapplication.helper

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.util.Log
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
                addressText = "${address.thoroughfare} ${address.featureName}"
                Log.d("DIRECCION", address.toString())
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return addressText
    }
}