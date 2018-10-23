package com.dev.skh.resellium.User

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.dev.skh.resellium.R
import com.dev.skh.resellium.databinding.ActivityUserMainBinding
import java.lang.ref.WeakReference


class UserMainActivity : AppCompatActivity(), View.OnClickListener, UserMainPresenter.View {


    companion object {
        fun weakRef(view: UserMainPresenter.View): WeakReference<UserMainPresenter> {
            return WeakReference(UserMainPresenter(view))
        }
    }

    private val weakReference by lazy { UserMainActivity.weakRef(this) }
    private lateinit var binding: ActivityUserMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_main)
        binding.onClickListener = this
        binding.layoutAppbar?.onClickListener = this
        binding.layoutAppbar?.title = "설정"
        binding.layoutAppbar?.layoutAdd?.visibility = View.GONE
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.layout_undo -> finish()
            R.id.txt_mail -> setEmail()
        }
    }


    private fun setEmail() {
        val email = Intent(Intent.ACTION_SEND)
        email.type = "plain/text"
        val address = arrayOf("seogkihongdroid@gmail.com")
        email.putExtra(Intent.EXTRA_EMAIL, address)
        email.putExtra(Intent.EXTRA_SUBJECT, "문의합니다.")
//        email.putExtra(Intent.EXTRA_TEXT, "보낼 email 내용을 미리 적어 놓을 수 있습니다.\n")
        startActivity(email)
    }

}
