package com.example.application830

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.application830.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    val DATABASE_VERSION = 1
    val DATABASE_NAME = "LocalDB.db"
    private lateinit var localDB: LocalDB
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        localDB= LocalDB(this, DATABASE_NAME,null, DATABASE_VERSION) // SQLite 모듈 생성

        binding.MaingotoMap.setOnClickListener {
            val intent = Intent(this, MapActivity::class.java)
            startActivity(intent)
        }
        binding.MaingotoQna.setOnClickListener {
            val intent = Intent(this, QnaActivity::class.java)
            startActivity(intent)
        }
        binding.MaingotoCommunity.setOnClickListener {
            val intent = Intent(this, CommunityActivity::class.java)
            startActivity(intent)
        }
        binding.MaingotoLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroy() {// 엑티비티가 종료시 close
        localDB.close()
        super.onDestroy()
    }
}