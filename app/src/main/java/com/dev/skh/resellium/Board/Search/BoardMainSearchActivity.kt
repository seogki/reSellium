package com.dev.skh.resellium.Board.Search

import android.annotation.SuppressLint
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.dev.skh.resellium.Base.BaseRecyclerViewAdapter
import com.dev.skh.resellium.Base.InnerBaseActivity
import com.dev.skh.resellium.Board.BoardMainAdapter
import com.dev.skh.resellium.Board.Model.BoardMainModel
import com.dev.skh.resellium.R
import com.dev.skh.resellium.Util.CustomNestedScrollListener
import com.dev.skh.resellium.Util.DLog
import com.dev.skh.resellium.databinding.ActivityBoardMainSearchBinding
import io.reactivex.disposables.Disposable
import java.lang.ref.WeakReference


class BoardMainSearchActivity : InnerBaseActivity()
        , BoardMainSearchPresenter.View
        , View.OnClickListener
        , TextView.OnEditorActionListener
        , BaseRecyclerViewAdapter.OnItemClickListener
        , CustomNestedScrollListener.OnScrollListener {


    companion object {
        fun weakRef(view: BoardMainSearchPresenter.View): WeakReference<BoardMainSearchPresenter> {
            return WeakReference(BoardMainSearchPresenter(view))
        }
    }

    private lateinit var binding: ActivityBoardMainSearchBinding
    private val weakReference by lazy { weakRef(this) }
    private var boardMainAdapter: BoardMainAdapter? = null
    private var layoutManager: LinearLayoutManager? = null
    private var rv: RecyclerView? = null
    private var isLoading: Boolean = false
    private var data: String? = null
    private var disposable: Disposable? = null
    private var searchView: SearchView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_main_search)
        binding.onClickListener = this
        setView()
        setBaseProgressBar(binding.progressBar)

    }


    private fun setView() {
        layoutManager = LinearLayoutManager(this)
        rv = setGameRv(binding.rvBoard, layoutManager!!)
        binding.editSearch.setOnEditorActionListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.img_search -> {
                onClickSearch()
            }
            R.id.layout_undo -> {
                closeKeyboard()
                finish()
            }
        }
    }


    private fun onClickSearch() {
        data = binding.editSearch.text.toString()
        if (binding.editSearch.text.toString().isNotEmpty()) {
            setData()
        } else {
            shortToast("검색어를 입력해주세요")
        }
    }

    override fun onItemClick(view: View, position: Int) {
        val data = boardMainAdapter?.getItem(position)
        setInnerIntent(data, view)
    }

    override fun onResume() {
        super.onResume()
        overridePendingTransition(0, 0)
    }

    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        var handle = false
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            onClickSearch()
            handle = true
        }
        return handle
    }

    @SuppressLint("SetTextI18n")
    private fun setData() {
        clearAndClose(binding.editSearch)
        binding.txtSearch.text = "검색어: $data"
        onRefresh()
        setProgressbarVisible()
        weakReference.get()?.getData(data)
    }

    override fun registerData(board: MutableList<BoardMainModel>?, disposable: Disposable?, isScroll: Boolean) {
        if (board != null) {
            if (boardMainAdapter == null) {
                boardMainAdapter = BoardMainAdapter(this, board)
                boardMainAdapter?.setOnItemClickListener(this)
                rv?.adapter = boardMainAdapter
            } else {
                boardMainAdapter?.addItems(board)
            }
        }

        Handler().postDelayed({
            if (boardMainAdapter?.itemCount == 0) {
                binding.txtNoComment.visibility = View.VISIBLE
            } else {
                binding.txtNoComment.visibility = View.GONE
            }
        }, 100)

        setProgressbarGone()
        this.disposable = disposable
        isLoading = false
        if (!isScroll)
            setRecyclerViewScrollbar()
    }

    override fun errorData(disposable: Disposable?, message: String?) {
        this.disposable = disposable
        DLog.e("error : $message")
        setProgressbarGone()
        showErrorToast()
    }

    private fun setRecyclerViewScrollbar() {
        binding.nestedScroll.setOnScrollChangeListener(CustomNestedScrollListener(layoutManager, this))
    }

    override fun onScrollEnd() {
        if (!isLoading) {
            isLoading = true
            setProgressbarVisible()
            val id = boardMainAdapter?.getItem(boardMainAdapter!!.itemCount - 1)?.id
            setSearchData(id)
        }
    }

    private fun setSearchData(id: String?) {
        Handler().postDelayed({
            weakReference.get()?.getScrollData(data, id)
        }, 500)
    }

    private fun onRefresh() {
        rv?.removeOnScrollListener(null)
        boardMainAdapter?.clearItems()
        isLoading = false

    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }
}
