package com.dev.skh.resellium.Board.Search

import android.content.Context
import android.databinding.DataBindingUtil
import android.graphics.PorterDuff
import android.os.Bundle
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.support.v4.widget.NestedScrollView
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import android.widget.Toast
import com.dev.skh.resellium.Board.BoardMainAdapter
import com.dev.skh.resellium.Board.Model.BoardMainModel
import com.dev.skh.resellium.R
import com.dev.skh.resellium.Util.DLog
import com.dev.skh.resellium.databinding.ActivityBoardMainSearchBinding
import io.reactivex.disposables.Disposable
import java.lang.ref.WeakReference

class BoardMainSearchActivity : AppCompatActivity(), BoardMainSearchPresenter.View, View.OnClickListener {


    private lateinit var binding: ActivityBoardMainSearchBinding
    private var weakReference: WeakReference<BoardMainSearchPresenter>? = null
    private var boardMainAdapter: BoardMainAdapter? = null
    private var layoutManager: LinearLayoutManager? = null
    private var rv: RecyclerView? = null
    private var progressBar: ProgressBar? = null
    private var isLoading: Boolean = false
    private var data: String? = null
    private var disposable: Disposable? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_main_search)
        binding.onClickListener = this
        setMVP()
        setView()
        setBaseProgressBar(binding.progressBar)
    }

    private fun setView() {
        binding.layoutUndo.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP)

        rv = binding.rvBoard
        val decor = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        decor.setDrawable(ContextCompat.getDrawable(this, R.drawable.survey_divder)!!)
        rv?.isNestedScrollingEnabled = false
        layoutManager = LinearLayoutManager(this)
        rv?.animation = null
        rv?.layoutManager = layoutManager
        rv?.addItemDecoration(decor)
    }

    private fun setMVP() {
        weakReference = WeakReference(BoardMainSearchPresenter(this))
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.img_search -> {
                data = binding.editSearch.text.toString()
                if (binding.editSearch.text.toString().isNotEmpty()) {
                    setData()
                } else {
                    Toast.makeText(this, "검색어를 입력해주세요", Toast.LENGTH_SHORT).show()
                }
            }
            R.id.layout_undo -> {
                closeKeyboard()
                finish()
            }
        }
    }

    private fun setData(){
        binding.editSearch.text.clear()
        binding.txtSearch.text = "검색어: $data"
        onRefresh()
        setProgressbarVisible()
        closeKeyboard()
        weakReference?.get()?.getData(data)
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
                                weakReference?.get()?.getScrollData(data, id)
                            }, 500)

                        }
                    }
                }
            }
        })
    }


    private fun closeKeyboard() {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }


    private fun setBaseProgressBar(progressBar: ProgressBar) {
        this.progressBar = progressBar
    }

    private fun setProgessbarGone() {
        progressBar?.visibility = View.GONE
    }

    private fun setProgressbarVisible() {
        progressBar?.visibility = View.VISIBLE
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
