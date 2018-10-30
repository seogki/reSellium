package com.dev.skh.resellium.Board.Sub

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.view.View
import com.dev.skh.resellium.Base.InnerBaseActivity
import com.dev.skh.resellium.Board.Model.BoardMainModel
import com.dev.skh.resellium.Game.Inner.GameMainCommentAdapter
import com.dev.skh.resellium.Game.Model.CommentModel
import com.dev.skh.resellium.R
import com.dev.skh.resellium.Util.DLog
import com.dev.skh.resellium.databinding.ActivityInnerBoardMainBinding
import io.reactivex.disposables.Disposable
import java.lang.ref.WeakReference

class InnerBoardMainActivity : InnerBaseActivity(), View.OnClickListener, InnerBoardMainPresenter.View {

    override fun setError(disposable: Disposable?, message: String?) {
        this.disposable = disposable
        DLog.e("error ${message.toString()}")
    }

    companion object {
        fun weakRef(view: InnerBoardMainPresenter.View): WeakReference<InnerBoardMainPresenter> {
            return WeakReference(InnerBoardMainPresenter(view))
        }
    }

    var disposable: Disposable? = null
    private val presenter by lazy { weakRef(this) }
    private var id: String? = ""
    private var linearLayoutManager: LinearLayoutManager? = null
    private var gameMainCommentAdapter: GameMainCommentAdapter? = null
    private var rv: RecyclerView? = null
    private lateinit var binding: ActivityInnerBoardMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_inner_board_main)
        setIntent()
        setView()
        presenter.get()?.getCommentData(id)
    }

    private fun setIntent() {
        val model = intent.getSerializableExtra("data")

        if (model == null)
            parent?.intent?.getSerializableExtra("data")

        binding.layoutAppbar?.title = "리뷰"
        binding.layoutAppbar?.layoutAdd?.visibility = View.GONE
        binding.layoutAppbar?.onClickListener = this
        binding.onClickListener = this
        binding.model = model as? BoardMainModel
        binding.executePendingBindings()
        id = binding.model?.id
    }

    private fun setView() {
        linearLayoutManager = LinearLayoutManager(this)
        rv = setCommentRv(binding.rvComment, linearLayoutManager!!)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.layout_undo -> finish()
            R.id.img_other -> popupMenu(v)
            R.id.btn_register -> setComment()
        }
    }

    private fun setComment() {
        if (binding.editComment.text.toString().isNotEmpty()) {
            presenter.get()?.registerCommentData(binding.model?.id, binding.editComment.text.toString())
        } else {
            shortToast("댓글을 입력부탁드립니다.")
        }
    }


    private fun popupMenu(v: View) {
        val popupMenu = PopupMenu(this, v)
        popupMenu.inflate(R.menu.game_menu)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item?.itemId) {
                R.id.menu_report -> {
                    presenter.get()?.setReport(binding.model)
                    true
                }
                else -> {

                    false
                }
            }
        }
        popupMenu.show()
    }



    override fun setCommentData(result: MutableList<CommentModel>?, disposable: Disposable?, isScroll: Boolean) {
        this.disposable = disposable
        if (result != null) {
            if (gameMainCommentAdapter == null) {
                gameMainCommentAdapter = GameMainCommentAdapter(this, result)
                rv?.adapter = gameMainCommentAdapter
            } else {
                gameMainCommentAdapter?.addItems(result)
            }
        }
    }

    override fun onRegisterData(disposable: Disposable?) {
        this.disposable = disposable
        refreshItem()
    }

    override fun setReport(disposable: Disposable?) {
        this.disposable = disposable
        shortToast("신고처리가 완료되었습니다.")
    }

    override fun setReportError(disposable: Disposable?) {
        this.disposable = disposable
        shortToast("오류가 발생하였습니다.")
    }


    private fun refreshItem() {
        gameMainCommentAdapter?.clearItems()
        clearAndClose(binding.editComment)
        if (id!!.isEmpty())
            id = binding.model?.id
        presenter.get()?.getCommentData(id)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }
}
