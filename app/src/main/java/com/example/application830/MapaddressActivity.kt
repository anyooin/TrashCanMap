package com.example.application830

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.application830.databinding.ActivityMapaddressBinding
import com.example.application830.databinding.ActivityQnaBinding
import java.io.ByteArrayOutputStream

class MapaddressActivity : AppCompatActivity() {
    val DATABASE_VERSION = 1
    val DATABASE_NAME1 = "LocalDB.db"
    val DATABASE_NAME2 = "AddressDB.db"
    private lateinit var localDB: LocalDB
    private lateinit var addressDB: AddressDB
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMapaddressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        localDB= LocalDB(this, DATABASE_NAME1,null, DATABASE_VERSION) // SQLite 모듈 생성
        addressDB= AddressDB(this, DATABASE_NAME2,null, DATABASE_VERSION)

//        binding.btnRegister.setOnClickListener {
//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)
//        }

        binding.btnRegister.setOnClickListener { view->
            //if(binding.editAddress.text.isEmpty()|| binding.editImage.drawable.equals(R.drawable.image_empty)){// 값이 전부 입력되지 않은경우
            if(binding.editAddress.text.isEmpty()){
                Toast.makeText(this,"값을 전부 입력해주세요..", Toast.LENGTH_LONG).show()
            }else{
                //addressDB.registerAddress(binding.editAddress.text.toString(),drawableToByteArray(binding.editImage.drawable))
                addressDB.registerAddress(binding.editAddress.text.toString())
                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)

            }
        }
    }

    fun drawableToByteArray(drawable: Drawable?) : ByteArray? {
        val bitmapDrawable = drawable as BitmapDrawable?
        val bitmap = bitmapDrawable?.bitmap
        val stream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val byteArray = stream.toByteArray()

        return byteArray
    }
}