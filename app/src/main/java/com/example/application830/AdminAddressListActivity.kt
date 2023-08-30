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

        //var datas = addressDB.getListAll()
        var datas1 = addressDB.getList("0")

        binding.AddressList.layoutManager = LinearLayoutManager(this)
        binding.AddressList.adapter = AdminAddressAdapter(datas1)
        binding.AddressList.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))

        var datas3 = addressDB.getList("1")
        binding.AddressDeleteList.layoutManager = LinearLayoutManager(this)
        binding.AddressDeleteList.adapter = AdminAddressAdapter(datas3)
        binding.AddressDeleteList.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
    }
}