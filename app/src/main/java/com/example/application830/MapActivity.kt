package com.example.application830


import android.Manifest
import android.annotation.SuppressLint
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
    View.OnClickListener{

    private lateinit var map: GoogleMap

    lateinit var locationPermission : ActivityResultLauncher<Array<String>>
    lateinit var fusedLocationClient: FusedLocationProviderClient //위치 서비스가 gps 사용해서 위치를 확인
    lateinit var locationCallback: LocationCallback //위치 값 요청에 대한 갱신 정보를 받는 변수
    lateinit var binding : ActivityMapBinding
    var currentMarker : Marker? = null

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

        //엑셀파일 읽어오기
        readExcelFileFromAsserts()
    }

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
                        else if(colno == 3)//도로명주소
                        {
                            str = str + myCell.toString() + "_"
                        }
                        else if(colno == 4)//상세주소
                        {
                            str = str + myCell.toString() + "_"
                        }
                        colno++
                    }
                    items.add(str)
                }
                rowno++
            }
            Log.d("checking", "items : " + items)
        }
        catch(e:Exception)
        {
            Log.d("checking","open excel failed $e")
        }

    }



    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.uiSettings.isZoomControlsEnabled = true

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        updateLocation()

        map.setOnMarkerClickListener(this)


        //마커표시하기
        for(i in 0..items.size-1)
        {
            makemarker(items[i])
        }
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
    //str => 지번주소, latitude,longitude, 도로명주소, 상세주소, 개수, type, date
    fun makemarker(str:String)
    {
        var str_arr = str.split("_")
        val latLng = LatLng(str_arr[1].toDouble(), str_arr[2].toDouble())

        val markerOptions = MarkerOptions()
        markerOptions.position(latLng)
        markerOptions.title(str_arr[0])

        map.addMarker(markerOptions)
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        Toast.makeText(this, marker.title + "\n" + marker.position, Toast.LENGTH_SHORT).show()

        val dig = RegistrationDialog(this)

        dig.setOnDeleteBtnClickedListener {
            content -> Toast.makeText(this,"${content}", Toast.LENGTH_SHORT).show()
        }
        dig.show(marker.title)
        return true
    }

    override fun onClick(v: View?) {
        when(v?.id){

        }
    }


}

/*
@RequiresApi(33)
class NetworkThread(var url : String, private val context : AppCompatActivity ) : Runnable{
    @RequiresApi(Build.VERSION_CODES.N)

    //var geocoder = Geocoder(context, Locale.getDefault())
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
            //geocoder.getFromLocationName(mutableList[0][0], 2, this)
        } catch(e: Exception)
        {
            Log.d("exception", "openAPI"+e.toString())
        }
    }

    /*override fun onGeocode(addresses: MutableList<Address>) {
        if(addresses.isNotEmpty()){
            val address = addresses[0]
            val latLng = LatLng(address.latitude, address.longitude)
            Log.d("LatLng", "Latitude ${latLng.latitude}, Longitude ${latLng.longitude}")

        }else
        {
            Log.d("LatLng", "address not found")
        }
    }

    override fun onError(errorMessage: String?) {
        Log.d("LatLng", "Geocoding error : $errorMessage")
    } */
}

*/



