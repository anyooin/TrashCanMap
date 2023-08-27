package com.example.application830

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.application830.databinding.ActivityLoginBinding
import com.example.application830.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    val DATABASE_VERSION = 1
    val DATABASE_NAME = "LocalDB.db"
    private lateinit var localDB: LocalDB
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        localDB= LocalDB(this, DATABASE_NAME,null, DATABASE_VERSION) // SQLite 모듈 생성

        binding.btnRegister.setOnClickListener { view->
            if(binding.editId.text.isEmpty()||binding.editPw.text.isEmpty()||binding.editPwCheck.text.isEmpty()){// 값이 전부 입력되지 않은경우
                Toast.makeText(this,"값을 전부 입력해주세요..",Toast.LENGTH_LONG).show()
            }else{
                if(binding.editPw.text.toString().equals(binding.editPwCheck.text.toString())){//패스워드/패스워드 확인이 일치
                    if(localDB.checkIdExist(binding.editId.text.toString())){// 존재하는 아이디
                        Toast.makeText(this,"아이디가 이미 존재합니다.",Toast.LENGTH_LONG).show()
                    }else{// 회원가입
                        localDB.registerUser(binding.editId.text.toString(),binding.editPw.text.toString())
                        val intent = Intent(this,LoginActivity::class.java)
                        startActivity(intent)
                    }
                }else{ // 패스워드/패스워드 확인이 일치하지 않음
                    Toast.makeText(this,"패스워드가 틀렸습니다.",Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}