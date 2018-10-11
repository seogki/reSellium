package com.dev.skh.resellium.Board.Search

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.dev.skh.resellium.R
import com.dev.skh.resellium.databinding.ActivityBoardMainSearchBinding
import java.lang.ref.WeakReference

class BoardMainSearchActivity : AppCompatActivity(), BoardMainSearchPresenter.View {

    private lateinit var binding: ActivityBoardMainSearchBinding
    private var weakReference: WeakReference<BoardMainSearchPresenter>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_main_search)

        setMVP()
    }

    private fun setMVP() {
        weakReference = WeakReference(BoardMainSearchPresenter(this))
    }
}
