package com.dev.skh.resellium.Board

import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import com.dev.skh.resellium.Base.BaseActivity
import com.dev.skh.resellium.Game.GameMainActivity
import com.dev.skh.resellium.Main.HomeMainActivity
import com.dev.skh.resellium.R
import com.dev.skh.resellium.User.UserMainActivity
import com.dev.skh.resellium.Util.DLog
import com.dev.skh.resellium.databinding.ActivityBoardMainBinding

class BoardMainActivity : BaseActivity(), View.OnClickListener {
    private var backKeyPressedTime: Long = 0
    private lateinit var binding: ActivityBoardMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_main)
        binding.layoutBottomTab?.onClickListener = this
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
        }
    }

    private fun setCurrentTab() {
        binding.fabBtn.drawable.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP)
        binding.layoutBottomTab?.bottomLayoutBtn2Txt?.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icons8_clipboard_24))
        binding.layoutBottomTab?.bottomLayoutBtn2Txt?.drawable?.setColorFilter(ContextCompat.getColor(this, R.color.btmTabColor), PorterDuff.Mode.SRC_ATOP)
        binding.layoutBottomTab?.bottomLayoutText2?.setTextColor(ContextCompat.getColor(this, R.color.btmTabColor))
    }

    override fun onBackPressed() {
        DLog.e("onBack Pressed" + isFirstFragment())

        if (isFirstFragment()) {

            if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
                backKeyPressedTime = System.currentTimeMillis()
                showGuide()
                return
            }
            if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
                this.finishAffinity()
                finishToast()
            }

        } else {
            super.onBackPressed()
        }

    }

    private fun isFirstFragment(): Boolean {
        val curFragment = supportFragmentManager.findFragmentById(R.id.frame_layout)
        DLog.e("current Fragment ${curFragment.tag}")
        return curFragment.tag == "BoardMainFragment"
    }
}
