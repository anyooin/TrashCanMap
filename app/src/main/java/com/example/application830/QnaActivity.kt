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

        var datas = qnaDB.getListQues()

        binding.addQnaItem.setOnClickListener {
            val intent = Intent(this,EditQnaActivity::class.java)
            intent.putExtra("how", "add")
            startActivity(intent)
        }

        binding.quesList.layoutManager = LinearLayoutManager(this)
        binding.quesList.adapter = QnaAdapter(datas)
        binding.quesList.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
    }
}