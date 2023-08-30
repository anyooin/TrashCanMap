package com.example.application830

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.application830.databinding.ActivityQnaBinding
import com.example.application830.databinding.ActivityQnaCheckBinding

class QnaCheckActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityQnaCheckBinding.inflate(layoutInflater)
        setContentView(binding.root)


        var num = intent.getIntExtra("quesnum", 0)
        var datas = qnaDB.getListAnsw(num.toString())

        println(num)

        binding.checkText.setText(qnaDB.getListQuesText(num.toString()).quesAnsw)
        binding.checkUser.setText(qnaDB.getListQuesText(num.toString()).id)

        binding.checkText.setOnClickListener {
            val intent = Intent(this,EditQnaActivity::class.java)
            intent.putExtra("how", "editDelete")
            intent.putExtra("num", num)
            startActivity(intent)
        }

        binding.answerBtn.setOnClickListener {
            val intent = Intent(this,EditQnaActivity::class.java)
            intent.putExtra("how", "answer")
            intent.putExtra("num", num)
            startActivity(intent)
        }

        binding.answList.layoutManager = LinearLayoutManager(this)
        binding.answList.adapter = QnaCheckAdapter(datas)
        binding.answList.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
    }
}