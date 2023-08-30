package com.example.application830

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import com.example.application830.databinding.ActivityMainBinding

var login = false
var ID = ""
var PW = ""
val DATABASE_VERSION = 1
val DATABASE_NAME1 = "LocalDB.db"
val DATABASE_NAME2 = "AddressDB.db"
val DATABASE_NAME3 = "QnaDB.db"
lateinit var localDB: LocalDB
lateinit var addressDB: AddressDB
lateinit var qnaDB: QnaDB

class MainActivity : AppCompatActivity() {
    @RequiresApi(33)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        localDB= LocalDB(this, DATABASE_NAME1,null, DATABASE_VERSION) // SQLite 모듈 생성
        addressDB= AddressDB(this, DATABASE_NAME2,null, DATABASE_VERSION)
        qnaDB= QnaDB(this, DATABASE_NAME3,null, DATABASE_VERSION)

        binding.MaingotoMap.setOnClickListener {
            val intent = Intent(this, MapActivity::class.java)
            startActivity(intent)
        }
        binding.MaingotoQna.setOnClickListener {
            val intent = Intent(this, QnaActivity::class.java)
            startActivity(intent)
        }
        /*binding.MaingotoCommunity.setOnClickListener {
            val intent = Intent(this, CommunityActivity::class.java)
            startActivity(intent)
        }*/
        binding.MaingotoLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        binding.MaingotoSite.setOnClickListener {
            val intent = Intent(this, SiteActivity::class.java)
            startActivity(intent)
        }

        binding.MaingotoLogout.setOnClickListener {
            login = false
            ID = ""
            PW = ""
            binding.MaingotoLogout.visibility = View.GONE
            binding.MaingotoLogin.visibility = View.VISIBLE

            binding.MaingotoLogin.isEnabled = true //로그인 버튼 활성화
            binding.MaingotoLogout.isEnabled = false  //로그아웃 버튼 비활성화
            binding.MaingotoAdmin.isEnabled = false
            binding.UserID.text = "None"  //ID 초기화 (None)
        }
        binding.MaingotoAdmin.setOnClickListener {
            val intent = Intent(this, AdminActivity::class.java)
            startActivity(intent)
        }
        /*
        binding.MaingotoRegisterAddress.setOnClickListener {
            val intent = Intent(this, MapaddressActivity::class.java)
            startActivity(intent)
        } */

        if(login)  //로그인 중이라면,
        {
            binding.MaingotoLogout.visibility = View.VISIBLE
            binding.MaingotoLogin.visibility = View.GONE

            binding.MaingotoLogin.isEnabled = false  //로그인 버튼 비활성화
            binding.MaingotoLogout.isEnabled = true  //로그아웃 버튼 활성화
            binding.MaingotoAdmin.isEnabled = false
            binding.UserID.text = ID;  //ID 화면에 뜨도록

        }
        else  //로그아웃 중이라면,
        {
            binding.MaingotoLogout.visibility = View.GONE
            binding.MaingotoLogin.visibility = View.VISIBLE

            binding.MaingotoLogin.isEnabled = true //로그인 버튼 활성화
            binding.MaingotoLogout.isEnabled = false  //로그아웃 버튼 비활성화
            binding.MaingotoAdmin.isEnabled = false
            binding.UserID.text = "None"  //ID 초기화 (None)
        }

        if(ID == "uin")  //관리자 ID가 uin 이라고 가정
        {
            binding.MaingotoAdmin.isEnabled = true  //관리자 버튼 활성화
        }
    }

    override fun onDestroy() {// 엑티비티가 종료시 close
        localDB.close()
        super.onDestroy()
    }
}