package com.dev.skh.resellium.Main

import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.WindowManager
import com.dev.skh.resellium.Base.BaseActivity
import com.dev.skh.resellium.Board.BoardMainActivity
import com.dev.skh.resellium.Game.GameMainActivity
import com.dev.skh.resellium.R
import com.dev.skh.resellium.User.UserMainActivity
import com.dev.skh.resellium.Util.DLog
import com.dev.skh.resellium.databinding.ActivityHomeMainBinding




class HomeMainActivity : BaseActivity(), View.OnClickListener {
    private var backKeyPressedTime: Long = 0
    private lateinit var binding: ActivityHomeMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home_main)
        binding.layoutBottomTab?.onClickListener = this
        addFragment(R.id.frame_layout, HomeMainFragment(), false, false, "HomeMainFragment")
        setCurrentTab()
//        setStatusBarTranslucent(true)
    }

    private fun setStatusBarTranslucent(makeTranslucent: Boolean) {
        if (makeTranslucent) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
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

    private fun setCurrentTab() {
        binding.layoutBottomTab?.bottomLayoutBtn0Txt?.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icons8_home_24))
        binding.layoutBottomTab?.bottomLayoutBtn0Txt?.drawable?.setColorFilter(ContextCompat.getColor(this, R.color.btmTabColor), PorterDuff.Mode.SRC_ATOP)
        binding.layoutBottomTab?.bottomLayoutText0?.setTextColor(ContextCompat.getColor(this, R.color.btmTabColor))
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
        return curFragment.tag == "HomeMainFragment"
    }
}
