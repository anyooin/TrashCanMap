package com.example.application830

import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.application830.databinding.ItemAddressBinding

class AdminAddressAdapter(val datas : MutableList<AddressImage>)
    :RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    class MyViewHolder(val binding: ItemAddressBinding) : RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int  = datas.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
    = MyViewHolder(ItemAddressBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as MyViewHolder).binding

        binding.itemDataAddress.text = datas[position].address
        binding.itemDataImage.setImageBitmap(
            BitmapFactory.decodeByteArray(datas[position].image, 0, datas[position].image!!.size))

        binding.AdminDelete.setOnClickListener{
            addressDB.deleteItem(datas[position].num)
        }

        binding.AdminRegisterMap.setOnClickListener{
            addressDB.registerMapItem(datas[position].num)
        }

        //임시
        binding.AdminCancel.setOnClickListener{
            addressDB.cancelItem(datas[position].num)
        }

        binding.itemAddress.setOnClickListener{
            Log.d("uin", "item click")
        }

    }
}