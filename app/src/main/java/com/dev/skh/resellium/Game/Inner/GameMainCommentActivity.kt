package com.dev.skh.resellium.Game.Inner

import android.animation.LayoutTransition
import android.annotation.SuppressLint
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.widget.NestedScrollView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.view.View
import com.dev.skh.resellium.Base.InnerBaseActivity
import com.dev.skh.resellium.Game.Model.CommentModel
import com.dev.skh.resellium.Game.Model.GameMainModel
import com.dev.skh.resellium.R
import com.dev.skh.resellium.Util.DLog
import com.dev.skh.resellium.databinding.ActivityGameMainCommentBinding
import io.reactivex.disposables.Disposable
import java.lang.ref.WeakReference


class GameMainCommentActivity : InnerBaseActivity(), GameMainCommentPresenter.View, View.OnClickListener {


    companion object {
        fun weakRef(view: GameMainCommentPresenter.View): WeakReference<GameMainCommentPresenter> {
            return WeakReference(GameMainCommentPresenter(view))
        }
    }

    private val presenter by lazy { GameMainCommentActivity.weakRef(this) }
    lateinit var binding: ActivityGameMainCommentBinding
    private var linearLayoutManager: LinearLayoutManager? = null
    private var gameMainCommentAdapter: GameMainCommentAdapter? = null
    private var rv: RecyclerView? = null
    private var type: String? = ""
    private var gameid: String? = ""
    private var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_game_main_comment)
        setIntent()
        setView()
        presenter.get()?.getCommentData(gameid, type)

    }

    @SuppressLint("SetTextI18n")
    private fun setIntent() {
        val model = intent.getSerializableExtra("data")

        if (model == null)
            parent?.intent?.getSerializableExtra("data")

        binding.layoutAppbar?.layoutAdd?.visibility = View.GONE
        binding.layoutAppbar?.onClickListener = this
        binding.onClickListener = this
        binding.model = model as? GameMainModel
        binding.executePendingBindings()
        binding.layoutAppbar?.title = binding.model?.which
        getType()

    }

    private fun setView() {
        linearLayoutManager = LinearLayoutManager(this)
        rv = setCommentRv(binding.rvComment, linearLayoutManager!!)
    }

    private fun getType() {
        when (binding.model?.platform) {
            "PS" -> type = "0"
            "XBOX" -> type = "1"
            "SWITCH" -> type = "2"
        }
        gameid = binding.model?.id
    }


    private fun setScrollListener() {
        binding.constComment.layoutTransition.enableTransitionType(LayoutTransition.DISAPPEARING)
        binding.constComment.layoutTransition.enableTransitionType(LayoutTransition.APPEARING)
        binding.nestedScroll.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY > oldScrollY) {
                //내리는곳
                slideDown(binding.constComment)
//                binding.constComment.visibility = View.INVISIBLE
            }
            if (scrollY < oldScrollY) {
                //올리기
                slideUp(binding.constComment)
//                binding.constComment.visibility = View.VISIBLE
            }
            if (scrollY == 0) {
                //최상단
            }
        })
    }

    override fun setError(disposable: Disposable?, message: String?) {
        this.disposable = disposable
        DLog.e("error ${message.toString()}")
    }

    override fun setReport(disposable: Disposable?) {
        this.disposable = disposable
        shortToast("신고처리가 완료되었습니다.")
    }

    override fun setReportError(disposable: Disposable?) {
        this.disposable = disposable
        shortToast("오류가 발생하였습니다.")
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.layout_undo -> finish()
            R.id.btn_register -> setComment()
            R.id.img_other -> popupMenu(v)
        }
    }

    private fun setComment() {
        if (type!!.isEmpty())
            getType()

        if (binding.editComment.text.toString().isNotEmpty()) {
            presenter.get()?.registerCommentData(type!!, binding.model?.id, binding.editComment.text.toString())
        } else {
            shortToast("댓글을 입력해주세요")
        }
    }

    override fun onBackPressed() {
        finish()
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
        if (result != null) {
            if (gameMainCommentAdapter == null) {
                gameMainCommentAdapter = GameMainCommentAdapter(this, result)
                rv?.adapter = gameMainCommentAdapter
                setScrollListener()
            } else {
                gameMainCommentAdapter?.addItems(result)
            }

        }

        if (this.gameMainCommentAdapter?.itemCount == 0) {
            binding.txtNoComment.visibility = View.VISIBLE
        } else {
            binding.txtNoComment.visibility = View.GONE
        }

        this.disposable = disposable

    }

    override fun onRegisterData(disposable: Disposable?) {
        this.disposable = disposable
        refreshItem()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }

    private fun refreshItem() {
        gameMainCommentAdapter?.clearItems()
        clearAndClose(binding.editComment)
        if (gameid!!.isEmpty() || type!!.isEmpty())
            getType()
        presenter.get()?.getCommentData(gameid, type)
    }
}
