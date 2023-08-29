package com.example.application830

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.application830.databinding.ActivityAdminBinding
import com.example.application830.databinding.ActivityCommunityBinding

class AdminActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.AdmingotoAddressList.setOnClickListener {
            val intent = Intent(this, AdminAddressListActivity::class.java)
            startActivity(intent)
        }

        binding.AdmingotoMain.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}