package com.example.application830

import android.Manifest
import android.annotation.SuppressLint
import android.hardware.Camera
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Transformations.map
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private var mMap: GoogleMap? = null

    lateinit var locationPermission : ActivityResultLauncher<Array<String>>
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient //위치 서비스가 gps 사용해서 위치를 확인
    lateinit var locationCallback: LocationCallback //위치 값 요청에 대한 갱신 정보를 받는 변수

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        /*val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
*/
        locationPermission = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()){ results ->
            if(results.all{it.value}){
                startProcess()
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

    fun startProcess(){
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    // NULL이 아닌 GoogleMap 객체를 파라미터로 제공해 줄 수 있을 때 호출
    override fun onMapReady(googleMap: GoogleMap) {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        updateLocation()
        /*mMap = googleMap
        val SEOUL = LatLng(37.556, 126.97)
        val markerOptions = MarkerOptions()
        markerOptions.position(SEOUL)
        markerOptions.title("서울")
        markerOptions.snippet("한국 수도")
        mMap!!.addMarker(markerOptions)
        mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(SEOUL, 10f)) */
    }

    @SuppressLint("MissingPermission")
    fun updateLocation()
    {
        val locationRequest = LocationRequest.create()
        locationRequest.run{
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 1000
        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                p0?.let{
                    for (location in it.locations){
                      Log.d("위치정보", "위도 : ${location.latitude} 경도 : ${location.longitude}")
                        setLastLocation(location)
                    }
                }
            }
        }

        fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback, Looper.myLooper())
    }

    fun setLastLocation(lastLocation : Location){
        val LATLNG = LatLng(lastLocation.latitude, lastLocation.longitude)

        val makerOptions = MarkerOptions().position(LATLNG).title("i am here")
        val cameraPosition = CameraPosition.Builder().target(LATLNG).zoom(15.0f).build()

        mMap?.clear()
        mMap?.addMarker(makerOptions)
        mMap?.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }
}