package com.dev.skh.resellium.Board.Sub

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.PopupMenu
import android.view.View
import android.widget.Toast
import com.dev.skh.resellium.Base.InnerBaseActivity
import com.dev.skh.resellium.Board.Model.BoardMainModel
import com.dev.skh.resellium.Network.ApiCilentRx
import com.dev.skh.resellium.R
import com.dev.skh.resellium.Util.DLog
import com.dev.skh.resellium.databinding.ActivityInnerBoardMainBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class InnerBoardMainActivity : InnerBaseActivity(), View.OnClickListener {

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.layout_undo -> {
                finish()
            }
            R.id.img_other -> {
                popupMenu(v)
            }
        }
    }

    var disposable: Disposable? = null
    val client by lazy { ApiCilentRx.create() }
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
        binding.onClickListener = this
        binding.model = model as? BoardMainModel
        binding.executePendingBindings()
    }


    private fun popupMenu(v: View) {
        val popupMenu = PopupMenu(this, v)
        popupMenu.inflate(R.menu.game_menu)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item?.itemId) {
                R.id.menu_report -> {
                    setReport()
                    true
                }
                else -> {

                    false
                }
            }
        }
        popupMenu.show()
    }

    private fun setReport() {
        val item = binding.model

        disposable = client.setReport(item?.platform!!, item.id!!, item.title!!, item.date!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe({
                    Toast.makeText(this, "신고처리가 완료되었습니다.", Toast.LENGTH_SHORT).show()
                }) {
                    Toast.makeText(this, "오류가 발생하였습니다.", Toast.LENGTH_SHORT).show()
                }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }
}
