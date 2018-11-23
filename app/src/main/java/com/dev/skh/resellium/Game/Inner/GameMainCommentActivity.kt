package com.dev.skh.resellium.Game.Inner

import android.animation.LayoutTransition
import android.annotation.SuppressLint
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.support.v4.widget.NestedScrollView
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.view.View
import com.dev.skh.resellium.Base.InnerBaseActivity
import com.dev.skh.resellium.Game.Model.CommentModel
import com.dev.skh.resellium.Game.Model.GameMainModel
import com.dev.skh.resellium.Game.WebView.GameMainWebViewActivity
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
    private var isWeb = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_game_main_comment)
        setIntent()
        setView()
        setBaseProgressBar(binding.progressBar)
        setProgressbarVisible()
        Handler().postDelayed({
            presenter.get()?.getCommentData(gameid, type)
        }, 500)
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
        binding.nestedScroll.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { _, _, scrollY, _, oldScrollY ->

            //내리는곳
            if (scrollY > oldScrollY) slideDown(binding.constComment)

            //올리기
            if (scrollY < oldScrollY) slideUp(binding.constComment)

            //최상단
            if (scrollY == 0){}
        })
    }

    override fun setError(disposable: Disposable?, message: String?) {
        this.disposable = disposable
        DLog.e("error ${message.toString()}")
        setProgressbarGone()
        showErrorToast()
    }

    override fun setReport(disposable: Disposable?) {
        this.disposable = disposable
        shortToast("신고처리가 완료되었습니다.")
    }

    override fun setReportError(disposable: Disposable?) {
        this.disposable = disposable
        showErrorToast()

    }

    private fun setConfirmDialog() {
        AlertDialog.Builder(this@GameMainCommentActivity, R.style.MyDialogTheme)
                .setTitle("신고")
                .setMessage("정말로 신고 하시겠습니까?")
                .setPositiveButton("확인") { dialog, _ ->
                    dialog.dismiss()
                    presenter.get()?.setReport(binding.model)

                }.setNegativeButton("취소") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.layout_undo -> finish()
            R.id.btn_register -> setComment()
            R.id.img_other -> popupMenu(v)
        }
    }

    private fun setComment() {

        when {
            type!!.isEmpty() -> getType()
            binding.editComment.text.toString().isNotEmpty() -> presenter.get()?.registerCommentData(type!!, binding.model?.id, binding.editComment.text.toString())
            else -> shortToast("댓글을 입력해주세요")
        }
    }

    override fun onBackPressed() {
        finish()
    }

    private fun popupMenu(v: View) {
        val popupMenu = PopupMenu(this, v)
        popupMenu.inflate(R.menu.game_menu)
        popupMenu.setOnMenuItemClickListener {
            when (it?.itemId) {
                R.id.menu_report -> {
                    setConfirmDialog()
                    true
                }
                R.id.menu_search -> {
                    setSearch()
                    true
                }
                else -> false

            }
        }
        popupMenu.show()
    }

    private fun setSearch() {

        val url = "https://www.google.co.kr/search?q=" + binding.model?.title
        val i = Intent(this, GameMainWebViewActivity::class.java)
        i.putExtra("URI", url)
        startActivity(i)
        isWeb = true
    }

    override fun setCommentData(result: MutableList<CommentModel>?, disposable: Disposable?, isScroll: Boolean) {

        result?.let {
            when (gameMainCommentAdapter) {
                null -> setAdapter(it)
                else -> gameMainCommentAdapter?.addItems(it)
            }
        }
        setProgressbarGone()
        Handler().postDelayed({
            gameMainCommentAdapter?.itemCount?.let { setViewGone(it) }
        }, 100)

        this.disposable = disposable
    }

    private fun setAdapter(it: MutableList<CommentModel>) {
        gameMainCommentAdapter = GameMainCommentAdapter(this, it)
        rv?.adapter = gameMainCommentAdapter
        setScrollListener()
    }

    private fun setViewGone(size: Int) {
        when (size) {
            0 -> binding.txtNoComment.visibility = View.VISIBLE
            else -> binding.txtNoComment.visibility = View.GONE
        }
    }

    override fun onRegisterData(disposable: Disposable?) {
        this.disposable = disposable
        refreshItem()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }

    override fun onResume() {
        super.onResume()
        if (isWeb) {
            setProgressbarGone()
            isWeb = false
        }
    }

    private fun refreshItem() {
        gameMainCommentAdapter?.clearItems()
        setProgressbarVisible()
        clearAndClose(binding.editComment)
        if (gameid!!.isEmpty() || type!!.isEmpty())
            getType()
        presenter.get()?.getCommentData(gameid, type)
    }
}
