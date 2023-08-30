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
        binding.gotoBukgu.setOnClickListener{
            var intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.buk.daegu.kr/index.do?menu_id=00001794"))
            startActivity(intent)
        }
        binding.gotoSeogu.setOnClickListener{
            var intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.dgs.go.kr/dgs/minwon/page.php?mnu_uid=11026"))
            startActivity(intent)
        }
        binding.gotoNamgu.setOnClickListener{
            var intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.nam.daegu.kr/index.do?menu_id=00000775"))
            startActivity(intent)
        }
        binding.gotoJunggu.setOnClickListener{
            var intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.jung.daegu.kr/new/pages/information/page.html?mc=2307"))
            startActivity(intent)
        }
        binding.gotoDalseong.setOnClickListener{
            var intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.dalseong.daegu.kr/index.do?menu_id=00000355&servletPath=%2Findex.do"))
            startActivity(intent)
        }
        binding.gotoDalseo.setOnClickListener{
            var intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.dalseo.daegu.kr/index.do?menu_id=00001361&servletPath=%2Findex.do"))
            startActivity(intent)
        }

    }
}