package com.dev.skh.resellium.User

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.View
import com.dev.skh.resellium.Base.BaseActivity
import com.dev.skh.resellium.Board.BoardMainActivity
import com.dev.skh.resellium.Game.GameMainActivity
import com.dev.skh.resellium.Main.HomeMainActivity
import com.dev.skh.resellium.R
import com.dev.skh.resellium.databinding.ActivityUserMainBinding

class UserMainActivity : BaseActivity(), View.OnClickListener {
    private lateinit var binding: ActivityUserMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_main)
        binding.layoutBottomTab?.onClickListener = this
        addFragment(R.id.frame_layout, UserMainFragment(), false, false, "UserMainFragment")
        setImageColor(binding.layoutBottomTab?.bottomLayoutBtn3Txt, binding.layoutBottomTab?.bottomLayoutText3, R.drawable.baseline_settings_black_24)
    }
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.bottom_layout_btn0 -> {
                beginActivity(Intent(this, HomeMainActivity::class.java))
            }
            R.id.bottom_layout_btn1 -> {
                beginActivity(Intent(this, GameMainActivity::class.java))
            }
            R.id.bottom_layout_btn2 -> {
                beginActivity(Intent(this, BoardMainActivity::class.java))
            }
        }
    }

}
