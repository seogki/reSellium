package com.dev.skh.resellium.Splash

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import com.dev.skh.resellium.Main.HomeMainActivity
import com.dev.skh.resellium.R
import com.dev.skh.resellium.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)


        Handler().postDelayed({
            startActivity(Intent(this, HomeMainActivity::class.java))
        }, 500)

    }


}
