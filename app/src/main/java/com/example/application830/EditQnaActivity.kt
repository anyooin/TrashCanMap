package com.example.application830

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.application830.databinding.ActivityEditQnaBinding
import com.example.application830.databinding.ActivityMapaddressBinding

class EditQnaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityEditQnaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(intent.getStringExtra("how") == "add")
        {
            binding.btn.text = "질문하기"

            binding.btn.setOnClickListener { view->
                //if(binding.editAddress.text.isEmpty()|| binding.editImage.drawable.equals(R.drawable.image_empty)){// 값이 전부 입력되지 않은경우
                if(binding.editText.text.isEmpty()){
                    Toast.makeText(this,"질문을 입력해주세요..", Toast.LENGTH_LONG).show()
                }else{
                    qnaDB.registerQues(ID, binding.editText.text.toString())
                    val intent = Intent(this,QnaActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}