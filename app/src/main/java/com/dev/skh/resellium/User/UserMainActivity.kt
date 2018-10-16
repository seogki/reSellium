package com.dev.skh.resellium.User

import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.v4.content.ContextCompat
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
        setCurrentTab()
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

    private fun setCurrentTab() {
        binding.layoutBottomTab?.bottomLayoutBtn3Txt?.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.baseline_settings_black_24))
        binding.layoutBottomTab?.bottomLayoutBtn3Txt?.drawable?.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP)
        binding.layoutBottomTab?.bottomLayoutText3?.setTextColor(ContextCompat.getColor(this, R.color.white))
    }
}
