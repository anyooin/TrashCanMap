package com.example.application830

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.application830.databinding.ActivitySiteBinding

class SiteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivitySiteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.gotoMinistryofEnvironment.setOnClickListener{
            var intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://me.go.kr/search/totalSearch/search_new.jsp?q=%EC%93%B0%EB%A0%88%EA%B8%B0&targetSiteId=main"))
            startActivity(intent)
        }
    }
}