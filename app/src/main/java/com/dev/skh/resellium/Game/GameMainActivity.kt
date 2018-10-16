package com.dev.skh.resellium.Game

import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import com.dev.skh.resellium.Base.BaseActivity
import com.dev.skh.resellium.Board.BoardMainActivity
import com.dev.skh.resellium.Game.Register.GameRegisterActivity
import com.dev.skh.resellium.Main.HomeMainActivity
import com.dev.skh.resellium.R
import com.dev.skh.resellium.User.UserMainActivity
import com.dev.skh.resellium.databinding.ActivityGameMainBinding

class GameMainActivity : BaseActivity(), View.OnClickListener {
    private lateinit var binding: ActivityGameMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_game_main)
        binding.layoutBottomTab?.onClickListener = this
        binding.onClickListener = this
        addFragment(R.id.frame_layout, GameMainFragment(), false, false, "GameMainFragment")
        setCurrentTab()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.bottom_layout_btn0 -> {
                beginActivity(Intent(this, HomeMainActivity::class.java))
            }
            R.id.bottom_layout_btn2 -> {
                beginActivity(Intent(this, BoardMainActivity::class.java))
            }
            R.id.bottom_layout_btn3 -> {
                beginActivity(Intent(this, UserMainActivity::class.java))
            }
            R.id.fab_btn -> {
                startActivity(Intent(this, GameRegisterActivity::class.java))
            }
        }
    }

    private fun setCurrentTab() {
        binding.fabBtn.drawable.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP)
        binding.layoutBottomTab?.bottomLayoutBtn1Txt?.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icons8_game_controller_24))
        binding.layoutBottomTab?.bottomLayoutBtn1Txt?.drawable?.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP)
        binding.layoutBottomTab?.bottomLayoutText1?.setTextColor(ContextCompat.getColor(this, R.color.white))
    }
}
