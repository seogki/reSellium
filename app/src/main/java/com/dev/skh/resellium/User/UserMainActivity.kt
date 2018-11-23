package com.dev.skh.resellium.User

import android.content.ActivityNotFoundException
import android.content.Intent
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.dev.skh.resellium.R
import com.dev.skh.resellium.databinding.ActivityUserMainBinding


class UserMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_main)
        binding.activity = this
        binding.layoutAppbar?.title = "설정"
        binding.layoutAppbar?.layoutAdd?.visibility = View.GONE
        binding.layoutAppbar?.layoutUndo?.setOnClickListener { finish() }
    }

    fun setReview() {
        val appPackageName = packageName // getPackageName() from Context or Activity object
        try {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName")))
        } catch (anfe: ActivityNotFoundException) {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")))
        }
    }


    fun setEmail() {
        val email = Intent(Intent.ACTION_SEND)
                .apply {
                    type = "plain/text"
                    putExtra(Intent.EXTRA_EMAIL, arrayOf("seogkihongdroid@gmail.com"))
                    putExtra(Intent.EXTRA_SUBJECT, "앱에 관하여 문의합니다.")
                    putExtra(Intent.EXTRA_TEXT, "최소한의 정보가 필요함으로 모델명을 입력부탁드립니다.\n핸드폰 모델명: \n문의내용: ")
                }
        startActivity(email)
    }

}
