package com.dev.skh.resellium.Board


import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.support.v4.widget.NestedScrollView
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dev.skh.resellium.Base.BaseFragment
import com.dev.skh.resellium.Base.BaseRecyclerViewAdapter
import com.dev.skh.resellium.Board.Model.BoardMainModel
import com.dev.skh.resellium.Board.Search.BoardMainSearchActivity
import com.dev.skh.resellium.Board.Sub.InnerBoardMainActivity
import com.dev.skh.resellium.R
import com.dev.skh.resellium.User.UserMainActivity
import com.dev.skh.resellium.Util.DLog
import com.dev.skh.resellium.databinding.FragmentBoardMainBinding
import io.reactivex.disposables.Disposable
import java.lang.ref.WeakReference


class BoardMainFragment : BaseFragment()
        , BoardMainPresenter.View
        , SwipeRefreshLayout.OnRefreshListener
        , View.OnClickListener, BaseRecyclerViewAdapter.OnItemClickListener {

    override fun onItemClick(view: View, position: Int) {
        val data = adapter?.getItem(position)
        val intent = Intent(view.context, InnerBoardMainActivity::class.java)
        intent.putExtra("data", data)
        startActivity(intent)
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
//        private var layoutManager: LinearLayoutManager? = null
    private var layoutManager: GridLayoutManager? = null
    private var adapter: BoardMainAdapter? = null
    private var rv: RecyclerView? = null
    private var isLoading: Boolean = false
    private var data: String? = ""
    private val weakReference by lazy { weakRef(this) }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_board_main, container, false)
        binding.layoutAppbar?.title = "추천"
        binding.onClickListener = this
        binding.layoutAppbar?.onClickListener = this
        setVIEW()
        setBaseProgressBar(binding.progressBar)
        return binding.root
    }

    private fun setVIEW() {
        layoutManager = GridLayoutManager(context!!, 2)
//        layoutManager = LinearLayoutManager(context!!)
        rv = setGridGameRv(binding.rvBoard, layoutManager!!)
        binding.swipeLayout.setDistanceToTriggerSync(350)
        binding.swipeLayout.setOnRefreshListener(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        weakReference.get()?.getBoardData()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.img_search -> startActivity(Intent(context!!, BoardMainSearchActivity::class.java))
            R.id.img_setting -> startActivity(Intent(context!!, UserMainActivity::class.java))
            R.id.btn_all -> refresh()
            R.id.btn_grade -> callSpinnerData("평점")
            R.id.btn_name -> callSpinnerData("이름")
            R.id.btn_old -> callSpinnerData("오래된 날짜")
        }
    }

    override fun registerData(board: MutableList<BoardMainModel>?, disposable: Disposable?, isScroll: Boolean) {
        if (board != null) {
            if (adapter == null) {
                adapter = BoardMainAdapter(context!!, board)
                adapter?.setOnItemClickListener(this)
                rv?.adapter = adapter
            } else {
                adapter?.addItems(board)
            }
        }

        setProgressbarGone()
        this.disposable = disposable
        isLoading = false
        if (!isScroll)
            setRecyclerViewScrollbar(false)
    }

    override fun registerSpinnerData(board: MutableList<BoardMainModel>?, disposable: Disposable?, isScroll: Boolean) {
        if (board != null)
            adapter?.addItems(board)
        setProgressbarGone()
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
                            val id = checkId()
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

    private fun callSpinnerData(data: String?) {
        when (data) {
            "평점" -> {
                this.data = "평점"
                setBtnAccent(binding.btnGrade)
                setBtnDefault(binding.btnAll)
                setBtnDefault(binding.btnName)
                setBtnDefault(binding.btnOld)
            }
            "이름" -> {
                this.data = "이름"
                setBtnAccent(binding.btnName)
                setBtnDefault(binding.btnAll)
                setBtnDefault(binding.btnGrade)
                setBtnDefault(binding.btnOld)
            }
            "오래된 날짜" -> {
                this.data = "오래된 날짜"
                setBtnAccent(binding.btnOld)
                setBtnDefault(binding.btnAll)
                setBtnDefault(binding.btnName)
                setBtnDefault(binding.btnGrade)
            }
        }
        refreshWithoutData()
        weakReference.get()?.getSpinnerData(data)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }

    private fun refresh() {
        adapter?.clearItems()
        setViewDefault()
        rv?.removeOnScrollListener(null)
        weakReference.get()?.getBoardData()
        isLoading = false
        data = ""
        binding.swipeLayout.isRefreshing = false
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
