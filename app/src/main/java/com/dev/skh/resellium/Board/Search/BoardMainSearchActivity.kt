package com.dev.skh.resellium.Board.Search

import android.annotation.SuppressLint
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.support.v4.widget.NestedScrollView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.View
import com.dev.skh.resellium.Base.InnerBaseActivity
import com.dev.skh.resellium.Board.BoardMainAdapter
import com.dev.skh.resellium.Board.Model.BoardMainModel
import com.dev.skh.resellium.Board.Model.SearchKeyModel
import com.dev.skh.resellium.R
import com.dev.skh.resellium.Util.DLog
import com.dev.skh.resellium.databinding.ActivityBoardMainSearchBinding
import io.reactivex.disposables.Disposable
import java.lang.ref.WeakReference


class BoardMainSearchActivity : InnerBaseActivity(), BoardMainSearchPresenter.View, View.OnClickListener, SearchView.OnQueryTextListener {



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

        weakReference.get()?.getKeyWordData()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_main_menu, menu)
        val menuItem = menu?.findItem(R.id.action_search)
        searchView = menuItem?.actionView as SearchView
        searchView?.setOnQueryTextListener(this)

        return true
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }

    override fun keyData(key: MutableList<SearchKeyModel>?, disposable: Disposable?) {

    }

    private fun setView() {

        rv = binding.rvBoard
        rv?.isNestedScrollingEnabled = false
        layoutManager = LinearLayoutManager(this)
        rv?.layoutManager = layoutManager
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.img_search -> {
                data = binding.editSearch.text.toString()
                if (binding.editSearch.text.toString().isNotEmpty()) {
                    setData()
                } else {
                    shortToast("검색어를 입력해주세요")
                }
            }
            R.id.layout_undo -> {
                closeKeyboard()
                finish()
            }
        }
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
                rv?.adapter = boardMainAdapter
            } else {
                boardMainAdapter?.addItems(board)
            }
        }

        setProgessbarGone()
        this.disposable = disposable
        isLoading = false
        if (!isScroll)
            setRecyclerViewScrollbar()
    }

    override fun errorData(disposable: Disposable?, message: String?) {
        this.disposable = disposable
        DLog.e("error : $message")
    }

    private fun setRecyclerViewScrollbar() {
        binding.nestedScroll.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, oldScrollY ->
            if (v.getChildAt(v.childCount - 1) != null) {
                if (scrollY >= v.getChildAt(v.childCount - 1).measuredHeight - v.measuredHeight && scrollY > oldScrollY) {

                    val visibleItemCount = layoutManager!!.childCount
                    val totalItemCount = layoutManager!!.itemCount
                    val pastVisiblesItems = layoutManager!!.findFirstVisibleItemPosition()

                    if (!isLoading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            isLoading = true
                            setProgressbarVisible()
                            val id = boardMainAdapter?.getItem(boardMainAdapter!!.itemCount - 1)?.id

                            Handler().postDelayed({
                                weakReference.get()?.getScrollData(data, id)
                            }, 500)

                        }
                    }
                }
            }
        })
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
