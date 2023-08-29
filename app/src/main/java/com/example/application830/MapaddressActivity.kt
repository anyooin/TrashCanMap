package com.example.application830

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.example.application830.databinding.ActivityMapaddressBinding
import com.example.application830.databinding.ActivityQnaBinding
import java.io.ByteArrayOutputStream

class MapaddressActivity : AppCompatActivity() {
    val DATABASE_VERSION = 1
    val DATABASE_NAME1 = "LocalDB.db"
    val DATABASE_NAME2 = "AddressDB.db"
    private lateinit var localDB: LocalDB
    private lateinit var addressDB: AddressDB

    private var uri: Uri? = null;
    private var uriString : String = R.drawable.image_empty.toString()
    lateinit var binding : ActivityMapaddressBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapaddressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        localDB= LocalDB(this, DATABASE_NAME1,null, DATABASE_VERSION) // SQLite 모듈 생성
        addressDB= AddressDB(this, DATABASE_NAME2,null, DATABASE_VERSION)

        binding.editImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            activityResult.launch(intent)
        }

        binding.btnRegister.setOnClickListener { view->
            //if(binding.editAddress.text.isEmpty()|| binding.editImage.drawable.equals(R.drawable.image_empty)){// 값이 전부 입력되지 않은경우
            if(binding.editAddress.text.isEmpty()){
                Toast.makeText(this,"값을 전부 입력해주세요..", Toast.LENGTH_LONG).show()
            }else{
                addressDB.registerAddressImage(binding.editAddress.text.toString(),drawableToByteArray(binding.editImage.drawable))
                //addressDB.registerAddress(binding.editAddress.text.toString())
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

    private val activityResult:ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) {

        if(it.resultCode == RESULT_OK && it.data != null) {
            uri = it.data!!.data!!
            //uriString = uri.toString()

            //binding.editImage.setImageURI(Uri.parse(uriString))
            //binding.editImage.tag = uriString

            Glide.with(this).load(uri).into(binding.editImage)
        }
    }
}