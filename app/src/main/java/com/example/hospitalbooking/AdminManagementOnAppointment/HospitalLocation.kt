package com.example.hospitalbooking.AdminManagementOnAppointment

import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hospitalbooking.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class HospitalLocation : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hospital_location)
        val hospitalName = intent.getStringExtra("HospitalName").toString()

        val mapView = findViewById<MapView>(R.id.map_view)

        // Receive parameter from the function
      /*  var hospitalName = hospitaLocation(docNameLocate)*/

        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync { googleMap ->
            // do something with the googleMap object
            var latitude = 0.0
            var longitude = 0.0

            val geocoder = Geocoder(this)
            val addressList = geocoder.getFromLocationName(hospitalName, 1)
            if (addressList.isNotEmpty()) {
                latitude = addressList[0].latitude
                longitude = addressList[0].longitude

                // add marker and move camera to the building location
                val markerOptions = MarkerOptions()
                markerOptions.position(LatLng(latitude, longitude))
                markerOptions.title(hospitalName)
                googleMap.addMarker(markerOptions)

                val cameraPosition = CameraPosition.Builder()
                    .target(LatLng(latitude, longitude))
                    .zoom(15f)
                    .build()
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

            }
        }






    }
}