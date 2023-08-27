package com.example.application830

import androidx.lifecycle.Transformations.map


import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener
import com.google.android.gms.maps.GoogleMap.OnMyLocationClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MapActivity : AppCompatActivity(), OnMapReadyCallback{

    private lateinit var map: GoogleMap

    lateinit var locationPermission : ActivityResultLauncher<Array<String>>
    lateinit var fusedLocationClient: FusedLocationProviderClient //위치 서비스가 gps 사용해서 위치를 확인
    lateinit var locationCallback: LocationCallback //위치 값 요청에 대한 갱신 정보를 받는 변수

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)


        locationPermission = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()){ results ->
            if(results.all{it.value}){
                val mapFragment =
                    supportFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment?
                mapFragment?.getMapAsync(this)
            }else
            {
                Toast.makeText(this,"승인 권한이 필요합니다", Toast.LENGTH_LONG).show()
            }
        }

        //권한 요청
        locationPermission.launch(
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        )
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        updateLocation()
    }

    @SuppressLint("MissingPermission")
    fun updateLocation(){
        val locationRequest = LocationRequest.create()
        locationRequest.run {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 1000
        }
        var flag = 1

        locationCallback = object : LocationCallback(){
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.let{
                    for(location in it.locations) {
                        Log.d("위치정보", "위도 : ${location.latitude}, 경도 : ${location.longitude}")
                        if(flag == 1)
                        {
                            flag = 0
                            moveMap(location)
                        }
                        movemarker(location)
                    }
                }
            }
        }

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
    }

    fun moveMap(location:Location){

        Log.d("위치정보", "-----moveMap 표시시작!")
        val latLng = LatLng(location.latitude, location.longitude)
        val position = CameraPosition.Builder()
            .target(latLng)
            .zoom(16f)
            .build()

        map.moveCamera((CameraUpdateFactory.newCameraPosition(position)))


        Log.d("위치정보", "-----moveMap 표시끝!")
    }

    fun movemarker(location: Location)
    {
        Log.d("위치정보", "마커정보!")
        val latLng = LatLng(location.latitude, location.longitude)
        val markerOptions = MarkerOptions()
        markerOptions.position(latLng)
        markerOptions.title("현위치")

        map.addMarker(markerOptions)
    }


}