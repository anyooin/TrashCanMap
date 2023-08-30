package com.example.application830

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
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
        else if(intent.getStringExtra("how") == "answer")
        {
            binding.editText.hint = "답변을 입력해주세요"
            binding.btn.text = "답변하기"

            binding.btn.setOnClickListener { view->
                //if(binding.editAddress.text.isEmpty()|| binding.editImage.drawable.equals(R.drawable.image_empty)){// 값이 전부 입력되지 않은경우
                if(binding.editText.text.isEmpty()){
                    Toast.makeText(this,"답변을 입력해주세요..", Toast.LENGTH_LONG).show()
                }else{
                    qnaDB.registerAnsw(intent.getIntExtra("num", 0), ID, binding.editText.text.toString())
                    val intent = Intent(this,QnaActivity::class.java)
                    startActivity(intent)
                }
            }
        }
        else if(intent.getStringExtra("how") == "editDelete") //내용을 불러와야해
        {
            //binding.deleteBtn.visibility = View.VISIBLE
            binding.btn.text = "삭제하기"
            var num = intent.getIntExtra("num", 0)
            binding.editText.setText(qnaDB.getListQuesText(num.toString()).quesAnsw)

//            binding.btn.setOnClickListener { view->
//                //if(binding.editAddress.text.isEmpty()|| binding.editImage.drawable.equals(R.drawable.image_empty)){// 값이 전부 입력되지 않은경우
//                if(binding.editText.text.isEmpty()){
//                    Toast.makeText(this,"질문을 입력해주세요..", Toast.LENGTH_LONG).show()
//                }else{
//                    qnaDB.registerQues(ID, binding.editText.text.toString())
//                    val intent = Intent(this,QnaActivity::class.java)
//                    startActivity(intent)
//                }
//            }
            binding.btn.setOnClickListener { view->
                    qnaDB.deleteQuesItem(intent.getIntExtra("num", 0))
                    val intent = Intent(this,QnaActivity::class.java)
                    startActivity(intent)
            }
        }
    }
}