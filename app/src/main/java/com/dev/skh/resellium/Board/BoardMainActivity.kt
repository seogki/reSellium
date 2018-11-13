package com.dev.skh.resellium.Board

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.View
import com.dev.skh.resellium.Base.BaseActivity
import com.dev.skh.resellium.Board.Register.BoardMainRegisterActivity
import com.dev.skh.resellium.Game.GameMainActivity
import com.dev.skh.resellium.Main.HomeMainActivity
import com.dev.skh.resellium.R
import com.dev.skh.resellium.databinding.ActivityBoardMainBinding

class BoardMainActivity : BaseActivity(), View.OnClickListener {
    private lateinit var binding: ActivityBoardMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_main)
        setAdView(binding.adView)
        binding.layoutBottomTab?.onClickListener = this
        binding.activity = this
        addFragment(R.id.frame_layout, BoardMainFragment(), false, false, "BoardMainFragment")
        setImageColor(binding.layoutBottomTab?.bottomLayoutBtn2Txt, binding.layoutBottomTab?.bottomLayoutText2, R.drawable.icons8_clipboard_24)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.bottom_layout_btn0 -> {
                beginActivity(Intent(this, HomeMainActivity::class.java))
            }
            R.id.bottom_layout_btn1 -> {
                beginActivity(Intent(this, GameMainActivity::class.java))
            }

        }
    }

    fun registerIntent() {
        startActivity(Intent(this, BoardMainRegisterActivity::class.java))
    }
}
