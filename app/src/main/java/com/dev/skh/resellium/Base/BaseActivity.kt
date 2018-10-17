package com.dev.skh.resellium.Base

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.PorterDuff
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.dev.skh.resellium.R
import com.dev.skh.resellium.Util.DLog

/**
 * Created by Seogki on 2018. 6. 7..
 */
@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() {

    private lateinit var toast: Toast
    private var backKeyPressedTime: Long = 0

    fun AppCompatActivity.addFragment(@IdRes frameId: Int, fragment: Fragment, AllowStateloss: Boolean, backstack: Boolean, tag: String) {
        val transaction = supportFragmentManager?.beginTransaction()?.add(frameId, fragment, tag)

        if (backstack) {
            transaction?.addToBackStack(fragment.tag)
        }

        if (AllowStateloss)
            transaction?.commitAllowingStateLoss()
        else
            transaction?.commit()
    }

    fun AppCompatActivity.beginActivity(intent: Intent) {

        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or Intent.FLAG_ACTIVITY_NO_ANIMATION)
        startActivity(intent)
    }




    private fun showGuide() {
        toast = Toast.makeText(this,
                "\'뒤로\'버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT)
        toast.show()
    }

    private fun finishToast() {
        toast.cancel()
    }

    override fun onResume() {
        super.onResume()
        overridePendingTransition(0, 0)
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
                || curFragment.tag == "GameMainFragment"
                || curFragment.tag == "BoardMainFragment"
                || curFragment.tag == "UserMainFragment"
    }


    fun setImageColor(imageView: ImageView?, textView: TextView?, imageValue: Int) {

        imageView?.setImageDrawable(ContextCompat.getDrawable(this, imageValue))
        imageView?.drawable?.setColorFilter(ContextCompat.getColor(this, R.color.accentColor), PorterDuff.Mode.SRC_ATOP)
        textView?.setTextColor(ContextCompat.getColor(this, R.color.accentColor))
    }

}