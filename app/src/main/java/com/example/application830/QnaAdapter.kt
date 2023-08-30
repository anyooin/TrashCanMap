package com.example.application830

import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.application830.databinding.ItemAddressBinding
import com.example.application830.databinding.ItemQnaBinding

class QnaAdapter (val datas : MutableList<IDQuesAnsw>)
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

        binding.itemDataQues.text = datas[position].quesAnsw
        binding.itemDataUser.text = datas[position].id

        binding.itemQna.setOnClickListener{
            val intent = Intent(holder.itemView?.context,QnaCheckActivity::class.java)
            intent.putExtra("quesnum", datas[position].num)
            ContextCompat.startActivity(holder.itemView.context, intent, null)
            //startActivity(intent)
        }

//        binding.itemDelete.setOnClickListener{
//            qnaDB.deleteQuesItem(datas[position].num)
//        }
//        binding.itemEdit.setOnClickListener{
//            val intent = Intent(holder.itemView?.context,EditQnaActivity::class.java)
//            intent.putExtra("how", "edit")
//            ContextCompat.startActivity(holder.itemView.context, intent, null)
//        }

    }
}