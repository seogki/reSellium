package com.dev.skh.resellium.Board.Sub

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.View
import com.dev.skh.resellium.Base.InnerBaseActivity
import com.dev.skh.resellium.Board.Model.BoardMainModel
import com.dev.skh.resellium.R
import com.dev.skh.resellium.Util.DLog
import com.dev.skh.resellium.databinding.ActivityInnerBoardMainBinding

class InnerBoardMainActivity : InnerBaseActivity(), View.OnClickListener {

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.layout_undo -> {
                finish()
            }
        }
    }


    private lateinit var binding: ActivityInnerBoardMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_inner_board_main)


        val model = intent.getSerializableExtra("data")

        if (model == null)
            parent?.intent?.getSerializableExtra("data")

        DLog.e("model $model")
        binding.layoutAppbar?.title = "리뷰"
        binding.layoutAppbar?.layoutAdd?.visibility = View.GONE
        binding.layoutAppbar?.onClickListener = this
        binding.model = model as? BoardMainModel
        binding.executePendingBindings()
    }
}
