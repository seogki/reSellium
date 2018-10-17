package com.dev.skh.resellium.Main

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.View
import com.dev.skh.resellium.Base.BaseActivity
import com.dev.skh.resellium.Board.BoardMainActivity
import com.dev.skh.resellium.Game.GameMainActivity
import com.dev.skh.resellium.R
import com.dev.skh.resellium.User.UserMainActivity
import com.dev.skh.resellium.databinding.ActivityHomeMainBinding




class HomeMainActivity : BaseActivity(), View.OnClickListener {
    private lateinit var binding: ActivityHomeMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home_main)
        binding.layoutBottomTab?.onClickListener = this
        addFragment(R.id.frame_layout, HomeMainFragment(), false, false, "HomeMainFragment")
        setImageColor(binding.layoutBottomTab?.bottomLayoutBtn0Txt, binding.layoutBottomTab?.bottomLayoutBtn0, R.drawable.icons8_home_24)
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.bottom_layout_btn1 -> {
                beginActivity(Intent(this, GameMainActivity::class.java))
            }
            R.id.bottom_layout_btn2 -> {
                beginActivity(Intent(this, BoardMainActivity::class.java))
            }
            R.id.bottom_layout_btn3 -> {
                beginActivity(Intent(this, UserMainActivity::class.java))
            }
        }
    }
}
