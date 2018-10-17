package com.dev.skh.resellium.Board


import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.support.v4.widget.NestedScrollView
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.dev.skh.resellium.Base.BaseFragment
import com.dev.skh.resellium.Board.Model.BoardMainModel
import com.dev.skh.resellium.Board.Search.BoardMainSearchActivity
import com.dev.skh.resellium.R
import com.dev.skh.resellium.Util.DLog
import com.dev.skh.resellium.databinding.FragmentBoardMainBinding
import io.reactivex.disposables.Disposable
import java.lang.ref.WeakReference


class BoardMainFragment : BaseFragment(), BoardMainPresenter.View, SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemSelectedListener, View.OnClickListener {


    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun spinner(arr: MutableList<String>) {
        activity!!.runOnUiThread {
            val spinnerAdapter = ArrayAdapter<String>(context, R.layout.item_spinner, arr)
            spinnerAdapter.setDropDownViewResource(R.layout.item_spinner)
            binding.spinner?.spinner?.adapter = spinnerAdapter
            binding.spinner?.spinner?.onItemSelectedListener = this
        }
    }

    companion object {
        fun weakRef(view: BoardMainPresenter.View): WeakReference<BoardMainPresenter> {
            return WeakReference(BoardMainPresenter(view))
        }
    }


    override fun errorData(disposable: Disposable?, message: String?) {
        this.disposable = disposable
        DLog.e("error : $message")
    }

    private var disposable: Disposable? = null
    private lateinit var binding: FragmentBoardMainBinding
    private var layoutManager: LinearLayoutManager? = null
    private var adapter: BoardMainAdapter? = null
    private var rv: RecyclerView? = null
    private var isLoading: Boolean = false
    private var data: String? = null
    private val weakReference by lazy { weakRef(this) }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_board_main, container, false)
        binding.layoutAppbar?.title = "추천"
        binding.layoutAppbar?.onClickListener = this
        setVIEW()
        setBaseProgressBar(binding.progressBar)
        return binding.root
    }

    private fun setVIEW() {

        layoutManager = LinearLayoutManager(context!!)

        rv = setGameRv(binding.rvBoard, layoutManager!!)
        binding.swipeLayout.setDistanceToTriggerSync(350)
        binding.swipeLayout.setOnRefreshListener(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        Handler().postDelayed({
            weakReference.get()?.addSpinnerData()
            weakReference.get()?.getBoardData()
        }, 100)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.img_search -> startActivity(Intent(context!!, BoardMainSearchActivity::class.java))
        }
    }

    override fun registerData(board: MutableList<BoardMainModel>?, disposable: Disposable?, isScroll: Boolean) {
        if (board != null) {
            if (adapter == null) {
                adapter = BoardMainAdapter(context!!, board)
                rv?.adapter = adapter
            } else {
                adapter?.addItems(board)
            }
        }

        setProgessbarGone()
        this.disposable = disposable
        isLoading = false
        if (!isScroll)
            setRecyclerViewScrollbar(false)
    }

    override fun registerSpinnerData(board: MutableList<BoardMainModel>?, disposable: Disposable?, isScroll: Boolean) {
        if (board != null)
            adapter?.addItems(board)
        setProgessbarGone()
        this.disposable = disposable
        isLoading = false
        if (!isScroll)
            setRecyclerViewScrollbar(true)
    }

    private fun setRecyclerViewScrollbar(isSpinner: Boolean) {
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
                            DLog.e("data = $data")
                            val id = checkId()
                            DLog.e("id = $id")
                            Handler().postDelayed({

                                if (isSpinner)
                                    weakReference.get()?.getSpinnerScrollData(data, id)
                                else
                                    weakReference.get()?.getScrollData(id)
                            }, 500)

                        }
                    }
                }
            }
        })
    }

    private fun checkId(): String? {
        var id: String? = null
        when (data) {
            "" -> id = adapter?.getItem(adapter!!.itemCount - 1)?.id
            "평점" -> id = adapter?.getItem(adapter!!.itemCount - 1)?.grade
            "이름" -> id = adapter?.getItem(adapter!!.itemCount - 1)?.title
            "최근 날짜" -> id = adapter?.getItem(adapter!!.itemCount - 1)?.date
            "오래된 날짜" -> id = adapter?.getItem(adapter!!.itemCount - 1)?.date
        }

        return id
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (parent?.id) {
            R.id.spinner -> {
                data = binding.spinner?.spinner?.selectedItem.toString()
                if (data!!.isNotEmpty()) {
                    callSpinnerData(data)
                }
            }
        }
    }

    private fun callSpinnerData(data: String?) {
        refreshWithoutData()
        weakReference.get()?.getSpinnerData(data)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }

    private fun refresh() {
        adapter?.clearItems()
        setProgressbarVisible()
        rv?.removeOnScrollListener(null)
        binding.spinner?.spinner?.setSelection(0, false)
        weakReference.get()?.getBoardData()
        isLoading = false
        data = ""
        binding.swipeLayout.isRefreshing = false
    }

    override fun onRefresh() {
        refresh()
    }

    private fun refreshWithoutData() {
        adapter?.clearItems()
        setProgressbarVisible()
        rv?.removeOnScrollListener(null)
        isLoading = false
        binding.swipeLayout.isRefreshing = false
    }
}
