package com.dev.skh.resellium.Board.Register

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.dev.skh.resellium.R
import com.dev.skh.resellium.databinding.ActivityBoardMainRegisterBinding
import java.lang.ref.WeakReference

class BoardMainRegisterActivity : AppCompatActivity(), BoardMainRegisterPresenter.View {


    private lateinit var binding: ActivityBoardMainRegisterBinding
    private var weakReference: WeakReference<BoardMainRegisterPresenter>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_main_register)

        setMVP()
    }

    private fun setMVP() {
        weakReference = WeakReference(BoardMainRegisterPresenter(this))
    }
}
