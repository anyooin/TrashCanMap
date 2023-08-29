package com.example.application830

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.application830.databinding.ActivityAdminAddressListBinding
import com.example.application830.databinding.ActivityMapaddressBinding

class AdminAddressListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityAdminAddressListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var datas = addressDB.getList("0")

        binding.AddressList.layoutManager = LinearLayoutManager(this)
        binding.AddressList.adapter = AdminAddressAdapter(datas)
        binding.AddressList.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
    }
}