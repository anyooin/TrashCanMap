package com.example.application830


import android.Manifest
import android.annotation.SuppressLint
import android.app.ProgressDialog.show
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Build.VERSION_CODES.S
import android.os.Bundle
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.application830.databinding.ActivityMapBinding
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.apache.poi.hssf.usermodel.HSSFCell
import org.apache.poi.hssf.usermodel.HSSFRow
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.poifs.filesystem.POIFSFileSystem
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.apache.poi.xssf.usermodel.XSSFCell
import org.apache.poi.xssf.usermodel.XSSFRow
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.w3c.dom.Document
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import java.io.InputStream
import java.util.*
import javax.xml.parsers.DocumentBuilderFactory


var items:MutableList<String> = mutableListOf()

@RequiresApi(33)
class MapActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener,
    View.OnClickListener, GoogleMap.OnMyLocationButtonClickListener{

    private lateinit var map: GoogleMap

    lateinit var locationPermission : ActivityResultLauncher<Array<String>>
    lateinit var fusedLocationClient: FusedLocationProviderClient //위치 서비스가 gps 사용해서 위치를 확인
    lateinit var locationCallback: LocationCallback //위치 값 요청에 대한 갱신 정보를 받는 변수
    lateinit var binding : ActivityMapBinding
    var currentMarker : Marker? = null
    var registerBtnClick = false

    private val permission = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
    private val checkPermission = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){
        result -> result.forEach{
            if(!it.value){
                Toast.makeText(this@MapActivity, "갤러리를 열기 위해 권한 동의가 필요합니다.", Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }

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

        //등록요청 버튼
        val registerationBtn = findViewById<Button>(R.id.LocationRegistrationBtn)
        registerationBtn.setOnClickListener {
            Toast.makeText(this,"지도를 눌러 위치를 선택해주세요!", Toast.LENGTH_LONG).show()
            registerBtnClick = true
        }

        //권한 요청
        locationPermission.launch(
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        )

        //엑셀파일 읽어오기
        readExcelFileFromAsserts()
    }


    //read excel file
    private fun readExcelFileFromAsserts() {
        try{
            val myInput : InputStream
            val assetManager = assets

            //엑셀 시트 열기
            myInput = assetManager.open("trashcan.xlsx")

            val myWorkBook = WorkbookFactory.create(myInput)
            val sheet = myWorkBook.getSheetAt(0)

            val rowIter = sheet.rowIterator()
            var rowno = 0
            while(rowIter.hasNext())
            {
                val myRow = rowIter.next() as XSSFRow
                if(rowno != 0)
                {
                    val cellIter = myRow.cellIterator()

                    var colno = 0
                    var str = ""

                    while(cellIter.hasNext())
                    {
                        val myCell = cellIter.next() as XSSFCell
                        if(colno == 0){ //지번주소
                            str = str + myCell.toString() + "_"
                        }
                        else if(colno == 1) //latitude
                        {
                            str = str + myCell.toString() + "_"
                        }
                        else if(colno == 2) //longitude
                        {
                            str = str + myCell.toString() + "_"
                        }
                        else if(colno == 3)//상세주소
                        {
                            str = str + myCell.toString() + "_"
                        }
                        colno++
                    }
                    items.add(str)
                }
                rowno++
            }
            //Log.d("checking", "items : " + items)
            for(i in 0..items.size-1) {
                Log.d("checking", "item = ${items[i]}")
            }

        }
        catch(e:Exception)
        {
            Log.d("checking","open excel failed $e")
        }

    }

    //googleMap 준비하기
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.uiSettings.isZoomControlsEnabled = true
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
        map.isMyLocationEnabled = true
        map.setOnMyLocationButtonClickListener(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        updateLocation()


        //google map click event 처리
        map.setOnMarkerClickListener(this)
        map.setOnMapClickListener(object : GoogleMap.OnMapClickListener{
            override fun onMapClick(latLng: LatLng)
            {
                Log.d("locationCheck", "location = ${latLng.latitude}, ${latLng.longitude}")
                if(registerBtnClick) {
                    val dig = PositionRegisterDialog(this@MapActivity)

                    dig.setOnYesBtnClicked {
                            content ->
                                    Toast.makeText(this@MapActivity, "등록이 요청되었습니다.", Toast.LENGTH_LONG).show()

                    }

                    dig.show()
                    registerBtnClick = false
                }
            }
        })

        //마커표시하기
        for(i in 0..items.size-1)
        {
            makemarker(items[i])
        }
    }

    override fun onMyLocationButtonClick(): Boolean {
        return false
    }


    // 위치 update
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

    // 맵 이동
    fun moveMap(location:Location){

        val latLng = LatLng(location.latitude, location.longitude)
        val position = CameraPosition.Builder()
            .target(latLng)
            .zoom(16f)
            .build()

        map.moveCamera((CameraUpdateFactory.newCameraPosition(position)))
    }

    //현재 마커 위치 변경
    fun movemarker(location: Location)
    {
        currentMarker?.remove()
        //Log.d("위치정보", "마커정보!")
        val latLng = LatLng(location.latitude, location.longitude)
        val markerOptions = MarkerOptions()
        markerOptions.position(latLng)
        markerOptions.title("현위치")

        currentMarker = map.addMarker(markerOptions)
    }

    //str => 지번주소 상세주소
    fun makemarker(str:String)
    {
        var str_arr = str.split("_")
        Log.d("stt","${str_arr}")
        val latLng = LatLng(str_arr[1].toDouble(), str_arr[2].toDouble())

        val markerOptions = MarkerOptions()
        markerOptions.position(latLng)
        markerOptions.title(str_arr[0])
        markerOptions.snippet(str_arr[3])

        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))

        map.addMarker(markerOptions)
    }

    //marker click event
    override fun onMarkerClick(marker: Marker): Boolean {
        //Toast.makeText(this, marker.title + "\n" + marker.position, Toast.LENGTH_SHORT).show()

        val dig = RegistrationDialog(this)

        dig.setOnDeleteBtnClickedListener {
            content -> Toast.makeText(this,"${content}", Toast.LENGTH_SHORT).show()
        }
        dig.show(marker.title, marker.snippet)
        return true
    }

    override fun onClick(v: View?) {
        when(v?.id){

        }
    }
}


