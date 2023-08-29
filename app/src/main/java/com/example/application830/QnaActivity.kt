package com.example.application830

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.application830.databinding.ActivityQnaBinding

class QnaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityQnaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var datas = addressDB.getListAll() //임시

        binding.qnaList.layoutManager = LinearLayoutManager(this)
        binding.qnaList.adapter = QnaAdapter(datas)
        binding.qnaList.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
    }
}