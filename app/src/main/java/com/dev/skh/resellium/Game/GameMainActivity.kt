package com.dev.skh.resellium.Game

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.View
import com.dev.skh.resellium.Base.BaseActivity
import com.dev.skh.resellium.Board.BoardMainActivity
import com.dev.skh.resellium.Game.Register.GameRegisterActivity
import com.dev.skh.resellium.Main.HomeMainActivity
import com.dev.skh.resellium.R
import com.dev.skh.resellium.databinding.ActivityGameMainBinding

class GameMainActivity : BaseActivity(), View.OnClickListener {
    private lateinit var binding: ActivityGameMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_game_main)
        setAdView(binding.adView)
        binding.layoutBottomTab?.onClickListener = this
        binding.activity = this

        addFragment(R.id.frame_layout, GameMainFragment(), false, false, "GameMainFragment")
        setImageColor(binding.layoutBottomTab?.bottomLayoutBtn1Txt, binding.layoutBottomTab?.bottomLayoutText1, R.drawable.icons8_game_controller_24)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.bottom_layout_btn0 -> {
                beginActivity(Intent(this, HomeMainActivity::class.java))
            }
            R.id.bottom_layout_btn2 -> {
                beginActivity(Intent(this, BoardMainActivity::class.java))
            }

        }
    }
    fun registerIntent(){
        startActivity(Intent(this, GameRegisterActivity::class.java))
    }
}
