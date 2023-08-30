package com.example.application830

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.application830.databinding.ItemQnaBinding
import com.example.application830.databinding.ItemQnaCheckBinding

class QnaCheckAdapter (val datas : MutableList<IDQuesAnsw>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    class MyViewHolder(val binding: ItemQnaCheckBinding) : RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int  = datas.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
            = QnaCheckAdapter.MyViewHolder(
        ItemQnaCheckBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as QnaCheckAdapter.MyViewHolder).binding

        binding.itemDataQues.text = datas[position].quesAnsw
        binding.itemDataUser.text = datas[position].id

//        binding.itemQna.setOnClickListener{
//            val intent = Intent(holder.itemView?.context,QnaCheckActivity::class.java)
//            intent.putExtra("quesChecknum", datas[position].num)
//            ContextCompat.startActivity(holder.itemView.context, intent, null)
//            //startActivity(intent)
//        }

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