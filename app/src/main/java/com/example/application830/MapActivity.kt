package com.example.application830


import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.application830.databinding.ActivityMapBinding
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.w3c.dom.Document
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import java.util.*
import javax.xml.parsers.DocumentBuilderFactory


// 설치위치 지번주소 - 설치위치 도로명주소 - 상세위치 - 기준일자 - 개수 - 종류
var mutableList = mutableListOf<Array<String>>()


@RequiresApi(33)
class MapActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener,
    View.OnClickListener{

    private lateinit var map: GoogleMap

    lateinit var locationPermission : ActivityResultLauncher<Array<String>>
    lateinit var fusedLocationClient: FusedLocationProviderClient //위치 서비스가 gps 사용해서 위치를 확인
    lateinit var locationCallback: LocationCallback //위치 값 요청에 대한 갱신 정보를 받는 변수
    lateinit var binding : ActivityMapBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        val registerationBtn = findViewById<Button>(R.id.LocationRegistrationBtn)
        registerationBtn.setOnClickListener {

        }

        //권한 요청
        locationPermission.launch(
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        )


        val url = "https://api.odcloud.kr/api/15093750/v1/uddi:b567f95c-7810-4bab-8f14-d0109ccc492b?page=1&perPage=10&returnType=XML&serviceKey=4hFAq3GRnmhcQmJbDNBj5lCELvELwDgkwXTtcFf%2BGiFy9Fl2aWBgwgfEIw4KcyHn6dJtvNGMow0JCeggxu0t3Q%3D%3D"
        val thread = Thread(NetworkThread(url))
        thread.start()
        thread.join()
    }


    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.uiSettings.isZoomControlsEnabled = true

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        updateLocation()

        map.setOnMarkerClickListener(this)

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
                        //Log.d("위치정보", "위도 : ${location.latitude}, 경도 : ${location.longitude}")
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

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
    }

    fun moveMap(location:Location){

        val latLng = LatLng(location.latitude, location.longitude)
        val position = CameraPosition.Builder()
            .target(latLng)
            .zoom(16f)
            .build()

        map.moveCamera((CameraUpdateFactory.newCameraPosition(position)))
    }

    fun movemarker(location: Location)
    {
        //Log.d("위치정보", "마커정보!")
        val latLng = LatLng(location.latitude, location.longitude)
        val markerOptions = MarkerOptions()
        markerOptions.position(latLng)
        markerOptions.title("현위치")

        map.addMarker(markerOptions)
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        Toast.makeText(this, marker.title + "\n" + marker.position, Toast.LENGTH_SHORT).show()
        //Log.d("marker","click the mark")

        val dig = RegistrationDialog(this)

        dig.setOnDeleteBtnClickedListener {
            content -> Toast.makeText(this,"${content}", Toast.LENGTH_SHORT).show()
        }

        //marker tag에 저장 가능

        dig.show(marker.title)
        return true
    }

    override fun onClick(v: View?) {
        when(v?.id){

        }
    }


}


class NetworkThread( var url : String ) : Runnable {
    @RequiresApi(Build.VERSION_CODES.N)
    override fun run()
    {
        try {
            val xml : Document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(url)
            xml.documentElement.normalize()

            val list: NodeList = xml.getElementsByTagName("col")
            //Log.d("위치정보", "=============length = ${list.length}================")

            var trashPositionArray : Array<String> = Array(6,{"-"})
            for(i in 0..list.length-1) {
                val n : Node = list.item(i)
                //Log.d("위치정보", "No.${i+1}, ${n.textContent}, ${n.attributes.item(0).textContent}")

                // 설치위치 지번주소 - 설치위치 도로명주소 - 상세위치 - 기준일자 - 개수 - 종류
                if(n.attributes.item(0).textContent.equals("설치위치 지번주소")) {
                    trashPositionArray[0] = n.textContent
                }
                else if(n.attributes.item(0).textContent.equals("설치위치 도로명주소")) {
                    trashPositionArray[1] = n.textContent
                }
                else if(n.attributes.item(0).textContent.equals("상세위치")) {
                    trashPositionArray[2] = n.textContent
                }
                else if(n.attributes.item(0).textContent.equals("기준일자")) {
                    trashPositionArray[3] = n.textContent
                }
                else if(n.attributes.item(0).textContent.equals("개수")) {
                    trashPositionArray[4] = n.textContent
                }
                else if(n.attributes.item(0).textContent.equals("종류")) {
                    trashPositionArray[5] = n.textContent
                }

                if((i+1)%6 == 0) {
                    val temp : Array<String> = Array(6,{"-"})
                    System.arraycopy(trashPositionArray,0,temp,0,6)
                    mutableList.add(temp)
                }
            }
        } catch(e: Exception)
        {
            Log.d("exception", "openAPI"+e.toString())
        }
    }
}





