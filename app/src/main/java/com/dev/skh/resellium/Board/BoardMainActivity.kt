package com.dev.skh.resellium.Board

import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import com.dev.skh.resellium.Base.BaseActivity
import com.dev.skh.resellium.Board.Register.BoardMainRegisterActivity
import com.dev.skh.resellium.Game.GameMainActivity
import com.dev.skh.resellium.Main.HomeMainActivity
import com.dev.skh.resellium.R
import com.dev.skh.resellium.User.UserMainActivity
import com.dev.skh.resellium.databinding.ActivityBoardMainBinding

class BoardMainActivity : BaseActivity(), View.OnClickListener {
    private lateinit var binding: ActivityBoardMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_main)
        binding.layoutBottomTab?.onClickListener = this
        binding.onClickListener = this
        addFragment(R.id.frame_layout, BoardMainFragment(), false, false, "BoardMainFragment")
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
            R.id.bottom_layout_btn3 -> {
                beginActivity(Intent(this, UserMainActivity::class.java))
            }
            R.id.fab_btn -> {
                startActivity(Intent(this, BoardMainRegisterActivity::class.java))
            }
        }
    }

    private fun setCurrentTab() {
        binding.fabBtn.drawable.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP)
        binding.layoutBottomTab?.bottomLayoutBtn2Txt?.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icons8_clipboard_24))
        binding.layoutBottomTab?.bottomLayoutBtn2Txt?.drawable?.setColorFilter(ContextCompat.getColor(this, R.color.btmTabColor), PorterDuff.Mode.SRC_ATOP)
        binding.layoutBottomTab?.bottomLayoutText2?.setTextColor(ContextCompat.getColor(this, R.color.btmTabColor))
    }
}
