package com.example.application830

import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.application830.databinding.ItemAddressBinding
import com.example.application830.databinding.ItemQnaBinding

class QnaAdapter (val datas : MutableList<QnaDatas>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    class MyViewHolder(val binding: ItemQnaBinding) : RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int  = datas.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
            = QnaAdapter.MyViewHolder(
        ItemQnaBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as QnaAdapter.MyViewHolder).binding

        //binding.itemDataQues.text = datas[position].question

        binding.itemCheck.setOnClickListener{
            //인텐트 넘겨주기
        }

//        binding.itemDelete.setOnClickListener{
//            qnaDB.deleteItem(datas[position].num)
//        }


        binding.itemAddress.setOnClickListener{
            Log.d("uin", "item click")
        }
    }
}