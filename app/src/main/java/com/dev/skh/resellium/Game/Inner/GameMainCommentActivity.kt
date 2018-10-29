package com.dev.skh.resellium.Game.Inner

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.dev.skh.resellium.Base.InnerBaseActivity
import com.dev.skh.resellium.Game.Model.GameCommentModel
import com.dev.skh.resellium.Game.Model.GameMainModel
import com.dev.skh.resellium.R
import com.dev.skh.resellium.Util.DLog
import com.dev.skh.resellium.databinding.ActivityGameMainCommentBinding
import io.reactivex.disposables.Disposable
import java.lang.ref.WeakReference

class GameMainCommentActivity : InnerBaseActivity(), GameMainCommentPresenter.View, View.OnClickListener {


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.layout_undo -> finish()
            R.id.btn_register -> setComment()
        }
    }

    private fun setComment() {
        if (type!!.isEmpty())
            getType()

        if (binding.editComment.text.toString().isNotEmpty()) {
            presenter.get()?.registerCommentData(type!!, binding.model?.id, binding.editComment.text.toString())
        } else {
            shortToast("댓글을 입력부탁드립니다.")
        }
    }

    companion object {
        fun weakRef(view: GameMainCommentPresenter.View): WeakReference<GameMainCommentPresenter> {
            return WeakReference(GameMainCommentPresenter(view))
        }
    }

    private val presenter by lazy { GameMainCommentActivity.weakRef(this) }
    lateinit var binding: ActivityGameMainCommentBinding
    private var linearLayoutManager: LinearLayoutManager? = null
    private var gameMainCommentAdapter: GameMainCommentAdapter? = null
    private var type: String? = ""
    private var gameid: String? = ""
    private var disposable: Disposable? = null
    private var rv: RecyclerView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_game_main_comment)
        setIntent()
        setView()
        presenter.get()?.getCommentData(gameid,type)

    }

    private fun setIntent() {
        val model = intent.getSerializableExtra("data")

        if (model == null)
            parent?.intent?.getSerializableExtra("data")

        DLog.e("model $model")
        binding.layoutAppbar?.title = "게임"
        binding.layoutAppbar?.layoutAdd?.visibility = View.GONE
        binding.layoutAppbar?.onClickListener = this
        binding.onClickListener = this
        binding.model = model as? GameMainModel

        binding.executePendingBindings()
        getType()
        gameid = binding.model?.id
    }

    private fun setView() {
        linearLayoutManager = LinearLayoutManager(this)
        rv = setGameRv(binding.rvComment, linearLayoutManager!!)
    }

    private fun getType() {
        when (binding.model?.platform) {
            "PS" -> type = "0"
            "XBOX" -> type = "1"
            "SWITCH" -> type = "2"
        }
    }

    override fun setError(disposable: Disposable?, message: String?) {
        this.disposable = disposable
        DLog.e("error ${message.toString()}")
    }

    override fun setCommentData(result: MutableList<GameCommentModel>?, disposable: Disposable?, isScroll: Boolean) {
        if (result != null) {
            if (gameMainCommentAdapter == null) {
                gameMainCommentAdapter = GameMainCommentAdapter(this, result)
                rv?.adapter = gameMainCommentAdapter
            } else {
                gameMainCommentAdapter?.addItems(result)
            }
        }
        this.disposable = disposable
    }

    override fun onRegisterData(disposable: Disposable?) {
        this.disposable = disposable
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }
}
