package com.dev.skh.resellium.Board


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dev.skh.resellium.Base.BaseFragment
import com.dev.skh.resellium.Base.BaseRecyclerViewAdapter
import com.dev.skh.resellium.Board.Model.BoardMainModel
import com.dev.skh.resellium.R
import com.dev.skh.resellium.Util.CustomNestedScrollListener
import com.dev.skh.resellium.Util.DLog
import com.dev.skh.resellium.databinding.FragmentBoardMainBinding
import io.reactivex.disposables.Disposable


class BoardMainFragment : BaseFragment()
        , SwipeRefreshLayout.OnRefreshListener
        , View.OnClickListener
        , BaseRecyclerViewAdapter.OnItemClickListener
        , CustomNestedScrollListener.OnScrollListener {


    override fun onItemClick(view: View, position: Int) {
        val data = adapter?.getItem(position)
        setInnerIntent(data, view)
    }

    private var disposable: Disposable? = null
    private lateinit var binding: FragmentBoardMainBinding
    private var layoutManager: LinearLayoutManager? = null
    private var adapter: BoardMainAdapter? = null

    private var rv: RecyclerView? = null

    private var data = ""
    private var isSpinner = false
    private var isScroll = false
    private var isLoading = false
    private var viewModel: BoardMainViewModel? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_board_main, container, false)
        binding.layoutAppbar?.title = "추천"
        binding.onClickListener = this
        binding.fragment = this
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
        viewModel = ViewModelProviders.of(this).get(BoardMainViewModel::class.java)
        viewModel?.setId(null, null)
        getObservedResult(viewModel)
    }

    private fun setError() {
        setProgressbarGone()
        showErrorToast()
    }

    private fun getObservedResult(viewModel: BoardMainViewModel?) {
        viewModel?.boardListObservable?.observe(this, Observer {
            if (it != null)
                regiData(it)
        })

        viewModel?.boardErrorObservable?.observe(this, Observer {
            if (it != null) {
                DLog.e("it $it")
                setError()
            }
        })

        viewModel?.boardDisposableObservable?.observe(this, Observer {
            if (it != null)
                this.disposable = it
        })
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.img_search -> searchBoardIntent()
            R.id.img_setting -> settingIntent()
        }
    }

    private fun initAdapter(board: MutableList<BoardMainModel>) {
        adapter = BoardMainAdapter(context!!, board)
        adapter?.setOnItemClickListener(this)
        rv?.adapter = adapter
    }

    private fun regiData(board: MutableList<BoardMainModel>) {
        if (adapter == null)
            initAdapter(board)
        else
            adapter?.addItems(board)
        setProgressbarGone()
        isLoading = false
        if (!isScroll)
            setRecyclerViewScrollbar()
    }


    private fun setRecyclerViewScrollbar() {
        binding.nestedScroll.setOnScrollChangeListener(CustomNestedScrollListener(layoutManager, this))
    }

    override fun onScrollEnd() {
        if (!isLoading) {
            isLoading = true
            activity!!.runOnUiThread { setProgressbarVisible() }
            setData(checkId())
        }
    }

    private fun setData(id: String?) {
        Handler().postDelayed({
            isScroll = true
            binding.nestedScroll.fling(0)
            if (isSpinner)
                viewModel?.setId(data, id)
            else
                viewModel?.setId(null, id)

        }, 500)

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

    fun callSpinnerData(view: View?) {
        when (view?.id) {
            R.id.btn_grade -> {
                this.data = "평점"
                setBtnAccent(binding.btnGrade)
                setBtnDefault(binding.btnAll)
                setBtnDefault(binding.btnName)
                setBtnDefault(binding.btnOld)
            }
            R.id.btn_name -> {
                this.data = "이름"
                setBtnAccent(binding.btnName)
                setBtnDefault(binding.btnAll)
                setBtnDefault(binding.btnGrade)
                setBtnDefault(binding.btnOld)
            }
            R.id.btn_old -> {
                this.data = "오래된 날짜"
                setBtnAccent(binding.btnOld)
                setBtnDefault(binding.btnAll)
                setBtnDefault(binding.btnName)
                setBtnDefault(binding.btnGrade)
            }
        }
        refreshWithoutData()
        isSpinner = true
        viewModel?.setId(data, null)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }

    fun refresh() {
        adapter?.clearItems()
        setViewDefault()
        rv?.removeOnScrollListener(null)
        viewModel?.setId(null, null)
        setEverythingFalse()
        data = ""
        binding.swipeLayout.isRefreshing = false
    }

    private fun setEverythingFalse() {
        isLoading = false
        isScroll = false
        isSpinner = false
    }

    override fun onRefresh() {
        refresh()
    }

    private fun setViewDefault() {
        setBtnAccent(binding.btnAll)
        setBtnDefault(binding.btnName)
        setBtnDefault(binding.btnGrade)
        setBtnDefault(binding.btnOld)
        setProgressbarVisible()
    }

    private fun refreshWithoutData() {
        adapter?.clearItems()
        setProgressbarVisible()
        rv?.removeOnScrollListener(null)
        isLoading = false
        binding.swipeLayout.isRefreshing = false
    }
}
