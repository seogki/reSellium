package com.dev.skh.resellium.Board.Sub

import android.animation.LayoutTransition
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.support.v4.widget.NestedScrollView
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
        setProgressbarGone()
        showErrorToast()
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
        setBaseProgressBar(binding.progressBar)
        Handler().postDelayed({
            presenter.get()?.getCommentData(id)
        }, 500)

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

    override fun onBackPressed() {
        finish()
    }

    private fun setComment() {
        if (binding.editComment.text.toString().isNotEmpty()) {
            presenter.get()?.registerCommentData(binding.model?.id, binding.editComment.text.toString())
        } else {
            shortToast("댓글을 입력해주세요")
        }
    }


    private fun popupMenu(v: View) {
        val popupMenu = PopupMenu(this, v)
        popupMenu.inflate(R.menu.board_menu)
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
                setScrollListener()
            } else
                gameMainCommentAdapter?.addItems(result)
        }
        setProgressbarGone()
        Handler().postDelayed({
            if (gameMainCommentAdapter?.itemCount == 0) {
                binding.txtNoComment.visibility = View.VISIBLE
            } else {
                binding.txtNoComment.visibility = View.GONE
            }
        }, 100)

    }

    private fun setScrollListener() {
        binding.constComment.layoutTransition.enableTransitionType(LayoutTransition.DISAPPEARING)
        binding.constComment.layoutTransition.enableTransitionType(LayoutTransition.APPEARING)
        binding.nestedScroll.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
            if (scrollY > oldScrollY) {
                //내리는곳
                slideDown(binding.constComment)
            }
            if (scrollY < oldScrollY) {
                //올리기
                slideUp(binding.constComment)
            }
            if (scrollY == 0) {
                //최상단
            }
        })
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
        setProgressbarVisible()
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
