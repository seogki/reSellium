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
import com.dev.skh.resellium.Util.DLog
import com.dev.skh.resellium.databinding.ActivityUserMainBinding

class UserMainActivity : BaseActivity(), View.OnClickListener {
    private var backKeyPressedTime: Long = 0
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
        binding.layoutBottomTab?.bottomLayoutBtn3Txt?.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icons8_user_24_fill))
        binding.layoutBottomTab?.bottomLayoutBtn3Txt?.drawable?.setColorFilter(ContextCompat.getColor(this, R.color.btmTabColor), PorterDuff.Mode.SRC_ATOP)
        binding.layoutBottomTab?.bottomLayoutText3?.setTextColor(ContextCompat.getColor(this, R.color.btmTabColor))
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
        return curFragment.tag == "UserMainFragment"
    }
}
